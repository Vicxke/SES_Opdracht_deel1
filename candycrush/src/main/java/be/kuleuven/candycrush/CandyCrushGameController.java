package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.CandyCrushModel;
import be.kuleuven.candycrush.view.CandyCrushView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class CandyCrushGameController {
    CandyCrushModel game;
    CandyCrushView view;

    @FXML
    private AnchorPane speelveld;

    @FXML
    private AnchorPane gameView;
    @FXML
    private Label lblScore;

    @FXML
    private void initialize(/*CandyCrushModel model*/){
        this.game = new CandyCrushModel(10, 10);
        this.view  = new CandyCrushView(game, gameView, speelveld, lblScore);
        updateView();
    }


    public void updateView(){
        view.update();
    }
}
