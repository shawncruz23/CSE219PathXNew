/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.car.BanditCar;
import pathx.car.Car;
import pathx.car.PlayerCar;
import pathx.car.PoliceCar;
import pathx.car.ZombieCar;
import pathx.game.PathXGameGraphManager;
import pathx.ui.PathXCar;
import pathx.ui.PathXCarState;
import pathx.ui.PathXEventHandler;
import static pathx.ui.PathXEventHandler.xCoordinates;
import pathx.ui.PathXMiniGame;
import static pathx.ui.PathXMiniGame.isGameStarted;
import pathx.ui.PathXPanel;
import properties_manager.PropertiesManager;

/**
 * This class manages the game data for PathX
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXDataModel extends MiniGameDataModel {
    
     // THIS IS THE LEVEL CURRENTLY BEING EDITING
    Level level;
    
    // USED TO MANAGE WHAT THE USER IS CURRENTLY EDITING
    //PXLE_EditMode editMode;

    // DATA FOR RENDERING
    pathx.data.Viewport viewport;

    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    Image backgroundImage;
    Image startingLocationImage;
    Image destinationImage;

    // THE SELECTED INTERSECTION OR ROAD MIGHT BE EDITED OR DELETED
    // AND IS RENDERED DIFFERENTLY
    Intersection selectedIntersection;
    Road selectedRoad;
    
    // WE'LL USE THIS WHEN WE'RE ADDING A NEW ROAD
    Intersection startRoadIntersection;

    // IN CASE WE WANT TO TRACK MOVEMENTS
    int lastMouseX;
    int lastMouseY;    
    
    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;

    // THIS IS THE UI, WE'LL NOTIFY IT WHENEVER THE DATA CHANGES SO
    // THAT THE UI RENDERING CAN BE UPDATED AT THAT TIME
   // PXLE_View view;
    

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THIS STORES THE CARS ON THE GRID DURING THE GAME
    private ArrayList<PathXCar> tilesToSort;
   
     // THESE ARE THE TILES STACKED AT THE START OF THE GAME
    private ArrayList<PoliceCar> policeCarStack;
    private ArrayList<ZombieCar> zombieCarStack;
    private ArrayList<BanditCar> banditCarStack;
    
    private ArrayList<PlayerCar> playerCar;
   

    // THE LEGAL TILES IN ORDER FROM LOW SORT INDEX TO HIGH
   // private ArrayList<SnakeCell> snake;

    // GAME GRID AND TILE DATA
    private int gameTileWidth;
    private int gameTileHeight;
    private int numGameGridColumns;
    private int numGameGridRows;
    private String TTT ;

    // THESE ARE THE CARS STACKED AT THE START OF THE GAME
    private ArrayList<PathXCar> stackCars;
    private int stackCarsX;
    private int stackCarsY;

    // THESE ARE THE CARS THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<PathXCar> movingCars;

    // THIS IS THE CAR THE USER IS DRAGGING
    private PathXCar selectedCar; 
    private int selectedCarIndex;

    // THIS IS THE TEMP CAR
    private PathXCar tempCar;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // CURRENT LEVEL USER IS ON
    private Level currentLevel;


    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
    private ArrayList<CarMotionTransaction> properTransactionOrder;
    private int transactionCounter;
  //  private boolean isUndo;
    
    private PathXCarState editMode;
  
    private int mousePressX;
    private int mousePressY;
    
    // THESE ARE THE POLICE THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<PoliceCar> movingPolice;
    // THESE ARE THE ZOMBIES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<ZombieCar> movingZombies;
    // THESE ARE THE BANDITS THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<BanditCar> movingBandits;
    
    private ArrayList<PlayerCar> movingPlayerCar;
    
    private int playerBalance;
    
    private int levelBounty;
    
 
    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent on file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The Sorting Hat game UI.
     */
    public PathXDataModel(MiniGame initMiniGame) {
        
        playerBalance = 0;
        levelBounty = 0;
        
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;

        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        policeCarStack = new ArrayList();
        movingPolice = new ArrayList();
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        zombieCarStack = new ArrayList();
        movingZombies = new ArrayList();
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        banditCarStack = new ArrayList();
        movingBandits = new ArrayList();
      
        playerCar = new ArrayList();
   
       
        movingPlayerCar = new ArrayList();
        
       TTT= new String();

        // NOTHING IS BEING DRAGGED YET
        selectedCar = null;
        selectedCarIndex = -1;
        tempCar = null;
        
        editMode = PathXCarState.NOTHING_SELECTED;
        
        viewport = new Viewport();
    }

    // ACCESSOR METHODS
   // public ArrayList<SnakeCell> getSnake()
    {
  //      return snake;
    }
//public String getAlgorithmName()
    {
     //   return sortingAlgorithm.name;
    }
    
    public int getPlayerBalance() {
        return playerBalance;
    }
    
     public int getLevelBounty() {
        return levelBounty;
    }
    
    public ArrayList<PoliceCar> getPoliceCarStack(){
        return movingPolice;
    }
    
    public ArrayList<ZombieCar> getZombieCarStack(){
        return movingZombies;
    }
    
    public ArrayList<BanditCar> getBanditCarStack(){
        return movingBandits;
    }

    public ArrayList<PlayerCar> getPlayerCarStack(){
        return movingPlayerCar;
    }
    
    public ArrayList<PlayerCar> getPlayerCar(){
        return playerCar;
    }
    
    public int getGameTileWidth() { 
        return gameTileWidth; 
    }

    public int getGameTileHeight() {
        return gameTileHeight;
    }

    public int getNumGameGridColumns() {
        return numGameGridColumns;
    }

    public int getNumGameGridRows() {
        return numGameGridRows;
    }

    public PathXCar getSelectedCar() {
        return selectedCar;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public Iterator<PathXCar> getMovingTiles() {
        return movingCars.iterator();
    }

    // MUTATOR METHODS
    public void setCurrentLevel(Level initCurrentLevel) {
        currentLevel = initCurrentLevel;
    }

    // INIT METHODS - AFTER CONSTRUCTION, THESE METHODS SETUP A GAME FOR USE
    // - initLevel
    // - initTiles
    // - initTile
    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     */
    public void initLevel (String levelName) {
      
        // UPDATE THE VIEWPORT IF WE ARE SCROLLING (WHICH WE'RE NOT)
        viewport.updateViewportBoundaries();

        // INITIALIZE THE PLAYER RECORD IF NECESSARY
        PathXRecord playerRecord = ((PathXMiniGame) miniGame).getPlayerRecord();
        if (!playerRecord.hasLevel(levelName))
        {
           // playerRecord.addLevel(levelName, initSortingAlgorithm.name);
        }
    }
    
    
    public Viewport getVport() {
        return viewport;
    }

//    public int getMousePressX() {
//        return mousePressX;
//    }
//    
//     public int getMousePressY() {
//        return mousePressY;
//    }
    
      /**
     * Searches the level graph and finds and returns the intersection
     * that overlaps (canvasX, canvasY).
     */
    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX + viewport.x, canvasY + viewport.y);
            if (distance < INTERSECTION_RADIUS)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }
    
    // MUTATOR METHODS

    public void switchState(PathXCarState newMode) {
        
        editMode = newMode;
        
        miniGame.getCanvas().repaint();
        
    }
    
    public void setLevelBounty(int levelBounty) {
        this.levelBounty = levelBounty;
    }
    
     public void setPlayerBalance(int playerWin) {
        playerBalance += playerWin;
    }
    
