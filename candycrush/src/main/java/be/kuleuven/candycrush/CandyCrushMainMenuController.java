package be.kuleuven.candycrush;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class CandyCrushMainMenuController {
    @FXML
    private Button Startbtn;
    @FXML
    private Pane Paneel;

    @FXML
    private void initialize(){
        System.out.println("start");
        Startbtn.setOnMouseClicked(Event -> loadLevel());

    }

    private void loadLevel(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CandyCrushGameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 500);

            // Get the stage from the current button
            Stage stage = (Stage) Startbtn.getScene().getWindow();

            stage.setTitle("Level 1"); // Set the title of the new scene
            stage.setScene(scene); // Set the scene to the new one
            stage.show(); // Show the new scene
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}