/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType; 
import pathx.data.PathXRecord;
import pathx.data.PathXDataModel;
import pathx.ui.PathXMiniGame;
import pathx.data.PathXLevelRecord;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;

/**
 * This class provides services for efficiently loading and saving
 * binary files for The Sorting Hat game application.
 * 
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXFileManager
{
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE
    private PathXMiniGame miniGame;
    
    /**
     * Constructor for initializing this file manager, it simply keeps
     * the game for later.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public PathXFileManager(PathXMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }

    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(String levelFile)
    {
        /*
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            File fileToOpen = new File(levelFile);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            
            // FIRST READ THE ALGORITHM NAME TO USE FOR THE LEVEL
            String algorithmName = dis.readUTF();
         //   SortingHatAlgorithmType algorithmTypeToUse = SortingHatAlgorithmType.valueOf(algorithmName);
           // SortingHatAlgorithm algorithmToUse = SortingHatAlgorithmFactory.buildSortingHatAlgorithm(algorithmTypeToUse, ((SortingHatDataModel)miniGame.getDataModel()).getTilesToSort());
            
            // THEN READ THE GRID DIMENSIONS
            // WE DON'T ACTUALLY USE THESE
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            
            ArrayList<SnakeCell> newSnake = new ArrayList();
            
            // READ IN THE SNAKE CELLS, KEEPING TRACK OF THE
            // GRID BOUNDS AS WE GO
            int initSnakeLength = dis.readInt();
            int minCol = initSnakeLength; int maxCol = 0; int minRow = initSnakeLength; int maxRow = 0;
            for (int i = 0; i < initSnakeLength; i++)
            {
                int col = dis.readInt();
                int row = dis.readInt();
                if (col < minCol) minCol = col;
                if (row < minRow) minRow = row;
                if (col > maxCol) maxCol = col;
                if (row > maxRow) maxRow = row;
                SnakeCell newCell = new SnakeCell(col, row);
                newSnake.add(newCell);
            }
            int numColumns = maxCol - minCol + 1;
            int numRows = maxRow - minRow + 1;
            
            // WE SHOULD NOW HAVE THE CORRECT MIN AND MAX ROWS AND COLUMNS,
            // SO LET'S USE THAT INFO TO CORRECT THE SNAKE SO THAT IT'S PACKED
            for (int i = 0; i < newSnake.size(); i++)
            {
                SnakeCell sC = newSnake.get(i);
                sC.col -= minCol;
                sC.row -= minRow;
            }
            
            // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT
            PathXDataModel dataModel = (PathXDataModel)miniGame.getDataModel();
            Viewport viewport = dataModel.getViewport();
            viewport.setGameWorldSize(numColumns * TILE_WIDTH, numRows * TILE_HEIGHT);
            viewport.setNorthPanelHeight(NORTH_PANEL_HEIGHT);
            viewport.initViewportMargins();
            dataModel.setCurrentLevel(levelFile);
            dataModel.initLevel(levelFile, newSnake, algorithmToUse);
        }
        catch(Exception e)
        {
            // LEVEL LOADING ERROR
            miniGame.getErrorHandler().processError(PathXPropertyType.TEXT_ERROR_LOADING_LEVEL);
        }
        */
    }    
    
    /**
     * This method saves the record argument to the player records file.
     * 
     * @param record The complete player record, which has the records
     * on all levels.
     */
    public void saveRecord(PathXRecord record)
    {
/*
        try {
         PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(PathXPropertyType.FILE_PLAYER_RECORD);
            File fileToSave = new File(recordPath);
            
            
            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
           
  
            FileOutputStream fos = new FileOutputStream(fileToSave);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
          byte[] bytes = record.toByteArray();
            System.out.println(bytes);
            bos.write(bytes);
            bos.close();
        } catch(Exception e)
        {
            // LEVEL LOADING ERROR
            miniGame.getErrorHandler().processError(PathXPropertyType.TEXT_ERROR_LOADING_LEVEL);
        }
 */       
    }

    /**
     * This method loads the player record from the records file
     * so that the user may view stats.
     * 
     * @return The fully loaded record from the player record file.
     */
    public PathXRecord loadRecord()
    {
       /*
        PathXRecord recordToLoad = new PathXRecord();
        /*
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(PathXPropertyType.FILE_PLAYER_RECORD);
            File fileToOpen = new File(recordPath);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
            int numLevels = dis.readInt();

            for (int i = 0; i < numLevels; i++)
            {
                String levelName = dis.readUTF();
                SortingHatLevelRecord rec = new SortingHatLevelRecord();
                rec.algorithm = dis.readUTF();
                rec.gamesPlayed = dis.readInt();
                rec.wins = dis.readInt();
                rec.perfectWin=dis.readInt();
                rec.fastWin=dis.readUTF();
                recordToLoad.addSortingHatLevelRecord(levelName, rec);
            }
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
     */   return null;
    }
}