//    public void setView(PXLE_View initView)
//    {   view = initView;    }    
    public void setLevelBeingEdited(boolean initLevelBeingEdited)
    {   levelBeingEdited = initLevelBeingEdited;    }
    public void setLastMousePosition(int initX, int initY)
    {
        lastMouseX = initX;
        lastMouseY = initY;
       
    }    
    public void setSelectedIntersection(Intersection i)
    {
        selectedIntersection = i;
        selectedRoad = null;
        
    }    
    public void setSelectedRoad(Road r)
    {
        selectedRoad = r;
        selectedIntersection = null;
        
    }
    
    public void initPlayerCar(/*int x , int y*/) {
        //IMAGE_PLAYER_CAR
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        SpriteType sT;

          Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
          Intersection startingLocation = levelBeingPlayed.getStartingLocation();
        
        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
        String playerCarFileName = props.getProperty(PathXPropertyType.IMAGE_PLAYER_CAR);
        BufferedImage playerCarImage = miniGame.loadImageWithColorKey(imgPath + playerCarFileName, COLOR_KEY);
        //((PathXPanelPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);

        sT = new SpriteType(PLAYER_CAR_TYPE);
        addSpriteType(sT);

            // LET'S GENERATE AN IMAGE FOR EACH STATE FOR EACH SPRITE
        //sT.addState(PathXCarState.INVISIBLE_STATE.toString(), buildTileImage(img, img)); // DOESN'T MATTER
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), playerCarImage);
        //sT.addState(PathXCarState.VISIBLE_STATE.toString(), playerCarImage);
            //sT.addState(SortingHatTileState.SELECTED_STATE.toString(), buildTileImage(blankTileSelectedImage, img));
        //sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), buildTileImage(blankTileMouseOverImage, img));
        PlayerCar newPlayer = new PlayerCar(sT, (int)(startingLocation.x), (int)(startingLocation.y) , 0, 0, PathXCarState.INVISIBLE_STATE.toString(), 1);

       // playerCar.clear();
        //movingPlayerCar.clear();
