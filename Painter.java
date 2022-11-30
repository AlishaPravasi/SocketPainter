package edu.du.cs.aliprava.socketpainter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

import edu.du.cs.aliprava.socketpainter.Painter.ClientThread;

public class Painter extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	public Color c; 
	public PaintingPrimitive tempPP;
	public String tempS;
	public Point startPoint;
	public Point endPoint;
	public PaintingPanel centerPanel = new PaintingPanel();
	public JTextArea chat = new JTextArea(5, 20);
	public JTextArea inbox;
	public int status = -1;
	public Object o;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public String name = "";
	public Painter() 
	{
		try {
			Socket s = new Socket("localhost", 2345);
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			System.out.println("Connected");
			Painter.ClientThread innerObject = this.new ClientThread(s);
			Thread th = new Thread(innerObject);
			th.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel holder = new JPanel();
		holder.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,1));
		JButton circleButton = new JButton("Circle");
		JButton lineButton = new JButton("Line");
		circleButton.addActionListener(this);
		circleButton.setActionCommand("circle");
		lineButton.addActionListener(this);
		lineButton.setActionCommand("line");
		topPanel.add(lineButton, BorderLayout.EAST);
		topPanel.add(circleButton, BorderLayout.WEST);
		holder.add(topPanel, BorderLayout.NORTH);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 1)); 
		JButton redPaint = new JButton();
		redPaint.addActionListener(this);
		redPaint.setActionCommand("red");
		redPaint.setBackground(Color.RED);
		redPaint.setOpaque(true);
		redPaint.setBorderPainted(false);
		leftPanel.add(redPaint);
		JButton greenPaint = new JButton();
		greenPaint.addActionListener(this);
		greenPaint.setActionCommand("green");
		greenPaint.setBackground(Color.GREEN);
		greenPaint.setOpaque(true);
		greenPaint.setBorderPainted(false);
		leftPanel.add(greenPaint);
		JButton bluePaint = new JButton();
		bluePaint.addActionListener(this);
		bluePaint.setActionCommand("blue");
		bluePaint.setBackground(Color.BLUE);
		bluePaint.setOpaque(true);
		bluePaint.setBorderPainted(false);
		leftPanel.add(bluePaint);
		holder.add(leftPanel, BorderLayout.WEST);

		holder.add(centerPanel, BorderLayout.CENTER);
		centerPanel.addMouseListener(this);

		JPanel chatBox = new JPanel();
		chatBox.setLayout(new BorderLayout());
		chat.setBackground(Color.GRAY);
		inbox = new JTextArea(1,40);
		chatBox.add(inbox, BorderLayout.WEST);
		chatBox.add(chat, BorderLayout.CENTER);
		JScrollPane scrollBar = new JScrollPane(chat);
		chatBox.add(scrollBar, BorderLayout.NORTH);
		JButton sendMessage = new JButton("Send Message");
		sendMessage.addActionListener(this);
		sendMessage.setActionCommand("send");
		chatBox.add(sendMessage);
		holder.add(chatBox, BorderLayout.SOUTH);
		name = JOptionPane.showInputDialog("Enter your name");

		setContentPane(holder);
		setVisible(true);
		centerPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("red"))
		{
			status = 0;
			c = Color.red;
		}		
		if(e.getActionCommand().equals("blue"))
		{	
			status = 1;
			c = Color.blue;	
		}
		if(e.getActionCommand().equals("green"))
		{
			status = 2;
			c = Color.green;
		}
		if(e.getActionCommand().equals("send"))
		{
			if(!inbox.getText().equals(""))
			{
				String message = name + ": " + inbox.getText() + "\n";
				inbox.selectAll();
				inbox.replaceSelection("");
				try {
					this.oos.writeObject(message);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			status = 5;
		}
		if(e.getActionCommand().equals("line"))
		{
			status = 3;
		}
		if(e.getActionCommand().equals("circle"))
		{
			status = 4;
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		this.startPoint = e.getPoint();	
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		this.endPoint = e.getPoint();
		if(status == 3)
		{
			tempPP = new Line(c, this.startPoint, this.endPoint);
			this.centerPanel.addPrimitive(this.tempPP);
		}
		if(status == 4)
		{
			tempPP = new Circle(c, this.startPoint, this.endPoint);
			this.centerPanel.addPrimitive(this.tempPP);
		}
		this.centerPanel.repaint();
		try {
			if(status == 4 || status == 3)
			{
				oos.writeObject(tempPP);
			}
			else
			{
				oos.writeObject(tempS);
			}
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) 
	{
		Painter p = new Painter();
	}

	public class ClientThread implements Runnable 
	{
		private Socket sock;
		public ClientThread(Socket s)
		{
			this.sock = s;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true)
			{
				try {
					o = ois.readObject();
					if(o instanceof PaintingPrimitive)
					{
						centerPanel.addPrimitive((PaintingPrimitive) o);
						centerPanel.repaint();
					}
					else if(o instanceof String)
					{
						chat.append((String)o);
						chat.repaint();
					}
					else if((((ArrayList<Object>) o) instanceof ArrayList<Object>) && (o != null))
					{
						if(((ArrayList<Object>) o).size() > 0)
						{
							for (int i = 0; i < ((ArrayList)o).size(); i++)
							{
								if(((ArrayList<Object>)o).get(i) instanceof PaintingPrimitive && ((ArrayList<Object>)o).get(i) != null)
								{
									centerPanel.addPrimitive(((ArrayList<PaintingPrimitive>) o).get(i));		
									centerPanel.repaint();
								}
								else if(((ArrayList<Object>)o).get(i) instanceof String && ((ArrayList<Object>)o).get(i) != null)
								{	
									chat.append((String)((ArrayList<String>) o).get(i));
									chat.repaint();
								}
							}
						}

					}	

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
