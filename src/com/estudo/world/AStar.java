package com.estudo.world;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {
	public static double lastTime=System.currentTimeMillis();
	public static Comparator<Node> comparator= new Comparator<Node>() {
		@Override
		public int compare(Node n0,Node n1)
		{
			if(n0.fCost<n1.fCost)
			{
				return + 1;
			}
			if(n0.fCost>n1.fCost)
			{
				return - 1;
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
	
	public static List<Node> findPath(ZaWarudo world, Vector2i start,Vector2i end)
	{
		List<Node> openList=new ArrayList<Node>();
		List<Node> closedList=new ArrayList<Node>();
		Node current=new Node(start,null,0,getDistance(start,end));
		openList.add(current);
		while(openList.size()>0)
		{
			Collections.sort(openList, comparator);
			current=openList.get(0);
			if(current.tile.equals(end))
			{
				List<Node>path=new ArrayList<Node>();
				while(current.parent!=null)
				{
					path.add(current);
					current=current.parent;
				}
					openList.clear();
					closedList.clear();
					return path;
			}
			openList.remove(current);
			closedList.add(current);
			for(int i=0;i<9;i++)
			{
				if(i==4)
				continue;
				int x=current.tile.x;
				int y=current.tile.y;
				int xi=i%3-1;
				int yi=i/3-1;
				Tile tile= ZaWarudo.tiles[x+xi+(y+yi)*ZaWarudo.w_width];
				if(tile==null)
					continue;
				if(tile instanceof Tile_wall)
					continue;
				if(i==0)
				{	
					Tile test= ZaWarudo.tiles[x+xi+1+(y+yi)*ZaWarudo.w_width];
					Tile test2= ZaWarudo.tiles[x+xi+(y+yi+1)*ZaWarudo.w_width];
					if(test instanceof Tile_wall||test2 instanceof Tile_wall)
						continue;
				}
				if(i==2)
				{
					Tile test= ZaWarudo.tiles[x+xi-1+(y+yi)*ZaWarudo.w_width];
					Tile test2= ZaWarudo.tiles[x+xi+(y+yi+1)*ZaWarudo.w_width];
					if(test instanceof Tile_wall||test2 instanceof Tile_wall)
						continue;
				}
				if(i==6)
				{
					Tile test= ZaWarudo.tiles[x+xi+(y+yi-1)*ZaWarudo.w_width];
					Tile test2= ZaWarudo.tiles[x+xi+1+(y+yi)*ZaWarudo.w_width];
					if(test instanceof Tile_wall||test2 instanceof Tile_wall)
						continue;
				}
				if(i==8)
				{
					Tile test= ZaWarudo.tiles[x+xi+(y+yi-1)*ZaWarudo.w_width];
					Tile test2= ZaWarudo.tiles[x+xi-1+(y+yi)*ZaWarudo.w_width];
					if(test instanceof Tile_wall||test2 instanceof Tile_wall)
						continue;
				}
				Vector2i a=new Vector2i(x+xi,y+yi);
				double gCost=current.gCost+getDistance(current.tile,a);
				double hCost=getDistance(a,end);
				Node node=new Node(a,current,gCost,hCost);
				if(vecInList(closedList,a)&&gCost>=current.gCost)
				continue;
				if(!vecInList(openList,a))
					openList.add(node);
				else if(gCost<current.gCost)
				{
					openList.remove(current);
					openList.add(node);
				}
			}	
			System.out.println(openList.size());
		}
		closedList.clear();
		return null;
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
}
