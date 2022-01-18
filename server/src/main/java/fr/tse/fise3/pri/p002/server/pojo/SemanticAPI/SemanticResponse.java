package fr.tse.fise3.pri.p002.server.pojo.SemanticAPI;

import java.util.List;

public class SemanticResponse {

	private Long total;
	private Long offset;
	private Long next;
	private List<SemanticDocs> data;

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
