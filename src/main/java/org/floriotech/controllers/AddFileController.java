package org.floriotech.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AddFileController implements Initializable {

    /* INPUT : BASIC INFORMATION TO CREATE A FILE*/
    @FXML
    private JFXTextField fileName;
        /*INPUT : CHOOSING OF A FILE TYPE*/
        @FXML
        private JFXRadioButton simpleFileTypeSelector;

        @FXML
        private JFXRadioButton searchingFileTypeSelector;
        /* END INPUT : CHOOSING OF A FILE TYPE*/
    /* END INPUT : BASIC INFORMATION TO CREATE A FILE*/

    /* INPUT : EXTRA INFORMATION FOR SEARCHING FILE TYPE */
    @FXML
    private JFXComboBox connexionSelector;

    @FXML
    private JFXComboBox dataBaseSelector;

    @FXML
    private JFXComboBox CollectionSelector;
    /* END INPUT : EXTRA INFORMATION FOR SEARCHING FILE TYPE */

    /* OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */
    @FXML
    private Text validationText;
    /* END OUTPUT : VALIDATION INFORMATION ON THE CONNEXION CREATION PROCESS  */


    private CanvasController canvasController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void addASimpleFile(){

    }

    private void addAMongoDBEditorFile(){

    }

    public void addANewFile(){
        if(simpleFileTypeSelector.isSelected()){
            addASimpleFile();
        }else if (searchingFileTypeSelector.isSelected()){
            addAMongoDBEditorFile();
        }else{
            validationText.setText("Selectionnez un type de fichier");
            validationText.setFill(Color.RED);
        }
    }

    public CanvasController getCanvasController() {
        return canvasController;
    }

    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
    }
}
