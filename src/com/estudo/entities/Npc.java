package com.estudo.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.estudo.main.Game;

public class Npc extends Entities{
	public String[] frases;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		frases=new String[2];
		frases[0]="Olaaaaaaaaaaaaaaaa";
		frases[1]="Destrua os inimigos";
	}
	
	public int curFraseIndex=0;
	public int curSubstringIndex=0;
	public int fraseTime=0;
	public int fraseMaxTime=15;
	public boolean renderedGraphics=false;
	public boolean renderGraphics=false;

	public void tick()
	{
		super.tick();
		if(pitagoras(this.getx(),Game.player.getx(),this.gety(),Game.player.gety())<50)
		{
			if(!renderedGraphics)
			{
				renderGraphics=true;
				if(fraseTime>=fraseMaxTime) {
					fraseTime=0;
					if(curSubstringIndex<frases[curFraseIndex].length())
					{
						curSubstringIndex++;
						System.out.println(curSubstringIndex+"   "+frases[curFraseIndex].length());
					}
					else
					{
						if(curFraseIndex+1<frases.length)
						{
							curSubstringIndex=0;
							curFraseIndex++;
						}
					}
				}
				fraseTime++;
				
			}
			else
			{
				renderGraphics=false;
			}
		}
		else
		{
			renderGraphics=false;
			renderedGraphics=false;
		}
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		if(renderGraphics)
		{
			g.setColor(Color.white);
			g.drawRect(8,8, Game.width-16, Game.height-16);
			g.setColor(Color.blue);
			g.fillRect(9,9, Game.width-17, Game.height-17);
			g.setColor(Color.white);
			g.setFont(new Font("arial",Font.PLAIN,12));
			g.drawString(frases[curFraseIndex].substring(0, curSubstringIndex), 30, 40);
			g.drawString(">Pressione enter para fechar<", 30, 60);
		}
	}
}
