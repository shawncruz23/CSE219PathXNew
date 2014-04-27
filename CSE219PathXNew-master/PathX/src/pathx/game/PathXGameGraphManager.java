/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import pathx.data.*;

/**
 *
 * @author ShawnCruz
 */
public class PathXGameGraphManager {
    
    
     // THIS WILL STORE ALL OF OUR FILM DATA
    private TreeMap<String, Road> roads;

    // THIS WILL STORE ALL OF OUR ACTOR DATA
    private TreeMap<String, Intersection> intersections;
    private ArrayList<String> intersectionIDs;
    
     /**
     * This constructor initializes the graph's data structures so
     * that the file data can be properly loaded.
     */
    public PathXGameGraphManager()
    {
        // CONSTRUCT OUR GRAPH DATA STRUCTURES
        roads = new TreeMap();
        intersections = new TreeMap();        
        intersectionIDs = new ArrayList();
    }
    
     /**
     * Adds an actor to the graph.
     */
    public void addIntersections(Intersection intersectionToAdd)
    {
        intersections.put(intersectionToAdd.getiD(), intersectionToAdd);
        intersectionIDs.add(intersectionToAdd.getiD());
    }
    
    /**
     * Adds a film to the graph.
     */
    public void addRoad(Road roadToAdd)
    {
        roads.put(roadToAdd.getiD(), roadToAdd);
    }
    
    /**
     * This method does the same thing as the other path finding
     * algorithm, except this one finds the optimal path. The easiest 
     * way to do this is with a breadth first search algorithm.
     */
    public ArrayList<Connection> findShortestPathToHome(Intersection intersection1, Intersection intersection2)
    {
	// WE'LL MAINTAIN A SHORTEST PATH FROM THE
        // STARTING ACTOR TO EACH ACTOR WE ENCOUNTER
        TreeMap<String, ArrayList<Connection>> shortestPaths;
        shortestPaths = new TreeMap();

        // THIS WILL STORE THE PATH WE ARE CURRENTLY
        // BUILDING UPON
        ArrayList<Connection> currentPath;

	// WE ARE USING A BREADTH FIRST SEARCH, AND
        // WE'LL ONLY CHECK EACH Actor AND Film ONCE
        // WE ARE USING 2 DATA STRUCTURES FOR EACH
        // BECAUSE WE WILL USE ONE AS A LIST OF 
        // ITEMS TO CHECK IN ORDER, AND ANOTHER
        // FOR FAST SEARCHING
        ArrayList<Intersection> intersectionsVisited = new ArrayList();
        TreeMap<String, Intersection> intersectionsVisitedFast = new TreeMap();
        ArrayList<Road> roadsVisited = new ArrayList();
        TreeMap<String, Road> roadsVisitedFast = new TreeMap();

        // INDEX OF Actors AND Films TO CHECK
        int intersectionIndex = 0;
        int roadIndex = 0;

	// THE SHORTEST PATH FROM THE START ACTOR
        // TO THE START ACTOR IS NOTHING, SO WE'll
        // START OUT WITH AN EMPTY ArrayList
        intersectionsVisited.add(intersection1);
        intersectionsVisitedFast.put(intersection1.getiD(), intersection1);
        shortestPaths.put(intersection1.getiD(), new ArrayList<>());

	// GO THROUGH ALL THE ACTORS WE HAVE REACHED
        // NEVER RE-VISITING AN ACTOR
        while (intersectionIndex < intersectionsVisited.size())
        {
            // FIRST GET ALL THE MOVIES FOR THE
            // ACTOR AT THE intersectionIndex
            Intersection currentIntersection = intersectionsVisited.get(intersectionIndex);

            // MAKE THE SHORTEST PATH FOR THE CURRENT
            // ACTOR THE CURRENT PATH, SINCE WE WILL
            // BUILD ON IT
            currentPath = shortestPaths.get(currentIntersection.getiD());

            Iterator<String> itRoadIDs = currentIntersection.getRoadIDs().iterator();
            while (itRoadIDs.hasNext())
            {
                String roadID = itRoadIDs.next();
                Road road = roads.get(roadID);
                if (!roadsVisitedFast.containsKey(roadID))
                {
                    roadsVisited.add(road);
                    roadsVisitedFast.put(road.getiD(), road);
                    
                }
            }

            while (roadIndex < roadsVisited.size())
            {
		// NOW GO THROUGH THE FILMS AND GET
                // ALL THE ACTORS WHO WERE IN THOSE
                // FILMS, DO NOT GET ACTORS ALREADY
                // VISITED
                Road currentRoad = roadsVisited.get(roadIndex);
                Iterator<String> itIntersectionIDs = currentRoad.getIntersectionIDs().iterator();
                while (itIntersectionIDs.hasNext())
                {
                    String intersectionID = itIntersectionIDs.next();
                    Intersection intersectionToTest = intersections.get(intersectionID);
                    if (!intersectionsVisitedFast.containsKey(intersectionID))
                    {
                        intersectionsVisited.add(intersectionToTest);
                        intersectionsVisitedFast.put(intersectionID, intersectionToTest);
                        ArrayList<Connection> actorPath;
                        actorPath = (ArrayList<Connection>) currentPath.clone();
                        Connection c = new Connection(currentIntersection.getiD(),
                                currentRoad.getiD(),
                                intersectionToTest.getiD());
                        actorPath.add(c);
                        shortestPaths.put(intersectionID, actorPath);

                        // IF THIS IS KEVIN BACON WE'RE DONE
                        if (intersectionID.equals(intersection2.getiD()))
                        {
                            return actorPath;
                        }
                    }
                }
                roadIndex++;
            }
            intersectionIndex++;
        }
        return new ArrayList();
    }
    
}
