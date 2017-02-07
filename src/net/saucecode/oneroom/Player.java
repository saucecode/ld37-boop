package net.saucecode.oneroom;

import java.awt.geom.Rectangle2D;

import net.saucecode.oneroom.util.Texture;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.openal.Audio;


public class Player extends Vector2f {

	private static final long serialVersionUID = -1119311656178536591L;
	
	Vector2f velocity = new Vector2f(0, 0);
	static final Vector2f gravity = new Vector2f(0, -10/60.0f);
	
	Audio boop = Resources.sounds.get("boop");
	
	SceneGame game;
	float width = 16;
	float height = 16;
	float angle = 0;
	
	Texture spritesheet;
	int animationFrame = 0;
	int animationTicker = 0;
	boolean animationDirection = false;

	public Player(SceneGame parent, float x, float y){
		super(x,y);
		game = parent;
		spritesheet = Resources.textures.get("spritesheet");
	}
	
	public void update(){
		for(int j=0; j<4; j++){
			velocity.x += gravity.x * 0.25f;
			velocity.y += gravity.y * 0.25f;
			x += velocity.x * 0.25f;
			y += velocity.y * 0.25f;
			
			if(y > Display.getHeight()){
				velocity.y *= -0.9f;
				if(!boop.isPlaying()) boop.playAsSoundEffect(1, 0.1f * SceneGame.MASTER_VOLUME, false);
				y = Display.getHeight();
			}
			if(x < 0 || x > Display.getWidth()){
				velocity.x *= -0.9f;
				if(!boop.isPlaying()) boop.playAsSoundEffect(1, 0.1f * SceneGame.MASTER_VOLUME, false);
				
				if(x < 0)
					x = 0;
				else
					x = Display.getWidth();
			}
			
			// chose player lines
			Vector4f[] lines = {
				new Vector4f(x, y, x, y + height/2),
				new Vector4f(x, y, x + width/2, y),
				new Vector4f(x, y, x, y - height/2),
				new Vector4f(x, y, x - width/2, y)
			};
			
			// check collisions
			for(Vector4f platform : game.platforms){
				Rectangle2D rect = new Rectangle2D.Float(platform.x, platform.y, platform.z, platform.w);
				for(int i=0; i<4; i++){
					Vector4f line = lines[i];
					if(rect.intersectsLine(line.x, line.y, line.z, line.w)){
						if(i == 0 || i == 2) velocity.y *= -0.9f;
						else velocity.x *= -0.9f;
						
						if(i == 0)
							y = platform.y - height/2;
						else if(i == 2)
							y = platform.y + platform.w + height/2;
						else if(i == 1)
							x = platform.x - width/2;
						else
							x = platform.x + platform.z + width/2;
						
						if(!boop.isPlaying()) boop.playAsSoundEffect(1, 0.1f * SceneGame.MASTER_VOLUME, false);
					}
				}
			}
		}
		
		angle = (float) Math.atan2(velocity.y, velocity.x);
	}
	
	public void render(){
		animationTicker++;
		
		if(animationTicker >= 7){
			animationTicker = 0;
			animationFrame += animationDirection ? 1 : -1;
			
			if(animationFrame >= 2){
				animationFrame = 2;
				animationDirection = !animationDirection;
			}else if(animationFrame < 0){
				animationFrame = 1;
				animationDirection = !animationDirection;
			}
		}
		
		GL11.glColor3f(1,1,1);
		spritesheet.bind();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
		renderBox(-width, -height, width, height, animationFrame);
		GL11.glPopMatrix();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void renderBox(float x1, float y1, float x2, float y2, int frame){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(frame*64 / 256.0f, 64f/256f);
		GL11.glVertex2f(x1, y1);
		
		GL11.glTexCoord2f((frame+1)*64 / 256.0f, 64f/256f);
		GL11.glVertex2f(x2, y1);
		
		GL11.glTexCoord2f((frame+1)*64 / 256.0f, 128f/256f);
		GL11.glVertex2f(x2, y2);
		
		GL11.glTexCoord2f(frame*64 / 256.0f, 128f/256f);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}
}
