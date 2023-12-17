package Yang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;

public class SizeHandler implements ComponentListener, Serializable {
    ToDoList toDoList;
    public SizeHandler(ToDoList list){
        toDoList = list;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JPanel bigPanel = toDoList.getBigListPanel();
        JPanel smallPanel = toDoList.getSmallListPanel();
        JPanel panel = toDoList.getListPanel();
        panel.setPreferredSize(new Dimension(panel.getPreferredSize()));
        smallPanel.setPreferredSize(new Dimension(smallPanel.getPreferredSize()));
        bigPanel.revalidate();
        bigPanel.repaint();
        bigPanel.setPreferredSize(new Dimension(bigPanel.getPreferredSize()));
        // 重新绘制面板
        if(bigPanel.getParent()!=null){
            bigPanel.setPreferredSize(new Dimension(bigPanel.getPreferredSize()));
            bigPanel.getParent().revalidate();
            bigPanel.getParent().repaint();
        }
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
}
