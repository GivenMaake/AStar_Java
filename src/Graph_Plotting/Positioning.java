package Graph_Plotting;

 /*
 * User: Given Maake
 * Date: 2017/12/05
 */

public class Positioning {

    //Variables needed for positioning and reusable functions for for calculating cost of movement
    private int row, column;
    private String value;
    private boolean isChecked, isBlockedThisWay;
    private Positioning parent;

    /*  F (score for square)
        G (cost from @ to square)
        H (estimated cost from square)
    */
    private int F, G, H;





    //FUNCTION UPDATED FROM AreaMap.buildMap function, where string of i, j and (i, j, entries[i][j]) will fill
    //the below variables respectively, row, column and value, helping in filling all the variables needed for 'Map<String, Positioning>'
    public Positioning(int row, int column, String value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Positioning getParent() {
        return parent;
    }

    public void setParent(Positioning parent) {
        this.parent = parent;
    }



    public int getF() {
        return F;
    }
    public void setF() {
        F = G + H;
    }

    public int getG() {
        return G;
    }
    public void setG(int g) {
        G = g;
    }


    public void setH(int h) {
        H = h;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if(!(obj instanceof Positioning))
            return false;

        Positioning positioning = (Positioning) obj;
        return getRow() == positioning.getRow() &&
                getColumn() == positioning.getColumn() &&
                getValue().equals(positioning.getValue());

    }
}
