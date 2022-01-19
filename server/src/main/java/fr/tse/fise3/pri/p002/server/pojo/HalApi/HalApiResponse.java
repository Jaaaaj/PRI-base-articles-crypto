package fr.tse.fise3.pri.p002.server.pojo.HalApi;

/**
 * Objet qui pemet de recuperer la trame complete de reponse renvoyee par l'API
 * de Hal Inria.
 */
public class HalApiResponse {

	private HalApiResponseEntity response;

	public HalApiResponse() {
	}

	public HalApiResponse(HalApiResponseEntity response) {
		this.response = response;
	}

	public HalApiResponseEntity getResponse() {
		return response;
	}

	public void setResponse(HalApiResponseEntity response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "HalApiResponse{" + "response=" + response + '}';
	}
}
