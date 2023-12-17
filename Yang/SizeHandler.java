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
        Component component = bigPanel.getParent();
        while(!(component instanceof JFrame) && component != null){
            component = component.getParent();
        }
        int newWidth = 0;
        if (component != null) {
            newWidth = component.getWidth();
        }
        int height = 40;
        // 更新面板的尺寸
//        panel.setMaximumSize(new Dimension(newWidth -  10, height));
//        smallPanel.setMaximumSize(new Dimension(newWidth -  10, height*50));
//        bigPanel.setMaximumSize(new Dimension(newWidth -  10,height*50));
//
//        panel.setMinimumSize(new Dimension(newWidth -  10, height));
//        smallPanel.setMinimumSize(new Dimension(newWidth -  10, 0));
//        bigPanel.setMinimumSize(new Dimension(newWidth -  10,0));

        panel.setPreferredSize(new Dimension(panel.getPreferredSize()));
        smallPanel.setPreferredSize(new Dimension(smallPanel.getPreferredSize()));
        bigPanel.setPreferredSize(new Dimension(bigPanel.getPreferredSize()));
        // 重新绘制面板

        bigPanel.getParent().revalidate();
        bigPanel.getParent().repaint();
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
}
