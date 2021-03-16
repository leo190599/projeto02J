package com.estudo.world;

public class Node {
	public Vector2i tile;
	public Node parent;
	public double fCost,gCost,hCost;
	
	public Node(Vector2i tile, Node parent,double gCost,double hCost)
	{
		this.tile=tile;
		this.parent=parent;
		this.gCost=gCost;//custo para chegar a esse tile
		this.hCost=hCost;//custo para chegar ao final
		this.fCost=gCost+hCost;//estimativa de custo total desse caminho para o fim
	}
}
