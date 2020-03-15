package com.studios0110.CliffJumper.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.objects.Floor;
import com.studios0110.CliffJumper.objects.PlatformCollisionInfo;
import com.studios0110.CliffJumper.objects.Platforms;
import com.studios0110.CliffJumper.screens.GameScreen;

/*
Sam Merante: Player object, updates collisions
 */

public class Player implements  InputProcessor {
    private  TextureRegion currentFrame;
    private float stateTime,startCamPosY,startCamPosX,jumpStrength;
    private Animation walkBack, run,jump,land,floorCrash,wallCrash;
    private TextureRegion standing;
    public Vector2 position,movement;
    private Rectangle bounds;
    private boolean onGround,justJumped,collidedWall,collidedFloor,touchedDown,jump2,canGetPoint;
    public static float camPosY,camPosX,JUMP;
    public boolean isDead;
    public Player() {
        run = initAnimation("Player/running.png",15,2,0.016f);
        walkBack = initAnimation("Player/walkingBack.png",30,2,0.033f);
        standing = new TextureRegion((Texture)Splash.manager.get("Player/standing.png"));
        jump = initAnimation("Player/jumping.png",21,2,0.012f);
        land = initAnimation("Player/landing.png",10,8,0.013f);
        floorCrash = initAnimation("Player/crash2.png",10,6,0.016f);
        wallCrash = initAnimation("Player/crash4.png",26,2,0.016f);
        animateStanding();
        stateTime = 0.0f;
        startCamPosY = 350;
        startCamPosX = 300;
        position = new Vector2(300,1500);
        Player.camPosY = 0;
        Player.camPosX = 0;
        movement = new Vector2(0,0);
        onGround = false;
        collidedFloor = false;
        collidedWall = false;
        justJumped = false;
        canGetPoint = false;
        JUMP = 1700;
        jumpStrength = 0;
        touchedDown = false;
        isDead = false;
        jump2 = false;
        bounds = new Rectangle(position.x,position.y,currentFrame.getTexture().getWidth(),currentFrame.getTexture().getHeight());
    }

    public void draw(SpriteBatch batch)
    {
        animate();
        batch.draw(currentFrame,position.x-Player.camPosX,position.y-Player.camPosY);
    }
    public void drawMesh(ShapeRenderer shapes)
    {
        shapes.setColor(Color.WHITE);
        shapes.rect(bounds.x-Player.camPosX,bounds.y-Player.camPosY,bounds.width,bounds.height);
    }
    private void updateBounds(){
        bounds.set(position.x,position.y,currentFrame.getRegionWidth(),currentFrame.getRegionHeight());
    }
    private void updateCollisions(Platforms platforms, Floor floor){
        PlatformCollisionInfo collisionInfo = platforms.updateCollisions(this.bounds);
        if(collisionInfo.isCollided()){
            if(collisionInfo.isCollidedTop()){
                if(canGetPoint) {
                    canGetPoint = false;
                    GameScreen.scoreFont.increaseScore();
                    GameScreen.soundEffects.point();
                }
                this.position.y = collisionInfo.getTop()-1;
                this.movement.y = 0;
                this.onGround = true;
            }
            else if(collisionInfo.isCollidedLeft()){
                if(!(this.position.x == (collisionInfo.getLeft() - currentFrame.getRegionWidth()+30))) {
                    GameScreen.soundEffects.crash();
                    GameScreen.soundEffects.playDeath();
                    GameScreen.soundEffects.stopSounds();
                    this.position.x = collisionInfo.getLeft() - currentFrame.getRegionWidth() + 30;
                    this.movement.y = -100;
                    this.movement.x = 0;
                    if (!collidedWall) {
                        stateTime = 0;
                    }
                    collidedWall = true;
                }
            }
        }
        else if(position.y < (Splash.FLOOR-50)){
            GameScreen.soundEffects.playDeath();
            GameScreen.soundEffects.stopSounds();
            if(!collidedFloor){
                isDead = true;
                GameScreen.soundEffects.resetCrash();
                stateTime = 0;
            }
            GameScreen.soundEffects.crash();
            movement.y=0;
            position.y = Splash.FLOOR-50;
            this.movement.x =0;
            collidedWall = false;
            collidedFloor = true;
        }else{
            this.onGround = false;
        }
    }
    public void update(float deltaT,Platforms platforms,Floor floor){
        if(touchedDown){
            jumpStrength+=deltaT;
            if(!jump2 && !onGround && jumpStrength>0.15f){
                jump2();
            }
        }
        position.x+=movement.x*deltaT;
        position.y+=movement.y*deltaT;
        movement.y+=Splash.GRAVITY*deltaT;
        updateBounds();
        updateCollisions(platforms,floor);
        Player.camPosY =  (position.y - startCamPosY);
        Player.camPosX =  (position.x - startCamPosX);
        //System.out.println(Player.camPosY+" : "+  position.y+  " Draw@: "+(position.y-Player.camPosY));;
    }
    private void animate(){
        if(collidedWall){
            animateCrash(2);
        }
        else if(collidedFloor){
            animateCrash(1);
        }
        else if(movement.x == 0) {
            justJumped = false;
            animateStanding();
        }
        else if(movement.y==0 || onGround){
            justJumped = false;
            animateRunning();
        }
        else if(!onGround && justJumped && !jump.isAnimationFinished(stateTime)){
            animateJumping();
        }
        else{
            if(justJumped){
                stateTime = 0;
                justJumped = false;
            }
            animateLanding();
        }
    }
    private void animateStanding(){
        currentFrame = standing;
    }
    private void animateWalkingBack(){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkBack.getKeyFrame(stateTime,true);
    }

    private void animateRunning(){
        GameScreen.soundEffects.running();
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = run.getKeyFrame(stateTime,true);
    }

    private void animateJumping(){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = jump.getKeyFrame(stateTime,false);
    }
    private void animateLanding(){
        if(stateTime >= land.getAnimationDuration()){
            animateRunning();
        }
        else {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = land.getKeyFrame(stateTime, false);
        }
    }
    private void animateCrash(int crashType){
        if(crashType == 1) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = floorCrash.getKeyFrame(stateTime, false);
        }
        else if(crashType == 2) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = wallCrash.getKeyFrame(stateTime, false);
        }
    }
    private Animation initAnimation(String sheet, int cols, int rows,float framesPerSecond){
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
    private void jump1(){
        GameScreen.soundEffects.jump();
        GameScreen.soundEffects.stopRunning();
        onGround = false;
        justJumped = true;
        jump2 = false;
        movement.y = JUMP/1.5f;
        stateTime=0;
    }
    private void jump2(){
        GameScreen.soundEffects.swoosh();
        onGround = false;
        movement.y = JUMP/1.1f;
        jump2 = true;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(onGround && movement.x==0){
            movement.x = 650;
            GameScreen.soundEffects.playMusic();
        }
        else if(onGround){
            touchedDown = true;
            canGetPoint = true;
            jumpStrength = 0;
            jump1();
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchedDown = false;
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
