package com.estudo.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.estudo.entities.BulletShoot;
import com.estudo.entities.Enemy;
import com.estudo.entities.Entities;
import com.estudo.entities.Npc;
import com.estudo.entities.Player;
import com.estudo.graficos.SpriteSheet;
import com.estudo.graficos.Ui;
import com.estudo.sons.Som;
import com.estudo.world.ZaWarudo;


public class Game  extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static int width=240,height=160,scale=3;
	public boolean isRunning=false;
	public Thread thread;
	public BufferedImage imagem;
	private BufferedImage sombra;
	public int pixels[];
	public int lightMap[];
	public BufferStrategy bs;
	public Graphics g;
	public static SpriteSheet sprite;
	public static List<Entities> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Player player;
	public static Npc npc;
	public static ZaWarudo world;
	public static Random rand;
	public Ui ui;
	public static String state="cutscene";
	private int maxFrame =30;
	private int curFrame=0;
	private boolean curAnimState=false;
	public Menu menu;
	public static BufferedImage minimapa;
	public static int minimapaPixels[];
	//private int offSetX=0,offSetY=0;
	//int mx,my;
	//public InputStream stream=ClassLoader.getSystemClassLoader().getResourceAsStream("aAtmospheric.ttf");
	//public Font fonte;
	
	public Game()
	{	
		rand=new Random();
		this.setPreferredSize(new Dimension(width*scale,height*scale));
		imagem=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		pixels=((DataBufferInt)imagem.getRaster().getDataBuffer()).getData();
		try {
			sombra=ImageIO.read(getClass().getResource("/Sombra.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lightMap=new int[sombra.getWidth()*sombra.getHeight()];
		sombra.getRGB(0, 0, sombra.getWidth(),sombra.getHeight(), lightMap, 0, sombra.getWidth());
		sombra.flush();
		sprite=new SpriteSheet("/spritesheet.png");
		ui=new Ui();
		bullets=new ArrayList<BulletShoot>();
		entities=new ArrayList<Entities>();
		enemies=new ArrayList<Enemy>();
		player=new Player(0, 0, 16, 16, sprite.getSprite(0, 16, 16, 16));
		npc=new Npc(128,32,16,16,sprite.getSprite(16, 32, 16, 16));
		
		player.setZ(-100);
		
		entities.add(player);
		entities.add(npc);
		world=new ZaWarudo("/Level1.png");
		minimapa=new BufferedImage(ZaWarudo.w_width,ZaWarudo.w_height,BufferedImage.TYPE_INT_RGB);
		minimapaPixels=((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
		/*
		try {
			fonte=Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(15f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		this.addMouseListener(this);
		this.addKeyListener(this);
		//this.addMouseMotionListener(this);
		menu=new Menu();
	}
	
	public void initFrame()
	{
		JFrame frame=new JFrame("JOj");
		//frame.setUndecorated(true);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		Image imagemT=null;
		BufferedImage imagemT2=null;
		try {
		imagemT=ImageIO.read(getClass().getResource("/Cursor.png"));
	   	imagemT2=ImageIO.read(getClass().getResource("/Level1.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
		Toolkit toolkit= Toolkit.getDefaultToolkit();
		Image image= toolkit.getImage(getClass().getResource("/Cursor.png"));
		Cursor c=toolkit.createCustomCursor(imagemT2,new Point(0,0) , "img");
		frame.setCursor(c);
		frame.setIconImage(imagemT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		//JFrame janela=new JFrame("Zelda clone");
		Game jogo=new Game();
		jogo.initFrame();
		jogo.start();
		//janela.add(jogo);
		//janela.pack();
		//janela.setResizable(false);
		//janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//janela.setLocationRelativeTo(null);
		//janela.setVisible(true);
		//jogo.start();
		
	}
	
	public synchronized void start()
	{
		isRunning=(true);
		thread=new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void tick()
	{
		if(state=="is_running")
		{
			for(int i=0;i<entities.size();i++)
			{
				Entities e=entities.get(i);
				e.tick();
			}
			for(int i=0;i<bullets.size();i++)
			{
				bullets.get(i).tick();
			}
			if(enemies.size()==0)
			{
				entities.clear();
				enemies.clear();
				player=new Player(0, 0, 16, 16, sprite.getSprite(0, 16, 16, 16));
				entities.add(player);
				bullets.clear();
				ZaWarudo.curWorld++;
				if(ZaWarudo.curWorld>ZaWarudo.maxWorld)
				{
					ZaWarudo.curWorld=1;
				}
				world=new ZaWarudo("/Level"+ZaWarudo.curWorld+".png");
				minimapa=new BufferedImage(ZaWarudo.w_width,ZaWarudo.w_height,BufferedImage.TYPE_INT_RGB);
				minimapaPixels=((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
			}
		}
		else if(state=="menu")
			{
			menu.tick();
			}
		else if(state=="cutscene")
		{
			if(player.getZ()!=0)
			{
				player.setZ(player.getZ()+1);
			}
			else
			state="is_running";
		}
		
	}
	
	/*
	public void retangulo(int sizeX, int sizeY,int xOffset, int yOffset)
	{
		for(int xx=0;xx<sizeX;xx++)
		{
			for(int yy=0;yy<sizeY;yy++)
			{
				if(xOffset+xx<0||yOffset+yy<0||xOffset+xx>=width||yOffset+yy>=height)
					continue;
				pixels[xx + (yOffset+yy)*width+xOffset]=0xFF0000;
			}
		}
	}
	*/
	public void renderizaSombra()
	{
		for(int xx=0;xx<Game.width;xx++)
		{
			for(int yy=0;yy<Game.height;yy++)
			{
				if(lightMap[xx+(yy*Game.width)]==0xFF000000)
				{
					pixels[xx+(yy*Game.width)]=0;
				}
			}
		}
	}
	public void render()
	{
		bs=this.getBufferStrategy();
		if(bs==null)
		{
			this.createBufferStrategy(3);
			return;
		}
		g=imagem.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, width, height);
		if(state!="menu")
		{
			Collections.sort(entities, Entities.comparatorEntities);
			world.render(g);
			for(int i=0;i<entities.size();i++)
			{
				Entities e =entities.get(i);
				e.render(g);
			}
			for(int i=0;i<bullets.size();i++)
			{
				bullets.get(i).render(g);
			}
			//renderizaSombra();
			ui.render(g);
			ZaWarudo.desenhaMapa();
			g.drawImage(minimapa, 170, 90, ZaWarudo.w_width*3, ZaWarudo.w_height*3, null);
			if(state=="game_over")
			{
				Graphics2D g2=(Graphics2D) g;
				g2.setColor(new Color(0,0,0,200));
				g2.fillRect(0, 0, width, height);
				g2.setColor(new Color(255,255,255));
				g2.setFont(new Font("arial",Font.PLAIN,14));
				g2.drawString("Game Over", width/2-40, height/2-10);
			
				if(curFrame>=maxFrame)
				{
					curFrame=0;
					curAnimState=!curAnimState;
				}
				curFrame++;
				if(curAnimState)
				{
					g2.setFont(new Font("arial",Font.PLAIN,9));
					g2.drawString(">Pressione enter para continuar<",  width/2-70, height/2+10);
				}
			}
			else if(state=="cutscene")
			{
				Graphics2D g2=(Graphics2D) g;
				g2.setColor(new Color(0,0,0));
				g2.setFont(new Font("arial",Font.PLAIN,14));
				g2.drawString("Prepare-se para jogar", width/2-55, height/2-10);
			
			}
		}
		else
		{
			menu.render(g);
			/*
			g.setColor(Color.red);
			g.setFont(fonte);
			g.drawString("aaaaa", 15, 15);
			*/
		}
		//retangulo(10,10,offSetX,offSetY);
		//offSetX--;
		g=bs.getDrawGraphics();
		g.drawImage(imagem, 0, 0, width*scale, height*scale, null);
		/*Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.red);
		g2.rotate(Math.atan2(my-125,mx-125)+Math.toRadians(45), 125, 125);
		g2.fillRect(100, 100,50, 50);
		*/
		bs.show();
		
	}
	
	public static void restartGame(String level)
	{
		Game.player=new Player(0, 0, 16, 16, sprite.getSprite(0, 16, 16, 16));
		Game.bullets.clear();
		Game.enemies.clear();
		Game.entities.clear();
		Game.entities.add(player);
		Game.world=new ZaWarudo("/Level"+level+".png");
		minimapa=new BufferedImage(ZaWarudo.w_width,ZaWarudo.w_height,BufferedImage.TYPE_INT_RGB);
		minimapaPixels=((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
		Game.state="is_running";
		ZaWarudo.curWorld=Integer.parseInt(level);
		return;
	}
	
	@Override
	public void run() {
		//Som.background.loop();
		requestFocus();
		long delta=0;
		long last_time=System.nanoTime();
		int fps=60;
		long ns=1000000000/fps;
		long current_time;
		int frame=0;
		double ct_f;
		double lt_f=System.currentTimeMillis();
		double delta_f=0;
		while(isRunning==true)
		{
			current_time=System.nanoTime();
			delta+=current_time-last_time;
			last_time=current_time;
			if(delta>=ns) 
			{
				delta=0;
				tick();
				render();
				frame++;
			}
			ct_f=System.currentTimeMillis();
			delta_f+=ct_f-lt_f;
			if(delta_f>=1000)
			{
				System.out.println(frame);
				frame=0;
				delta_f=0;
			}
			lt_f=ct_f;
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(Game.state)
		{
		 case"is_running":
			if(e.getKeyCode()==KeyEvent.VK_W||e.getKeyCode()==KeyEvent.VK_UP)
			{
			player.foward=true;
			}
			else if(e.getKeyCode()==KeyEvent.VK_S||e.getKeyCode()==KeyEvent.VK_DOWN)
			{
				player.back=true;
			}
			if(e.getKeyCode()==KeyEvent.VK_A||e.getKeyCode()==KeyEvent.VK_LEFT)
			{
				player.left=true;
			}
			else if(e.getKeyCode()==KeyEvent.VK_D||e.getKeyCode()==KeyEvent.VK_RIGHT)
				{
				player.right=true;
				}
			if(e.getKeyCode()==KeyEvent.VK_X)
			{
				player.isShooting=true;
			}
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
			{
				menu.pause();
			}
			if(e.getKeyCode()==KeyEvent.VK_SPACE)
			{
				player.jump();
			}
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				npc.renderedGraphics=true;
			}
			break;
		 case"menu":
			 if(e.getKeyCode()==KeyEvent.VK_W||e.getKeyCode()==KeyEvent.VK_UP)
			 {
				 menu.up();
			 }
			 else if(e.getKeyCode()==KeyEvent.VK_S||e.getKeyCode()==KeyEvent.VK_DOWN) 
			 		{
				 		menu.down();
			 		}
			 		else if(e.getKeyCode()==KeyEvent.VK_ENTER)
			 		{
			 			menu.enter();
			 		}
			 		else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
					{
						Game.state="is_running";
					}
		 break;
		 case "game_over":
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				Game.restartGame("1");
			}
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(Game.state)
		{
		 case"is_running":
		if(e.getKeyCode()==KeyEvent.VK_W||e.getKeyCode()==KeyEvent.VK_UP)
		{
			player.foward=false;
		}
		else if(e.getKeyCode()==KeyEvent.VK_S||e.getKeyCode()==KeyEvent.VK_DOWN)
			{
				player.back=false;
			}
		if(e.getKeyCode()==KeyEvent.VK_A||e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			player.left=false;
		}
		else if(e.getKeyCode()==KeyEvent.VK_D||e.getKeyCode()==KeyEvent.VK_RIGHT)
			{
				player.right=false;
			}
		break;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mX=(e.getX()/3);
		player.mY=(e.getY()/3);
		player.mouseShoot=true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//mx=e.getX();
		//my=e.getY();
	}
	
}