//        System.out.println("X INCREASE " + PathXPanel.sourceX1);
//        System.out.println("Y INCREASE " + PathXPanel.sourceY1);
        
        // AND ADD IT TO THE STACK
        playerCar.add(newPlayer);
        
        //movingPlayerCar.add(playerCar.get(0));
        playerCar.get(0).setDestinationLocation(levelBeingPlayed.getDestination());
        
        playerCar.get(0).setLocation(levelBeingPlayed.getStartingLocation());
    }

    public void initEnemyCars(int numPolice, int numZombies, int numBandits)
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        SpriteType sT;

        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
        String policeCarFileName = props.getProperty(PathXPropertyType.IMAGE_POLICE_CAR);
        BufferedImage policeCarImage = miniGame.loadImageWithColorKey(imgPath + policeCarFileName, COLOR_KEY);
        //((PathXPanelPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);

        // THIS IS A HIGHLIGHTED BLANK TILE FOR WHEN THE PLAYER SELECTS ONE
        String zombieCarFileName = props.getProperty(PathXPropertyType.IMAGE_ZOMBIE_CAR);
        BufferedImage zombieCarImage = miniGame.loadImageWithColorKey(imgPath + zombieCarFileName, COLOR_KEY);
        //((SortingHatPanel) (miniGame.getCanvas())).setBlankTileSelectedImage(blankTileSelectedImage);

        // THIS IS A MOUSE-OVER BLANK TILE
        String banditCarFileName = props.getProperty(PathXPropertyType.IMAGE_BANDIT_CAR);
        BufferedImage banditCarImage = miniGame.loadImageWithColorKey(imgPath + banditCarFileName, COLOR_KEY);
        //((SortingHatPanel) (miniGame.getCanvas())).setBlankTileMouseOverImage(blankTileMouseOverImage);

        // NOW LOAD ALL THE TILES FROM A SPRITE SHEET
        //String tilesSpriteSheetFile = props.getProperty(SortingHatPropertyType.IMAGE_SPRITE_SHEET_CHARACTER_TILES);
        //ArrayList<BufferedImage> tileImages = miniGame.loadSpriteSheetImagesWithColorKey(imgPath + tilesSpriteSheetFile,
         //       68, 14, 5, 18, 5, COLOR_KEY);

        for (int i = 0; i < numPolice; i++)
        {
            //BufferedImage img = tileImages.get(i);

            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
            sT = new SpriteType(POLICE_TYPE_PREFIX + (i + 1));
            addSpriteType(sT);
            
            int indexOfRandomNumberPolice = 0;
            
            Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
            ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
           
            int randomInteger = (int)(Math.random()*intersectionList.size());
            policeStartingIndex.add(randomInteger);
            indexOfRandomNumberPolice = policeStartingIndex.indexOf(randomInteger);
            
            while(intersectionList.get(randomInteger) == levelBeingPlayed.getStartingLocation()
                    || intersectionList.get(randomInteger) == levelBeingPlayed.getDestination()) {
                
                policeStartingIndex.remove(policeStartingIndex.indexOf(randomInteger));
                randomInteger = (int)(Math.random()*intersectionList.size());
                policeStartingIndex.add(randomInteger);
               indexOfRandomNumberPolice = policeStartingIndex.indexOf(randomInteger);
            }
            
            sT.addState(PathXCarState.VISIBLE_STATE.toString(), policeCarImage);
            
             PoliceCar newPolice = new PoliceCar(sT,
                    intersectionList.get(policeStartingIndex.get(indexOfRandomNumberPolice)).x,
                    intersectionList.get(policeStartingIndex.get(indexOfRandomNumberPolice)).y, 0, 0,
                    PathXCarState.VISIBLE_STATE.toString(), i + 1);
            System.out.println("zombieStartingIndex " + policeStartingIndex);
            System.out.println("Zombie #" + i + " starting coordinates: ("
                    + intersectionList.get(policeStartingIndex.get(indexOfRandomNumberPolice)).x + 
                    ", " + intersectionList.get(policeStartingIndex.get(indexOfRandomNumberPolice)).y + ")");
            // AND ADD IT TO THE STACK
            policeCarStack.add(newPolice);
        }
        
         for (int i = 0; i < numZombies; i++)
        {

            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
            sT = new SpriteType(ZOMBIE_TYPE_PREFIX + (i + 1));
            addSpriteType(sT);
            
            int indexOfRandomNumberZombie = 0;
            
            Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
            ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
           
            int randomInteger = (int)(Math.random()*intersectionList.size());
            zombieStartingIndex.add(randomInteger);
            indexOfRandomNumberZombie = zombieStartingIndex.indexOf(randomInteger);
            
            while(intersectionList.get(randomInteger) == levelBeingPlayed.getStartingLocation()
                    || intersectionList.get(randomInteger) == levelBeingPlayed.getDestination()) {
                
                zombieStartingIndex.remove(zombieStartingIndex.indexOf(randomInteger));
                randomInteger = (int)(Math.random()*intersectionList.size());
                zombieStartingIndex.add(randomInteger);
               indexOfRandomNumberZombie = zombieStartingIndex.indexOf(randomInteger);
            }
            
            sT.addState(PathXCarState.VISIBLE_STATE.toString(), zombieCarImage);

            ZombieCar newZombie = new ZombieCar(sT,
                    intersectionList.get(zombieStartingIndex.get(indexOfRandomNumberZombie)).x,
                    intersectionList.get(zombieStartingIndex.get(indexOfRandomNumberZombie)).y, 0, 0,
                    PathXCarState.VISIBLE_STATE.toString(), i + 1);
            System.out.println("zombieStartingIndex " + zombieStartingIndex);
            System.out.println("Zombie #" + i + " starting coordinates: ("
                    + intersectionList.get(zombieStartingIndex.get(indexOfRandomNumberZombie)).x + 
                    ", " + intersectionList.get(zombieStartingIndex.get(indexOfRandomNumberZombie)).y + ")");
            // AND ADD IT TO THE STACK
            zombieCarStack.add(newZombie);
        }
         
          for (int i = 0; i < numBandits; i++)
        {
            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
            sT = new SpriteType(BANDIT_TYPE_PREFIX + (i + 1));
            addSpriteType(sT);

            int indexOfRandomNumberBandit = 0;
            
            Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
            ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
            
            int randomInteger = (int)(Math.random()*intersectionList.size());
            banditStartingIndex.add(randomInteger);
            indexOfRandomNumberBandit = banditStartingIndex.indexOf(randomInteger);
            
            while(intersectionList.get(randomInteger) == levelBeingPlayed.getStartingLocation()
                    || intersectionList.get(randomInteger) == levelBeingPlayed.getDestination()) {
                
                banditStartingIndex.remove(banditStartingIndex.indexOf(randomInteger));
                randomInteger = (int)(Math.random()*intersectionList.size());
                banditStartingIndex.add(randomInteger);
               indexOfRandomNumberBandit = banditStartingIndex.indexOf(randomInteger);
            }

            // LET'S GENERATE AN IMAGE FOR EACH STATE FOR EACH SPRITE
            sT.addState(PathXCarState.VISIBLE_STATE.toString(), banditCarImage);

            BanditCar newBandit = new BanditCar(sT,
                    intersectionList.get(banditStartingIndex.get(indexOfRandomNumberBandit)).x,
                    intersectionList.get(banditStartingIndex.get(indexOfRandomNumberBandit)).y, 0, 0,
                    PathXCarState.VISIBLE_STATE.toString(), i + 1);
            System.out.println("banditStartingIndex " + banditStartingIndex);
            System.out.println("Bandit #" + i + " starting coordinates: ("
                    + intersectionList.get(banditStartingIndex.get(indexOfRandomNumberBandit)).x + 
                    ", " + intersectionList.get(banditStartingIndex.get(indexOfRandomNumberBandit)).y + ")");
            // AND ADD IT TO THE banditStartingIndex
            banditCarStack.add(newBandit);
        }
    }
    public static ArrayList<Integer> banditStartingIndex = new ArrayList<>();
    public static ArrayList<Integer> policeStartingIndex = new ArrayList<>();
    public static ArrayList<Integer> zombieStartingIndex = new ArrayList<>();
    /**
     *
     */
    //public static Intersection playerPosition = ((PathXMiniGame) miniGame).getCurrentLevel().getStartingLocation();
    
      public void movePlayer(Intersection i1, Intersection i2)
    {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
        
         Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        
        // NULL PathXPanel gamePanel = ((PathXMiniGame)miniGame).getPanel();
        
        ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
        
        PathXGameGraphManager gameGraph = ((PathXMiniGame) miniGame).getEventHandler().getGameGraph();
        
       
        
        ArrayList<Integer> xPoints = PathXEventHandler.xCoordinates;
        ArrayList<Integer> yPoints = PathXEventHandler.yCoordinates;

        //moveAllPoliceToStack();

        Intersection intersection1 = new Intersection(0,0);
        Intersection intersection2 = new Intersection(0,0);
        
        int error = 30;
       // int diffX = (int)(xPoints.get(randomIndex1) - 80);
        //int diffY = (int)(yPoints.get(randomIndex1) + 10); //- (int)sourceY1;
        
//        for(Intersection anIntersection: intersectionList) {
//            if (((diffX + error) >= anIntersection.x)
//                    && ((diffX - error) <= anIntersection.x)
//                    && ((diffY + error) >= anIntersection.y)
//                    && ((diffY - error) <= anIntersection.y)) {
//                System.out.println("Relative to Map X (1): " + xPoints.get(randomIndex1));
//                System.out.println("Relative to Map Y (1): " + yPoints.get(randomIndex1));
//                System.out.println("Map Coordinates(1) X: " + anIntersection.x);
//                 System.out.println("Map Coordinates(1) Y: " + anIntersection.y);
//                 System.out.println("Difference in X(1): " + (xPoints.get(randomIndex1) -  anIntersection.x));
//                 System.out.println("Difference in Y(1): " + (yPoints.get(randomIndex1) -  anIntersection.y));
//               intersection1 = anIntersection;
//            }
//        }
        
        ArrayList<Connection> path = gameGraph.findShortestPathToHome(i1, i2);
//       ArrayList<Connection> path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);
//       
//       for(Connection aPath: path2) {
//           path.add(aPath);
//       }
//       
        ArrayList<Integer> velocity = new ArrayList<>();// = gameGraph.getRoad(path.get(0).getRoadId()).getSpeedLimit()/10;
        
       for(Connection connection: path) {
        Intersection inters2 = gameGraph.getIntersection(connection.getIntersection2Id());
        
        velocity.add(gameGraph.getRoad(connection.getRoadId()).speedLimit/10);
        
           System.out.println("REAL GAMEPATH X: " + (inters2.x));
           System.out.println("REAL GAMEPATH Y: " + (inters2.y));
        winPath.add(inters2.x);
        winPath.add(inters2.y);
       }
        playerCar.get(0).setVelocity(velocity);
     
         movingPlayerCar.clear();
        // START THE ANIMATION FOR ALL THE TILES
       for (int i = 0; i < playerCar.size(); i++)
        {
            // GET EACH TILE
            PlayerCar car = playerCar.get(i);         
            
            // MAKE SURE IT'S MOVED EACH FRAME
            movingPlayerCar.add(car);
            
            System.out.println("movingPlayerCar size: " + movingPlayerCar.size());
            
            //car.setTarget(540, 68);
            
            car.setTarget(winPath.get(i), winPath.get(i+1));

            //winPath.remove(0);
            //winPath.remove(0);
            // AND GET IT ON A PATH
            car.initWinPath(winPath);
            winPath.clear();
           
        }
    }
    
    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playPoliceAnimation()
    {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        
        // NULL PathXPanel gamePanel = ((PathXMiniGame)miniGame).getPanel();
        
        ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
        
        PathXGameGraphManager gameGraph = ((PathXMiniGame) miniGame).getEventHandler().getGameGraph();
        

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
   
       // int randomIndex1 = (int) (Math.random() * (xPoints.size()));
         for (int k = 0; k < policeCarStack.size(); k++)
        {
        int randomIndex2 = (int) (Math.random() * (intersectionList.size()));
            System.out.println("policeStartingIndex "+k+ ": " + policeStartingIndex.get(k));
        while (policeStartingIndex.get(k) == randomIndex2) {
            //randomIndex1 = (int) (Math.random() * (xPoints.size()));
            randomIndex2 = (int) (Math.random() * (intersectionList.size()));
        }

        Intersection intersection1;
        Intersection intersection2;
      

                intersection1 = intersectionList.get(policeStartingIndex.get(k));

        System.out.println("Starting position: X: " + intersection1.x
                + "Y: " + intersection1.y);

                intersection2 = intersectionList.get(randomIndex2);

        ArrayList<Connection> path = gameGraph.findShortestPathToHome(intersection1, intersection2);
       ArrayList<Connection> path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);

        while (isBadPath(path, path2) || path.isEmpty() || path2.isEmpty()) {
                System.out.println("POLICE STUCK");
                int randomNum = (int) (Math.random() * (intersectionList.size()));
                intersection2 = intersectionList.get(randomNum);
                path = gameGraph.findShortestPathToHome(intersection1, intersection2);
                path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);
            }
            System.out.println("POLICE UNSTUCK");
            
        System.out.println("Path 1: ");
       for (Connection connection : path) {
            Intersection i1 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i1.x));
            System.out.println("Y: " + (i1.y));
            winPath.add(i1.x);
            winPath.add(i1.y);
        }
       System.out.println("Path 2: ");
        for (Connection connection : path2) {
            Intersection i2 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i2.x));
            System.out.println("Y: " + (i2.y));
            winPath.add(i2.x);
            winPath.add(i2.y);
        }
    
//        moveAllTilesToStack();

        // START THE ANIMATION FOR ALL THE TILES
//       for (int k = 0; k < banditCarStack.size(); k++)
//        {
            // GET EACH TILE
            PoliceCar car = policeCarStack.get(k);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingPolice.add(car);
            System.out.println("BLAH BLAH");
            car.setTarget(winPath.get(winPath.indexOf(intersection1.x)),
                   winPath.get(winPath.indexOf(intersection1.y)));
            //car.setTarget(winPath.get(n++), winPath.get(m++));

            System.out.println("Target for " + k + " (" +
                    winPath.get(winPath.indexOf(intersection1.x))
                    + ", " + winPath.get(winPath.indexOf(intersection1.y)) + ")");
