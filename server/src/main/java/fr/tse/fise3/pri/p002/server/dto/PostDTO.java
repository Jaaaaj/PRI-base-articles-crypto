package fr.tse.fise3.pri.p002.server.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.tse.fise3.pri.p002.server.model.Post;

public class PostDTO {

	private String address;

	private Date date;

	private String title;

	private String url;

	private List<String> authors;
	private List<String> keywords;
	private String book_title;

	public String getBook_title() {
		return book_title;
	}

	public void setBook_title(String book_title) {
		this.book_title = book_title;
	}

	public PostDTO() {
	}

	public PostDTO(Post p) {
		this.address = p.getAddress();
		this.date = p.getDate();
		this.title = p.getTitle();
		this.url = p.getUrl();
		this.authors = new ArrayList<String>();
		p.getAuthors().forEach(author -> this.authors.add(author.getAuthorName()));
		this.keywords = new ArrayList<String>();
		p.getKeywords().forEach(key -> this.keywords.add(key.getKeywordName()));
		this.book_title = p.getBookTitle();
	}

	public PostDTO(String address, Date date, String title, String url, List<String> author, List<String> keywords,String book_title) {
		super();
		this.address = address;
		this.date = date;
		this.title = title;
		this.url = url;
		this.authors = author;
		this.keywords = keywords;
		this.book_title = book_title;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return "PostDTO{" + "address='" + address + '\'' + ", date=" + date + ", title='" + title + '\'' + ", url='"
				+ url + '\'' + ", authors=" + authors + ", keywords=" + keywords + '}';
	}
}
