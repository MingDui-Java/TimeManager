package CalendarUnit;

import java.awt.CardLayout;

import javax.swing.*;
/**
 * CalendarPanel 类用于显示日历和待办事项。它继承自 JPanel 类，采用单例模式设计，确保在应用程序中只存在一个日历面板实例。
 */

public class CalendarPanel extends JPanel {
	/**
	 * 月份面板
	 */
	private MonthPanel monthPanel;
	/**
	 * 布局
	 */
	private CardLayout cardLayout;
	/**
	 * 日历面板的实例
	 */
	private static CalendarPanel calendarPanel;

	/**
	 * CalendarPanel 构造方法是私有的，以确保只能通过 getInstance() 方法获取实例。
	 * 创建日历面板并添加月份面板到卡片布局中。
	 */
	private CalendarPanel() {
		cardLayout = new CardLayout(); // 初始化卡片布局
		setLayout(cardLayout); // 设置布局为卡片布局
		monthPanel = new MonthPanel(); // 创建月份面板
		add(monthPanel, "MonthPanel"); // 将月份面板添加到卡片布局中，并指定名称为 "MonthPanel"
	}
	/**
	 * 获取 CalendarPanel 的实例。
	 *
	 * @return CalendarPanel 类的实例
	 */
	public static CalendarPanel getInstance() {
		if (calendarPanel == null) {
			calendarPanel = new CalendarPanel();
		}
		return calendarPanel;
	}
	/**
	 * 从提醒面板处接收待办事项的信息，并将其传递给月份面板。
	 *
	 * @param name  待办事项的名称
	 * @param year  年份
	 * @param month 月份
	 * @param day   日期
	 */
	public void receiveTodoFromTip(String name, int year, int month, int day) {
		monthPanel.receiveTodo("提醒：" + name, year, month, day);
	}
	/**
	 * 序列化
	 */
	public void saveTodoMap() {
		monthPanel.saveTodoMap();
	}
	/**
	 * 切换到日历面板。
	 */
	public void showMonthPanel() {
		cardLayout.show(this, "MonthPanel");
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Java Calendar");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CalendarPanel calendarPanel = CalendarPanel.getInstance();
		frame.add(calendarPanel);
		frame.setVisible(true);
	}
}
