package be.kuleuven.candycrush;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class CandyCrushController {
    @FXML
    private Button Startbtn;
    @FXML
    private Pane Paneel;

    @FXML
    private void initialize(){
        Startbtn.setOnMouseClicked(Event -> loadLevel());

    }

    private void loadLevel(){
        Paneel.getChildren().clear();
        System.out.println("cleared");
    }
}