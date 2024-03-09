package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.CandyCrushModel;
import be.kuleuven.candycrush.view.CandyCrushView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    private Button btnResetGame;

    @FXML
    private Label lblNaamSpeler;

    @FXML
    private void initialize(){
        this.game = new CandyCrushModel(10, 10);
        this.view  = new CandyCrushView(game, gameView, speelveld, lblScore);

        updateView();

        btnResetGame.setOnMouseClicked(Event->resetGame());
    }

    public void resetGame(){
        game.resetGame();
        view.update();
    }

    public void setNaamSpeler(String naamSpeler) {
        game.setPlayerName(naamSpeler);
        lblNaamSpeler.setText(naamSpeler);
    }

    public void updateView(){
        view.update();
    }
}
