/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.Graphics;
import pathx.ui.PathXCar;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import pathx.PathX.PathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXPanel;
import pathx.ui.PathXCarState;

/**
 * This class manages the game data for PathX
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXDataModel extends MiniGameDataModel {

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THIS STORES THE CARS ON THE GRID DURING THE GAME
    private ArrayList<PathXCar> tilesToSort;
   

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
    private String currentLevel;


    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
    private ArrayList<CarMotionTransaction> properTransactionOrder;
    private int transactionCounter;
  //  private boolean isUndo;
    

 
    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent on file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The Sorting Hat game UI.
     */
    public PathXDataModel(MiniGame initMiniGame) {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;

        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        stackCars = new ArrayList();
        movingCars = new ArrayList();
        
       TTT= new String();

        // NOTHING IS BEING DRAGGED YET
        selectedCar = null;
        selectedCarIndex = -1;
        tempCar = null;
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

    public String getCurrentLevel() {
        return currentLevel;
    }

    public Iterator<PathXCar> getMovingTiles() {
        return movingCars.iterator();
    }

    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel) {
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
        x -= viewport.getViewportMarginLeft();

        // ADJUST FOR THE VIEWPORT
        x = x + viewport.getViewportX();

        if (x < 0)
        {
            return -1;
        }

        // AND NOW GET THE COLUMN
        return x / TILE_WIDTH;
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
        y -= viewport.getViewportMarginTop();

        // ADJUST FOR THE VIEWPORT
        y = y + viewport.getViewportY();

        if (y < 0)
        {
            return -1;
        }

        // AND NOW GET THE ROW
        return y / TILE_HEIGHT;
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
        tile2.setTarget(calculateGridTileX(tile1.getGridColumn()), calculateGridTileY(tile1.getGridRow()));

        // THEN MOVE TILE 1
        tilesToSort.set(index2, tile1);
        tile1.setGridCell(tile2Col, tile2Row);
        tile1.setTarget(calculateGridTileX(tile2Col), calculateGridTileY(tile2Row));
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
            endGameAsWin();
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
        // FIGURE OUT THE CELL IN THE GRID
        int col = calculateGridCellColumn(x);
        int row = calculateGridCellRow(y);

        // DISABLE THE STATS DIALOG IF IT IS OPEN
      //  if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(PathXCarState.VISIBLE_STATE.toString()))
        {
     //       game.getGUIDialogs().get(STATS_DIALOG_TYPE).setState(PathXCarState.INVISIBLE_STATE.toString());
     //       return;
        }

        // CHECK THE CELL AT col, row
        int index = getSnakeIndex(col, row);
        
        // IT'S OUTSIDE THE GRID
        if (index < 0)
        {
            // DESELECT A TILE IF ONE IS SELECTED
            if (selectedCar != null)
            {
                selectedCar.setState(PathXCarState.VISIBLE_STATE.toString());
                selectedCar = null;
                selectedCarIndex = -1;
                miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_DESELECT_TILE.toString(), false);
            }
        }
        // IT'S IN THE GRID
        else
        {
            // SELECT THE TILE IF NONE IS SELECTED
            if (selectedCar == null)
            {
                selectedCar = tilesToSort.get(index);
                selectedCar.setState(PathXCarState.SELECTED_STATE.toString());
                selectedCarIndex = index;
                miniGame.getAudio().play(PathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
            }
            // A TILE WAS ALREADY SELECTED, SO THIS MUST HAVE BEEN THE SECOND TILE
            // SELECTED, SO SWAP THEM
            else
            {
                processSwap(index, selectedCarIndex);
            }
        }
    }

    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin()
    {
        /*
        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();

        // RECORD THE TIME IT TOOK TO COMPLETE THE GAME
        long gameTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        // RECORD IT AS A WIN
        ((SortingHatMiniGame) miniGame).getPlayerRecord().addWin(currentLevel);

        if(badSpellsCounter==0)
            ((SortingHatMiniGame) miniGame).getPlayerRecord().addPerfectWin(currentLevel, this.gameTimeToText());
           
        // SAVE PLAYER DATA
        ((SortingHatMiniGame) miniGame).savePlayerRecord();

        // DISPLAY THE WIN DIALOG
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());

        // AND PLAY THE WIN ANIMATION
        playWinAnimation();

        // AND PLAY THE WIN AUDIO
        miniGame.getAudio().stop(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString());
        miniGame.getAudio().stop(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString());
      
        miniGame.getAudio().play(SortingHatPropertyType.AUDIO_CUE_WIN.toString(), false);
        */
    }
    
    /**
     * Updates the player record, adding a game without a win.
     */
    public void endGameAsLoss()
    {
        // ADD A LOSS
      //  ((SortingHatMiniGame) miniGame).getPlayerRecord().addLoss(currentLevel);
 
        // SAVE PLAYER DATA
    //    ((SortingHatMiniGame) miniGame).savePlayerRecord();        
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

            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingCars.size(); i++)
            {
                // GET THE NEXT TILE
                PathXCar tile = movingCars.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingCars.remove(tile);
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
}