/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.data;

import java.awt.image.BufferedImage;

/**
 *
 * @author ShawnCruz
 */
public class PathXLevelNode {

    // WE'LL STORE PROPERTIES HERE
    protected int levelReward;
    protected String levelState;
    protected String leveliD;
    protected int levelNumber;
    protected int xCoordinate, yCoordinate;
    protected BufferedImage img;

    /**
     * CONDITIONS: locked = 0: if locked (WHITE STATE) locked = 1: if unlocked,
     * but not completed (RED STATE) locked = 2: if unlocked and completed
     * (GREEN STATE)
     */
    private int locked; //ADD ENUMERATORS FOR THESE VALUES

    // THESE CONSTANTS ARE USED FOR LOADING PROPERTIES AS THEY ARE
    // THE ESSENTIAL ELEMENTS AND ATTRIBUTES
    public static final String GREEN = "IMAGE_LEVEL_SELECT_GREEN";
    public static final String RED = "IMAGE_LEVEL_SELECT_RED";
    public static final String WHITE = "IMAGE_LEVEL_SELECT_WHITE";

    /* IMAGE_LEVEL_SELECT_GREEN,
     IMAGE_LEVEL_SELECT_RED,
     IMAGE_LEVEL_SELECT_WHITE,*/
    /**
     * This constructor creates a new PathXLevelNode
     */
    public PathXLevelNode(int levelReward, int levelNumber,
            int xCoordinate, int yCoordinate, String leveliD, String levelState/*, BufferedImage img*/) {
       
        this.levelReward = levelReward;
        this.levelNumber = levelNumber;
        
        //this.levelState = levelState;
        this.leveliD = leveliD;
        this.levelState = levelState;
        
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        this.img = img;
       // locked = 0;
    }

    //MUTATOR METHODS
   public void setLockState(int newLockState) {
       locked = newLockState;
       if (locked == 0) {
           levelState = WHITE;
       } else if (locked == 1) {
           levelState = RED;
       } else if (locked == 2) {
           levelState = GREEN;
       }
    }
   
    public void setImage(BufferedImage img) {
         this.img = img;
    }

    public void setXCoordinate(int newX) {
         xCoordinate = newX;
    }

    public void setYCoordinate(int newY) {
         yCoordinate = newY;
    }


    public void setLevelReward(int reward) {
         levelReward = reward;
    }

    public void setLevelNumber(int newLevelNumber) {
        levelNumber = newLevelNumber;
    }

    public void setLevelNumber(String newLeveliD) {
        leveliD = newLeveliD;
    }

    public void setLevelState(String newLevelState) {
        levelState = newLevelState;
    }

    

    //ACCESSOR METHODS
    public int getLockState() {
        return locked;
    }
    
    public BufferedImage getImage() {
         return img;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getLevelReward() {
        return levelReward;
    }

    public String getLevelState() {
        return levelState;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLeveliD() {
        return leveliD;
    }
}
