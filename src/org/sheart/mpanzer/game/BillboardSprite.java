package org.sheart.mpanzer.game;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
public class BillboardSprite {

    private Texture[] textures;
    public float size = 5;

    public BillboardSprite(Texture texture) {
        this.textures = new Texture[]{texture};
    }

    public BillboardSprite(Texture[] textures) {
        this.textures = textures;
    }

    public BillboardSprite(Animation a) {
        this.textures = a.textures;
    }

    public void render(Graphics g, Camera c) {
        render(g, c, 0);
    }
    
    public void render(Graphics g, Camera c, int frame) {
        glRotatef(c.rotation.z + 90, 0, 0, 1);
        glRotatef(c.rotation.y, 0, 1, 0);
        glRotatef(c.rotation.x, 1, 0, 0);
        textures[frame].bind();
        glDisable(GL_LIGHTING);
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex2d(-size / 2, -size / 2);
            glTexCoord2d(textures[frame].getWidth(), 0);
            glVertex2d(+size / 2, -size / 2);
            glTexCoord2d(textures[frame].getWidth(), textures[frame].getHeight());
            glVertex2d(+size / 2, +size / 2);
            glTexCoord2d(0, textures[frame].getHeight());
            glVertex2d(-size / 2, +size / 2);
        }
        glEnd();
        glEnable(GL_LIGHTING);
        glRotatef(c.rotation.x, -1, 0, 0);
        glRotatef(c.rotation.y, 0, -1, 0);
        glRotatef(c.rotation.z + 90, 0, 0, -1);
    }
    
}
