package com.studios0110.CliffJumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.player.Player;
/*
Sam Merante: Platform object
 */

class Platform {
    private Texture platformImage,platformLight;
    Vector2 position;
    Rectangle bounds;
    private float stateTime;
    private PlatformCollisionInfo collisionInfo;
    Platform(String platformImage, Vector2 position) {
        this.platformImage = Splash.manager.get(platformImage);
        this.position = position;
        this.bounds = new Rectangle(this.position.x,this.position.y,this.platformImage.getWidth(),this.platformImage.getHeight()-30);
        platformLight = Splash.manager.get("Objects/light.png");
        stateTime = 0;
        collisionInfo = new PlatformCollisionInfo();
    }
    void draw(SpriteBatch batch)
    {
        stateTime+= Gdx.graphics.getDeltaTime();
        batch.draw(platformImage,position.x-Player.camPosX,position.y-Player.camPosY);
    }
    void drawLight(SpriteBatch batch){
        batch.draw(platformLight,position.x-Player.camPosX,(position.y-Player.camPosY)+bounds.height-40);
    }
    void drawMesh(ShapeRenderer shapes){
        shapes.setColor(Color.WHITE);
        shapes.rect(bounds.x-Player.camPosX,bounds.y-Player.camPosY,bounds.width,bounds.height);
    }
    private Animation initAnimation(String sheet, int cols, int rows, float framesPerSecond){
        Texture spriteSheet = Splash.manager.get(sheet);
        TextureRegion[][] temp = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/cols, spriteSheet.getHeight()/rows);
        TextureRegion[] tempAnim = new TextureRegion[cols*rows];
        int index = 0;
        for(int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j++) {
                tempAnim[index++] = temp[i][j];
            }
        }
        return new Animation(framesPerSecond,tempAnim);
    }
    PlatformCollisionInfo checkCollision(Rectangle bounds){
        float pBottom = bounds.y + 30;//height of players knees

        float thisLeft = this.bounds.x;
        float thisTop = this.bounds.y + this.bounds.getHeight();
        if(collisionInfo.collided(bounds, this.bounds)){
            collisionInfo.setCollided(true);
            collisionInfo.setLeft(thisLeft);
            collisionInfo.setTop(thisTop);
            if(pBottom < thisTop && bounds.x < (this.bounds.x + bounds.getWidth()/2)){
                collisionInfo.setCollidedLeft(true);
            }else{
                collisionInfo.setCollidedTop(true);
            }
        }else{
            collisionInfo.reset();
        }
        return collisionInfo;
    }

    void update(float dx, float dy,float deltaT){
        position.x+=dx*deltaT;
        position.y+=dy*deltaT;
        this.updateBounds();
    }
    private void updateBounds(){
        this.bounds.setPosition(this.position);
    }
}
