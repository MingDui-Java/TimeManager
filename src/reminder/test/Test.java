package reminder.test;

import reminder.page.DrinkWater;
import reminder.page.Review;

import javax.swing.*;

/**
 * @author 86155
 **/
public class Test extends JFrame {
    public static void main(String[] args) throws Exception {
        JFrame jf = new JFrame();
        jf.setBounds(400,150,800,600);
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //喝水提醒
        //jf.setContentPane(DrinkWater.getInstance().initialize());

        //复习提醒
        jf.setContentPane(Review.getInstance().initialize());

        jf.setVisible(true);
    }
}
