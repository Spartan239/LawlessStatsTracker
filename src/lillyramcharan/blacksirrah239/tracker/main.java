package lillyramcharan.blacksirrah239.tracker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.swing.JPanel;

public class main extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	
	/* Static variables */
	String szFont = "Arial";
	int iPort = 7777;
	int iTimeout = 5000;
	int aPort[] = {iPort & 0xFF, iPort >> 8 & 0xFF}; //Port bytes for packet
	
	/* Dynamic variables */
	int iWidth, iHeight; 
	String szHostIP;
	String szName = "Lawless Roleplay";
	String szIP = "IP: Unknown";
	String szLoc = "Server location: Unknown";
	String szPlayersOnline = "Players: Unknown";
	String szServerIP = "samp.lawlessrp.com";
	String szOnline = "Status: Offline";
	String[] szServerInfo = new String[7]; 
	String szPacket = "SAMP";
	char szIPAddr[] = new char[4];
	boolean socketConnected = false;
	DatagramSocket socket; //Later used with UDPConnect & UDPDisconnect
	
	public String appendstr(String szOld, String szAppend)
	{
		String szNew;
		szNew = szOld+szAppend;
		return szNew;
	}
	public String appendchar(String szOld, char szAppend)
	{
		String szNew;
		szNew = szOld+szAppend;
		return szNew;
	}
	public int ascii(char c)
	{
		int i;
		i = c;
		return i;
	}
	public char ascii(int i)
	{
		char c;
		c = (char) i;
		return c;
	}
	public boolean UDPConnect() throws IOException
	{
		socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(szServerIP);
		socket.connect(address, iPort);
	        
	    //byte[] bytepacket = szPacket.getBytes(Charset.forName("UTF-8"));
		socketConnected = true;
	    return true;
	}
	public boolean UDPDisconnect() throws IOException
	{
		socket.close();
		socket.disconnect();
		socketConnected = false;
		return true;
	}
	public boolean UDPSendByte(byte[] data, InetAddress addr)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, addr, 7777);
		packet.setData(data);
		packet.setAddress(addr);
		packet.setPort(iPort);
		return true;
	}
	public boolean UDPRecieveByte(DatagramPacket packet) throws IOException
	{
		socket.receive(packet);
		String recieved = new String(packet.getData(), 0, packet.getLength());
		return true;
	}
	main()
	{
		try 
		{
			InetAddress address = InetAddress.getByName(szServerIP);
			szIP = "IP: "+ address.getHostAddress()+":7777";
			szHostIP = address.getHostAddress();
			
			/* Converts the IP address into relative ASCII codes */
			if(szHostIP.contains("."))
			{
				char j;
				int k = 0;
				int len = 0;
				int per = 0;
				String tmp = "";
				String[] tmpstore = new String[4];
				for(int i = 0; i < szHostIP.length(); i++)
				{
					j = szHostIP.charAt(i);
					if(j == '.')
					{
						tmpstore[k] = "";
						tmpstore[k] = appendstr(tmpstore[k], tmp);
						tmp = "";
						k++;
						per++;
					}
					else
					{
						tmp = appendchar(tmp, j);
					//	System.out.println(tmp);
					}
					len++;
					if(len == szHostIP.length() && per != 4)
					{
						tmpstore[k] = "";
						tmpstore[k] = appendstr(tmpstore[k], tmp);
						tmp = "";
					}
				}
				for(int i = 0; i < tmpstore.length; i++)
				{
					if(tmpstore[i] != null)
					{
						int val = Integer.valueOf(tmpstore[i]);
						szIPAddr[i] = ascii(val);
					}
					else
					{
						System.out.println("IP " + i + " has returned a null value!");
					}
				}
				/* Appends all data into final packet */
				szPacket = appendchar(szPacket, szIPAddr[0]);
				szPacket = appendchar(szPacket, szIPAddr[1]);
				szPacket = appendchar(szPacket, szIPAddr[2]);
				szPacket = appendchar(szPacket, szIPAddr[3]);
				szPacket = appendchar(szPacket, ascii(aPort[0]));
				szPacket = appendchar(szPacket, ascii(aPort[1]));
				szPacket = appendstr(szPacket, "i");
				System.out.println("Packet: "+szPacket);
				/*System.out.println("Port Byte 1: "+aPort[0]);
				System.out.println("Port Byte 2: "+aPort[1]);
				System.out.println("Port Ascii 1: "+ascii(aPort[0]));
				System.out.println("Port Ascii 2: "+ascii(aPort[1]));
				System.out.println("ip: "+ szIPAddr[0] + " ip2: "+ szIPAddr[1] + " ip3: "+ szIPAddr[2]+ " ip4: "+ szIPAddr[3] + " port1:" + ascii(aPort[0]) + " port2: "+ ascii(aPort[1]));
				*/
			}
			//UDPConnect();
			//UDPDisconnect();
			DatagramSocket dataSocket = new DatagramSocket(7777);
			dataSocket.setSoTimeout(iTimeout);
			byte[] bytepacket = szPacket.getBytes(Charset.forName("UTF-8"));
			byte[] buf = new byte[512];
			DatagramPacket recpacket = new DatagramPacket(buf, buf.length);
			DatagramPacket packet = new DatagramPacket(bytepacket, bytepacket.length, address, 7777);
			packet.setData(bytepacket);
			packet.setAddress(address);
			packet.setPort(iPort);
			dataSocket.connect(address, iPort);
			socketConnected = true;
			System.out.println("Local port: " + dataSocket.getLocalPort());
			System.out.println("Local IP: " + dataSocket.getLocalAddress());
			System.out.println("Remote port: " + dataSocket.getPort());
			System.out.println("Remote IP: " + dataSocket.getInetAddress());
			System.out.println("Recieve Timeout: "+iTimeout+" milliseconds.");
			dataSocket.send(packet);
			System.out.println("Sent packet: "+ packet.getData());
			dataSocket.receive(packet);
			String recieved = new String(recpacket.getData(), 0, recpacket.getLength());
			System.out.println("Recieved packet: "+ recieved);
			dataSocket.close();
			dataSocket.disconnect();
			socketConnected = false;
			int iOnline, iTotalPlayers;
			iOnline = 0;
			iTotalPlayers = 0;
			szPlayersOnline = "Players: " + iOnline + "/" + iTotalPlayers;
			szOnline = "Status: Online";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void start()
	{
	}
	public void init()
	{
	}
	public void paint(Graphics g)
	{
		iWidth = getWidth();
		iHeight = getHeight();
		g.setFont(new Font(szFont, Font.PLAIN, 24));
		g.drawString("Lawless Roleplay", iWidth/4, 30);
		g.drawString(szOnline, iWidth/15, iHeight/3);
		g.drawString(szIP, iWidth/15, iHeight/2);
		g.drawString(szPlayersOnline, iWidth/15, iHeight/2 + iHeight/6);
		g.drawString(szLoc, iWidth/15, iHeight/2 + iHeight/3);
	}
	public void stop()
	{
	}

}