//            winPath.remove(0);
//            winPath.remove(0);
            
            // AND GET IT ON A PATH
            car.initWinPath(winPath);
        winPath.clear();
        }
    }
    
    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playZombieAnimation()
    {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        
        // NULL PathXPanel gamePanel = ((PathXMiniGame)miniGame).getPanel();
        
        ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
        
        PathXGameGraphManager gameGraph = ((PathXMiniGame) miniGame).getEventHandler().getGameGraph();
        

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
   
       // int randomIndex1 = (int) (Math.random() * (xPoints.size()));
         for (int k = 0; k < zombieCarStack.size(); k++)
        {
        int randomIndex2 = (int) (Math.random() * (intersectionList.size()));
            System.out.println("zombieStartingIndex "+k+ ": " + zombieStartingIndex.get(k));
        while (zombieStartingIndex.get(k) == randomIndex2) {
            //randomIndex1 = (int) (Math.random() * (xPoints.size()));
            randomIndex2 = (int) (Math.random() * (intersectionList.size()));
        }

        Intersection intersection1;
        Intersection intersection2;
      

                intersection1 = intersectionList.get(zombieStartingIndex.get(k));

        System.out.println("Starting position: X: " + intersection1.x
                + "Y: " + intersection1.y);

                intersection2 = intersectionList.get(randomIndex2);

            ArrayList<Connection> path = gameGraph.findShortestPathToHome(intersection1, intersection2);
            ArrayList<Connection> path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);

            while (isBadPath(path, path2) || path.isEmpty() || path2.isEmpty()) {
                System.out.println("ZOMBIE STUCK");
                int randomNum = (int) (Math.random() * (intersectionList.size()));
                intersection2 = intersectionList.get(randomNum);
                path = gameGraph.findShortestPathToHome(intersection1, intersection2);
                path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);
            }
            System.out.println("ZOMBIE UNSTUCK");
            
            System.out.println("Path 1: ");
            for (Connection connection : path) {
                Intersection i1 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i1.x));
            System.out.println("Y: " + (i1.y));
            winPath.add(i1.x);
            winPath.add(i1.y);
        }
       System.out.println("Path 2: ");
        for (Connection connection : path2) {
            Intersection i2 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i2.x));
            System.out.println("Y: " + (i2.y));
            winPath.add(i2.x);
            winPath.add(i2.y);
        }
    
//        moveAllTilesToStack();

        // START THE ANIMATION FOR ALL THE TILES
//       for (int k = 0; k < banditCarStack.size(); k++)
//        {
            // GET EACH TILE
            ZombieCar car = zombieCarStack.get(k);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingZombies.add(car);
            System.out.println("BLAH BLAH");
            car.setTarget(winPath.get(winPath.indexOf(intersection1.x)),
                   winPath.get(winPath.indexOf(intersection1.y)));
            //car.setTarget(winPath.get(n++), winPath.get(m++));

            System.out.println("Target for " + k + " (" +
                    winPath.get(winPath.indexOf(intersection1.x))
                    + ", " + winPath.get(winPath.indexOf(intersection1.y)) + ")");
//            winPath.remove(0);
//            winPath.remove(0);
            
            // AND GET IT ON A PATH
            car.initWinPath(winPath);
        winPath.clear();
        }
    }
    
    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playBanditAnimation()
    {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        
        ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
        
        PathXGameGraphManager gameGraph = ((PathXMiniGame) miniGame).getEventHandler().getGameGraph();
        

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
   
       // int randomIndex1 = (int) (Math.random() * (xPoints.size()));
         for (int k = 0; k < banditCarStack.size(); k++)
        {
        int randomIndex2 = (int) (Math.random() * (intersectionList.size()));
            System.out.println("banditStartingIndex "+k+ ": " + banditStartingIndex.get(k));
        while (banditStartingIndex.get(k) == randomIndex2
                || intersectionList.get(randomIndex2) == levelBeingPlayed.getDestination()
                || intersectionList.get(randomIndex2) == levelBeingPlayed.getStartingLocation()) {
            //randomIndex1 = (int) (Math.random() * (xPoints.size()));
            randomIndex2 = (int) (Math.random() * (intersectionList.size()));
        }

        Intersection intersection1;
        Intersection intersection2;
      

                intersection1 = intersectionList.get(banditStartingIndex.get(k));
                //intersection1 = levelBeingPlayed.getDestination();

        System.out.println("Starting position: X: " + intersection1.x
                + "Y: " + intersection1.y);

                intersection2 = intersectionList.get(randomIndex2);

            ArrayList<Connection> path = gameGraph.findShortestPathToHome(intersection1, intersection2);
            ArrayList<Connection> path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);

            while (isBadPath(path, path2) || path.isEmpty() || path2.isEmpty()) {
                System.out.println("BANDIT STUCK"); 
                int randomNum = (int) (Math.random() * (intersectionList.size()));
                intersection2 = intersectionList.get(randomNum);
                path = gameGraph.findShortestPathToHome(intersection1, intersection2);
                path2 = gameGraph.findShortestPathToHome(intersection2, intersection1);
            }
           System.out.println("BANDIT UNSTUCK");
           
            System.out.println("Path 1 size: " + path.size());
            for (Connection connection : path) {
            Intersection i1 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i1.x));
            System.out.println("Y: " + (i1.y));
            winPath.add(i1.x);
            winPath.add(i1.y);
        }
      
       System.out.println("Path 2: " + path2.size());
        for (Connection connection : path2) {
            Intersection i2 = gameGraph.getIntersection(connection.getIntersection1Id());
            System.out.println("X: " + (i2.x));
            System.out.println("Y: " + (i2.y));
            winPath.add(i2.x);
            winPath.add(i2.y);
        }
    
//        moveAllTilesToStack();

        // START THE ANIMATION FOR ALL THE TILES
//       for (int k = 0; k < banditCarStack.size(); k++)
//        {
            // GET EACH TILE
            BanditCar car = banditCarStack.get(k);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingBandits.add(car);
            System.out.println("BLAH BLAH");
            car.setTarget(winPath.get(winPath.indexOf(intersection1.x)),
                   winPath.get(winPath.indexOf(intersection1.y)));
            //car.setTarget(winPath.get(n++), winPath.get(m++));

            System.out.println("Target for " + k + " (" +
                    winPath.get(winPath.indexOf(intersection1.x))
                    + ", " + winPath.get(winPath.indexOf(intersection1.y)) + ")");
