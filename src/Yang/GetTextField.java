package Yang;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * @author 杨智方
 */
public class GetTextField {
	public static JTextField getTextField(String string) {
		JTextField textField = new JTextField(15);
		textField.setText(string);
		textField.setForeground(Color.gray);
//        textField.setFont(new Font("宋体", Font.PLAIN, 12));
		textField.addFocusListener(new FocusListener() {
			boolean listTyped;

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().trim().isEmpty()) {
					listTyped = false;
					textField.setText(string);
					textField.setForeground(Color.gray);
				} else {
					listTyped = true;
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (!listTyped) {
					textField.setText("");
					textField.setForeground(Color.black);
				}
			}
		});
		return textField;
	}

	public static void getDigitText(JTextField time) {
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
