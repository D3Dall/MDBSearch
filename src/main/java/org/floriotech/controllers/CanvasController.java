package org.floriotech.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.floriotech.data.Connexion;
import org.floriotech.data.MDBSearchData;
import org.floriotech.data.Message;
import org.floriotech.data.MessageType;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CanvasController extends Controller{

    /* ---------------------------------------- VARIABLE ---------------------------------------- */
        /* ------------ Connexion ------------- */
            @FXML
            private JFXComboBox connexionsList;

            @FXML
            private JFXComboBox dataBasesList;

            @FXML
            private JFXComboBox collectionList;
        /* ------------ Connexion ------------- */


        /* ------------ Params ------------- */
        @FXML
        private JFXTextField keyword;

        @FXML
        private JFXTextField value;
        /* ------------ Params ------------- */


        /* ------------ Result ------------- */
        @FXML
        private JFXTextArea result;

        private MDBSearchData instance;
        /* ------------ Result ------------- */
    /* ---------------------------------------- VARIABLE ---------------------------------------- */




    /* ---------------------------------------- METHODS ---------------------------------------- */

    /* ------------ Override methods ------------- */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                instance = MDBSearchData.getInstance();
            } catch (IOException | ClassNotFoundException e) {
                openMessageWindow(new Message("Impossible de charger les données : " + e.getMessage(), MessageType.Error));
            }
        }
        @Override
        public Stage getStage() {
            return (Stage) result.getScene().getWindow();
        }
    /* ------------ Override methods  ------------- */


    /* ------------ Connexion methods  ------------- */
        /**
         *  IHM : Met à jours le choix des connexions possible.
         */
        public void updateListOfConnexions(){
            connexionsList.getItems().clear();
            for(Connexion cnx : instance.getConnexionList()){
                connexionsList.getItems().add(new Label(cnx.getName()));
            }
        }

        /**
         * Change la connexion actuel de l'application
         * @throws Exception La connexion n'existe plus
         */
        public void changeActualConnexion() throws Exception {
            if (connexionsList.getValue() == null){
                return;
            }
            String cnxName = ((Label) connexionsList.getValue()).getText();
            if(cnxName.trim().isEmpty()){
                instance.unsetMongoClient();
            }else{
                Connexion connexion = instance.getConnexionList().stream().filter(cnx -> cnx.getName().equals(cnxName)).findFirst().orElse(null);
                if(connexion == null ){
                    openMessageWindow(new Message("La connexion n'existe pas !", MessageType.Error));
                }
                try {
                    instance.setMongoClient(connexion.getConnexionString());
                }catch (Exception e){
                    openMessageWindow(new Message("Impossible de charger la connexion : " + e.getMessage(), MessageType.Error));
                    instance.unsetMongoClient();
                }
                updateInputDataBasesList();
            }
        }

        /**
         *  IHM : Met à jours le choix des bases de données possibles.
         */
        public void updateInputDataBasesList(){
            dataBasesList.getItems().clear();
            for(String dbNames : instance.getMongoClient().listDatabaseNames()) {
                dataBasesList.getItems().add(new Label(dbNames));
            }
        }

        /**
         * Change la base de données actuelle de l'application
         * @throws Exception
         */
        public void changeActualDataBase() throws Exception {
            if (dataBasesList.getValue() == null){
                return;
            }
            String dbName = ((Label) dataBasesList.getValue()).getText();
            if(dbName.trim().isEmpty()){
                instance.unsetDatabase();
            }else{
                MongoDatabase database = instance.getMongoClient().getDatabase(dbName);
                if(database == null ){
                    openMessageWindow(new Message("La base de données n'existe pas !", MessageType.Error));
                    instance.unsetDatabase();
                    return;
                }
                instance.setDatabase(dbName);
                updateInputCollectionsList();
            }
        }

        /**
         *  IHM : Met à jours le choix des collections possibles.
         */
        public void updateInputCollectionsList(){
            collectionList.getItems().clear();
            for(String collNames : instance.getDatabase().listCollectionNames()) {
                collectionList.getItems().add(new Label(collNames));
            }
        }

        /**
         * Change la base de données actuelle de l'application
         * @throws Exception
         */
        public void changeActualCollection() throws Exception {
            if (collectionList.getValue() == null){
                return;
            }
            String collName = ((Label) collectionList.getValue()).getText();
            if(collName.trim().isEmpty()){
                instance.unsetCollection();
            }else{
                MongoCollection collection = instance.getDatabase().getCollection(collName);
                if(collName == null ){
                    openMessageWindow(new Message("La collection n'existe pas !", MessageType.Error));
                    instance.unsetCollection();
                    return;
                }
                instance.setCollection(collName);
                searchAllDocument();
            }
        }
    /* ------------ Connexion methods  ------------- */


    /* ------------ Opening windows methods  ------------- */
        /**
         * Ouvre la fenêtre permettant d'ajouter et enregistrer une nouvelle connexion
         */
        public void openAddingConnexionWindow(){
            openNewWindow("addconnexion");
        }
    /* ------------ Opening windows methods  ------------- */


    /* ------------ Research methods  ------------- */
    /**
     *  Recherche et affiche le resultat d'une recherche d'un document en fonction d'une clé et de sa valeur
     */
        private void searchAllDocument(){
            FindIterable<Document> documents = instance.getCollection().find();
            writeResult(documents);
        }

        public void searchDocument(){
            if(!checkConnexionElements()){
                return;
            }
            if(keyword.getText().trim().isEmpty() && value.getText().trim().isEmpty()) {
                searchAllDocument();
                return;
            }
            if(!checkSearchingElements()){
                return;
            }
            AggregateIterable<Document> documents = executeSearchingRequest();
            writeResult(documents);
        }

        private boolean checkConnexionElements(){
            if(instance.getMongoClient() == null){
                openMessageWindow(new Message("Veuillez entrer une connexion",MessageType.Error));
                return false;
            }
            if(instance.getDatabase() == null){
                openMessageWindow(new Message("Veuillez entrer une base de données",MessageType.Error));
                return false;
            }
            if(instance.getCollection() == null){
                openMessageWindow(new Message("Veuillez entrer une collection",MessageType.Error));
                return false;
            }
            return true;
        }

        private boolean checkSearchingElements(){
            if(keyword.getText().trim().isEmpty()){
                openMessageWindow(new Message("Veuillez entrer une clé",MessageType.Error));
                return false;
            }
            if(value.getText().trim().isEmpty()){
                openMessageWindow(new Message("Veuillez entrer une valeur",MessageType.Error));
                return false;
            }
            return true;
        }

        private AggregateIterable<Document> executeSearchingRequest(){
            return instance.getCollection().aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq(keyword.getText(), value.getText()))
                        ));
        }

        private void writeResult(MongoIterable<Document> documents){
            if(!documents.iterator().hasNext()){
                result.setText("Aucun document n'a été trouvé.");
            }else{
                String stringResult = "";
                for (MongoCursor<Document> it = documents.iterator(); it.hasNext();){
                    Document doc = it.next();
                    Bson bson = doc;
                    BsonDocument bsonDocument = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
                    JsonWriterSettings.Builder settingsBuilder = JsonWriterSettings.builder().indent(true);
                    settingsBuilder.indentCharacters("      ");
                    stringResult += "[\n" + bsonDocument.toJson(settingsBuilder.build()) + "\n]";
                }
                result.setText(stringResult);
            }
        }
    /* ------------ Research methods  ------------- */
    /* ---------------------------------------- METHODS ---------------------------------------- */
}
