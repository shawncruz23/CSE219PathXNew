/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import pathx.PathX;
import static pathx.PathXConstants.*;
//import static pathx.PathXConstants.VIEWPORT_INC;
import pathx.data.PathXDataModel;
import pathx.file.PathXFileManager;
import static pathx.ui.PathXMiniGame.*;
//import sorting_hat.data.SortingHatDataModel;
//import sorting_hat.file.SortingHatFileManager;

/**
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXEventHandler {

    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
    }

    /**
     * Called when the user clicks the close window button.
     */
    public void respondToExitRequest() {
        //int closeGame = JOptionPane.YES_NO_OPTION;
        //int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "pathX Exit", closeGame);
       // if (result == 0) {
        /*SAVE GAME IF A GAME IS STARTED*/
        if(game.isCurrentScreenState(GAME_SCREEN_STATE)) {
            game.switchToLevelSelectScreen();
        }
        else {
            System.exit(0);
        }
       // }
    }
    
    /**
     * Called when the user clicks on an available level button.
     */
    public void respondToGameplayScreen() {
        game.switchToGameplayScreen();
    }
    
    public void respondToHomeRequest() {
        System.out.println("Responding to go home");
        game.switchToHomeScreen();
    }
    
    public void adjustScrollScreen(String direction) {

        switch (direction) {
            case "Up": //max(sy1,sy2)
                if ((sy1 > 0) && (sy2 > 440)) {
                    //FOR MAP
                    sy1 -= 21.99999999999999;
                    sy2 -= 21.99999999999999;

                    //levels y up
                    yButtonLevel1 += 20;
                    yButtonLevel2 += 20;
                    yButtonLevel3 += 20;
                    yButtonLevel4 += 20;
                    yButtonLevel5 += 20;
                    yButtonLevel6 += 20;
                    yButtonLevel7 += 20;
                    yButtonLevel8 += 20;
                    yButtonLevel9 += 20;
                    yButtonLevel10 += 20;
                    yButtonLevel11 += 20;
                    yButtonLevel12 += 20;
                    yButtonLevel13 += 20;
                    yButtonLevel14 += 20;
                    yButtonLevel15 += 20;
                    yButtonLevel16 += 20;
                    yButtonLevel17 += 20;
                    yButtonLevel18 += 20;
                    yButtonLevel19 += 20;
                    yButtonLevel20 += 20;
                }
                break;
            case "Down": //min(sy1,sy2)
                if ((sy1 < 1073) && (sy2 < 1470)) {
                    //FOR MAP
                    sy1 += 21.99999999999997;
                    sy2 += 21.99999999999997;

                    //level y down
                    yButtonLevel1 -= 20;
                    yButtonLevel2 -= 20;
                    yButtonLevel3 -= 20;
                    yButtonLevel4 -= 20;
                    yButtonLevel5 -= 20;
                    yButtonLevel6 -= 20;
                    yButtonLevel7 -= 20;
                    yButtonLevel8 -= 20;
                    yButtonLevel9 -= 20;
                    yButtonLevel10 -= 20;
                    yButtonLevel11 -= 20;
                    yButtonLevel12 -= 20;
                    yButtonLevel13 -= 20;
                    yButtonLevel14 -= 20;
                    yButtonLevel15 -= 20;
                    yButtonLevel16 -= 20;
                    yButtonLevel17 -= 20;
                    yButtonLevel18 -= 20;
                    yButtonLevel19 -= 20;
                    yButtonLevel20 -= 20;
                }
                break;
            case "Right": //max(sx1,sx2)
                if ((sx1 < 48) && (sx2 < 678)) {
                    //FOR MAP
                    sx1 += 20;
                    sx2 += 20;

                    //level x right
                    xButtonLevel1 -= 20;
                    xButtonLevel2 -= 20;
                    xButtonLevel3 -= 20;
                    xButtonLevel4 -= 20;
                    xButtonLevel5 -= 20;
                    xButtonLevel6 -= 20;
                    xButtonLevel7 -= 20;
                    xButtonLevel8 -= 20;
                    xButtonLevel9 -= 20;
                    xButtonLevel10 -= 20;
                    xButtonLevel11 -= 20;
                    xButtonLevel12 -= 20;
                    xButtonLevel13 -= 20;
                    xButtonLevel14 -= 20;
                    xButtonLevel15 -= 20;
                    xButtonLevel16 -= 20;
                    xButtonLevel17 -= 20;
                    xButtonLevel18 -= 20;
                    xButtonLevel19 -= 20;
                    xButtonLevel20 -= 20;
                }
                break;
            case "Left": //min(sx1,sx2)
                if ((sx1 > 0) && (sx2 > 620)) {
                    //FOR MAP
                    sx1 -= 20;
                    sx2 -= 20;

                    //level x left
                    xButtonLevel1 += 20;
                    xButtonLevel2 += 20;
                    xButtonLevel3 += 20;
                    xButtonLevel4 += 20;
                    xButtonLevel5 += 20;
                    xButtonLevel6 += 20;
                    xButtonLevel7 += 20;
                    xButtonLevel8 += 20;
                    xButtonLevel9 += 20;
                    xButtonLevel10 += 20;
                    xButtonLevel11 += 20;
                    xButtonLevel12 += 20;
                    xButtonLevel13 += 20;
                    xButtonLevel14 += 20;
                    xButtonLevel15 += 20;
                    xButtonLevel16 += 20;
                    xButtonLevel17 += 20;
                    xButtonLevel18 += 20;
                    xButtonLevel19 += 20;
                    xButtonLevel20 += 20;
                }
                break;
        }
        //((PathXMiniGame)game).updateButtons();
    }
    
    public void respondToScrollRequest(String arrowType) {
        System.out.println(arrowType);
        switch (arrowType) {
            case UP_ARROW_BUTTON_TYPE:
                System.out.println("Up arrow was pressed");
                game.scrollToTheNorth();
                break;
            case DOWN_ARROW_BUTTON_TYPE:
                System.out.println("Down arrow was pressed");
                game.scrollToTheSouth();
                break;
            case RIGHT_ARROW_BUTTON_TYPE:
                System.out.println("Right arrow was pressed");
                game.scrollToTheEast();
                break;
            case LEFT_ARROW_BUTTON_TYPE:
                System.out.println("Left arrow was pressed");
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
            game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        game.reset();
    }

    public void respondToBackRequest() {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        //  if (game.getDataModel().inProgress())
        // {
        //if( game.isCurrentScreenState(GAME_SCREEN))

        //  if(game.
        
        game.getDataModel().endGameAsLoss();

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
            System.out.println("dfg: " + gameMenuType);
            if (gameMenuType.contains("Play")/*gameMenuType.equals("./data/./pathx/ButtonPlay.png")*/) {
                game.switchToLevelSelectScreen();
            } else if (gameMenuType.contains("Reset")) {
                int closeGame = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset? GET RID OF ME", "pathX Reset", closeGame);
                if (result == 0) {
                    System.out.println("Succesful");
                   //game.reset();
                }
            } else if (gameMenuType.contains("Settings")) {
                game.switchToSettingsScreen();
            } else if (gameMenuType.contains("Help")) {
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

    /**
     * Called when the user presses a key on the keyboard.
     */
    public void respondToKeyPress(int keyCode) {
        //PathXDataModel data = (PathXDataModel) game.getDataModel();

       //SCROLL UP
        if (keyCode == KeyEvent.VK_UP) {
           
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("Up button key pressed");
                game.scrollToTheNorth();
            }
        }
        else
            //SCROLL DOWN
        if (keyCode == KeyEvent.VK_DOWN) {
     
             if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("Down button key pressed");
                game.scrollToTheSouth();
            }
        }
        else
            //SCROLL LEFT
        if (keyCode == KeyEvent.VK_LEFT) {
     
             if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("Left button key pressed");
                game.scrollToTheWest();
            }
        }
        else
            //SCROLL RIGHT
        if (keyCode == KeyEvent.VK_RIGHT) {
     
             if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                System.out.println("Right button key pressed");
                game.scrollToTheEast();
            }
        }
    }

    void respondToCheckRequest(String boxType) {
       game.toggleCheckBox(boxType);
    }
}