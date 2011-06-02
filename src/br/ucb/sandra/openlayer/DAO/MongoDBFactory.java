package br.ucb.sandra.openlayer.DAO;



import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBFactory {

protected static Logger logger = Logger.getLogger("mongo");
	
	private static Mongo m;
	
	// Make sure no one can instantiate our factory
	private MongoDBFactory() {}
	
	// Return an instance of Mongo
	public static Mongo getMongo() {
		logger.debug("Retrieving MongoDB");
		if (m == null) {
			try {
				m = new Mongo( "localhost" , 27017 );
			} catch (UnknownHostException e) {
				logger.error(e);
			} catch (MongoException e) {
				logger.error(e);
			}
		}
		
		return m;
	}
	
	// Retrieve a db
	public static DB getDB(String dbname) {
		logger.debug("Retrieving db: " + dbname);
		return getMongo().getDB(dbname);
	}
	
	// Retrieve a collection
	public static DBCollection getCollection(String dbname, String collection) {
		logger.debug("Retrieving collection: " + collection);
		return getDB(dbname).getCollection(collection);
	}
	
}
