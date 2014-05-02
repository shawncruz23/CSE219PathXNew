package pathx.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import static pathx.PathX.PathXPropertyType.IMAGE_BACKGROUND_HELP;
import static pathx.PathX.PathXPropertyType.IMAGE_GO_TO_HOME_SCREEN;
import pathx.PathXConstants;
import static pathx.PathXConstants.*;
import pathx.data.Level;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevelNode;
import pathx.data.PathXRecord;
import pathx.file.PathXFileManager;
import pathx.file.PathX_XML_Level_IO;
import properties_manager.PropertiesManager;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXMiniGame extends MiniGame {

    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private PathXRecord record;

    // HANDLES GAME UI EVENTS
    private PathXEventHandler eventHandler;

    // HANDLES ERROR CONDITIONS
    private PathXErrorHandler errorHandler;

    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private PathXFileManager fileManager;

    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    // PATHXPANEL
    private PathXPanel gamePanel;
    
    //PATHX XML LEVEL IO
    private PathX_XML_Level_IO filetoLoad;

//    private Insets marginlessInsets;
//
//    // THE TOOLBAR WILL BE AT THE ABOVE THE WORKSPACE
//    // TO PROVIDE FOR NAVIGATION BETWEEN THEM
//    private JPanel northToolbar;
//    private JButton gameButton;
//    private JButton statsButton;
//    private JButton helpButton;
//    private JButton exitButton;
//
//    // THE GAME STATS PANEL WILL DISPLAY GAME STATS
//    private JScrollPane statsScrollPane;
//    private JEditorPane statsPane;
//
//    // THE HELP PANEL WILL EXPLAIN HOW TO PLAY THE
//    // GAME. THIS WILL BE PRESENTED USING AN HTML PAGE
//    private JPanel helpPanel;
//    private JScrollPane helpScrollPane;
//    private JEditorPane helpPane;
//    private JButton homeButton;
    //CHECK BOX CONTROLS
    public static boolean checkedMusic = false;
    public static boolean checkedSound = false;

    //STORES ALL THE LEVELS
    protected ArrayList<PathXLevelNode> levelList;

    //DESTINATION COORDINATES
    public static int dx1 = 10;
    public static int dy1 = 45;
    public static int dx2 = 630;
    public static int dy2 = 450;
    //SOURCE COORDINATES FOR MAP
    public static double sx1 = 0;
    public static double sy1 = 0;
    public static double sx2 = 620;
    public static double sy2 = 440;
    //Level 1: Craster's Keep
    //SOURCE COORDINATES LEVEL 1 BUTTON ON MAP
    public static int xButtonLevel1 = 326;
    public static int yButtonLevel1 = 75;
    //Level 2: Craster's Keep
    //SOURCE COORDINATES LEVEL 2 BUTTON ON MAP
    public static int xButtonLevel2 = 395;
    public static int yButtonLevel2 = 110;
    //Level 3: Last Hearth
    //SOURCE COORDINATES LEVEL 3 BUTTON ON MAP
    public static int xButtonLevel3 = 430;
    public static int yButtonLevel3 = 210;
    //Level 4: Deepwood Motte
    //SOURCE COORDINATES LEVEL 4 BUTTON ON MAP
    public static int xButtonLevel4 = 162;
    public static int yButtonLevel4 = 270;
    //Level 5: Winterfell
    //SOURCE COORDINATES LEVEL 5 BUTTON ON MAP
    public static int xButtonLevel5 = 280;
    public static int yButtonLevel5 = 348;
    //Level 6: Torrhen's Square
    //SOURCE COORDINATES LEVEL 6 BUTTON ON MAP
    public static int xButtonLevel6 = 201;
    public static int yButtonLevel6 = 398;
    //Level 7: White Harbor
    //SOURCE COORDINATES LEVEL 7 BUTTON ON MAP
    public static int xButtonLevel7 = 360;
    public static int yButtonLevel7 = 495;
    //Level 8: Grey Watch
    //SOURCE COORDINATES LEVEL 8 BUTTON ON MAP
    public static int xButtonLevel8 = 271;
    public static int yButtonLevel8 = 618;
    //Level 9: The Twins
    //SOURCE COORDINATES LEVEL 9 BUTTON ON MAP
    public static int xButtonLevel9 = 258;
    public static int yButtonLevel9 = 697;
    //Level 10: The Eyrie
    //SOURCE COORDINATES LEVEL 10 BUTTON ON MAP
    public static int xButtonLevel10 = 483;
    public static int yButtonLevel10 = 725;
    //Level 11: Riverrun
    //SOURCE COORDINATES LEVEL 11 BUTTON ON MAP
    public static int xButtonLevel11 = 256;
    public static int yButtonLevel11 = 824;
    //Level 12: Casterly Rock
    //SOURCE COORDINATES LEVEL 12 BUTTON ON MAP
    public static int xButtonLevel12 = 75;
    public static int yButtonLevel12 = 945;
    //Level 13: Old Oak
    //SOURCE COORDINATES LEVEL 13 BUTTON ON MAP
    public static int xButtonLevel13 = 80;
    public static int yButtonLevel13 = 1100;
    //Level 14: Highgarden
    //SOURCE COORDINATES LEVEL 14 BUTTON ON MAP
    public static int xButtonLevel14 = 180;
    public static int yButtonLevel14 = 1160;
    //Level 15: Old Town
    //SOURCE COORDINATES LEVEL 15 BUTTON ON MAP
    public static int xButtonLevel15 = 93;
    public static int yButtonLevel15 = 1270;
    //Level 16: Red Mountains
    //SOURCE COORDINATES LEVEL 16 BUTTON ON MAP
    public static int xButtonLevel16 = 320;
    public static int yButtonLevel16 = 1300;
    //Level 17: Ghost Hill
    //SOURCE COORDINATES LEVEL 17 BUTTON ON MAP
    public static int xButtonLevel17 = 580;
    public static int yButtonLevel17 = 1300;
    //Level 18: Tarth
    //SOURCE COORDINATES LEVEL 18 BUTTON ON MAP
    public static int xButtonLevel18 = 610;
    public static int yButtonLevel18 = 1075;
    //Level 19: Storm's End
    //SOURCE COORDINATES LEVEL 19 BUTTON ON MAP
    public static int xButtonLevel19 = 550;
    public static int yButtonLevel19 = 1109;
    //Level 20: King's Landing
    //SOURCE COORDINATES LEVEL 20 BUTTON ON MAP
    public static int xButtonLevel20 = 436;
    public static int yButtonLevel20 = 975;

    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    /**
     * Accessor method for getting the player record object, which summarizes
     * the player's record on all levels.
     *
     * @return The player's complete record.
     */
    public PathXRecord getPlayerRecord() {
        return record;
    }

    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public PathXErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public PathXFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Accessor method for getting the game's level list.
     *
     * @return The file manager.
     */
    public ArrayList<PathXLevelNode> getLevelList() {
        return levelList;
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    // VIEWPORT UPDATE METHODS
    // - initViewport
    // - scroll
    // - moveViewport
    // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameScreen
    // - switchToSplashScreen
    // - updateBoundaries
    /**
     * This method displays makes the stats dialog display visible, which
     * includes the text inside.
     *
     * public void displayStats() { // MAKE SURE ONLY THE PROPER DIALOG IS
     * VISIBLE
     * guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
     * guiDialogs.get(STATS_DIALOG_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
     * }
     */
    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord() {
        fileManager.saveRecord(record);
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToLevelSelectScreen() {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        ArrayList<String> homeScreens = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        for (String homeScreen : homeScreens) {
            guiButtons.get(homeScreen).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(homeScreen).setEnabled(false);
        }

        if (isCurrentScreenState(GAME_SCREEN_STATE)) {
            //disable/reset all game screen controls
            PathXPanel.sourceX1 = 0;
            PathXPanel.sourceY1 = 0;
            PathXPanel.sourceX2 = 620;
            PathXPanel.sourceY2 = 440;
            audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);

      //  if (isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
//            for (int i = 0; i < levelList.size(); i++) {
//
//                guiButtons.get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                if ((levelList.get(i).getXCoordinate() > 10) && (levelList.get(i).getXCoordinate() < 630)
//                        && (levelList.get(i).getYCoordinate() > 45) && (levelList.get(i).getYCoordinate() < 450)) {
//                    guiButtons.get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                    guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(true);
//
//                    if (levelList.get(i).getLevelState().equals(PathXCarState.RED_STATE.toString())) {
//
//                        guiButtons.get(levelList.get(i).getLeveliD()).setActionListener(new ActionListener() {
//                            public void actionPerformed(ActionEvent ae) {
//                                System.out.println("YOU SELECTED THE RED BUTTON!");
//                                eventHandler.respondToGameplayScreen();
//                            }
//                        });
//                    }
//                // s = new Sprite(sT, xButtonLevel1, yButtonLevel1, 0, 0, PathXCarState.VISIBLE_STATE.toString());
//                    //game.getGUIButtons().put(LEVEL_SELECT_BUTTON_TYPE, s);
//                } else {
//                    guiButtons.get(levelList.get(i).getLeveliD()).setState(PathXCarState.INVISIBLE_STATE.toString());
//                    guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                }
//            }
        //}
        //ENABLE ARROWS
        guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_ARROW_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(UP_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setEnabled(true);

        //ENABLE HOME BUTTON
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setEnabled(true);

        //PropertiesManager props = PropertiesManager.getPropertiesManager();
        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        //guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        // guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        // guiButtons.get(BACK_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        // guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        //guiButtons.get(UNDO_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        // guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(true);
        // guiDecor.get(MISCASTS_COUNT_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        //guiDecor.get(TIME_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        //guiButtons.get(STATS_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        // guiButtons.get(STATS_BUTTON_TYPE).setEnabled(true);
        // guiDecor.get(ALGORITHM_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        // ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
        // for (String level : levels)
        //  {
        //     guiButtons.get(level).setState(PathXCarState.INVISIBLE_STATE.toString());
        //     guiButtons.get(level).setEnabled(false);
        //  }
        // AND CHANGE THE SCREEN STATE
        currentScreenState = LEVEL_SELECT_SCREEN_STATE;

        // PLAY THE LEVEL SELECT SCREEN SONG
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString(), true);
    }
    
    private Level currentLevel;
    
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * This method switches the application to the Gameplay screen, making all
     * the appropriate UI controls
     * @param aLevel visible & invisible.
     */
    public void switchToGameplayScreen(Level aLevel) {

        currentLevel = aLevel;
        
         //DISABLE CHECKBOXES
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(false);

        //DISABLE CHECKBOXES
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(false);

        
        //DISABLE ARROWS
        if (isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
//            guiButtons.get(UP_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//            guiButtons.get(UP_ARROW_BUTTON_TYPE).setEnabled(false);
//            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setEnabled(false);
//            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setEnabled(false);
//            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setEnabled(false);
            //DISABLE ALL LEVEL SELECT BUTTONS
            for (int i = 1; i < 21; i++) {
                String level = "LEVEL_";
                int levelNumber = i;
                String buttonType = "_SELECT_BUTTON_TYPE";
                String concatenation = level + levelNumber + buttonType;
                guiButtons.get(concatenation).setState(PathXCarState.INVISIBLE_STATE.toString());
                guiButtons.get(concatenation).setEnabled(true);
            }
//       
        }
        
        

        //ENABLE HOME BUTTON
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setEnabled(true);

        // CHANGE THE BACKGROUND STATE
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        currentScreenState = GAME_SCREEN_STATE;

        // PLAY THE GAMEPLAY SCREEN SONG
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);

        JOptionPane.showMessageDialog(null, "Info About " + currentLevel.getLevelName());
        //JOptionPane.showMessageDialog(null, "pathX Info Dialog", "<Info About Level 1>", JOptionPane.CLOSED_OPTION);
        //JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "pathX Info Dialog", JOptionPane.CLOSED_OPTION);
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToHomeScreen() {

        // WE'RE GOING TO USE IMAGES FOR ALL BUTTONS,
        // SO WE'LL SET THIS AS THE MARGIN FOR ALL OF THEM
        //marginlessInsets = new Insets(0, 0, 0, 0);
        //DISABLE MAP
//        if(isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
//        guiDecor.get(MAP_GAME_IMAGE_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
//        guiDecor.get(MAP_GAME_IMAGE_TYPE).setEnabled(false);
//        }
        //DISABLE ARROWS
        if (isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) || isCurrentScreenState(GAME_SCREEN_STATE)) {
            guiButtons.get(UP_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(UP_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setEnabled(false);
            //DISABLE ALL LEVEL SELECT BUTTONS
            for (int i = 1; i < 21; i++) {
                String level = "LEVEL_";
                int levelNumber = i;
                String buttonType = "_SELECT_BUTTON_TYPE";
                String concatenation = level + levelNumber + buttonType;
                guiButtons.get(concatenation).setState(PathXCarState.INVISIBLE_STATE.toString());
                guiButtons.get(concatenation).setEnabled(true);
            }
            //filetoLoad.loadLevel();
        }

        //DISABLE CHECKBOXES
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(false);

        //DISABLE CHECKBOXES
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(false);

        //ENABLE Home DESCRIPTION
        guiButtons.get(HELP_GAME_DESCRIPTION_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_GAME_DESCRIPTION_TYPE).setEnabled(false);

        // CHANGE THE BACKGROUND STATE
        guiDecor.get(BACKGROUND_TYPE).setState(HOME_SCREEN_STATE);

        //Enable Home Screen options
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> homeScreens = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        for (String homeScreen : homeScreens) {
            guiButtons.get(homeScreen).setState(PathXCarState.VISIBLE_STATE.toString());
            guiButtons.get(homeScreen).setEnabled(true);
        }

        //Disable Home Button
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setEnabled(false);

        // DEACTIVATE ALL DIALOGS
        //   guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        //   guiDialogs.get(STATS_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
        // HIDE THE TILES
        ((PathXDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = HOME_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        // PLAY THE WELCOME SCREEN SONG
        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
    }

    public void switchToHelpScreen() {

        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);

        //ENABLE HOME BUTTONS
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setEnabled(true);

        //ENABLE Home DESCRIPTION
        guiButtons.get(HELP_GAME_DESCRIPTION_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(HELP_GAME_DESCRIPTION_TYPE).setEnabled(true);

        //DISABLE HOME SCREEN OPTIONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> homeScreens = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        for (String homeScreen : homeScreens) {
            guiButtons.get(homeScreen).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(homeScreen).setEnabled(false);
        }

        currentScreenState = HELP_SCREEN_STATE;

        // PLAY THE GAMEPLAY SCREEN SONG
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString(), true);

    }

    public void switchToSettingsScreen() {

        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);

        //ENABLE HOME BUTTON
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setEnabled(true);

        //ENABLE SOUND CHECKBOX
        if (checkedSound) {
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.SELECTED_STATE.toString());
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
        } else {
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
        }

        //ENABLE MUSIC CHECKBOX
        if (checkedMusic) {
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.SELECTED_STATE.toString());
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
        } else {
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
        }

        //DISABLE HOME SCREEN BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> homeScreens = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        for (String homeScreen : homeScreens) {
            guiButtons.get(homeScreen).setState(PathXCarState.INVISIBLE_STATE.toString());
            guiButtons.get(homeScreen).setEnabled(false);
        }

        currentScreenState = SETTINGS_SCREEN_STATE;

        // PLAY THE GAMEPLAY SCREEN SONG
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString(), true);
    }

    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(PathXPropertyType.AUDIO_CUE_SELECT_TILE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_DESELECT_TILE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_GOOD_MOVE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_BAD_MOVE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_CHEAT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_UNDO);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(PathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_LEVEL_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_GAME_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(PathXPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new PathXErrorHandler(window);

        // INIT OUR FILE MANAGER
        //fileManager = new PathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        //record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);

        //INIT LEVEL LIST ARRAYLIST
        levelList = new ArrayList<>(20);

    }
    
    @Override
    public PathXDataModel getDataModel() {
        return (PathXDataModel) data;
    }

    /**
     * Initializes the game controls, like the home screen buttons, used by the
     * game application. Note that this includes the game backgrounds as well
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        /*currentScreenState = HOME_SCREEN_STATE;
         img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HOME));
         sT = new SpriteType(BACKGROUND_TYPE);
         sT.addState(HOME_SCREEN_STATE, img);
        
         img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
         sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
         s = new Sprite(sT, 0, 0, 0, 0, LEVEL_SELECT_SCREEN_STATE);
         guiDecor.put(BACKGROUND_TYPE, s);*/
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = HOME_SCREEN_STATE;

        sT = new SpriteType(BACKGROUND_TYPE);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HOME));
        sT.addState(HOME_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);

        s = new Sprite(sT, 0, 0, 0, 0, HOME_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        /*img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
         sT.addState(GAME_SCREEN_STATE, img);
         s = new Sprite(sT, 0, 0, 0, 0, HOME_SCREEN_STATE);
         guiDecor.put(BACKGROUND_TYPE, s);*/
        // LOAD THE WAND CURSOR
        // String cursorName = props.getProperty(PathXPropertyType.IMAGE_CURSOR_WAND);
        //  img = loadImageWithColorKey(imgPath + cursorName, COLOR_KEY);
        //  Point cursorHotSpot = new Point(0,0);
        //   Cursor wandCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
        //   window.setCursor(wandCursor);
        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
        ArrayList<String> homeMenuButton = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        // ArrayList<String> menuImageNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> homeMenuMouseOverImageNames = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_MOUSE_OVER_IMAGE_OPTIONS);
        float totalWidth = homeMenuButton.size() * (GAME_BUTTON_WIDTH + GAME_BUTTON_MARGIN) - GAME_BUTTON_MARGIN; //shifting buttons in the x-direction
        Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth) / 2.0f;
        for (int i = 0; i < homeMenuButton.size(); i++) {
            sT = new SpriteType(HOME_MENU_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + homeMenuButton.get(i), COLOR_KEY);
            sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + homeMenuMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
            s = new Sprite(sT, x, GAME_BUTTON_Y, 0, 0, PathXCarState.VISIBLE_STATE.toString());
            guiButtons.put(homeMenuButton.get(i), s);
            guiButtons.put(homeMenuMouseOverImageNames.get(i), s);
            x += GAME_BUTTON_WIDTH + GAME_BUTTON_MARGIN; // edits space between buttons
        }

        //VIEWPORT MAP INTIT
