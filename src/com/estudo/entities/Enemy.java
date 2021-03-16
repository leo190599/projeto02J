package com.estudo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.estudo.graficos.SpriteSheet;
import com.estudo.main.Game;
import com.estudo.sons.Som;
import com.estudo.world.AS;
import com.estudo.world.AStar;
import com.estudo.world.Camera;
import com.estudo.world.Node;
import com.estudo.world.Vector2i;
import com.estudo.world.ZaWarudo;

import debug.Debug;

public class Enemy extends Entities{

	int speed=1;
	public int rX=this.getx(),rY=this.gety(),rW=this.getWidth()-10,rH=this.getHeight()-7;
	public BufferedImage anim[];
	int frame=0;
	int maxFrame=15;
	int index=0;
	int maxIndex=1;
	private int life=2;
	private boolean isDamaged=false;
	private int damagedFrame=0,damagedMaxFrame=10;
	private List<Node> path;
	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height, null);
		depth=2;
		path=new ArrayList<Node>();
		// TODO Auto-generated constructor stub
		anim=new BufferedImage[2];
		anim[0]=Game.sprite.getSprite(112, 16, 16, 16);
		anim[1]=Game.sprite.getSprite(128, 16, 16, 16);
	}
	
	public void tick()
	{
		/*
		if(pitagoras(this.getx(),Game.player.getx(),this.gety(),Game.player.gety())<100)
		{
			if(this.isCollidingWithPlayer()==false)
			{
				rX=this.getx()+5;
				rY=this.gety()+6;
				if(Game.player.getx()>this.getx() && ZaWarudo.isFree(this.getx()+speed, this.gety() )&& !isColliding(rX+speed, rY))
				{
					this.setx(getx()+speed);
				}
				else if(Game.player.getx()<this.getx()&& ZaWarudo.isFree(this.getx()-speed, this.gety()) && !isColliding(rX-speed, rY))
				{
					this.setx(getx()-speed);
				}
		
				if(Game.player.y>this.gety()&& ZaWarudo.isFree(this.getx(), this.gety()+speed)  && !isColliding(rX, rY+speed))
				{
					this.sety(this.gety()+speed);
				}	
				else if(Game.player.y<this.gety()&& ZaWarudo.isFree(this.getx(), this.gety()-speed) && !isColliding(rX, rY-speed))
				{
					this.sety(this.gety()-speed);
				}
			}
			else
			{
				if(Game.player.vida>0)
				{
					if(Game.rand.nextInt(100)>89)
					{
						Game.player.vida--;
						Game.player.isDamaged=true;
						System.out.println("Vida: "+Game.player.vida);
					}
				}
				else
				{
					Game.state="game_over";
				}
			}
			
		}
		*/
		
		/*
			if(path.size()!=0)
			{
				//Os inimigos eventualmente vão travar disputando um mesmo tile, talvez futuramente checar se esse tile está ocupado
				rX=this.getx()+5;
				rY=this.gety()+6;
				if(path.get(path.size()-1).tile.x*16<this.x && !isColliding(rX-speed, rY))
				{
					x-=speed;
				}
				else if(path.get(path.size()-1).tile.x*16>this.x && !isColliding(rX+speed, rY))
				{
					x+=speed;
				}
			
				if(path.get(path.size()-1).tile.y*16<this.y  && !isColliding(rX, rY-speed))
				{
					y-=speed;
				}
				else if(path.get(path.size()-1).tile.y*16>this.y  && !isColliding(rX, rY+speed))
				{
					y+=speed;
				}
				//System.out.println(path.get(path.size()-1).tile.x+"     "+path.get(path.size()-1).tile.y+"   "+path.size());
				if(path.get(path.size()-1).tile.y*16==this.y && path.get(path.size()-1).tile.x*16==this.x)
				{
					path.remove(path.size()-1);
				}
			}
			else
			{
				Vector2i current=new Vector2i(this.x/16,this.y/16);
				path = AS.search(Game.world, current, Game.player.curTile);
			}
		*/
		frame++;
		if(frame>maxFrame)
		{
			frame=0;
			index++;
			if(index>maxIndex)
			{
				index=0;
			}
		}
		colisaoBala();
		if(life<=0)
		{
			Game.enemies.remove(this);
			Game.entities.remove(this);
		}
		animDamage();
	}
	
	public void animDamage()
	{
		damagedFrame++;
		if(damagedFrame>=damagedMaxFrame)
		{
			damagedFrame=0;
			isDamaged=false;
		}
	}
	
	public void colisaoBala()
	{
		for(int i=0;i<Game.bullets.size();i++)
		{
			BulletShoot b=Game.bullets.get(i);
			if(checkCollision(this,b))
			{
				isDamaged=true;
				life--;
				Som.hurt.play();
				Game.bullets.remove(b);
			}
		}
	}
	
	public boolean isColliding(int nextX, int nextY)
	{
		Rectangle rEnemy=new Rectangle(nextX,nextY,rW,rH);
		for(int i=0; i<Game.enemies.size();i++)
		{
			if(Game.enemies.get(i)==this)
				continue;
			Enemy e=Game.enemies.get(i);
			Rectangle rOther=new Rectangle(e.rX,e.rY,e.rW,e.rH);
			if(rEnemy.intersects(rOther))
			{
				//System.out.println("Colide");
				return true;
			}
		}
		return false;
	}
	public boolean isCollidingWithPlayer()
	{
		Rectangle r=new Rectangle(rX,rY,rW,rH);
		Rectangle pR=new Rectangle(Game.player.x,Game.player.y,Game.player.width,Game.player.height);
		if(r.intersects(pR)&& Game.player.getZ()==0)
		  {
			return true;
		  }
		return false;
	}
	public void render(Graphics g)
	{
		if(!(Camera.x-16>=this.x||Camera.y-16>=this.y||Camera.x+Game.width<=this.x||Camera.y+Game.height<=this.y))
		{
			if(!isDamaged)
			g.drawImage(anim[index], this.getx()-Camera.x, this.gety()-Camera.y, null);
			else
			g.drawImage(Entities.enemy_damaged, this.getx()-Camera.x, this.gety()-Camera.y, null);
		}
		/*
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(new Color(0,0,255,128));
		g2.drawRect(rX-Camera.x, rY-Camera.y, rW, rH);
		*/
	}
}
