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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.*;
import javax.swing.Timer;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.*;
import static org.sheart.mpanzer.Main.RANDOM;
import org.sheart.mpanzer.game.object.GameObject;
import org.sheart.mpanzer.game.object.Tank;

/**
 *
 * @author yew_mentzaki
 */
public final class World {

    public Camera camera = new Camera();
    public Terrain terrain = new Terrain();
    public int player = 1;
    public Player[] players = new Player[8 + 1];
    public GameObject[] gameObjects = new GameObject[128];
    private double wheelx, wheely;
    private boolean init = true;
    Light light = new Light(20, 20, 100);
    BillboardSprite bs;

    public World() {
        players[0] = new Player("None", Faction.SIBRILIANS, Color.white);
        players[1] = new Player("Player 1", Faction.SIBRILIANS, Color.red);
        players[2] = new Player("Player 2", Faction.YOOHESIANS, Color.blue);
        players[3] = new Player("Player 3", Faction.SIBRILIANS, Color.orange);
        players[4] = new Player("Player 4", Faction.YOOHESIANS, Color.green);
        players[5] = new Player("Player 5", Faction.SIBRILIANS, Color.magenta);
        players[6] = new Player("Player 6", Faction.YOOHESIANS, Color.cyan);
        players[7] = new Player("Player 7", Faction.SIBRILIANS, Color.pink);
        players[8] = new Player("Player 8", Faction.YOOHESIANS, Color.yellow);
        camera.zoom = 5;
        camera.rotation.x = 25;
        camera.rotation.z = 45;
        for (int i = 0; i < 20; i++) {
            add(new Tank(RANDOM.nextFloat()*20, RANDOM.nextFloat()*20, RANDOM.nextFloat()*25, 0, players[0]));
        }
        
    }

    public void initGraphics() {
        init = false;
    }

    public void add(GameObject go) {
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] == null) {
                gameObjects[i] = go;
                go.world = this;
                go.worldIndex = i;
                return;
            }
        }
    }

    public void remove(GameObject go) {
        if (go.worldIndex == -1 || go.world == null) {
            return;
        }
        gameObjects[go.worldIndex] = null;
        go.worldIndex = -1;
        go.world = null;
    }

    public GameObject[] objectsForRender() {
        GameObject[] o = new GameObject[gameObjects.length];
        int[] in = new int[o.length];
        for (int index = 0; index < o.length; index++) {
            o[index] = gameObjects[index];
            if (o[index] != null) {
                in[index] = (int) sqrt(pow(o[index].x - camera.location.x, 2) + pow(camera.location.y - o[index].y, 2));
            }
        }
        for (int i = 0; i < o.length - 1; i++) {
            for (int j = 0; j < o.length - i - 1; j++) {
                if (in[j] < in[j + 1]) {
                    int inj = in[j];
                    in[j] = in[j + 1];
                    in[j + 1] = inj;
                    GameObject oj = o[j];
                    o[j] = o[j + 1];
                    o[j + 1] = oj;
                }
            }
        }
        return o;
    }

    public Timer timer = new Timer(10, new ActionListener() {

        public void actionPerformed(ActionEvent ae) {
            tick();
        }
    });

    public void tick() {
        for (GameObject go : gameObjects) {
            if (go != null && go.ticks) {
                go.tick();
            }
        }
    }

    public void render(Graphics g) {
        if (init) {
            initGraphics();
        }
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
        //Передача камере расположения, равного высоте тайла, над которым она на-
        //ходится:
        camera.location.z = -terrain.getHeight(camera.location.x, camera.location.y);
        //Применение проекций и смещений камеры:
        camera.setProjection(this);
        //Отрисовка ландшафта:
        terrain.render(g, camera);
        //Отрисовка игровых объектов:
        for (GameObject go : gameObjects) {
            if (go != null) {
                if (go.visible) {
                    go.render();
                }
            } else {
                break;
            }
        }
    }
    int a = 0;
}
