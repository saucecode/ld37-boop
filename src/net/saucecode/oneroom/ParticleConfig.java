package net.saucecode.oneroom;

import java.awt.Color;

public class ParticleConfig {

	float red, green, blue;
	float width, height;
	
	SceneGame game;
	
	public ParticleConfig(SceneGame parent, float r, float g, float b, float w, float h){
		game = parent;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.width = w;
		this.height = h;
	}
	
	public ParticleConfig(float w, float h){
		this.width = w;
		this.height = h;
	}
	
	public void setColor(Color c){
		this.red = c.getRed();
		this.green = c.getGreen();
		this.blue = c.getBlue();
	}
}
