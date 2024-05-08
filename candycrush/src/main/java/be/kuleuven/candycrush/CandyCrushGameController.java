package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.BoardSize;
import be.kuleuven.candycrush.model.Candy;
import be.kuleuven.candycrush.model.CandyCrushModel;
import be.kuleuven.candycrush.model.Position;
import be.kuleuven.candycrush.model.candys.NormalCandy;
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
        this.game = model1; //new CandyCrushModel(10, 10);
        this.view  = new CandyCrushView(game, gameView, speelveld, lblScore);

        game.maximizeScore();

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

    CandyCrushModel model1 = createBoardFromString("""
   @@o#
   o*#o
   @@**
   *#@@""");

    CandyCrushModel model2 = createBoardFromString("""
   #oo##
   #@o@@
   *##o@
   @@*@o
   **#*o""");

    CandyCrushModel model3 = createBoardFromString("""
   #@#oo@
   @**@**
   o##@#o
   @#oo#@
   @*@**@
   *#@##*""");

    public static CandyCrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandyCrushModel(size.breedte(), size.hoogte());
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.getGrid().replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }

    private static Candy characterToCandy(char c) {
        return switch(c) {
            case 'o' -> new NormalCandy(0);
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }
}
