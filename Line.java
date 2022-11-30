package edu.du.cs.aliprava.socketpainter;

import java.awt.*;

public class Line extends PaintingPrimitive
{
	public Point start, end;
	public Line(Color c, Point s, Point e) 
	{
		super(c);
		this.start = s;
		this.end = e;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void drawGeometry(Graphics g)
	{
		// TODO Auto-generated method stub
		g.drawLine(start.x, start.y, end.x, end.y);
	}
}
