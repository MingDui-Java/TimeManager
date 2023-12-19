package Yang;
import Yang.AddButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author 杨智方
 */
public class GetTodoButtonPanel {
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
        DeleteButtonListener deleteButtonListener = new DeleteButtonListener(bigListPanel,listPanel,TodoListPanel,list);
        deleteButton.addActionListener(deleteButtonListener);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(downButton);
        return buttonPanel;
    }
}
