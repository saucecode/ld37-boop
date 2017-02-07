package net.saucecode.oneroom;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.saucecode.oneroom.util.FontTT;
import net.saucecode.oneroom.util.TextureLoader;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Main {

	static Map<String, Scene> scenes = new HashMap<String, Scene>();
	static Scene currentScene;
	private static Scene nextScene;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + File.separator + "natives");
		System.out.println("Starting game...");
		
		try{
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.create();
			Display.setTitle("boop!");
		}catch(LWJGLException e){
			System.out.println("LWJGL failed to initialize - Exiting...");
			System.exit(0);
		}
		
		loadResources();
		
		scenes.put("mainmenu", new SceneMainMenu("MainMenu"));
		currentScene = scenes.get("mainmenu");
		
		initGL2();
		
		currentScene.create();
		
		while(!Display.isCloseRequested() && !closeRequested){
			GL11.glLoadIdentity();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glColor3f(1,1,1);
			
			if(currentScene.sceneDone == true){
				System.out.println("Switching scenes to: " + nextScene.name);
				currentScene.destroy();
				nextScene.create();
				currentScene = nextScene;
			}
			
			currentScene.update();
			currentScene.render();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
		AL.destroy();
		System.exit(0);
	}
	
	private static void loadResources() {
		TextureLoader loader = new TextureLoader();
		try {
			Resources.textures.put("spritesheet", loader.getNMMTexture("spritesheet", ImageIO.read(new File("res" + File.separator + "spritesheet.png"))));
			Resources.textures.put("background", loader.getNMMTexture("background", ImageIO.read(new File("res" + File.separator + "bg.png"))));
			
			Resources.fonts.put("kenpixel", new FontTT(Font.createFont(Font.TRUETYPE_FONT, new File("res" + File.separator + "Harabara.ttf")), 32, 0));
			
			Resources.sounds.put("boop", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "short_boop.wav")));
			Resources.sounds.put("explosion1", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "explosion_1.wav")));
			Resources.sounds.put("explosion2", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "explosion_2.wav")));
			Resources.sounds.put("explosion3", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "explosion_3.wav")));
			Resources.sounds.put("hoopCapture", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "hoop.wav")));
			Resources.sounds.put("gameOver", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res" + File.separator + "failure.wav")));
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Loaded: " + Resources.textures.size() + " textures.");
		System.out.println("Loaded: " + Resources.fonts.size() + " fonts.");
	}

	private static void initGL2() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glEnable(GL11.GL_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void setNextScene(Scene scene){
		System.out.println("Setting next scene as: " + scene.name);
		nextScene = scene;
	}
}
