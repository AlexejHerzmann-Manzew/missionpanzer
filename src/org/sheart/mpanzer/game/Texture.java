package org.sheart.mpanzer.game;

/**
 *
 * @author yew_mentzaki
 */
public class Texture {
    String address;
    org.newdawn.slick.opengl.Texture texture;

    public Texture(String name, org.newdawn.slick.opengl.Texture texture) {
        this.address = name;
    }
    
    private org.newdawn.slick.opengl.Texture t(){
        if(texture==null){
            texture = Textures.texture(address);
            address = null;
        }
        return texture;
    }
    
    public void bind(){
        t().bind();
    }
    
    public float getWidth(){
        return t().getWidth();
    }
    
    public float getHeight(){
        return t().getHeight();
    }
    
}
