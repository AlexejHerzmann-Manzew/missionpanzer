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

/**
 *
 * @author yew_mentzaki
 */
public class Light {
    public final Vector3f location = new Vector3f();

    public Light(double x, double y, double z) {
        location.x = (float)x;
        location.y = (float)y;
        location.z = (float)z;
    }
    
}
