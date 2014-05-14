/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX;
import pathx.PathX.PathXPropertyType;
import static pathx.PathX.PathXPropertyType.IMAGE_PLAYER_CAR;
import static pathx.PathXConstants.*;
import pathx.car.BanditCar;
import pathx.car.PlayerCar;
import pathx.car.PoliceCar;
import pathx.car.ZombieCar;
import pathx.data.Intersection;
import pathx.data.Level;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevelNode;
import pathx.data.PathXRecord;
import pathx.data.Road;
import static pathx.ui.PathXMiniGame.*;
import properties_manager.PropertiesManager;

/**
 * This class performs all of the rendering for The Sorting Hat game
 * application.
 *
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXPanel extends JPanel {

    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;

    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;

    // THIS IS FOR WHEN THE USE MOUSES OVER A TILE
    private BufferedImage blankTileMouseOverImage;
    private PathXRecord theRecord;
    private boolean perfectW;
    private int count;
    // private String perfectTime;
    private String pTime;

    // WE'LL RECYCLE THESE DURING RENDERING
    Ellipse2D.Double recyclableCircle;
    Ellipse2D.Double playerCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH / 2, -ONE_WAY_TRIANGLE_WIDTH / 2, ONE_WAY_TRIANGLE_WIDTH / 2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH / 2, -ONE_WAY_TRIANGLE_WIDTH / 2, 0};
    GeneralPath recyclableTriangle;
    
    // DATA FOR RENDERING
    pathx.data.Viewport viewport;
    
    //CURRENT LEVEL
    Level currentLevel;

    private ArrayList<Double> xCoord;
    private ArrayList<Double> yCoord;
    
    public static int car_X;
    public static int car_Y;

    //protected ArrayList<PathXLevelNode> levelList;
    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame The Sorting Hat game that is using this panel for
     * rendering.
     *
     * @param initData The Sorting Hat game data.
     */
    public PathXPanel(MiniGame initGame, PathXDataModel initData) {
        game = initGame;
        data = initData;
        perfectW = false;
        count = 0;
        //perfectWin = 0;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        //perfectTime = data.getCurrentLevel().//.gameTimeToText();
        pTime = data.gameTimeToText();
        //levelList = new ArrayList<>(20);

        // MAKE THE RENDER OBJECTS TO BE RECYCLED
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0, 0, 0, 0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++) {
            recyclableStrokes.put(i, new BasicStroke(i * 2));
        }

        playerCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
        
        viewport = new pathx.data.Viewport();
        
        xCoord = new ArrayList();
        yCoord = new ArrayList();
        
        
        //currentLevel = ((PathXMiniGame) game).getCurrentLevel();
    }

    
    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankTileImage The image to use as the base for rendering
     * tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage) {
        blankTileImage = initBlankTileImage;
    }

    /**
     * This mutator method sets the base image to use for rendering selected
     * tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for
     * rendering selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage) {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }

    public void setBlankTileMouseOverImage(BufferedImage initBlankTileMouseOverImage) {
        blankTileMouseOverImage = initBlankTileMouseOverImage;
    }

    private static int toggle = 0;
   
    public static boolean levelButtonHover = false;
       
    public static boolean getLevelButtonHover() {
        return levelButtonHover;
    }
    
    public static void setLevelButtonHover(boolean value) {
        levelButtonHover = value;
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        try {

            // WE'LL USE THE Graphics2D FEATURES, WHICH IS 
            // THE ACTUAL TYPE OF THE g OBJECT
            Graphics2D g2 = (Graphics2D) g;

            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();

            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            if(!((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE))
            renderBackground(g);

            if (((PathXMiniGame) game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
              
                renderMapScreen(g);
                
                //FIX UP FOR REACH LEVEL, ETC. GOOD LUCK
                  if(levelButtonHover) {
                    renderLevelInfo(g);
                }
                  
                  renderPlayerBalance(g);
            }

            if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)) {
                
           
                
              currentLevel = ((PathXMiniGame) game).getCurrentLevel();    
//                car_X = currentLevel.getStartingLocation().x;
//                car_Y = currentLevel.getStartingLocation().y;
//                // RENDER THE BACKGROUND IMAGE
             //   renderLevelBackground(g);
                
                 
                
                 // RENDER THE BACKGROUND IMAGE
                renderLevelBackground(g);
                
                // RENDER THE ROADS
                renderRoads(g2);

                // RENDER THE INTERSECTIONS
                renderIntersection(g2);
                
                   renderLevelStats(g);
                
                //renderPlayerCar(g2);
                
                //((PathXDataModel)data).initPlayerCar(car_X, car_Y);
                
//                Iterator<PlayerCar> carSprites = data.getPlayerCarStack().iterator();
//                while (carSprites.hasNext()) {
//                    Sprite s = carSprites.next();
//                    renderSprite(g, s);
//                }
                
             
              

//                ((PathXDataModel)data).playPoliceAnimation();
//                ((PathXDataModel)data).playZombieAnimation();
                
              // if (toggle % 2 == 0) {
                    //((PathXDataModel) data).playBanditAnimation();
                  //  toggle = 1;
                //}
                
                // RENDER THE INTERSECTIONS
                //renderIntersections(g2);
            }

            // ONLY RENDER THIS STUFF IF WE'RE ACTUALLY IN-GAME
            if (!data.notStarted()) {
                // RENDER THE SNAKE
                if (!data.won()) {
                    renderSnake(g);
                }

                // AND THE TILES
                renderTiles(g);

                // AND THE DIALOGS, IF THERE ARE ANY
                renderDialogs(g);

                // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
                // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
                renderGrid(g);

                // RENDER THE ALGORITHM NAME
                renderHeader(g);
            }

            // AND THE BUTTONS AND DECOR
           
            
            renderCars(g);
            
            if(((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)) {
               renderBackground(g);
                
                 renderLevelStats(g);
                 
            }
            
             renderGUIControls(g);
             
             if(((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                     && !PathXMiniGame.isDialogClosed) {
                 renderLevelInfoDialog(g);
             }
             
             if(((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                     && PathXMiniGame.isDialogWin) {
                 renderWinDialog(g);
             }
             
             if(((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                     && PathXMiniGame.isDialogLose) {
                 renderLossDialog(g);
             }

            if (!data.notStarted()) {
                // AND THE TIME AND TILES STATS
                renderStats(g);
            }

            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }
   
    public static int levelNumber;
    
    public static void setLevel(int levelNum) {
        //APPROPRIATE LEVEL TO RENDER
        levelNumber = levelNum;
    }

    /**
     * sifts through XML filename and returns
     * the proper level name
     * @param xmlFileName
     * @return 
     */
    private String convertLevelName(String xmlFileName) {
        if(xmlFileName.contains("CrastersKeep")) {
            return "Craster's Keep";
        }
        else if(xmlFileName.contains("CastleBlack")) {
            return "Castle Black";
        }
        else if(xmlFileName.contains("LastHearth")) {
            return "Last Hearth";
        }
        else if(xmlFileName.contains("DeepwoodMotte")) {
            return "Deepwood Motte";
        }
        else if(xmlFileName.contains("Winterfell")) {
            return "Winterfell";
        }
        else if(xmlFileName.contains("TorrhensSquare")) {
            return "Torrhens Square";
        }
        else if(xmlFileName.contains("WhiteHarbor")) {
            return "White Harbor";
        }
        else if(xmlFileName.contains("GreywaterWatch")) {
            return "Greywater Watch";
        }
        else if(xmlFileName.contains("TheTwins")) {
            return "The Twins";
        }
        else if(xmlFileName.contains("TheEyrie")) {
            return "The Eyrie";
        }
        else if(xmlFileName.contains("Riverrun")) {
            return "Riverrun";
        }
        else if(xmlFileName.contains("CasterlyRock")) {
            return "Casterly Rock";
        }
        else if(xmlFileName.contains("OldOak")) {
            return "Old Oak";
        }
        else if(xmlFileName.contains("Highgarden")) {
            return "Highgarden";
        }
        else if(xmlFileName.contains("OldOak")) {
            return "Old Oak";
        }
        else if(xmlFileName.contains("OldTown")) {
            return "Old Town";
        }
        else if(xmlFileName.contains("RedMountains")) {
            return "Red Mountains";
        }
        else if(xmlFileName.contains("GhostHill")) {
            return "Ghost Hill";
        }
        else if(xmlFileName.contains("Tarth")) {
            return "Tarth";
        }
        else if(xmlFileName.contains("StormsEnd")) {
            return "Storm's End";
        }
        else if(xmlFileName.contains("KingsLanding")) {
            return "King's Landing";
        }
        return "Not a Valid Name";
    }
    
     public void renderLevelInfoDialog(Graphics g) {
         
           // RENDER THE LEVEL BOUNTY
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.setColor(Color.BLACK);

            // RENDER LEVEL INFO
            String levelBounty1 = "Succesfully loot " + convertLevelName(currentLevel.getLevelName()) + ".";
            int x = 185;
            int y = 220;
            g.drawString(levelBounty1, x, y);
          
            String levelBounty2 = "Make your getaway to earn $" + currentLevel.getMoney();
           
            g.drawString(levelBounty2, x, y + 20);
     }
    
      public void renderWinDialog(Graphics g) {
          
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.setColor(Color.BLACK);
            
             // RENDER LEVEL INFO
            String winString1 = "You have succesfully looted " + convertLevelName(currentLevel.getLevelName()) + ".";
            int x = 130;
            int y = 220;
            g.drawString(winString1, x, y);
            
            String winString2 = "You've gotten away with $" + data.getLevelBounty();
             g.drawString(winString2, x, y + 20);
          
            String winString3 = "Decide what you'd like to do next.";
           
            g.drawString(winString3, x, y + 40);
      }
      
      public void renderLossDialog(Graphics g) {
          
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.setColor(Color.BLACK);
            
             // RENDER LEVEL INFO
            String winString1 = "You have been caught!";
            int x = 170;
            int y = 220;
            g.drawString(winString1, x, y);
            
            String winString2 = "Better luck next time.";
             g.drawString(winString2, x, y + 20);
          
            String winString3 = "Decide what you'd like to do next.";
           
            g.drawString(winString3, x, y + 40);
      }
     
    public void renderLevelStats(Graphics g) {
        
            // RENDER THE LEVEL BOUNTY
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            g.setColor(Color.WHITE);

            // RENDER LEVEL BOUNTY
            String levelBounty = "Level Bounty: $" + data.getLevelBounty();
            int x = 110;
            int y = 40;
            g.drawString(levelBounty, x, y);
            
            // RENDER PLAYER BALANCE
            String playerBalance = "Player Balance: $" + data.getPlayerBalance();
            g.drawString(playerBalance, x, y - 15);
            
            g.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
             g.setColor(Color.WHITE);
            //RENDER LEVEL NAME BEING PLAYED
            String levelBeingPlayed =
                    convertLevelName(data.getCurrentLevel().getLevelName());
            g.drawString(levelBeingPlayed, x + 200, y);
    }

    public static double sourceX1 = 0;
    public static double sourceY1 = 0;
    public static double sourceX2 = 812;
    public static double sourceY2 = 406;
    
    public static int destinationX1 = 107;
    public static int destinationY1 = 0;
    public static int destinationX2 = 640;
    public static int destinationY2 = 480;

    public static BufferedImage img;
    
    // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics g) {
