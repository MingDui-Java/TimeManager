package reminder.tool;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
/**
 * @author 86155
 **/
public class MouseInOut {
    public static void addColorChange(JLabel jl) {
        jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel but = (JLabel) e.getSource();						//鼠标指针指向标签
                but.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel but = (JLabel) e.getSource();						//鼠标指针离开标签
                but.setForeground(Color.BLACK);
            }
        });
    }
}
