package com.estudo.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.estudo.main.Game;

public class Ui {
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.PLAIN,9));
		g.drawString("Vida: ",5,13);
		g.setColor(new Color(255,255,255,128));
		g.fillRect(28, 6, 104, 8);
		
		if(Game.player.vida>50)
		g.setColor(Color.green);
		else if(Game.player.vida>15)
			g.setColor(Color.yellow);
		else
		g.setColor(Color.red);
		g.fillRect(30, 8, Game.player.vida, 4);
		
		g.setColor(Color.white);
		g.drawString("Ammo: "+ Game.player.ammo, 195, 13);
	}
}