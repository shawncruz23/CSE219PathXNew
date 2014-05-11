/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.data;

import java.util.*;

/**
 * This class keeps track of the edges in a path through the actor-film graph.
 * This is useful for finding shortest paths through the graph.
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class Connection implements Comparable, Comparator {

    // A CONNECTION HAS TWO ACTOR NODES AND A FILM NODE
    private String intersection1Id;
    private String roadId;
    private String intersection2Id;
    
    /**
     * During gameplay we'll know the first actor and film before
     * we'll know the second actor, so we'll only initialize the
     * first two with this constructor.
     * 
     * @param initActor1Id The id of the first actor in the connection.
     * @param initFilmId The film used to connect the two actors.
     */
    public Connection(String initIntersection1Id, String initRoadId)
    {
        // KEEP THESE FOR LATER
        intersection1Id = initIntersection1Id;
        roadId = initRoadId;
        
        // BE CAFEUL WITH THIS
        intersection2Id = null;
    }

    /**
     * Used for initializing a connection when all three nodes are
     * known values.
     * 
     * @param initActor1Id The first actor in the connection.
     * @param initFilmId The film connecting the two actors.
     * @param initActor2Id The second actor in the connection.
     */
    public Connection(  String initIntersection1Id,
                        String initRoadId,
                        String initIntersection2Id)
    {
        // USE THE OTHER CONSTRUCTOR TO INIT THE FIRST TWO THINGS
        this(initIntersection1Id, initRoadId);
        
        // AND INIT THE SECOND ACTOR
        intersection2Id = initIntersection2Id;
    }

    // ACCESSOR METHODS

    public String getRoadId()   {   return roadId;      }
    public String getIntersection1Id() {   return intersection1Id;    }
    public String getIntersection2Id() {   return intersection2Id;    }
    
    // MUTATOR METHODS
    
    public void setRoadId(String initRoadId)
    {
        roadId = initRoadId;
    }
    
    public void setIntersection2Id(String initIntersection2Id)
    {
        intersection2Id = initIntersection2Id;
    }

    // METHODS FOR TESTING CERTAIN CONDITIONS
    
    /**
     * Tests to see if this connection knows the second actor or not.
     * 
     * @return true if both actors are known, false otherwise.
     */
    public boolean hasTwoIntersections()
    {
        return (intersection2Id != null);
    }
    
    /**
     * Tests to see if the testActorId argument is one of the actors
     * part of this connection.
     * 
     * @param testActorId The actor to test.
     * 
     * @return true if that actor is one of the two in this connection,
     * false otherwise.
     */
    public boolean hasIntersection(String testIntersectionId)
    {
        if (intersection1Id.equals(testIntersectionId))       {   return true;    } 
        else if (intersection2Id == null)              {   return false;   }
        else if (intersection2Id.equals(testIntersectionId))  {   return true;    } 
        else                                    {   return false;   }
    }
    
    /**
     * Tests to see if the testFilmId argument is part of this connection.
     * 
     * @param testFilmId The film to test.
     * 
     * @return true if the film is part of the connection, false otherwise.
     */
    public boolean hasRoad(String testRoadId)
    {
        return testRoadId.equals(roadId);
    }

    /**
     * Used for comparing Connection objects for the purpose
     * of sorting.
     * 
     * @param obj Another Connection to compare to.
     * 
     * @return 0 if obj is equivalent to this one, meaning it has
     * the same actors (in same order) and film. Return 1 if
     * this connection is before obj, otherwise -1.
     */
    @Override
    public int compareTo(Object obj)
    {
        if (equals(obj))
        {
            return 0;
        } else
        {
            Connection otherConnection = (Connection) obj;
            return (roadId + intersection1Id + intersection2Id)
                    .compareTo(otherConnection.roadId
                            + otherConnection.intersection1Id
                            + otherConnection.intersection2Id);
        }
    }

    /**
     * Tests to see if this Connection is equivalent to obj.
     * @param obj Another Connection to use in this comparison.
     * @return true if they have the same actors and film, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        Connection otherConnection = (Connection) obj;
        boolean sameActors;
        sameActors = ((intersection1Id.equals(otherConnection.intersection1Id)
                && intersection2Id.equals(otherConnection.intersection2Id))
                || (intersection1Id.equals(otherConnection.intersection2Id)
                && intersection2Id.equals(otherConnection.intersection1Id)));
        return sameActors && (roadId.equals(otherConnection.roadId));
    }

    /**
     * Used for comparator comparisions for the purpose of searching
     * or sorting.
     * 
     * @param arg0 The first Connection in the comparison.
     * @param arg1 The second Connection in the comparison.
     * @return Works the same as compareTo, making arg0 this and
     * arg1 the obj argument.
     */
    @Override
    public int compare(Object arg0, Object arg1)
    {
        Connection conn0 = (Connection) arg0;
        Connection conn1 = (Connection) arg1;
        return conn0.compareTo(conn1);
    }
    
    /**
     * Creates and returns a textual representation of this connection, which is
     * represented by the three ids.
     * 
     * @return the three IMDB ids strung together.
     */
    @Override
    public String toString()
    {
        return intersection1Id + " " + roadId + " " +  intersection2Id;
    }
}
