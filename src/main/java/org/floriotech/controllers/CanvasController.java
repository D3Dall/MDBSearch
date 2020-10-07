package org.floriotech.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.floriotech.App;
import org.floriotech.data.Connexion;
import org.floriotech.data.MongoDBEditorData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {

    @FXML
    private JFXTextField searchingTextField;

    @FXML
    private JFXComboBox connexionsList;

    @FXML
    private JFXComboBox dataBasesList;

    @FXML
    private JFXComboBox collectionList;


    @FXML
    private JFXTextField keyWord;

    @FXML
    private JFXTextArea result;

    private MongoDBEditorData instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //On configure les comboBox
        connexionsList.setConverter(new StringConverter<Label>() {
            @Override
            public String toString(Label o) {
                return o==null? "" : o.getText();
            }

            @Override
            public Label fromString(String s) {
                return new Label(s);
            }
        });
        dataBasesList.setConverter(new StringConverter<Label>() {
            @Override
            public String toString(Label o) {
                return o==null? "" : o.getText();
            }

            @Override
            public Label fromString(String s) {
                return new Label(s);
            }
        });
        collectionList.setConverter(new StringConverter<Label>() {
            @Override
            public String toString(Label o) {
                return o==null? "" : o.getText();
            }

            @Override
            public Label fromString(String s) {
                return new Label(s);
            }
        });
        instance = MongoDBEditorData.getInstance();
    }

    /**
     *  IHM : Met à jours le choix des connexions possible.
     */
    public void updateListOfConnexions(){
        //On supprime tous les items de la comboBox
        connexionsList.getItems().clear();
        //Pour toutes les connexions présentes dans la BD, on ajoute un item
        for(Connexion cnx : instance.getConnexionList()){
            connexionsList.getItems().add(new Label(cnx.getName()));
        }
    }

    /**
     * Change la connexion actuel de l'application
     * @throws Exception La connexion n'existe plus
     */
    public void changeActualConnexion() throws Exception {
        String cnxName = ((Label) connexionsList.getValue()).getText();
        if(cnxName.trim().isEmpty()){
            instance.unsetMongoClient();
        }else{
            System.out.println(cnxName);
            Connexion connexion = instance.getConnexionList().stream().filter(cnx -> cnx.getName().equals(cnxName)).findFirst().orElse(null);
            if(connexion == null ){
                throw new Exception("blabla");
            }
            instance.setMongoClient(connexion.getConnexionString());
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
        String dbName = ((Label) dataBasesList.getValue()).getText();
        if(dbName.trim().isEmpty()){
            instance.unsetDatabase();
        }else{
            MongoDatabase database = instance.getMongoClient().getDatabase(dbName);
            if(dbName == null ){
                throw new Exception("bloblo");
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
        String collName = ((Label) collectionList.getValue()).getText();
        if(collName.trim().isEmpty()){
            instance.unsetCollection();
        }else{
            MongoCollection collection = instance.getDatabase().getCollection(collName);
            if(collName == null ){
                throw new Exception("blobloAI");
            }
            instance.setCollection(collName);
        }
    }

    public void openAddingConnexionWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/addconnexion.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Ajouter une connexion");
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void searchDocument(){
        System.out.println("search ...");
        String word = keyWord.getText();
        if(word.trim().isEmpty()){
           return;
        }

        Document doc = (Document) instance.getCollection().find().first();
        Bson bson = doc;
        BsonDocument bsonDocument = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());

        JsonWriterSettings.Builder settingsBuilder = JsonWriterSettings.builder().indent(true);
        settingsBuilder.indentCharacters("      ");

        System.out.println("0"+bsonDocument.toJson(settingsBuilder.build()));

        result.setText(bsonDocument.toJson(settingsBuilder.build()));

    }
}
