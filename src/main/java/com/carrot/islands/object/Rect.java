package com.carrot.islands.object;

import java.util.ArrayList;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector2i;

public class Rect
{
	private UUID world;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	
	public Rect(UUID world, Vector2i point)
	{
		this(world, point.getX(), point.getX(), point.getY(), point.getY());
	}

	public Rect(Point a, Point b)
	{
		this(a.getWorld().getUniqueId(), Math.min(a.getX(), b.getX()), Math.max(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.max(a.getY(), b.getY()));
	}

	public Rect(UUID world, int minX, int maxX, int minY, int maxY)
	{
		this.world = world;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public UUID getWorld()
	{
		return world;
	}

	public void setWorld(UUID world)
	{
		this.world = world;
	}

	public int getMinX()
	{
		return minX;
	}

	public void setMinX(int minX)
	{
		this.minX = minX;
	}

	public int getMaxX()
	{
		return maxX;
	}

	public void setMaxX(int maxX)
	{
		this.maxX = maxX;
	}

	public int getMinY()
	{
		return minY;
	}

	public void setMinY(int minY)
	{
		this.minY = minY;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public void setMaxY(int maxY)
	{
		this.maxY = maxY;
	}

	public int width()
	{
		return maxX - minX + 1;
	}
	
	public int height()
	{
		return maxY - minY + 1;
	}
	
	public int size()
	{
		return width()*height();
	}
	
	public boolean isInside(Vector2i point)
	{
		return minX <= point.getX() && point.getX() <= maxX && minY <= point.getY() && point.getY() <= maxY;
	}

	public boolean isInside(Location<World> loc)
	{
		return loc.getExtent().getUniqueId().equals(world) && isInside(new Vector2i(loc.getBlockX(), loc.getBlockZ()));
	}
	
	public boolean intersects(Rect rect)
	{
		return this.minX <= rect.maxX && rect.minX <= this.maxX && this.minY <= rect.maxY && rect.minY <= this.maxY;
	}
	
	public ArrayList<Vector2i> pointsInside(Rect rect)
	{
		ArrayList<Vector2i> points = new ArrayList<Vector2i>();
		Vector2i A = new Vector2i(minX, minY);
		if (rect.isInside(A))
			points.add(A);
		Vector2i B = new Vector2i(minX, maxY);
		if (rect.isInside(B))
			points.add(B);
		Vector2i C = new Vector2i(maxX, minY);
		if (rect.isInside(C))
			points.add(C);
		Vector2i D = new Vector2i(maxX, maxY);
		if (rect.isInside(D))
			points.add(D);
		return points;
	}

}
