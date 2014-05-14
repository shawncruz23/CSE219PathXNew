/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pathx.PathX;
import static pathx.PathXConstants.*;
//import static pathx.PathXConstants.VIEWPORT_INC;
import pathx.data.Connection;
import pathx.data.Intersection;
import pathx.data.Level;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevelNode;
import pathx.data.Road;
import pathx.file.PathXFileManager;
import pathx.file.PathX_XML_Level_IO;
import pathx.game.PathXGameGraphManager;
import static pathx.ui.PathXMiniGame.*;
import static pathx.ui.PathXPanel.*;
//import sorting_hat.data.SortingHatDataModel;
//import sorting_hat.file.SortingHatFileManager;

/**
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXEventHandler implements MouseListener, MouseMotionListener {

    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;
    
    //PATHX XML LEVEL IO
    private PathX_XML_Level_IO filetoLoad;
    
    private Level eventHandlerCurrentLevel;
    
    private PathXGameGraphManager gameGraph;
    
    private PathXDataModel model;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
        gameGraph = new PathXGameGraphManager();
        model = game.getDataModel();
    }

    /**
     * Called when the user clicks the close window button.
     */
    public void respondToExitRequest() {
        //int closeGame = JOptionPane.YES_NO_OPTION;
        //int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "pathX Exit", closeGame);
        // if (result == 0) {
        /*SAVE GAME IF A GAME IS STARTED*/
        if (game.isCurrentScreenState(GAME_SCREEN_STATE)) {
            game.switchToLevelSelectScreen();
        } else {
            System.exit(0);
        }
        // }
    }
    
    public PathXGameGraphManager getGameGraph() {
        return gameGraph;
    }

    public static ArrayList<Integer> xCoordinates = new ArrayList<>();
    public static ArrayList<Integer> yCoordinates = new ArrayList<>();
    
    public static String levelString = new String();
    public static boolean enteredGameLoss = false;
    
    /**
     * Called when the user clicks on an unlocked/completed level button.
     */
    public void respondToGameplayScreen(String level) {
        levelString = level;
        System.out.println("level name: " + level);
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) || enteredGameLoss) {

            Level aLevel = new Level();
            model.beginGame();
          //  if (!enteredGameLoss) {
                File schemaFile = new File(PROPERTIES_SCHEMA_LEVEL_FILE_NAME);
                filetoLoad = new PathX_XML_Level_IO(schemaFile);
                aLevel = filetoLoad.loadLevel(level);
                eventHandlerCurrentLevel = aLevel;
                model.setCurrentLevel(aLevel);
                //if()
                ArrayList<Intersection> intersectionList = filetoLoad.getIntersectionList();
                ArrayList<Road> roadList = filetoLoad.getRoadList();
                for (int i = 0; i < intersectionList.size(); i++) {
                    gameGraph.addIntersections(intersectionList.get(i));
                    xCoordinates.add(intersectionList.get(i).x);
                    yCoordinates.add(intersectionList.get(i).y);
                }
                for (int i = 0; i < roadList.size(); i++) {
                    gameGraph.addRoad(roadList.get(i));
                }

         //   }     
          
//            ArrayList<Connection> pathToHome = gameGraph.findShortestPathToHome(aLevel.getStartingLocation(), aLevel.getDestination());
//   
//            System.out.println("PATH TO HOME:\n START: ");
//             for (int i = 0; i < pathToHome.size(); i++) {
//                 //System.out.println(pathToHome.get(i).toString() + "\n");
//                 System.out.println("(X: " + gameGraph.getIntersection(pathToHome.get(i).getIntersection1Id()).x
//                         + " Y: " + gameGraph.getIntersection(pathToHome.get(i).getIntersection1Id()).y + ")"
//                         + ", (X: " + gameGraph.getIntersection(pathToHome.get(i).getIntersection2Id()).x
//                         + " Y: " + gameGraph.getIntersection(pathToHome.get(i).getIntersection2Id()).y + ")");
//                 //System.out.println("\nROAD WEIGHT: " + gameGraph.getRoad(pathToHome.get(i).getRoadId()).getRoadweight());
//             }
             
    
            model.setLevelBounty(aLevel.getMoney());
             
            System.out.println("MONEY: " + aLevel.getMoney());
            System.out.println("NUM POLICE: " + aLevel.getNumPolice());
            System.out.println("NUM BANDITS: " + aLevel.getNumBandits());
            System.out.println("NUM ZOMBIES: " + aLevel.getNumZombies());
            System.out.println("Starting Location Image: " + aLevel.getStartingLocationImageFileName());
                //System.out.println("succesful");
            //}
            //else System.out.println("unsuccesful");
        game.switchToGameplayScreen(aLevel);
        
        
        enteredGameLoss = false;
        }
    }

     /**
     * Called when the user clicks on an home button.
     */
    public void respondToHomeRequest() {
        System.out.println("Responding to go home");
        game.switchToHomeScreen();
    }
    
    
    public Intersection intersectionClickerCheck(int x, int y) {
        
        Intersection destinationIntersection;
        int error = 30;
        int diffX = x; //- 80; //- (int)sourceX1;
        int diffY = y;// + 10; //- (int)sourceY1;
        ArrayList<Intersection> node = ((PathXMiniGame) game).getCurrentLevel().getIntersections();
        for (int i = 0; i < node.size(); i++) {
//            while(i < 1){
//                for(int j = 0; j < node.size(); j++)
//                //System.out.println("Value: " + node.get(j).toString());
//            }
//            System.out.println("clickX >= nodeX: " + (((diffX + error) >= node.get(i).x)));
//            System.out.println("clickX <= nodeX: " + (((diffX - error) <= node.get(i).x)));
//            System.out.println("clickY >= nodeY: " + (((diffY + error) >= node.get(i).y)));
//            System.out.println("clickY >= nodeY: " + (((diffY - error) <= node.get(i).y)));
           // System.out.println("Difference: X: " + (x - node.get(i).x));
            //System.out.println("Difference: Y: " + (y - node.get(i).y));
            if (((diffX + error) >= node.get(i).x)
                    && ((diffX - error) <= node.get(i).x)
                    && ((diffY + error) >= node.get(i).y)
                    && ((diffY - error) <= node.get(i).y)) {
                System.out.println("Coordinates: " + node.get(i).toString());
                System.out.println("Send to: X: " + x + " Y: " + y);
                destinationIntersection = node.get(i);
                checker++;
                playerX = x;
                playerY = y;
                return destinationIntersection;

            }
        }
        return null;
    }

     /**
     * Called when the user clicks on an arrow/directional button.
     * Adjusts the coordinates for GAME map source
     * coordinates.
     * @param direction direction in which the user desires to venture
     */
    public void adjustGameScrollScreen(String direction) {

        switch (direction) {
            case UP_BUTTON_DIRECTION: //max(sy1,sy2) bounds
                if ((sourceY1 < 40) && (sourceY2 < 446)) {
                    //FOR MAP
                    sourceY1 += 20;//SOURCE_Y_SCROLL_UP;
                    sourceY2 += 20;//SOURCE_Y_SCROLL_UP;
//                    System.out.println("sourceY1: " + sourceY1 +" > "
//                            + "SOURCE_Y1_MAX_COORD: " + (SOURCE_Y1_MAX_COORD-30));
//                     System.out.println("sourceY2: " + sourceY2 +" > "
//                            + "SOURCE_Y2_MAX_COORD: " + (SOURCE_Y2_MAX_COORD-30));
                    for(int i = 0; i < 1; i++) {
                   // model.getPlayerCar().get(0).increaseY();
                    }
                }
                break;
            case DOWN_BUTTON_DIRECTION: //min(sy1,sy2)
                if ((sourceY1 > -140/*SOURCE_Y1_MIN_COORD*/) && (sourceY2 > 266/*SOURCE_Y2_MIN_COORD*/)) {
                    //FOR MAP
                    sourceY1 -= 20;//SOURCE_Y_SCROLL_DOWN;
                    sourceY2 -= 20;//SOURCE_Y_SCROLL_DOWN;
//                    System.out.println("sourceY1: " + sourceY1 + " > "
//                            + "SOURCE_Y1_MIN_COORD: " + -140);
//                    System.out.println("sourceY2: " + sourceY2 + " > "
//                            + "SOURCE_Y2_MIN_COORD: " + 266);
                    for(int i = 0; i < 1; i++) {
                   // model.getPlayerCar().get(0).decreaseY();
                    }
                }
                break;
            case RIGHT_BUTTON_DIRECTION: //max(sx1,sx2)
               if ((sourceX1 > -560/*40*/) && (sourceX2 > 252/*670*/)) {
                    //FOR MAP
                    sourceX1 -= 20;
                    sourceX2 -= 20;
//                   System.out.println("sourceX1: " + sourceX1 + " > "
//                           + "SOURCE_X1_MAX_COORD: " + -560);
//                   System.out.println("sourceX2: " + sourceX2 + " > "
//                           + "SOURCE_X2_MAX_COORD: " + 252);
                    for(int i = 0; i < 1; i++) {
                    //model.getPlayerCar().get(0).decreaseX();
                    }
                }
                break;
            case LEFT_BUTTON_DIRECTION: //min(sx1,sx2)
                if ((sourceX1 < 100) && (sourceX2 < 912)) {
                    //FOR MAP
                    sourceX1 += 20;
                    sourceX2 += 20;
//                    System.out.println("sourceX1: " + sourceX1 + " < "
//                            + "SOURCE_X1_MIN_COORD: " + 100);
//                    System.out.println("sourceX2: " + sourceX2 + " < "
//                            + "SOURCE_X2_MIN_COORD: " + 912);
                       for(int i = 0; i < 1; i++) {
                     // model.getPlayerCar().get(0).increaseX();
//                           System.out.println("X: " +  model.getPlayerCar().get(0).getX());
//                           System.out.println("Y: " +  model.getPlayerCar().get(0).getY());
                    }
                }
                break;
        }
    }

     /**
     * Called when the user clicks on an arrow/directional button.
     * Adjusts the coordinates for level buttons and the map source
     * coordinates.
     * @param direction direction in which the user desires to venture
     */
    public void adjustScrollScreen(String direction) {

        ArrayList<PathXLevelNode> levelNodeList = game.getLevelList();
        
        //System.out.println("Direction String Value: " + direction);
        switch (direction) {
            case UP_BUTTON_DIRECTION: //max(sy1,sy2) bounds
                if ((sy1 > SOURCE_Y1_MAX_COORD) && (sy2 > SOURCE_Y2_MAX_COORD)) {
                    //FOR MAP
                    sy1 -= SOURCE_Y_SCROLL_UP;
                    sy2 -= SOURCE_Y_SCROLL_UP;
                    
                    for(int i = 0; i < levelNodeList.size(); i++) {
                      double incrementY = levelNodeList.get(i).getYCoordinate() + LEVEL_BUTTONS_Y_SCROLL_UP;
                      levelNodeList.get(i).setYCoordinate(incrementY);  
                    }
                 
                }
                break;
            case DOWN_BUTTON_DIRECTION: //min(sy1,sy2)
                if ((sy1 < SOURCE_Y1_MIN_COORD) && (sy2 < SOURCE_Y2_MIN_COORD)) {
                    //FOR MAP
                    sy1 += SOURCE_Y_SCROLL_DOWN;
                    sy2 += SOURCE_Y_SCROLL_DOWN;
                    
                    for(int i = 0; i < levelNodeList.size(); i++) {
                      double incrementY = levelNodeList.get(i).getYCoordinate()-20;
                      levelNodeList.get(i).setYCoordinate(incrementY);  
                    }          
                }
                break;
            case RIGHT_BUTTON_DIRECTION: //max(sx1,sx2)
                if ((sx1 < 40) && (sx2 < 670)) {
                    //FOR MAP
                    sx1 += 20;
                    sx2 += 20;
                    
                     for(int i = 0; i < levelNodeList.size(); i++) {
                      double decrementX = levelNodeList.get(i).getXCoordinate()-20;
                      levelNodeList.get(i).setXCoordinate(decrementX);  
                    }
                }
                break;
            case LEFT_BUTTON_DIRECTION: //min(sx1,sx2)
                if ((sx1 > 0) && (sx2 > 620)) {
                    //FOR MAP
                    sx1 -= 20;
                    sx2 -= 20;
                    
                    for(int i = 0; i < levelNodeList.size(); i++) {
                      double incrementX = levelNodeList.get(i).getXCoordinate()+20;
                      levelNodeList.get(i).setXCoordinate(incrementX);  
                    }
                }
                break;
        }

    }

    /**
     * In the situation where the user loses and chooses
     * the TRY AGAIN button, restart level
     */
    public void respondToTryAgainRequest() {
        //game.switchToLevelSelectScreen();
        //game.switchToGameplayScreen(model.getLevel());
        //System.out.println("LEVEL NAME: " + model.getLevel().getLevelName());
//        ((PathXMiniGame)game).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//        ((PathXMiniGame)game).getGUIButtons().get(BUTTON_LEAVE_TOWN_TYPE).setEnabled(false);
//        ((PathXMiniGame)game).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//        ((PathXMiniGame)game).getGUIButtons().get(BUTTON_TRY_AGAIN_TYPE).setEnabled(false);
        PathXMiniGame.isDialogClosed = false;
        PathXMiniGame.isDialogWin = false;
         PathXMiniGame.isDialogLose = false;
        enteredGameLoss = true;
        respondToGameplayScreen(levelString);
    } 
    /**
     * In the situation where the user loses and chooses
     * the LEAVE TOWN button, switch to LEVEL SELECT SCREEN
     */
    public void respondToLeaveTownRequest() {
        PathXMiniGame.isDialogClosed = false;
        PathXMiniGame.isDialogWin = false;
         PathXMiniGame.isDialogLose = false;
        game.switchToLevelSelectScreen();
    }
    
    public void respondToScrollRequest(String arrowType) {
        System.out.println(arrowType);
        switch (arrowType) {
            case UP_ARROW_BUTTON_TYPE:
               // System.out.println("Up arrow was pressed");
                game.scrollToTheNorth();
                break;
            case DOWN_ARROW_BUTTON_TYPE:
               // System.out.println("Down arrow was pressed");
                game.scrollToTheSouth();
                break;
            case RIGHT_ARROW_BUTTON_TYPE:
               // System.out.println("Right arrow was pressed");
                game.scrollToTheEast();
                break;
            case LEFT_ARROW_BUTTON_TYPE:
               // System.out.println("Left arrow was pressed");
                game.scrollToTheWest();
                break;
            default:
                System.out.println("No valid arrow was pressed. Invalid String");
                break;
        }
    }

    /**
     * Called when the user clicks the New button.
     */
    public void respondToNewGameRequest() {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            //game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        game.reset();
    }

    public void respondToStartRequest() {
        game.setGameToStart();
    }
    
    public void respondToBackRequest() {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        //  if (game.getDataModel().inProgress())
        // {
        //if( game.isCurrentScreenState(GAME_SCREEN))

        //  if(game.
        //game.getDataModel().endGameAsLoss();

        //game.switchToSplashScreen();
        //  }
        // RESET THE GAME AND ITS DATA
        // game.reset();        
    }

    public void respondToUndoRequest() {

        PathXDataModel data = (PathXDataModel) game.getDataModel();
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS

        // ONLY DO THIS IF THE GAME IS NO OVER
        if (game.getDataModel().inProgress()) {
            // FIND A MOVE IF THERE IS ONE
            // SortTransaction theSwap = data.updateUndo();
            //  if(theSwap!=null)
            {
                //      data.swapTiles(theSwap.getFromIndex(), theSwap.getToIndex());
                //      game.getAudio().play(TheSortingHat.SortingHatPropertyType.AUDIO_CUE_CHEAT.toString(), false);
            }
        }

    }

    /**
     * Called when the user clicks a button to select a level.
     * @param gameMenuType
     */
    public void respondToSelectGameMenuRequest(String gameMenuType) {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(HOME_SCREEN_STATE)) {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            // UPDATE THE DATA
            //PathXFileManager fileManager = game.getFileManager();
            //fileManager.loadLevel(levelFile);
            //data.reset(game);
            // GO TO THE GAME
            //CALL .contains() METHOD ON gameMenuType
            System.out.println("BUTTON PRESSED: " + gameMenuType);
            if (gameMenuType.contains(PLAY_BUTTON_OPTION)/*gameMenuType.equals("./data/./pathx/ButtonPlay.png")*/) {
                game.switchToLevelSelectScreen();
            } else if (gameMenuType.contains(RESET_BUTTON_OPTION)) {

                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "pathX Reset", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    System.out.println("Succesful");
                   //RESET GAME INFO HERE
                    //game.reset();
                }
            } else if (gameMenuType.contains(SETTINGS_BUTTON_OPTION)) {
                game.switchToSettingsScreen();
            } else if (gameMenuType.contains(HELP_BUTTON_OPTION)) {
                game.switchToHelpScreen();
            }
        }
    }

    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest() {
        // DISPLAY THE STATS
        // game.displayStats();        
    }

    public void respondToCloseDialogRequest() {
        game.setDialogInvisible();
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     * @param keyCode
     */
    public void respondToKeyPress(int keyCode) {
        //PathXDataModel data = (PathXDataModel) game.getDataModel();

        //SCROLL UP
        if (keyCode == KeyEvent.VK_UP) {

            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
                   game.isCurrentScreenState(GAME_SCREEN_STATE)) {
              //  System.out.println("Up button key pressed");
                game.scrollToTheNorth();
            }
        } else //SCROLL DOWN
        if (keyCode == KeyEvent.VK_DOWN) {

            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
                    game.isCurrentScreenState(GAME_SCREEN_STATE)) {
                //System.out.println("Down button key pressed");
                game.scrollToTheSouth();
            }
        } else //SCROLL LEFT
        if (keyCode == KeyEvent.VK_LEFT) {

            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)||
                    game.isCurrentScreenState(GAME_SCREEN_STATE)) {
                //System.out.println("Left button key pressed");
                game.scrollToTheWest();
            }
        } else //SCROLL RIGHT
        if (keyCode == KeyEvent.VK_RIGHT) {

            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)||
                    game.isCurrentScreenState(GAME_SCREEN_STATE)) {
                //System.out.println("Right button key pressed");
                game.scrollToTheEast();
            }
        } else //SETS MONEY TO $0 FOR TESTING
        if (keyCode == KeyEvent.VK_L) {

            if (game.isCurrentScreenState(GAME_SCREEN_STATE)) {
                System.out.println("MONEY CHUTE");
                model.setLevelBounty(0);
            } 
        }
        else //INCREASES PLAYER BALANCE FOR TESTING
        if (keyCode == KeyEvent.VK_9) {

            if (game.isCurrentScreenState(GAME_SCREEN_STATE) ||
                    game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("MONEY GETTER");
                model.setPlayerBalance(moneyCheat);
            } 
        }
         else //UNLOCKS ALL LEVELS FOR PLAYER
        if (keyCode == KeyEvent.VK_1) {

            if (game.isCurrentScreenState(GAME_SCREEN_STATE) ||
                    game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("LEVEL BEATER");
                cheatIsEnabled = !cheatIsEnabled;
            } 
        }

    }
    
    public int moneyCheat = 100;
    public static boolean cheatIsEnabled = false;

    /**
     * Called when the user presses the pause/resume button
     */
    public void respondToPauseRequest() {
        game.togglePause();
    }

    /**
     * Called when the user presses a check box on the settings screen
     */
    public void respondToCheckRequest(String boxType) {
        game.toggleCheckBox(boxType);
    }

     /**
     * Responds to when the user presses the mouse button on the canvas,
     * it may respond in a few different ways depending on what the 
     * current edit mode is.
     */
    @Override
    public void mousePressed(MouseEvent me)
    {
         //model = (PathXDataModel)game.getDataModel();
        
        // MAKE SURE THE CANVAS HAS FOCUS SO THAT IT
        // WILL PROCESS THE PROPER KEY PRESSES
        ((JPanel)me.getSource()).requestFocusInWindow();
        
        System.out.println("REACHED mousePressed");
        
        // THESE ARE CANVAS COORDINATES
        int canvasX = me.getX();
        int canvasY = me.getY();
        
        // IF WE ARE IN ADDING INTERSECTION EDIT MODE
        //if (model.isAddingIntersection())
        {
            // TRY ADDING AN INTERSECTION
          //  model.addIntersectionAtCanvasLocation(canvasX, canvasY);
        }
        // IF WE ARE IN ONE OF THESE MODES WE MAY WANT TO SELECT
        // ANOTHER INTERSECTION ROAD
        //else
        if (model.isNothingSelected()
                || model.isIntersectionSelected()
                || model.isRoadSelected())
        {
            // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
            Intersection i = model.findIntersectionAtCanvasLocation(canvasX, canvasY);
            if (i != null)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                model.setSelectedIntersection(i);
                model.switchState(PathXCarState.PLAYER_DRAGGED);
                return;
            }                      
            
            // IF NO INTERSECTION WAS SELECTED THEN CHECK TO SEE IF 
            // THE USER IS SELECTING A ROAD
            Road r = model.selectRoadAtCanvasLocation(canvasX, canvasY);
            if (r != null)
            {
                // MAKE THIS THE SELECTED ROAD
                model.setSelectedRoad(r);
                model.switchState(PathXCarState.ROAD_SELECTED);
                return;
            }

            // OTHERWISE DESELECT EVERYTHING
            model.unselectEverything();            
        }
       
    }
