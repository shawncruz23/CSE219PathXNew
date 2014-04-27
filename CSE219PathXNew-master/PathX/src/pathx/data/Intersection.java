package pathx.data;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents an intersection in a level. Note that an intersection
 * connects roads and can be thought of as a node on a graph.
 * 
 * @author Richard McKenna
 */

public class Intersection {
    
     // INTERSECTION LOCATION
    public int x;
    public int y;
    
    // IS IT OPEN OR NOT
    public boolean open;
    
    //RANDOM NUMBER & iD
    private int randomInteger;
    private String intersectioniD;
    
    // FILM IDs OF FILMS THIS ACTOR HAS BEEN IN
    public ArrayList<String> roadIDs = new ArrayList<String>();

    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public Intersection(int initX, int initY)
    {
        x = initX;
        y = initY;
        open = true;
        randomInteger = (int)(Math.random()*13);
        intersectioniD = "" + x + y + randomInteger;
        //System.out.println("Random Integer: " + randomInteger);
        //System.out.println("Intersection iD: " + intersectioniD);
    }
    
    //public int getId

    // ACCESSOR METHODS
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOpen() {
        return open;
    }

    public String getiD() {
        return intersectioniD;
    }
    
    public ArrayList<String> getRoadIDs() {
        return roadIDs;
    }
    
    public void addRoadID(String idToAdd)
    {
        roadIDs.add(idToAdd);
    }

    // MUTATOR METHODS
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setiD(String intersectioniD) {
        this.intersectioniD = intersectioniD;
    }

    /**
     * This toggles the intersection open/closed.
     */
    public void toggleOpen()
    {
        open = !open;
    }
    
    /**
     * Returns a textual representation of this intersection.
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
