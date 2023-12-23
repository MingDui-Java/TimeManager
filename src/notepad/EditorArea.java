package notepad;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

/**
 * 记事本的文本编辑区域
 * 
 * @author Aintme
 * @version 1.0
 */
public class EditorArea extends JTextArea {
	/**
	 * EditorArea类版本的标识符
	 */
	private static final long serialVersionUID = -6354421493441903496L;
	/**
	 * 存储记事本内容的文件的信息
	 */
	private FileInfo fileInfo = null;
	/**
	 * 记事本内容
	 */
	private String contentSaved;
	/**
	 * 撤销管理器
	 */
	private UndoManager undoManager = new UndoManager();
	/**
	 * 记录上一次查找的位置
	 */
	private int index;
	/**
	 * 程序主窗体，用于显示查找和替换弹窗体
	 */
	private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
	/**
	 * 查找和替换窗体
	 */
	private JDialog findDialog = null;

	/**
	 * 通过文件信息创建一个记事本编辑区域
	 * 
	 * @param fileInfo 文件信息，用于初始化内容和保存位置
	 */
	public EditorArea(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		setLineWrap(FormatSetting.getInstance().getWrapped());
		setBackground(FormatSetting.getInstance().getBackgroundColor());
		setForeground(FormatSetting.getInstance().getForegroundColor());
		setFont(new Font("Diolag", 0, FormatSetting.getInstance().getFontSize()));
		contentSaved = FileIOUtil.loadFromFile(fileInfo.getPath());
		setText(contentSaved);
		undoManager.discardAllEdits(); // 清空撤销栈(内设字符串不需要撤销)
		setWrapStyleWord(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					EditorMenuBar.getInstance().showPopupMenu(e);
				}
			}
		});
		getDocument().addUndoableEditListener(undoManager);
		setVisible(true);
	}

	/**
	 * 创建一个新的记事本编辑区域
	 */
	public EditorArea() {
		setLineWrap(FormatSetting.getInstance().getWrapped());
		setBackground(FormatSetting.getInstance().getBackgroundColor());
		setForeground(FormatSetting.getInstance().getForegroundColor());
		setFont(new Font("Diolag", 0, FormatSetting.getInstance().getFontSize()));
		contentSaved = ""; // 新建文件内容为空
		setLineWrap(true);
		setWrapStyleWord(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					EditorMenuBar.getInstance().showPopupMenu(e);
				}
			}
		});
		getDocument().addUndoableEditListener(undoManager);
		setVisible(true);
	}

	/**
	 * 尝试保存记事本编辑内容
	 * 
	 * @return 是否成功保存编辑内容
	 */
	public boolean saveFile() {
		if (fileInfo == null) { // 新建
			File file = FileIOUtil.choosePath();
			if (file != null) {
				if (file.exists()) {
					int result = JOptionPane.showConfirmDialog(null, "文件已经存在，确认覆盖该文件吗?", "覆盖文件",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION) {
						return false;
					} else {
						fileInfo = FileManager.getInstance().findFileInfo(file);
					}
				} else {
					fileInfo = new FileInfo(file);
					FileManager.getInstance().addFileInfo(fileInfo);
					FileManager.getInstance().saveFileInfos();
				}
				contentSaved = getText();
				FileIOUtil.saveToFile(getText(), fileInfo.getPath());
				return true;
			}
			return false;
		} else {
			fileInfo.update();
			FileManager.getInstance().saveFileInfos();
			contentSaved = getText();
			FileIOUtil.saveToFile(getText(), fileInfo.getPath());
			return true;
		}
	}

	/**
	 * 尝试关闭记事本编辑区域
	 * <p>
	 * 尝试关闭记事本编辑区域，会自动关闭打开的查找窗体，如果文件没有保存会提醒用户保存
	 * 
	 * @return 是否成功关闭记事本编辑区域
	 */
	public boolean closeFile() {
		if (findDialog != null) {
			findDialog.setVisible(false);
		}
		if (!contentSaved.equals(getText())) {
			int result = JOptionPane.showConfirmDialog(null, "文件没有保存，是否保存文件?", "文件保存", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return true;
			}
			return saveFile();
		}
		return true;
	}

	/**
	 * 撤销操作
	 */
	public void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}

	/**
	 * 重做操作
	 */
	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}

	/**
	 * 显示查找和替换窗体
	 */
	public void showFind() {
		index = 0;
		if (findDialog == null) { // "懒汉"加载
			findDialog = new JDialog(parentFrame, "查找/替换", false);
			findDialog.setAlwaysOnTop(true);
			findDialog.setLayout(new FlowLayout());
			findDialog.add(new JLabel("查找:"));
			JTextField findField = new JTextField(12);
			findField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					index = 0;
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					index = 0;
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					index = 0;
				}
			});
			findDialog.add(findField);
			findDialog.add(new JLabel("替换:"));
			JTextField replaceField = new JTextField(12);
			findDialog.add(replaceField);
			JButton findButton = new JButton("查找下一个");
			findButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					find(findField.getText(), true);
				}
			});
			findDialog.add(findButton);
			JButton replaceButton = new JButton("替换");
			replaceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					replace(findField.getText(), replaceField.getText());
				}
			});
			findDialog.add(replaceButton);
			JButton findAndReplaceButton = new JButton("查找并替换");
			findAndReplaceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					find(findField.getText(), true);
					replace(findField.getText(), replaceField.getText());
				}
			});
			findDialog.add(findAndReplaceButton);
			JButton replaceAllButton = new JButton("全部替换");
			replaceAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					index = 0;
					int count = 0; // 记录替换的次数
					while (find(findField.getText(), false)) {
						count++;
						replace(findField.getText(), replaceField.getText());
					}
					JOptionPane.showMessageDialog(null, String.format("%d 处内容已经被替换", count), "替换",
							JOptionPane.INFORMATION_MESSAGE);
				}
			});
			findDialog.add(replaceAllButton);
			findDialog.setSize(250, 165);
			findDialog.setResizable(false);
			findDialog.setLocationRelativeTo(parentFrame);
		}
		findDialog.setVisible(true);
	}

	/**
	 * 查找文本编辑区域下一个指定字符串的位置
	 * 
	 * @param findText    需要查找的指定字符串
	 * @param showNotFind 如果不存在下一个指定字符串是否需要显示提示窗体
	 * @return 是否找到指定的字符串
	 */
	boolean find(String findText, boolean showNotFind) {
		requestFocus();
		String text = getText();
		index = text.indexOf(findText, index);
		if (index == -1) {
			if (showNotFind) {
				JOptionPane.showMessageDialog(null, "文本没有找到", "查找", JOptionPane.INFORMATION_MESSAGE);
			}
			index = 0;
			return false;
		} else {
			setSelectionStart(index);
			setSelectionEnd(index + findText.length());
			index += findText.length();
			return true;
		}
	}

	/**
	 * 将文本编辑区域中指定的字符串替换成另一个字符串
	 * 
	 * @param findText    查找的字符串
	 * @param replaceText 替换的字符串
	 */
	void replace(String findText, String replaceText) {
		// 通过检测文本编辑区域中选中的字符串是否和指定的字符串相同指定替换操作是否生效
		if (getSelectedText() != null && getSelectedText().equals(findText)) {
			replaceSelection(replaceText);
			index += replaceText.length() - findText.length(); // 修改index防止死循环
		}
	}
}
