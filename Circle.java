package edu.du.cs.aliprava.socketpainter;

import java.awt.*;

public class Circle extends PaintingPrimitive
{
	public Point center, radiusPoint;
	public Circle(Color c, Point cent, Point rad) 
	{
		super(c);
		this.center = cent;
		this.radiusPoint = rad;
		// TODO Auto-generated constructor stub
	}

	protected void drawGeometry(Graphics g) 
	{
       int radius = (int) Math.abs(center.distance(radiusPoint));
       g.drawOval(center.x - radius, center.y - radius, radius*2, radius*2);           
	}	
}
