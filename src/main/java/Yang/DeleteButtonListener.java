package main.java.Yang;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author 杨智方
 */
class DeleteButtonListener implements ActionListener, Serializable {
	private JPanel bigListPanel;
	private JPanel listPanel;
	private JPanel TodoListPanel;
	private ArrayList<ToDoList> list;

	public DeleteButtonListener(JPanel bigListPanel, JPanel listPanel, JPanel TodoListPanel, ArrayList<ToDoList> list) {
		this.bigListPanel = bigListPanel;
		this.listPanel = listPanel;
		this.TodoListPanel = TodoListPanel;
		this.list = list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame popupFrame = new JFrame("删除待办集");
		JPanel popupPanel = new JPanel();
		JLabel tipLabel = new JLabel("您确定要删除该待办集吗？");
		popupFrame.setVisible(true);
		JButton okButton = new JButton("确定");
		okButton1Listener okButton1Listener = new okButton1Listener(popupFrame, list, bigListPanel, TodoListPanel);
		okButton.addActionListener(okButton1Listener);
		popupPanel.setLayout(new FlowLayout());
		popupPanel.add(tipLabel);
		popupPanel.add(okButton);
		popupFrame.add(popupPanel);
		popupFrame.setSize(300, 100);
		int x = listPanel.getX() + (listPanel.getWidth() - popupFrame.getWidth()) / 2;
		int y = listPanel.getY() + (listPanel.getHeight() - popupFrame.getHeight()) / 2;
		popupFrame.setLocation(x, y);
		popupFrame.setVisible(true);
		okButton.dispatchEvent(new FocusEvent(okButton, FocusEvent.FOCUS_GAINED, true));
		okButton.requestFocusInWindow();
	}
}

class okButton1Listener implements ActionListener, Serializable {
	private JFrame popupFrame;
	private ArrayList<ToDoList> list;
	private JPanel bigListPanel;
	private JPanel TodoListPanel;

	public okButton1Listener(JFrame popupFrame, ArrayList<ToDoList> list, JPanel panel, JPanel todoListPanel) {

		this.popupFrame = popupFrame;
		this.list = list;
		bigListPanel = panel;
		TodoListPanel = todoListPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		popupFrame.dispose();
		list.removeIf(toDoList -> toDoList.getBigListPanel().equals(bigListPanel));
		TodoListPanel.remove(bigListPanel);
		TodoListPanel.revalidate();
		TodoListPanel.repaint();
	}

}
