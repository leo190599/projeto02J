package com.estudo.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.estudo.world.ZaWarudo;

public class Menu {
	
	private String[] states;
	private int cur_state_index;
	private int max_state_index;
	
	public Menu()
	{
		states=new String[4];
		states[0]="Novo jogo";
		states[1]="salvar";
		states[2]="Carregar jogo";
		states[3]="Sair";
		cur_state_index=0;
		max_state_index=states.length-1;
	}

	public void tick()
	{
		
	}
	public void render(Graphics g)
	{
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("arial",Font.PLAIN,20));
		g.drawString("Joj", 100, 50);
		g.setFont(new Font("arial",Font.PLAIN,14));
		g.drawString(states[0], 80, 70);
		g.drawString(states[1], 90, 90);
		g.drawString(states[2], 70, 110);
		g.drawString(states[3], 100, 130);
		g.drawString(">", 60, 70+20*cur_state_index);
	}
	
	public void up()
	{
		cur_state_index--;
		if(cur_state_index<0)
		{
			cur_state_index=max_state_index;
		}
	}
	public void down()
	{
		cur_state_index++;
		if(cur_state_index>max_state_index)
		{
			cur_state_index=0;
		}
	}
	
	public void pause()
	{
		states[0]="Continuar";
		Game.state="menu";
	}
	
	
	public static String loadGame(int encode)
	{
		String line="";
		File file =new File("save.txt");
		if(file.exists())
		{
			try {
				BufferedReader reader=new BufferedReader(new FileReader("save.txt"));
				String singleLine=null;
				try {
					while((singleLine=reader.readLine())!=null)
					{
						String[] dividedLine=singleLine.split(":");
						char[] charLine=dividedLine[1].toCharArray();
						dividedLine[1]="";
						for(int i=0;i<charLine.length;i++)
						{
							charLine[i]-=encode;
							dividedLine[1]+=charLine[i];
						}
						line+=dividedLine[0];
						line+=":";
						line+=dividedLine[1];
						line+="/";
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return line;
	}
	
	public static void applySave(String fullLine)
	{
		String[] lines=fullLine.split("/");
		for(int i=0;i<lines.length;i++)
		{
			String[] parcialLine=lines[i].split(":");
			switch(parcialLine[0])
			{
			case "level":
				Game.restartGame(parcialLine[1]);
			break;
			}
		}
	}
	
	public static void saveGame(String[] index,int[] value,int encode)
	{
		File saveFile=new File("save.txt");
		if(saveFile.exists())
		{
			saveFile.delete();
		}
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter("save.txt"));
			for(int i=0;i<index.length;i++)
			{
				String current=index[i];
				current+=":";
				char[] valueC = Integer.toString(value[i]).toCharArray();
				for(int n=0;n<valueC.length;n++)
				{
					valueC[n]+=encode;
					current+=valueC[n];
				}
				writer.write(current);
				if(i<index.length-1)
				{
					writer.newLine();
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Game.state="is_running";
	}
	
	public void enter()
	{
		switch(states[cur_state_index])
		{
			case"Novo jogo":
				Game.state="is_running";
			break;
			case"Continuar":
				Game.state="is_running";
			break;
			case "salvar":
				String[] saveVariables= {"level"};
				int[] saveValues= {ZaWarudo.curWorld};
				int saveEncode=10;
				saveGame(saveVariables,saveValues,saveEncode);
			break;
			case "Carregar jogo":
				File testFile =new File("save.txt");
				if(testFile.exists())
				{
					String saver=loadGame(10);
					applySave(saver);
				}
			break;
			case"Sair":
				System.exit(1);
			break;
		}
	}
}
