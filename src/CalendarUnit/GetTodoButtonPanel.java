/**
 * 日历模块
 */
package CalendarUnit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * GetTodoButtonPanel 类用于创建包含待办集中包含按钮的面板。
 * 这些按钮用于打开、收起、添加和删除待办事项列表。
 * @author 杨智方
 */
public class GetTodoButtonPanel {
    /**
     * 创建包含待办事项管理按钮的面板。
     *
     * @param listPanel 包含一个待办集中所有待办的面板
     * @param bigListPanel 包含待办集和其持有的待办的面板
     * @param TodoListPanel 最大的装在scrollPane里的面板
     * @param list 待办集的集合
     * @param toDoList 该按钮面板对应的待办集
     * @return 包含管理按钮的 JPanel
     */
    public static JPanel getTodoButtonPanel(JPanel listPanel, JPanel bigListPanel, Box TodoListPanel,ArrayList<ToDoList> list,ToDoList toDoList) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

        JButton downButton = new JButton("打开");
        JButton closeButton = new JButton("收起");
        DownButtonListener downButtonListener = new DownButtonListener(buttonPanel,closeButton,downButton,bigListPanel,listPanel);
        downButton.addActionListener(downButtonListener);

        CloseButtonListener closeButtonListener = new CloseButtonListener(buttonPanel,closeButton,downButton,bigListPanel,listPanel);
        closeButton.addActionListener(closeButtonListener);

        JButton addButton = new JButton("+");
        AddButtonListener addButtonListener = new AddButtonListener(buttonPanel,closeButton,downButton,bigListPanel,listPanel,TodoListPanel,toDoList);
        addButton.addActionListener(addButtonListener);

        JButton deleteButton = new JButton("删除");
        DeleteButtonListener deleteButtonListener = new DeleteButtonListener(bigListPanel,TodoListPanel,list);
        deleteButton.addActionListener(deleteButtonListener);

        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(downButton);
        return buttonPanel;
    }
}
