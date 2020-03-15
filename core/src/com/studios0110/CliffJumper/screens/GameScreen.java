package com.studios0110.CliffJumper.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.CliffJumper.Interface.Button;
import com.studios0110.CliffJumper.Interface.ScoreFont;
import com.studios0110.CliffJumper.Interface.SoundEffects;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.objects.Background;
import com.studios0110.CliffJumper.objects.Floor;
import com.studios0110.CliffJumper.objects.Platforms;
import com.studios0110.CliffJumper.player.Player;

/*
Sam Merante: This Starter Screen is a menu to get to other screens.
 */
public class GameScreen implements NewScreenInterface{
	
	

	private BitmapFont font;
	private float showFPSDelay,FPS,increasScoreAfterDeadDelay;
	private float width = Splash.screenW, height = Splash.screenH;
	private Player player;
	private Platforms platforms;
	private Background background;
    private Floor floor;
	public static ScoreFont scoreFont,bestScoreFont;
	private boolean showDebug,justDied;
	public static SoundEffects soundEffects;
	Button restartButton,menuButton;
	private Texture gameOverMenu,score, best;
	Preferences prefs;
	private int gameOverMenuPosX,gameOverMenuPosy;
	private String scoreReached;
	public void show() {
		
		System.out.println("Construct Start Screen");
		Splash.camera.zoom=1;
		Splash.camera.update();
		font = Splash.manager.get("Fonts/Font.fnt");
		gameOverMenu = Splash.manager.get("ui/gameOverScreen.png");
        score = Splash.manager.get("ui/score.png");
        best = Splash.manager.get("ui/best.png");
		gameOverMenuPosX = 0;
		gameOverMenuPosy = 0;
		Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
		Gdx.input.setCatchBackKey(true);
		restartButton = new Button("restartButton",new Vector2((width/2 - 32)-100- gameOverMenuPosX,(height/2)-100-gameOverMenuPosy));
		restartButton.visible = false;
		mp.addProcessor(new GestureDetector(restartButton));
		menuButton = new Button("menuButton",new Vector2((width/2 -32)+100- gameOverMenuPosX,(height/2)-100-gameOverMenuPosy));
		menuButton.visible = false;
	 	mp.addProcessor(new GestureDetector(menuButton));
		prefs = Gdx.app.getPreferences(Splash.PREF_NAME);
		player = new Player();
		platforms = new Platforms();
        floor = new Floor("Objects/floor.png",new Vector2(-2048*2,0));
		background = new Background();
        scoreFont = new ScoreFont(750,700);
		bestScoreFont = new ScoreFont(750,100);
		mp.addProcessor(player);
		showDebug = false;
		justDied = false;
		increasScoreAfterDeadDelay = 0;
		soundEffects = new SoundEffects();
		soundEffects.playBirds();
	}
	private void reset(){
		mp.clear();
		player = new Player();
		platforms = new Platforms();
		floor = new Floor("Objects/floor.png",new Vector2(-2048*2,0));
		background = new Background();
        scoreFont = new ScoreFont(750,700);
		soundEffects.reset();
		soundEffects.playBirds();
		restartButton.visible = false;
		justDied = false;
		menuButton.visible = false;
		increasScoreAfterDeadDelay = 0;
		mp.addProcessor(new GestureDetector(restartButton));
		mp.addProcessor(new GestureDetector(menuButton));
		mp.addProcessor(player);
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(Splash.camera.combined);
		shapes.setProjectionMatrix(Splash.camera.combined);
		shapes.setAutoShapeType(true);

        drawDebugBG();

		batch.begin();
		batch.enableBlending();
			background.draw(batch);
			floor.draw(batch);
			platforms.draw(batch);
			player.draw(batch);
			if(!player.isDead)
            scoreFont.draw(batch);
			platforms.drawLight(batch);
		batch.end();

		shapes.begin();
			floor.drawUNDERWORLDlol(shapes);
		shapes.end();

		if(player.isDead){
			batch.begin();
				batch.draw(gameOverMenu,(width/2)-256- gameOverMenuPosX,(height/2)-128- gameOverMenuPosy);
                batch.draw(score,(width/2)- score.getWidth()/2 - gameOverMenuPosX,(height/2) - gameOverMenuPosy+245);
                batch.draw(best,(width/2)- best.getWidth()/2 - gameOverMenuPosX,(height/2)- gameOverMenuPosy+70);
				restartButton.draw(batch);
				menuButton.draw(batch);
				scoreFont.draw(batch);
				bestScoreFont.draw(batch);
			batch.end();
		}
		//comment out on release
		drawDebugBG();
		showFPS(delta);
		update(delta);
	}
	private void update(float deltaT){
		player.update(deltaT,platforms,floor);
		background.update();
		platforms.update(deltaT);
		floor.update();
		if(player.isDead && !justDied)
		{
			justDied = true;
			restartButton.visible = true;
			menuButton.visible = true;
			scoreReached = scoreFont.score;
			scoreFont.setScore("0");
			scoreFont.setPos(width/2 - gameOverMenuPosX, height/2+155 - gameOverMenuPosy);
			if(prefs.getInteger(Splash.HIGH_SCORE) < Integer.parseInt(scoreReached)){
				//HighScore
				prefs.putInteger(Splash.HIGH_SCORE,Integer.parseInt(scoreReached));
				prefs.flush();
			}

			bestScoreFont.setScore(Integer.toString(prefs.getInteger(Splash.HIGH_SCORE)));
			bestScoreFont.setPos(width/2 - gameOverMenuPosX, height/2 - 20 - gameOverMenuPosy);
		}
		if(player.isDead){
			increasScoreAfterDeadDelay+= Gdx.graphics.getDeltaTime();
			if(increasScoreAfterDeadDelay>=0.05 && !scoreFont.score.equalsIgnoreCase(scoreReached)) {
				scoreFont.increaseScore();
				increasScoreAfterDeadDelay = 0;
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			reset();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
			showDebug = !showDebug;
		}

		if(restartButton.clicked)
		{
			restartButton.resetButton();
			reset();
		}
		if(menuButton.clicked)
		{
			menuButton.resetButton();
			dispose();
			((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
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
			font.draw(batch, "" + player.position.y, width -24*6, height - 150);
			batch.end();
		}
	}
	
	private void drawDebugBG()
    {
		if(showDebug) {
			shapes.begin();
			platforms.drawMesh(shapes);
			player.drawMesh(shapes);
			floor.drawMesh(shapes);
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
	public void dispose() {
		font = null;
		gameOverMenu =  null;
		Gdx.input.setInputProcessor( null); // set the default input processor to the multiplex processor from NewScreenInterface
		restartButton =null;
		menuButton = null;
		player = null;
		platforms = null;
		floor = null;
		background = null;
		scoreFont = null;
		soundEffects.endAll();
		soundEffects = null;
	}
	
}