//        String mapImage = props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MAP);
//        sT = new SpriteType(MAP_GAME_IMAGE_TYPE);
//        img = loadImage(imgPath + mapImage);
//        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, x, 0, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
//        guiDecor.put(MAP_GAME_IMAGE_TYPE, s);
//        viewport.setGameWorldSize(50, 50);
//         viewport.setScreenSize(100, 100);
//         //viewport.setViewportSize(50, 50);
//        viewport.setViewportSize(100, 100);
//        
//         System.out.println("VWPRT MAX X: " + viewport.getScreenWidth());
//         System.out.println("VWPRT MAX Y: " + viewport.getMaxViewportY());
//         System.out.println("VWPRT MIN X: " + viewport.getMinViewportX());
//         System.out.println("VWPRT MIN Y: " + viewport.getMinViewportY());
        //Specials Menu Row 1
//        ArrayList<String> specialsRow1 = props.getPropertyOptionsList(PathXPropertyType.SPECIALS_IMAGE_OPTIONS_R1);
//        
//        //ArrayList<String> specialsRow1MouseOverImage = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_MOUSE_OVER_IMAGE_OPTIONS);
//        float totalWidth2 = specialsRow1.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN + 30) - LEVEL_BUTTON_MARGIN - 35; //edit constants
//        //Viewport viewport = data.getViewport();
//        x = (viewport.getScreenWidth() - totalWidth2) / 2.0f;
//        for (int i = 0; i < specialsRow1.size(); i++) {
//            sT = new SpriteType(SPECIAL_SELECT_BUTTON_TYPE);
//            img = loadImageWithColorKey(imgPath + specialsRow1.get(i), COLOR_KEY);
//            sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
//            //img = loadImageWithColorKey(imgPath + menuMouseOverImageNames.get(i), COLOR_KEY);
//            //sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
//            s = new Sprite(sT, x, LEVEL_BUTTON_Y + 10, 0, 0, PathXCarState.VISIBLE_STATE.toString());
//            guiButtons.put(specialsRow1.get(i), s);
//           // guiButtons.put(menuMouseOverImageNames.get(i), s);
//            x += LEVEL_BUTTON_WIDTH + 30 + LEVEL_BUTTON_MARGIN - 5; // edit dimensions
//        }
        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
        //PUT THE X BUTTON
        String exitButton = props.getProperty(PathXPropertyType.IMAGE_EXIT_HOME_SCREEN);
        sT = new SpriteType(EXIT_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + exitButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        //X BUTTON MOUSE OVER
        String exitMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_EXIT_HOME_SCREEN_MOUSE_OVER);
        img = loadImage(imgPath + exitMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EXIT_BUTTON_X, EXIT_BUTTON_Y, 0, 0, PathXCarState.VISIBLE_STATE.toString());
        guiButtons.put(EXIT_GAME_BUTTON_TYPE, s);

        //PUT IN THE HOME BUTTON
        String homeButton = props.getProperty(PathXPropertyType.IMAGE_GO_TO_HOME_SCREEN);
        sT = new SpriteType(HOME_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + homeButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        //HOME BUTTON MOUSE OVER
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_GO_TO_HOME_SCREEN_MOUSE_OVER);
        img = loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(HOME_GAME_BUTTON_TYPE, s);

        //PUT IN THE UP ARROW BUTTON
        String upArrowButton = props.getProperty(PathXPropertyType.IMAGE_UP_ARROW);
        sT = new SpriteType(UP_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + upArrowButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_X/*x coordinates*/, UP_BUTTON_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(UP_ARROW_BUTTON_TYPE, s);

        //PUT IN THE DOWN ARROW BUTTON
        String downArrowButton = props.getProperty(PathXPropertyType.IMAGE_DOWN_ARROW);
        sT = new SpriteType(DOWN_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + downArrowButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_BUTTON_X/*x coordinates*/, DOWN_BUTTON_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(DOWN_ARROW_BUTTON_TYPE, s);

        //PUT IN THE LEFT ARROW BUTTON
        String leftArrowButton = props.getProperty(PathXPropertyType.IMAGE_LEFT_ARROW);
        sT = new SpriteType(LEFT_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + leftArrowButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_BUTTON_X/*x coordinates*/, LEFT_BUTTON_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEFT_ARROW_BUTTON_TYPE, s);

        //PUT IN THE RIGHT ARROW BUTTON
        String rightArrowButton = props.getProperty(PathXPropertyType.IMAGE_RIGHT_ARROW);
        sT = new SpriteType(RIGHT_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + rightArrowButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_BUTTON_X/*x coordinates*/, RIGHT_BUTTON_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(RIGHT_ARROW_BUTTON_TYPE, s);

        //PUT IN THE HELP DESCRIPTION
        String helpGameDescription = props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP_DESCRIPTION);
        sT = new SpriteType(HELP_GAME_DESCRIPTION_TYPE);
        img = loadImage(imgPath + helpGameDescription);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HELP_SCREEN_DESCRIPTION_X/*x coordinates*/, HELP_SCREEN_DESCRIPTION_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(HELP_GAME_DESCRIPTION_TYPE, s);

        //PUT IN THE SOUND CHECK BOXES
        String soundBoxUnchecked = props.getProperty(PathXPropertyType.IMAGE_UNCHECKED_BOX);
        sT = new SpriteType(SOUND_CHECK_BOX_TYPE);
        img = loadImage(imgPath + soundBoxUnchecked);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        String soundBoxChecked = props.getProperty(PathXPropertyType.IMAGE_CHECKED_BOX);
        img = loadImage(imgPath + soundBoxChecked);
        sT.addState(PathXCarState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, SOUND_CHECK_BOX_X/*x coordinates*/, SOUND_CHECK_BOX_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_CHECK_BOX_TYPE, s);

        //PUT IN THE MUSIC CHECK BOXES
        String musicBoxUnchecked = props.getProperty(PathXPropertyType.IMAGE_UNCHECKED_BOX);
        sT = new SpriteType(MUSIC_CHECK_BOX_TYPE);
        img = loadImage(imgPath + musicBoxUnchecked);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        String musicBoxChecked = props.getProperty(PathXPropertyType.IMAGE_CHECKED_BOX);
        img = loadImage(imgPath + musicBoxChecked);
        sT.addState(PathXCarState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_CHECK_BOX_X/*x coordinates*/, MUSIC_CHECK_BOX_Y/*y coordinates*/, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_CHECK_BOX_TYPE, s);

    }

    public void updateLevelSelectButtons() {

        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);

          //System.out.println("Punk Bitch");
        /*<------LEVEL 1------>*/
        //RED STATE
        String level1SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_1_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level1SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level1SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level1SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level1SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level1SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel1, yButtonLevel1, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_1_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(1, 0, 0, xButtonLevel1, yButtonLevel1, LEVEL_1_SELECT_BUTTON_TYPE, PathXCarState.RED_STATE.toString()));
        /*<------LEVEL 2------>*/
        //RED STATE
        String level2SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_2_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level2SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level2SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level2SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level2SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level2SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel2, yButtonLevel2, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_2_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel2, yButtonLevel2, LEVEL_2_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 3------>*/
        //RED STATE
        String level3SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_3_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level3SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level3SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level3SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level3SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level3SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel3, yButtonLevel3, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_3_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel3, yButtonLevel3, LEVEL_3_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 4------>*/
        //RED STATE
        String level4SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_4_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level4SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level4SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level4SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level4SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level4SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel4, yButtonLevel4, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_4_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel4, yButtonLevel4, LEVEL_4_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 5------>*/
        //RED STATE
        String level5SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_5_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level5SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level5SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level5SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level5SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level5SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel5, yButtonLevel5, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_5_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel5, yButtonLevel5, LEVEL_5_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 6------>*/
        //RED STATE
        String level6SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_6_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level6SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level6SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level6SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level6SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level6SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel6, yButtonLevel6, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_6_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel6, yButtonLevel6, LEVEL_6_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 7------>*/
        //RED STATE
        String level7SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_7_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level7SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level7SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level7SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level7SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level7SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel7, yButtonLevel7, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_7_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel7, yButtonLevel7, LEVEL_7_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 8------>*/
        //RED STATE
        String level8SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_8_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level8SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level8SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level8SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level8SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level1SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel8, yButtonLevel8, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_8_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel8, yButtonLevel8, LEVEL_8_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 9------>*/
        //RED STATE
        String level9SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_9_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level9SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level9SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level9SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level9SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level9SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel9, yButtonLevel9, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_9_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel9, yButtonLevel9, LEVEL_9_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 10------>*/
        //RED STATE
        String level10SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_10_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level10SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level10SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level10SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level10SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level10SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel10, yButtonLevel10, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_10_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel10, yButtonLevel10, LEVEL_10_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 11------>*/
        //RED STATE
        String level11SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_11_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level11SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level11SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level11SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level11SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level11SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel11, yButtonLevel11, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_11_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel11, yButtonLevel11, LEVEL_11_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 12------>*/
        //RED STATE
        String level12SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_12_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level12SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level12SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level12SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level12SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level12SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel12, yButtonLevel12, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_12_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel12, yButtonLevel12, LEVEL_12_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 13------>*/
        //RED STATE
        String level13SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_13_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level13SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level13SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level13SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level13SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level13SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel13, yButtonLevel13, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_13_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel13, yButtonLevel13, LEVEL_13_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 14------>*/
        //RED STATE
        String level14SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_14_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level14SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level14SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level14SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level14SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level14SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel14, yButtonLevel14, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_14_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel14, yButtonLevel14, LEVEL_14_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 15------>*/
        //RED STATE
        String level15SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_15_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level15SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level15SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level15SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level15SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level15SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel15, yButtonLevel15, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        getGUIButtons().put(LEVEL_15_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel15, yButtonLevel15, LEVEL_15_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 16------>*/
        //RED STATE
        String level16SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_16_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level16SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level16SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level16SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level16SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level16SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel16, yButtonLevel16, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_16_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel16, yButtonLevel16, LEVEL_16_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 17------>*/
        //RED STATE
        String level17SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_17_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level17SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level17SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level17SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level17SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level17SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel17, yButtonLevel17, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_17_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel17, yButtonLevel17, LEVEL_17_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 18------>*/
        //RED STATE
        String level18SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_18_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level18SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level18SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level18SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level18SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level18SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel18, yButtonLevel18, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_18_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel18, yButtonLevel18, LEVEL_18_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 19------>*/
        //RED STATE
        String level19SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_19_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level19SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level19SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level19SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level19SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level19SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel19, yButtonLevel19, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_19_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel19, yButtonLevel19, LEVEL_19_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<------LEVEL 20------>*/
        //RED STATE
        String level20SelectButtonRed = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_RED);
        sT = new SpriteType(LEVEL_20_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + level20SelectButtonRed);
        sT.addState(PathXCarState.RED_STATE.toString(), img);
        //WHITE STATE
        String level20SelectButtonWhite = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_WHITE);
        img = loadImage(imgPath + level20SelectButtonWhite);
        sT.addState(PathXCarState.WHITE_STATE.toString(), img);
        //GREEN STATE
        String level20SelectButtonGreen = props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_GREEN);
        img = loadImage(imgPath + level20SelectButtonGreen);
        sT.addState(PathXCarState.GREEN_STATE.toString(), img);
        s = new Sprite(sT, xButtonLevel20, yButtonLevel20, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_20_SELECT_BUTTON_TYPE, s);

        levelList.add(new PathXLevelNode(0, 0, 0, xButtonLevel20, yButtonLevel20, LEVEL_20_SELECT_BUTTON_TYPE, PathXCarState.WHITE_STATE.toString()));
        /*<-------------------------------Level Select Buttons: END-------------------------------------->*/

