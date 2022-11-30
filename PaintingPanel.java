package edu.du.cs.aliprava.socketpainter;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class PaintingPanel extends JPanel
{
	public ArrayList<PaintingPrimitive> primitives = new ArrayList<PaintingPrimitive>();
	//PaintingPrimitive obj = new PaintingPrimitive();
	public PaintingPanel()
	{
		setBackground(Color.WHITE);
	}
	public void addPrimitive(PaintingPrimitive obj) 
	{
        this.primitives.add(obj);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i = 0; i < primitives.size(); i++) 
		{  
			primitives.get(i).draw(g);
		}
	}
}
