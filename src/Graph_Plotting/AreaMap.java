package Graph_Plotting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* User: Given Maake
* Date: 2017/12/05
*/

public class AreaMap {

    //Grid variables(rows and columns) to draw or specify the working area declared as integers
    private int rowNum, columnNum;

    //Variables to denote our start and end points declared here
    private Positioning endPoint, startPoint;

    //map declared as a 2D string array, this will store each line of characters read from our specified path in load
    private String[][] map;

    //Index declared as Map, function in java.util.map
    private Map<String, Positioning> index;

    /*
    MAP VARIABLES-All the details of the map expected are populated below, these are the values that were determined
    from load function in AStarAlgorithm_Main class, namely 'terrains.size()' and 'terrains.get(0).length' and they
    will be stored in rowNum and columnNum respectively.
    */
    public AreaMap(int rowNum, int columnNum){
        this.rowNum = rowNum;
        this.columnNum = columnNum;

        //Map determined using rows and columns
        map = new String[rowNum][columnNum];

        // index initialized, HashMap<String, Positioning>() being a function
        index = new HashMap<String, Positioning>();
    }



    /*
    FILLING/BUILDING THE MAP-This is where the map is now filled with characters read and built from 'load' class
    IDENTIFYING THE FLAT LANDS REPRESENTING CHARACTERS from the map built from 'load' class
    entries declared as 2D string array, entries variable will fetch the values that where populated from
    load( areaMap.buildMap(terrains.toArray(new String[terrains.size()][])); )
    */
    public void buildMap(String[][] entries){

        /*
        FINDING THE INDEXES OF ALL CHARACTERS IN A BUILT MAP
        initially all entries are filled to the map, starting with the row then filling all the columns of that current row
        then follows the next rows and filling all their columns, where 'i' is a row and 'j' is a columns
        at the very same time finding the index of each character in a map and storing this indexes in 'index' variable
        */
        for (int i = 0; i < rowNum; i++) {

            for (int j = 0; j < columnNum; j++) {

                map[i][j] = entries[i][j];//map filled with entries built from load

                /*
                This is how the update of the index is happening for each and every character added to map above
                index.put( String values of i and J will be put separated by '.', 'Positioning' class is called)
                */
                index.put(String.valueOf(i) + "." + String.valueOf(j), new Positioning(i, j, entries[i][j]));
            }
        }

        /*
        Determines the end point by running and checking every character in entries comparing it with 'X' as that
        represent the end point
        */
        for (int i = rowNum - 1; i >= 0; i--) {
            for (int j = columnNum - 1; j >= 0; j--) {
                //Comparing each entry if it holds 'X', if it does then store entry coordinates together with the contents
                if(entries[i][j].equals("X")){
                    endPoint = new Positioning(i, j, entries[i][j]);
                    break;
                }
            }
            if(endPoint != null){
                break;
            }
        }

        /*
        Determines the start point by running and checking every character in entries comparing it with '@' as that
        represent the end point
        */
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                //Comparing each entry if it holds '@', if it does then store entry coordinates together with the contents
                if(entries[i][j].equals("@")){
                    startPoint = new Positioning(i, j, entries[i][j]);
                    break;
                }
            }
            if(startPoint != null){
                break;
            }
        }
    }




    //FINDING CURRENT AND SURROUNDING TILES COORDINATES
    public List<Coordinates> getSurroundingTiles(Positioning current){
        /*
        CURRENT TILE COORDINATES
        This sets the values of 'current.getRow()' to the x and the value of 'current.getColumn()' to the y in
        Coordinates class and same time assigns the values of Coordinates in that class to currentTileCoordinates here
        */
        Coordinates currentTileCoordinates = new Coordinates(current.getRow(), current.getColumn());

        List<Coordinates> SurroundingTiles;
        SurroundingTiles = new ArrayList<Coordinates>();
        //The following code prevents checking tiles twice or circling back
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if((x != 0 || y != 0)) {
                    int cx= currentTileCoordinates.getX() + x;
                    int cy= currentTileCoordinates.getY() + y;
                    if((cx >= 0 && cy >= 0) && (cx < rowNum && cy < columnNum)){
                        //And finally adding the correct Coordinates to SurroundingTiles
                        SurroundingTiles.add(new Coordinates(x, y));
                    }
                }
            }
        }

        return SurroundingTiles;
    }











    public Map<String, Positioning> getIndex() {
        return index;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public String[][] getMap() {
        return map;
    }

    public Positioning getStartPoint() {
        return startPoint;
    }

    public Positioning getEndPoint() {
        return endPoint;
    }

    public int getManhattanDistance(Positioning position){
        //Calculating the Manhattan Distance, formula is Math.abs[x-x1]+[y+y1]
        return Math.abs(position.getRow() - endPoint.getRow()) + Math.abs(position.getColumn() - endPoint.getColumn());
    }

    public void setPosition(Positioning positioning){

        map[positioning.getRow()][positioning.getColumn()] = positioning.getValue();

        index.put(String.valueOf(positioning.getRow()) + String.valueOf(positioning.getColumn()), positioning);
    }
}
