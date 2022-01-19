package fr.tse.fise3.pri.p002.server.pojo.HalApi;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Objet qui correspond a 1 article au format d'une response a une requete a
 * l'API de Hal Inria.
 */
public class HalApiDoc {

	private String label_s; // Informations generales
	private List<String> title_s; // Titre
	private List<String> abstract_s; // Resume
	private List<String> authAlphaLastNameFirstNameIdHal_fs; // Auteurs
	private String uri_s; // Url vers la publication
	private String journalTitle_s; // Journal source de la publication
	private String bookTitle_s; // Livre source de la publication
	private String conferenceTitle_s; // Conference source de la publication
	private List<String> keyword_s; // Mots cles
	private Date submittedDate_tdate; // Date de parution

	public HalApiDoc() {
	}

	public HalApiDoc(String label_s, List<String> title_s, List<String> abstract_s,
			List<String> authAlphaLastNameFirstNameIdHal_fs, String uri_s, List<String> keyword_s,
			Date submittedDate_tdate) {
		this.label_s = label_s;
		this.title_s = title_s;
		this.abstract_s = abstract_s;
		this.authAlphaLastNameFirstNameIdHal_fs = authAlphaLastNameFirstNameIdHal_fs;
		this.uri_s = uri_s;
		this.keyword_s = keyword_s;
		this.submittedDate_tdate = submittedDate_tdate;
	}

	public List<String> getKeyword_s() {
		return keyword_s;
	}

	public void setKeyword_s(List<String> keyword_s) {
		this.keyword_s = keyword_s;
	}

	public String getLabel_s() {
		return label_s;
	}

	public void setLabel_s(String label_s) {
		this.label_s = label_s;
	}

	public List<String> getTitle_s() {
		return title_s;
	}

	public void setTitle_s(List<String> title_s) {
		this.title_s = title_s;
	}

	public List<String> getAbstract_s() {
		return abstract_s;
	}

	public void setAbstract_s(List<String> abstract_s) {
		this.abstract_s = abstract_s;
	}

	public List<String> getAuthAlphaLastNameFirstNameIdHal_fs() {
		return authAlphaLastNameFirstNameIdHal_fs;
	}

	public void setAuthAlphaLastNameFirstNameIdHal_fs(List<String> authAlphaLastNameFirstNameIdHal_fs) {
		this.authAlphaLastNameFirstNameIdHal_fs = authAlphaLastNameFirstNameIdHal_fs;
	}

	public String getUri_s() {
		return uri_s;
	}

	public void setUri_s(String uri_s) {
		this.uri_s = uri_s;
	}

	public Date getSubmittedDate_tdate() {
		return submittedDate_tdate;
	}

	public void setSubmittedDate_tdate(String submittedDate_tdate) {
		this.submittedDate_tdate = Date.from(Instant.parse(submittedDate_tdate));
	}

	public void setSubmittedDate_tdate(Date submittedDate_tdate) {
		this.submittedDate_tdate = submittedDate_tdate;
	}

	public String getJournalTitle_s() {
		return journalTitle_s;
	}

	public void setJournalTitle_s(String journalTitle_s) {
		this.journalTitle_s = journalTitle_s;
	}

	public String getBookTitle_s() {
		return bookTitle_s;
	}

	public void setBookTitle_s(String bookTitle_s) {
		this.bookTitle_s = bookTitle_s;
	}

	public String getConferenceTitle_s() {
		return conferenceTitle_s;
	}

	public void setConferenceTitle_s(String conferenceTitle_s) {
		this.conferenceTitle_s = conferenceTitle_s;
	}

	@Override
	public String toString() {
		return "HalApiDoc{" + "label_s='" + label_s + '\'' + ", title_s=" + title_s + ", abstract_s=" + abstract_s
				+ ", authAlphaLastNameFirstNameIdHal_fs=" + authAlphaLastNameFirstNameIdHal_fs + ", uri_s='" + uri_s
				+ '\'' + ", keyword_s=" + keyword_s + ", submittedDate_tdate=" + submittedDate_tdate + '}';
	}
}
