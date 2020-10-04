package org.floriotech;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Clock;

/**
 * JavaFX App
 */
public class App extends Application {

    /*@Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }*/

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("canvas"), 960, 540);
        stage.setScene(scene);
        stage.show();
    }

    private static void setRoot(String fxml) throws IOException{
        scene.setRoot(loadFXML((fxml)));
    }

    private static Parent loadFXML(String fxml) throws IOException{
        System.out.println(App.class.getResource("fxml/" + fxml + ".fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }

}