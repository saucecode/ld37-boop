package net.saucecode.oneroom;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class FloatingText extends Vector2f {

	private static final long serialVersionUID = -2814297123055097802L;

	String text;
	SceneGame game;
	Color color;
	float fontSize;
	Vector2f velocity = new Vector2f();
	
	boolean destroy = false;
	
	public FloatingText(SceneGame parent, String text, float fontSize, Color color, float x, float y){
		super(x,y);
		this.text = text;
		this.game = parent;
		this.fontSize = fontSize + 4;
		this.color = color;
	}
	
	public void update(){
		Vector2f.add(velocity, Player.gravity, velocity);
		Vector2f.add(this, velocity, this);
		
		if(y < 0) destroy = true;
	}
	
	public void render(){
		game.font.drawText(text, fontSize, x, y, 0, color, 0, 0, 0, true);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
