/*
*   Класс: org.sheart.mpanzer.game.Models
*   Описание:
*       Класс предназначен для загрузки и хранения моделей проекта.
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer.game;

import org.sheart.mpanzer.game.Model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author yew_mentzaki
 */
public class Models {

    private static Model[] models;

    public static void load() {
        //Подргузка текстур модели:
        Model.load();
        ArrayList<Model> modelList = new ArrayList<Model>();
        //Последовательная обработка res/models/ с рекурсивным спуском в директо-
        //рии, которые в ней находятся и добавляением моделей в список:
        for (File f : new File("res/models").listFiles()) {
            if (f.isDirectory()) {
                modelList.addAll(load(f.getName(), f));
            } else {
                try {
                    modelList.add(new Model(f.getName(), f));
                } catch (FileNotFoundException ex) {
                   
                }
            }
        }
        //Перенос моделей из списка в массив:
        models = new Model[modelList.size()];
        for (int i = 0; i < models.length; i++) {
            models[i] = modelList.get(i);
        }
    }

    //Метод, возвращающий модель по пути из res/models/:
    public static Model model(String name) {
        for (Model a : models) {
            if (a.name.equals(name)) {
                return a;
            }
        }
        return null;
    }

    private static ArrayList<Model> load(String names, File folder) {
        ArrayList<Model> models = new ArrayList<Model>();
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                models.addAll(load(names + "/" + f.getName(), f));
            } else {
                try {
                    models.add(new Model(names + "/" + f.getName(), f));
                } catch (FileNotFoundException ex) {
                    
                }
            }
        }
        return models;
    }
}
