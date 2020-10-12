package org.floriotech.controllers;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.floriotech.App;
import org.floriotech.data.Message;
import org.floriotech.data.MessageType;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageController extends Controller{

    /* ---------------------------------------- VARIABLE ---------------------------------------- */
        @FXML
        public JFXTextArea messageText;

        private Message message;
    /* ---------------------------------------- VARIABLE ---------------------------------------- */



    /* ---------------------------------------- METHODS ---------------------------------------- */
    /* ------------ Override methods ------------- */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }
        @Override
        public Stage getStage() {
            return (Stage) messageText.getScene().getWindow();
        }
    /* ------------ Override methods ------------- */


    public void setMessage(Message message) {
        this.message = message;
        if(message.getType().equals(MessageType.Error)){
        }
        messageText.setText(message.getMessage());
    }

    /* ---------------------------------------- METHODS ---------------------------------------- */

}