//        Image backgroundImage = data.getBackgroundImage();
//        g.drawImage(backgroundImage, 0, 0, viewport.width, viewport.height,
//                viewport.x, viewport.y, viewport.x + viewport.width,
//                viewport.y + viewport.height, null);

        

        // GET THE MAP IMAGE PATH
        //PropertiesManager props = PropertiesManager.getPropertiesManager();
        //String imgPath = props.getProperty(LEVELS_PATH);
        String levelMapImage = currentLevel.getBackgroundImageFileName();

        //updateBackgroundImage(levelMapImage);
        
        img = game.loadImage(LEVELS_PATH + levelMapImage);
        
        
        int originalX1 = img.getMinX();
        int originalY1 = img.getMinY();
        int originalX2 = img.getWidth();
        int originalY2 = img.getHeight();
//        System.out.println("originalX1: " + originalX1 +
//        "\noriginalY1: " + originalY1 +
//        "\noriginalX2: " + originalX2 +    
//        "\noriginalY2: " + originalY2);

        //PUT IT INTO A MINIATURE FRAME ON THE LARGER GAME SCREEN
//        g.drawImage(img,
//                /*DESTINATION_X1*/destinationX1, /*DESTINATION_Y1*/destinationY1,
//                /*DESTINATION_X2*/destinationX2, /*DESTINATION_Y2*/destinationY2,
//                (int) (sourceX1), (int) (sourceY1),
//                (int) (sourceX2), (int) (sourceY2),
//                null);
        //g.drawImage(img, (int)sourceX1, (int)sourceY1, null);
         g.drawImage(img, (int) (originalX1 + sourceX1), (int)(originalY1 + sourceY1), (int)(originalX2), (int)(originalY2), null);
         //g.draw
