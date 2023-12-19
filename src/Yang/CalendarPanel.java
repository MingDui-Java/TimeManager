package Yang;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CalendarPanel extends JPanel {
	private MonthPanel monthPanel;
	private CardLayout cardLayout;
	private static CalendarPanel calendarPanel;

	private CalendarPanel() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		monthPanel = new MonthPanel();
		add(monthPanel, "MonthPanel");
	}

	public static CalendarPanel getInstance() {
		if (calendarPanel == null) {
			calendarPanel = new CalendarPanel();
		}
		return calendarPanel;
	}

	public void receiveTodoFromTip(String name, int year, int month, int day) {
		monthPanel.receiveTodo(name, year, month, day);
	}

	public void saveTodoMap() {
		monthPanel.saveTodoMap();
	}

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
