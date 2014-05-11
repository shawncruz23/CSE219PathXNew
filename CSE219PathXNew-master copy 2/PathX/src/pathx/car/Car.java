/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.car;

import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author ShawnCruz
 */
public abstract class Car extends Sprite {

    public Car(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, String initState) {
        super(initSpriteType, initX, initY, initVx, initVy, initState);
    }
    
}
