package lillyramcharan.blacksirrah239.tracker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;

import javax.swing.JPanel;

public class main extends JPanel
{
	private static final long serialVersionUID = 1L;


	/* Static variables */
	String
		szFont = "Arial";

	int
		iPort = 7777,
		iUDPPort = 55056,
		iTimeout = 10000,
		aPort[] = {iPort & 0xFF, iPort >> 8 & 0xFF}; //Port bytes for packet

	/* Dynamic variables */
	int
		iWidth,
		iHeight,
		iOnlinePlayers,
		iMaxPlayers;

	boolean
		bPass,
		socketConnected = false;

	String
		szHostIP,
		szName = "Lawless Roleplay",
		szIP = "IP: Unknown",
		szLoc = "Server location: Unknown",
		szPlayersOnline = "Players: Unknown",
		szServerVer = "Server version: Unknown",
		szServerIP = "samp.lawlessrp.com",
		szOnline = "Status: Offline",
		szPass = "Password: "+bPass,
		szPacket = "SAMP";

	String[]
		szServerInfo = new String[7],
		szRetPacket =  new String[100];

	char szIPAddr[] = new char[4];

	DatagramSocket socket; //Later used with UDPConnect & UDPDisconnect

	main()
	{
		try
		{
			InetAddress address = InetAddress.getByName(szServerIP);
			InetAddress localaddr = InetAddress.getByName(RetrieveLocalExtIP());
			szIP = "IP: "+ address.getHostAddress()+":7777";
			szHostIP = address.getHostAddress();

			/* Converts the IP address into relative ASCII codes */
			if(szHostIP.contains("."))
			{
				char j;
				int
					k = 0,
					len = 0,
					per = 0;
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
			DatagramSocket dataSocket = new DatagramSocket(iPort);
			dataSocket.setSoTimeout(iTimeout);
			byte[] bytepacket = szPacket.getBytes(Charset.forName("US-ASCII"));
			byte[] buf = new byte[256];
			DatagramPacket recpacket = new DatagramPacket(buf, buf.length);
			DatagramPacket packet = new DatagramPacket(bytepacket, bytepacket.length, address, iPort);
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
			dataSocket.receive(recpacket);
			String recieved = new String(recpacket.getData(), 0, recpacket.getLength());
			System.out.println("Recieved packet: "+ recieved);
			dataSocket.close();
			dataSocket.disconnect();
			socketConnected = false;
			szRetPacket = DiscardBytes(SortRecievedPacket(recieved));
			FinalProcess(szRetPacket);
			szPlayersOnline = "Players: " + iOnlinePlayers + "/" + iMaxPlayers;
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
		g.drawString(szPass, iWidth/15, iHeight/2 + iHeight/3);
	}
	public void stop()
	{
	}

	public String RetrieveLocalExtIP()
	{
		BufferedReader in;
		try
		{
			URL dip = new URL("http://checkip.amazonaws.com/");
			in = new BufferedReader(new InputStreamReader(dip.openStream()));
			String ip = in.readLine();
			System.out.println(ip);
			return ip;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "NULL";
	}

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
		if(!socketConnected) {
			socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(szServerIP);
			socket.connect(address, iPort);

		    //byte[] bytepacket = szPacket.getBytes(Charset.forName("UTF-8"));
			socketConnected = true;
	    return true;
		}
		else
			return false;
	}

	public boolean UDPDisconnect() throws IOException
	{
		if(socketConnected) {
			socket.close();
			socket.disconnect();
			socketConnected = false;
			return true;
	}
	else
		return false;
	}

	public boolean UDPSendByte(byte[] data, InetAddress addr)
	{
			if(socketConnected) {
				DatagramPacket packet = new DatagramPacket(data, data.length, addr, 7777);
				packet.setData(data);
				packet.setAddress(addr);
				packet.setPort(iPort);
				return true;
		}
		else
			return false;
	}

	public boolean UDPRecieveByte(DatagramPacket packet) throws IOException
	{
		if(socketConnected) {
			socket.receive(packet);
			String recieved = new String(packet.getData(), 0, packet.getLength());
			return true;
	}
	else
		return false;

	}

	public String[] DiscardBytes(String[] str)
	{
		//remove first 11 bytes of data (header)
		String[] tmp = new String[str.length];
		String ftmp = "";
		for(int i = 0; i < str.length; i++)
		{
			if(i > 10)
				tmp[i] = str[i];
			else
			{
				str[i] = "";
				tmp[i] = "";
			}
		}
		for(int i = 0; i < tmp.length; i++)
		{
			ftmp = appendstr(ftmp, tmp[i]);
		}
		System.out.println("Final packet: "+ftmp);
		return tmp;
	}

	public String[] SortRecievedPacket(String packet)
	{
		char c;
		String[] str = new String[packet.length()];
		//sorts the string into an array
		for(int i = 0; i < packet.length(); i++)
		{
			c = packet.charAt(i);
			str[i] = appendchar(str[i], c);
		}
		return str;
	}
	public void FinalProcess(String[] str)
	{
		//String[] merge = new String[str.length];
		String string = "";
		char c;
		int pass = 0;
		int[]
			players = new int[2],
			maxplayers = new int[2];
		int j, k;

		for(int i = 0; i < str.length; i++)
			string = string+str[i];

		pass = ascii(string.charAt(0));
		if(pass == 1) bPass = true;
		else bPass = false;
		players[0] = ascii(string.charAt(1));
		players[1] = ascii(string.charAt(2));
		maxplayers[0] = ascii(string.charAt(3));
		maxplayers[1] = ascii(string.charAt(4));
		j = players[0];
		k = players[1];
		iOnlinePlayers = j + k;
		//System.out.println("Pass: "+pass);
		//System.out.println("Players: "+iOnlinePlayers);
		j = maxplayers[0];
		k = maxplayers[1];
		iMaxPlayers = j + k;
		//System.out.println("Max Players: "+iMaxPlayers);
	}

}