//            winPath.remove(0);
//            winPath.remove(0);
            
            // AND GET IT ON A PATH
            car.initWinPath(winPath);
        winPath.clear();
        }
    
}
    
    public boolean isBadPath(ArrayList<Connection> p1, ArrayList<Connection> p2) {

        Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        ArrayList<Intersection> intersectionList = levelBeingPlayed.getIntersections();
        PathXGameGraphManager gameGraph = ((PathXMiniGame) miniGame).getEventHandler().getGameGraph();

        for (Connection c : p1) {
            if (gameGraph.getIntersection(c.getIntersection1Id()) == levelBeingPlayed.getDestination()
                    || gameGraph.getIntersection(c.getIntersection1Id()) == levelBeingPlayed.getStartingLocation()
                    || gameGraph.getIntersection(c.getIntersection2Id()) == levelBeingPlayed.getDestination()
                    || gameGraph.getIntersection(c.getIntersection2Id()) == levelBeingPlayed.getStartingLocation()) {
                return true;
            }

        }

        for (Connection c : p2) {
            if (gameGraph.getIntersection(c.getIntersection1Id()) == levelBeingPlayed.getDestination()
                    || gameGraph.getIntersection(c.getIntersection1Id()) == levelBeingPlayed.getStartingLocation()
                    || gameGraph.getIntersection(c.getIntersection2Id()) == levelBeingPlayed.getDestination()
                    || gameGraph.getIntersection(c.getIntersection2Id()) == levelBeingPlayed.getStartingLocation()) {
                return true;
            }

        }

        return false;
    }

            
    public static int n = 0;
    public static int m = 1;
     /**
     * This method moves all the tiles not currently in the stack to the stack.
     */
    public void moveAllPoliceToStack()
    {
        moveTiles(movingPolice, policeCarStack);
        //moveTiles(tilesToSort, stackTiles);
    }

    /**
     * This method removes all the tiles in from argument and moves them to
     * argument.
     *
     * @param from The source data structure of tiles.
     *
     * @param to The destination data structure of tiles.
     */
    private void moveTiles(ArrayList<PoliceCar> from, ArrayList<PoliceCar> to)
    {
        // GO THROUGH ALL THE TILES, TOP TO BOTTOM
        for (int i = from.size() - 1; i >= 0; i--)
        {
            PoliceCar car = from.remove(i);

            // ONLY ADD IT IF IT'S NOT THERE ALREADY
            if (!to.contains(car))
            {
                to.add(car);
            }
        }
    }
    
    /**
     * This method loads the tiles, creating an individual sprite for each. Note
     * that tiles may be of various types, which is important during the tile
     * matching tests.
     */
    public void initTiles() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        SpriteType sT;

        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
       // String blankTileFileName = props.getProperty(PathXPropertyType.IMAGE_TILE_BACKGROUND);
       // BufferedImage blankTileImage = miniGame.loadImageWithColorKey(imgPath + blankTileFileName, COLOR_KEY);
       // ((PathXPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);

        // THIS IS A HIGHLIGHTED BLANK TILE FOR WHEN THE PLAYER SELECTS ONE
       // String blankTileSelectedFileName = props.getProperty(SortingHatPropertyType.IMAGE_TILE_BACKGROUND_SELECTED);
       // BufferedImage blankTileSelectedImage = miniGame.loadImageWithColorKey(imgPath + blankTileSelectedFileName, COLOR_KEY);
       // ((SortingHatPanel) (miniGame.getCanvas())).setBlankTileSelectedImage(blankTileSelectedImage);

        // THIS IS A MOUSE-OVER BLANK TILE
       // String blankTileMouseOverFileName = props.getProperty(SortingHatPropertyType.IMAGE_TILE_BACKGROUND_MOUSE_OVER);
       // BufferedImage blankTileMouseOverImage = miniGame.loadImageWithColorKey(imgPath + blankTileMouseOverFileName, COLOR_KEY);
       // ((SortingHatPanel) (miniGame.getCanvas())).setBlankTileMouseOverImage(blankTileMouseOverImage);

        // NOW LOAD ALL THE TILES FROM A SPRITE SHEET
      //  String tilesSpriteSheetFile = props.getProperty(SortingHatPropertyType.IMAGE_SPRITE_SHEET_CHARACTER_TILES);
      //  ArrayList<BufferedImage> tileImages = miniGame.loadSpriteSheetImagesWithColorKey(imgPath + tilesSpriteSheetFile,
    //            68, 14, 5, 18, 5, COLOR_KEY);

     //   for (int i = 0; i < tileImages.size(); i++)
        {
      //      BufferedImage img = tileImages.get(i);

            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
      //      sT = new SpriteType(TILE_SPRITE_TYPE_PREFIX + (i + 1));
      //      addSpriteType(sT);

            // LET'S GENERATE AN IMAGE FOR EACH STATE FOR EACH SPRITE
      //      sT.addState(PathXCarState.INVISIBLE_STATE.toString(), buildTileImage(img, img)); // DOESN'T MATTER
      //      sT.addState(PathXCarState.VISIBLE_STATE.toString(), buildTileImage(blankTileImage, img));
      //      sT.addState(PathXCarState.SELECTED_STATE.toString(), buildTileImage(blankTileSelectedImage, img));
       //     sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), buildTileImage(blankTileMouseOverImage, img));
       //     PathXCar newTile = new PathXCar(sT, stackCarsX, stackCarsY, 0, 0, PathXCarState.INVISIBLE_STATE.toString(), i + 1);

            // AND ADD IT TO THE STACK
       //     stackCars.add(newTile);
        }
         
    }

    // HELPER METHOD FOR INITIALIZING OUR WIZARD AND WITCHES TRADING CARD TILES
    private BufferedImage buildTileImage(BufferedImage backgroundImage, BufferedImage spriteImage) {
        // BASICALLY THIS RENDERS TWO IMAGES INTO A NEW ONE, COMBINING THEM, AND THEN
        // RETURNING THE RESULTING IMAGE
        BufferedImage bi = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(spriteImage, TILE_IMAGE_OFFSET_X, TILE_IMAGE_OFFSET_Y, null);
        return bi;
    }
    
    public void resetCars() {
        movingBandits = new ArrayList<>();
        movingPlayerCar = new ArrayList<>();
        movingPolice = new ArrayList<>();
        movingZombies = new ArrayList<>();
        banditCarStack = new ArrayList<>();
        zombieCarStack = new ArrayList<>();
        policeCarStack = new ArrayList<>();
        playerCar = new ArrayList<>();
        PlayerCar.finalX = 0;
        PlayerCar.finalY = 0;
        PlayerCar.initialized = false;
    }

    
    /**
     * Used to calculate the x-axis pixel location in the game grid for a tile
     * placed at column with stack position z.
     *
     * @param column The column in the grid the tile is located.
     *
     * @return The x-axis pixel location of the tile
     */
    public int calculateGridTileX(int column) {
        return viewport.getViewportMarginLeft() + (column * TILE_WIDTH) - viewport.getViewportX();
    }

    /**
     * Used to calculate the y-axis pixel location in the game grid for a tile
     * placed at row.
     *
     * @param row The row in the grid the tile is located.
     *
     * @return The y-axis pixel location of the tile
     */
    public int calculateGridTileY(int row) {
        return viewport.getViewportMarginTop() + (row * TILE_HEIGHT) - viewport.getViewportY();
    }
    
    public static int XCoordinate;
    public static int YCoordinate;

    /**
     * Used to calculate the grid column for the x-axis pixel location.
     *
     * @param x The x-axis pixel location for the request.
     *
     * @return The column that corresponds to the x-axis location x.
     */
    public int calculateGridCellColumn(int x)
    {
        // ADJUST FOR THE MARGIN
        //x -= 60;

        // ADJUST FOR THE GAME SCREEN
        x = (int) (x - PathXPanel.destinationX1 - PathXPanel.sourceX1 + 98);
        XCoordinate = x;
      //  if (x < 0)
        {
        //    return -1;
        }

        // AND NOW GET THE COLUMN
        return x;// / INTERSECTION_RADIUS;
    }

    /**
     * Used to calculate the grid row for the y-axis pixel location.
     *
     * @param y The y-axis pixel location for the request.
     *
     * @return The row that corresponds to the y-axis location y.
     */
    public int calculateGridCellRow(int y)
    {
        // ADJUST FOR THE MARGIN
        //y -= viewport.getViewportMarginTop();

        // ADJUST FOR THE GAME SCREEN
        y = (int) (y - PathXPanel.destinationY1 - PathXPanel.sourceY1 - 10);
        YCoordinate = y;
        //if (y < 0)
        {
            //  return -1;
        }

        // AND NOW GET THE ROW
        return y;// / INTERSECTION_RADIUS;
    }

    // TIME TEXT METHODS
    // - timeToText
    // - gameTimeToText
    /**
     * This method creates and returns a textual description of the timeInMillis
     * argument as a time duration in the format of (H:MM:SS).
     *
     * @param timeInMillis The time to be represented textually.
     *
     * @return A textual representation of timeInMillis.
     */
    public String timeToText(long timeInMillis)
    {
        // FIRST CALCULATE THE NUMBER OF HOURS,
        // SECONDS, AND MINUTES
        long hours = timeInMillis / MILLIS_IN_AN_HOUR;
        timeInMillis -= hours * MILLIS_IN_AN_HOUR;
        long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
        timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
        long seconds = timeInMillis / MILLIS_IN_A_SECOND;

        // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
        String minutesText = "" + minutes;
        if (minutes < 10)
        {
            minutesText = "0" + minutesText;
        }
        String secondsText = "" + seconds;
        if (seconds < 10)
        {
            secondsText = "0" + secondsText;
        }
        // TTT= hours + ":" + minutesText + ":" + secondsText;
        return hours + ":" + minutesText + ":" + secondsText;
    }

    
 //   public FlyingTransaction updateUndo()
 //   {
  //      FlyingTransaction undoMoves;
 //       if(transactionCounter>0)
   //     {
  //   undoMoves = properTransactionOrder.get(transactionCounter-1);
        
  //   transactionCounter--;
  //   isUndo = true;
        
   //     return undoMoves;
  ///      }
  //      return null;
  //  }
    /**
     * This method builds and returns a textual representation of the game time.
     * Note that the game may still be in progress.
     *
     * @return The duration of the current game represented textually.
     */
    public String gameTimeToText()
    {
        // CALCULATE GAME TIME USING HOURS : MINUTES : SECONDS
        if ((startTime == null) || (endTime == null))
        {
            return "";
        }
        long timeInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        return timeToText(timeInMillis);
    }

    // GAME DATA SERVICE METHODS
    // -enableTiles
    // -moveAllTilesToStack
    // -moveTiles
    // -playWinAnimation
    // -processMove
    // -selectTile
    // -undoLastMove
    /**
     * This method can be used to make all of the tiles either visible (true) or
     * invisible (false). This should be used when switching between the menu
     * and game screens.
     *
     * @param enable Specifies whether the tiles should be made visible or not.
     */
    public void enableTiles(boolean enable)
    {
        // PUT ALL THE TILES IN ONE PLACE WHERE WE CAN PROCESS THEM TOGETHER
        moveAllTilesToStack();

        // GO THROUGH ALL OF THEM 
        for (PathXCar car : stackCars)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                car.setState(PathXCarState.VISIBLE_STATE.toString());
            } else
            {
                car.setState(PathXCarState.INVISIBLE_STATE.toString());
            }
        }
    }
    
     public void enableCars(boolean enable)
    {
        // PUT ALL THE TILES IN ONE PLACE WHERE WE CAN PROCESS THEM TOGETHER
        //moveAllTilesToStack();

        // GO THROUGH ALL OF THEM 
        for (PoliceCar car : policeCarStack)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                car.setState(PathXCarState.VISIBLE_STATE.toString());
            } else
            {
                car.setState(PathXCarState.INVISIBLE_STATE.toString());
            }
        }
        for (ZombieCar car : zombieCarStack)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                car.setState(PathXCarState.VISIBLE_STATE.toString());
            } else
            {
                car.setState(PathXCarState.INVISIBLE_STATE.toString());
            }
        }
        for (BanditCar car : banditCarStack)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                car.setState(PathXCarState.VISIBLE_STATE.toString());
            } else
            {
                car.setState(PathXCarState.INVISIBLE_STATE.toString());
            }
        }
         for (PlayerCar car : playerCar)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                car.setState(PathXCarState.VISIBLE_STATE.toString());
            } else
            {
                car.setState(PathXCarState.INVISIBLE_STATE.toString());
            }
        }
    }

    /**
     * This method moves all the tiles not currently in the stack to the stack.
     */
    public void moveAllTilesToStack()
    {
        moveCars(movingCars, stackCars);
       // moveTiles(tilesToSort, stackCars);
    }

    /**
     * This method removes all the tiles in from argument and moves them to
     * argument.
     *
     * @param from The source data structure of tiles.
     *
     * @param to The destination data structure of tiles.
     */
    private void moveCars(ArrayList<PathXCar> from, ArrayList<PathXCar> to)
    {
        // GO THROUGH ALL THE TILES, TOP TO BOTTOM
        for (int i = from.size() - 1; i >= 0; i--)
        {
            PathXCar car = from.remove(i);

            // ONLY ADD IT IF IT'S NOT THERE ALREADY
            if (!to.contains(car))
            {
                to.add(car);
            }
        }
    }

    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playWinAnimation()
    {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
      //  winPath.add(viewport.getScreenWidth() - WIN_PATH_COORD);
        
        
        winPath.add(637);
        winPath.add(100);
        
        winPath.add(250);
        winPath.add(591);
        
        winPath.add(1100);
        winPath.add(300);
        
        winPath.add(174);
        winPath.add(300);
        
        winPath.add(874);
        winPath.add(591);
        
       // winPath.add(WIN_PATH_COORD);
      //  winPath.add(WIN_PATH_COORD);
      //  winPath.add(WIN_PATH_COORD);
        
        
        
      //  winPath.add(viewport.getScreenHeight() - WIN_PATH_COORD);
      //  winPath.add(viewport.getScreenWidth() - WIN_PATH_COORD);
      //  winPath.add(viewport.getScreenHeight() - WIN_PATH_COORD);
        moveAllTilesToStack();

        // START THE ANIMATION FOR ALL THE TILES
        for (int i = 0; i < stackCars.size(); i++)
        {
            // GET EACH TILE
            PathXCar tile = stackCars.get(i);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingCars.add(tile);

            // AND GET IT ON A PATH
            tile.initWinPath(winPath);
        }
    }

    /**
     * Gets the next swap operation using the list generated by the proper
     * algorithm.
     */
    public CarMotionTransaction getNextSwapTransaction()
    {
        return properTransactionOrder.get(transactionCounter);
    }

    /**
     * Swaps the tiles at the two indices.
     */
    public void swapTiles(int index1, int index2)
    {
        // GET THE TILES
        PathXCar tile1 = tilesToSort.get(index1);
        PathXCar tile2 = tilesToSort.get(index2);
        
        
//           gameTiles[arrayCount] = tile1.getID();
        //     gameTiles2[arrayCount] = tile2.getID();
      //        arrayCount++;
              
        // GET THE TILE TWO LOCATION
        int tile2Col = tile2.getGridColumn();
        int tile2Row = tile2.getGridRow();

        // LET'S MOVE TILE 2 FIRST
        tilesToSort.set(index1, tile2);
        tile2.setGridCell(tile1.getGridColumn(), tile1.getGridRow());
        //tile2.setTarget(calculateGridTileX(tile1.getGridColumn()), calculateGridTileY(tile1.getGridRow()));

        // THEN MOVE TILE 1
        tilesToSort.set(index2, tile1);
        tile1.setGridCell(tile2Col, tile2Row);
        //tile1.setTarget(calculateGridTileX(tile2Col), calculateGridTileY(tile2Row));
        movingCars.add(tile1);
        movingCars.add(tile2);

        // SEND THEM TO THEIR DESTINATION
        tile1.startMovingToTarget(MAX_TILE_VELOCITY);
        tile2.startMovingToTarget(MAX_TILE_VELOCITY);

        // AND ON TO THE NEXT TRANSACTION
      //  if(!isUndo)
        transactionCounter++;
   // else
   //         isUndo=false;
       
        // HAS THE PLAYER WON?
        if (transactionCounter == this.properTransactionOrder.size())
        {
            // YUP UPDATE EVERYTHING ACCORDINGLY
            //endGameAsWin();
        }
    }

    /**
     * This method updates all the necessary state information to process the
     * swap transaction.
     */
    public void processSwap(int index1, int index2)
    {
        // FIRST CHECK AND SEE IF IT'S THE PROPER SWAP AT THIS TIME
        CarMotionTransaction potentialSwap = new CarMotionTransaction(index1, index2);
        CarMotionTransaction correctSwap = properTransactionOrder.get(transactionCounter);

        // IT'S A GOOD SWAP, MEANING IT'S WHAT THE SORTING ALGORITHM
        // IS SUPPOSED TO DO
        if (potentialSwap.equals(correctSwap))
        {
            // SWAP THEM
            swapTiles(index1, index2);

            // DESELECT THE SELECTED TILE
            selectedCar.setState(PathXCarState.VISIBLE_STATE.toString());
            selectedCar = null;
            selectedCarIndex = -1;

            // PLAY THE GOOD MOVE SOUND EFFECT
            miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
        } 
        else
        {

            // PLAY THE BAD MOVE SOUND EFFECT
            miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
        }
    }

    // THIS HELPER METHOD FINDS THE TILE IN THE DATA STRUCTURE WITH
    // THE GRID LOCATION OF col, row, AND RETURNS IT'S INDEX
    private int getSnakeIndex(int col, int row)
    {
        for (int i = 0; i < tilesToSort.size(); i++)
        {
            PathXCar tile = tilesToSort.get(i);
            if ((tile.getGridColumn() == col) && tile.getGridRow() == row)
            {
                return i;
            }
        }
        return -1;
    }

    // OVERRIDDEN METHODS
    // - checkMousePressOnSprites
    // - endGameAsWin
    // - reset
    // - updateAll
    // - updateDebugText

    public static int init = 0;
    public static Intersection location;
    
    public boolean isAnIntersection(int x, int y) {
        int error = 30;
        for (Intersection i : ((PathXMiniGame) miniGame).getCurrentLevel().getIntersections()) {
            if (((x + error) >= i.x)
                    && ((x - error) <= i.x)
                    && ((y + error) >= i.y)
                    && ((y - error) <= i.y)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Sorting Hat game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y)
    {
 
        if(init == 0) {
//            location = level.getStartingLocation();
        }
        // FIGURE OUT THE CELL IN THE GRID
        int col = calculateGridCellColumn(x);
        int row = calculateGridCellRow(y);

        System.out.println("REACHED checkMousePressOnSprites: "
                + "\nX: " + col + "\nY: " + row);

        System.out.println("isCurrentScreenState: " + ((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE));
      //  System.out.println("isGameStarted: " + PathXMiniGame.isGameStarted);
       // System.out.println("NOT isMovingToTarget: " + !playerCar.get(0).isMovingToTarget());
        
        
        
        if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                && !playerCar.get(0).isMovingToTarget() && isGameStarted) {

            
            Intersection destination = ((PathXMiniGame) game).getEventHandler().intersectionClickerCheck(col, row);

            if (isAnIntersection(col, row) && playerCar.get(0).getLocation() != destination
                    && destination != getLevel().getStartingLocation()) {

                movePlayer(playerCar.get(0).getLocation(), destination);
                playerCar.get(0).setLocation(destination);
                
                if(playerCar.get(0).getX() == getLevel().getDestination().x
                        && playerCar.get(0).getY() == getLevel().getDestination().y) {
                    System.out.println("YOU WON THE LEVEL!!");
                }
            }
        }
        // DISABLE THE STATS DIALOG IF IT IS OPEN
      //  if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(PathXCarState.VISIBLE_STATE.toString()))
        {
     //       game.getGUIDialogs().get(STATS_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
     //       return;
        }

        // CHECK THE CELL AT col, row
        //int index = getSnakeIndex(col, row);
        
        // IT'S OUTSIDE THE GRID
        //if (index < 0)
        {
            // DESELECT A TILE IF ONE IS SELECTED
//            if (selectedCar != null)
//            {
//                selectedCar.setState(PathXCarState.VISIBLE_STATE.toString());
//                selectedCar = null;
//                selectedCarIndex = -1;
//                miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_DESELECT_TILE.toString(), false);
//            }
        }
        // IT'S IN THE GRID
       // else
        {
            // SELECT THE TILE IF NONE IS SELECTED
//            if (selectedCar == null)
//            {
//         //       selectedCar = tilesToSort.get(index);
//                selectedCar.setState(PathXCarState.SELECTED_STATE.toString());
//          //      selectedCarIndex = index;
//                miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
//            }
//            // A TILE WAS ALREADY SELECTED, SO THIS MUST HAVE BEEN THE SECOND TILE
//            // SELECTED, SO SWAP THEM
//            else
            {
        //        processSwap(index, selectedCarIndex);
            }
        }
    }

    /**
     * Helps to identify which level is currently
     * being played in the levelList
     * @param levelConstant
     * @return 
     */
    public int getLevelNumber(String levelConstant) {
        if (levelConstant.contains("CrastersKeep")) {
            return 0;
        } else if (levelConstant.contains("CastleBlack")) {
            return 1;
        } else if (levelConstant.contains("LastHearth")) {
            return 2;
        } else if (levelConstant.contains("DeepwoodMotte")) {
            return 3;
        } else if (levelConstant.contains("Winterfell")) {
            return 4;
        } else if (levelConstant.contains("TorrhensSquare")) {
            return 5;
        } else if (levelConstant.contains("WhiteHarbor")) {
            return 6;
        } else if (levelConstant.contains("GreywaterWatch")) {
            return 7;
        } else if (levelConstant.contains("TheTwins")) {
            return 8;
        } else if (levelConstant.contains("TheEyrie")) {
            return 9;
        } else if (levelConstant.contains("Riverrun")) {
            return 10;
        } else if (levelConstant.contains("CasterlyRock")) {
            return 11;
        } else if (levelConstant.contains("OldOak")) {
            return 12;
        } else if (levelConstant.contains("Highgarden")) {
            return 13;
        } else if (levelConstant.contains("OldTown")) {
            return 14;
        } else if (levelConstant.contains("RedMountains")) {
            return 15;
        } else if (levelConstant.contains("GhostHill")) {
            return 16;
        } else if (levelConstant.contains("Tarth")) {
            return 17;
        } else if (levelConstant.contains("StormsEnd")) {
            return 18;
        } else if (levelConstant.contains("KingsLanding")) {
            return 19;
        } else {
            return -666;
        }
    }
    
    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin() {
        
        playerBalance += levelBounty;
        Level levelBeingPlayed = ((PathXMiniGame) miniGame).getCurrentLevel();
        
        int levelNumberIndex = getLevelNumber(levelBeingPlayed.getLevelName());
        System.out.println("LEVEL NUMBER: " + levelNumberIndex);
        ArrayList<PathXLevelNode> listNodes = ((PathXMiniGame) miniGame).getLevelList();
        if(listNodes.get(levelNumberIndex).getLevelKey() == 1) {
        listNodes.get(levelNumberIndex).setLevelKey(2);
        if(levelNumberIndex < 19)
        listNodes.get(levelNumberIndex + 1).setLevelKey(1);
        }
        //System.out.println("LEVEL LIST: " + listNodes.get());
        
        ((PathXMiniGame) miniGame).showWinScreenDialog();
        ((PathXMiniGame) miniGame).isPaused = true;
        PathXMiniGame.isDialogWin = true;
         //PathXMiniGame.isDialogClosed = false;
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setEnabled(true);
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setEnabled(true);
       //((PathXMiniGame)miniGame).togglePause();
        // AND PLAY THE WIN AUDIO
//        miniGame.getAudio().stop(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString());
//        miniGame.getAudio().stop(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString());
//      
//        miniGame.getAudio().play(SortingHatPropertyType.AUDIO_CUE_WIN.toString(), false);
        
    }
    
    /**
     * Updates the game to reflect the player has lost the level
     */
    public void endGameAsLoss() {
        ((PathXMiniGame) miniGame).showLossScreenDialog();
         ((PathXMiniGame) miniGame).isPaused = true;
         PathXMiniGame.isDialogLose = true;
         //PathXMiniGame.isDialogClosed = false;
        //FIX THIS PROBLEM
      //  ((PathXMiniGame) miniGame).togglePause();
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setEnabled(true);
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        ((PathXMiniGame) miniGame).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setEnabled(true);
      
    }

    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game)
    {
        /*
        // PUT ALL THE TILES IN ONE PLACE AND MAKE THEM VISIBLE
        moveAllTilesToStack();

        // RESET THE BAD SPELLS COUNTER
        badSpellsCounter = 0;

        // RANDOMLY ORDER THEM
        moveAllTilesToStack();
        Collections.shuffle(stackCars);        
        for (PathXCar tile : stackCars)
        {
            tile.setX(TEMP_TILE_X);
            tile.setY(TEMP_TILE_Y);
            tile.setState(PathXCarState.VISIBLE_STATE.toString());
        }

        // SEND THE TILES OFF TO THE GRID TO BE SORTED
        for (int i = 0; i < snake.size(); i++)
        {
            PathXCar tileToPlace = stackCars.remove(stackCars.size() - 1);
            tilesToSort.add(tileToPlace);
            SnakeCell sC = snake.get(i);
            int targetX = this.calculateGridTileX(sC.col);
            int targetY = this.calculateGridTileY(sC.row);
            tileToPlace.setTarget(targetX, targetY);
            tileToPlace.setGridCell(sC.col, sC.row);
            tileToPlace.startMovingToTarget(MAX_TILE_VELOCITY);
            tileToPlace.setState(PathXCarState.VISIBLE_STATE.toString());
            movingCars.add(tileToPlace);
        }
*/
        // GENERATE THE PROPER SORT TRANSACTIONS
        ////properTransactionOrder = sortingAlgorithm.generateFlyingTransactions();
      //  this.transactionCounter = 0;

        // START THE CLOCK
        startTime = new GregorianCalendar();

        // AND START ALL UPDATES
        beginGame();

        // CLEAR ANY WIN OR LOSS DISPLAY
       // miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
       // miniGame.getGUIDialogs().get(STATS_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
    }

    public static int numEntered = 0;
    public static int callController = 0;
    public static int callerControl = 0;
    
    public void checkPlayerPosition() {
        if (!playerCar.isEmpty()) {

            if (playerCar.get(0).isAtEndLocation) {
                System.out.println("YOU REALLY WIN!! " + ++numEntered);
                endGameAsWin();
                callController = 1;
            }
        }
    }
    
    public void checkPlayerBounty() {
        if(levelBounty == 0) {
            System.out.println("YOU LOSE!! ");
            endGameAsLoss();
            callerControl = 1;
        }
    }
    
    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game PathX game to be updated.
     */
    @Override
    public void updateAll(MiniGame game)
    {
        try
        {

            
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();

            if(callController == 0 && ((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE))
            checkPlayerPosition();
            
            if(callerControl == 0 && ((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE))
            checkPlayerBounty();
            
            
            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingPolice.size(); i++)
            {
                // GET THE NEXT TILE
                PoliceCar tile = movingPolice.get(i);

                if (!movingPlayerCar.isEmpty()) {

                    if ((tile.getX() + 2 >= movingPlayerCar.get(0).getX() && tile.getX() - 2 <= movingPlayerCar.get(0).getX())
                            && (tile.getY() + 2 >= movingPlayerCar.get(0).getY() && tile.getY() - 2 <= movingPlayerCar.get(0).getY())) {
                        System.out.println("TOUCHED POLICE");
                        levelBounty = 0;
                        playerBalance -= playerBalance/10;
                        System.out.println("playerBalance/10: " + playerBalance/10);
                        miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_CHEAT.toString(), false);
                        movingPlayerCar.clear();
                        return;
                    }
                }
                
                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingPolice.remove(tile);
                }
            }
             // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingZombies.size(); i++)
            {
                // GET THE NEXT TILE
                ZombieCar tile = movingZombies.get(i);

                if (!movingPlayerCar.isEmpty()) {

                    if ((tile.getX() + 2 >= movingPlayerCar.get(0).getX() && tile.getX() - 2 <= movingPlayerCar.get(0).getX())
                            && (tile.getY() + 2 >= movingPlayerCar.get(0).getY() && tile.getY() - 2 <= movingPlayerCar.get(0).getY())) {
                        if((int)movingPlayerCar.get(0).getCarVelocity()/2 == 0) {
                            System.out.println("You lose!");
                          endGameAsLoss();
                          return;
                         // movingPlayerCar.get(0).resetCarVelocity();
                        }
                        movingPlayerCar.get(0).setCarVelocity(movingPlayerCar.get(0).getCarVelocity() - (int)movingPlayerCar.get(0).getCarVelocity()/2);
                        System.out.println("This: " + movingPlayerCar.get(0).getCarVelocity());
                        System.out.println("Minus This: " + (int)movingPlayerCar.get(0).getCarVelocity()/2);
                        miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_CHEAT.toString(), false);

                    }
                }
                
                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingZombies.remove(tile);
                }
            }
             // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingBandits.size(); i++)
            {
                // GET THE NEXT TILE
                BanditCar tile = movingBandits.get(i);
               
                if (!movingPlayerCar.isEmpty()) {

                    if ((tile.getX() + 2 >= movingPlayerCar.get(0).getX() && tile.getX() - 2 <= movingPlayerCar.get(0).getX())
                            && (tile.getY() + 2 >= movingPlayerCar.get(0).getY() && tile.getY() - 2 <= movingPlayerCar.get(0).getY())) {
                        levelBounty -= (levelBounty * (.1));
                    
                        miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_CHEAT.toString(), false);

                    }
                }

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingBandits.remove(tile);
                }
            }
            
               for (int i = 0; i < movingPlayerCar.size(); i++)
            {
                // GET THE NEXT TILE
                PlayerCar tile = movingPlayerCar.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);
                
                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingPlayerCar.remove(tile);
                }
            }

            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
            if (inProgress())
            {
                // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                endTime = new GregorianCalendar();
            }
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
            
        }
    }
    
    public static int numTimesEntered = 0;

    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Sorting Hat game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game)
    {
    }

    public Level getLevel() {
        return currentLevel;
    }
    
    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage)
    {
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        level.backgroundImageFileName = newBgImage;
        BufferedImage img;
         PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        String leve1MapImage = props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_1);
       
        //img = ((PathXMiniGame)miniGame).loadImage(imgPath + leve1MapImage);
        backgroundImage = ((PathXMiniGame)miniGame).loadImage(imgPath + leve1MapImage);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setLevelDimensions(levelWidth, levelHeight);
        //repaint();
    }

    public void updateStartingLocationImage(String startImageName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateDestinationImage(String destImageName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
      /**
     * Unselects any intersection or road that might be selected.
     */
    public void unselectEverything()
    {
        selectedIntersection = null;
        selectedRoad = null;
        startRoadIntersection = null;
        miniGame.getCanvas().repaint();
    }

    /**
     * Searches to see if there is a road at (canvasX, canvasY), and if
     * there is, it selects and returns it.
     */
    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX+viewport.x, canvasY+viewport.y);
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
                this.selectedRoad = r;
                //mode = PathXCarState.SELECTED_STATE;
                return selectedRoad;
            }
        }
        return null;
    }

    /**
     * Checks to see if (canvasX, canvasY) is free (i.e. there isn't
     * already an intersection there, and if not, adds one.
     */