//        System.out.println("sourceX1: " + sourceX1 + "\nsourceY1: " + sourceY1
//        + "\nsourceX2: " + sourceX2 + "\nsourceY2: " + sourceY2);
//        System.out.println("sourceX1: " + sourceX1 + "\nsourceY1: " + sourceY1
//        + "\nsourceX2: " + sourceX2 + "\nsourceY2: " + sourceY2);
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2) {
        
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        //Viewport viewport = model.getViewport();
        Iterator<Road> it = currentLevel.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext()) {
            Road road = it.next();
            if (true) {
                renderRoad(g2, road, INT_OUTLINE_COLOR);
            }
        }

        // NOW DRAW THE LINE BEING ADDED, IF THERE IS ONE
        if (true) {
//            Intersection startRoadIntersection = currentLevel.getStartingLocation();
//            recyclableLine.x1 = startRoadIntersection.x - sourceX1;
//            recyclableLine.y1 = startRoadIntersection.y - sourceY1;
//            //recyclableLine.x2 = model.getLastMouseX()-sourceX2;
//            //recyclableLine.y2 = model.getLastMouseY()-sourceY2;
//            g2.draw(recyclableLine);
        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
        //Road selectedRoad = model.getSelectedRoad();
        //if (selectedRoad != null)
        {
            //renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
        }

    }

    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c) {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit() / 10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) {
            strokeId = 1;
        }
        if (strokeId > 10) {
            strokeId = 10;
        }
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x + sourceX1 + destinationX1 - 20 - 80;
        recyclableLine.y1 = road.getNode1().y + sourceY1 + destinationY1 + 10;
        recyclableLine.x2 = road.getNode2().x + sourceX1 + destinationX1 - 20 - 80;
        recyclableLine.y2 = road.getNode2().y + sourceY1 + destinationY1 + 10;

       
        
        // AND DRAW IT
        g2.draw(recyclableLine);

        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay()) {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }
    
     // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
    }

    public static ArrayList<Double> coordX = new ArrayList<>();
    public static ArrayList<Double> coordY = new ArrayList<>();
    
    public static boolean initialized = false;
    
    public void renderIntersection(Graphics2D g) {

        Iterator<Intersection> it = currentLevel.intersectionsIterator();
        while (it.hasNext()) {

            Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!currentLevel.isStartingLocation(intersection))
                    && (!currentLevel.isDestination(intersection))
                   ) {
                // FIRST FILL
                if (intersection.isOpen()) {
                    g.setColor(OPEN_INT_COLOR);
                } else {
                    g.setColor(CLOSED_INT_COLOR);
                }
               
                
                recyclableCircle.x = intersection.x + sourceX1 - INTERSECTION_RADIUS + destinationX1 - 20 - 80;
                recyclableCircle.y = intersection.y + sourceY1 - INTERSECTION_RADIUS + destinationY1 + 10;

                
              
              //  System.out.println("ROAD DIFFERENCE X: " + (sourceX1 - INTERSECTION_RADIUS + destinationX1 - 20));
               // System.out.println("ROAD DIFFERENCE Y: " + (sourceY1 - INTERSECTION_RADIUS + destinationY1));

//                if ((recyclableCircle.x > DESTINATION_X1 && recyclableCircle.x < DESTINATION_X2)
//                        && (recyclableCircle.y > DESTINATION_Y1 && recyclableCircle.y < DESTINATION_Y2)) {
                {
                    g.fill(recyclableCircle);

                    // AND NOW THE OUTLINE
                    if (currentLevel.isSelectedIntersection(intersection)) {
                        g.setColor(HIGHLIGHTED_COLOR);
                    } else {
                        g.setColor(INT_OUTLINE_COLOR);
                    }
                    Stroke s = recyclableStrokes.get(INT_STROKE);
                    g.setStroke(s);
                    g.draw(recyclableCircle);
                }
            }
        }

