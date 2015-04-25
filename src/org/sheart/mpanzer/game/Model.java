/*
*   Класс: org.sheart.mpanzer.game.Model
*   Описание:
*       Класс предназначен для парсинга, хранения и отрисовки моделей в формате
*       Wavefront OBJ.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/
package org.sheart.mpanzer.game;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.atan2;
import java.util.ArrayList;
import java.util.Scanner;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.sheart.mpanzer.game.Camera;

/**
 *
 * @author yew_mentzaki
 */
public class Model {

    private static Texture triangle, quad;
    private static Texture triangleSmall, quadSmall;
    
    public static void load() {
        //Подгрузка крупных и мелких текстур:
        triangle = Textures.texture("units/plate_triangle.png");
        quad = Textures.texture("units/plate_quad.png");
        triangleSmall = Textures.texture("units/plate_triangle_small.png");
        quadSmall = Textures.texture("units/plate_quad_small.png");
        //Отключение интерполяции:
        triangle.setTextureFilter(GL_NEAREST);
        quad.setTextureFilter(GL_NEAREST);
        triangleSmall.setTextureFilter(GL_NEAREST);
        quadSmall.setTextureFilter(GL_NEAREST);
    }

    private class Polygon {

        Vector3f[] v;

        public Polygon(Vector3f[] v) {
            this.v = v;
        }

        public void render(boolean quality) {
            //Отрисовка треугольного полигона:
            if (v.length == 3) {
                (quality?triangle:triangleSmall).bind();
                glBegin(GL_TRIANGLES);
                {
                    glTexCoord2d(0, 1);
                    glNormal3f(v[0].x, v[0].y, v[0].z);
                    glVertex3f(v[0].x, v[0].y, v[0].z);
                    glTexCoord2d(0.5, 0);
                    glNormal3f(v[1].x, v[1].y, v[1].z);
                    glVertex3f(v[1].x, v[1].y, v[1].z);
                    glTexCoord2d(1, 1);
                    glNormal3f(v[2].x, v[2].y, v[2].z);
                    glVertex3f(v[2].x, v[2].y, v[2].z);
                }
                glEnd();
            }
            //Отрисовка четырёхугольного полигона:
            if (v.length == 4) {
                (quality?quad:quadSmall).bind();
                glBegin(GL_QUADS);
                {
                    glTexCoord2d(0, 0);
                    glNormal3f(v[0].x, v[0].y, v[0].z);
                    glVertex3f(v[0].x, v[0].y, v[0].z);
                    glTexCoord2d(1, 0);
                    glNormal3f(v[1].x, v[1].y, v[1].z);
                    glVertex3f(v[1].x, v[1].y, v[1].z);
                    glTexCoord2d(1, 1);
                    glNormal3f(v[2].x, v[2].y, v[2].z);
                    glVertex3f(v[2].x, v[2].y, v[2].z);
                    glTexCoord2d(0, 1);
                    glNormal3f(v[3].x, v[3].y, v[3].z);
                    glVertex3f(v[3].x, v[3].y, v[3].z);
                }
                glEnd();
            }
        }
    }
    String name;

    private float f(String line) {
        return Float.parseFloat(line);
    }

    private int i(String line) {
        return Integer.parseInt(line);
    }
    private Polygon[] polygons;

    public Model(String name, File file) throws FileNotFoundException {
        this.name = name;
        ArrayList<Vector3f> v = new ArrayList<Vector3f>();
        ArrayList<Polygon> p = new ArrayList<Model.Polygon>();
        Scanner s = new Scanner(file);
        //Построчный парсинг файла в формате Wavefront OBJ:
        while (s.hasNextLine()) {
            String l = s.nextLine().trim();
            String a[] = l.split(" ");
            //Если строка начинается с v, записывает её как вершину с тремя ко-
            //ординатами:
            if (l.charAt(0) == 'v') {
                v.add(new Vector3f(f(a[1]), -f(a[3]), f(a[2])*1.5f));
            }
            //Если строка начинается с f, записывает её как полигон, добавив но-
            //мера всех вершин фигуры:
            if (l.charAt(0) == 'f') {
                Vector3f[] vs = new Vector3f[a.length - 1];
                for (int i = 0; i < a.length - 1; i++) {
                    vs[i] = v.get(i(a[i + 1]) - 1);
                }
                p.add(new Polygon(vs));
            }
        }
        //Перенос полигоны из списка в массив:
        polygons = new Polygon[p.size()];
        for (int i = 0; i < p.size(); i++) {
            polygons[i] = p.get(i);
        }
    }
    
    public Model(String name){
        this.name = name;
        this.polygons = null;
    }

    public void render(Camera camera, boolean highpoly) {
        //Последовательная отрисовка всех полигонов:
        if(polygons==null){
            polygons = Models.model(name).polygons;
        }
        for (Polygon p : polygons) {
            p.render(highpoly);
        }
    }
}
