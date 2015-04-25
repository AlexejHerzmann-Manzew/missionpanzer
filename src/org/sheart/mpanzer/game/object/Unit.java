package org.sheart.mpanzer.game.object;

import org.sheart.mpanzer.game.*;

/**
 *
 * @author yew_mentzaki
 */
public class Unit extends GameObject{

    public float anglex, angley, anglez;
    public float accelx, accely, accelz;
    public float velocx, velocy, velocz;
    ////////////////////////////////////
    public float targetx, targety;
    public float speed = 0.05f;
    ////////////////////////////////////
    Player owner;
    public static final byte TACTIC_PROTECTIVE = 0, TACTIC_AGRESSIVE = 1, TACTIC_PASSIVE = 2;
    public byte tactic = 0;
    ////////////////////////////////////
    public float hp, maximalHp, regenerationHp;
    public float shield, maximalShield, restoreShield;
    ////////////////////////////////////
    
    public Unit(float x, float y, float z, float az, Player owner) {
        super(x, y, z);
        this.anglez = az;
        this.owner = owner;
    }
    
}
