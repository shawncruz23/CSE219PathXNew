/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;

import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static pathx.PathXConstants.*;
import pathx.data.Intersection;
import pathx.data.Level;
import pathx.data.PathXDataModel;
import pathx.data.Road;
import xml_utilities.XMLUtilities;

/**
 * This class serves as a does the reading and writing of levels
 * to and from XML files.
 * 
 * @author  Richard McKenna & Shawn Cruz
 */
public class PathX_XML_Level_IO {
    
     // THIS WILL HELP US PARSE THE XML FILES
    private XMLUtilities xmlUtil;
    
    // THIS IS THE SCHEMA WE'LL USE
    private File levelSchema;
    
    private ArrayList<Road> roadList;
    private ArrayList<Intersection> intersectionList;

    /**
     * Constructor for making our importer/exporter. Note that it
     * initializes the XML utility for processing XML files and it
     * sets up the schema for use.
     */
    public PathX_XML_Level_IO(File initLevelSchema)
    {
        // THIS KNOWS HOW TO READ AND ACCESS XML FILES
        xmlUtil = new XMLUtilities();
        
        // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
        levelSchema = initLevelSchema;
        
        roadList = new ArrayList<>();
        intersectionList = new ArrayList<>();
    }
    
    public static ArrayList<Level> levelList = new ArrayList<>();
    
    public ArrayList<Level> getLevelList() {
        return levelList;
    }
    
    public static Level level = new Level();

    public Level getLevel() {
        return level;
    }
    
    public ArrayList<Road> getRoadList() {
        return roadList;
    }
    
    public ArrayList<Intersection> getIntersectionList() {
        return intersectionList;
    }
    
    private String getLevelXMLHelper(int levelNumber) {
        String levelString = "";
        if (levelNumber == 1) {
            return LEVEL_1_XML;
        } else if (levelNumber == 2) {
            return LEVEL_2_XML;
        } else if (levelNumber == 3) {
            return LEVEL_3_XML;
        } else if (levelNumber == 4) {
            return LEVEL_4_XML;
        } else if (levelNumber == 5) {
            return LEVEL_5_XML;
        } else if (levelNumber == 6) {
            return LEVEL_6_XML;
        } else if (levelNumber == 7) {
            return LEVEL_7_XML;
        } else if (levelNumber == 8) {
            return LEVEL_8_XML;
        } else if (levelNumber == 9) {
            return LEVEL_9_XML;
        } else if (levelNumber == 10) {
            return LEVEL_10_XML;
        } else if (levelNumber == 11) {
            return LEVEL_11_XML;
        } else if (levelNumber == 12) {
            return LEVEL_12_XML;
        } else if (levelNumber == 13) {
            return LEVEL_13_XML;
        } else if (levelNumber == 14) {
            return LEVEL_14_XML;
        } else if (levelNumber == 15) {
            return LEVEL_15_XML;
        } else if (levelNumber == 16) {
            return LEVEL_16_XML;
        } else if (levelNumber == 17) {
            return LEVEL_17_XML;
        } else if (levelNumber == 18) {
            return LEVEL_18_XML;
        } else if (levelNumber == 19) {
            return LEVEL_19_XML;
        } else if (levelNumber == 20) {
            return LEVEL_20_XML;
        }
        return null;
    }
    
    private String getCurrentLevelXML(String levelDescription) {
        String levelXML = "";
        for (int i = 1; i <= 20; i++) {
            if (levelDescription.contains("LEVEL_" + i + "_")) {
                levelXML = getLevelXMLHelper(i);
                return levelXML;
            }
        }
        return null;
    }
    
