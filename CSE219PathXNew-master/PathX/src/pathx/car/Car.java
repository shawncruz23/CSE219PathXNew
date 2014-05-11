/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.car;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathx.data.Intersection;

/**
 *
 * @author ShawnCruz
 */
public abstract class Car extends Sprite {

    // EACH TILE HAS AN ID, WHICH WE'LL USE FOR SORTING
    private int tileId;

    // WHEN WE PUT A TILE IN THE GRID WE TELL IT WHAT COLUMN AND ROW
    // IT IS LOCATED TO MAKE THE UNDO OPERATION EASY LATER ON
    private int gridColumn;
    private int gridRow;

    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;

    // THE TARGET COORDINATES IN WHICH IT IS CURRENTLY HEADING
    private float targetX;
    private float targetY;

    // WIN ANIMATIONS CAN BE GENERATED SIMPLY BY PUTTING TILES ON A PATH    
    private ArrayList<Integer> winPath;

    // THIS INDEX KEEPS TRACK OF WHICH NODE ON THE WIN ANIMATION PATH
    // THIS TILE IS CURRENTLY TARGETING
    private int winPathIndex;
    
    private Intersection currentLocation;
    
    public Car(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, String initState) {
        super(initSpriteType, initX, initY, initVx, initVy, initState);
    }
    
     public void setLocation(Intersection newIntersection) {
        currentLocation = newIntersection;
    }
    
    public Intersection getLocation() {
       if(currentLocation != null)
           return currentLocation;
       
        System.out.println("NULL POINTER");
       return null;
    }

    // ACCESSOR METHODS
    // -getTileId
    // -getTileType
    // -getGridColumn
    // -getGridRow
    // -getTargetX
    // -getTargetY
    // -isMovingToTarget
    /**
     * Accessor method for getting the tile id for this tile.
     *
     * @return The id for this tile, which should match the rendered id.
     */
    public int getTileId() {
        return tileId;
    }

    /**
     * Accessor method for getting the tile grid column that this tile is either
     * currently in, or was most recently in.
     *
     * @return The grid column this tile is or most recently was located in.
     */
    public int getGridColumn() {
        return gridColumn;
    }

    /**
     * Accessor method for getting the tile grid row that this tile is either
     * currently in, or was most recently in.
     *
     * @return The grid row this tile is or most recently was located in.
     */
    public int getGridRow() {
        return gridRow;
    }

    /**
     * Accessor method for getting the x-axis target coordinate for this tile.
     *
     * @return The x-axis target coordinate for this tile.
     */
    public float getTargetX() {
        return targetX;
    }

    /**
     * Accessor method for getting the y-axis target coordinate for this tile.
     *
     * @return The y-axis target coordinate for this teil.
     */
    public float getTargetY() {
        return targetY;
    }

    public float getY() {
        return super.y;
    }
    
    public float getX() {
        return super.x;
    }
    
    /**
     * Accessor method for getting whether this tile is currently moving toward
     * target coordinates or not.
     *
     * @return true if this tile is currently moving toward target coordinates,
     * false otherwise.
     */
    public boolean isMovingToTarget() {
        return movingToTarget;
    }
    
    

    // MUTATOR METHODS
    // -setGridCell
    // -setTarget
    /**
     * Mutator method for setting both the grid column and row that this tile is
     * being placed in.
     *
     * @param initGridColumn The column this tile is being placed in in The
     * Sorting Hat game grid.
     *
     * @param initGridRow The row this tile is being placed in in The Sorting
     * Hat game grid.
     */
    public void setGridCell(int initGridColumn, int initGridRow) {
        gridColumn = initGridColumn;
        gridRow = initGridRow;
    }

    /**
     * Mutator method for setting bot the x-axis and y-axis target coordinates
     * for this tile.
     *
     * @param initTargetX The x-axis target coordinate to move this tile
     * towards.
     *
     * @param initTargetY The y-axis target coordinate to move this tile
     * towards.
     */
    public void setTarget(float initTargetX, float initTargetY) {
        targetX = initTargetX;
        targetY = initTargetY;
    }

