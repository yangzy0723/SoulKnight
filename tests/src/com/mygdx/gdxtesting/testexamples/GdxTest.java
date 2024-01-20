package com.mygdx.gdxtesting.testexamples;

import com.badlogic.gdx.Gdx;
import com.mygdx.gdxtesting.GdxTestRunner;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.MediaLose;
import com.mygdx.soulknight.assets.MediaMenu;
import com.mygdx.soulknight.assets.MediaWin;
import com.mygdx.soulknight.character.Knight;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.map.Map;
import com.mygdx.soulknight.screen.GamePlayingScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class GdxTest {
	@Test
	public void testFileExistence(){
		assertTrue("This test will only pass when the basic/building/floor1.png file exists.", Gdx.files
				.internal("basic/building/floor1.png").exists());
		assertTrue("This test will only pass when the basic/bullets/blueBullet.png file exists.", Gdx.files
				.internal("basic/bullets/blueBullet.png").exists());
		assertTrue("This test will only pass when the basic/doors/door1.png file exists.", Gdx.files
				.internal("basic/doors/door1.png").exists());
		assertTrue("This test will only pass when the basic/roles/colliderLeft.png file exists.", Gdx.files
				.internal("basic/roles/colliderLeft.png").exists());
		assertTrue("This test will only pass when the basic/roles/colliderLeft.png file exists.", Gdx.files
				.internal("basic/roles/colliderLeft.png").exists());
		assertTrue("This test will only pass when the fonts/Adequate.ttf file exists.", Gdx.files
				.internal("fonts/Adequate.ttf").exists());
		assertTrue("This test will only pass when the maps/level1.txt file exists.", Gdx.files
				.internal("maps/level1.txt").exists());
		assertTrue("This test will only pass when the music/beHurt.mp3 file exists.", Gdx.files
				.internal("music/beHurt.mp3").exists());
		assertTrue("This test will only pass when the ui/Health.png file exists.", Gdx.files
				.internal("ui/Health.png").exists());
		assertTrue("This test will only pass when the figs/lose.jpg file exists.", Gdx.files
				.internal("ui/figs/lose.jpg").exists());
	}
	@Test(timeout = 100)
	public void testLoadEfficiency(){
		MediaGamePlaying.loadAssets();
		MediaLose.loadAssets();
		MediaMenu.loadAssets();
		MediaWin.loadAssets();
		MediaGamePlaying.disposeAssets();
		MediaLose.disposeAssets();
		MediaMenu.disposeAssets();
		MediaWin.disposeAssets();
	}
	@Test(timeout = 16)
	public void testWorkEfficiency(){
		GamePlayingScreen gamePlayingScreen = new GamePlayingScreen(new SoulKnightGame(), "maps/level1.txt");
		for(Monster monster : GamePlayingScreen.monsters)
			monster.attackAct(GamePlayingScreen.monstersBullets, GamePlayingScreen.knights);
		gamePlayingScreen.update();
		for(Monster monster : GamePlayingScreen.monsters)
			monster.monsterThread.stopThread();
	}
	@Test(expected = NullPointerException.class)
	public void testMapArrayPointer(){
		Map map = new Map("maps/level1.txt");
		System.out.println(map.levelPath);
	}
	@Test
	public void testDoorGenerate(){
		GamePlayingScreen gamePlayingScreen = new GamePlayingScreen(new SoulKnightGame(), "maps/level1.txt");
		for(Monster monster : GamePlayingScreen.monsters)
			monster.monsterThread.stopThread();
		GamePlayingScreen.monsters.clear();
		gamePlayingScreen.update();
        assertNotNull(gamePlayingScreen.door);
	}
	@Test
	public void testCreatureState(){
		new GamePlayingScreen(new SoulKnightGame(), "maps/level1.txt");
		for(Monster monster : GamePlayingScreen.monsters)
			monster.monsterThread.stopThread();
		for(Monster monster : GamePlayingScreen.monsters)
			assertTrue(monster.health > 0);
		for(Knight knight : GamePlayingScreen.knights)
			assertTrue(knight.health > 0);
	}
	@Test
	public void testCreatureDeath(){
		new GamePlayingScreen(new SoulKnightGame(), "maps/level1.txt");
		for(Monster monster : GamePlayingScreen.monsters)
			monster.monsterThread.stopThread();
		for(Knight knight : GamePlayingScreen.knights) {
			knight.beAttack(10000);
			assertTrue(knight.health < 0);
		}
	}
	@Test
	public void testBulletState(){
		new GamePlayingScreen(new SoulKnightGame(), "maps/level1.txt");
		for(Monster monster : GamePlayingScreen.monsters) {
			monster.attackAct(GamePlayingScreen.monstersBullets, GamePlayingScreen.knights);
			monster.move(1, GamePlayingScreen.knights);
		}
		for(Monster monster : GamePlayingScreen.monsters)
			monster.monsterThread.stopThread();
		for(Bullet bullet : GamePlayingScreen.monstersBullets)
			assertTrue(bullet.getAttack() > 0);
	}
}
