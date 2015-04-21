/*
 *   Класс: org.sheart.mpanzer.Main
 *   Описание:
 *       Этот статический является является ядром проекта, осуществляет подключе-
 *       ние библиотек, подгрузку текстур и создание окна. В классе также осу-
 *       ществляется создание и инициализация игрового мира.
 *   ____________________________________________________________________________
 *   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
 *   торой можно в корне проекта, она изложена в файле "license.txt".
 *   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
 *   и использует кодировку UTF-8.
 *   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
 *   но увидеть в корне проекта, в файле "contributors.txt".
 */
package org.sheart.mpanzer;

import org.sheart.mpanzer.game.Models;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.*;
import org.sheart.mpanzer.game.Textures;
import org.sheart.mpanzer.game.World;
import org.sparkle.fontrender.FontRender;

/**
 *
 * @author yew_mentzaki
 */
public class Main {

    public static Random RANDOM = new Random();
    public static FontRender fontRender;

    /*
     * Метод вызывается при инициализации дисплея и графики.
     */
    public static void displayInit() throws LWJGLException {
        fontRender = FontRender.getTextRender(Font.SANS_SERIF, 0, 15);
        Textures.load();
        Models.load();
    }
    public static World w = new World();

    /*
     * Метод предназначен для отрисовки мира.
     */
    public static void displayRender(Graphics g) throws LWJGLException {
        try {
            w.render(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
        glLoadIdentity();
    }

    /*
     * Метод предназначен для отрисовки интерфейса.
     */
    public static void displayInterfaceRender(Graphics g) throws LWJGLException {
    }

    public static void main(String[] args) {
        setUpNatives();
        setUpDisplay();
    }

    public static void setUpDisplay() {
        try {

            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.setTitle("Mission „Panzer“");
            Display.setResizable(true);
            Display.create();
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL11.GL_BLEND);
            glEnable(GL11.GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
            FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
            fogColours.put(new float[]{0.7f, 0.85f, 0.9f, 0});
            glClearColor(0.7f, 0.85f, 0.9f, 0);
            fogColours.flip();
            glFog(GL_FOG_COLOR, fogColours);
            glFogi(GL_FOG_MODE, GL_LINEAR);
            glHint(GL_FOG_HINT, GL_NICEST);
            glFogf(GL_FOG_START, 45f);
            glFogf(GL_FOG_END, 70f);
            glFogf(GL_FOG_DENSITY, 0.005f);
            glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
            try {
                Image i = new Image("res/textures/loader/logo.png");
                i.draw((Display.getWidth() - i.getWidth()) / 2, (Display.getHeight() - i.getHeight()) / 2);

            } catch (SlickException ex) {
                Logger.getLogger(Main.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            Display.update();
            displayInit();
        } catch (LWJGLException e) {
            Display.destroy();
            JOptionPane.showMessageDialog(null, "Error!\n" + e);
            System.exit(1);
        }
        Graphics g = new Graphics();
        int displayWidth = Display.getWidth(), displayHeight = Display.getHeight();
        try {
            while (!Display.isCloseRequested()) {
                long beforeRender = System.currentTimeMillis();
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                if (displayWidth != Display.getWidth() || displayHeight != Display.getHeight()) {
                    displayWidth = Display.getWidth();
                    displayHeight = Display.getHeight();
                    glViewport(0, 0, Display.getWidth(), Display.getHeight());
                }
                gluPerspective((float) 90, displayWidth / displayHeight, 0.001f, 1000);
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_FOG);
                glEnable(GL_CULL_FACE);
                displayRender(g);
                long afterRender = System.currentTimeMillis();
                int frames = ((int) (afterRender - beforeRender));
                int fps = frames == 0 ? 1000 : (1000 / frames);
                glMatrixMode(GL_PROJECTION);
                glOrtho(0, displayWidth, displayHeight, 0, 0, 1000);
                glMatrixMode(GL_MODELVIEW);
                glDisable(GL_DEPTH_TEST);
                glDisable(GL_FOG);
                glDisable(GL_CULL_FACE);
                displayInterfaceRender(g);
                fontRender.drawString("Frames per second: " + fps, 10, 10, Color.white);
                fontRender.drawString("Camera zoom: " + w.camera.zoom, 10, 30, Color.white);
                Display.update();
            }
        } catch (LWJGLException e) {
            Display.destroy();
            JOptionPane.showMessageDialog(null, "Error!\n" + e);
            System.exit(1);
        }
        Display.destroy();
    }

    public static void setUpNatives() {
        if (!new File("native").exists()) {

            JOptionPane.showMessageDialog(null, "Error!\nNative libraries not found!");
            System.exit(1);
        }
        try {

            System.setProperty("java.library.path", new File("native").getAbsolutePath());

            Field fieldSysPath = ClassLoader.class
                    .getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(
                    true);

            try {
                fieldSysPath.set(null, null);
            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            } catch (IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            }
        } catch (NoSuchFieldException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        }
    }

}