//        if (isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
//            for (int i = 0; i < levelList.size(); i++) {
//
//                guiButtons.get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                if ((levelList.get(i).getXCoordinate() > 10) && (levelList.get(i).getXCoordinate() < 630)
//                        && (levelList.get(i).getYCoordinate() > 45) && (levelList.get(i).getYCoordinate() < 435)) {
//                    guiButtons.get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                    guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(true);
//
//                    if (levelList.get(i).getLevelState().equals(PathXCarState.RED_STATE.toString())) {
//
//                        guiButtons.get(levelList.get(i).getLeveliD()).setActionListener(new ActionListener() {
//                            public void actionPerformed(ActionEvent ae) {
//                                System.out.println("YOU SELECTED THE RED BUTTON!");
//                                eventHandler.respondToGameplayScreen();
//                            }
//                        });
//                    }
//                // s = new Sprite(sT, xButtonLevel1, yButtonLevel1, 0, 0, PathXCarState.VISIBLE_STATE.toString());
//                    //game.getGUIButtons().put(LEVEL_SELECT_BUTTON_TYPE, s);
//                } else {
//                    guiButtons.get(levelList.get(i).getLeveliD()).setState(PathXCarState.INVISIBLE_STATE.toString());
//                    guiButtons.get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                }
//
//            }
//        }
    }

