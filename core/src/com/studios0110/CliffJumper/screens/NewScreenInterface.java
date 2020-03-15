package com.studios0110.CliffJumper.screens;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

interface NewScreenInterface extends Screen{
	
	SpriteBatch batch = new SpriteBatch();
	ShapeRenderer shapes = new ShapeRenderer();
	InputMultiplexer mp = new InputMultiplexer();
	@Override
	void show();
	@Override
	void render(float delta);

	
	@Override
	void resize(int width, int height) ;
	@Override
	void pause() ;
	@Override
	void resume();
	@Override
	void hide() ;
	@Override
	void dispose() ;
	
}
