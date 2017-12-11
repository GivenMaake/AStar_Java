import Graph_Plotting.*;


import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.*;
import java.util.*;

import java.applet.*;
import java.util.List;

 /*
 * User: GivenMaake
 * Date: 2017/12/05
 */

public class AStarAlgorithm_Main extends Applet implements AdjustmentListener {


    //File name in project's root folder
    public static String MapFileName ="large_map.txt";


    static Graphics screen;
    public static Object[] MapData;
    public static ArrayList<String> OutputData=new ArrayList<String>();
    static String output="";



    //Main Function
    public static void main (String[] args) {

        // wholeMap declared as Area class type
        AreaMap wholeMap;

        /* wholeMap will have all the data needed for path finding:
        rows, columns, endPoint, startPoint, map, indexes
        ...after below code*/
        wholeMap = load(MapFileName);
        AreaMap outputMap = load(MapFileName);

        //COMPARATOR
        Comparator<Positioning> comparator;
        comparator = new Comparator<Positioning>() {
            @Override
            public int compare(Positioning o1, Positioning o2) {
                return (o1.getF() - o2.getF());
            }
        };

        //OPEN SET
        PriorityQueue<Positioning> openSet;
        openSet = new PriorityQueue<Positioning>(10, comparator);

        //CLOSED SET
        List<Positioning> closedSet;
        closedSet = new ArrayList<Positioning>();

        //Starting Point at @
        Positioning current = wholeMap.getStartPoint();


        //This will get the cost of move
        current.setG(getCosts().get(current.getValue()));// G (cost from @ to square)
        current.setH(wholeMap.getManhattanDistance(current));//H (estimated cost from square)
        current.setF();//F (score for square)

        //Add current coordinates to the openSet(initially the start point will be added as an open set)
        openSet.add(current);


        //Path finding loop-remaining tiles from the start point will be search using the following loop
        while(!closedSet.contains(wholeMap.getEndPoint()) || openSet.isEmpty()){

            //The best possible move is chosen from the openSet
            current = openSet.remove();//***important

            //Current tile is added to closedSet(by doing so it makes it the start point of the next move)
            closedSet.add(current);

            /*
            Within this Enhanced 'for loop', the SurroundingTiles that are next to the 'current' tile are evaluated at
            a time as it loops. Their coordinates are determined using coordinates.getX() and coordinates.getY()
            Tile in the 'openSet' becomes the 'current' tile and at same time be set as a closedSet above!
            */
            for (Coordinates coordinates : wholeMap.getSurroundingTiles(current)) {

                //SurroundingTile as a Positioning type of class is declared here
                Positioning SurroundingTile;


                //SurroundingTile of the 'current' tile will be located using the index determined by Coordinates cX and
                // cY in the whole map as coded below
                int cX=current.getRow() + coordinates.getX();
                int cY=current.getColumn() + coordinates.getY();
                SurroundingTile = wholeMap.getIndex().get(new Coordinates(cX, cY).toString());

                /*Having determined the SurroundingTile, to determine the walkable and non-walkable area, we then check
                if the closedSet does not contain any of exact values of the SurroundingTile (in terms of row, column and value) 'and'
                the 'SurroundingTile' value is not any of '~' representing non-walkable(water) then the 'if statement' can be entered
                only if both conditions are as explained, otherwise the above for loop continue to pick the next SurroundingTile*/
                if(!closedSet.contains(SurroundingTile) && !SurroundingTile.getValue().equals("~")){



                    //Since 'current' was added as the openSet, the following 'if statement' prevents the code to set
                    //the 'current' tile as a SurroundingTile. Therefore this 'if statement' will not allow the current tile
                    //to be taken as the SurroundingTile
                    if(!openSet.contains(SurroundingTile)){

                        /*
                        Having passed the above condition, then the SurroundingTile can be set as a parent to the current tile
                        and similarly the current tile is a node to the parent tile.
                        */
                        SurroundingTile.setParent(current);

                        /*
                        The following will determine the cost of movement of the above SurroundingTile
                        as well as others that will be set as parent tiles to the current tile.
                        */
                        SurroundingTile.setG(current.getG() + getCosts().get(SurroundingTile.getValue()));

                        //Determine the distance from the SurroundingTile chosen to the goal using Manhattan Distance
                        //and the value produced by 'wholeMap.getManhattanDistance(SurroundingTile)' will be set to H
                        SurroundingTile.setH(wholeMap.getManhattanDistance(SurroundingTile));


                        /*
                        The following will call the sub setF() in positioning class and you will note that F = G + H,
                        which becomes total score of the movement.
                        */
                        SurroundingTile.setF();

                        openSet.add(SurroundingTile);
                    }
                    else{
                        /*
                        This is what determines the best tile amoungs all the SurroundingTile that are already in the openSet
                        If SurroundingTile's G is less than current's G then set current as the parent to that SurroundingTile
                        */
                        if(SurroundingTile.getG() < current.getG()){

                            SurroundingTile.setParent(current);

                            SurroundingTile.setG(current.getG() + getCosts().get(SurroundingTile.getValue()));
                            SurroundingTile.setH(wholeMap.getManhattanDistance(SurroundingTile));
                            SurroundingTile.setF();
                            openSet.add(SurroundingTile);
                        }
                    }
                }
            }
        }




        //If there is no SurroundingTile to use for path
        if(openSet.isEmpty()){
            System.out.println("Sorry, the path from 'start point = @' to 'end point = X' could not be resolved");
        }
        //else '#' will be set to all all parents of the current tile as the best move. and the output file will be created
        else{
            while(current != null){
                current.setValue("#");

                //At that current position, set the above character '#'
                outputMap.setPosition(current);

                //Move to the next parent of the current tile replacing their values with '#'
                current = current.getParent();
            }
            try {
                //Create a new text file to write the outputMap to
                PrintWriter writer = new PrintWriter( MapFileName.replace(".txt","") + "_OUTPUT.txt", "UTF-8");

                //loop through all indexes of the outputMap writing every line to the output text
                for (int i = 0; i < outputMap.getMap().length; i++) {
                    for (int j = 0; j < outputMap.getMap()[i].length; j++) {
                        writer.print(outputMap.getMap()[i][j]);


                        OutputData.add(outputMap.getMap()[i][j]);
                        output += (outputMap.getMap()[i][j]).toString();
                    }
                    writer.println();
                }

                writer.close();
                System.out.println("Successfully created output file,"  + MapFileName.replace(".txt","") + "_OUTPUT.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }
















    /*---------------------------FUNCTIONS------------------------------------*/

    //LOAD THE FILE
    public static AreaMap load(String MapfilePath) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(MapfilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AreaMap wholeMap = null;//Initialize wholeMap at zero

        try {
            //The very first line of the text MapfilePath is read here and stored in string variable 'line'
            String line = reader.readLine();


            /*A new variable called terrains is declared as 1D array, this will hold each character read from the text
             MapfilePath in evaluation*/
            List<String[]> terrains = new ArrayList<String[]>();


            /*The following loop reads all the lines that are in the text MapfilePath,
            loop as long as 'line' from the reader is not equal to zero/null, note that 'chr(13)' or '/n' are not
            considered nulls*/
            while(line != null){

                /*Apart from terrains that holds all characters of  the text MapfilePath, terrain is declared below as
                1D string array,and will be used to hold character of the last line read.*/
                String[] terrain = new String[line.length()];

                //All characters of the line read are now stored in terrain
                for (int i = 0; i < line.length(); i++) {
                    terrain[i] = String.valueOf(line.charAt(i));
                }

                //Now the characters of the lines read are then added, not replaced, in terrains
                terrains.add(terrain);

                //then the next line is read, then loop until all lines are read in the text MapfilePath
                line = reader.readLine();
            }


            /*
            Now size of our area map will be determined by the code below,
            to determine the size of the expected map in rows and columns, we need to know an overall size of the
            terrains and the size of the first index of that specific terrains. Using the example text MapfilePath, we
            will have 5 rows and 5 columns
            */
            wholeMap = new AreaMap(terrains.size(), terrains.get(0).length);

            /*
            This converts terrains to array and passes the arraylist to wholeMap which is equated as AreaMap class above
            Overall values to be filled in the map are populated here and plotted**
            */
            wholeMap.buildMap(terrains.toArray(new String[terrains.size()][]));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*
        Values of wholeMap are finally returned and available to be used in other parts of the package
        --Just a little explanation here, 'public AreaMap' is going to return wholeMap which consists of two values
        --these two values are 'terrains.size()' and 'terrains.get(0).length' representing rows and columns respectively
        */
        return wholeMap;
    }



    //COSTS
    private static final Map<String, Integer> COSTS;
    static {
        COSTS = new HashMap<String, Integer>();
        COSTS.put(".", 1);
        COSTS.put("@", 1);
        COSTS.put("X", 1);
        COSTS.put("*", 2);
        COSTS.put("^", 3);
        COSTS.put("~", null);
    }
    public static Map<String, Integer> getCosts() {
        return COSTS;
    }




    //APPLET WINDOW
    Scrollbar scrollbar;
    public void init()
    {
        /*
        To resize an applet window use,
        void resize(int x, int y) method of an applet class.
        */
        resize(500,690);

        //Setting up the font, font size...
        Font myFont = new Font("TimesRoman", Font.PLAIN, 14);
        setFont(myFont);

        //Setup the background color of the applet window
        setBackground (Color.black);
        setForeground(Color.green);

        /*
        //Creating scroll bars
        //scrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 50, 0, 0, 100);
        scrollbar = new Scrollbar(Scrollbar.VERTICAL, 200, 0, 0, 100);
        scrollbar.setSize(100,100);
        add(scrollbar);
        scrollbar.addAdjustmentListener(this);*/


        //Calling the main activity for applet
        AppletWindow.AppletWindow_Main();


    }

    //Scrollbar listener
    public void adjustmentValueChanged(AdjustmentEvent e) {

    }


    //Drawing in the applet window
    public  void paint(Graphics g)
    {
        //Displaying the output of the main
        AppletWindow.outputW(g);
    }




}
