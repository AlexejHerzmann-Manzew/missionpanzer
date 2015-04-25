package org.sheart.mpanzer.game.object;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static org.sheart.mpanzer.Main.RANDOM;
import org.sheart.mpanzer.game.*;

/**
 *
 * @author yew_mentzaki
 */
public class Tank extends Unit {

    static Model model = new Model("tank.obj");
    static Model modelTurret = new Model("tank_turret.obj");
    
    public Tank(float x, float y, float z, float az, Player owner) {
        super(x, y, z, az, owner);
        ticks = true;
        visible = true;
        velocz = 1;
    }

    @Override
    public void tick() {
        move();
    }

    public void move() {

        {
            velocx += accelx;
            velocy += accely;
            velocz += accelz;
            x += velocx;
            y += velocy;
            z += velocz;
            velocx *= 0.95f;
            velocy *= 0.95f;
            if (z > world.terrain.getHeight(x, y) + 3) {
                velocz = -(z - world.terrain.getHeight(x, y)) / 500;
            } else if (z > world.terrain.getHeight(x, y)) {
                accelz = -0.005f;
            } else {
                velocz = -velocz / 3;
                accelz = 0;
                z = (z + world.terrain.getHeight(x + velocx * 2, y + velocy * 2)) / 2f;
                velocx = 0.1f;
            }
            if (z < world.terrain.getHeight(x, y) + 3) {
                anglex = (float) ((angley * 7 + atan2(world.terrain.getHeight(x, y + 2) - world.terrain.getHeight(x, y - 2), 5)) / 8);
                angley = (float) ((anglex * 7 + atan2(world.terrain.getHeight(x - 2, y) - world.terrain.getHeight(x + 2, y), 5) * 2) / 8);
                {
                    double dist = sqrt(pow(targetx - x, 2) + pow(targety - y, 2));
                    if (dist < 10) {
                        targetx = RANDOM.nextFloat()*50;
                        targety = RANDOM.nextFloat()*50;
                    }
                    double targetAngle = atan2(targety - y, targetx - x);
                    anglez = (float) targetAngle;
                    velocx = (float) (cos(targetAngle) * speed);
                    velocy = (float) (sin(targetAngle) * speed);
                }
            }
        }
    }

    @Override
    public void render() {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float anglex = this.anglex;
        float angley = this.angley;
        float anglez = this.anglez;
        glTranslatef(x, y, z);
        glRotated(angley / PI * 180, 0, 1, 0);
        glRotated(anglex / PI * 180, 1, 0, 0);
        glRotated(anglez / PI * 180, 0, 0, 1);
        renderBody();
        glRotated(anglez / PI * 180, 0, 0, -1);
        glRotated(anglex / PI * 180, -1, 0, 0);
        glRotated(angley / PI * 180, 0, -1, 0);
        glTranslatef(-x, -y, -z);
    }

    public void renderBody() {
        model.render(world.camera, false);
        glTranslatef(0, 0, 1f);
        modelTurret.render(world.camera, false);
        glTranslatef(0, 0, -1f);
    }

}
