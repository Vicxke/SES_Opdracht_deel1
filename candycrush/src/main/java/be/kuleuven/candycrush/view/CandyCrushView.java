package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.CandyCrushModel;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

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

    public void update(){
        lblbScore.setText(String.valueOf(game.getScore()));
        speelVeld.getChildren().clear();
        double circleWidth = game.getCircleRadius();//(speelveld.getPrefWidth() / game.getVeldBreedte()); //57
        //double circleHeight = (speelveld.getPrefHeight() / game.getVeldHooghte()); //40
        int b = game.getVeldBreedte();
        for (int i = 0; i < game.getVeldBreedte(); i++) {
            for (int j = 0; j < game.getVeldHooghte(); j++) {
                double radius = circleWidth/2;
                Circle c = new Circle(radius);
                Text text = new Text(game.getGrid().get(i+b*j).toString());

                c.setTranslateX(i*circleWidth + radius);
                c.setTranslateY(j*circleWidth + radius);
                text.setTranslateX(i*circleWidth + radius);
                text.setTranslateY(j*circleWidth + radius);
                text.setFill(Color.BLACK);

                switch (game.getGrid().get(i+b*j)){
                    case 1: c.setFill(Color.MEDIUMPURPLE);
                        break;
                    case 2: c.setFill(Color.CRIMSON);
                        break;
                    case 3: c.setFill(Color.YELLOW);
                        break;
                    case 4: c.setFill(Color.BLUEVIOLET);
                        break;
                    case 5: c.setFill(Color.INDIANRED);
                        break;
                    default: c.setFill(Color.BLACK);
                        break;
                }

                c.setOnMouseClicked(e -> onCircleClick(e));
                text.setOnMouseClicked(e -> onCircleClick(e));

                speelVeld.getChildren().add(c);
                speelVeld.getChildren().add(text);
            }
        }
    }

    public void onCircleClick(MouseEvent e){
        Shape object = (Shape) e.getSource();
        game.onCircleClick(object.getTranslateX(), object.getTranslateY());
        update();
    }
}