    /**
     * Reads the level data found in levelFile into levelToLoad.
     */
    public Level loadLevel(String fileToLoad)
    {
        try
        {
            //if(fileToLoad.contains("LEVEL_1_")) {
                //String path = ""
            //}
            
            // WE'LL FILL IN SOME OF THE LEVEL OURSELVES
           // Level levelToLoad = model.getLevel();
            //levelToLoad.reset();
            
            String levelXML = getCurrentLevelXML(fileToLoad);
            
            // FIRST LOAD ALL THE XML INTO A TREE
            Document doc = xmlUtil.loadXMLDocument(LEVELS_PATH + levelXML, 
                                                    "./data/"+PROPERTIES_SCHEMA_LEVEL_FILE_NAME);
            
            Level levelLoaded = new Level();
            
            // FIRST LOAD THE LEVEL INFO
            Node levelNode = doc.getElementsByTagName(LEVEL_NODE).item(0);
            NamedNodeMap attributes = levelNode.getAttributes();
            String levelName = attributes.getNamedItem(NAME_ATT).getNodeValue();
            levelLoaded.setLevelName(levelName);
            String bgImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            levelLoaded.setBackgroundImageFileName(bgImageName);
            //model.updateBackgroundImage(bgImageName);

            // THEN LET'S LOAD THE LIST OF ALL THE REGIONS
            loadIntersectionsList(doc, levelLoaded);
            ArrayList<Intersection> intersections = levelLoaded.getIntersections();
            
            // AND NOW CONNECT ALL THE REGIONS TO EACH OTHER
            loadRoadsList(doc, levelLoaded);
            
            // LOAD THE START INTERSECTION
            Node startIntNode = doc.getElementsByTagName(START_INTERSECTION_NODE).item(0);
            attributes = startIntNode.getAttributes();
            String startIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int startId = Integer.parseInt(startIdText);
            String startImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            Intersection startingIntersection = intersections.get(startId);
            levelLoaded.setStartingLocation(startingIntersection);
            levelLoaded.setStartingLocationImageFileName(startImageName);
            //model.updateStartingLocationImage(startImageName); // update this method
            
            // LOAD THE DESTINATION
            Node destIntNode = doc.getElementsByTagName(DESTINATION_INTERSECTION_NODE).item(0);
            attributes = destIntNode.getAttributes();
            String destIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int destId = Integer.parseInt(destIdText);
            String destImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            levelLoaded.setDestination(intersections.get(destId));
            levelLoaded.setDestinationImageFileName(destImageName);
            //model.updateDestinationImage(destImageName); // update this method
            
            // LOAD THE MONEY
            Node moneyNode = doc.getElementsByTagName(MONEY_NODE).item(0);
            attributes = moneyNode.getAttributes();
            String moneyText = attributes.getNamedItem(AMOUNT_ATT).getNodeValue();
            int money = Integer.parseInt(moneyText);
            levelLoaded.setMoney(money);
            
            // LOAD THE NUMBER OF POLICE
            Node policeNode = doc.getElementsByTagName(POLICE_NODE).item(0);
            attributes = policeNode.getAttributes();
            String policeText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numPolice = Integer.parseInt(policeText);
            levelLoaded.setNumPolice(numPolice);
            
            // LOAD THE NUMBER OF BANDITS
            Node banditsNode = doc.getElementsByTagName(BANDITS_NODE).item(0);
            attributes = banditsNode.getAttributes();
            String banditsText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numBandits = Integer.parseInt(banditsText);
            levelLoaded.setNumBandits(numBandits);
            
            // LOAD THE NUMBER OF ZOMBIES
            Node zombiesNode = doc.getElementsByTagName(ZOMBIES_NODE).item(0);
            attributes = zombiesNode.getAttributes();
            String zombiesText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numZombies = Integer.parseInt(zombiesText);
            levelLoaded.setNumZombies(numZombies);    
            
//            System.out.println("Background Filename: " + levelLoaded.getBackgroundImageFileName());
//            System.out.println("Destination Filename: " + levelLoaded.getDestinationImageFileName());
//            System.out.println("Level Name: " + levelLoaded.getLevelName());
//            System.out.println("Starting Location Filename: " + levelLoaded.getStartingLocationImageFileName());
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
            
            //Level aLevel = new Level();
//            level.setDestination(destId);
//            level.setDestinationImageFileName();
//            level.setLevelName(levelName);
//            level.setMoney(money);
//            level.setNumBandits(numBandits);
//            level.setNumPolice(numPolice);
//            level.setNumZombies(numZombies);
//            level.setStartingLocation();
//            level.setStartingLocationImageFileName(startImageName);
//            levelList.add(aLevel);
            return levelLoaded;
        }
        catch(Exception e)
        {
            // LEVEL DIDN'T LOAD PROPERLY
            System.out.println(e);
            //return false;
        }
        // LEVEL LOADED PROPERLY
        return null;
    }
    
   
    
