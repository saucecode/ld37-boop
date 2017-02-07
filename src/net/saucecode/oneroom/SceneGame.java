package net.saucecode.oneroom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import net.saucecode.oneroom.util.FontTT;
import net.saucecode.oneroom.util.Texture;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class SceneGame extends Scene {
	
	static Random rng = new Random();
	static float MASTER_VOLUME = 1.0f;
	static int MY_HIGHEST_SCORE = 0, MINIMUM_SCORE = 500;
	
	Player player;
	int score = 0, ticker = 0, scoreTicker = 0;
	float ASCENSION_SPEED = 4;
	long time = System.currentTimeMillis()/1000;
	
	List<Platform> platforms = new ArrayList<Platform>();
	List<Hoop> hoops = new ArrayList<Hoop>();
	List<Grenade> grenades = new ArrayList<Grenade>();
	List<FloatingText> floatingTexts = new ArrayList<FloatingText>();
	
	ParticleConfig explosionConfig;
	List<Particle> particles = new ArrayList<Particle>();
	
	FontTT font = Resources.fonts.get("kenpixel");
	Texture spritesheet, background;
	
	float closingScreen = 0;
	boolean gameOver = false;
	boolean scoreSubmitted = false;
	
	public SceneGame(String name) {
		super(name);
		player = new Player(this, 50, Display.getHeight()/2);
		spritesheet = Resources.textures.get("spritesheet");
		background = Resources.textures.get("background");
		
		explosionConfig = new ParticleConfig(this, 1f, 1f, 0.3f, 3, 3);
	}

	public void create(){
		super.create();
	}
	
	public void destroy() {
		
	}
	
	public void update() {
		player.update();
		if(!gameOver) mouseController();
		
		if(!gameOver){
			generateGameElements();
			
			if(player.y < 0) closingScreen += 22; else closingScreen = 0;
			
			keepScore();
		}
		
		updatePlatforms();
		updateHoops();
		updateGrenades();
		updateFloatingTexts();
		
		if(closingScreen > Display.getHeight() && !gameOver){
			triggerGameOver();
		}
		
		if(gameOver){
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Main.setNextScene(Main.scenes.get("mainmenu"));
				System.out.println(name + " exiting to main menu...");
				sceneDone = true;
			}
			
			if(Mouse.isButtonDown(1)){ // right button down
				Main.setNextScene(new SceneGame("Game-" + (int)(System.currentTimeMillis()/1000) ));
				System.out.println("Playing again! Resetting the stage.");
				sceneDone = true;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !scoreSubmitted && score > MINIMUM_SCORE && score > MY_HIGHEST_SCORE){
				String name = JOptionPane.showInputDialog("Enter a name (3-16 characters, alphanumeric)");
				boolean success = HighScore.submitNewScore(name, score);
				System.out.println("Uploaded score success: " + success);
				scoreSubmitted = success;
				if(success){
					JOptionPane.showMessageDialog(null, "Done.");
					MY_HIGHEST_SCORE = score;
				}else{
					JOptionPane.showMessageDialog(null, "Failed to upload score.");
				}
			}
		}
	}
	
	private void updateFloatingTexts() {
		for(int i=0; i<floatingTexts.size(); i++){
			FloatingText text = floatingTexts.get(i);
			if(text == null) continue;
			
			text.update();
			
			if(text.y < 0){
				floatingTexts.remove(i);
				i--;
			}
		}		
	}

	private void keepScore() {
		scoreTicker++;
		if(scoreTicker >= 60){
			long difference = System.currentTimeMillis()/1000 - time;
			score += (int) difference;
			
			time = System.currentTimeMillis()/1000;
			scoreTicker = 0;
		}		
	}

	private void generateGameElements() {
		ticker++;
		if(ticker >= 40){
			ticker = 0;
			
			if(Math.random() < 0.08){
				grenades.add(new Grenade(this, (float) Math.random() * Display.getWidth()/2 + Display.getWidth()/4, 0));
				if(Math.random() < 0.1){
					for(int i=0; i<5; i++) grenades.add(new Grenade(this, (float) Math.random() * Display.getWidth()/2 + Display.getWidth()/4, 0));
				}
			}
			
			if(Math.random() > 0.4){
				platforms.add(new Platform(
					(float) Math.random() * Display.getWidth() - 160,
					-32,
					96 + (float) Math.random()*160,
					16 + (float) Math.random()*64
				));
				if(Math.random() > 0.85)
				platforms.add(new Platform(
					(float) Math.random() * Display.getWidth() - 160,
					-32,
					96 + (float) Math.random()*160,
					16 + (float) Math.random()*64
				));
			}else{
				// hoops have sizes 20, 40, 64, 82
				hoops.add(new Hoop(this, (float) (Math.random()*Display.getWidth()), 0, Hoop.SIZES[rng.nextInt(Hoop.SIZES.length)]));
			}
		}		
	}

	private void updateHoops() {
		for(int i=0; i<hoops.size(); i++){
			Hoop hoop = hoops.get(i);
			if(hoop == null) continue;
			
			hoop.y += ASCENSION_SPEED;
			hoop.update();
			
			if(hoop.y > Display.getHeight() + 200){
				hoops.remove(i);
				i--;
			}
		}		
	}
	
	private void updateGrenades() {
		for(int i=0; i<grenades.size(); i++){
			Grenade grenade = grenades.get(i);
			if(grenade == null) continue;
			
			grenade.update();
			
			if(grenade.y < -500 || grenade.destroy){
				grenades.remove(i);
				i--;
			}
		}		
	}

	private void updatePlatforms() {
		for(int i=0; i<platforms.size(); i++){
			Vector4f platform = platforms.get(i);
			if(platform == null) continue;
			
			platform.y += ASCENSION_SPEED;
			if(platform.y > Display.getHeight() + 200){
				platforms.remove(i);
				i--;
			}
		}		
	}

	private void triggerGameOver() {
		gameOver = true;
		System.out.println("Game over!");
		Resources.sounds.get("gameOver").playAsSoundEffect(1, 0.15f * MASTER_VOLUME, false);
	}

	public void render() {
		GL11.glClearColor(0.5f, 0.6f, 0.6f, 1.0f);
		
		// draw background
		GL11.glColor3f(0.75f, 0.75f, 0.75f);
		background.bind();
		for(float x=0; x<Display.getWidth(); x+=128){
			for(float y=0; y<Display.getHeight(); y+=128){
				renderBox(x, y, x+128, y+128);
			}
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		/*Resources.textures.get("jungle").bind();
		renderBox(0, 0, Display.getWidth(), Display.getWidth());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);*/
		
		drawClosingScreen();
		
		player.render();
		
		GL11.glColor3f(0.1f, 0.1f, 0.1f);
		//spritesheet.bind();
		for(Platform platform : platforms)
			platform.render();
		GL11.glColor3f(1, 1, 1);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		for(Hoop hoop : hoops)
			hoop.render();
		
		for(Grenade grenade : grenades)
			grenade.render();
		
		for(FloatingText text : floatingTexts)
			text.render();
		
		for(Particle particle : particles)
			particle.render();
		
		if(mouseDown && !gameOver){
			GL11.glLineWidth(3);
			GL11.glColor3f(1, 1, 1);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(mouseStart.x, mouseStart.y);
			GL11.glVertex2f(Mouse.getX(), Mouse.getY());
			GL11.glEnd();
			GL11.glLineWidth(1);
		}
		
		font.drawText("Score: " + score, 18, 0, 32, 0, Color.WHITE, 0,0,0, false);
		
		if(gameOver){
			font.drawText("Game Over", 40, Display.getWidth()/2, Display.getHeight()/2 + 100, 0, Color.BLACK, 0, 0, 0, true);
			
			font.drawText("right click to continue", 24, Display.getWidth()/2, Display.getHeight()/2 - 100, 0, Color.BLACK, 0,0,0, true);
			font.drawText("escape to exit", 20, Display.getWidth()/2, Display.getHeight()/2 - 130, 0, Color.BLACK, 0,0,0, true);
			
			if(score > MINIMUM_SCORE && !scoreSubmitted && score > MY_HIGHEST_SCORE){
				font.drawText("press space to submit your high score", 20, Display.getWidth()/2, Display.getHeight()/2 - 200, 0, Color.GREEN, 0, 0, 0, true);
				font.drawText("(requires an internet connection)", 16, Display.getWidth()/2, Display.getHeight()/2 - 230, 0, Color.BLACK, 0, 0, 0, true);
			}
		}
	}

	private void drawClosingScreen() {
		GL11.glColor3f(1, 0, 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(0, Display.getHeight());
		GL11.glVertex2f(Display.getWidth(), Display.getHeight());
		GL11.glVertex2f(Display.getWidth(), Display.getHeight() - closingScreen);
		GL11.glVertex2f(0, Display.getHeight() - closingScreen);
		GL11.glEnd();
	}

	boolean mouseDown = false;
	Vector2f mouseStart = new Vector2f(0, 0);
	Vector2f mouseEnd = new Vector2f(0, 0);
	private void mouseController() {
		if(Mouse.isButtonDown(0)){
			if(!mouseDown){
				mouseStart.x = Mouse.getX();
				mouseStart.y = Mouse.getY();
			}
			mouseDown = true;
		}
		
		if(!Mouse.isButtonDown(0) && mouseDown){ // mouse released
			mouseDown = false;
			mouseEnd.x = Mouse.getX();
			mouseEnd.y = Mouse.getY();
			
			Vector2f profile = new Vector2f();
			Vector2f.sub(mouseStart, mouseEnd, profile);
			
			player.velocity.x += profile.x * 0.05f;
			player.velocity.y += profile.y * 0.05f;
		}
	}
	
	public void renderBox(float x1, float y1, float x2, float y2, int tileX, int tileY){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(tileX*64 / 256.0f, tileY*64 / 256.0f);
		GL11.glVertex2f(x1, y1);
		
		GL11.glTexCoord2f((tileX+1)*64 / 256.0f, tileY*64 / 256.0f);
		GL11.glVertex2f(x2, y1);
		
		GL11.glTexCoord2f((tileX+1)*64 / 256.0f, (tileY+1)*64 / 256.0f);
		GL11.glVertex2f(x2, y2);
		
		GL11.glTexCoord2f(tileX*64 / 256.0f, (tileY+1)*64 / 256.0f);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}
}
