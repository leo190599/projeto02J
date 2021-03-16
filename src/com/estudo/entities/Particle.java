package com.estudo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.estudo.main.Game;
import com.estudo.world.Camera;

public class Particle extends Entities{

	private double dx=Game.rand.nextGaussian();
	private double dy=Game.rand.nextGaussian();
	private final double speed = 3;
	private int lifeTime=0;
	private final int maxLifeTime=10;
	
	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	
	public void tick()
	{
		x+=(int) dx*speed;
		y+=(int) dy*speed;
		lifeTime++;
		if(lifeTime>=maxLifeTime)
			Game.entities.remove(this);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.fillRect(x-Camera.x, y-Camera.y, width, height);
	}

}
