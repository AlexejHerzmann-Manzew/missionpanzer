/*
 *   Класс: org.sheart.mpanzer.game.Light
 *   Описание:
 *       Класс предназначен для обработки мирового и локального освещения.
 *   ____________________________________________________________________________
 *   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
 *   торой можно в корне проекта, она изложена в файле "license.txt".
 *   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
 *   и использует кодировку UTF-8.
 *   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
 *   но увидеть в корне проекта, в файле "contributors.txt".
 */
package org.sheart.mpanzer.game;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import org.sheart.mpanzer.BufferTools;

/**
 *
 * @author yew_mentzaki
 */
public class Light {

    public final Vector3f location = new Vector3f();

    public Light(double x, double y, double z) {
        location.x = (float) x;
        location.y = (float) y;
        location.z = (float) z;
    }

    public void setUp() {
        //glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1f}));
        glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(new float[]{location.x, location.y, location.z, 1}));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }
    public void setDown() {
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glDisable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }
}
