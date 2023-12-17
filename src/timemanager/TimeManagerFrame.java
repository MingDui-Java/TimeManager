package timemanager;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UnsupportedLookAndFeelException;

import View.TomatoPanel;
import Yang.CalendarPanel;
import notepad.FileIOUtil;
import notepad.NotepadPanel;
import reminder.page.DrinkWater;
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
		jTabbedPane.addTab("记事本", NotepadPanel.getInstance());
		try {
			jTabbedPane.addTab("提醒事项", Review.getInstance().initialize());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		jTabbedPane.add("Calendar", new CalendarPanel());
		jTabbedPane.add("Tomato", new TomatoPanel());
		try {
			jTabbedPane.add("喝水提醒", DrinkWater.getInstance().initialize());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		setVisible(true);
	}

	// 切换主页面显示的模块(静态方法)
	static public void showNotepad() {
		instance.jTabbedPane.setSelectedIndex(0);
	}

	static public void showReminder() {
		instance.jTabbedPane.setSelectedIndex(1);
	}

	static public void showCalendar() {
		instance.jTabbedPane.setSelectedIndex(2);
	}

	static public void showTomato() {
		instance.jTabbedPane.setSelectedIndex(3);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel"); // no10
//			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"); // no4
//			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel"); // no1
//			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel"); // no2
//			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel"); // no3
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		instance = new TimeManagerFrame();
	}
}
