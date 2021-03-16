package com.estudo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.estudo.main.Game;
import com.estudo.world.Camera;
import com.estudo.world.Vector2i;
import com.estudo.world.ZaWarudo;

public class Player extends Entities{
	
	public boolean left=false,right=false, foward=false, back=false;
	private int speed=2;
	private BufferedImage[] animD;
	private BufferedImage[] animE;
	private BufferedImage[] animDamage;
	private int frame=0,maxFrame=6,index=0,maxIndex=3;
	public boolean dir=true,rightDir=true,leftDir=false,isMoving=false;
	public int vida=100;
	public int ammo=0;
	public boolean isDamaged=false;
	private int frameD=0,maxFrameD=8;
	private boolean hasGun=false;
	public boolean isShooting=false, mouseShoot=false;
	public double mX,mY;
	private boolean is_jumping=false;
	private boolean jumping_up=false;
	private int jumping_limit=-50;
	private int jump_vel=3;
	public Vector2i curTile;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth=1;
		animD= new BufferedImage[4];
		animE=new BufferedImage[4];
		animDamage= new BufferedImage[1];
		
		animDamage[0]=Game.sprite.getSprite(0, 32, 16, 16);
		for(int i=0;i<4;i++)
		{
			animD[i]= Game.sprite.getSprite(80-i*16, 16, 16, 16);
			animE[i]=Game.sprite.getSprite(32+i*16, 32, 16, 16);
		}
		this.setMasks(4, 3, -8, -4);
	}
	
	public void tick()
	{
		for(int r=0;r<9;r++)
		{
			int rX=r%3-1;
			int rY=r/3-1;
			ZaWarudo.tiles[(x+maskX)/16+rX+((rY+(maskY+y)/16)*ZaWarudo.w_width)].show=true;
		}
		Camera.x=Camera.clamp(this.x-(Game.width/2)+8,0, Game.world.w_width*16-Game.width);
		Camera.y=Camera.clamp(this.y-(Game.height/2)+8, 0, Game.world.w_height*16-Game.height);
		isMoving=false;
		if(left && ZaWarudo.isFree(x-speed,y))
		{			
			isMoving=true;
			x-=speed;
			dir=leftDir;
		}
		else if(right && ZaWarudo.isFree(x+speed,y))
			{
				isMoving=true;
				x+=speed;
				dir=rightDir;
			}
		
		if(foward && ZaWarudo.isFree(x,y - speed))
		{
			y-=speed;
			isMoving=true;
		}
		else if(back && ZaWarudo.isFree(x,y + speed))
			{
				y+=speed;
				isMoving=true;
			}
		if(isMoving)
		{
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
		}
		else
		{
			index=0;
			frame=0;
		}
		
		curTile=new Vector2i((this.x+4)>>4,(this.y+3)>>4);
		
		for(int i=0;i<Game.entities.size();i++)
		{
			Entities e =Game.entities.get(i);
			if(e instanceof Gun && checkCollision(this, e))
			{
				hasGun=true;
				Game.entities.remove(e);
			}
		}
		for(int i=0;i<Game.entities.size();i++)
		{
			Entities e= Game.entities.get(i);
			if(e instanceof Bullet && this.checkCollision(this, e))
			{
				ammo+=10;
				Game.entities.remove(e);
			}
		}
		
		for(int i=0;i<Game.entities.size();i++)
		{
			Entities e= Game.entities.get(i);
			if(e instanceof Health && this.checkCollision(this,e))
			{
				if(vida<=90)
				this.vida+=10;
				else
				vida=100;
				Game.entities.remove(e);
			}
		}
		if(isShooting==true && ammo>0)
		{
			isShooting=false;
			if(hasGun==true)
			{
				ammo--;
				int dx;
				int oX;
				if(dir) {
					dx=1;
					oX=16;
				}
				else
				{
					dx=-1;
					oX=-3;
				}
				Game.bullets.add(new BulletShoot(x+oX,y+9,3,3,null,dx,0));
			}
		}
		if(mouseShoot)
		{
			mouseShoot=false;
			if(hasGun && ammo>0)
			{
				ammo--;
				double angle= Math.atan2(mY-(y+8-Camera.y), mX-(x+8-Camera.x));
				if(dir)
				Game.bullets.add(new BulletShoot(x+15,y+9,3,3,null,Math.cos(angle),Math.sin(angle)));
				else
				Game.bullets.add(new BulletShoot(x-2,y+9,3,3,null,Math.cos(angle),Math.sin(angle)));
			}
		}
		if(is_jumping)
		{
			if(jumping_up)
			{
				z-=jump_vel;
				if(z<=jumping_limit)
				{
					jumping_up=false;
				}
			}
			else
			{
				z+=jump_vel;
			}
			if(z>=0)
			{
				z=0;
				is_jumping=false;
			}
		}
	}
	public void jump()
	{
		is_jumping=true;
		jumping_up=true;
		
	}
	public void render(Graphics g)
	{
		if(!isDamaged)
		{
			if(dir==rightDir)
			{
				g.drawImage(animD[index], x-Camera.x,y-Camera.y+z,null);
				if(hasGun)
				{
					g.drawImage(r_gun, x-Camera.x+2, y-Camera.y+2+z,null);
				}
			}
			else if(dir==leftDir)
			{
				g.drawImage(animE[index], x-Camera.x,y-Camera.y+z,null);
				if(hasGun)
				{
					g.drawImage(l_gun, x-Camera.x-2, y-Camera.y+2+z,null);
				}
			}
		}
		else
		{
			g.drawImage(animDamage[0], x-Camera.x,y-Camera.y+z,null);
			frameD++;
			if(frameD>=maxFrameD)
			{
				frameD=0;
				isDamaged=false;
			}
		}
		if(is_jumping)
		{
			g.setColor(Color.black);
			g.fillOval(x+4-Camera.x, y+10-Camera.y, 6, 6);
		}
			//g.drawLine(x-Camera.x+8, y-Camera.y+8,(int) mX,(int) mY);
		//g.setColor(Color.black);
		//g.drawRect(this.x+this.maskX-Camera.x,this.y+this.maskY-Camera.y, this.width+this.maskWidth, this.height+this.maskHeight);
	}
}
