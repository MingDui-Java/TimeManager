package Yang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;

public class SizeHandler implements ComponentListener, Serializable {
    ToDoList toDoList;

    public SizeHandler(ToDoList list) {
        toDoList = list;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JPanel bigPanel = toDoList.getBigListPanel();
        JPanel smallPanel = toDoList.getSmallListPanel();
        JPanel panel = toDoList.getListPanel();

        // 设置面板的最小大小和首选大小
        panel.setMinimumSize(new Dimension(panel.getWidth(), 40));
        panel.setPreferredSize(new Dimension(panel.getWidth(), 40));
        panel.setMaximumSize(new Dimension(panel.getWidth(), 40));

        // 重新验证和绘制面板
        panel.revalidate();
        panel.repaint();

        if (bigPanel.getParent() != null) {
            bigPanel.getParent().revalidate();
            bigPanel.getParent().repaint();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
