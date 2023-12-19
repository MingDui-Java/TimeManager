package notepad;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NotepadPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6982642957521472039L;
	private static NotepadPanel instance = null;

	private JFrame parentFrame = null;

	private NotepadPanel() {
		parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
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

	public boolean openFile(FileInfo fileInfo) {
		if (FileManager.getInstance().findFileInfo(fileInfo.getFile()) == null) {
			return false;
		}
		showEditor();
		FileEditor.getInstance().openfile(fileInfo);
		return true;
	}

	public static NotepadPanel getInstance() {
		if (instance == null) {
			instance = new NotepadPanel();
		}
		return instance;
	}

	public FileInfo selectFile() { // cancel error
		JFileChooser fileChooser = new JFileChooser();
		// 设置文件过滤器
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TMNote 文件", "tmnote");
		fileChooser.setFileFilter(filter);
		// 设置只显示目录
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setCurrentDirectory(new File("./Note"));
		int result = fileChooser.showOpenDialog(parentFrame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (!selectedFile.exists() || !selectedFile.getName().toLowerCase().endsWith(".tmnote")) {
				return null;
			} else {
				FileManager.getInstance().importFile(selectedFile);
				return FileManager.getInstance().findFileInfo(selectedFile);
			}
		} else {
			return null;
		}
	}
}
