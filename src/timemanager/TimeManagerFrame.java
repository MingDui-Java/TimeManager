package timemanager;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.ImageIcon;
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

/**
 * TimeManager 程序主窗体
 * <p>
 * 采用单例模式，包含日历，提醒事项，番茄钟，记事本，喝水提醒，更换主题六个主要功能模块
 * 
 * @author Aintme
 * @version 1.0
 * @see CalendarPanel
 * @see Review
 * @see TomatoPanel
 * @see NotepadPanel
 * @see DrinkWaterPanel
 * @see SkinChangePanel
 */
public class TimeManagerFrame extends JFrame {

	/**
	 * TimeManagerFrame类版本的标识符
	 */
	private static final long serialVersionUID = 6653251764918939618L;
	/**
	 * 程序主窗体单例
	 */
	public static TimeManagerFrame instance;
	/**
	 * 组织功能板块的标签面板
	 */
	private JTabbedPane jTabbedPane = null;

	/**
	 * 创建一个程序主窗体
	 */
	public TimeManagerFrame() {
		super("TimeManager");
		setLocation(400, 150);
		setSize(800, 600);
		setIconImage(new ImageIcon(TimeManagerFrame.class.getResource("/image/icon.png")).getImage());
		// 添加窗口关闭事件监听器
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				showNotepad();
				FileEditor.getInstance().closeFile();
				TaskPanel.saveTaskList();
				dispose(); // 关闭窗口
				System.exit(0);
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

	/**
	 * 显示日历板块
	 */
	static public void showCalendar() {
		instance.jTabbedPane.setSelectedIndex(0);
	}

	/**
	 * 显示提醒事项板块
	 */
	static public void showReminder() {
		instance.jTabbedPane.setSelectedIndex(1);
	}

	/**
	 * 显示番茄钟板块
	 */
	static public void showTomato() {
		instance.jTabbedPane.setSelectedIndex(2);
		TomatoPanel.stopTimer();
	}

	/**
	 * 显示记事本板块
	 */
	static public void showNotepad() {
		instance.jTabbedPane.setSelectedIndex(3);
	}

	/**
	 * 统一初始化程序字体
	 */
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

	/**
	 * 程序入口
	 */
	public static void main(String[] args) {
		FileIOUtil.ckeckDir();
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SkinSetting.setSkin();
		instance = new TimeManagerFrame();
	}
}
