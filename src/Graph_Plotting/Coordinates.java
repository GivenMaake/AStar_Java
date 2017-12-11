package Graph_Plotting;

 /*
 * User: Given Maake
 * Date: 2017/12/05
 */


//Reusable class used to determine coordinates of tiles
public class Coordinates {
    private int x, y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return String.valueOf(x) + "."  +String.valueOf(y);
    }
}
