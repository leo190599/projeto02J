package com.estudo.entities;

import java.awt.image.BufferedImage;

public class Health extends Entities{

	public Health(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setMasks(4,9,-10,-10);
		this.depth=1;
	}
}
