package timemanager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import notepad.FileIOUtil;
import notepad.NotepadPanel;
import reminder.page.Review;

public class TimeManagerFrame extends JFrame {

	/**
	 * 
	 */
	public static TimeManagerFrame instance;

	private static final long serialVersionUID = 6653251764918939618L;
	private JTabbedPane jTabbedPane = null;

	public TimeManagerFrame() {
		super("TimeManager");
		FileIOUtil.ckeckDir();
		setLocation(400, 150);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		jTabbedPane = new JTabbedPane();
		add(jTabbedPane);
		jTabbedPane.addTab("Notepad", NotepadPanel.getInstance());
		try {
			jTabbedPane.addTab("Reminder", Review.getInstance().initialize());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		setVisible(true);
	}

	static public void showNotepad() {
		instance.jTabbedPane.setSelectedIndex(0);
	}

	static public void showReminder() {
		instance.jTabbedPane.setSelectedIndex(1);
	}

	public static void main(String[] args) {
		instance = new TimeManagerFrame();
//		timeManagerFrame.init();
	}
}
