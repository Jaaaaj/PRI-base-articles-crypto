package fr.tse.fise3.pri.p002.server.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the posts database table.
 */
@Entity
@Table(name = "posts")
@NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "POST_ID")
	private BigInteger postId;

	@Lob
	private String address;

	@Column(name = "BOOK_TITLE")
	@Lob
	private String bookTitle;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Lob
	private String title;

	@Lob
	private String url;

	@Column(columnDefinition = "LONGTEXT")
	private String abstract_;



	// bi-directional many-to-many association to Author
	@ManyToMany
	@JoinTable(name = "posts_authors", joinColumns = { @JoinColumn(name = "POST_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "AUTHOR_ID") })
	private List<Author> authors;

	// bi-directional many-to-many association to Category
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "posts_catergories", joinColumns = { @JoinColumn(name = "POST_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CATEGORY_ID") })
	private List<Category> categories;

	// bi-directional many-to-many association to Keyword
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "posts_keywords", joinColumns = { @JoinColumn(name = "POST_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "KEYWORD_ID") })
	private List<Keyword> keywords;

	// bi-directional many-to-one association to DataSource
	@ManyToOne
	@JoinColumn(name = "SOURCE_NAME")
	private DataSource dataSource;

	public Post() {
	}

	public Post(String bookTitle, Date date, String title, String url, String abstract_, List<Author> authors,
			DataSource dataSource) {
		super();
		this.bookTitle = bookTitle;
		this.date = date;
		this.title = title;
		this.url = url;
		this.abstract_ = abstract_;
		this.authors = authors;
		this.dataSource = dataSource;
	}

	public BigInteger getPostId() {
		return this.postId;
	}

	public void setPostId(BigInteger postId) {
		this.postId = postId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBookTitle() {
		return this.bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Author> getAuthors() {
		return this.authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Keyword> getKeywords() {
		return this.keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getAbstract() {
		return abstract_;
	}

	public void setAbstract(String abstract_) {
		this.abstract_ = abstract_;
	}

}