//    public void addIntersectionAtCanvasLocation(int canvasX, int canvasY)
//    {
//        // FIRST MAKE SURE THE ENTIRE INTERSECTION IS INSIDE THE LEVEL
//        if ((canvasX - INTERSECTION_RADIUS) < 0) return;
//        if ((canvasY - INTERSECTION_RADIUS) < 0) return;
//        if ((canvasX + INTERSECTION_RADIUS) > viewport.levelWidth) return;
//        if ((canvasY + INTERSECTION_RADIUS) > viewport.levelHeight) return;
//        
//        // AND ONLY ADD THE INTERSECTION IF IT DOESN'T OVERLAP WITH
//        // AN EXISTING INTERSECTION
//        for(Intersection i : level.intersections)
//        {
//            double distance = calculateDistanceBetweenPoints(i.x-viewport.x, i.y-viewport.y, canvasX, canvasY);
//            if (distance < INTERSECTION_RADIUS)
//                return;
//        }          
//        
//        // LET'S ADD A NEW INTERSECTION
//        int intX = canvasX + viewport.x;
//        int intY = canvasY + viewport.y;
//        Intersection newInt = new Intersection(intX, intY);
//        level.intersections.add(newInt);
//        miniGame.getCanvas().repaint();
//    }
    
     /**
     * Calculates and returns the distance between two points.
     */
    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2)
    {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }
    
    public boolean isNothingSelected()      { return editMode == PathXCarState.NOTHING_SELECTED; }
    public boolean isIntersectionSelected() { return editMode == PathXCarState.INTERSECTION_SELECTED; }
    public boolean isIntersectionDragged()  { return editMode == PathXCarState.PLAYER_DRAGGED; }
    public boolean isRoadSelected()         { return editMode == PathXCarState.ROAD_SELECTED; }
}