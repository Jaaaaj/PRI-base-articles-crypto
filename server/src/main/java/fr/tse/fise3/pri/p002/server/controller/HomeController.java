package fr.tse.fise3.pri.p002.server.controller;

import fr.tse.fise3.pri.p002.server.dto.DataSourceDTO;
import fr.tse.fise3.pri.p002.server.dto.PostDTO;
import fr.tse.fise3.pri.p002.server.model.Author;
import fr.tse.fise3.pri.p002.server.model.DataSource;
import fr.tse.fise3.pri.p002.server.model.Keyword;
import fr.tse.fise3.pri.p002.server.model.Post;
import fr.tse.fise3.pri.p002.server.repository.AuthorRepository;
import fr.tse.fise3.pri.p002.server.repository.DataSourceRepository;
import fr.tse.fise3.pri.p002.server.repository.PostRepository;
import fr.tse.fise3.pri.p002.server.service.*;
import fr.tse.fise3.pri.p002.server.thread.HalApiRequestThread;
import fr.tse.fise3.pri.p002.server.thread.SemanticApiRequestThread;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Controlleur de l'application : contient tous les endpoints de l'API.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeController {

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private PostService postService;
	@Autowired
	private HalApiService halApiService;
	@Autowired
	private SemanticApiService semanticApiService;
	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private DataSourceRepository datasetRepository;

	private Converter<Post, PostDTO> converter;

	public HomeController() {
		converter = context -> {
			PostDTO dto = new PostDTO();
			dto.setTitle(context.getSource().getTitle());
			dto.setAuthors(
					context.getSource().getAuthors().stream().map(Author::getAuthorName).collect(Collectors.toList()));
			dto.setAddress(context.getSource().getAddress());
			dto.setDate(context.getSource().getDate());
			dto.setUrl(context.getSource().getUrl());
			dto.setKeywords(context.getSource().getKeywords().stream().map(Keyword::getKeywordName)
					.collect(Collectors.toList()));
			return dto;
		};
		modelMapper.createTypeMap(Post.class, PostDTO.class).setConverter(converter);
	}

	/**
	 * Permet de recuperer tous les articles de la base de donnees dans un objet
	 * Page.
	 * 
	 * @param page Le nombre de pages attendues
	 * @param size Le nombre d'articles par page
	 * @return Un objet page conteant les page*size premiers articles de la database
	 */
	@GetMapping("/posts")
	public Page<PostDTO> findAllPosts(@RequestParam int page, @RequestParam int size) {
		Pageable pageable = PageRequest.of(page, size);
		return postService.findAllPosts(pageable).map(post -> modelMapper.map(post, PostDTO.class));
	}

	/**
	 * Permet de rechercher des articles selon un champ et les retourne au format
	 * Page.
	 * 
	 * @param tag   Le champ sur lequel on veut faire la recherche (titre, auteur ou
	 *              keywords)
	 * @param value La String contenant le texte a recherher
	 * @param page  Le nombre de pages attendus (pour l'attribut Page)
	 * @param size  Le nombre d'articles par page (pour l'attribut Page)
	 * @return Un objet Page contenant page*size articles retournes par la recherche
	 */
	@GetMapping("/posts/search")
	public Page<PostDTO> findPostsByTileLike(@RequestParam String tag, @RequestParam String value,
			@RequestParam int page, @RequestParam int size) {
		Pageable pageable = PageRequest.of(page, size);
		switch (tag) {
		case "title":
			return postService.findPostByTitleLike(value, pageable).map(post -> modelMapper.map(post, PostDTO.class));

		case "author":
			return postService.findByAuthors_authorNameContaining(value, pageable)
					.map(post -> modelMapper.map(post, PostDTO.class));
		case "keywords":
			return postService.findByKeywords_keywordNameContaining(value, pageable)
					.map(post -> modelMapper.map(post, PostDTO.class));
		default:
			return new PageImpl<>(Collections.emptyList());
		}
	}

	/**
	 * Permet de recuperer des informations sur la source Hal Inria (nombre
	 * d'articles trouves & statut de la recherche (en train de faire une recherche
	 * ou inactif))
	 * 
	 * @return Le statut de Hal Inria
	 */
	@GetMapping("/hal/info")
	public DataSourceDTO getHalSourceInfo() {
		DataSource halDataSource = dataSourceService.getHalDataSource();
		return new DataSourceDTO(halDataSource, HalApiRequestThread.isRunning());
	}

	/**
	 * Permet de recuperer des informations sur la source Semantic Scholar (nombre
	 * d'articles trouves & statut de la recherche (en train de faire une recherche
	 * ou inactif))
	 * 
	 * @return Le statut de Semantic Scholar
	 */
	@GetMapping("/semantic/info")
	public DataSourceDTO getSemanticSourceInfo() {
		DataSource semanticDataSource = dataSourceService.getSemanticDataSource();
		return new DataSourceDTO(semanticDataSource, SemanticApiRequestThread.isRunning());
	}

	/**
	 * Lance l'execution des threads charges de faire des requetes pour recuperer de
	 * nouveaux articles.
	 * 
	 * @return Le statut de la recherche
	 */
	@GetMapping("/start")
	public String start() {

		if (!HalApiRequestThread.isRunning()) {
			this.semanticApiService.start();
			halApiService.start();
			return "Start";
		} else {
			return "is already running";
		}
	}

	/**
	 * Permet de recuperer une liste de tous les articles dans notre base de
	 * donnees.
	 * 
	 * @return Une liste de tous les articles present en local
	 */
	@GetMapping("/allposts")
	public List<PostDTO> getAllPosts() {
		ArrayList<PostDTO> response = new ArrayList<PostDTO>();

		this.postRepository.findAll().forEach(post -> response.add(new PostDTO(post)));

		return response;
	}

	/**
	 * Permet de vider completement la base de donnees.
	 */
	@DeleteMapping("/delete")
	public String delete() {
		authorRepository.deleteAll();
		postRepository.deleteAll();
		datasetRepository.deleteAll();

		if (!dataSourceService.dataSourceExistsByName(DataSourceService.SOURCE_HAL)) {
			// Add HAL API to resource entry
			DataSource halDataSource = new DataSource();
			halDataSource.setName(DataSourceService.SOURCE_HAL);
			// halDataSource.setCurrentOffset(0);
			halDataSource.setTotal(0);
			dataSourceService.saveDataSource(halDataSource);
		}

		if (!dataSourceService.dataSourceExistsByName(DataSourceService.SOURCE_SEMANTIC_SCHOLAR)) {
			// Add E print to resource entry
			DataSource semanticDataSource = new DataSource();
			semanticDataSource.setName(DataSourceService.SOURCE_SEMANTIC_SCHOLAR);
			// ePrintDataSource.setCurrentOffset(0);
			semanticDataSource.setTotal(0);
			dataSourceService.saveDataSource(semanticDataSource);
		}
		return "deleted";
	}
}
