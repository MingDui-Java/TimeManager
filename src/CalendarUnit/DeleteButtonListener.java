/**
 * 日历模块
 */
package CalendarUnit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * DeleteButtonListener 类实现 ActionListener 接口，用于处理添加待办事项的动作。
 * 当用户点击删除按钮时，该待办集以及其持有的所有待办都将被删除
 * @author 杨智方
 */
class DeleteButtonListener implements ActionListener, Serializable {
	private JPanel bigListPanel;
	private Box TodoListPanel;
	private ArrayList<ToDoList> list;
	/**
	 * 实现删除按钮的功能
	 *
	 * @param bigListPanel 包含待办集和其持有的待办的面板
	 * @param TodoListPanel 最大的装在scrollPane里的面板
	 * @param list 待办集的集合
	 */
	public DeleteButtonListener(JPanel bigListPanel, Box TodoListPanel, ArrayList<ToDoList> list) {
		this.bigListPanel = bigListPanel;
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