//    public void updateButtons() {
//       
//            levelList.get(0).setXCoordinate(xButtonLevel1);
//            levelList.get(0).setXCoordinate(yButtonLevel1);
//            
//            levelList.get(1).setXCoordinate(xButtonLevel2);
//            levelList.get(1).setXCoordinate(yButtonLevel2);
//            
//            levelList.get(2).setXCoordinate(xButtonLevel3);
//            levelList.get(2).setXCoordinate(yButtonLevel3);
//            
//            levelList.get(3).setXCoordinate(xButtonLevel4);
//            levelList.get(3).setXCoordinate(yButtonLevel4);
//            
//            levelList.get(4).setXCoordinate(xButtonLevel5);
//            levelList.get(4).setXCoordinate(yButtonLevel5);
//            
//            levelList.get(5).setXCoordinate(xButtonLevel6);
//            levelList.get(5).setXCoordinate(yButtonLevel6);
//            
//            levelList.get(6).setXCoordinate(xButtonLevel7);
//            levelList.get(6).setXCoordinate(yButtonLevel7);
//            
//            levelList.get(7).setXCoordinate(xButtonLevel8);
//            levelList.get(7).setXCoordinate(yButtonLevel8);
//            
//            levelList.get(8).setXCoordinate(xButtonLevel9);
//            levelList.get(8).setXCoordinate(yButtonLevel9);
//            
//            levelList.get(9).setXCoordinate(xButtonLevel10);
//            levelList.get(9).setXCoordinate(yButtonLevel10);
//            
//            levelList.get(10).setXCoordinate(xButtonLevel11);
//            levelList.get(10).setXCoordinate(yButtonLevel11);
//            
//            levelList.get(11).setXCoordinate(xButtonLevel12);
//            levelList.get(11).setXCoordinate(yButtonLevel12);
//            
//            levelList.get(12).setXCoordinate(xButtonLevel13);
//            levelList.get(12).setXCoordinate(yButtonLevel13);
//            
//            levelList.get(13).setXCoordinate(xButtonLevel14);
//            levelList.get(13).setXCoordinate(yButtonLevel14);
//            
//            levelList.get(14).setXCoordinate(xButtonLevel15);
//            levelList.get(14).setXCoordinate(yButtonLevel15);
//            
//            levelList.get(15).setXCoordinate(xButtonLevel16);
//            levelList.get(15).setXCoordinate(yButtonLevel16);
//            
//            levelList.get(16).setXCoordinate(xButtonLevel17);
//            levelList.get(16).setXCoordinate(yButtonLevel17);
//            
//            levelList.get(17).setXCoordinate(xButtonLevel18);
//            levelList.get(17).setXCoordinate(yButtonLevel18);
//            
//            levelList.get(18).setXCoordinate(xButtonLevel19);
//            levelList.get(18).setXCoordinate(yButtonLevel19);
//            
//            levelList.get(19).setXCoordinate(xButtonLevel20);
//            levelList.get(19).setXCoordinate(yButtonLevel20);
//            
//        
//    }
    public void moveLevelSelectButtons() {

    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);

        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                eventHandler.respondToExitRequest();
            }
        });

        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.HOME_SCREEN_IMAGE_OPTIONS);
        for (String levelFile : levels) {
            Sprite levelButton = guiButtons.get(levelFile);
            levelButton.setActionCommand(PATH_DATA + levelFile);
            levelButton.setActionListener(new ActionListener() {
                Sprite s;

                public ActionListener init(Sprite initS) {
                    s = initS;
                    return this;
                }

                public void actionPerformed(ActionEvent ae) {
                    eventHandler.respondToSelectGameMenuRequest(s.getActionCommand());
                    //System.out.println("SHIT: " + s.getActionCommand());
                }
            }.init(levelButton));
        }
        System.out.println("Current Screen: " + currentScreenState);
        
         //LEVEL BUTTON SELECTION HANDLER
        //!!!!!NOT USED!!!!???
        for (int i = 0; i < levelList.size(); i++) {
            if (levelList.get(i).getLevelState().equals(PathXCarState.RED_STATE.toString())) {
            
                Sprite levelButton = guiButtons.get(levelList.get(i).getLeveliD());
                levelButton.setActionCommand(PATH_DATA + levelList.get(i).getLeveliD());
                System.out.println("MINIGAME");
                guiButtons.get(levelList.get(i).getLeveliD()).setActionListener(new ActionListener() {
                    
                    Sprite s;

                    public ActionListener init(Sprite initS) {
                        s = initS;
                        return this;
                    }

                    public void actionPerformed(ActionEvent ae) {
                        //System.out.println("OTHER STUFF: " + levelButton);
                        System.out.println("SAND");
                        System.out.println("OTHER STUFF: " + levelButton);
                        System.out.println("WITCH");
                        System.out.println("YOU SELECTED THE RED BUTTON!");
                        eventHandler.respondToGameplayScreen("Hello");
                        //System.out.println("SHIT: " + s.getActionCommand());
                    }
                }.init(levelButton));
               
            }
        }

