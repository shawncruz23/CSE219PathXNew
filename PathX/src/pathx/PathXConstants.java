/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx;

import java.awt.Color;
import java.awt.Font;

/**
 * This class stores all the constants used by the PathX application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 * 
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXConstants {
    
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";

    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    public static final String HOME_MENU_BUTTON_TYPE = "HOME_MENU_BUTTON_TYPE";
    
    //BUTTONS FOR EACH LEVEL NODE IN THE LEVEL SELECT SCREEN STATE
    public static final String LEVEL_1_SELECT_BUTTON_TYPE = "LEVEL_1_SELECT_BUTTON_TYPE";
    public static final String LEVEL_2_SELECT_BUTTON_TYPE = "LEVEL_2_SELECT_BUTTON_TYPE";
    public static final String LEVEL_3_SELECT_BUTTON_TYPE = "LEVEL_3_SELECT_BUTTON_TYPE";
    public static final String LEVEL_4_SELECT_BUTTON_TYPE = "LEVEL_4_SELECT_BUTTON_TYPE";
    public static final String LEVEL_5_SELECT_BUTTON_TYPE = "LEVEL_5_SELECT_BUTTON_TYPE";
    public static final String LEVEL_6_SELECT_BUTTON_TYPE = "LEVEL_6_SELECT_BUTTON_TYPE";
    public static final String LEVEL_7_SELECT_BUTTON_TYPE = "LEVEL_7_SELECT_BUTTON_TYPE";
    public static final String LEVEL_8_SELECT_BUTTON_TYPE = "LEVEL_8_SELECT_BUTTON_TYPE";
    public static final String LEVEL_9_SELECT_BUTTON_TYPE = "LEVEL_9_SELECT_BUTTON_TYPE";
    public static final String LEVEL_10_SELECT_BUTTON_TYPE = "LEVEL_10_SELECT_BUTTON_TYPE";
    public static final String LEVEL_11_SELECT_BUTTON_TYPE = "LEVEL_11_SELECT_BUTTON_TYPE";
    public static final String LEVEL_12_SELECT_BUTTON_TYPE = "LEVEL_12_SELECT_BUTTON_TYPE";
    public static final String LEVEL_13_SELECT_BUTTON_TYPE = "LEVEL_13_SELECT_BUTTON_TYPE";
    public static final String LEVEL_14_SELECT_BUTTON_TYPE = "LEVEL_14_SELECT_BUTTON_TYPE";
    public static final String LEVEL_15_SELECT_BUTTON_TYPE = "LEVEL_15_SELECT_BUTTON_TYPE";
    public static final String LEVEL_16_SELECT_BUTTON_TYPE = "LEVEL_16_SELECT_BUTTON_TYPE";
    public static final String LEVEL_17_SELECT_BUTTON_TYPE = "LEVEL_17_SELECT_BUTTON_TYPE";
    public static final String LEVEL_18_SELECT_BUTTON_TYPE = "LEVEL_18_SELECT_BUTTON_TYPE";
    public static final String LEVEL_19_SELECT_BUTTON_TYPE = "LEVEL_19_SELECT_BUTTON_TYPE";
    public static final String LEVEL_20_SELECT_BUTTON_TYPE = "LEVEL_20_SELECT_BUTTON_TYPE";
    
    
     // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR SPECIAL SELECTION
    public static final String SPECIAL_SELECT_BUTTON_TYPE = "SPECIAL_SELECT_BUTTON_TYPE";
    

    // IN-GAME UI CONTROL TYPES
    
    //STANDARD UI CONTROLS
    public static final String EXIT_GAME_BUTTON_TYPE = "EXIT_GAME_BUTTON_TYPE";
    public static final String HOME_GAME_BUTTON_TYPE = "HOME_GAME_BUTTON_TYPE";
    
    
    public static final String HELP_GAME_DESCRIPTION_TYPE = "HELP_GAME_DESCRIPTION_TYPE";
    public static final String SOUND_CHECK_BOX_TYPE = "SOUND_CHECK_BOX_TYPE";
    public static final String MUSIC_CHECK_BOX_TYPE = "MUSIC_CHECK_BOX_TYPE";
    public static final String SCROLL_CHECK_BOX_TYPE = "MUSIC_CHECK_BOX_TYPE";
    //public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
    
    //ARROW TYPES
    public static final String UP_ARROW_BUTTON_TYPE = "UP_ARROW_BUTTON_TYPE";
    public static final String DOWN_ARROW_BUTTON_TYPE = "DOWN_ARROW_BUTTON_TYPE";
    public static final String RIGHT_ARROW_BUTTON_TYPE = "RIGHT_ARROW_BUTTON_TYPE";
    public static final String LEFT_ARROW_BUTTON_TYPE = "LEFT_ARROW_BUTTON_TYPE";
    
    public static final String MAP_GAME_IMAGE_TYPE = "MAP_GAME_IMAGE_TYPE";
    
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE FOUR
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String HOME_SCREEN_STATE = "HOME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";

    // ANIMATION SPEED. !!!UPDATE FOR DIFFERENT SETTINGS!!!
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640;//450;
    public static final int WINDOW_HEIGHT = 480;//360;
    public static final int VIEWPORT_MARGIN_LEFT = 20;//20
    public static final int VIEWPORT_MARGIN_RIGHT = 20;//20
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    
    
    public static final int GAME_BUTTON_WIDTH = 100; //150
    public static final int GAME_BUTTON_MARGIN = 5;
    public static final int GAME_BUTTON_Y = 390;
    //public static final int VIEWPORT_INC = 5;
    
    public static final int LEVEL_BUTTON_WIDTH = 100; //150
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 390;
   // public static final int VIEWPORT_INC = 5;
    
    /*<-----DESTINATION AND SOURCE COORDINATES FOR MAP SEGMENT----->*/
    //(dx1,dy1): pair of coord. for destination box
    public static final int DESTINATION_X1 = 10;
    public static final int DESTINATION_Y1 = 45;
    //(dx1,dy1): pair of coord. for destination box
    public static final int DESTINATION_X2 = 630;
    public static final int DESTINATION_Y2 = 450;
    //(sx1,sy1): pair of coord. for amount of source image to shown
    public static final int SOURCE_X1 = 0;
    public static final int SOURCE_Y1 = 0;
    //(sx2,sy2): pair of coord. for amount of source image to shown
    public static final int SOURCE_X2 = 620;
    public static final int SOURCE_Y2 = 440;
 
    
        
    // FOR TILE RENDERING
    public static final int NUM_TILES = 30;
    public static final int TILE_WIDTH = 135;
    public static final int TILE_HEIGHT = 126;
    public static final int TILE_IMAGE_OFFSET_X = 45;
    public static final int TILE_IMAGE_OFFSET_Y = 30;
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
    public static final int COLOR_INC = 10;
    
    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 20;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    //public static final int NORTH_PANEL_HEIGHT = 130;
    //public static final int CONTROLS_MARGIN = 0;
    public static final int EXIT_BUTTON_X = WINDOW_WIDTH - 40;
    public static final int EXIT_BUTTON_Y = 5;
    public static final int HOME_BUTTON_X = EXIT_BUTTON_X - 40;
    public static final int HOME_BUTTON_Y = EXIT_BUTTON_Y;
    
    // UI CONTROLS POSITIONS IN THE LEVEL SELECT SCREEN
    public static final int UP_BUTTON_X = 40;
    public static final int UP_BUTTON_Y = 380;
    public static final int DOWN_BUTTON_X = 40;
    public static final int DOWN_BUTTON_Y = 430;
    public static final int RIGHT_BUTTON_X = 67;
    public static final int RIGHT_BUTTON_Y = 400;
    public static final int LEFT_BUTTON_X = 20;
    public static final int LEFT_BUTTON_Y = 400;
    
    // HELP SCREEN BUTTONS
    public static final int HELP_SCREEN_DESCRIPTION_X = 0;
    public static final int HELP_SCREEN_DESCRIPTION_Y = 45;
    public static final int SOUND_CHECK_BOX_X = 275;
    public static final int SOUND_CHECK_BOX_Y = 177;
    public static final int MUSIC_CHECK_BOX_X = 275;
    public static final int MUSIC_CHECK_BOX_Y = 250;
    
   // public static final int BACK_BUTTON_X = 130;
   // public static final int BACK_BUTTON_Y = 0;
    
   //  public static final int UNDO_BUTTON_X = 950;
   //// public static final int UNDO_BUTTON_Y = 0;
    
    
   // public static final int TILE_COUNT_X = NEW_BUTTON_X + 260 + CONTROLS_MARGIN;
  //  public static final int TILE_COUNT_Y = 0;
  //  public static final int TILE_COUNT_OFFSET = 145;
 //   public static final int TILE_TEXT_OFFSET = 60;
 //   public static final int TIME_X = TILE_COUNT_X + 232 + CONTROLS_MARGIN;
  //  public static final int TIME_Y = 0;
  //  public static final int TIME_OFFSET = 130;
  //  public static final int TIME_TEXT_OFFSET = 55;
 //   public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
   // public static final int STATS_Y = 0;
  //  public static final int TEMP_TILE_X = STATS_X + 280 + CONTROLS_MARGIN;
  //  public static final int TEMP_TILE_Y = 0;
  //  public static final int TEMP_TILE_OFFSET_X = 30;
  //  public static final int TEMP_TILE_OFFSET_Y = 12;
  //  public static final int TEMP_TILE_OFFSET2 = 105;
  //  
    // STATS DIALOG COORDINATES
   // public static final int STATS_LEVEL_INC_Y = 30;
  //  public static final int STATS_LEVEL_X = 460;
  //  public static final int STATS_LEVEL_Y = 300;
  //  public static final int ALGORITHM_TEMP_TILE_X = TEMP_TILE_X +10;
  //  public static final int  ALGORITHM_TEMP_TILE_Y =TEMP_TILE_Y+45;
   // public static final int STATS_ALGORITHM_Y = STATS_LEVEL_Y + (STATS_LEVEL_INC_Y * 2);
   // public static final int STATS_GAMES_Y = STATS_ALGORITHM_Y + STATS_LEVEL_INC_Y;
   // public static final int STATS_WINS_Y = STATS_GAMES_Y + STATS_LEVEL_INC_Y;
   // public static final int STATS_PERFECT_WINS_Y = STATS_WINS_Y + STATS_LEVEL_INC_Y;
   // public static final int STATS_FASTEST_PERFECT_WIN_Y = STATS_PERFECT_WINS_Y + STATS_LEVEL_INC_Y;
    
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
  //  public static final int WIN_PATH_NODES = 5;
  //  public static final int WIN_PATH_TOLERANCE = 100;
  //  public static final int WIN_PATH_COORD = 100;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    public static final Color COLOR_STATS = new Color(0, 60, 0);
    public static final Color COLOR_ALGORITHM_HEADER = Color.WHITE;

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
     public static final Font FONT_SORT_NAME = new Font(Font.MONOSPACED, Font.BOLD, 20);
}