package com.estudo.sons;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Som {
	
	private Clip musica;
	public static final Som hurt=new Som("res\\hurt.wav");
	public static final Som background=new Som("res\\y2mate.com - Action 52 - CheetahMen Theme_WZlYGN5W2Yg (online-audio-converter.com).wav");
	
	public  Som(String path)
	{
		try {
			File arquivo=new File(path);
			AudioInputStream audio=AudioSystem.getAudioInputStream(arquivo);//OutputStream "Entra na saida" e InputStream "Sai da entrada"
			musica=AudioSystem.getClip();
			musica.open(audio);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		musica.stop();
		musica.setFramePosition(0);
		musica.start();
	}
	
	public void continueP()
	{
		musica.start();
	}
	
	public void pause()
	{
		musica.stop();
	}
	
	public void loop()
	{
		musica.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
