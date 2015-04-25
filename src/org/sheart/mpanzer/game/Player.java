package org.sheart.mpanzer.game;

import org.newdawn.slick.Color;

/**
 *
 * @author yew_mentzaki
 */
public class Player {
    String name; Faction faction; Color color;

    public Player(String name, Faction faction, Color color) {
        this.name = name;
        this.faction = faction;
        this.color = color;
    }
    
}
