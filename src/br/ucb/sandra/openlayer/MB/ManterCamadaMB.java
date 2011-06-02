package br.ucb.sandra.openlayer.MB;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import br.ucb.sandra.openlayer.negocio.Camada;
import br.ucb.sandra.openlayer.service.CamadaService;

@Component
public class ManterCamadaMB implements Serializable {

	private static final long serialVersionUID = -8783953503570187859L;
	private Camada camada = new Camada();
	private List<Camada> camadas;
	private String id;

	public ManterCamadaMB() {
		if (this.camada == null) {
			this.camada = new Camada();
		}
	}

	@PostConstruct
	public void inicializar() {
		listarTodos();

	}

	@Resource
	private CamadaService camadaService;

	public String save() throws UnknownHostException {
		camadaService.salvar(this.camada);
		listarTodos();
		return "success";
	}

	public void listarTodos() {
		this.camadas = camadaService.getAll();

	}
	
	public String deletar(){
		camadaService.delete(this.id);
		return "apagado";
		
		
	}

	 public int getTamanho() {
	 return camadas.size();
	 }

	public void setCamada(Camada camada) {
		this.camada = camada;
	}

	public Camada getCamada() {
		return camada;
	}

	public void setCamadas(List<Camada> camadas) {
		this.camadas = camadas;
	}

	public List<Camada> getCamadas() {
		return camadas;

	}

	public CamadaService getCamadaService() {
		return camadaService;
	}

	public void setCamadaService(CamadaService camadaService) {
		this.camadaService = camadaService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}
