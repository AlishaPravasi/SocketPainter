package edu.du.cs.aliprava.socketpainter;

import java.awt.*;
import java.io.*;

public abstract class PaintingPrimitive implements Serializable
{
	public Color color;
	
	public PaintingPrimitive(Color c)
	{
		this.color = c;
	}
	public final void draw(Graphics g) 
	{
	    g.setColor(this.color);
	    drawGeometry(g);
	}
	protected abstract void drawGeometry(Graphics g);
}
