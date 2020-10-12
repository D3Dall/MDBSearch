package org.floriotech.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.floriotech.App;
import org.floriotech.data.Message;

import java.io.IOException;

public abstract class Controller implements Initializable {

    protected Controller openNewWindow(String fxmlName){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/"+ fxmlName +".fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Message");
            stage.setScene(new Scene(root));
            stage.show();
            return (Controller) fxmlLoader.getController();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    protected void openMessageWindow(Message message){
        MessageController controller = (MessageController) openNewWindow("message");
        controller.setMessage(message);
    }

    protected abstract Stage getStage();

    /**
     * Ferme la fenêtre
     */
    public void cancel(){
        Stage stage = getStage();
        stage.close();
    }

}
