//package Yang;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;
//import java.io.Serializable;
//
//public class SizeHandler implements ComponentListener, Serializable {
//    ToDoList toDoList;
//    public SizeHandler(ToDoList list){
//        toDoList = list;
//    }
//
//    @Override
//    public void componentResized(ComponentEvent e) {
//        JPanel bigPanel = toDoList.getBigListPanel();
//        JPanel smallPanel = toDoList.getSmallListPanel();
//        JPanel panel = toDoList.getListPanel();
//        Component component = bigPanel.getParent();
//        while(!(component instanceof JFrame)){
//            component = component.getParent();
//        }
//        int newWidth = component.getWidth();
//        int height = 59;
//        // 更新面板的尺寸
//        panel.setMaximumSize(new Dimension(newWidth, height));
//        smallPanel.setMaximumSize(new Dimension(newWidth, height));
//        bigPanel.setMaximumSize(new Dimension(newWidth, height));
//
//        panel.setMinimumSize(new Dimension(newWidth, height));
//        smallPanel.setMinimumSize(new Dimension(newWidth, height));
//        bigPanel.setMinimumSize(new Dimension(newWidth, height));
//
//        panel.setPreferredSize(new Dimension(newWidth, height));
//        smallPanel.setPreferredSize(new Dimension(newWidth, height));
//        bigPanel.setPreferredSize(new Dimension(newWidth, height));
//        // 重新绘制面板
//        bigPanel.getParent().revalidate();
//        bigPanel.getParent().repaint();
//    }
//    @Override
//    public void componentMoved(ComponentEvent e) {}
//    @Override
//    public void componentShown(ComponentEvent e) {}
//    @Override
//    public void componentHidden(ComponentEvent e) {}
//}
