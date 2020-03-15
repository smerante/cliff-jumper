package com.studios0110.CliffJumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.player.Player;

import java.awt.Shape;

/**
 * Created by Sam Merante on 2017-09-17.
 */

public class Background {
    private Vector2 positions[];
    private Texture bg1, bg2;
    private int lastBG;
    private float bgDelaySpeedX,bgDelaySpeedY;
    public Background()
    {
        bg1 = Splash.manager.get("Objects/groundBG.png");
        bg2 = Splash.manager.get("Objects/skyBG.png");
        positions = new Vector2[3];
        positions[0]= new Vector2(0,0);
        positions[1] = new Vector2(bg1.getWidth(),0);
        positions[2] = new Vector2(bg1.getWidth()*2,0);
        lastBG = 2;
        bgDelaySpeedX = 8.0f;
        bgDelaySpeedY = 20.0f;
    }
    public void draw(SpriteBatch batch){
        for(int i =0; i < positions.length; i++){
            batch.draw(bg1,positions[i].x - (Player.camPosX/bgDelaySpeedX),positions[i].y - (Player.camPosY/bgDelaySpeedY));
            batch.draw(bg2,positions[i].x - (Player.camPosX/bgDelaySpeedX),(positions[i].y - (Player.camPosY/bgDelaySpeedY))+bg1.getHeight());
        }
        //System.out.println("BG 1 pos: " +(positions[0].x- Player.camPosX));
    }
    public void update(){
        for(int i =0; i < positions.length; i++){
           if((positions[i].x- (Player.camPosX/bgDelaySpeedX))<-bg1.getWidth()){
            positions[i].x = positions[lastBG].x+bg1.getWidth();
            lastBG = i;
           }
        }
    }
}
