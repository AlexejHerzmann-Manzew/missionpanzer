/*
*   Класс: org.sheart.mpanzer.game.Camera
*   Описание:
*       Класс предназначен для хранения и обработки камеры игрока. Класс хранит
*       координаты, угол камеры, а также увеличение.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer.game;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author yew_mentzaki
 */
public class Camera {

    public Vector3f location = new Vector3f(), rotation = new Vector3f();
    public float zoom;

    public void setProjection(World w) {
        //Сброс всех предыдущих смещений:
        glLoadIdentity();
        //Отдаление камеры на расстояние зума:
        glTranslatef(0, 0, zoom - 30);
        //Вращение камеры в трёх плоскостях.
        glRotatef(rotation.x, -1, 0, 0);
        glRotatef(rotation.y, 0, -1, 0);
        glRotatef(rotation.z, 0, 0, -1);
        //Поворот камеры на 90 градусов вдоль оси Z, чтобы при значении z=0 она
        //смотрела на восток, как и все другие объекты в игре:
        glRotatef(90, 0, 0, -1);
        //Включение мирового света:
        w.light.setUp();
        //Сдвиг камеры на её мировые координаты.
        glTranslatef(location.x, location.y, location.z);
    }
}
