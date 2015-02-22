package lillyramcharan.blacksirrah239.tracker;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * @author Jackson Harris
 *
 */
public class launch extends JFrame
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	
	static JFrame frame = new JFrame();
	static JPanel panel = new main();
	
	public static void main(String[] args)
	{
	//	setContentPane(new main());
		
		frame.setSize(300,300);
		frame.setVisible(true);
		frame.setTitle("Lawless Stat Tracker");
		frame.setContentPane(panel);
		
	}

}
