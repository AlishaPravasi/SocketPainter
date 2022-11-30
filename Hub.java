package edu.du.cs.aliprava.socketpainter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import edu.du.cs.aliprava.socketpainter.Hub.HubThread;

public class Hub 
{
	public static ArrayList<Socket> sockets = new ArrayList<Socket>();
	public static ArrayList<Thread> threads = new ArrayList<Thread>();
	public static ArrayList<PaintingPrimitive> shapes = new ArrayList<PaintingPrimitive>();
	public static ArrayList<ObjectInputStream> ois = new ArrayList<ObjectInputStream>();
	public static ArrayList<ObjectOutputStream> oos = new ArrayList<ObjectOutputStream>();
	public static ArrayList<String> messages = new ArrayList<String>();
	public static Object temp;
	public static ObjectInputStream input;
	public static ObjectOutputStream output;

	public static void main(String[] args) 
	{
		Hub h = new Hub();
		try {
			ServerSocket ss = new ServerSocket(2345);
			while(!Thread.currentThread().isInterrupted())
			{
				sockets.add(ss.accept());
				output = new ObjectOutputStream(sockets.get(sockets.size()-1).getOutputStream());
				input = new ObjectInputStream(sockets.get(sockets.size()-1).getInputStream());
				ois.add(input);
				oos.add(output);
				Hub.HubThread innerObject = h.new HubThread(sockets.size()-1);
				output.writeObject(shapes);
				output.writeObject(messages);
				Thread th = new Thread(innerObject);
				th.start();
				threads.add(th);
			}
		} 
		catch(SocketException e)
		{
			Thread.currentThread().isInterrupted();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Thanks for painting!");
		}	
	}
	public class HubThread implements Runnable
	{
		private int sID;
		public HubThread(int sockID)
		{
			this.sID = sockID;
		}

		@Override
		public void run() 
		{
			while(true)
			{
				try {
					temp = ois.get(sID).readObject();
					if(temp instanceof PaintingPrimitive)
					{
						shapes.add((PaintingPrimitive) temp);
					}
					else
					{
						messages.add((String) temp);
					}
					sendToAll(temp);
				}
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				catch (IOException e)
				{
					System.out.println("Thanks for painting!");
					return;
				}
			}
			// TODO Auto-generated method stub
		}
		public synchronized void sendToAll(Object o)
		{
			for(int i = 0; i < sockets.size(); i++)
			{
				try 
				{	
					oos.get(i).writeObject(temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