    // PRIVATE HELPER METHOD FOR LOADING INTERSECTIONS INTO OUR LEVEL
    private void loadIntersectionsList(Document doc, Level levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node intersectionsListNode = doc.getElementsByTagName(INTERSECTIONS_NODE).item(0);
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(intersectionsListNode, INTERSECTION_NODE);
        for (int i = 0; i < intersectionsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node intersectionNode = intersectionsList.get(i);
            NamedNodeMap intersectionAttributes = intersectionNode.getAttributes();
            String idText = intersectionAttributes.getNamedItem(ID_ATT).getNodeValue();
            String openText = intersectionAttributes.getNamedItem(OPEN_ATT).getNodeValue();
            String xText = intersectionAttributes.getNamedItem(X_ATT).getNodeValue();
            int x = Integer.parseInt(xText);
            String yText = intersectionAttributes.getNamedItem(Y_ATT).getNodeValue();
            int y = Integer.parseInt(yText);
            
            // NOW MAKE AND ADD THE INTERSECTION
            Intersection newIntersection = new Intersection(x, y);
            newIntersection.open = Boolean.parseBoolean(openText);
            intersections.add(newIntersection);
            //intersections.get(i).
            intersectionList.add(newIntersection);
        }
    }

    // PRIVATE HELPER METHOD FOR LOADING ROADS INTO OUR LEVEL
    private void loadRoadsList( Document doc, Level levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node roadsListNode = doc.getElementsByTagName(ROADS_NODE).item(0);
        ArrayList<Road> roads = levelToLoad.getRoads();
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_NODE);
        for (int i = 0; i < roadsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node roadNode = roadsList.get(i);
            NamedNodeMap roadAttributes = roadNode.getAttributes();
            String id1Text = roadAttributes.getNamedItem(INT_ID1_ATT).getNodeValue();
            int int_id1 = Integer.parseInt(id1Text);
            String id2Text = roadAttributes.getNamedItem(INT_ID2_ATT).getNodeValue();
            int int_id2 = Integer.parseInt(id2Text);
            String oneWayText = roadAttributes.getNamedItem(ONE_WAY_ATT).getNodeValue();
            boolean oneWay = Boolean.parseBoolean(oneWayText);
            String speedLimitText = roadAttributes.getNamedItem(SPEED_LIMIT_ATT).getNodeValue();
            int speedLimit = Integer.parseInt(speedLimitText);
            
            // NOW MAKE AND ADD THE ROAD
            Road newRoad = new Road();
            newRoad.setNode1(intersections.get(int_id1));
            newRoad.setNode2(intersections.get(int_id2));
            newRoad.setOneWay(oneWay);
            newRoad.setSpeedLimit(speedLimit);
            
            newRoad.setiD(intersections.get(int_id1), intersections.get(int_id2));
            intersections.get(int_id1).addRoadID(newRoad.getiD());
            intersections.get(int_id2).addRoadID(newRoad.getiD());
            
            roads.add(newRoad);
            
            roadList.add(newRoad);
        }
    }
    
    /**
     * This method saves the level currently being edited to the levelFile. Note
     * that it will be saved as an .xml file, which is an XML-format that will
     * conform to the schema.
     */
    public boolean saveLevel(File levelFile, Level levelToSave)
    {
        try 
        {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            
            // THEN THE LEVEL (i.e. THE ROOT) ELEMENT
            Element levelElement = doc.createElement(LEVEL_NODE);
            doc.createAttribute(NAME_ATT);
            levelElement.setAttribute(NAME_ATT, levelToSave.getLevelName());
            doc.appendChild(levelElement);
            doc.createAttribute(IMAGE_ATT);
            levelElement.setAttribute(IMAGE_ATT, levelToSave.getBackgroundImageFileName());
 
            // THEN THE INTERSECTIONS
            Element intersectionsElement = makeElement(doc, levelElement, INTERSECTIONS_NODE, "");
            
            // AND LET'S ADD EACH INTERSECTION
            int id = 0;
            doc.createAttribute(ID_ATT); 
            doc.createAttribute(X_ATT);
            doc.createAttribute(Y_ATT);
            doc.createAttribute(OPEN_ATT);
            for (Intersection i : levelToSave.getIntersections())
            {
                // MAKE AN INTERSECTION NODE AND ADD IT
                Element intersectionNodeElement = makeElement(doc, intersectionsElement,
                        INTERSECTION_NODE, "");
                
                // NOW LET'S FILL IN THE INTERSECTION'S DATA. FIRST MAKE THE ATTRIBUTES
                intersectionNodeElement.setAttribute(ID_ATT,    "" + id);
                intersectionNodeElement.setAttribute(X_ATT,     "" + i.x);
                intersectionNodeElement.setAttribute(Y_ATT,     "" + i.y);
                intersectionNodeElement.setAttribute(OPEN_ATT,  "" + i.open);
             }

            // AND NOW ADD ALL THE ROADS
            Element roadsElement = makeElement(doc, levelElement, ROADS_NODE, "");
            doc.createAttribute(INT_ID1_ATT);
            doc.createAttribute(INT_ID2_ATT);
            doc.createAttribute(SPEED_LIMIT_ATT);
            doc.createAttribute(ONE_WAY_ATT);
            ArrayList<Intersection> intersections = levelToSave.getIntersections();
            for (Road r : levelToSave.getRoads())
            {
                // MAKE A ROAD NODE AND ADD IT TO THE LIST
                Element roadNodeElement = makeElement(doc, roadsElement, ROAD_NODE, "");
                int intId1 = intersections.indexOf(r.getNode1());
                roadNodeElement.setAttribute(INT_ID1_ATT, "" + intId1);
                int intId2 = intersections.indexOf(r.getNode2());
                roadNodeElement.setAttribute(INT_ID2_ATT, "" + intId2);
                roadNodeElement.setAttribute(SPEED_LIMIT_ATT, "" + r.getSpeedLimit());
                roadNodeElement.setAttribute(ONE_WAY_ATT, "" + r.isOneWay());
            }
            
            // NOW THE START INTERSECTION
            Element startElement = makeElement(doc, levelElement, START_INTERSECTION_NODE, "");
            int startId = intersections.indexOf(levelToSave.getStartingLocation());
            startElement.setAttribute(ID_ATT, "" + startId);
            startElement.setAttribute(IMAGE_ATT, levelToSave.getStartingLocationImageFileName());
            
            // AND THE DESTINATION
            Element destElement = makeElement(doc, levelElement, DESTINATION_INTERSECTION_NODE, "");
            int destId = intersections.indexOf(levelToSave.getDestination());
            destElement.setAttribute(ID_ATT, "" + destId);
            destElement.setAttribute(IMAGE_ATT, levelToSave.getDestinationImageFileName());
            
            // NOW THE MONEY
            Element moneyElement = makeElement(doc, levelElement, MONEY_NODE, "");
            doc.createAttribute(AMOUNT_ATT);
            moneyElement.setAttribute(AMOUNT_ATT, "" + levelToSave.getMoney());
            
            // AND THE POLICE COUNT
            Element policeElement = makeElement(doc, levelElement, POLICE_NODE, "");
            doc.createAttribute(NUM_ATT);
            policeElement.setAttribute(NUM_ATT, "" + levelToSave.getNumPolice());
            
            // AND THE BANDIT COUNT
            Element banditElement = makeElement(doc, levelElement, BANDITS_NODE, "");
            banditElement.setAttribute(NUM_ATT, "" + levelToSave.getNumBandits());
            
            // AND FINALLY THE ZOMBIES COUNT
            Element zombiesElement = makeElement(doc, levelElement, ZOMBIES_NODE, "");
            zombiesElement.setAttribute(NUM_ATT, "" + levelToSave.getNumZombies());

            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(levelFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);    

            // SUCCESS
            return true;
        }
        catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG
            return false;
        }    
    }   
    
    // THIS HELPER METHOD BUILDS ELEMENTS (NODES) FOR US TO HELP WITH
    // BUILDING A Doc WHICH WE WOULD THEN SAVE TO A FILE.
    private Element makeElement(Document doc, Element parent, String elementName, String textContent)
    {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
    
    /**
     * This method does the same thing as the other path finding
     * algorithm, except this one finds the optimal path. The easiest 
     * way to do this is with a breadth first search algorithm.
     */
//    public ArrayList<Intersection> findShortestPath(Intersection intersection1, Intersection intersection2)
//    {
//	// WE'LL MAINTAIN A SHORTEST PATH FROM THE
//        // STARTING ACTOR TO EACH ACTOR WE ENCOUNTER
//        TreeMap<String, ArrayList<Intersection>> shortestPaths;
//        shortestPaths = new TreeMap();
//
//        // THIS WILL STORE THE PATH WE ARE CURRENTLY
//        // BUILDING UPON
//        ArrayList<Intersection> currentPath;
//
//	// WE ARE USING A BREADTH FIRST SEARCH, AND
//        // WE'LL ONLY CHECK EACH Actor AND Film ONCE
//        // WE ARE USING 2 DATA STRUCTURES FOR EACH
//        // BECAUSE WE WILL USE ONE AS A LIST OF 
//        // ITEMS TO CHECK IN ORDER, AND ANOTHER
//        // FOR FAST SEARCHING
//        ArrayList<Actor> actorsVisited = new ArrayList();
//        TreeMap<String, Actor> actorsVisitedFast = new TreeMap();
//        ArrayList<Film> filmsVisited = new ArrayList();
//        TreeMap<String, Film> filmsVisitedFast = new TreeMap();
//
//        // INDEX OF Actors AND Films TO CHECK
//        int actorIndex = 0;
//        int filmIndex = 0;
//
//	// THE SHORTEST PATH FROM THE START ACTOR
//        // TO THE START ACTOR IS NOTHING, SO WE'll
//        // START OUT WITH AN EMPTY ArrayList
//        actorsVisited.add(actor);
//        actorsVisitedFast.put(actor.getId(), actor);
//        shortestPaths.put(actor.getId(), new ArrayList<Connection>());
//
//	// GO THROUGH ALL THE ACTORS WE HAVE REACHED
//        // NEVER RE-VISITING AN ACTOR
//        while (actorIndex < actorsVisited.size())
//        {
//            // FIRST GET ALL THE MOVIES FOR THE
//            // ACTOR AT THE actorIndex
//            Actor currentActor = actorsVisited.get(actorIndex);
//
//            // MAKE THE SHORTEST PATH FOR THE CURRENT
//            // ACTOR THE CURRENT PATH, SINCE WE WILL
//            // BUILD ON IT
//            currentPath = shortestPaths.get(currentActor.getId());
//
//            Iterator<String> itFilmIDs = currentActor.getFilmIDs().iterator();
//            while (itFilmIDs.hasNext())
//            {
//                String filmID = itFilmIDs.next();
//                Film film = films.get(filmID);
//                if (!filmsVisitedFast.containsKey(filmID))
//                {
//                    filmsVisited.add(film);
//                    filmsVisitedFast.put(film.getId(), film);
//                }
//            }
//
//            while (filmIndex < filmsVisited.size())
//            {
//		// NOW GO THROUGH THE FILMS AND GET
//                // ALL THE ACTORS WHO WERE IN THOSE
//                // FILMS, DO NOT GET ACTORS ALREADY
//                // VISITED
//                Film currentFilm = filmsVisited.get(filmIndex);
//                Iterator<String> itActorIDs = currentFilm.getActorIDs().iterator();
//                while (itActorIDs.hasNext())
//                {
//                    String actorID = itActorIDs.next();
//                    Actor actorToTest = actors.get(actorID);
//                    if (!actorsVisitedFast.containsKey(actorID))
//                    {
//                        actorsVisited.add(actorToTest);
//                        actorsVisitedFast.put(actorID, actorToTest);
//                        ArrayList<Connection> actorPath;
//                        actorPath = (ArrayList<Connection>) currentPath.clone();
//                        Connection c = new Connection(currentFilm.getId(),
//                                currentActor.getId(),
//                                actorToTest.getId());
//                        actorPath.add(c);
//                        shortestPaths.put(actorID, actorPath);
//
//                        // IF THIS IS KEVIN BACON WE'RE DONE
//                        if (actorID.equals(kevinBacon.getId()))
//                        {
//                            return actorPath;
//                        }
//                    }
//                }
//                filmIndex++;
//            }
//            actorIndex++;
//        }
//        return new ArrayList();
//    }
}
