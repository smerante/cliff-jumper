package com.studios0110.CliffJumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.player.Player;

import java.awt.Shape;
/*
Sam Merante: Platform object
 */

public class Floor {
    private Texture platformImage;
    Vector2 positions[];
    Rectangle bounds[];
    int lastFloor;
    public float y;
    Color underWorldColor;
    float lastPosX;
    public Floor(String groundImage, Vector2 position) {
        positions = new Vector2[4];
        bounds = new Rectangle[4];
        this.platformImage = Splash.manager.get(groundImage);
        positions[0] = position;
        y = Splash.FLOOR;
        bounds[0] = new Rectangle(this.positions[0].x,this.positions[0].y,this.platformImage.getWidth(),y);
        positions[1] = new Vector2(positions[0].x + bounds[0].width-1,positions[0].y);
        this.bounds[1] = new Rectangle(this.positions[1].x,this.positions[1].y,this.platformImage.getWidth(),y);
        positions[2] = new Vector2(positions[1].x + bounds[1].width-1,positions[1].y);
        this.bounds[2] = new Rectangle(this.positions[2].x,this.positions[2].y,this.platformImage.getWidth(),y);
        positions[3] = new Vector2(positions[2].x + bounds[2].width-1,positions[2].y);
        this.bounds[3] = new Rectangle(this.positions[3].x,this.positions[3].y,this.platformImage.getWidth(),y);
        lastFloor = 3;
        underWorldColor = new Color(126.0f/255.0f,82.0f/255.0f,72.0f/255.0f,1);
    }
    public void draw(SpriteBatch batch)
    {
        for(int i =0; i <positions.length;i++){
            batch.draw(platformImage,positions[i].x-Player.camPosX,positions[i].y-Player.camPosY);
        }
    }
    public void drawUNDERWORLDlol(ShapeRenderer shapes){
        shapes.set(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(underWorldColor);
        shapes.rect(0,-1000-Player.camPosY,1500,1000);
    }
    public void drawMesh(ShapeRenderer shapes){
        shapes.setColor(Color.WHITE);
        for(int i =0; i <positions.length;i++){
            shapes.rect(bounds[i].x-Player.camPosX,bounds[i].y-Player.camPosY,bounds[i].width,bounds[i].height);
        }
    }

    public boolean checkCollision(Rectangle bounds){
        boolean collided = false;
        for(int i =0; i <positions.length;i++){
            collided = bounds.overlaps(this.bounds[i]);
            if(collided){
                return true;
            }
        }
        return collided;
    }

    public  void update(){
        for(int i =0; i <positions.length;i++) {
            this.updateBounds(i);
            floorWrapAround(i);
        }
    }
    private void floorWrapAround(int i){
        if ((positions[i].x-Player.camPosX) < -bounds[i].width*2) { //wrapPlatform
            float lastX = positions[lastFloor].x;
            float lastY = positions[lastFloor].y;
            positions[i].x = lastX + bounds[i].width-1;
            positions[i].y = lastY;
            lastFloor = i;
        }
    }
    private void updateBounds(int i ){
        this.bounds[i].setPosition(this.positions[i]);
    }
}
