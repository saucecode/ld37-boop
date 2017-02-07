package net.saucecode.oneroom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.saucecode.oneroom.util.FontTT;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class SceneMainMenu extends Scene {

	FontTT font;
	List<String> menuItems = new ArrayList<String>();
	int selector = 0;
	boolean keyDown = false;
	
	static List<String> recentScores, highestScores;
	
	public SceneMainMenu(String name) {
		super(name);
		
		font = Resources.fonts.get("kenpixel");
		
		menuItems.add("play");
		menuItems.add("how to play");
		menuItems.add("quit");
	}
	
	public void create(){
		super.create();
		
		(new Thread(){
			public void run(){
				SceneMainMenu.recentScores = HighScore.getRecentScores();
			}
		}).start();
		
		(new Thread(){
			public void run(){
				SceneMainMenu.highestScores = HighScore.getHighestScores();
			}
		}).start();
		
	}
	
	public void destroy() {
		
	}

	public void update() {
		if(!keyDown){
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				selector += 1;
				keyDown = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				selector -= 1;
				keyDown = true;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				doMenuSelect();
			}
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_DOWN)) keyDown = false;
	}

	private void doMenuSelect() {
		switch(selector){
		case 0:
			System.out.println("Play game!");
			Main.setNextScene(new SceneGame("Game-" + (int) (System.currentTimeMillis()/1000)));
			sceneDone = true;
			break;
			
		case 1:
			System.out.println("How to play!");
			Main.setNextScene(new SceneInstructions("Instructions"));
			sceneDone = true;
			break;
			
		case 2:
			System.out.println("Close game!");
			Main.closeRequested = true;
			break;
			
		default:
			System.out.println("Unknown option.");
			break;
		};		
	}

	public void render() {
		GL11.glClearColor(0, 0, 0, 1);
		font.drawText(Display.getTitle(), 24, Display.getWidth()/2, Display.getHeight() - 200, 0, Color.WHITE, 0, 0, 0, true);
		
		for(int i=0; i<menuItems.size(); i++){
			String item = menuItems.get(i);
			
			font.drawText(item, 20, Display.getWidth()/2 - 100, Display.getHeight() - 300 - i*32, 0, Color.white, 0, 0, 0, false);
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		float x = Display.getWidth()/2 - 130;
		float y = Display.getHeight() - 320 - selector*32;
		renderBox(x, y, x + 16, y + 16);
		
		font.drawText("created by saucecode for LudumDare37", 16, 4, 28, 0, Color.GREEN, 0, 0, 0, false);
		
		if(recentScores != null)
			if(recentScores.size() > 0){
				font.drawText("recent scores", 20, 0, Display.getHeight()-24, 0, Color.WHITE, 0, 0, 0, false);
				for(int i=0; i<recentScores.size(); i++){
					String[] score = recentScores.get(i).split(" ");
					
					font.drawText(score[0], 16, 4, Display.getHeight()-64 - i*16, 0, Color.WHITE, 0, 0, 0, false);
					font.drawText(score[1], 16, 200, Display.getHeight()-64 - i*16, 0, Color.WHITE, 0, 0, 0, false);
				}
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			}
		
		if(highestScores != null)
			if(highestScores.size() > 0){
				font.drawText("highest scores", 20, Display.getWidth() - 306, Display.getHeight()-24, 0, Color.GREEN, 0, 0, 0, false);
				for(int i=0; i<highestScores.size(); i++){
					String[] score = highestScores.get(i).split(" ");
					
					font.drawText(score[0], 16, Display.getWidth() - 306, Display.getHeight()-64 - i*16, 0, Color.WHITE, 0, 0, 0, false);
					font.drawText(score[1], 16, Display.getWidth() - 106, Display.getHeight()-64 - i*16, 0, Color.WHITE, 0, 0, 0, false);
				}
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			}
	}

}
