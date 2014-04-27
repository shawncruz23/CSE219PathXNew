package pathx.data;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents a road in level graph, which means it's 
 * basically a graph edge.
 * 
 * @author Richard McKenna
 */
public class Road {
    
    // THESE ARE THE EDGE'S NODES
    Intersection node1;
    Intersection node2;
    
    // false IF IT'S TWO-WAY, true IF IT'S ONE WAY
    boolean oneWay;
    
    // ROAD SPEED LIMIT
    int speedLimit;
    
    //road iD
    String iD;
    
    // ACTOR IDs OF ACTORS WHO APPEARED IN THIS FILM
    private ArrayList<String> intersectionsIDs;
    
    public Road() {
        intersectionsIDs = new ArrayList<String>();
    }

    // ACCESSOR METHODS
    public Intersection getNode1()  {   return node1;       }
    public Intersection getNode2()  {   return node2;       }
    public String getiD() {
        return iD;
    }

    public ArrayList<String> getIntersectionIDs() {
        return intersectionsIDs;
    }
    public boolean isOneWay()       {   return oneWay;      }
    public int getSpeedLimit()      {   return speedLimit;  }
    
    // MUTATOR METHODS
    public void setNode1(Intersection node1) {
        this.node1 = node1;
        intersectionsIDs.add(node1.getiD());
    }

    public void setNode2(Intersection node2) {
        this.node2 = node2;
        intersectionsIDs.add(node2.getiD());
    }

    public void setiD(Intersection node1, Intersection node2) {
        iD = node1.getiD() + node2.getiD();
    }

    public void addActorID(String intersectionIDToAdd) {
        intersectionsIDs.add(intersectionIDToAdd);
    }
    public void setOneWay(boolean oneWay)       {   this.oneWay = oneWay;           }
    public void setSpeedLimit(int speedLimit)   {   this.speedLimit = speedLimit;   }

    /**
     * Builds and returns a textual representation of this road.
     */
    @Override
    public String toString()
    {
        return node1 + " - " + node2 + "(" + speedLimit + ":" + oneWay + ")";
    }
}
