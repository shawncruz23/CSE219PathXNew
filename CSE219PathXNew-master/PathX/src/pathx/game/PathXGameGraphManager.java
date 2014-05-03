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

    private TreeMap<String, ArrayList<Connection>> allGamePaths;
    private ArrayList<ArrayList<Connection>> arrayListGamePath;

    /**
     * This constructor initializes the graph's data structures so that the file
     * data can be properly loaded.
     */
    public PathXGameGraphManager() {
        // CONSTRUCT OUR GRAPH DATA STRUCTURES
        roads = new TreeMap();
        intersections = new TreeMap();
        intersectionIDs = new ArrayList();
        allGamePaths = new TreeMap();
        arrayListGamePath = new ArrayList();
    }

    /**
     * Adds an intersection to the graph.
     */
    public void addIntersections(Intersection intersectionToAdd) {
        intersections.put(intersectionToAdd.getiD(), intersectionToAdd);
        intersectionIDs.add(intersectionToAdd.getiD());
    }

    /**
     * Adds a road to the graph.
     */
    public void addRoad(Road roadToAdd) {
        roads.put(roadToAdd.getiD(), roadToAdd);
    }

    public Intersection getIntersection(String intersectionID) {
        return intersections.get(intersectionID);
    }

    public Road getRoad(String roadID) {
        return roads.get(roadID);
    }

    public void printAllGamePaths() {
        Iterator i = allGamePaths.values().iterator();
        while (i.hasNext()) {
            ArrayList<Connection> c = (ArrayList<Connection>) i.next();
            for (Connection aConnection : c) {
                System.out.println("Path: " + intersections.get(aConnection.getIntersection1Id()).toString()
                        + " " + intersections.get(aConnection.getIntersection2Id()).toString());
            }
        }
    }

    public void printArrayListGamePaths() {
        Iterator i = arrayListGamePath.iterator();
        int k = 0;
        System.out.println("printArrayListGamePaths CALLED HERE");
        while (i.hasNext()) {
            ArrayList<Connection> c = (ArrayList<Connection>) i.next();
            for (Connection aConnection : c) {
                System.out.println("Path " + (++k) + ": " + intersections.get(aConnection.getIntersection1Id()).toString()
                        + " " + intersections.get(aConnection.getIntersection2Id()).toString());
            }
        }
        System.out.println("printArrayListGamePaths END HERE");
    }

    /**
     * This method examines the Actor corresponding to the provided
     * intersectionID and build return a ArrayList with a constructed Connection
     * object for each intersection connected by one degree to the intersection
     * referenced in the argument.
     */
    public ArrayList<Connection> getAllNeighbors(String intersectionID) {
        ArrayList<Connection> connections = new ArrayList();
        Intersection intersection = intersections.get(intersectionID);
        for (int i = 0; i < intersection.getRoadIDs().size(); i++) {
            String roadID = intersection.getRoadIDs().get(i);
            Road road = roads.get(roadID);
            for (int j = 0; j < road.getIntersectionIDs().size(); j++) {
                String intersectionID2 = road.getIntersectionIDs().get(j);
                if (!intersectionID2.equals(intersectionID)) {
                    Connection connection;
                    connection = new Connection(intersectionID, roadID, intersectionID2);
                    connections.add(connection);
                }
            }
        }
        return connections;
    }

    /**
     * This method does the same thing as the other path finding algorithm,
     * except this one finds the optimal path. The easiest way to do this is
     * with a breadth first search algorithm.
     */
    public ArrayList<Connection> findShortestPathToHome(Intersection intersection1, Intersection intersection2) {
//        Iterator i = intersectionIDs.iterator();
//        int k = 0;
//        while (i.hasNext()) {
//            //i.next();
//            System.out.println("INTERSECTION(" + (++k) + "): " + intersections.get(i.next()).toString());
//        }

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

        //Iterator iterate = intersections.values().iterator();
        //while(iterate.hasNext()) {
        // GO THROUGH ALL THE ACTORS WE HAVE REACHED
        // NEVER RE-VISITING AN ACTOR
        while (intersectionIndex < intersectionsVisited.size()) {
            // FIRST GET ALL THE MOVIES FOR THE
            // ACTOR AT THE intersectionIndex
            Intersection currentIntersection = intersectionsVisited.get(intersectionIndex);

            // MAKE THE SHORTEST PATH FOR THE CURRENT
            // ACTOR THE CURRENT PATH, SINCE WE WILL
            // BUILD ON IT
            currentPath = shortestPaths.get(currentIntersection.getiD());

            Iterator<String> itRoadIDs = currentIntersection.getRoadIDs().iterator();
            while (itRoadIDs.hasNext()) {
                String roadID = itRoadIDs.next();
                Road road = roads.get(roadID);
                if (!roadsVisitedFast.containsKey(roadID)) {
                    roadsVisited.add(road);
                    roadsVisitedFast.put(road.getiD(), road);

                }
            }

            while (roadIndex < roadsVisited.size()) {
                // NOW GO THROUGH THE ROADS AND GET
                // ALL THE INTERSECTIONS WHO WERE IN THOSE
                // ROADS, DO NOT GET INTERSECTIONS ALREADY
                // VISITED
                Road currentRoad = roadsVisited.get(roadIndex);
                Iterator<String> itIntersectionIDs = currentRoad.getIntersectionIDs().iterator();
                while (itIntersectionIDs.hasNext()) {
                    String intersectionID = itIntersectionIDs.next();
                    Intersection intersectionToTest = intersections.get(intersectionID);
                    if (!intersectionsVisitedFast.containsKey(intersectionID)) {
                        intersectionsVisited.add(intersectionToTest);
                        intersectionsVisitedFast.put(intersectionID, intersectionToTest);
                        ArrayList<Connection> intersectionPath;
                        intersectionPath = (ArrayList<Connection>) currentPath.clone();
                        Connection c = new Connection(currentIntersection.getiD(),
                                currentRoad.getiD(),
                                intersectionToTest.getiD());

                        intersectionPath.add(c);
                        shortestPaths.put(intersectionID, intersectionPath);

                        allGamePaths.put(currentRoad.getiD(), intersectionPath);

                        // IF THIS IS KEVIN BACON WE'RE DONE
                        if (intersectionID.equals(intersection2.getiD())) {
                            return intersectionPath;
                        }
                    }
                }
                roadIndex++;
            }
            intersectionIndex++;
        }
        return new ArrayList();
    }

    public ArrayList<Connection> someMethod(Intersection intersection1, Intersection intersection2) {

        Iterator i = intersectionIDs.iterator();

        ArrayList<Connection> finalPath = new ArrayList<>();

        while (i.hasNext()) {
            intersection1 = (Intersection) intersections.get(i.next());
            ArrayList<Connection> testNeightbor = getAllNeighbors(intersection1.getiD());

            for (int g = 0; g < testNeightbor.size(); g++) {
                ArrayList<Connection> temp = shortestPathHelper(intersections.get(testNeightbor.get(g).getIntersection1Id()), intersection2);
                for (int z = 0; z < temp.size(); z++) {
                    finalPath.add(temp.get(z));
                }
            }
        }
        return finalPath;
    }

    public ArrayList<Connection> findAPathToHome(Intersection intersection1, Intersection intersection2) {

        Iterator i = intersectionIDs.iterator();

        ArrayList<Connection> finalPath = new ArrayList<>();

        while (i.hasNext()) {
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

            intersection1 = (Intersection) intersections.get(i.next());
            ArrayList<Connection> testNeightbor = getAllNeighbors(intersection1.getiD());

            for (int g = 0; g < testNeightbor.size(); g++) {
                ArrayList<Connection> temp = shortestPathHelper(intersections.get(testNeightbor.get(g).getIntersection1Id()), intersection2);
                for (int z = 0; z < temp.size(); z++) {
                    finalPath.add(temp.get(z));
                }
            }
            // THE SHORTEST PATH FROM THE START ACTOR
            // TO THE START ACTOR IS NOTHING, SO WE'll
            // START OUT WITH AN EMPTY ArrayList
            intersectionsVisited.add(intersection1);
            intersectionsVisitedFast.put(intersection1.getiD(), intersection1);
            shortestPaths.put(intersection1.getiD(), new ArrayList<>());

        //Iterator iterate = intersections.values().iterator();
            //while(i.hasNext()) {
            // GO THROUGH ALL THE ACTORS WE HAVE REACHED
            // NEVER RE-VISITING AN ACTOR
            while (intersectionIndex < intersectionsVisited.size()) {
                // FIRST GET ALL THE MOVIES FOR THE
                // ACTOR AT THE intersectionIndex
                Intersection currentIntersection = intersectionsVisited.get(intersectionIndex);

                // MAKE THE SHORTEST PATH FOR THE CURRENT
                // ACTOR THE CURRENT PATH, SINCE WE WILL
                // BUILD ON IT
                currentPath = shortestPaths.get(currentIntersection.getiD());

                Iterator<String> itRoadIDs = currentIntersection.getRoadIDs().iterator();
                while (itRoadIDs.hasNext()) {
                    String roadID = itRoadIDs.next();
                    Road road = roads.get(roadID);
                    if (!roadsVisitedFast.containsKey(roadID)) {
                        roadsVisited.add(road);
                        roadsVisitedFast.put(road.getiD(), road);

                    }
                }

                while (roadIndex < roadsVisited.size()) {
                    // NOW GO THROUGH THE ROADS AND GET
                    // ALL THE INTERSECTIONS WHO WERE IN THOSE
                    // ROADS, DO NOT GET INTERSECTIONS ALREADY
                    // VISITED
                    Road currentRoad = roadsVisited.get(roadIndex);
                    Iterator<String> itIntersectionIDs = currentRoad.getIntersectionIDs().iterator();
                    while (itIntersectionIDs.hasNext()) {
                        String intersectionID = itIntersectionIDs.next();
                        Intersection intersectionToTest = intersections.get(intersectionID);
                        if (!intersectionsVisitedFast.containsKey(intersectionID)) {
                            intersectionsVisited.add(intersectionToTest);
                            intersectionsVisitedFast.put(intersectionID, intersectionToTest);
                            ArrayList<Connection> intersectionPath;
                            intersectionPath = (ArrayList<Connection>) currentPath.clone();
                            Connection c = new Connection(currentIntersection.getiD(),
                                    currentRoad.getiD(),
                                    intersectionToTest.getiD());

                            intersectionPath.add(c);
                            finalPath.add(c);
                            shortestPaths.put(intersectionID, intersectionPath);

                            allGamePaths.put(currentRoad.getiD(), intersectionPath);

                            arrayListGamePath.add(intersectionPath);
                            // IF THIS IS KEVIN BACON WE'RE DONE
                            // if (intersectionID.equals(intersection2.getiD()))
                            {
                                //    return intersectionPath;
                            }
                        }
                    }
                    roadIndex++;
                }
                intersectionIndex++;
            }

        }
        return finalPath;
    }

    public static int counter = 0;

    private ArrayList<Connection> shortestPathHelper(Intersection intersection1, Intersection intersection2) {

        ArrayList<Connection> finalPath;
        // WE'LL MAINTAIN A SHORTEST PATH FROM THE
        // STARTING ACTOR TO EACH ACTOR WE ENCOUNTER
        TreeMap<String, ArrayList<Connection>> shortestPaths;
        shortestPaths = new TreeMap();

        if (counter == 0) {
            finalPath = new ArrayList<>();
            ++counter;
        }

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

        //Iterator iterate = intersections.values().iterator();
        //while(iterate.hasNext()) {
        // GO THROUGH ALL THE ACTORS WE HAVE REACHED
        // NEVER RE-VISITING AN ACTOR
        while (intersectionIndex < intersectionsVisited.size()) {
            // FIRST GET ALL THE MOVIES FOR THE
            // ACTOR AT THE intersectionIndex
            Intersection currentIntersection = intersectionsVisited.get(intersectionIndex);

            // MAKE THE SHORTEST PATH FOR THE CURRENT
            // ACTOR THE CURRENT PATH, SINCE WE WILL
            // BUILD ON IT
            currentPath = shortestPaths.get(currentIntersection.getiD());

            Iterator<String> itRoadIDs = currentIntersection.getRoadIDs().iterator();
            while (itRoadIDs.hasNext()) {
                String roadID = itRoadIDs.next();
                Road road = roads.get(roadID);
                if (!roadsVisitedFast.containsKey(roadID)) {
                    roadsVisited.add(road);
                    roadsVisitedFast.put(road.getiD(), road);

                }
            }

            while (roadIndex < roadsVisited.size()) {
                // NOW GO THROUGH THE ROADS AND GET
                // ALL THE INTERSECTIONS WHO WERE IN THOSE
                // ROADS, DO NOT GET INTERSECTIONS ALREADY
                // VISITED
                Road currentRoad = roadsVisited.get(roadIndex);
                Iterator<String> itIntersectionIDs = currentRoad.getIntersectionIDs().iterator();
                while (itIntersectionIDs.hasNext()) {
                    String intersectionID = itIntersectionIDs.next();
                    Intersection intersectionToTest = intersections.get(intersectionID);
                    if (!intersectionsVisitedFast.containsKey(intersectionID)) {
                        intersectionsVisited.add(intersectionToTest);
                        intersectionsVisitedFast.put(intersectionID, intersectionToTest);
                        ArrayList<Connection> intersectionPath;
                        intersectionPath = (ArrayList<Connection>) currentPath.clone();
                        Connection c = new Connection(currentIntersection.getiD(),
                                currentRoad.getiD(),
                                intersectionToTest.getiD());

                        intersectionPath.add(c);
                        shortestPaths.put(intersectionID, intersectionPath);
                        arrayListGamePath.add(intersectionPath);
                        allGamePaths.put(currentRoad.getiD(), intersectionPath);

                        // IF THIS IS KEVIN BACON WE'RE DONE
                        //if (intersectionID.equals(intersection2.getiD())) {
                        //  return intersectionPath;
                        //}
                    }
                }
                roadIndex++;
            }
            intersectionIndex++;
        }
        return new ArrayList();
    }

}
