import Graph_Plotting.AreaMap;
import Graph_Plotting.Coordinates;
import Graph_Plotting.Positioning;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/*
* User: GivenMaake
* Date: 2017/12/05
*/
public  class AppletWindow {

    //Output variable to be used
    static String output="";
    //File name
    static String FileName="large_map";
    // Full path
    static String mapPath= "C:\\Astar\\" + AStarAlgorithm_Main.MapFileName;;



    public static void AppletWindow_Main(){
        // wholeMap declared as Area class type
        AreaMap wholeMap;

        /* wholeMap will have all the data needed for path finding:
        rows, columns, endPoint, startPoint, map, indexes
        ...after below code*/
        wholeMap = AStarAlgorithm_Main.load(mapPath);
        AreaMap outputMap = AStarAlgorithm_Main.load(mapPath);

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
        current.setG(AStarAlgorithm_Main.getCosts().get(current.getValue()));// G (cost from @ to square)
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
                        SurroundingTile.setG(current.getG() + AStarAlgorithm_Main.getCosts().get(SurroundingTile.getValue()));

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

                            SurroundingTile.setG(current.getG() + AStarAlgorithm_Main.getCosts().get(SurroundingTile.getValue()));
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
                PrintWriter writer = new PrintWriter( mapPath.replace(".txt","") + "_OUTPUT.txt", "UTF-8");

                //loop through all indexes of the outputMap writing every line to the output text
                for (int i = 0; i < outputMap.getMap().length; i++) {
                    for (int j = 0; j < outputMap.getMap()[i].length; j++) {
                        writer.print(outputMap.getMap()[i][j]);

                        output += (outputMap.getMap()[i][j]).toString();
                    }
                    writer.println();
                }

                writer.close();
                System.out.println("Successfully created output file,"  + mapPath.replace(".txt","") + "_OUTPUT.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void outputW(Graphics g){
        //Initialize content
        String content = "";
        try
        {
            char ch;
            //Empty the buffer
            StringBuffer buff = new StringBuffer("");

            //Locate the output file that was created and read from it
            FileInputStream fis = new FileInputStream(mapPath.replace(".txt","") + "_OUTPUT.txt");

            while(fis.available()!=0)
            {
                //Read each character
                ch = (char)fis.read();
                buff.append(ch);
            }
            fis.close();

            //Store everything that was read in content
            content = new String(buff);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Cannot find the specified file...");
            g.drawString("Cannot find the specified file...",130,150);
        }
        catch(IOException i)
        {
            g.drawString("Cannot read file...",130,150);
            System.out.println("Cannot read file...");
        }

        //Create an object to store the splitted contents read stored in content variable
        Object FinalContent[];
        FinalContent=content.split("\\n");

        //First lines on the applet window
        g.drawString("Below is the output map of '"+FileName+".txt'",1,15);
        g.drawString("Text file of the output may be viewed directly from "+ mapPath,1,30);

        //Loop through all lines spitted while displaying them on the applet viewer
        for (int i=0;i<=FinalContent.length;i++){
            g.drawString(FinalContent[i].toString(),1,50+(i*13));
        }

    }

}
