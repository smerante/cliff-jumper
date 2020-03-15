package com.studios0110.CliffJumper.objects;

import com.badlogic.gdx.math.Rectangle;
/*
Sam Merante: Platform collision info when play collides with given platform
 */
public class PlatformCollisionInfo {
    private boolean collidedLeft,collidedTop,collided;
    private float left,top;

    PlatformCollisionInfo(){
        setCollidedLeft(false);
        setCollidedLeft(false);
        setCollided(false);
        setLeft(0);
        setTop(0);
    }
    boolean collided(Rectangle bounds1, Rectangle bounds2){
        return bounds1.overlaps(bounds2) || bounds1.contains(bounds2);
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public boolean isCollided() {
        return collided;
    }

    public boolean isCollidedTop() {
        return collidedTop;
    }

    public boolean isCollidedLeft() {
        return collidedLeft;
    }

    void reset(){
        setCollided(false);
        setCollidedLeft(false);
        setCollidedTop(false);
        setLeft(0);
        setTop(0);
    }

    void setLeft(float left) {
        this.left = left;
    }

    void setTop(float top) {
        this.top = top;
    }
    void setCollided(boolean collided) {
        this.collided = collided;
    }

    void setCollidedTop(boolean collidedTop) {
        this.collidedTop = collidedTop;
    }

    void setCollidedLeft(boolean collidedLeft) {
        this.collidedLeft = collidedLeft;
    }



}
