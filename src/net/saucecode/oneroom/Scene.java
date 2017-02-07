package net.saucecode.oneroom;

import org.lwjgl.opengl.GL11;

public abstract class Scene {

	String name;
	boolean sceneDone = false;
	
	public Scene(String name){
		this.name = name;
	}
	
	public void create(){
		sceneDone = false;
	}
	public abstract void destroy();
	public abstract void update();
	public abstract void render();
	
	public static void renderBox(float x1, float y1, float x2, float y2){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x1, y1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x2, y1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x2, y2);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}
}
