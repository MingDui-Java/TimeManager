package Yang;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author 杨智方
 */
class DeleteButtonListener implements ActionListener, Serializable {
	private JPanel bigListPanel;
	private JPanel listPanel;
	private Box TodoListPanel;
	private ArrayList<ToDoList> list;

	public DeleteButtonListener(JPanel bigListPanel, JPanel listPanel, Box TodoListPanel, ArrayList<ToDoList> list) {
		this.bigListPanel = bigListPanel;
		this.listPanel = listPanel;
		this.TodoListPanel = TodoListPanel;
		this.list = list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] optionString = { "是", "否" };
		int result = JOptionPane.showOptionDialog(null, "您确定要删除该待办集吗？", "删除待办集", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, optionString, optionString[0]);
		if (result == JOptionPane.YES_OPTION) {
			list.removeIf(toDoList -> toDoList.getBigListPanel().equals(bigListPanel));
			TodoListPanel.remove(bigListPanel);
			TodoListPanel.revalidate();
			TodoListPanel.repaint();
		}
	}
}
