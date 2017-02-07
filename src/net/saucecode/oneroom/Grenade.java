package net.saucecode.oneroom;

import net.saucecode.oneroom.util.Texture;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.openal.Audio;

public class Grenade extends Vector2f {

	private static final long serialVersionUID = 5383301233825522268L;
	private static final float radius = 96;
	
	static Audio[] explosionSounds = new Audio[3];
	
	SceneGame game;
	Vector2f velocity = new Vector2f();
	int ticker = 0, rotationRate = 0;
	int animationTicker = 0;
	Texture spritesheet;
	int spriteX, spriteY;
	
	boolean destroy = false;
	
	public Grenade(SceneGame parent, float x, float y){
		super(x,y);
		game = parent;
		spritesheet = Resources.textures.get("spritesheet");
		
		velocity.set((0.5f - SceneGame.rng.nextFloat()) * 2 * 7, 15);
		
		if(SceneGame.rng.nextBoolean()){
			spriteX = 3;
			spriteY = 1;
		}else{
			spriteX = 0;
			spriteY = 2;
		}
		
		if(explosionSounds[0] == null){
			explosionSounds[0] = Resources.sounds.get("explosion1");
			explosionSounds[1] = Resources.sounds.get("explosion2");
			explosionSounds[2] = Resources.sounds.get("explosion3");
		}
		
		rotationRate = 30 - SceneGame.rng.nextInt(60);
	}
	
	public void update(){
		Vector2f.add(velocity, Player.gravity, velocity);
		Vector2f.add(this, velocity, this);
		
		if(!destroy)
		if((game.player.x - x)*(game.player.x - x) + (game.player.y - y)*(game.player.y - y) < radius*radius){
			ticker++;
			if(ticker > 8) explode();
		}
		
		if(x < 0){
			x = 0;
			velocity.x *= -1;
		}
		if(x > Display.getWidth()){
			x = Display.getWidth();
			velocity.x *= -1;
		}
	}
	
	private void explode() {
		destroy = true;
		float magnitude = (float) Math.hypot(game.player.x - x, game.player.y - y) / radius * 15f;
		float angle = (float) Math.atan2(y - game.player.y, x - game.player.x);
		Vector2f explosionForce = new Vector2f(-magnitude * (float) Math.cos(angle), -magnitude * (float) Math.sin(angle));
		Vector2f.add(game.player.velocity, explosionForce, game.player.velocity);
		
		// game.floatingTexts.add(new FloatingText(game, "Explosion!", 24, Color.red, game.player.x, game.player.y));
		
		for(int i=0; i<30; i++){
			game.particles.add(new Particle(game.explosionConfig, x, y).setVelocity(
					5 - SceneGame.rng.nextFloat()*10,
					5 - SceneGame.rng.nextFloat()*10
					));
		}
		
		explosionSounds[SceneGame.rng.nextInt(explosionSounds.length)].playAsSoundEffect(1, 0.2f * SceneGame.MASTER_VOLUME, false);
	}

	public void render(){
		GL11.glColor3f(1, 1, 1);
		spritesheet.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(animationTicker, 0, 0, 1);
		renderBox(-16, -16, 16, 16, spriteX, spriteY);
		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		animationTicker += rotationRate;

		/*GL11.glColor3f(0,0,0);
		GL11.glBegin(GL11.GL_POINTS);
		for(float r=0; r<Math.PI*2; r+=0.1){
			GL11.glVertex2d(x + radius * Math.cos(r), y + radius * Math.sin(r));
		}
		GL11.glEnd();*/
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
