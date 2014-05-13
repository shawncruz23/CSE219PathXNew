/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx;

import pathx.ui.PathXMiniGame;
import pathx.ui.PathXErrorHandler;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;

/**
 * PathX is a game application that's ready to be customized
 * to play different flavors of the game. It has been setup using art
 * from Game of Thrones, but it could easily be swapped out just by changing
 * the artwork and audio files.
 * 
 * @author Richard McKenna & Shawn Cruz
 */
public class PathX {
    
    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    static PathXMiniGame miniGame = new PathXMiniGame();

    /**
     * This is where the PathX game application starts execution. We'll
     * load the application properties and then use them to build our
     * user interface and start the window in real-time mode.
     */
    public static void main(String[] args) {
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            
            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_GAME);
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // GET THE PROPER WINDOW DIMENSIONS
            miniGame.startGame();
        }
        // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
        catch(InvalidXMLFileFormatException ixmlffe) {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
            PathXErrorHandler errorHandler = miniGame.getErrorHandler();
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_XML_FILE);
        }
    }

   
    
    /**
     * PathXPropertyType represents the types of data that will need
     * to be extracted from XML files.
     */
    public enum PathXPropertyType {
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // pathx_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_BACKGROUND_GAME,
        IMAGE_BACKGROUND_LEVEL_SELECT,
        IMAGE_BACKGROUND_HOME,
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BACKGROUND_HELP,
        IMAGE_BACKGROUND_MAP,
        
        IMAGE_INFO_DIALOG,
        
        IMAGE_BACKGROUND_HELP_DESCRIPTION,
       
        IMAGE_UNCHECKED_BOX,
        IMAGE_CHECKED_BOX,
      
         IMAGE_WINDOW_ICON,
         IMAGE_ICON_GAME,
        //IMAGE_BUTTON_NEW,
       // IMAGE_BUTTON_NEW_MOUSE_OVER,
        /*IMAGE_BUTTON_BACK,
        IMAGE_BUTTON_BACK_MOUSE_OVER,
        IMAGE_BUTTON_STATS,
        
        IMAGE_BUTTON_UNDO_MOUSE_OVER,
        IMAGE_BUTTON_UNDO,
        
        
        IMAGE_BUTTON_STATS_MOUSE_OVER,
        IMAGE_BUTTON_TEMP_TILE,
        IMAGE_BUTTON_TEMP_TILE_MOUSE_OVER,
        IMAGE_CURSOR_WAND,
        IMAGE_DECOR_TIME,           
        IMAGE_TILE_BACKGROUND,
        IMAGE_TILE_BACKGROUND_SELECTED,
        IMAGE_TILE_BACKGROUND_MOUSE_OVER,
        
        */
       
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        
        /* AUDIO CUES */
        AUDIO_CUE_BAD_MOVE,
        AUDIO_CUE_CHEAT,
        AUDIO_CUE_DESELECT_TILE,
        AUDIO_CUE_GOOD_MOVE,
        AUDIO_CUE_SELECT_TILE,
        AUDIO_CUE_UNDO,
        AUDIO_CUE_WIN,
        
        AUDIO_CUE_COLLISION,
        
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        SONG_CUE_LEVEL_SCREEN,
        SONG_CUE_SETTINGS_SCREEN,
        SONG_CUE_HELP_SCREEN,
        
        
      // LEVEL_IMAGE_OPTIONS, 
       HOME_SCREEN_IMAGE_OPTIONS,
       HOME_SCREEN_MOUSE_OVER_IMAGE_OPTIONS,
       
       SPECIALS_IMAGE_OPTIONS_R1,
       SPECIALS_IMAGE_OPTIONS_R2,
       SPECIALS_IMAGE_OPTIONS_R3,
       SPECIALS_IMAGE_OPTIONS_R4,
       
       IMAGE_EXIT_HOME_SCREEN,
       IMAGE_EXIT_HOME_SCREEN_MOUSE_OVER,
       IMAGE_GO_TO_HOME_SCREEN,
       IMAGE_GO_TO_HOME_SCREEN_MOUSE_OVER,
    
       
       //ARROWS
       IMAGE_UP_ARROW,
       IMAGE_DOWN_ARROW,
       IMAGE_LEFT_ARROW,
       IMAGE_RIGHT_ARROW,
       
       //LEVEL SELECT BUTTONS: red, green, white
       IMAGE_LEVEL_SELECT_GREEN,
       IMAGE_LEVEL_SELECT_RED,
       IMAGE_LEVEL_SELECT_WHITE,
  
       //LEVEL BACKGROUND IMAGES
       IMAGE_BACKGROUND_LEVEL_1,
       
       //PLAYER CAR IMAGE
       IMAGE_PLAYER_CAR,
       IMAGE_POLICE_CAR,
       IMAGE_ZOMBIE_CAR,
       IMAGE_BANDIT_CAR,
       
       BUTTON_TRY_AGAIN,
       BUTTON_LEAVE_TOWN,
       BUTTON_START,
       BUTTON_CLOSE,
       
       BUTTON_PAUSE,
       BUTTON_RESUME
       
    }
}