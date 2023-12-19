package timemanager;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import View.TaskPanel;
import View.TomatoPanel;
import Yang.CalendarPanel;
import notepad.FileEditor;
import notepad.FileIOUtil;
import notepad.NotepadPanel;
import reminder.page.DrinkWaterPanel;
import reminder.page.Review;
import skin.SkinChangePanel;
import skin.SkinSetting;

public class TimeManagerFrame extends JFrame {

	/**
	 * 
	 */
	public static TimeManagerFrame instance;

	private static final long serialVersionUID = 6653251764918939618L;
	private JTabbedPane jTabbedPane = null;

	public TimeManagerFrame() {
		super("TimeManager");
		setLocation(400, 150);
		setSize(800, 600);
		// 添加窗口关闭事件监听器
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				showNotepad();
				FileEditor.getInstance().closeFile();
				TaskPanel.saveTaskList();
				dispose(); // 关闭窗口
			}
		});
		InitGlobalFont(new Font("华文中宋", Font.PLAIN, 15));
		jTabbedPane = new JTabbedPane();
		add(jTabbedPane);
		try {
			jTabbedPane.addTab("日历", CalendarPanel.getInstance());
			jTabbedPane.addTab("提醒事项", Review.getInstance().initialize());
			jTabbedPane.addTab("番茄钟", new TomatoPanel());
			jTabbedPane.addTab("记事本", NotepadPanel.getInstance());
			jTabbedPane.addTab("喝水提醒", DrinkWaterPanel.getInstance().initialize());
			jTabbedPane.addTab("更换主题", SkinChangePanel.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

	// 切换主页面显示的模块(静态方法)
	static public void showCalendar() {
		instance.jTabbedPane.setSelectedIndex(0);
	}

	static public void showReminder() {
		instance.jTabbedPane.setSelectedIndex(1);
	}

	static public void showTomato() {
		instance.jTabbedPane.setSelectedIndex(2);
		TomatoPanel.stopTimer();
	}

	static public void showNotepad() {
		instance.jTabbedPane.setSelectedIndex(3);
	}

	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	public static void main(String[] args) {
		FileIOUtil.ckeckDir();
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SkinSetting.setSkin();
		instance = new TimeManagerFrame();
	}
}
