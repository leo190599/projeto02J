package com.estudo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.estudo.main.Game;
import com.estudo.world.Camera;
import com.estudo.world.ZaWarudo;

public class BulletShoot extends Entities{

	private int speed=4;
	private int life=0,maxLife=50;
	private double dx,dy;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
		this.dx=dx;
		this.dy=dy;
	}

	public void tick()
	{
		x+=(int)(dx*speed);
		y+=(int)(dy*speed);
		life++;
		if(life==maxLife||!ZaWarudo.isFree(x, y, 3, 3, 0, 0))
		{
			Game.bullets.remove(this);
			ZaWarudo.generateParticles(this.x, this.y, 100);
		}
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.yellow);
		g.fillRect(this.getx()-Camera.x, this.gety()-Camera.y, 3, 3);
	}
}
