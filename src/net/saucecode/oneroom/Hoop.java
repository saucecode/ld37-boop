package net.saucecode.oneroom;

import java.awt.Color;

import net.saucecode.oneroom.util.Texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.openal.Audio;

public class Hoop extends Vector2f {

	private static final long serialVersionUID = -9027837228411980592L;
	
	public static final int[] SIZES = {32, 48, 64, 80, 96};
	
	Audio hoopSound;
	
	SceneGame game;
	float width, height;
	private float platformWidth = 24, platformHeight = 16;
	Platform[] platforms = new Platform[2];
	Texture spritesheet;
	boolean captured = false;
	
	float fontSize;
	
	int animationFrame = 0;
	boolean direction = false;
	int ticker = 0;
	
	public Hoop(SceneGame parent, float x, float y, float height){
		super(x,y);
		game = parent;
		
		this.width = 50;
		this.height = height;
		
		// bottom platform
		platforms[0] = new Platform(x - platformWidth/2, y - height/2 - platformHeight, platformWidth, platformHeight);
		// top platform
		platforms[1] = new Platform(x - platformWidth/2, y + height/2, platformWidth, platformHeight);
		
		game.platforms.add(platforms[0]);
		game.platforms.add(platforms[1]);
		
		spritesheet = Resources.textures.get("spritesheet");
		
		fontSize = 18;
		if(height == SIZES[4]) fontSize = 16;
		if(height == SIZES[3]) fontSize = 20;
		if(height == SIZES[2]) fontSize = 24;
		if(height == SIZES[1]) fontSize = 28;
		if(height == SIZES[0]) fontSize = 32;
		
		hoopSound = Resources.sounds.get("hoopCapture");
	}
	
	public void update(){
		if(!captured){
			if( (game.player.x > x - platformWidth/2 && game.player.x < x + platformWidth/2 && game.player.y > y - height/2 && game.player.y < y + height/2)
			 || (game.player.x + game.player.velocity.x > x - platformWidth/2 && game.player.x + game.player.velocity.x < x + platformWidth/2
					 && game.player.y + game.player.velocity.y > y - height/2 && game.player.y + game.player.velocity.y < y + height/2)
			 || (game.player.x - game.player.velocity.x > x - platformWidth/2 && game.player.x - game.player.velocity.x < x + platformWidth/2
					 && game.player.y - game.player.velocity.y > y - height/2 && game.player.y - game.player.velocity.y < y + height/2)){
				hoopCaptured();
			}
		}
	}
	
	private void hoopCaptured() {
		captured = true;
		game.score += 112 - height;
		
		FloatingText text = new FloatingText(game, "+" + (int) (112 - height), fontSize, Color.BLACK, game.player.x, game.player.y);
		text.velocity.set((float) (0.5 - Math.random()) * 2 * 10, (float) Math.random() * 5);
		game.floatingTexts.add(text);
		
		hoopSound.playAsSoundEffect(1, 0.2f * SceneGame.MASTER_VOLUME, false);
	}

	public void render(){
		if(!captured){
			ticker++;
			if(ticker >= 5){
				ticker = 0;
				animationFrame += direction ? 1 : -1;
			}
			
			if(animationFrame >= 4){
				animationFrame = 2;
				direction = !direction;
			}else if(animationFrame < 0){
				animationFrame = 1;
				direction = !direction;
			}
			
			GL11.glColor3f(0, 1, 0);
			spritesheet.bind();
			renderBox(x - width/2, y - height/2, x + width/2, y + height/2, animationFrame);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}
	
	// DET FINNS FYRA RAMAR
	public void renderBox(float x1, float y1, float x2, float y2, int frame){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(frame*64 / 256.0f, 0);
		GL11.glVertex2f(x1, y1);
		
		GL11.glTexCoord2f((frame+1)*64 / 256.0f, 0);
		GL11.glVertex2f(x2, y1);
		
		GL11.glTexCoord2f((frame+1)*64 / 256.0f, 64f/256f);
		GL11.glVertex2f(x2, y2);
		
		GL11.glTexCoord2f(frame*64 / 256.0f, 64f/256f);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}
}
