/**
 * 日历模块
 */
package CalendarUnit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * MonthPanel 类用于显示月视图，并提供了与每一天相关联的待办事项列表。
 *
 * @author 杨智方
 * @version 1.0
 */
public class MonthPanel extends JPanel implements Serializable {
	/**
	 * 显示当前年月的标签
	 */
	private final JLabel monthLabel;
	/**
	 * 月视图面板
	 */
	private final JPanel monthPanel;
	/**
	 * 当前日历的克隆实例，便于遍历日历
	 */
	private final Calendar currentCalendarCopy;
	/**
	 * 存储年月日信息的Map
	 */
	private static Map<Integer, Map<Integer, Map<Integer, EachDay>>> secondPanelMapByYear;
	/**
	 * 用于序列化和反序列化的类
	 */
	private final TodoManager todoManager;
	/**
	 * MonthPanel 类的构造函数。初始化 MonthPanel 实例和相关变量。
	 */
	public MonthPanel() {
		setLayout(new BorderLayout());
		monthLabel = new JLabel("", JLabel.CENTER);
		JButton previousButton = new JButton("<<");
		JButton nextButton = new JButton(">>");
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentCalendarCopy.add(Calendar.MONTH, -1);
				updateCalendar();
			}
		});
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentCalendarCopy.add(Calendar.MONTH, 1);
				updateCalendar();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				updateCalendar();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(previousButton);
		buttonPanel.add(monthLabel);
		buttonPanel.add(nextButton);
		monthPanel = new JPanel(new GridLayout(7, 7));
		// 当前日历的实例
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendarCopy = Calendar.getInstance();

		secondPanelMapByYear = new HashMap<>();
		for (int year = currentCalendar.get(Calendar.YEAR) - 1; year <= currentCalendar.get(Calendar.YEAR)
				+ 1; year++) {
			Map<Integer, Map<Integer, EachDay>> monthMap = new HashMap<>();
			secondPanelMapByYear.put(year, monthMap);
			for (int month = 0; month < 12; month++) {
				Map<Integer, EachDay> dayMap = new HashMap<>();
				monthMap.put(month, dayMap);
			}
		}
		add(buttonPanel, BorderLayout.NORTH);
		add(monthPanel, BorderLayout.CENTER);
		todoManager = new TodoManager();
		updateCalendar(); // 更新日历
		loadTodoMap();
		updateCalendar();
	}
	/**
	 * 反序列化
	 */
	public void loadTodoMap() {
		todoManager.loadTodoMap(secondPanelMapByYear, currentCalendarCopy);
	}

	/**
	 * 序列化
	 */
	public void saveTodoMap() {
		todoManager.saveTodoMap(secondPanelMapByYear, currentCalendarCopy);
	}
	/**
	 * 更新日历
	 */
	public void updateCalendar() {
		int year = currentCalendarCopy.get(Calendar.YEAR);
		int month = currentCalendarCopy.get(Calendar.MONTH);
		LocalDateTime localDateTime = LocalDateTime.now();
		int day = localDateTime.getDayOfMonth();
		monthLabel.setText(year + "年 - " + (month + 1) + "月"); // 更新月份标签文字

		int daysInMonth = currentCalendarCopy.getActualMaximum(Calendar.DAY_OF_MONTH);

		currentCalendarCopy.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfWeek = currentCalendarCopy.get(Calendar.DAY_OF_WEEK);
		monthPanel.removeAll();
		monthPanel.setLayout(new GridLayout(7, 7));

		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		for (String weekDay : weekDays) {
			JLabel dayLabel = new JLabel(weekDay, JLabel.CENTER);
			monthPanel.add(dayLabel);
		}

		Calendar previousMonth = (Calendar) currentCalendarCopy.clone();
		previousMonth.add(Calendar.MONTH, -1);
		int daysInPreviousMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int startDayOfPreviousMonth = daysInPreviousMonth - (firstDayOfWeek - 2);

		for (int i = startDayOfPreviousMonth; i <= daysInPreviousMonth; i++) {
			JButton dayButton = new JButton(String.valueOf(i));
			dayButton.setEnabled(false);
			monthPanel.add(dayButton);
		}

		for (int i = 1; i <= daysInMonth; i++) {
			Map<Integer, EachDay> dayMap = secondPanelMapByYear.get(currentCalendarCopy.get(Calendar.YEAR))
					.get(currentCalendarCopy.get(Calendar.MONTH));
			EachDay eachDay = dayMap.getOrDefault(i, null);
			DayPanel panelForDay = null;
			if (eachDay != null) {
				panelForDay = eachDay.dayPanel;
			}
			if (panelForDay == null) {
				// 如果没有对应的 DayPanel，则创建一个并放入 secondPanelMapByYear
				panelForDay = new DayPanel();
				eachDay = new EachDay(new JButton(), panelForDay, 0, i);
				dayMap.put(i, eachDay);
			}
			int buttonNumber = panelForDay.getList().size();
			JButton dayButton = new JButton(String.format("<html>" + i
					+ "<br><center><font size=\"-5\"><span style='position:relative;'><span style='position:absolute; top:0; left:-5px; font-size:5px;'>&#9679;</span>%d</font></center></span></html>",
					buttonNumber));
			eachDay.setDayButton(dayButton);
			eachDay.setNum(buttonNumber);
			if (i == day) {
				eachDay.dayButton.setBackground(new Color(190, 190, 190));
				eachDay.dayButton.setOpaque(true);
			}
			monthPanel.add(dayButton);
			DayPanel finalPanelForDay = panelForDay;
			dayButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JPanel container = (JPanel) getParent();
					if (!finalPanelForDay.getList().isEmpty()) {
						finalPanelForDay.ToDoListPanel.removeAll();
						finalPanelForDay.add(finalPanelForDay.scrollPane, BorderLayout.CENTER);
						ArrayList<ToDoList> temp = new ArrayList<>(finalPanelForDay.list);
						for (ToDoList list : temp) {
							finalPanelForDay.list.remove(list);
							finalPanelForDay.createTodoList(list.getName());
							finalPanelForDay.createTodo(list);
						}
						finalPanelForDay.revalidate();
						finalPanelForDay.repaint();
					}
					container.add(finalPanelForDay, "DayPanel");
					CardLayout cardLayout = (CardLayout) container.getLayout();
					cardLayout.show(container, "DayPanel");
				}
			});
		}

		int remainingSpaces = 42 - (daysInMonth + firstDayOfWeek - 1);
		if (remainingSpaces > 7) {
			remainingSpaces -= 7;
			monthPanel.setLayout(new GridLayout(6, 7));
		}
		for (int i = 1; i <= remainingSpaces; i++) {
			JButton dayButton = new JButton(String.valueOf(i));
			dayButton.setEnabled(false);
			monthPanel.add(dayButton);
		}
		monthPanel.revalidate();
		monthPanel.repaint();
	}
	/**
	 * 从提醒事项处接受待办
	 * @param name 待办名称
	 * @param year 待办建立的年份
	 * @param month 待办建立的月份
	 * @param day 待办建立的日期
	 */
	public void receiveTodo(String name, int year, int month, int day) {
		Map<Integer, EachDay> dayMap = secondPanelMapByYear.get(year).get(month - 1);
		if (dayMap != null) {
			EachDay eachDay = dayMap.get(day);
			if (eachDay != null) {
				DayPanel panelForDay = eachDay.dayPanel;
				if (panelForDay != null) {
					panelForDay.createTodoList(name);
				} else {
					panelForDay = new DayPanel();
					eachDay = new EachDay(new JButton(), panelForDay, 0, day);
					dayMap.put(day, eachDay);
					panelForDay.createTodoList(name);
				}
				updateCalendar();
			}
		}
	}

}