//        //X BUTTON EVENT HANDLER
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToExitRequest();
            }
        });

        //HOME BUTTON EVENT HANDLER
        guiButtons.get(HOME_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHomeRequest();
            }
        });

        //SOUND CHECK BOX EVENT HANDLER
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCheckRequest(SOUND_CHECK_BOX_TYPE);
            }
        });

        //MUSIC CHECK EVENT HANDLER
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCheckRequest(MUSIC_CHECK_BOX_TYPE);
            }
        });

        //<--SCROLLING EVENT HANDLERS-->
        //UP SCROLL BUTTON EVENT HANDLER
        guiButtons.get(UP_ARROW_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToScrollRequest(UP_ARROW_BUTTON_TYPE);
            }
        });
        //DOWN SCROLL BUTTON EVENT HANDLER
        guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToScrollRequest(DOWN_ARROW_BUTTON_TYPE);
            }
        });
        //RIGHT SCROLL BUTTON EVENT HANDLER
        guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToScrollRequest(RIGHT_ARROW_BUTTON_TYPE);
            }
        });
        //LEFT SCROLL BUTTON EVENT HANDLER
        guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToScrollRequest(LEFT_ARROW_BUTTON_TYPE);
            }
        });

////     //   
        // BACK EVENT HANDLER
        //   guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener(){
        //       public void actionPerformed(ActionEvent ae)
        //       {   eventHandler.respondToBackRequest();     }
        //   });
        // UNDO BUTTON EVENT HANDLER
        //   guiButtons.get(UNDO_BUTTON_TYPE).setActionListener(new ActionListener(){
        //       public void actionPerformed(ActionEvent ae)
        //       {   eventHandler.respondToUndoRequest();     }
        //   });
        // STATS BUTTON EVENT HANDLER
        //    guiButtons.get(STATS_BUTTON_TYPE).setActionListener(new ActionListener(){
        //        public void actionPerformed(ActionEvent ae)
        //        {   eventHandler.respondToDisplayStatsRequest();    }
        //    });
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }

    /**
     * This method helps to initialize buttons for a simple toolbar.
     *
     * @param toolbar The toolbar for which to add the button.
     *
     * @param prop The property for the button we are building. This will
     * dictate which image to use for the button.
     *
     * @return A constructed button initialized and added to the toolbar.
     */
