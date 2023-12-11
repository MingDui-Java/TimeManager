package notepad;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class NotepadPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6982642957521472039L;
	private static NotepadPanel instance = null;

	private NotepadPanel() {
		setLayout(new BorderLayout());
		add(FileManager.getInstance(), BorderLayout.CENTER);
		setVisible(true);
	}

	public void showManager() {
		removeAll();
		add(FileManager.getInstance(), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	public void showEditor() {
		removeAll();
		add(FileEditor.getInstance(), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	public static NotepadPanel getInstance() {
		if (instance == null) {
			instance = new NotepadPanel();
		}
		return instance;
	}
}
