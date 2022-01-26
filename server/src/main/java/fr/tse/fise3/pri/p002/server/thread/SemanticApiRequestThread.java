package fr.tse.fise3.pri.p002.server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tse.fise3.pri.p002.server.model.Author;
import fr.tse.fise3.pri.p002.server.model.DataSource;
import fr.tse.fise3.pri.p002.server.model.Post;
import fr.tse.fise3.pri.p002.server.pojo.SemanticAPI.SemanticDocs;
import fr.tse.fise3.pri.p002.server.pojo.SemanticAPI.SemanticResponse;
import fr.tse.fise3.pri.p002.server.service.AuthorService;
import fr.tse.fise3.pri.p002.server.service.DataSourceService;
import fr.tse.fise3.pri.p002.server.service.PostService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Thread qui s'occupe des requetes et de la recuperation des donnees en
 * provenance de l'API de Semantic Scholar.
 */
@Component
public class SemanticApiRequestThread implements Runnable {

	@Autowired
	private PostService postService;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private DataSourceService dataSourceService;

	private final OkHttpClient okHttpClient;
	private Long nbResults = (long) 200;
	private Long offset = (long) 0;
	private final Long limit = (long) 100; // Nombre de resultats par requete (max. 100)
	private static Boolean running = false;

	public SemanticApiRequestThread() {
		this.okHttpClient = new OkHttpClient();
	}

	/**
	 * Envoie une requete a l'API de Semantic Scholar
	 * 
	 * @param query La requete
	 * @return Le resultat de la recherche (String au format Json)
	 * @throws IOException
	 */
	private String doRequest(String query) throws IOException {
		Request req = new Request.Builder().url(query).build();

		try (Response res = okHttpClient.newCall(req).execute()) {
			return res.body().string();
		}
	}

	/**
	 * Sauvegarde un article au format retourne par l'API de Semantic Scholar dans
	 * notre base de donnees. Pour cela, on doit le convertir en Post (notre format
	 * pour stocker les articles dans la database).
	 * 
	 * @param doc Une publication venant de Semantic Scholar
	 * @return Un int : 0 si l'enregistrement est réussi ou 1 si l'article est deja
	 *         present dans la database (cela nous permet de compter combien
	 *         d'articles sont reelement inseres dans la database)
	 */
	private Integer saveArticle(SemanticDocs doc) {

		if (this.postService.findPostByTitleLike(doc.getTitle(), PageRequest.of(1, 10)).getTotalElements() > 0) {
			System.out.println("POST_ALREADY_EXISTS_BY_TITLE");
			return 1;
		}

		Post post = new Post();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, doc.getYear());

		post.setBookTitle(doc.getTitle());
		post.setDate(cal.getTime());
		post.setTitle(doc.getTitle());
		post.setUrl(doc.getUrl());
		post.setAbstract(doc.getAbstract());

		List<Author> authors = new ArrayList<Author>();
		doc.getAuthors().forEach(auth -> authors.add(this.authorService.findOrCreateByName(auth.getName())));

		post.setAuthors(authors);
		post.setDataSource(this.dataSourceService.findByName(DataSourceService.SOURCE_SEMANTIC_SCHOLAR)
				.orElseThrow(() -> new ResourceNotFoundException("Semantic Scholar source doesn't exist")));

		this.postService.savePost(post);

		return 0;
	}

	/**
	 * Permet de mettre a jour les informations sur le statut des recherches sur
	 * l'API de Semantic Scholar.
	 * 
	 * @param total Le nombre total de publications uniques trouvees via cette API
	 */
	private void updateSemanticDataSource(long total) {
		DataSource semanticDataSource = dataSourceService.getSemanticDataSource();
		semanticDataSource.setTotal(total);
		dataSourceService.saveDataSource(semanticDataSource);

	}

	/**
	 * Lit une liste de mots cles pour les recherches contenus dans un fichier.
	 * 
	 * @param inputStream Flux de lecture (qui pointe vers un fichier, ici un txt)
	 * @return Un String qui contient le contenu du fichier (des mots cles) separes
	 *         par une virgule et un saut de ligne (\n)
	 * @throws IOException
	 */
	private String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	public static Boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		SemanticApiRequestThread.running = true;

		try {
			Long nouveauxArticles = (long) 0;

			String[] keyword_list = new String[0];
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("keywords.txt");
			try {
				String keywords = this.readFromInputStream(inputStream);
				keyword_list = keywords.replaceAll(",", "").split("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String keyword : keyword_list) {
				do {
					String response = this.doRequest(
							"https://api.semanticscholar.org/graph/v1/paper/search?query="+keyword+"&offset="
									+ this.offset + "&limit=" + this.limit
									+ "&fields=title,authors,year,url,abstract,venue");

					ObjectMapper objectMapper = new ObjectMapper();
					SemanticResponse semanticResponse = objectMapper.readValue(response, SemanticResponse.class);
					List<SemanticDocs> articles = semanticResponse.getData();

					for (SemanticDocs a : articles) {
						nouveauxArticles -= this.saveArticle(a);
					}

					nouveauxArticles += this.limit;
					this.offset += this.limit;

				} while (this.offset < this.nbResults);

				System.out.println("SEMANTIC - " + keyword);
				System.out.println("NEW ARTICLES FOUND --> " + (nouveauxArticles));
			}
			this.updateSemanticDataSource(this.dataSourceService.getSemanticDataSource().getTotal() + nouveauxArticles);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			SemanticApiRequestThread.running = false;
		}

	}

}
