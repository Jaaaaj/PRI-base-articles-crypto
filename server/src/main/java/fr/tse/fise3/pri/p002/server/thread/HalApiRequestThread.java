package fr.tse.fise3.pri.p002.server.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tse.fise3.pri.p002.server.model.Author;
import fr.tse.fise3.pri.p002.server.model.DataSource;
import fr.tse.fise3.pri.p002.server.model.Keyword;
import fr.tse.fise3.pri.p002.server.model.Post;
import fr.tse.fise3.pri.p002.server.pojo.HalApi.HalApiDoc;
import fr.tse.fise3.pri.p002.server.pojo.HalApi.HalApiResponse;
import fr.tse.fise3.pri.p002.server.service.AuthorService;
import fr.tse.fise3.pri.p002.server.service.DataSourceService;
import fr.tse.fise3.pri.p002.server.service.KeywordService;
import fr.tse.fise3.pri.p002.server.service.PostService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

@Component
@Scope("prototype")
public class HalApiRequestThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HalApiRequestThread.class);
    private static boolean running = false;
    private final OkHttpClient okHttpClient;
    private Integer lastRequestYear;
    long rows = 100;        // Nombre de résultats retournes par un appel a l'API

    @Autowired
    private PostService postService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private DataSourceService dataSourceService;

    public HalApiRequestThread() {
        this.okHttpClient = new OkHttpClient();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        HalApiRequestThread.running = running;
    }

    private String doRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public void run() {
        running = true;

        // Innit
        DataSource halDataSource = dataSourceService.getHalDataSource();

        if (halDataSource.getCreateDate().equals(halDataSource.getModifyDate())) {
            // Si le programme n'a jamais tourne
            this.lastRequestYear = 0;
        } else {
            // Sinon on recupere la derniere date d'utilisation
            Calendar cal = Calendar.getInstance();
            cal.setTime(halDataSource.getModifyDate());
            this.lastRequestYear = cal.get(Calendar.YEAR);
        }

        System.out.println(this.lastRequestYear);

        String[] keyword_list = new String[0];
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("keywords.txt");
        try {
            String keywords = readFromInputStream(inputStream);
            keyword_list = keywords.replaceAll(",", "").split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String keyword : keyword_list) {
            try {

                Long nbResponses = (long) 0;
                Long totalResponses = (long) 0;
                Long existingPosts = (long) 0;

                do {

                    System.out.println(keyword);
                    String response = this.doRequest(
                            "https://api.archives-ouvertes.fr/search?q="+keyword+"~2&fl=uri_s,label_s,title_s,authEmail_s,abstract_s,keyword_s,authAlphaLastNameFirstNameIdHal_fs,submittedDate_tdate&fq=submittedDateY_i:["
                                    + this.lastRequestYear + " TO *]&rows=" + rows + "&start=" + nbResponses);

                    ObjectMapper objectMapper = new ObjectMapper();
                    HalApiResponse halApiResponse = objectMapper.readValue(response, HalApiResponse.class);

                    System.out.println(halApiResponse.getResponse().getDocs().size());

                    totalResponses = halApiResponse.getResponse().getNumFound();

                    for (HalApiDoc halApiDoc : halApiResponse.getResponse().getDocs()) {
                        existingPosts += saveHalApiDoc(halApiDoc);
                    }

                    nbResponses += rows;

                    // start = halApiResponse.getResponse().getStart() + rows;

                } while (totalResponses > nbResponses);

                System.out.println("NEW ARTICLES FOUND --> " + (totalResponses - existingPosts));
                halDataSource = dataSourceService.getHalDataSource();
                System.out.println(halDataSource.getTotal() + totalResponses - existingPosts);
                this.updateHalDataSource(halDataSource.getTotal() + totalResponses - existingPosts);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running = false;
            }
        }
    }

    private Integer saveHalApiDoc(HalApiDoc halApiDoc) {
        Post post = new Post();

        if (postService.postExistsByUrl(halApiDoc.getUri_s())) {
            System.out.println("POST_ALREADY_EXISTS_BY_URL");
            return 1;
        }

        post.setTitle(halApiDoc.getTitle_s().get(0));
        post.setDate(halApiDoc.getSubmittedDate_tdate());
        post.setUrl(halApiDoc.getUri_s());

        if (halApiDoc.getAuthAlphaLastNameFirstNameIdHal_fs() != null) {
            Map<BigInteger, Author> authorsMap = new HashMap<>();
            for (String authorString : halApiDoc.getAuthAlphaLastNameFirstNameIdHal_fs()) {
                authorString = StringUtils.substringAfter(authorString, "AlphaSep_");
                authorString = StringUtils.substringBefore(authorString, "_FacetSep");
                Author author = authorService.findOrCreateByName(authorString);
                authorsMap.put(author.getAuthorId(), author);
            }
            post.setAuthors(new ArrayList<>(authorsMap.values()));
        }

        if (halApiDoc.getKeyword_s() != null) {
            Map<BigInteger, Keyword> keywordsMap = new HashMap<>();
            for (String keywordString : halApiDoc.getKeyword_s()) {
                Keyword keyword = keywordService.findOrCreateByName(keywordString);
                keywordsMap.put(keyword.getKeywordId(), keyword);
            }
            post.setKeywords(new ArrayList<>(keywordsMap.values()));
        }

        if (halApiDoc.getAbstract_s() != null) {
			post.setAbstract(halApiDoc.getAbstract_s().get(0));
		}
        
        post.setDataSource(dataSourceService.findByName(DataSourceService.SOURCE_HAL)
                .orElseThrow(() -> new ResourceNotFoundException("Hal source doesn't exist")));
        try {
            postService.savePost(post);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void updateHalDataSource(long total) {
        DataSource halDataSource = dataSourceService.getHalDataSource();
        halDataSource.setTotal(total);
        // halDataSource.setCurrentOffset(offset);
        dataSourceService.saveDataSource(halDataSource);

    }

}
