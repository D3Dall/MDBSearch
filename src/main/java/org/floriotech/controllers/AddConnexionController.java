package org.floriotech.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.floriotech.data.Connexion;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.floriotech.data.MDBSearchData;
import org.floriotech.data.Message;
import org.floriotech.data.MessageType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddConnexionController extends Controller {

    /* ---------------------------------------- VARIABLE ---------------------------------------- */
        /* INPUT : MINIMUM INFORMATION TO CREATE A CONNEXION */
            @FXML
            private JFXTextField adress;
            @FXML
            private JFXTextField port;
            @FXML
            private JFXTextField name;
        /* END INPUT : MINIMUM INFORMATION TO CREATE A CONNEXION */


        /* OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */
            @FXML
            private Text validationText;
        /* END OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */

        private MDBSearchData instance;

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
            return (Stage) validationText.getScene().getWindow();
        }
    /* ------------ Override methods ------------- */

    /* ------------ Validation methods ------------- */
        /**
         * Vérifie si l'input JFXTextField contient une valeur non non vide.
         * @param input l'input concerné par la vérification.
         * @return true si l'input est vide, false sinon.
         */
        private boolean inputIsEmpty(JFXTextField input){
            return input.getText().trim().isEmpty() ? true : false;
        }

        /**
         * Vérifie la validité des Input du formulaire d'ajout de connexion.
         * @return true si les données du formulaire sont correctes, false sinon.
         */
        private boolean checkInputData(){
            validationText.setText("");

            Pattern adressPattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
            if(inputIsEmpty(adress) || !adressPattern.matcher(adress.getText()).matches()){
                validationText.setText("L'adresse n'est pas correct : elle doit être sous la forme xxx.xxx.xxx.xxx ou x ~ [0-9]");
                validationText.setFill(Color.RED);
                return false;
            }

            Pattern portPattern = Pattern.compile("(\\d{1,5})");
            if(inputIsEmpty(port) || !portPattern.matcher(port.getText()).matches()){
                validationText.setText("Le port n'est pas correct ! Elle est composé d'au moins 1 chiffre");
                validationText.setFill(Color.RED);
                return false;
            }

            if(inputIsEmpty(name)){
                validationText.setText("Le champs \"nom\" est vide !");
                validationText.setFill(Color.RED);
                return false;
            }
            return true;
        }

        public void addConnexion(){
            if(!checkInputData()){
                return;
            }

            Connexion newConnexion = new Connexion(name.getText(), adress.getText(), port.getText());

            if(instance.isConnexionInList(newConnexion)){
                validationText.setText("L'ajout de la connexion à échoué ! La connexion existe déjà !");
                validationText.setFill(Color.RED);
                return;
            }

            MongoClient client = new MongoClient(new MongoClientURI(newConnexion.getConnexionString()));

            if(client != null){
                validationText.setText("La connexion est ajouté ! Vous pouvez fermer la fenêtre");
                validationText.setFill(Color.GREEN);
                try {
                    instance.addNewConnexion(newConnexion);
                } catch (IOException e) {
                    openMessageWindow(new Message("Impossible de sauvegarder la connexion : " + e.getMessage(), MessageType.Error));
                }
            }else{
                validationText.setText("L'ajout de la connexion à échoué !");
                validationText.setFill(Color.RED);
            }

        }
    /* ------------ Validation methods ------------- */
    /* ---------------------------------------- METHODS ---------------------------------------- */
}
