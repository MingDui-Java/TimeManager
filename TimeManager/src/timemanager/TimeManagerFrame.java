package timemanager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import notepad.FileIOUtil;
import notepad.NotepadPanel;

public class TimeManagerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653251764918939618L;

	public TimeManagerFrame() {
		super("TimeManager");
		FileIOUtil.ckeckDir();
		setLocation(100, 100);
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JTabbedPane jTabbedPane = new JTabbedPane();
		add(jTabbedPane);
		jTabbedPane.addTab("Notepad", NotepadPanel.getInstance());
		setVisible(true);
	}

	public static void main(String[] args) {
		new TimeManagerFrame();
	}
}
