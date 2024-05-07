package be.kuleuven.candycrush.model.candys;

import be.kuleuven.candycrush.model.Candy;

//NormalCandy, met een attribuut color (een int met mogelijke waarden 0, 1, 2, of 3)
public record NormalCandy(int color) implements Candy {
    public NormalCandy{
        if(color < 0 || color > 3){
            throw new IllegalArgumentException("color moet tussen 0 en 3 liggen");
        }
    }
}
