package org.floriotech.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.floriotech.data.Connexion;
import org.floriotech.data.ConnexionWithAuthentification;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.floriotech.data.MongoDBEditorData;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddConnexionController implements Initializable {

    /* INPUT : MINIMUM INFORMATION TO CREATE A CONNEXION */
    @FXML
    private JFXTextField adress;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXTextField name;
    /* END INPUT : MINIMUM INFORMATION TO CREATE A CONNEXION */


    /* INPUT : AUTHENTIFICATION INFORMATION TO CREATE A CONNEXION */
    @FXML
    private JFXCheckBox authentification;
    @FXML
    private JFXTextField login;
    @FXML
    private JFXTextField password;
    /* END INPUT : AUTHENTIFICATION INFORMATION TO CREATE A CONNEXION */

    /* OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */
    @FXML
    private Text validationText;
    /* END OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */

    private MongoDBEditorData instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = MongoDBEditorData.getInstance();
    }

    public void cancel(){
        // get a handle to the stage
        Stage stage = (Stage) validationText.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    private boolean inputIsEmpty(JFXTextField input){
        if(input.getText().trim().isEmpty()){
            return true;
        }
        return false;
    }

    private boolean checkInputData(){
        validationText.setText("");
        //Check Adress
        Pattern adressPattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
        if(inputIsEmpty(adress) || !adressPattern.matcher(adress.getText()).matches()){
            validationText.setText("L'adresse n'est pas correct : elle doit être sous la forme xxx.xxx.xxx.xxx ou x ~ [0-9]");
            validationText.setFill(Color.RED);
            return false;
        }
        //Check Port
        Pattern portPattern = Pattern.compile("(\\d{1,5})");
        if(inputIsEmpty(port) || !portPattern.matcher(port.getText()).matches()){
            validationText.setText("Le port n'est pas correct ! Elle est composé d'au moins 1 chiffre");
            validationText.setFill(Color.RED);
            return false;
        }
        //Check name
        if(inputIsEmpty(name)){
            validationText.setText("Le champs \"nom\" est vide !");
            validationText.setFill(Color.RED);
            return false;
        }
        System.out.println(inputIsEmpty(name) + " " + name.getText() );

        if(authentification.isSelected()){
            //Check login
            if(inputIsEmpty(login)){
                validationText.setText("Le champs \"login\" est vide !");
                validationText.setFill(Color.RED);
                return false;
            }
            //Check Password
            if(inputIsEmpty(password)){
                validationText.setText("Le champs \"mot de passe\" est vide !");
                validationText.setFill(Color.RED);
                return false;
            }
        }
        return true;
    }

    public void addConnexion(){
        if(!checkInputData()){
            return;
        }

        //Créer la connexion
        Connexion newConnexion = !authentification.isSelected() ?
                new Connexion(name.getText(), adress.getText(), port.getText()) :
                new ConnexionWithAuthentification(name.getText(), adress.getText(), port.getText(), login.getText(), password.getText());

        if(instance.isConnexionInList(newConnexion)){
            validationText.setText("L'ajout de la connexion à échoué ! La connexion existe déjà !");
            validationText.setFill(Color.RED);
        }else{
            //Vérifier que la connexion est OK
            MongoClient client = new MongoClient(new MongoClientURI(newConnexion.getConnexionString()));

            System.out.println(client.listDatabaseNames().first());
            //Afficher la confirmation ou l'echec de l'ajout de la connexion
            if(client != null){
                validationText.setText("La connexion est ajouté ! Vous pouvez fermer la fenêtre");
                validationText.setFill(Color.GREEN);
                MongoDBEditorData.getInstance().addNewConnexion(newConnexion);
            }else{
                validationText.setText("L'ajout de la connexion à échoué !");
                validationText.setFill(Color.RED);
            }
        }


    }
}
