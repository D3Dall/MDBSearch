package org.floriotech.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
