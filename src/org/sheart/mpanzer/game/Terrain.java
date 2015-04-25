/*
*   Класс: org.sheart.mpanzer.game.Terrain
*   Описание:
*       Класс предназначен для генерации, загрузки, хранения, обработки и отри-
*       совки игрового ландшафта.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer.game;

import java.awt.Point;
import static java.lang.Math.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import static org.sheart.mpanzer.Main.RANDOM;
import org.sheart.mpanzer.Utils;

/**
 *
 * @author yew_mentzaki
 */
public final class Terrain {

    public float[][] height = new float[1024][1024];
    public byte[][] type = new byte[1024][1024];
    public Point pointer = new Point();
    private Image grass, grassBig, water, target;

    public Terrain() {
        //Генерация ёжикоподобного ландшафта:
        for (int x = 0; x < 1024; x++) {
            for (int y = 0; y < 1024; y++) {
                //Высота вершины - случайное число от 0 до 19:
                height[x][y] = RANDOM.nextInt(20);
                //С вероятностью 1:100, высота вершины - число от 0 до 99:
                if (RANDOM.nextInt(100) == 0) {
                    height[x][y] = RANDOM.nextInt(100);
                }
                //С вероятностью 1:100, высота вершины - число от 0 до -99:
                if (RANDOM.nextInt(100) == 0) {
                    height[x][y] = -RANDOM.nextInt(100);
                }
                //С вероятностью 1:500, высота вершины и десяти вершин рядом -
                //- случайное число от 0 до 99, причём примерно схожее:
                if (RANDOM.nextInt(500) == 0) {
                    height[x][y] = RANDOM.nextInt(100);
                    for (int i = 0; i < 10; i++) {
                        setHeight(x + RANDOM.nextInt(9) - 4, y + RANDOM.nextInt(9) - 4, height[x][y] - RANDOM.nextInt(50));
                    }
                }

            }
        }
        //4 цикла сглаживания ландшафта по реальной высоте:
        for (int time = 0; time < 4; time++) {
            for (int x = 0; x < 1025; x++) {
                for (int y = 0; y < 1025; y++) {
                    setHeight(x, y,
                            (getRealHeight(x - 1, y)
                            + getRealHeight(x + 1, y)
                            + getRealHeight(x, y - 1)
                            + getRealHeight(x, y + 1)
                            + getRealHeight(x - 1, y + 1)
                            + getRealHeight(x - 1, y - 1)
                            + getRealHeight(x + 1, y - 1)
                            + getRealHeight(x + 1, y + 1)
                            + getRealHeight(x, y)) / 9f
                    );
                }
            }
        }
        //Цикл сглаживания ландшафта по видимой высоте (создаёт ровную местность):
        for (int x = 0; x < 1025; x++) {
            for (int y = 0; y < 1025; y++) {
                setHeight(x, y,
                        (getHeight(x - 1, y)
                        + getHeight(x + 1, y)
                        + getHeight(x, y - 1)
                        + getHeight(x, y + 1)
                        + getHeight(x - 1, y + 1)
                        + getHeight(x - 1, y - 1)
                        + getHeight(x + 1, y - 1)
                        + getHeight(x + 1, y + 1)
                        + getHeight(x, y)) / 9f
                );
            }
        }

    }

    public void setHeight(int x, int y, float value) {
        while (x < 0) {
            x += 1024;
        }
        while (x >= 1024) {
            x -= 1024;
        }
        while (y < 0) {
            y += 1024;
        }
        while (y >= 1024) {
            y -= 1024;
        }

        height[x][y] = value;
    }

    public float getHeight(int x, int y) {
        while (x < 0) {
            x += 1024;
        }
        while (x >= 1024) {
            x -= 1024;
        }
        while (y < 0) {
            y += 1024;
        }
        while (y >= 1024) {
            y -= 1024;
        }
        if (height[x][y] > 9) {
            return height[x][y];
        }
        return 9;
    }

    public float getRealHeight(int x, int y) {
        while (x < 0) {
            x += 1024;
        }
        while (x >= 1024) {
            x -= 1024;
        }
        while (y < 0) {
            y += 1024;
        }
        while (y >= 1024) {
            y -= 1024;
        }

        return height[x][y];
    }

