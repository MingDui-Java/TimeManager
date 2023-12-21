package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 采用单例模式的记事本编辑面板
 * <p>
 * 包含记事本编辑菜单栏单例和记事本文本编辑区域
 * 
 * @author Aintme
 * @version 1.0
 * @see EditorMenuBar
 * @see EditorArea
 */
public class FileEditor extends JPanel {

	/**
	 * FileEditor类版本的标识符
	 */
	private static final long serialVersionUID = -6982642957521472039L;
	/**
	 * 记事本编辑面板单例
	 */
	private static FileEditor instance = null;
	/**
	 * 记事本编辑菜单栏
	 */
	private EditorArea editorArea = null;
	/**
	 * 为记事本编辑区域添加的滚动条
	 */
	private JScrollPane scrollPane = null;
	/**
	 * 记录系统的滚轮事件，用于消除滚动和缩放功能的冲突
	 */
	private MouseWheelListener sysWheelListener = null;

	/**
	 * 创建一个记事本编辑面板实例
	 */
	private FileEditor() {
		setLayout(new BorderLayout());
		add(EditorMenuBar.getInstance(), BorderLayout.NORTH);
		setVisible(true);
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sysWheelListener = scrollPane.getMouseWheelListeners()[0];
		scrollPane.removeMouseWheelListener(sysWheelListener); // 移除系统默认滚轮事件防止缩放和滚动冲突
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
				if (mouseWheelEvent.isControlDown()) { // 通过调整字号实现缩放
					if (editorArea != null) {
						Font font = editorArea.getFont();
						if (mouseWheelEvent.getWheelRotation() < 0) { // 向前滚放大
							editorArea.setFont(new Font(font.getFamily(), font.getStyle(), font.getSize() + 1));
						} else {
							editorArea.setFont(new Font(font.getFamily(), font.getStyle(),
									font.getSize() - 1 >= 1 ? font.getSize() - 1 : 1));
						}
					}
				} else {
					scrollPane.addMouseWheelListener(sysWheelListener);
					sysWheelListener.mouseWheelMoved(mouseWheelEvent); // 触发系统滚动事件。
					scrollPane.removeMouseWheelListener(sysWheelListener);
				}
			}
		});
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * 获取唯一的记事本编辑面板实例
	 * 
	 * @return 唯一的记事本编辑面板实例
	 */
	public static FileEditor getInstance() {
		if (instance == null) {
			instance = new FileEditor();
		}
		return instance;
	}

	/**
	 * 新建一个新的记事本编辑区域
	 * 
	 * @see EditorArea
	 */
	public void newFile() {
		if (editorArea != null) { // 先remove防止新的实例被覆盖
			remove(editorArea);
		}
		editorArea = new EditorArea();
		scrollPane.setViewportView(editorArea);
		revalidate();
		repaint();
	}

	/**
	 * 尝试保存记事本编辑区域内容
	 * 
	 * @return 是否成功保存记事本编辑区域内容
	 */
	public boolean saveFile() {
		return editorArea.saveFile();
	}

	/**
	 * 关闭记事本编辑面板
	 * <p>
	 * 尝试关闭记事本编辑区域和记事本编辑操作栏，如果文件没有保存会提醒用户保存，如果成功关闭则显示文件管理面板
	 */
	public void closeFile() {
		if (editorArea != null) {
			if (editorArea.closeFile()) {
				FormatSetting.getInstance().setFontSize(editorArea.getFont().getSize());
				EditorMenuBar.getInstance().close();
				FileManager.getInstance().updateContent();
				NotepadPanel.getInstance().showManager();
				editorArea = null;
			}
		}
	}

	/**
	 * 通过文件信息创建一个记事本编辑区域
	 * 
	 * @param fileInfo 创建记事本编辑区域使用的文件信息
	 * @see EditorArea
	 */
	public void openfile(FileInfo fileInfo) {
		editorArea = new EditorArea(fileInfo);
		scrollPane.setViewportView(editorArea);
		revalidate();
		repaint();
	}

	/**
	 * 撤销操作
	 */
	public void undo() {
		editorArea.undo();
	}

	/**
	 * 重做操作
	 */
	public void redo() {
		editorArea.redo();
	}

	/**
	 * 剪切操作
	 */
	public void cut() {
		editorArea.cut();
	}

	/**
	 * 复制操作
	 */
	public void copy() {
		editorArea.copy();
	}

	/**
	 * 粘贴操作
	 */
	public void paste() {
		editorArea.paste();
	}

	/**
	 * 删除操作
	 */
	public void delete() {
		editorArea.replaceSelection("");
	}

	/**
	 * 全选操作
	 */
	public void selectAll() {
		editorArea.requestFocus();
		editorArea.selectAll();
	}

	/**
	 * 查找操作，显示查找和替换窗体
	 */
	public void showFind() {
		editorArea.showFind();
	}

	/**
	 * 设置文本是否自动换行
	 * 
	 * @param wrapped 文本是否自动换行
	 */
	public void setWrapped(boolean wrapped) {
		FormatSetting.getInstance().setWrapped(wrapped);
		if (editorArea != null) {
			editorArea.setLineWrap(wrapped);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}

	/**
	 * 设置文本编辑区域的背景颜色
	 * 
	 * @param backgroundColor 设定的背景颜色
	 */
	public void setBackgroundColor(Color backgroundColor) {
		FormatSetting.getInstance().setBackgroundColor(backgroundColor);
		if (editorArea != null) {
			editorArea.setBackground(backgroundColor);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}

	/**
	 * 设置文本编辑区域的字体颜色
	 * 
	 * @param foregroundColor 设定的字体颜色
	 */
	public void setForegroundColor(Color foregroundColor) {
		FormatSetting.getInstance().setForegroundColor(foregroundColor);
		if (editorArea != null) {
			editorArea.setForeground(foregroundColor);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}
}
