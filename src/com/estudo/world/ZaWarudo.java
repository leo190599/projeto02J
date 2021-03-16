package com.estudo.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.estudo.entities.*;
import com.estudo.entities.Entities;
import com.estudo.main.Game;

public class ZaWarudo {
	
	public static BufferedImage floor_sprite=Game.sprite.getSprite(0, 0, 16, 16);
	public static BufferedImage wall_sprite=Game.sprite.getSprite(16,0,16,16);
	private int[] pixels;
	public static Tile[] tiles;
	public static int w_width,w_height;
	private static int tileSize=16;
	public static int curWorld=1;
	public static int maxWorld=2;
	public ZaWarudo(String path) {
		
		try {
			BufferedImage imagem=ImageIO.read(getClass().getResource(path));
			w_width=imagem.getWidth();
			w_height=imagem.getHeight();
			pixels=new int[imagem.getWidth()*imagem.getHeight()];
			tiles=new Tile[pixels.length];
			imagem.getRGB(0, 0, imagem.getWidth(), imagem.getHeight(), pixels, 0, imagem.getWidth());
				for(int xx=0;xx<imagem.getWidth();xx++)
				{
					for(int yy=0;yy<imagem.getHeight();yy++)
					{
						switch(pixels[xx+yy*imagem.getWidth()])
						{
							case 0xFF808080:
								tiles[xx+yy*imagem.getWidth()]=new Tile_wall(xx*16,yy*16,wall_sprite);
								break;
							case 0xFFFF0000:
								Enemy e=new Enemy(xx*16,yy*16,16,16);
								Game.entities.add(e);
								Game.enemies.add(e);
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);

							break;
							case 0xFFFFFFFF:
								Game.entities.add(new Health(xx*16,yy*16,16,16,Entities.health_im));
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);

							break;
							case 0xFF7F3300:
								Game.entities.add(new Gun(xx*16,yy*16,16,16,Entities.gun_im));
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);

							break;
							
							case 0xFFFFD800:
								Game.entities.add(new Bullet(xx*16,yy*16,16,16,Entities.bullet_im));
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);

							break;
							case 0xFF0026FF:
								Game.player.setx(xx*16);
								Game.player.sety(yy*16);
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);
							break;
							default:
								tiles[xx+yy*imagem.getWidth()]=new Tile_floor(xx*16,yy*16,floor_sprite);
								break;
						}
					}
				}
			}
		 catch (IOException e) {
			e.printStackTrace();
		}
		//randomizeWorld();
	}
	public void randomizeWorld()
	{
		Game.player.setx(32);
		Game.player.sety(32);
		for(int xx=0;xx<w_width;xx++)
		{
			for(int yy=0;yy<w_height;yy++)
			{
				tiles[xx+yy*w_width]=new Tile_wall(xx*16,yy*16,wall_sprite);
			}
		}
		int curTileXX=Game.player.getx()/16;
		int curTileYY=Game.player.gety()/16;
		for(int i=0;i<400;i++)
		{
			tiles[curTileXX+curTileYY*w_width]=new Tile_floor(curTileXX*16,curTileYY*16,floor_sprite);
			int nextTile=Game.rand.nextInt(100);
			if(nextTile<25 && curTileXX>1)
			{
				curTileXX--;
			}
			else if(nextTile<50 && curTileXX<w_width-2)
				{
					curTileXX++;
				}
				else if(nextTile<75 && curTileYY>1)
						{
							curTileYY--;
						}
					else if(curTileYY<w_height-2)
					{
						curTileYY++;
					}
		}
	}
	
	public static void generateParticles(int x, int y, int amount)
	{
		for(int number=0;number<=amount;number++)
		{
			Game.entities.add(new Particle(x,y,2,2,null));
		}
	}
	
	public static boolean isFree(int x,int y,int width,int height,int offsetX,int offsetY)
	{
		int xI=(x+offsetX)/tileSize;
		int yI=(y+offsetY)/tileSize;
		int xF=(x+width+offsetX)/tileSize;
		int yF=(y+height+offsetY)/tileSize;
		
		if(!(tiles[xI+yI*w_width] instanceof Tile_wall||
			 tiles[xF+yI*w_width] instanceof Tile_wall||
			 tiles[xI+yF*w_width] instanceof Tile_wall||
			 tiles[xF+yF*w_width] instanceof Tile_wall))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isFree(int x,int y)
	{
		int xI=(x+4)/tileSize;
		int yI=(y+3)/tileSize;
		int xF=(x+tileSize-5)/tileSize;
		int yF=(y+tileSize-2)/tileSize;
		
		if(!(tiles[xI+yI*w_width] instanceof Tile_wall||
			tiles[xF+yI*w_width]instanceof Tile_wall||
			tiles[xI+yF*w_width] instanceof Tile_wall||
			tiles[xF+yF*w_width] instanceof Tile_wall))
		{
			return true;
		}
		else if(Game.player.getZ()<0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static void desenhaMapa()
	{
		for(int xx=0;xx<w_width;xx++)
		{
			for(int yy=0;yy<w_height;yy++)
			{
					Game.minimapaPixels[xx+yy*w_width]=0x000000;
			}
		}
		
		for(int xx=0;xx<w_width;xx++)
		{
			for(int yy=0;yy<w_height;yy++)
			{
				if(tiles[xx+yy*w_width]instanceof Tile_wall)
				{
					Game.minimapaPixels[xx+yy*w_width]=0xFF0000;
				}
			}
		}
		int playerX=(Game.player.getx()+4)/16;
		int playerY=(Game.player.gety()+3)/16;
		Game.minimapaPixels[playerX+playerY*w_width]=0x0000FF;
		if(Game.enemies.size()>0) {
			for(int i=0;i<Game.enemies.size();i++)
			{
				int enemyX=(Game.enemies.get(i).getx()+4)/16;
				int enemyY=(Game.enemies.get(i).gety()+3)/16;
				Game.minimapaPixels[enemyX+enemyY*w_width]=0x00FF00;
			}
		}
	}
	
	public void render(Graphics g)
	{
		int initX=Camera.x>>4;
		int initY=Camera.y>>4;
		
		int finalX=initX+(Game.width>>4);
		int finalY=initY+(Game.height>>4);
		
		for(int xx=initX;xx<=finalX;xx++)
		{
			for(int yy=initY;yy<=finalY;yy++)
			{
				if(xx<0||xx>=w_width||yy<0||yy>=w_height)
					continue;
				Tile t=tiles[xx+yy*w_width];
				t.render(g);
			}
		}
	}
}
