/*
*   Класс: org.sheart.mpanzer.game.Animation
*   Описание:
*       Класс предназначен для обработки анимаций с помощью Slick Util и LWJGL.  
*       Класс разработан по просьбе Whizzpered и изначально был предназначен для
*       проекта "That's my Dungeon!", расположенного по ссылке ниже:
*       https://github.com/Whizzpered/That-s-My-Dungeon
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer.game;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
class Animation{
    private Image[] images(ArrayList<Textures.Tex> t){
        Textures.Tex[] tex = new Textures.Tex[t.size()];
        for (int i = 0; i < t.size(); i++) {
            tex[i] = t.get(i);
        }
        Image[] i = new Image[tex.length];
        for (int j = 0; j < i.length; j++) {
            i[j] = tex[j].image;
        }
        return i;
    }
    
    private Texture[] textures(ArrayList<Textures.Tex> t){
        Textures.Tex[] tex = new Textures.Tex[t.size()];
        for (int i = 0; i < t.size(); i++) {
            tex[i] = t.get(i);
        }
        Texture[] i = new Texture[tex.length];
        for (int j = 0; j < i.length; j++) {
            i[j] = tex[j].texture;
        }
        return i;
    }
    
    Animation(String name, ArrayList<Textures.Tex> tex) {
        this.name = name;
        images = images(tex);
        textures = textures(tex);
        slickAnimation = new org.newdawn.slick.Animation(images, 10);
    }
    
    public final String name;
    public final org.newdawn.slick.Animation slickAnimation;
    public final Image[] images;
    public final Texture[] textures;
    
}
