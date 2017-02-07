package net.saucecode.oneroom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Particle extends Vector2f {

	private static final long serialVersionUID = -3725618871962749330L;
	
	Vector2f velocity = new Vector2f();
	ParticleConfig config;
	boolean destroy = false;
	
	public Particle(ParticleConfig config, float x, float y){
		super(x,y);
		this.config = config;
	}
	
	public Particle setVelocity(float vx, float vy){
		velocity.set(vx, vy);
		return this;
	}
	
	public void render(){
		Vector2f.add(velocity, Player.gravity, velocity);
		Vector2f.add(this, velocity, this);
		
		GL11.glColor3f(config.red, config.green, config.blue);
		Scene.renderBox(x - config.width/2, y - config.height/2, x + config.width/2, y + config.height/2);
	}
}