    public float getVertexColor(int x, int y) {
        float p1 = (getHeight(x, y) - getHeight(x + 1, y)) / 5;
        float p2 = (getHeight(x, y) - getHeight(x + 1, y + 1)) / 5;
        float p3 = (getHeight(x, y) - getHeight(x, y + 1)) / 5;
        return (max(min(p1 + p2 + p3, 20), -20) + 0.5f) * 1.2f;
    }

    public void render(Graphics g, Camera camera) {
        //Отключение глобального света:
        glDisable(GL_LIGHTING);
        //Получение изображений:
        grass = Textures.image("terrain/grass.png");
        grassBig = Textures.image("terrain/grass_big.png");
        target = Textures.image("terrain/target.png");
        water = Textures.image("terrain/water.png");
        //Отключение интерполяции:
        grass.setFilter(GL_NEAREST);
        water.setFilter(GL_NEAREST);
        grassBig.setFilter(GL_NEAREST);
        //Подготовка к поиску цели:
        int px = 0, py = 0, d = Integer.MAX_VALUE;
        //Последовательная обработка сетки высот вокруг камеры:
        for (int x = (int) (-(75 - camera.zoom) - camera.location.x); x <= (75 - camera.zoom) - camera.location.x; x++) {
            for (int y = (int) (-(75 - camera.zoom) - camera.location.y); y <= (75 - camera.zoom) - camera.location.y; y++) {
                //Если у камеры сильный зум, отрисовывает большие тексту-
                //ры, если нет - обычные.
                if (camera.zoom < 17) {
                    grass.bind();
                } else {
                    grassBig.bind();
                }
                //Получение координат центра блока на экране, если они ближе к
                //курсору чем координаты предыдущих блоков, берутся эти коорди-
                //наты. 
                int[] coords = Utils.getScreenCoords(x + 0.5, y + 0.5, getHeight(x, y));
                int d2 = (abs(Mouse.getX() - coords[0]) + abs(Mouse.getY() - coords[1]));
                if (d > d2) {
                    d = d2;
                    px = x;
                    py = y;
                }
                //Отрисовка полигона:
                float vc;
                glBegin(GL_POLYGON);
                {
                    //Получение цвета:
                    vc = getVertexColor(x, y);
                    //Передача цвета с небольшим синим отливом (эффект отраженно-
                    //го небом света):
                    glColor3f(vc, vc, vc + 0.2f);
                    //Указание координат текстуры:
                    glTexCoord2d(0, 0);
                    //Создание вершины в данных координатах и с данной высотой:
                    glVertex3d(x, y, getHeight(x, y));
                    //Последующие итерации аналогичны первой.
                    vc = getVertexColor(x + 1, y);
                    glColor3f(vc, vc, vc + 0.2f);
                    glTexCoord2d(1, 0);
                    glVertex3d(x + 1, y, getHeight(x + 1, y));
                    vc = getVertexColor(x + 1, y + 1);
                    glColor3f(vc, vc, vc + 0.2f);
                    glTexCoord2d(1, 1);
                    glVertex3d(x + 1, y + 1, getHeight(x + 1, y + 1));
                    vc = getVertexColor(x, y + 1);
                    glTexCoord2d(0, 1);
                    glColor3f(vc, vc, vc + 0.2f);
                    glVertex3d(x, y + 1, getHeight(x, y + 1));
                }
                glEnd();

            }
        }
        //Отрисовка цели под мышкой:
        target.bind();
        glColor3f(0f, 1f, 0f);
        glBegin(GL_POLYGON);
        {
            glTexCoord2d(0, 0);
            glVertex3d(px, py, getHeight(px, py) + 0.01);
            glTexCoord2d(1, 0);
            glVertex3d(px + 1, py, getHeight(px + 1, py) + 0.01);
            glTexCoord2d(1, 1);
            glVertex3d(px + 1, py + 1, getHeight(px + 1, py + 1) + 0.01);
            glTexCoord2d(0, 1);
            glVertex3d(px, py + 1, getHeight(px, py + 1) + 0.01);
        }
        glEnd();
        //Включение глобального света:
        glEnable(GL_LIGHTING);
    }

    public float getHeight(double x, double y) {
        return getHeight((int) x, (int) y);
    }
}
