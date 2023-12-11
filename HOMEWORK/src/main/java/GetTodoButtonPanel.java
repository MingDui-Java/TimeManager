import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GetTodoButtonPanel {
    public static ArrayList<ToDos> todos;
    public static JPanel getTodoButtonPanel(JPanel listPanel, JPanel bigListPanel, JPanel TodoListPanel) {
        todos = new ArrayList<>();
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton downButton = new JButton("打开");
        JButton closeButton = new JButton("收起");
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.remove(downButton);
                buttonPanel.add(closeButton);
                buttonPanel.revalidate();
                buttonPanel.repaint();
                bigListPanel.add(listPanel);
                bigListPanel.revalidate();
                bigListPanel.repaint();
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.remove(closeButton);
                buttonPanel.add(downButton);
                buttonPanel.revalidate();
                buttonPanel.repaint();
                bigListPanel.remove(listPanel);
                bigListPanel.revalidate();
                bigListPanel.repaint();
            }
        });
        JButton setButton = new JButton("设置");
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popupFrame = new JFrame("设置待办集");
                JPanel popupPanel = new JPanel();
                String DEFAULT_SHORT = "设置完成单个待办后休息时间";
                String DEFAULT_LONG = "设置完成待办集后的长休息时间";
                JTextField shortTime = GetTextField.getTextField(DEFAULT_SHORT);
                JTextField longTime = GetTextField.getTextField(DEFAULT_LONG);
                getDigitText(shortTime);
                getDigitText(longTime);
                JButton okButton = new JButton("确定");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (shortTime.getText().isEmpty() || longTime.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(popupFrame, "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            popupFrame.dispose();
                        }
                    }
                });
                popupPanel.setLayout(new BoxLayout(popupPanel,BoxLayout.Y_AXIS));
                popupPanel.add(shortTime);
                popupPanel.add(longTime);
                popupPanel.add(okButton);
                popupFrame.add(popupPanel);
                popupFrame.setSize(300, 100);
                int x = listPanel.getX() + (listPanel.getWidth() - popupFrame.getWidth()) / 2;
                int y = listPanel.getY() + (listPanel.getHeight() - popupFrame.getHeight()) / 2;
                popupFrame.setLocation(x, y);
                popupFrame.setVisible(true);
                longTime.dispatchEvent(new FocusEvent(longTime,FocusEvent.FOCUS_GAINED,true));
                longTime.requestFocusInWindow();
                shortTime.dispatchEvent(new FocusEvent(shortTime,FocusEvent.FOCUS_GAINED,true));
                shortTime.requestFocusInWindow();
                okButton.dispatchEvent(new FocusEvent(okButton,FocusEvent.FOCUS_GAINED,true));
                okButton.requestFocusInWindow();
            }
        });
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popupFrame = new JFrame("添加待办");
                JPanel popupPanel = new JPanel();
                String DEFAULT_STRING = "请输入待办名称";
                JTextField textField = GetTextField.getTextField(DEFAULT_STRING);
                JButton okButton = new JButton("√");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(popupFrame, "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            popupFrame.dispose();
                            buttonPanel.remove(downButton);
                            buttonPanel.add(closeButton);
                            for (ToDos toDoItem : todos) {// 判断是否存在相同名称待办
                                Component[] components = toDoItem.getPanel().getComponents();
                                for (Component component : components) {
                                    if (component instanceof JLabel label) {
                                        if (textField.getText().equals(label.getText())) {
                                            JFrame tip = new JFrame("提示");
                                            JLabel tipLabel = new JLabel("已存在相同待办");
                                            tip.add(tipLabel);
                                            tip.setSize(300, 100);
                                            int x = listPanel.getX() + (listPanel.getWidth() - tip.getWidth()) / 2;
                                            int y = listPanel.getY() + (listPanel.getHeight() - tip.getHeight()) / 2;
                                            tip.setLocation(x, y);
                                            tip.setVisible(true);
                                            return;
                                        }
                                    }
                                }
                            }
                            JPanel TodoPanel = new JPanel(new BorderLayout());
                            ToDos toDoItem = new ToDos(TodoPanel,false);
                            TodoPanel.setBackground(Color.BLUE);
                            TodoPanel.setMaximumSize(new Dimension(1800, 59));
                            JLabel userInputLabel = new JLabel(textField.getText());
                            userInputLabel.setForeground(Color.white);
                            userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
                            TodoPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
                            JButton buttonPanel = new JButton("开始");
                            TodoPanel.add(buttonPanel, BorderLayout.EAST); // 将按钮添加到待办的右侧
                            todos.add(toDoItem);//待办集里加入待办
                            listPanel.add(TodoPanel);
                            listPanel.revalidate();
                            listPanel.repaint();
                            bigListPanel.revalidate();
                            bigListPanel.repaint();
                            TodoListPanel.revalidate();
                            TodoListPanel.repaint();
                        }
                    }
                });
                popupPanel.setLayout(new FlowLayout());
                popupPanel.add(textField);
                popupPanel.add(okButton);
                popupFrame.add(popupPanel);
                popupFrame.setSize(300, 100);
                int x = listPanel.getX() + (listPanel.getWidth() - popupFrame.getWidth()) / 2;
                int y = listPanel.getY() + (listPanel.getHeight() - popupFrame.getHeight()) / 2;
                popupFrame.setLocation(x, y);
                popupFrame.setVisible(true);
                textField.dispatchEvent(new FocusEvent(textField,FocusEvent.FOCUS_GAINED,true));
                textField.requestFocusInWindow();
                okButton.dispatchEvent(new FocusEvent(okButton,FocusEvent.FOCUS_GAINED,true));
                okButton.requestFocusInWindow();
            }
        });
        buttonPanel.add(setButton);
        buttonPanel.add(addButton);
        buttonPanel.add(downButton);
        return buttonPanel;
    }
    public void setTodoFinished(boolean finished){
        todos.get(1).setFinished(finished);
    }

    private static void getDigitText(JTextField time) {
        time.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(!Character.isDigit(c)){
                    e.consume();
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

}
