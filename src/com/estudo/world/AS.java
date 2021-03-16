package com.estudo.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AS 
{

	public static double lastTime=System.currentTimeMillis();
	public static Comparator<Node> comparator= new Comparator<Node>() {
		@Override
		public int compare(Node n0,Node n1)
		{
			if(n0.fCost<n1.fCost)
			{
				return - 1;
			}
			if(n0.fCost>n1.fCost)
			{
				return + 1;
			}
			return 0;
		}
	};
	public static boolean clear()
	{
		if(System.currentTimeMillis()-lastTime>=1000)
		{
			return true;
		}
		return false;
	}
	private static boolean vecInList(List<Node> list,Vector2i vector)
	{
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).tile.equals(vector))
			{
				return true;
			}
		}
		return false;
	}
	
	private static double getDistance(Vector2i start,Vector2i end)
	{
		double dx=start.x-end.x;
		double dy=start.y-end.y;
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	public static Node getNode(List<Node> list,Vector2i vector)
	{
		for(int i=0;i<list.size();i++)
		{
			if(vector.equals(list.get(i).tile))
			{
				return list.get(i);
			}
		}
		return null;
	}
	
	public static List<Node> search(ZaWarudo world,Vector2i start,Vector2i end)
	{
		List<Node> openList=new ArrayList<Node>();
		List<Node> closedList=new ArrayList<Node>();
		Node current=new Node(start,null,0,getDistance(start,end));
		openList.add(current);
		while(openList.size()>0)
		{
			Collections.sort(openList,comparator);
			current=openList.get(0);
			openList.remove(current);
			if(current.tile.equals(end))
			{
				List<Node> pathList=new ArrayList<Node>();
				while(current!=null)
				{
					pathList.add(current);
					current=current.parent;
				}
				/*for(int z=0;z<pathList.size();z++)
				{
					System.out.println(pathList.get(z).tile.x+"           "+pathList.get(z).tile.y);
				}*/
				return pathList;
			}
			if(!vecInList(closedList,current.tile))
			{
				closedList.add(current);
			}
			else if(getNode(closedList,current.tile).fCost>current.fCost)
			{
				getNode(closedList,current.tile).parent=current.parent;
				getNode(closedList,current.tile).gCost=current.gCost;
				getNode(closedList,current.tile).fCost=current.fCost;
				continue;
			}
			for(int i=0;i<9;i++)
			{
				if(i==4)
				continue;
				int xi=i%3-1;
				int yi=i/3-1;
				System.out.println();
				System.out.println();
				System.out.println();

				/*for(int z=0;z<closedList.size();z++)
				{
					System.out.println(closedList.get(z).tile.x+"    "+closedList.get(z).tile.y+"     "+end.x+"     "+end.y);
				}*/
				if(world.tiles[current.tile.x+xi+(current.tile.y+yi)*world.w_width] instanceof Tile_wall)
					continue;
				//Consertar a detecção de tilest
				if(i==0)
				{
					if(world.tiles[current.tile.x+xi+1+(current.tile.y+yi)*world.w_width] instanceof Tile_wall||
							world.tiles[current.tile.x+xi+(current.tile.y+yi+1)*world.w_width]instanceof Tile_wall)
					continue;
				}
				else if(i==2)
					{
						if(world.tiles[current.tile.x+xi-1+(current.tile.y+yi)*world.w_width] instanceof Tile_wall||
							world.tiles[current.tile.x+xi+(current.tile.y+yi+1)*world.w_width]instanceof Tile_wall)
							continue;
					}
					else if(i==6)
						{
							if(world.tiles[current.tile.x+xi+1+(current.tile.y+yi)*world.w_width] instanceof Tile_wall||
							world.tiles[current.tile.x+xi+(current.tile.y+yi-1)*world.w_width]instanceof Tile_wall)
							continue;
						}
						else if(i==8)
						{
							if(world.tiles[current.tile.x+xi-1+(current.tile.y+yi)*world.w_width] instanceof Tile_wall||
							world.tiles[current.tile.x+xi+(current.tile.y+yi-1)*world.w_width]instanceof Tile_wall)
								continue;
						}
				Vector2i a=new Vector2i(current.tile.x+xi,current.tile.y+yi);
				double gCost=current.gCost+getDistance(current.tile,a);
				double hCost=getDistance(a,end);
				Node nextO=new Node(a,current,gCost,hCost);
				openList.add(nextO);
			}
		}
		return null;
	}	
}
