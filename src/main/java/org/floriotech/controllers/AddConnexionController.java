package org.floriotech.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.floriotech.data.Connexion;
import org.floriotech.data.ConnexionWithAuthentification;

import java.net.URL;
import java.util.ResourceBundle;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addConnexion(){
        //Créer la connexion
        Connexion newConnexion = !authentification.isSelected() ?
                new Connexion(name.getText(), adress.getText(), port.getText()) :
                new ConnexionWithAuthentification(name.getText(), adress.getText(), port.getText(), login.getText(), password.getText());


        //Vérifier que la connexion est OK
        //Fermer la fenêtre si OK
        //Afficher un message d'erreur sinon
    }
}
