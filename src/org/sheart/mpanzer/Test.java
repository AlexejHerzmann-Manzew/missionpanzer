/*
*   Класс: org.sheart.mpanzer.Test
*   Описание:
*       Класс, использовавшийся в самом начале разработки, для тестирования про-
*       изводительности LWGJL при данных параметрах. На данный момент мусор уда-
*       лён, а Test сохранён как
*   ____________________________________________________________________________
*   Проект "Mission „Panzer“" лицензирован под BSD-3 License, ознакомиться с ко-
*   торой можно в корне проекта, она изложена в файле "license.txt".
*   Русскоязычная адаптация также находится в корне, в файле "license_rus.txt",
*   и использует кодировку UTF-8.
*   Разработчиком проекта является Yew_Mentzaki. Список всех контрибьюторов мож-
*   но увидеть в корне проекта, в файле "contributors.txt".
*/

package org.sheart.mpanzer;

/**
 *
 * @author yew_mentzaki
 */
public class Test {
    
    private static org.newdawn.slick.Graphics testGraphics = new org.newdawn.slick.Graphics();
    private static int testMark; private static boolean done;

    public static void testRender() {
        if(!done){
        testMark++;
        long beforeRender = System.currentTimeMillis();
        for (int i = 0; i < testMark; i++) {
            for (int j = 0; j < testMark; j++) {

                testGraphics.setColor(new org.newdawn.slick.Color(i * 20 + j * 40000));
                testGraphics.fillRect(10 + i, 10 + j, 1, 1);
            }
        }
        long afterRender = System.currentTimeMillis();
        if (afterRender - beforeRender > 100) {
            done = true;
            javax.swing.JOptionPane.showMessageDialog(null, "Test done, mark of engine: " + ((testMark*testMark)/100));
        }
        }
    }
}
