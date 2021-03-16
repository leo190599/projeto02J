package com.estudo.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	
	private int x,y;
	private BufferedImage tile_image;
	public boolean show=false;
	
	public Tile(int x,int y, BufferedImage tile_image)
	{
		this.x=x;
		this.y=y;
		this.tile_image=tile_image;
	}

	public void render(Graphics g)
	{
		if(show)
		g.drawImage(tile_image, x-Camera.x, y-Camera.y, null);
	}
}
