/*
 *   Класс: org.sheart.mpanzer.game.World
 *   Описание:
 *       Класс объединяет игровые объекты, частицы и ландшафт, осуществляет взаи-
 *       модействие игрока с игровым миром и различных элементов мира, указанных
 *       выше, друг с другом.
 *   ____________________________________________________________________________
 *   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
 *   торой можно в корне проекта, она изложена в файле "license.txt".
 *   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
 *   и использует кодировку UTF-8.
 *   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
 *   но увидеть в корне проекта, в файле "contributors.txt".
 */
package org.sheart.mpanzer.game;

import static java.lang.Math.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.*;

/**
 *
 * @author yew_mentzaki
 */
public class World {

    public Camera camera = new Camera();
    public Terrain terrain = new Terrain();
    private double wheelx, wheely;

    public World() {
        camera.zoom = 5;

        camera.rotation.x = 25;
        camera.rotation.z = 45;
    }

    public void render(Graphics g) {
        //Зум камеры и изменение угла наклона с помощью поворота колёсика:
        camera.zoom += Mouse.getDWheel() / 120;
        if (camera.zoom > 25) {
            camera.zoom = 25;
        }
        if (camera.zoom < 5) {
            camera.zoom = 5;
        }
        camera.rotation.x = camera.zoom * 2;
        //Поворот камеры относительно оси Z с помощью клавиш E и Q:
        if (isKeyDown(KEY_E)) {
            camera.rotation.z += 1;
        }
        if (isKeyDown(KEY_Q)) {
            camera.rotation.z -= 1;
        }
        //Поворот камеры относительно оси Z с помощью правой кнопки мыши:
        if (Mouse.isButtonDown(1)) {
            if (Mouse.getY() > Display.getHeight() / 2) {
                camera.rotation.z += Mouse.getDX() / 5;
            } else {
                camera.rotation.z -= Mouse.getDX() / 5;
            }
        }
        //Перемещение камеры с помощью клавиш WASD или клавиш со стрелочками:
        if (isKeyDown(KEY_W) || isKeyDown(KEY_UP)) {
            camera.location.x += cos(camera.rotation.z * PI / 180);
            camera.location.y += sin(camera.rotation.z * PI / 180);
        }
        if (isKeyDown(KEY_S) || isKeyDown(KEY_DOWN)) {
            camera.location.x -= cos(camera.rotation.z * PI / 180);
            camera.location.y -= sin(camera.rotation.z * PI / 180);
        }
        if (isKeyDown(KEY_A) || isKeyDown(KEY_LEFT)) {
            camera.location.x += cos((camera.rotation.z + 90) * PI / 180);
            camera.location.y += sin((camera.rotation.z + 90) * PI / 180);
        }
        if (isKeyDown(KEY_D) || isKeyDown(KEY_RIGHT)) {
            camera.location.x -= cos((camera.rotation.z + 90) * PI / 180);
            camera.location.y -= sin((camera.rotation.z + 90) * PI / 180);
        }
        //Перемещение камеры с помощью зажатого колёсика:
        if (Mouse.isButtonDown(2)) {
            wheelx -= Mouse.getDX();
            wheely += Mouse.getDY();
            camera.location.x
                    += cos(camera.rotation.z * PI / 180) * wheely / (10 * camera.zoom)
                    + cos((camera.rotation.z * PI + 90) / 180) * wheelx / (20 * camera.zoom);
            camera.location.y
                    += sin(camera.rotation.z * PI / 180) * wheely / (10 * camera.zoom)
                    + sin((camera.rotation.z * PI + 90) / 180) * wheelx / (20 * camera.zoom);
        } else {
            wheelx = 0;
            wheely = 0;
        }
        
        camera.location.z = -terrain.getHeight(camera.location.x, camera.location.y);
        camera.setProjection();
        terrain.render(g, camera);
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                glTranslated(i * 5, j * 5, terrain.getHeight(i * 5, j * 5));
                Models.model("tank_turret.obj").render(camera);
                glTranslated(-i * 5, -j * 5, -terrain.getHeight(i * 5, j * 5));
            }
        }
    }
}