//    private JButton initToolbarButton(JPanel toolbar, PathXPropertyType prop) {
//        // GET THE NAME OF THE IMAGE, WE DO THIS BECAUSE THE
//        // IMAGES WILL BE NAMED DIFFERENT THINGS FOR DIFFERENT LANGUAGES
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        String imageName = props.getProperty(prop);
//
//        // LOAD THE IMAGE
//        Image image = loadImage(imageName);
//        ImageIcon imageIcon = new ImageIcon(image);
//
//        // MAKE THE BUTTON
//        JButton button = new JButton(imageIcon);
//        button.setMargin(marginlessInsets);
//
//        // PUT IT IN THE TOOLBAR
//        toolbar.add(button);
//
//        // AND SEND BACK THE BUTTON
//        return button;
//    }
    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset() {
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXCarState.VISIBLE_STATE.toString())) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXCarState.MOUSE_OVER_STATE.toString());
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXCarState.VISIBLE_STATE.toString());
                }
            } else if (button.getState().equals(PathXCarState.SELECTED_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXCarState.SELECTED_STATE.toString());
                }
            }

            /*  
             //     props.getPropertyOptionsList(PathXPropertyType.values()
             ArrayList<SortingHatTile> tilesToSort;
                    
             tilesToSort =  ((PathXDataModel)data).getTilesToSort();
            
            
                    
             //  Iterator<Sprite> titlesIt = (SortingHatDateModel)data.ge
             Iterator<SortingHatTile>  titlesIt = tilesToSort.iterator();
                    
             while (titlesIt.hasNext())
             {
             Sprite titles = titlesIt.next();
          
             // ARE WE ENTERING A BUTTON?
             if (titles.getState().equals(PathXCarState.VISIBLE_STATE.toString()))
             {
             if (titles.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
             {
             titles.setState(PathXCarState.MOUSE_OVER_STATE.toString());
             // System.out.println(titles.getState()+" section 1");
             }
             }
             // ARE WE EXITING A BUTTON?
             else if (titles.getState().equals(PathXCarState.MOUSE_OVER_STATE.toString()))
             {
             if (!titles.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
             {
             titles.setState(PathXCarState.VISIBLE_STATE.toString());
             //   System.out.println(titles.getState()+" section ");
             }
             }
             }*/
        }
    }

    public PathXEventHandler getEventHandler() {
        return eventHandler;
    }

    public PathXPanel getPanel() {
        return gamePanel;
    }

    public void scrollToTheNorth() {
       
        if(currentScreenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
        System.out.println("Scrolling Up");
        eventHandler.adjustScrollScreen(UP_BUTTON_DIRECTION);
        }
        else if(currentScreenState.equals(GAME_SCREEN_STATE)) {
             System.out.println("MADE IT");
            eventHandler.adjustGameScrollScreen(UP_BUTTON_DIRECTION);
        }
    }

    public void scrollToTheSouth() {
        
        if(currentScreenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
        System.out.println("Scrolling Down");
        eventHandler.adjustScrollScreen(DOWN_BUTTON_DIRECTION);
        }
        else  if(currentScreenState.equals(GAME_SCREEN_STATE)) {
             System.out.println("MADE IT");
             eventHandler.adjustGameScrollScreen(DOWN_BUTTON_DIRECTION);
        }
    }

    public void scrollToTheEast() {
          if(currentScreenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
        System.out.println("Scrolling Right");
        eventHandler.adjustScrollScreen(RIGHT_BUTTON_DIRECTION);
          }
          else if(currentScreenState.equals(GAME_SCREEN_STATE)) {
               System.out.println("MADE IT");
              eventHandler.adjustGameScrollScreen(RIGHT_BUTTON_DIRECTION);
          }
    }

    public void scrollToTheWest() {
        if(currentScreenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
        System.out.println("Scrolling Left");
        eventHandler.adjustScrollScreen(LEFT_BUTTON_DIRECTION);
        }
        else if(currentScreenState.equals(GAME_SCREEN_STATE)) {
            System.out.println("MADE IT");
            eventHandler.adjustGameScrollScreen(LEFT_BUTTON_DIRECTION);
        }
    }

//    public static boolean checkedMusic = false;
//    public static boolean checkedSound = false;
    public void toggleCheckBox(String boxType) {
        //IF THE BOX HAS NOT BEEN CHECK
        System.out.println("MADE IT TO CHECK BOX");
        if (boxType.equals(SOUND_CHECK_BOX_TYPE)) {
            if (checkedSound) {
                System.out.println("PERFORMED UNCHECK");
                guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
                guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
                checkedSound = false;
            } else {
                System.out.println("PERFORMED CHECK");
                guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXCarState.SELECTED_STATE.toString());
                guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
                checkedSound = true;
            }
        } else {
            if (checkedMusic) {
                guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
                guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
                checkedMusic = false;
            } else {
                guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXCarState.SELECTED_STATE.toString());
                guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
                checkedMusic = true;
            }
        }
    }

}
