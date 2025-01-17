package fr.tse.fise3.pri.p002.server.pojo.SemanticAPI;

/**
 * Objet contenant les informations d'un auteur telles que renvoyees par l'API
 * de Semantic Scholar.
 */
public class SemanticAuthor {
	private Long authorId;
	private String name;

	public SemanticAuthor() {
		super();
	}

	public SemanticAuthor(Long authorId, String name) {
		super();
		this.authorId = authorId;
		this.name = name;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
