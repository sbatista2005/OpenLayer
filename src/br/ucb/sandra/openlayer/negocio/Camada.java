package br.ucb.sandra.openlayer.negocio;

import java.io.Serializable;

public class Camada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 322729666327730116L;

	private String id;
	private String titulo;
	private String endereco;
	private String layer;

	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}



}