    // PATHFINDING METHODS
    // -calculateDistanceToTarget
    // -initWinPath
    // -startMovingToTarget
    // -updateWinPath
    /**
     * This method calculates the distance from this tile's current location to
     * the target coordinates on a direct line.
     *
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget() {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetX - x;

        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targetY - y;

        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));

        // AND RETURN THE DISTANCE
        return distance;
    }

    /**
     * This method builds a path for this tile for the win animations by
     * slightly randomizing the locations of the nodes in the winPathNodes
     * argument.
     */
    public void initWinPath(ArrayList<Integer> winPathNodes) {
        // CONSTRUCT THE PATH
        winPath = new ArrayList(winPathNodes.size());
        for (int i = 0; i < winPathNodes.size(); i += 2) {
            // AND FILL IT WITH FUZZY PATH NODES
            int toleranceX = 1;//(int)(WIN_PATH_TOLERANCE_STAR * Math.random()) - (WIN_PATH_TOLERANCE_STAR/2);
            int toleranceY = 1;//(int)(WIN_PATH_TOLERANCE_STAR * Math.random()) - (WIN_PATH_TOLERANCE_STAR/2);
            int x = winPathNodes.get(i);// + toleranceX;
            int y = winPathNodes.get(i + 1);// + toleranceY;
            winPath.add(x);
            winPath.add(y);
        }
    }

    /**
     * Allows the tile to start moving by initializing its properly scaled
     * velocity vector pointed towards it target coordinates.
     *
     * @param maxVelocity The maximum velocity of this tile, which we'll then
     * compute the x and y axis components for taking into account the
     * trajectory angle.
     */
    public void startMovingToTarget(int maxVelocity) {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;

        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY / diffX;
        float angleInRadians = (float) Math.atan(tanResult);

        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float) (maxVelocity * Math.cos(angleInRadians));

        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) {
            vX *= -1;
        }
        if ((diffX > 0) && (vX < 0)) {
            vX *= -1;
        }

        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float) (maxVelocity * Math.sin(angleInRadians));

        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) {
            vY *= -1;
        }
        if ((diffY > 0) && (vY < 0)) {
            vY *= -1;
        }
    }
    
    public static int numTimesEntered = 0;
    
    /**
     * After a win, while the tiles are animating, this method is called each
     * frame to make sure that when the tile reaches the next node in the path,
     * it moves on to the following path node.
     *
     * @param game The Sorting Hat game we are updating.
     */
    public void updateWinPath(MiniGame game) {
        
          startMovingToTarget(1);
            // IS THE TILE ALMOST AT THE PATH NODE IT'S TARGETING?
        if (calculateDistanceToTarget() < 20) {
            
            System.out.println("NUMBER OF TIMES ENTERED: " + ++numTimesEntered);
            // PUT IT RIGHT ON THE NODE
            x = targetX;
            y = targetY;
            System.out.println("updateWinPath X: " + x);
            System.out.println("updateWinPath Y: " + y);
            // AND TARGET THE NEXT NODE IN THE PATH
            if(winPath.size() > 1) {
            targetX = winPath.get(0);
            targetY = winPath.get(1);
            
            winPath.remove(1);
            winPath.remove(0);
            }

//            x = targetX;
//            y = targetY;
            
            // START THE TILE MOVING AGAIN AND RANDOMIZE IT'S SPEED
           
           /// (Math.random() * 100) + 1);

            // AND ON TO THE NEXT PATH FOR THE NEXT TIME WE PICK A TARGET
            //winPathIndex += 2;
           // winPathIndex %= (winPath.size());
        } // JUST A NORMAL PATHING UPDATE
        else {
            // THIS WILL SIMPLY UPDATE THIS TILE'S POSITION USING ITS CURRENT VELOCITY
            super.update(game);
        }
    }

    // METHODS OVERRIDDEN FROM Sprite
    // -update
    /**
     * Called each frame, this method ensures that this tile is updated
     * according to the path it is on.
     *
     * @param game The Sorting Hat game this tile is part of.
     */
    @Override
    public void update(MiniGame game) {
        // IF WE ARE IN A POST-WIN STATE WE ARE PLAYING THE WIN
        // ANIMATION, SO MAKE SURE THIS TILE FOLLOWS THE PATH
        if (winPath != null) {
            updateWinPath(game);
        } // IF NOT, IF THIS TILE IS ALMOST AT ITS TARGET DESTINATION,
        // JUST GO TO THE TARGET AND THEN STOP MOVING
        else if (calculateDistanceToTarget() < 100) {
            vX = 0;
            vY = 0;
            x = targetX;
            y = targetY;
            movingToTarget = false;
        } // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else {
            super.update(game);
        }
    }
    
}
