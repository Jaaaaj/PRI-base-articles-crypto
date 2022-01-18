package fr.tse.fise3.pri.p002.server.pojo.SemanticAPI;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SemanticDocs {

	private String paperId;
	private String url;
	private String title;
	private String abstract_;
	private String venue;
	private Integer year;
	private List<SemanticAuthor> authors;

	public SemanticDocs() {

	}

	public SemanticDocs(String url, String title, String abstract_, String venue, Integer year,
			List<SemanticAuthor> authors) {
		super();
		this.url = url;
		this.title = title;
		this.abstract_ = abstract_;
		this.venue = venue;
		this.year = year;
		this.authors = authors;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("abstract")
	public String getAbstract() {
		return abstract_;
	}

	@JsonProperty("abstract")
	public void setAbstract(String abstract_) {
		this.abstract_ = abstract_;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<SemanticAuthor> getAuthors() {
		return authors;
	}

	public void setAuthors(List<SemanticAuthor> authors) {
		this.authors = authors;
	}

}
