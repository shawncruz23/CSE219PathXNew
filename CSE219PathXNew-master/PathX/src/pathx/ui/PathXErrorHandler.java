package pathx.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import pathx.PathX.PathXPropertyType; 
import properties_manager.PropertiesManager;

/**
 * This class provides dialog box responses to when errors happen.
 * 
 * @author Richard McKenna & Shawn Cruz
 */
public class PathXErrorHandler {
    
    // WE'LL CENTER DIALOG BOXES OVER THE WINDOW, SO WE NEED THIS
    private JFrame window;

    /**
     * This simple little class just needs the window.
     * 
     * @param initWindow 
     */
    public PathXErrorHandler(JFrame initWindow) {
        // KEEP THE WINDOW FOR LATER
        window = initWindow;
    }

    /**
     * This method provides all error feedback. It gets the feedback text,
     * which changes depending on the type of error, and presents it to
     * the user in a dialog box.
     * 
     * @param errorType Identifies the type of error that happened, which
     * allows us to get and display different text for different errors.
     */
    public void processError(PathXPropertyType errorType) {
        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String errorFeedbackText = props.getProperty(errorType);
        
        // NOTE THAT WE'LL USE THE SAME DIALOG TITLE FOR ALL ERROR TYPES
        String errorTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_ERROR);
        
        // POP OPEN A DIALOG TO DISPLAY TO THE USER
        JOptionPane.showMessageDialog(window, errorFeedbackText, errorTitle, JOptionPane.ERROR_MESSAGE);
    }    
}