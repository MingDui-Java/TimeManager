package main.java.Yang;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CalendarPanel extends JPanel {
	private final TodoManager todoManager;
	private MonthPanel monthPanel;
	private CardLayout cardLayout;

	public MonthPanel getMonthPanel() {
		return monthPanel;
	}

	public CalendarPanel() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		monthPanel = new MonthPanel();
		add(monthPanel, "MonthPanel");
		todoManager = new TodoManager();
	}

	public void showMonthPanel() {
		monthPanel.saveTodoMap();
		cardLayout.show(this, "MonthPanel");
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Java Calendar");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CalendarPanel calendarPanel = new CalendarPanel();

		frame.add(calendarPanel);
		frame.setVisible(true);

	}
}