//        System.out.println("starting location: " + currentLevel.getStartingLocationImageFileName());
//        System.out.println("ending location: " + currentLevel.getDestinationImageFileName());

        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        Image startImage = game.loadImage(LEVELS_PATH + currentLevel.getStartingLocationImageFileName());
        Intersection startInt = currentLevel.getStartingLocation();
        renderIntersectionImage(g, startImage, startInt);

        Image destImage = game.loadImage(LEVELS_PATH + currentLevel.getDestinationImageFileName());
        Intersection destInt = currentLevel.getDestination();
        renderIntersectionImage(g, destImage, destInt);
    }

    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i) {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x - (w / 2);
        int y1 = i.y - (h / 2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);

        // ONLY RENDER IF INSIDE THE VIEWPORT
//        if (x1 > DESTINATION_X1  && y1 > DESTINATION_Y1  && x2 < DESTINATION_X2  && y2 < DESTINATION_Y2 /*change to check boundaries*/)
        {
            g2.drawImage(img, (int)(x1 + sourceX1 + destinationX1 - 20 - 80), (int)(y1 + sourceY1 + destinationY1 + 10), null);
        }
    }

    //public static int counter = 0;
    public static int checker = 0;
    public static double playerX = 0;
    public static double playerY = 0;
    
    
    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage) {  
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        //currentLevel.setBackgroundImageFileName(newBgImage);
        Image backgroundImage = game.loadImage(LEVELS_PATH + currentLevel.getBackgroundImageFileName());
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setLevelDimensions(levelWidth, levelHeight);
        //repaint();
    }
    //public void render 
    // RENDERING HELPER METHODS
    // - renderBackground
    // - renderGUIControls
    // - renderSnake
    // - renderTiles
    // - renderDialogs
    // - renderGrid
    // - renderDebuggingText
    /**
     * `
     * Renders the background image, which is different depending on the screen.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

        public ArrayList<Double> getXCoordinates() {
            if(xCoord.size() == 0) System.out.println("EMPTY X LIST");
        return xCoord;
    }
    
    public ArrayList<Double> getYCoordinates() {
        if(yCoord.size() == 0) System.out.println("EMPTY Y LIST");
        return yCoord;
    }
    
    /**
     * Renders all the GUI decor and buttons.
     *
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE) {
                renderSprite(g, s);
            }
        }
        
         Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites) {
            renderSprite(g, s);
        }
        //System.out.println("renderGUIControls was called");
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
           
            renderSprite(g, s);
        }
            
    }
    
    public void renderCars(Graphics g) {
        
        Iterator<PlayerCar> playerCarSprite = data.getPlayerCar().iterator();
        while (playerCarSprite.hasNext()) {
            Sprite s = playerCarSprite.next();
            renderSprite(g, s);
        }
        
        Iterator<BanditCar> banditCarSprite = data.getBanditCarStack().iterator();
        while (banditCarSprite.hasNext()) {
            Sprite s = banditCarSprite.next();
            renderSprite(g, s);
        }
        
        Iterator<ZombieCar> zombieCarSprite = data.getZombieCarStack().iterator();
        while (zombieCarSprite.hasNext()) {
            Sprite s = zombieCarSprite.next();
            renderSprite(g, s);
        }
        
        Iterator<PoliceCar> policeCarSprite = data.getPoliceCarStack().iterator();
        while (policeCarSprite.hasNext()) {
            Sprite s = policeCarSprite.next();
            renderSprite(g, s);
        }
    }

    public void renderHeader(Graphics g) {
        g.setColor(COLOR_ALGORITHM_HEADER);

    }

    public static int control = 0;
    public static int counter = 0;

    /**
     * Renders the Map and Level Select Buttons
     *
     * @param g this panel's rendering context.
     */
    public void renderMapScreen(Graphics g) {

        //System.out.println("HELLO");
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img, buttonImg;

        if (control == 0) {
            ((PathXMiniGame) game).updateLevelSelectButtons();
            control++;
        }

        ArrayList<PathXLevelNode> levelList = ((PathXMiniGame) game).getLevelList();

        //System.out.println("COUNTER: " + counter++);
        //ArrayList<PathXLevelNode> levelList = ((PathXMiniGame)game).getLevelList();
        //BufferedImage img2 = levelList.get(0).
        // GET THE MAP IMAGE PATH
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        String mapImage = props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MAP);

        img = game.loadImage(imgPath + mapImage);

        //PUT IT INTO A MINIATURE FRAME ON THE LARGER GAME SCREEN
        g.drawImage(img,
                DESTINATION_X1, DESTINATION_Y1,
                DESTINATION_X2, DESTINATION_Y2,
                (int) sx1, (int) sy1,
                (int) sx2, (int) sy2,
                null);//POSITION IN LEFT HAND CORNER
        //((PathXMiniGame)game).updateButtons();

        // if(((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
        for (int i = 0; i < levelList.size(); i++) {
//       
            if (levelList.get(i).getLevelKey() == 0) {
                levelList.get(i).setLevelState(PathXCarState.WHITE_STATE.toString());
            } else if (levelList.get(i).getLevelKey() == 1) {
                levelList.get(i).setLevelState(PathXCarState.RED_STATE.toString());
            } else if (levelList.get(i).getLevelKey() == 2) {
                levelList.get(i).setLevelState(PathXCarState.GREEN_STATE.toString());
            }
            
            //TEMPORARY!!! GET RID OF AFTER HW6 FOR REAL GAMEPLAY
            if(PathXEventHandler.cheatIsEnabled)
            levelList.get(i).setLevelState(PathXCarState.RED_STATE.toString());
            
            
            
            //levelList.get(i).setLevelState(PathXCarState.WHITE_STATE.toString());
            buttonImg = game.getGUIButtons().get(levelList.get(i).getLeveliD()).getSpriteType().getStateImage(levelList.get(i).getLevelState());
            if ((levelList.get(i).getXCoordinate() > 10) && (levelList.get(i).getXCoordinate() < 630)
                    && (levelList.get(i).getYCoordinate() > 45) && (levelList.get(i).getYCoordinate() < 435)) {
                
                game.getGUIButtons().get(levelList.get(i).getLeveliD()).setX((float) levelList.get(i).getXCoordinate());
                game.getGUIButtons().get(levelList.get(i).getLeveliD()).setY((float) levelList.get(i).getYCoordinate());
                g.drawImage(buttonImg, (int) levelList.get(i).getXCoordinate(), (int) levelList.get(i).getYCoordinate(), null);

                if (levelList.get(i).getLevelState().equals(PathXCarState.RED_STATE.toString())
                        || levelList.get(i).getLevelState().equals(PathXCarState.GREEN_STATE.toString())) {
                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setX((float) levelList.get(i).getXCoordinate());
                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setY((float) levelList.get(i).getYCoordinate());
                    //System.out.println("PANEL");

                    Sprite levelButton = game.getGUIButtons().get(levelList.get(i).getLeveliD());
                    levelButton.setActionCommand(levelList.get(i).getLeveliD());

                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setActionListener(new ActionListener() {

                        Sprite s;

                        public ActionListener init(Sprite initS) {
                            s = initS;
                            return this;
                        }

                        public void actionPerformed(ActionEvent ae) {
                            System.out.println("Action Command: " + s.getActionCommand());
                            //System.out.println("Other Part of Command: " + levelButton);
                            System.out.println("YOU SELECTED THE RED BUTTON IN THE PANEL CLASS!");
                            ((PathXMiniGame) game).getEventHandler().respondToGameplayScreen(s.getActionCommand());
                        }
                    }.init(levelButton));
                }
            } else {
                levelList.get(i).setLevelState(PathXCarState.INVISIBLE_STATE.toString());
                buttonImg = game.getGUIButtons().get(levelList.get(i).getLeveliD()).getSpriteType().getStateImage(levelList.get(i).getLevelState());
                if ((levelList.get(i).getXCoordinate() > 10) && (levelList.get(i).getXCoordinate() < 630)
                        && (levelList.get(i).getYCoordinate() > 45) && (levelList.get(i).getYCoordinate() < 435)) {
                    g.drawImage(buttonImg, (int) levelList.get(i).getXCoordinate(), (int) levelList.get(i).getYCoordinate(), null);
                }
            }
            game.getGUIButtons().get(levelList.get(i).getLeveliD()).setX((float) levelList.get(i).getXCoordinate());
            game.getGUIButtons().get(levelList.get(i).getLeveliD()).setY((float) levelList.get(i).getYCoordinate());
        }
        // }

