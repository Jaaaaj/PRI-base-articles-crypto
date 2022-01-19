package fr.tse.fise3.pri.p002.server.pojo.SemanticAPI;

import java.util.List;

/**
 * Objet qui correspond au format d'une reponse complete venant de l'API de
 * Semantic Scholar.
 */
public class SemanticResponse {

	private Long total; // Nombre total d'articles qui correspondent a la recherche
	private Long offset; // Indice du depart de notre recherche
	private Long next; // Nombre d'articles retournes
	private List<SemanticDocs> data; // Liste de articles

	public SemanticResponse() {

	}

	public SemanticResponse(Long total, Long offset, Long next, List<SemanticDocs> data) {
		super();
		this.total = total;
		this.offset = offset;
		this.next = next;
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getNext() {
		return next;
	}

	public void setNext(Long next) {
		this.next = next;
	}

	public List<SemanticDocs> getData() {
		return data;
	}

	public void setData(List<SemanticDocs> data) {
		this.data = data;
	}

}
