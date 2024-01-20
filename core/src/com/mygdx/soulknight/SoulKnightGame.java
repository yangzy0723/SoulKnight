package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.screen.MenuScreen;

public class SoulKnightGame extends Game {
	public SpriteBatch batch;
	public void create () {
		batch = new SpriteBatch();
		Variable.generateSkin();

		this.setScreen(new MenuScreen(this));
	}

	public void render () {
		super.render();	// important!
	}

	public void dispose () {
		batch.dispose();
	}
}