//    
//    /**
//     * This method is called when the user releases the mouse button
//     * over the canvas. This will end intersection dragging if that is
//     * currently happening.
//     */
//    @Override
//    public void mouseReleased(MouseEvent me)
//    {
//        // IF WE ARE CURRENTLY DRAGGING AN INTERSECTION
//        PathXCarState editMode = model.getEditMode();
//        if (editMode == PXLE_EditMode.INTERSECTION_DRAGGED)
//        {
//            // RELEASE IT, BUT KEEP IT AS THE SELECTED ITEM
//            model.switchEditMode(PXLE_EditMode.INTERSECTION_SELECTED);
//        }
//    }

    /**
     * This method will be used to respond to right-button mouse clicks
     * on intersections so that we can toggle them open or closed.
     */
    @Override
    public void mouseClicked(MouseEvent me)
    {
        
        // RIGHT MOUSE BUTTON IS TO TOGGLE OPEN/CLOSE INTERSECTION
        if (me.getButton() == MouseEvent.BUTTON3)
        {
            // SEE IF WE CLICKED ON AN INTERSECTION
            Intersection i = model.findIntersectionAtCanvasLocation(me.getX(), me.getY());
            if (i != null)
            {
                // TOGGLE THE INTERSECTION
                i.toggleOpen();
                model.switchState(PathXCarState.NOTHING_SELECTED);
            }            
        }
    }
//
//    /**
//     * This method is called every time the user moves the mouse. We
//     * use this to keep track of where the mouse is at all times on
//     * the canvas, which helps us render the road being added after
//     * the user has selected the first intersection.
//     */
//    @Override
//    public void mouseMoved(MouseEvent me)
//    {
//        // UPDATE THE POSITION
//        model.setLastMousePosition(me.getX(), me.getY());
//    }
//    
//    /**
//     * This function is called when we drag the mouse across the canvas with
//     * the mouse button pressed. We use this to drag items intersections
//     * across the canvas.
//     */
//    @Override
//    public void mouseDragged(MouseEvent me)
//    {
//        // WE ONLY CARE IF WE ARE IN INTERSECTION DRAGGED MODE
//        PXLE_EditMode editMode = model.getEditMode();
//        if (editMode == PXLE_EditMode.INTERSECTION_DRAGGED)
//        {
//            // DRAG IT
//            model.moveSelectedIntersection(me.getX(), me.getY());
//        }    
//    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
