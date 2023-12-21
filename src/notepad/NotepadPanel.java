package notepad;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 采用单例模式的记事本面板
 * <p>
 * 包含记事本文件管理面板和记事本编辑面板
 * 
 * @author Aintme
 * @version 1.0
 */
public class NotepadPanel extends JPanel {

	/**
	 * NotepadPanel类版本的标识符
	 */
	private static final long serialVersionUID = -6982642957521472039L;
	/**
	 * 记事本面板单例
	 */
	private static NotepadPanel instance = null;
	/**
	 * 程序主窗体，用于显示查找和替换弹窗体
	 */
	private JFrame parentFrame = null;

	/**
	 * 创建一个记事本面板实例
	 */
	private NotepadPanel() {
		parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		setLayout(new BorderLayout());
		add(FileManager.getInstance(), BorderLayout.CENTER);
		setVisible(true);
	}

	/**
	 * 显示记事本文件管理面板
	 */
	public void showManager() {
		removeAll();
		add(FileManager.getInstance(), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	/**
	 * 显示记事本编辑面板
	 */
	public void showEditor() {
		removeAll();
		add(FileEditor.getInstance(), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	/**
	 * 尝试打开指定文件信息的编辑面板
	 * 
	 * @param fileInfo 指定的文件信息
	 * @return 是否成功打开编辑面板
	 */
	public boolean openFile(FileInfo fileInfo) {
		if (FileManager.getInstance().findFileInfo(fileInfo.getFile()) == null) {
			return false;
		}
		showEditor();
		FileEditor.getInstance().openfile(fileInfo);
		return true;
	}

	/**
	 * 获取唯一的记事本面板实例
	 * 
	 * @return 唯一的记事本面板实例
	 */
	public static NotepadPanel getInstance() {
		if (instance == null) {
			instance = new NotepadPanel();
		}
		return instance;
	}

	/**
	 * 打开资源管理器选择文件
	 * <p>
	 * 打开资源管理器让用户选择文件，资源管理器中只会显示文件夹和笔记文件
	 * 
	 * @return 用户选择文件的文件信息，如果未能获取文件信息返回null
	 */
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
