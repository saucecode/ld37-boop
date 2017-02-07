package net.saucecode.oneroom;

import java.awt.Color;

import net.saucecode.oneroom.util.FontTT;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class SceneInstructions extends Scene {

	public SceneInstructions(String name) {
		super(name);
		font = Resources.fonts.get("kenpixel");
	}
	
	FontTT font;

	public void create(){
		super.create();
	}
	
	public void destroy() {
		
	}

	public void update() {
		if(Mouse.isButtonDown(0)){
			Main.setNextScene(Main.scenes.get("mainmenu"));
			sceneDone = true;
		}
	}

	SceneGame tempGame = new SceneGame("tempgame");
	Player player = new Player(tempGame, 128, Display.getHeight() - 130);
	Hoop hoop = new Hoop(tempGame, Display.getWidth()-250, Display.getHeight()-200, 64);
	Vector2f[] grenade = {new Vector2f(156, Display.getHeight() - 280), new Vector2f(156 + 70, Display.getHeight() - 280)};
	
	public void render() {
		font.drawText("Instructions!", 24, Display.getWidth()/2, Display.getHeight()-32, 0, Color.white, 0, 0, 0, true);
		
		player.render();
		font.drawText("This is the player.", 16, 128 + 32, Display.getHeight() - 120, 0, Color.white, 0, 0, 0, false);
		font.drawText("Click and drag with the mouse to", 16, 128 + 32, Display.getHeight() - 120 - 18, 0, Color.white, 0, 0, 0, false);
		font.drawText("throw yourself around!", 16, 128 + 32, Display.getHeight() - 120 - 36, 0, Color.white, 0, 0, 0, false);
		
		hoop.render();
		font.drawText("This is a hoop. Fly through it to", 16, Display.getWidth()-250-128, Display.getHeight()-230, 0, Color.white, 0,0,0, false);
		font.drawText("earn points. Smaller hoops are", 16, Display.getWidth()-250-128, Display.getHeight()-230-18, 0, Color.white, 0,0,0, false);
		font.drawText("worth more!", 16, Display.getWidth()-250-128, Display.getHeight()-230-36, 0, Color.white, 0,0,0, false);
		
		hoop.spritesheet.bind();
		renderBox(grenade[0].x-32, grenade[0].y-32, grenade[0].x+32, grenade[0].y+32, 3, 1);
		renderBox(grenade[1].x-32, grenade[1].y-32, grenade[1].x+32, grenade[1].y+32, 0, 2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		font.drawText("These are grenades (clearly)", 16, 156, Display.getHeight() - 320, 0, Color.white, 0,0,0, false);
		font.drawText("if you get too close to them, they'll", 16, 156, Display.getHeight() - 320 - 18, 0, Color.white, 0,0,0, false);
		font.drawText("explode, and knock you away.", 16, 156, Display.getHeight() - 320 - 36, 0, Color.white, 0,0,0, false);

		font.drawText("If you fall out of the room, you lose.", 16, Display.getWidth()/2, 150, 0, Color.white, 0, 0, 0, true);
		font.drawText("You need at least 500 points to submit your score.", 18, Display.getWidth()/2, 100, 0, Color.white, 0, 0, 0, true);
		
		font.drawText("click anywhere to return.", 16, Display.getWidth() - 200, 32, 0, Color.white, 0, 0, 0, true);
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
