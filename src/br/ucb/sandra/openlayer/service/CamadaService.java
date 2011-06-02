package br.ucb.sandra.openlayer.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ucb.sandra.openlayer.DAO.MongoDBFactory;
import br.ucb.sandra.openlayer.negocio.Camada;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
@Transactional
public class CamadaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7575682479110405755L;

	public CamadaService() {

		init();
	}

	Logger logger = Logger.getLogger("service");

	public String salvar(Camada camada) {

		try {
			logger.debug("Salvando uma Camada");

			DBCollection coll = MongoDBFactory.getCollection("mapserver",
					"camada");
			BasicDBObject doc = new BasicDBObject();

			doc.put("id", UUID.randomUUID().toString());
			doc.put("titulo", camada.getTitulo());
			doc.put("endereco", camada.getEndereco());
			doc.put("layer", camada.getLayer());

			coll.insert(doc);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "cadastradoSucesso";
	}

	public String delete(String id) {
		logger.debug("Apagando uma Camada");

		try {
			BasicDBObject item = (BasicDBObject) getDBObject(id);
			DBCollection coll = MongoDBFactory.getCollection("mapserver",
					"camada");
			coll.remove(item);

		} catch (Exception e) {
			logger.error(
					"Ocorreu um erro ao apagar uma camada", e);

		}
		return "removido";
	}

	public List<Camada> getAll() {
		logger.debug("Recebendo todas camadas");

		DBCollection coll = MongoDBFactory.getCollection("mapserver", "camada");
		DBCursor cur = coll.find();

		List<Camada> items = new ArrayList<Camada>();

		while (cur.hasNext()) {
			DBObject dbObject = cur.next();
			Camada camada = new Camada();

			camada.setId(dbObject.get("id").toString());
			camada.setTitulo(dbObject.get("titulo").toString());
			camada.setEndereco(dbObject.get("endereco").toString());
			camada.setLayer((dbObject.get("layer").toString()));

			items.add(camada);
		}

		return items;
	}
    
	/**
	 * Usado para popular o banco.
	 */
	private void init() {

		logger.debug("Populando banco");

		MongoDBFactory.getCollection("mapserver", "camada").drop();
		DBCollection coll = MongoDBFactory.getCollection("mapserver", "camada");

		BasicDBObject doc = new BasicDBObject();
		doc.put("id", UUID.randomUUID().toString());
		doc.put("titulo", "Teste");
		doc.put("endereco",
				"http://mapas.mma.gov.br/cgi-bin/mapserv?map=/opt/www/html/webservices/ucs.map&");
		doc.put("layer", "ucsef");
		coll.insert(doc);
	}

	private DBObject getDBObject(String id) {
		logger.debug("Pegando um objeto Mongo");

		DBCollection coll = MongoDBFactory
				.getCollection("mapserver", "camada");
		DBObject doc = new BasicDBObject();
		doc.put("id", id);

		return coll.findOne(doc);
	}

}
