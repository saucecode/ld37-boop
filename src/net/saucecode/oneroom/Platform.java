package net.saucecode.oneroom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

public class Platform extends Vector4f {

	private static final long serialVersionUID = 4487176347487913237L;

	int spriteX, spriteY;
	
	public Platform(float x, float y, float z, float w){
		super(x,y,z,w);
		
		if(Math.random() > 0.5){
			spriteX = 1;
			spriteY = 2;
		}else{
			spriteX = 2;
			spriteY = 2;
		}
	}
	
	public void render(){
		renderBox(x, y, x + z, y + w, spriteX, spriteY);
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
