package com.studios0110.CliffJumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.player.Player;
import java.util.Random;


/*
Sam Merante: List of platforms
 */

public class Platforms {
    private Platform[] platforms;
    private int lastPlatform,jumpDistance,jumpDownReset;
    Random r;
    public Platforms(){
        platforms = new Platform[4];
        platforms[0] = (new Platform("Objects/platform.png", new Vector2(200,300)));
        platforms[1] =(new Platform("Objects/platform.png", new Vector2(1300,300)));
        platforms[2] =(new Platform("Objects/platform.png", new Vector2(2400,300)));
        platforms[3] =(new Platform("Objects/platform.png", new Vector2(3500,300)));
        lastPlatform = 3;
        jumpDownReset = 4;
        jumpDistance = (int)(Player.JUMP/2.8f);
        r = new Random(System.currentTimeMillis());
    }
    public void draw(SpriteBatch batch){
        for(int i = 0; i < platforms.length; i++){
            platforms[i].draw(batch);
        }
    }
    public void drawLight(SpriteBatch batch){
        for(int i = 0; i < platforms.length; i++){
            platforms[i].drawLight(batch);
        }
    }
    public void drawMesh(ShapeRenderer shapes){
        for(int i = 0; i < platforms.length; i++){
            platforms[i].drawMesh(shapes);
        }
    }
    public PlatformCollisionInfo updateCollisions(Rectangle playerBounds){
        PlatformCollisionInfo collisionInfo = new PlatformCollisionInfo();
        for(int i = 0; i < platforms.length; i++){
            collisionInfo = platforms[i].checkCollision(playerBounds);
            if(collisionInfo.isCollided()){
                return collisionInfo;
            }
        }
        return  collisionInfo;
    }
    public void update(float deltaT){
        for(int i = 0; i < platforms.length; i++){
            platforms[i].update(0,0,deltaT);
            platformSpawn(i);
        }
    }
    private void platformSpawn(int i){
        if ((platforms[i].position.x-Player.camPosX) < -platforms[i].bounds.width) { //wrapPlatform
            float lastX = platforms[lastPlatform].position.x; //Get last posX
            float lastY = platforms[lastPlatform].position.y; //get last PosY
            float increment = 0;

            if(jumpDownReset<=0){//has option to move platform up or down
                increment = r.nextFloat()*(jumpDistance*2) - jumpDistance;
                if(increment<0){ //if its moved down then reset jumpDown possibility
                    jumpDownReset = Math.round(r.nextFloat()*10);//reset num for jump down possibility
                }else
                {
                    jumpDownReset = 0; //if platform is not moved down, jumpdown is still possible
                }
            }else{
                increment = r.nextFloat()*jumpDistance;
                jumpDownReset--;
            }

            platforms[i].position.x = lastX + platforms[i].bounds.width*2.2f;
            platforms[i].position.y = (int)(lastY + increment);
            lastPlatform = i;
        }
    }
}
