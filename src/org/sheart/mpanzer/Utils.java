/*
*   Класс: org.sheart.mpanzer.Utils
*   Описание:
*       Класс содержит инструменты, предназначенные для повсеместного использо-
*       вания в коде данного проекта.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/
package org.sheart.mpanzer;

import java.awt.Point;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author yew_mentzaki
 */
public class Utils {

    private static FloatBuffer screenCoords = BufferUtils.createFloatBuffer(4);
    private static IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    private static FloatBuffer projection = BufferUtils.createFloatBuffer(16);

    public static int[] getScreenCoords(double x, double y, double z) {
        try {
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
            GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
            GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
            boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
            if (result) {
                return new int[]{(int) screenCoords.get(0), (int) screenCoords.get(1)};
            }
            return new int[]{0,0};
        } catch (Exception e) {
            return new int[]{0,0};
        }
    }

}
