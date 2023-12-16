package main.java.Yang;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author 杨智方
 */
public class ToDos implements Serializable {
	private boolean finished;
	private JPanel todoPanel;
	private JPanel buttonPanel;
	private JLabel label;

	public ToDos(JPanel todoPanel, boolean finished, JPanel buttonPanel, JLabel label) {
		this.todoPanel = todoPanel;
		this.finished = finished;
		this.buttonPanel = buttonPanel;
		this.label = label;
	}

	public void setFinished() {

		this.finished = true;
		this.label.setText("<html><s>" + label.getText() + "</s></html>");
	}

	public boolean isFinished() {
		return finished;
	}

	public JPanel getPanel() {
		return this.todoPanel;
	}

	public void setPanel(JPanel todoPanel) {
		this.todoPanel = todoPanel;
	}
}
