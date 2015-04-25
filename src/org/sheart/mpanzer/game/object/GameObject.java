/*
*   Класс: org.sheart.mpanzer.game.object.GameObject
*   Описание: 
*       Класс является родителем для всех игровых объектов - техники, снарядов,
*       авиации, строений и тому подобного. Количество игровых объектов в мире
*       конечно, поэтому для маловажных объектов рекомендуется использовать дру-
*       гой класс: org.sheart.mpanzer.game.particle.Particle.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer.game.object;

import java.util.Random;
import org.sheart.mpanzer.Main;
import org.sheart.mpanzer.game.*;

/**
 *
 * @author yew_mentzaki
 */
public class GameObject {
    protected Random random = Main.RANDOM;
    public float x, y, z; public int worldIndex = -1;
    public World world;
    
    public GameObject(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    /*
    *   Метод remove() удаляет объект из мира.
    */
    public void remove(){
        if(world!=null){
            world.remove(this);
        }
    }
    /*
    *   Метод tick() вызывается каждый цикл игрового времени.
    *   Логическая переменная ticks определяет, вызывать ли этот метод у объекта.
    */
    public boolean ticks = false;
    public void tick(){
        
    }
    /*
    *   Метод hover() вызывается если мышь игрока наведена на объект.
    *   Логическая переменная hovers определяет, вызывать ли этот метод у объекта.   
    */
    public boolean hovers = false;
    public void hover(){
        
    }
    /*
    *   Метод selected() вызывается если игрок выделил объект.
    *   Логическая переменная selectable определяет, вызывать ли этот метод у объекта.
    */
    public boolean selectable = false;
    public void selected(){
        
    }
    /*
    *   Метод render() вызывается если игрок выделил объект.
    *   Логическая переменная visible определяет, вызывать ли этот метод у объекта.
    */
    public boolean visible = false;
    public void render(){
        
    }
    
    public boolean interfaced = false;
    public void renderInteface(){
        
    }
}
