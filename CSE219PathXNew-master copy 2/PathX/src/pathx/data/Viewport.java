package pathx.data;

/**
 * This class keeps track of the portion of the level the user
 * is viewing during editing.
 * 
 * @author Richard McKenna
 */
public class Viewport {
    
     // VIEWPORT POSITION
    public int x;
    public int y;
    
    // WIDTH AND HEIGHT OF THE VIEWPORT WINDOW
    public int width;
    public int height;
    
    // WIDTH AND HEIGHT OF THE LEVEL, WHICH SHOULD
    // CORRESPOND TO THE BACKGROUND IMAGE SIZE
    public int levelWidth;
    public int levelHeight;
    
    // THIS IS THE VIEWPORT ZOOM LEVEL
    private float zoomLevel;
    
    // @todo - change this variable name
    private int northPanelHeight;
    
    // THIS IS THE FULL RENDERABLE PANEL DIMENSIONS, ON WHICH WE MAY RENDER
    // OUR GUI TOOLBARS AND CONTROLS AND THE ENTIRE GAME
    private int screenWidth;
    private int screenHeight;

    // THIS IS THE SIZE OF THE GAME WORLD, WHICH MAY BE MUCH
    // LARGER THAN THE SCREEN DIMENSIONS, SINCE WE CAN SCROLL
    // THE VIEWPORT TO SEE EVERYTHING
    private int gameWorldWidth;
    private int gameWorldHeight;

    // THIS IS THE SIZE OF THE VIEWPORT
    protected int viewportWidth;
    protected int viewportHeight;
    
    // THIS IS THE MARGIN OUTSIDE THE VIEWPORT BETWEEN AREA IT
    // AND THE EDGE OF THE RENDERABLE AREA
    protected int viewportMarginLeft;
    protected int viewportMarginRight;
    protected int viewportMarginTop;
    protected int viewportMarginBottom;

    // THIS IS THE POSITION OF THE VIEWPORT IN
    // GAME WORLD COORDINATES
    protected int viewportX;
    protected int viewportY;
    
    // THESE KEEP TRACK OF THE MIN/MAX GAME WORLD
    // COORDINATES FOR OUR VIEWPORT DURING SCROLLING
    protected int minViewportX;
    protected int maxViewportX;
    protected int minViewportY;
    protected int maxViewportY;

    /**
     * Default constructor, it initializes the viewport at the origin
     * and sets its and the level's size to 0.
     */
    public Viewport()
    {
        width = 0;
        height = 0;
        x = 0;
        y = 0;
        levelWidth = width;
        levelHeight = height;
    }

    /**
     * Resets the viewport back to the top-left hand corner.
     */
    public void reset()
    {
        x = 0;
        y = 0;
    }

    /**
     * Sets the viewport's dimensions, making sure it is smaller
     * than the level's dimensions.
     */
    public void setViewportDimensions(int initWidth, int initHeight)
    {
        width = initWidth;
        height = initHeight;
        
        if (width < levelWidth)
            levelWidth = width;
        if (height < levelHeight)
            levelHeight = height;
    }

    /**
     * Sets the level's dimensions, which shoudl be larger than the viewport.
     */
    public void setLevelDimensions(int initLevelWidth, int initLevelHeight)
    {
        if ((initLevelWidth >= width) && (initLevelHeight >= height))
        {
            levelWidth = initLevelWidth;
            levelHeight = initLevelHeight;
        }
    }

    /**
     * Scrolls the viewport by (incX, incY), clamping at
     * the level borders so that we don't scroll off the level.
     */
    public void move(int incX, int incY)
    {
        x += incX;
        y += incY;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > (levelWidth - width))
            x = levelWidth - width;
        if (y > (levelHeight - height))
            y = levelHeight - height;
    }
    
    public void updateViewportBoundaries()
    {
        viewportWidth = screenWidth - viewportMarginLeft - viewportMarginRight;
        viewportHeight = screenHeight - viewportMarginTop - viewportMarginBottom;
        maxViewportX = gameWorldWidth - viewportWidth - 1;
        maxViewportY = gameWorldHeight - viewportHeight - 1;
    }
    

    /**
     * Returns true if the test rect is inside the viewport,
     * returns false otherwise.
     */
    public boolean isRectInsideViewport(int x1, int y1, int x2, int y2)
    {
        if (x2 < x) return false;
        if (x1 > (x + width)) return false;
        if (y2 < y) return false;
        if (y1 > (y + height)) return false;
        return true;
    }

    // ACCESSOR METHODS
    public int getScreenWidth()             {       return screenWidth;            }
    public int getScreenHeight()            {       return screenHeight;           }
    public int getGameWorldWidth()          {       return gameWorldWidth;         }
    public int getGameWorldHeight()         {       return gameWorldHeight;        }
    public int getViewportWidth()           {       return viewportWidth;          }
    public int getViewportHeight()          {       return viewportHeight;         }
    public int getViewportMarginLeft()      {       return viewportMarginLeft;     }
    public int getViewportMarginRight()     {       return viewportMarginRight;    }
    public int getViewportMarginTop()       {       return viewportMarginTop;      }
    public int getViewportMarginBottom()    {       return viewportMarginBottom;   }
    public int getViewportX()               {       return viewportX;              }
    public int getViewportY()               {       return viewportY;              }
    public int getMinViewportX()            {       return minViewportX;           }
    public int getMaxViewportX()            {       return maxViewportX;           }
    public int getMinViewportY()            {       return minViewportY;           }
    public int getMaxViewportY()            {       return maxViewportY;           }
    public float getZoomLevel()             {       return zoomLevel;              }
    
    /**
     * Returns true if the circle argument's bounding box is inside the
     * viewport, false otherwise.
     */
    public boolean isCircleBoundingBoxInsideViewport(int centerX, int centerY, int radius)
    {
        return isRectInsideViewport(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
    }
}
