package debug;
import java.awt.Color;
import java.awt.Graphics;

import com.estudo.world.Camera;
import com.estudo.world.Vector2i;

public class Debug {
	public static void paint(Vector2i inicio, Vector2i fim, Graphics g)
	{
		g.setColor(Color.black);
		g.drawLine(inicio.x+8-Camera.x, inicio.y+8-Camera.y, fim.x+8-Camera.x, fim.y+8-Camera.y);
	}
}
