package main.java.Yang;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author 杨智方
 */
// 实现 ActionListener 接口的具名类
class SetButtonListener implements ActionListener, Serializable {
	private JPanel listPanel;

	public SetButtonListener(JPanel listPanel) {
		this.listPanel = listPanel;
	}

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
		popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
		popupPanel.add(shortTime);
		popupPanel.add(longTime);
		popupPanel.add(okButton);
		popupFrame.add(popupPanel);
		popupFrame.setSize(300, 100);
		int x = listPanel.getX() + (listPanel.getWidth() - popupFrame.getWidth()) / 2;
		int y = listPanel.getY() + (listPanel.getHeight() - popupFrame.getHeight()) / 2;
		popupFrame.setLocation(x, y);
		popupFrame.setVisible(true);
		longTime.dispatchEvent(new FocusEvent(longTime, FocusEvent.FOCUS_GAINED, true));
		longTime.requestFocusInWindow();
		shortTime.dispatchEvent(new FocusEvent(shortTime, FocusEvent.FOCUS_GAINED, true));
		shortTime.requestFocusInWindow();
		okButton.dispatchEvent(new FocusEvent(okButton, FocusEvent.FOCUS_GAINED, true));
		okButton.requestFocusInWindow();

	}

	private static void getDigitText(JTextField time) {
		time.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}
}
