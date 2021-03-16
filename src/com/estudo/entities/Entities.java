package com.estudo.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import com.estudo.main.Game;
import com.estudo.world.Camera;
public class Entities {
	
	public static BufferedImage bullet_im=Game.sprite.getSprite(96, 0,16, 16);
	public static BufferedImage gun_im=Game.sprite.getSprite(80, 0,16, 16);
	public static BufferedImage health_im=Game.sprite.getSprite(112, 0,16, 16);
	public static BufferedImage r_gun=Game.sprite.getSprite(80, 0, 16, 16);
	public static BufferedImage l_gun=Game.sprite.getSprite(64, 0, 16, 16);
	public static BufferedImage enemy_damaged=Game.sprite.getSprite(144, 16, 16, 16);
	private BufferedImage sprite;
	protected int x;
	protected int y;
	protected int z=0;
	protected int width;
	protected int height;
	protected int maskX=0;
	protected int maskY=0;
	protected int maskWidth=0;
	protected int maskHeight=0;
	public int depth=0;
	
	public Entities(int x, int y, int width, int height,BufferedImage sprite)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.sprite=sprite;
	}
	public void setMasks(int maskX,int maskY, int maskWidth, int maskHeight)
	{
		this.maskX=maskX;
		this.maskY=maskY;
		this.maskWidth=maskWidth;
		this.maskHeight=maskHeight;
	}
	
	public void setx(int x)
	{
		this.x=x;
	}
	public void sety(int y)
	{
		this.y=y;
	}
	
	public int getx()
	{
		return x;
	}
	public int gety()
	{
		return y;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public void tick()
	{
		
	}
	
	public static Comparator<Entities> comparatorEntities=new Comparator<Entities>()
	{

		@Override
		public int compare(Entities e0, Entities e1) {
			if(e0.depth<e1.depth)
			{
				return +1;
			}
			if(e0.depth>e1.depth)
			{
				return -1;
			}
			return 0;
		}
		
	};
	
	public boolean checkCollision(Entities esse, Entities outro)
	{
		Rectangle eR=new Rectangle(esse.x+esse.maskX,esse.y+esse.maskY,esse.width+esse.maskWidth,esse.height+esse.maskHeight);
		Rectangle oR=new Rectangle(outro.x+outro.maskX,outro.y+outro.maskY,outro.width+outro.maskWidth,outro.height+outro.maskHeight);
		
		return eR.intersects(oR);
	}
	
	public void render(Graphics g)
	{
		if(!(Camera.x-16>=this.x||Camera.y-16>=this.y||Camera.x+Game.width<=this.x||Camera.y+Game.height<=this.y))
		{
			g.drawImage(sprite, this.getx()-Camera.x, this.gety()-Camera.y, null);
		}
	}
	
	public double pitagoras(double x1, double x2, double y1,double y2)
	{
		return Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
	}
	
	public void setZ(int z)
	{
		this.z=z;
	}
	
	public int getZ()
	{
		return z;
	}
}
