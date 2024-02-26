package be.kuleuven;

import java.util.*;

public class CheckNeighboursInGrid {
    public CheckNeighboursInGrid() {
    }

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *@return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     *@param grid - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     *@param width - Specifies the width of the grid.
     *@param height - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     *@param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     */
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid,int width, int height, int indexToCheck){
        List<Integer> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for(int j = -1; j<= 1; j++){
                //index to check +1 -1 of 0
                //en de rij erboven of eronder de de breedte van een rij = kolom grote
                int indexOtherValue = indexToCheck + j + (i * width);
                //kleine controle zodat we niet buiten de lijst kunnen gaan
                if(indexOtherValue >= 0 && indexOtherValue < width * height) {
                    Integer otherValue = ((List<Integer>) grid).get(indexOtherValue);
                    //kijk of de waardes dezelfde zijn en zien dat de index niet hetzelfde is
                    if (otherValue.equals(((List<Integer>) grid).get(indexToCheck)) && indexOtherValue != indexToCheck) {
                        result.add(indexOtherValue);
                    }
                }
            }
        }
        return result;
    }
}
