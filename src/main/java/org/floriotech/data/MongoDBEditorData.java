package org.floriotech.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

public class MongoDBEditorData {

    private static MongoDBEditorData instance;

    private List<Connexion> connexionList;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;

    private MongoDBEditorData() {
        connexionList = new ArrayList<Connexion>();
        database = null;
        mongoClient = null;
    }



    /**
     * Ajoute une nouvelle connexion à la liste de connexions existantes
     * @param connexion La connexion à ajouter à la liste des connexions existantes
     */
    public void addNewConnexion(Connexion connexion){
        connexionList.add(connexion);
    }

    /**
     * Vérifie si la connexion est déjà présente dans la liste des connexions existantes
     * @param connexion La connexion sujet à vérification
     * @return Vrai si la connexion est déjà présente dans la liste des connexions existantes, Faux sinon.
     */
    public boolean isConnexionInList(Connexion connexion){
        for(Connexion cnx : connexionList){
            if(cnx.equals(connexion)){
                return true;
            }
        }
        return false;
    }



    /**
     * Recherche (ou créer s'il n'existe pas) l'unique instance de classe.
     * @return L'unique instance de class.
     */
    public static MongoDBEditorData getInstance(){
        if(instance == null){
            instance = new MongoDBEditorData();
        }
        return instance;
    }



    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public List<Connexion> getConnexionList() {
        return connexionList;
    }
    public MongoDatabase getDatabase() {
        return database;
    }
    public MongoCollection getCollection() {
        return collection;
    }

    public void setMongoClient(String connexionString) {
        MongoClientURI mongoClientURI = new MongoClientURI(connexionString);
        if(mongoClient != null){
            mongoClient.close();
            mongoClient = null;
        }
        mongoClient = new MongoClient(mongoClientURI);
        database = null;
    }
    public void unsetMongoClient(){
        if(mongoClient != null){
            mongoClient.close();
            mongoClient = null;
        }
        unsetDatabase();
    }

    public void setDatabase(String dbName) {
        database = mongoClient.getDatabase(dbName);
    }
    public void unsetDatabase(){
        database = null;
        unsetCollection();
    }

    public void setCollection(String collName) {
        collection = database.getCollection(collName);
    }
    public void unsetCollection(){
        collection = null;
    };
}
