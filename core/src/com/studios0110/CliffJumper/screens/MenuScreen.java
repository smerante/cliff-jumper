package com.studios0110.CliffJumper.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Interface.Button;
import com.studios0110.CliffJumper.Splash.Splash;

/*
Sam Merante: This Starter Screen is a menu to get to other screens.
 */
public class MenuScreen implements NewScreenInterface{



    private BitmapFont font;
    private float showFPSDelay,FPS;
    private float width = Splash.screenW, height = Splash.screenH;
    boolean showDebug;
    Button start;
    private Texture menuScreen;
    public void show() {

        System.out.println("Construct Menu Screen");
        Splash.camera.zoom=1;
        Splash.camera.update();
        font = Splash.manager.get("Fonts/Font.fnt");
        menuScreen = Splash.manager.get("ui/menuScreen.png");
        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
        Gdx.input.setCatchBackKey(true);
		 start = new Button("playButton",new Vector2((width/2)-256,(height/2)-64-200));
		 mp.addProcessor(new GestureDetector(start));


    }
    private void reset(){
        mp.clear();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(Splash.camera.combined);
        shapes.setProjectionMatrix(Splash.camera.combined);
        shapes.setAutoShapeType(true);

        drawDebugBG();

        batch.begin();
        batch.enableBlending();
            batch.draw(menuScreen,0,0);
            start.draw(batch);
        batch.end();

        shapes.begin();

        shapes.end();
        //comment out on release
        drawDebugBG();
        showFPS(delta);
        update(delta);
    }
    private void update(float deltaT){
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            showDebug = !showDebug;
        }
		if(start.clicked)
		{
			start.resetButton();
			((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
		}

    }
    private void showFPS(float delta)
    {
        if(showDebug) {
            showFPSDelay += 1 * Gdx.graphics.getDeltaTime();
            if (showFPSDelay >= 1) {
                showFPSDelay = 0;
                FPS = (int) Math.ceil(1 / delta);
            }
            batch.begin();
            font.draw(batch, "FPS " + (int) FPS, width - 24 * 3, height - 50);
            batch.end();
        }
    }

    private void drawDebugBG()
    {
        if(showDebug) {
            shapes.begin();
            shapes.end();
        }
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}

}
