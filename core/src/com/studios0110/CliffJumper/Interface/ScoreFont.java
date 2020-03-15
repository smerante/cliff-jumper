package com.studios0110.CliffJumper.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.studios0110.CliffJumper.Splash.Splash;

/**
 * Created by Sam Merante on 2017-09-24.
 */

public class ScoreFont {
    public String score;
    private Texture scoreTexture;
    private TextureRegion[][] numbers;
    private float width;
    public float xPos,yPos;
    public ScoreFont(float xPos, float yPos){
        scoreTexture = Splash.manager.get("Fonts/Score.png");
        numbers = TextureRegion.split(scoreTexture,scoreTexture.getWidth()/10,scoreTexture.getHeight()/1);
        setScore("0");
        width = scoreTexture.getWidth()/10;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public void setPos(float x, float y){
        this.xPos = x;
        this.yPos = y;
    }
    public void draw(SpriteBatch batch){
        String[] tempScoreToNums = score.split("");
        String[] scoreToNums;
        int count = 0;
        for(int i=0;i < tempScoreToNums.length; i++){
            if(!tempScoreToNums[i].equals("")){
                count++;
            }
        }
        scoreToNums = new String[count];
        count = 0;
        for(int i=0;i < tempScoreToNums.length; i++){
            if(!tempScoreToNums[i].equals("")){
                scoreToNums[count] = tempScoreToNums[i];
                count++;
            }
        }
        for(int i = 0; i < scoreToNums.length; i++){
            int num = Integer.parseInt(scoreToNums[i]);
            batch.draw(numbers[0][num],(xPos+(width*i)) - (int)((width*scoreToNums.length)/2),yPos);
        }
    }

    public void setScore(String score) {
        this.score = score;
    }
    public void increaseScore(){
        int scoreI = Integer.parseInt(score);
        scoreI++;
        score = ""+scoreI;

    }

}