//        if (((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
//            for (int i = 0; i < levelList.size(); i++) {
//                //System.out.println("Node(" +i+ "):" + "(" + );
//                game.getGUIButtons().get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                game.getGUIButtons().get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                if ((levelList.get(i).getXCoordinate() > 10) && (levelList.get(i).getXCoordinate() < 630)
//                        && (levelList.get(i).getYCoordinate() > 45) && (levelList.get(i).getYCoordinate() < 435)) {
//                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setState(levelList.get(i).getLevelState());
//                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setEnabled(true);
//
//                    if (levelList.get(i).getLevelState().equals(PathXCarState.RED_STATE.toString())) {
//
//                        game.getGUIButtons().get(levelList.get(i).getLeveliD()).setActionListener(new ActionListener() {
//                            public void actionPerformed(ActionEvent ae) {
//                                System.out.println("YOU SELECTED THE RED BUTTON!");
//                                ((PathXMiniGame)game).getEventHandler().respondToGameplayScreen();
//                            }
//                        });
//                    }
//                // s = new Sprite(sT, xButtonLevel1, yButtonLevel1, 0, 0, PathXCarState.VISIBLE_STATE.toString());
//                    //game.getGUIButtons().put(LEVEL_SELECT_BUTTON_TYPE, s);
//                } else {
//                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setState(PathXCarState.INVISIBLE_STATE.toString());
//                    game.getGUIButtons().get(levelList.get(i).getLeveliD()).setEnabled(false);
//
//                }
//
//            }
//        }
//             for(int i = 0; i < levelList.size(); i++) {
//           buttonImg =  game.getGUIButtons().get(levelList.get(i).getLeveliD()).getSpriteType().getStateImage(levelList.get(i).getLevelState());
//             g.drawImage(buttonImg, (int) levelList.get(i).getXCoordinate()  ,
//                (int) levelList.get(i).getYCoordinate() ,
//                null);//POSITION IN LEFT HAND CORNER
//        }
    }

    public void renderSnake(Graphics g) {
        /*
         ArrayList<SnakeCell> snake = data.getSnake();
         int red = 255;
         Viewport viewport = data.getViewport();
         for (SnakeCell sC : snake)
         {
         int x = data.calculateGridTileX(sC.col);
         int y = data.calculateGridTileY(sC.row);            
         g.setColor(new Color(0, 0, red, 200));
         g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
         red -= COLOR_INC;
         g.setColor(Color.BLACK);
         g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
         }
         */
    }

    /**
     * This method renders the on-screen stats that change as the game
     * progresses. This means things like the game time and the number of tiles
     * remaining.
     *
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g) {
        /*
         // RENDER THE GAME TIME AND THE TILES LEFT FOR IN-GAME
         if (((SortingHatMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) 
         && data.inProgress() || data.isPaused())
         {
         // RENDER THE TILES LEFT
         g.setFont(FONT_TEXT_DISPLAY);
         g.setColor(Color.BLACK);

         // RENDER THE TIME
         String time = data.gameTimeToText();
         int x = TIME_X + TIME_OFFSET;
         int y = TIME_Y + TIME_TEXT_OFFSET;
         g.drawString(time, x, y);
            
            
         // RENDER THE MISCAST
         String miscastCount = ""+data.getBadSpellsCounter();
         int xm = TILE_COUNT_X +TILE_COUNT_OFFSET;
         int ym = TILE_COUNT_Y +TILE_TEXT_OFFSET;
         g.drawString(miscastCount, xm, ym);
            
            
         // RENDER THE Algorithm Display
         //  PropertiesManager props = PropertiesManager.getPropertiesManager();     
         // String algorithmPrompt = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_ALGORITHM);
         //  g.drawString(algorithmPrompt + algorithm,                   STATS_LEVEL_X, STATS_ALGORITHM_Y);
         //  String algorithm = ""+
         ///SortingHatRecord algorithm1 = ((SortingHatMiniGame)game).getPlayerRecord();
         SortingHatRecord record = ((SortingHatMiniGame)game).getPlayerRecord();
         String algorithm =  data.getAlgorithmName();
         //game.getDataModel().//algorithm1.getAlgorithm(data.getCurrentLevel());
         int xa = ALGORITHM_TEMP_TILE_X;
         int ya = ALGORITHM_TEMP_TILE_Y;
           
         g.setFont(FONT_SORT_NAME);
         g.setColor(COLOR_ALGORITHM_HEADER);
         g.drawString(algorithm, xa, ya);
            
            
            
         }        
        
        
        
         // IF THE STATS DIALOG IS VISIBLE, ADD THE TEXTUAL STATS
         if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(SortingHatTileState.VISIBLE_STATE.toString()))
         {
         g.setFont(FONT_STATS);
         g.setColor(COLOR_STATS);
         String currentLevel = data.getCurrentLevel();
         int lastSlash = currentLevel.lastIndexOf("/");
         String levelName = currentLevel.substring(lastSlash+1);
         SortingHatRecord record = ((SortingHatMiniGame)game).getPlayerRecord();

         // GET ALL THE STATS
         String algorithm = record.getAlgorithm(currentLevel);
         int games = record.getGamesPlayed(currentLevel);
         int wins = record.getWins(currentLevel);
         int perfectWin=record.getPerfectWins(currentLevel);
         String perfectTime = record.getPerfectFastTime(currentLevel);
            
           
                
           
                 
           
         //  if(data.getBadSpellsCounter()==0 && data.won() )
         //  {
                
                 
         //   if(perfectTime==null){
         //        System.out.println(" sesstion 1 "+perfectTime);
         //            perfectTime = data.gameTimeToText();
                     
         // pTime =perfectTime;
         //  }
         //     if((data.gameTimeToText().compareTo(perfectTime))<0)
         //      {
         //    perfectTime = data.gameTimeToText();
         //       System.out.println("Thestint time askahglkdbelsbsdlgbdlghsl");
         //  }
                 
                
         //else 
         //   perfectTime = perfectTime;
               
         // as
         // }

         // GET ALL THE STATS PROMPTS
         PropertiesManager props = PropertiesManager.getPropertiesManager();            
         String algorithmPrompt = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_ALGORITHM);
         String gamesPrompt = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_GAMES);
         String winsPrompt = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_WINS);
         String perfectWins = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_PERFECT_WINS);
         String fastPerfectWins = props.getProperty(PathXPropertyType.TEXT_LABEL_STATS_FASTEST_PERFECT_WIN);

         // NOW DRAW ALL THE STATS WITH THEIR LABELS
         int dot = levelName.indexOf(".");
         levelName = levelName.substring(0, dot);
         g.drawString(levelName,                                     STATS_LEVEL_X, STATS_LEVEL_Y);
         g.drawString(algorithmPrompt + algorithm,                   STATS_LEVEL_X, STATS_ALGORITHM_Y);
         g.drawString(gamesPrompt + games,                           STATS_LEVEL_X, STATS_GAMES_Y);
         g.drawString(winsPrompt + wins,                             STATS_LEVEL_X, STATS_WINS_Y);
            
         g.drawString(perfectWins + perfectWin,                      STATS_LEVEL_X, STATS_PERFECT_WINS_Y );
         g.drawString(fastPerfectWins + perfectTime,                STATS_LEVEL_X, STATS_FASTEST_PERFECT_WIN_Y );
             
         }
         */
    }

    /**
     * Renders all the game tiles, doing so carefully such that they are
     * rendered in the proper order.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderTiles(Graphics g) {
        /*
         // DRAW THE GRID
         ArrayList<SortingHatTile> tilesToSort = data.getTilesToSort();
         for (int i = 0; i < tilesToSort.size(); i++)
         {
         SortingHatTile tile = tilesToSort.get(i);
         if (tile != null)
         renderTile(g, tile);
         }
        
         // THEN DRAW ALL THE MOVING TILES
         Iterator<SortingHatTile> movingTiles = data.getMovingTiles();
         while (movingTiles.hasNext())
         {
         SortingHatTile tile = movingTiles.next();
         renderTile(g, tile);
         }
        
         // AND THE SELECTED TILE, IF THERE IS ONE
         SortingHatTile selectedTile = data.getSelectedTile();
         if (selectedTile != null)
         renderTile(g, selectedTile);
         */
    }

    /**
     * Helper method for rendering the tiles that are currently moving.
     *
     * @param g Rendering context for this panel.
     *
     * @param tileToRender Tile to render to this panel.
     */
    public void renderTile(Graphics g, PathXCar tileToRender) {
        // ONLY RENDER VISIBLE TILES
        if (!tileToRender.getState().equals(PathXCarState.INVISIBLE_STATE.toString())) {
            Viewport viewport = data.getViewport();
            int correctedTileX = (int) (tileToRender.getX());
            int correctedTileY = (int) (tileToRender.getY());

            // THEN THE TILE IMAGE
            SpriteType bgST = tileToRender.getSpriteType();
            Image img = bgST.getStateImage(tileToRender.getState());
            g.drawImage(img, correctedTileX,
                    correctedTileY,
                    bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    /**
     * Renders the game dialog boxes.
     *
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites) {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }

    /**
     * Renders the s Sprite into the Graphics context g. Note that each Sprite
     * knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        
        if (!s.getState().equals(PathXCarState.INVISIBLE_STATE.toString())) {
         
            if (s.getSpriteType().getSpriteTypeID() == PLAYER_CAR_TYPE
                    || s.getSpriteType().getSpriteTypeID().contains(BANDIT_TYPE_PREFIX)
                    || s.getSpriteType().getSpriteTypeID().contains(POLICE_TYPE_PREFIX)
                    || s.getSpriteType().getSpriteTypeID().contains(ZOMBIE_TYPE_PREFIX)) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX() + (int)sourceX1, (int) s.getY() + (int)sourceY1, bgST.getWidth(), bgST.getHeight(), null);
            }
            else {
                
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);
            }
        }
        
    }

    public void renderPlayerBalance(Graphics g) {
        
         g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            //g.setColor(Color.CYAN);
            g.setColor(Color.WHITE);

            // RENDER LEVEL NAME/NUMBER
            String playerBalance = "Current Balance: $" + data.getPlayerBalance();
            int x = 318;
            int y = 40;
            g.drawString(playerBalance, x, y);
    }
    
    public void renderLevelInfo(Graphics g) {
        
            // RENDER THE LEVEL BOUNTY
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            //g.setColor(Color.CYAN);
            g.setColor(LEVEL_DESCRIPTION_HEADER);

            // RENDER LEVEL NAME/NUMBER
            String messageTop = "Level #"+levelNumber+": " + getLevelNameByNumber(levelNumber);
            int x = 85;
            int y = 20;
            g.drawString(messageTop, x, y);
            
            String messageBottom = "Bounty: $" + bountyAmount(levelNumber);
            g.drawString(messageBottom, x, y + 20);
    }
    
    public int bountyAmount(int i) {
        if(i == 1) return 100;
        else
        if(i == 2) return 100;
        else
        if(i == 3) return 100;
        else
        if(i == 4) return 200;
        else
        if(i == 5) return 200;
        else
        if(i == 6) return 200;
        else
        if(i == 7) return 300;
        else
        if(i == 8) return 300;
        else
        if(i == 9) return 300;
        else
        if(i == 10) return 400;
        else
        if(i == 11) return 400;
        else
        if(i == 12) return 400;
        else
        if(i == 13) return 500;
        else
        if(i == 14) return 500;
        else
        if(i == 15) return 500;
        else
        if(i == 16) return 600;
        else
        if(i == 17) return 600;
        else
        if(i == 18) return 600;
        else
        if(i == 19) return 700;
        else
        if(i == 20) return 800;
        
        return 666;
    }
    
    private String getLevelNameByNumber(int i) {
        if(i == 1) return "Craster's Keep";
        else
        if(i == 2) return "Castle Black";
        else
        if(i == 3) return "Last Hearth";
        else
        if(i == 4) return "Deepwood Motte";
        else
        if(i == 5) return "Winterfell";
        else
        if(i == 6) return "Torrhen's Square";
        else
        if(i == 7) return "White Harbor";
        else
        if(i == 8) return "Greywater Watch";
        else
        if(i == 9) return "The Twins";
        else
        if(i == 10) return "The Eyrie";
        else
        if(i == 11) return "Riverrun";
        else
        if(i == 12) return "Casterly Rock";
        else
        if(i == 13) return "Old Oak";
        else
        if(i == 14) return "Highgarden";
        else
        if(i == 15) return "Old Town";
        else
        if(i == 16) return "Red Mountains";
        else
        if(i == 17) return "Ghost Hill";
        else
        if(i == 18) return "Tarth";
        else
        if(i == 19) return "Storm's End";
        else
        if(i == 20) return "King's Landing";
        
        return "Invalid input";
    }
    
    /**
     * This method renders grid lines in the game tile grid to help during
     * debugging.
     *
     * @param g Graphics context for this panel.
     */
    public void renderGrid(Graphics g) {
        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
        if (data.isDebugTextRenderingActive()) {
            for (int i = 0; i < data.getNumGameGridColumns(); i++) {
                for (int j = 0; j < data.getNumGameGridRows(); j++) {
                    //int x = data.calculateGridTileX(i);
                    //int y = data.calculateGridTileY(j);
                    //g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
                }
            }
        }
    }

    /**
     * Renders the debugging text to the panel. Note that the rendering will
     * only actually be done if data has activated debug text rendering.
     *
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g) {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive()) {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);

            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext()) {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }

}
