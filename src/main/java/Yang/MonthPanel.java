package main.java.Yang;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author 杨智方
 */
public class MonthPanel extends JPanel implements Serializable {
	private final JLabel monthLabel;
	private final JPanel monthPanel;
	private final Calendar currentCalendar;
	private final Calendar currentCalendarCopy;
	public Map<Integer, String> todoMap; // 使用 Map 将待办事项与日期关联
	private final Map<Integer, Map<Integer, Map<Integer, DayPanel>>> secondPanelMapByYear;
	private final TodoManager todoManager;

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
		currentCalendar = Calendar.getInstance();
		currentCalendarCopy = Calendar.getInstance();
		todoMap = new HashMap<>();

		secondPanelMapByYear = new HashMap<>();
		for (int year = currentCalendar.get(Calendar.YEAR) - 1; year <= currentCalendar.get(Calendar.YEAR)
				+ 1; year++) {
			Map<Integer, Map<Integer, DayPanel>> monthMap = new HashMap<>();
			secondPanelMapByYear.put(year, monthMap);
			for (int month = 0; month < 12; month++) {
				Map<Integer, DayPanel> dayMap = new HashMap<>();
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

	public void loadTodoMap() {
		todoManager.loadTodoMap(secondPanelMapByYear, currentCalendarCopy);
	}

	public void saveTodoMap() {
		todoManager.saveTodoMap(secondPanelMapByYear, currentCalendarCopy);
	}

	protected void onExit() {
		saveTodoMap();
	}

	public void updateCalendar() {
		int year = currentCalendarCopy.get(Calendar.YEAR);
		int month = currentCalendarCopy.get(Calendar.MONTH);
		int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
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
			Map<Integer, DayPanel> dayMap = secondPanelMapByYear.get(currentCalendarCopy.get(Calendar.YEAR))
					.get(currentCalendarCopy.get(Calendar.MONTH));
			DayPanel panelForDay = dayMap.getOrDefault(i, null);
			if (panelForDay == null) {
				// 如果没有对应的 DayPanel，则创建一个并放入 secondPanelMapByYear
				panelForDay = new DayPanel("您在当天还没有代办，点击右上角 + 号创建一个吧", 1);
				dayMap.put(i, panelForDay);
			}
			int buttonNumber = panelForDay.getList().size();
			JButton dayButton = new JButton(String.format("<html>" + i
					+ "<br><center><font size=\"-5\"><span style='position:relative;'><span style='position:absolute; top:0; left:-5px; font-size:5px;'>&#9679;</span>%d</font></center></span></html>",
					buttonNumber));
			EachDay dayPanel = new EachDay(dayButton, panelForDay, buttonNumber, i);
			if (i == day) {
				dayPanel.dayButton.setBackground(new Color(190, 190, 190));
				dayPanel.dayButton.setOpaque(true);
			}
			monthPanel.add(dayButton);
			DayPanel finalPanelForDay = panelForDay;
			dayButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JPanel container = (JPanel) getParent();
					if (!finalPanelForDay.getList().isEmpty()) {
						finalPanelForDay.remove(finalPanelForDay.hintPanel);
						finalPanelForDay.add(finalPanelForDay.scrollPane, BorderLayout.CENTER);
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

	public static void receiveTodoFromTips(String name, int year, int month, int day) {

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Java Calendar");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MonthPanel monthPanel = new MonthPanel();
		JPanel container = new JPanel(new CardLayout());
		System.out.println(container);
		container.add(monthPanel, "MonthPanel");
		frame.add(container);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				monthPanel.onExit();
				System.exit(0);
			}
		});
	}
}