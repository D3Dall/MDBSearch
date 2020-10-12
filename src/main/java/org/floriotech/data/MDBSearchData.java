package org.floriotech.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MDBSearchData {

    /* ---------------------------------------- VARIABLE ---------------------------------------- */
        private static MDBSearchData instance;

        private List<Connexion> connexionList;


        private MongoClient mongoClient;
        private MongoDatabase database;
        private MongoCollection collection;
    /* ---------------------------------------- VARIABLE ---------------------------------------- */



    /* ---------------------------------------- METHODS ---------------------------------------- */
    /* ------------ Create instance ------------- */
        private MDBSearchData() throws IOException, ClassNotFoundException {
            connexionList = new ArrayList<Connexion>();
            database = null;
            mongoClient = null;
            LoadCnxData();
        }
        /**
         * Recherche (ou créer s'il n'existe pas) l'unique instance de classe.
         * @return L'unique instance de class.
         */
        public static MDBSearchData getInstance() throws IOException, ClassNotFoundException {
            if(instance == null){
                instance = new MDBSearchData();
            }
            return instance;
        }
    /* ------------ Create instance ------------- */

    /* ------------ ConnexionList ------------- */
        /**
         * Ajoute une nouvelle connexion à la liste de connexions existantes
         * @param connexion La connexion à ajouter à la liste des connexions existantes
         */
        public void addNewConnexion(Connexion connexion) throws IOException {
            connexionList.add(connexion);
            SaveCnxData();
        }
        /**
         * Vérifie si la connexion est déjà présente dans la liste des connexions existantes
         * @param connexion La connexion sujet à vérification
         * @return Vrai si la connexion est déjà présente dans la liste des connexions existantes, Faux sinon.
         */
        public boolean isConnexionInList(Connexion connexion){
            for(Connexion cnx : connexionList){
                if(cnx.getName().equals(connexion.getName())){
                    return true;
                }
            }
            return false;
        }
    /* ------------ ConnexionList ------------- */


    /* ------------ Save and Load ------------- */
        /**
         * Sauvegarde les données de connexion dans un fichier à la racine du logiciel
         * @throws IOException
         */
        private void SaveCnxData() throws IOException {
            String PATH = "/";
            String directoryName = "dataFile";
            String fileName = "connexionsList.ser";

            File directory = new File(directoryName);
            File file = new File(directoryName + "/" + fileName);
            if (! directory.exists()){
                directory.mkdir();
                file.createNewFile();
            }else{
                FileOutputStream fileOut = new FileOutputStream(file.getAbsoluteFile());
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(connexionList);
                out.close();
                fileOut.close();
            }





        }

        /**
         * Charge les données de connexion qui se trouve dans un fichier à la racine du logiciel
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void LoadCnxData() throws IOException, ClassNotFoundException, EOFException{
            String PATH = "/";
            String directoryName = "dataFile";
            String fileName = "connexionsList.ser";

            File directory = new File(directoryName);
            File file = new File(directoryName + "/" + fileName);
            if (! directory.exists()){
                directory.mkdir();
                file.createNewFile();
            }else{
                FileInputStream fileIn = new FileInputStream(file.getAbsoluteFile());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                connexionList = (List<Connexion>) in.readObject();
                in.close();
                fileIn.close();
            }
        }
    /* ------------ Save and Load ------------- */


    /* DEBUT GETTERS */
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
    /* FIN GETTERS */


    /* DEBUT SETTERS */
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
    /* FIN SETTERS */
    /* ---------------------------------------- METHODS ---------------------------------------- */
}
