package Yang;

import javax.swing.*;
import java.awt.*;

public class CalendarPanel extends JPanel {
    private MonthPanel monthPanel;
    private CardLayout cardLayout;
    public CalendarPanel(){
        cardLayout=new CardLayout();
        setLayout(cardLayout);
        monthPanel = new MonthPanel();
        add(monthPanel,"MonthPanel");
    }
    public void showMonthPanel(){
        monthPanel.saveTodoMap();
        cardLayout.show(this, "MonthPanel");
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Calendar");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalendarPanel calendarPanel = new CalendarPanel();
        frame.add(calendarPanel);
        frame.setVisible(true);
    }
}
