/**
 * 提醒事项和喝水提醒的工具包
 *
 * @author DdddM
 * @version 1.0
 */
package reminder.tool;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * 未输入时显示文字的文本框
 *
 * @author DdddM
 * @version 1.0
 **/
public class JTextFieldHintListener implements FocusListener {
    /**
     * 未输入时的提示字符串
     */
    private String hintText;
    /**
     * 文本框
     */
    private JTextField textField;
    /**
     * 未输入时显示文字的文本框的构造函数
     *
     * @param hintText 未输入时的提醒内容
     * @param jTextField 文本框
     */
    public JTextFieldHintListener(JTextField jTextField,String hintText) {
        this.textField = jTextField;
        this.hintText = hintText;
        jTextField.setText(hintText);  //默认直接显示
        jTextField.setForeground(Color.GRAY);
    }
    /**
     * 获取焦点时的动作
     *
     * @param e 焦点动作
     */
    @Override
    public void focusGained(FocusEvent e) {
        //获取焦点时，清空提示内容
        String temp = textField.getText();
        if(temp.equals(hintText)) {
            textField.setText("");
            textField.setForeground(Color.BLACK);
        }

    }
    /**
     * 失去焦点时的动作
     *
     * @param e 焦点动作
     */
    @Override
    public void focusLost(FocusEvent e) {
        //失去焦点时，没有输入内容，显示提示内容
        String temp = textField.getText();
        if("".equals(temp)) {
            textField.setForeground(Color.GRAY);
            textField.setText(hintText);
        }

    }
}