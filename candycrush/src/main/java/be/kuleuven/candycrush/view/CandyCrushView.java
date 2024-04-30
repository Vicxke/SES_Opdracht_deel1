package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.CandyCrushModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static be.kuleuven.candycrush.model.CandyCrushModel.Position.fromIndex;

public class CandyCrushView extends Region {
    private AnchorPane speelVeld;
    private AnchorPane gameView;

    private CandyCrushModel game;

    private Label lblbScore;

    public CandyCrushView(CandyCrushModel game, AnchorPane gameView, AnchorPane speeldVeld, Label score) {
        this.speelVeld = speeldVeld;
        this.gameView = gameView;
        this.game = game;
        this.lblbScore = score;
        update();
    }

    public Node makeCandyShape(CandyCrushModel.Position position, CandyCrushModel.Candy candy){
        int x = position.col()*game.getCircleRadius();
        int y = position.row()*game.getCircleRadius();

        return switch (candy) {
            case CandyCrushModel.NormalCandy normalCandy -> {
                Circle circle = new Circle(x + game.getCircleRadius()/2, y + game.getCircleRadius()/2, game.getCircleRadius()/2);
                circle.setFill(getCorrectColor(normalCandy.color()));
                yield circle;
            }
            case CandyCrushModel.Stheefje stheefje -> {
                Rectangle rectangle = new Rectangle(x , y, game.getCircleRadius(), game.getCircleRadius());
                rectangle.setFill(Color.BROWN);
                yield rectangle;
            }
            case CandyCrushModel.bolleke bolleke -> {
                Rectangle rectangle = new Rectangle(x, y, game.getCircleRadius(), game.getCircleRadius());
                rectangle.setFill(Color.DARKGREEN);
                yield rectangle;
            }
            case CandyCrushModel.petoterke petoterke -> {
                Rectangle rectangle = new Rectangle(x, y, game.getCircleRadius(), game.getCircleRadius());
                rectangle.setFill(Color.ORANGE);
                yield rectangle;
            }
            case CandyCrushModel.haaariebooo haaariebooo -> {
                Rectangle rectangle = new Rectangle(x, y, game.getCircleRadius(), game.getCircleRadius());
                rectangle.setFill(Color.LIME);
                yield rectangle;
            }
        };
    }

    public Color getCorrectColor(int candy){
        switch (candy){
            case 0: return Color.MEDIUMPURPLE;
            case 1: return Color.CRIMSON;
            case 2: return Color.YELLOW;
            case 3: return Color.BLUEVIOLET;
            default: return Color.BLACK;
        }
    }

    public void update(){
        lblbScore.setText(String.valueOf(game.getScore()));
        speelVeld.getChildren().clear();
        //double circleWidth = game.getCircleRadius();//(speelveld.getPrefWidth() / game.getVeldBreedte()); //57
        //double circleHeight = (speelveld.getPrefHeight() / game.getVeldHooghte()); //40
        //int b = game.getBoard().breedte();
        for (int i = 0; i < game.getGrid().getSize().boardSize(); i++) {

            CandyCrushModel.Position pos = fromIndex(i, game.getSize());
            Node candy = makeCandyShape(pos,game.getGrid().getCellAt(pos));
            //System.out.println("X: " + candy.getBoundsInParent().getMinX() + " Y: " + candy.getBoundsInParent().getMinY());

            candy.setOnMouseClicked(e -> onCircleClick(e));

            speelVeld.getChildren().add(candy);
        }

        //teken een kleine zwarte cirkel op een match van de game.allMatches()
        for(List<CandyCrushModel.Position> match : game.findAllMatches()){
            for(CandyCrushModel.Position pos : match) {
                Circle circle = new Circle(pos.col() * game.getCircleRadius() + game.getCircleRadius() / 2, pos.row() * game.getCircleRadius() + game.getCircleRadius() / 2, game.getCircleRadius() / 4);
                circle.setFill(Color.BLACK);
                speelVeld.getChildren().add(circle);
            }
        }
    }

    public void onCircleClick(MouseEvent e){
        Node candy = (Node) e.getSource();
        System.out.println("X: " + candy.getBoundsInParent().getMinX() + " Y: " + candy.getBoundsInParent().getMinY());
        game.onCircleClick(candy.getBoundsInParent().getMinX(), candy.getBoundsInParent().getMinY());
        update();
    }
}
