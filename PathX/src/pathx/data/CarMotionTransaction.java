/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

/**
 *
 * @author Robert McKenna & ShawnCruz
 */
class CarMotionTransaction {
    
    // THE TWO INDICES OF THE DATA BEING SWAPPED
    private int fromIndex;
    private int toIndex;

    /**
     * Constructor initializes the swap indices.
     */
    public CarMotionTransaction(int initFromIndex, int initToIndex) {
        fromIndex = initFromIndex;
        toIndex = initToIndex;
    }

    // ACCESSOR METHODS
    public int getFromIndex()   { return fromIndex; }
    public int getToIndex()     { return toIndex; }

    /**
     * For testing equivalence between transaction objects.
     */
    public boolean equals(Object obj) {
        CarMotionTransaction otherTransaction = (CarMotionTransaction)obj;
        return ((fromIndex == otherTransaction.fromIndex) 
                    && (toIndex == otherTransaction.toIndex))
                ||
                    ((fromIndex == otherTransaction.toIndex) 
                    && (toIndex == otherTransaction.fromIndex));
    }
}