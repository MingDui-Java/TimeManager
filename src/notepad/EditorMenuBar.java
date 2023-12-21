package notepad;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * 采用单例模式的记事本编辑菜单栏
 * 
 * @author Aintme
 * @version 1.0
 */
public class EditorMenuBar extends JMenuBar {

	/**
	 * EditorMenuBar类版本的标识符
	 */
	private static final long serialVersionUID = -3393661883314287039L;
	/**
	 * 记事本编辑菜单栏单例
	 */
	private static EditorMenuBar instance = null;
	/**
	 * 右键单击菜单栏
	 */
	private JPopupMenu popupMenu = new JPopupMenu();
	/**
	 * 是否开启文本自动换行的标志位
	 */
	private boolean wrapped;
	/**
	 * 颜色设置窗体
	 */
	private JDialog colorDialog = null;
	/**
	 * 显示背景颜色的纯色标签
	 */
	private ColoredRectangleLabel backgroundColorLabel = null;
	/**
	 * 显示字体颜色的纯色标签
	 */
	private ColoredRectangleLabel foregroundColorLabel = null;
	/**
	 * 程序主窗体，用于显示颜色设置窗体
	 */
	private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

	/**
	 * 创建一个记事本编辑菜单栏实例
	 */
	private EditorMenuBar() {
		wrapped = FormatSetting.getInstance().getWrapped();
		// 菜单栏和右键菜单共享的action
		AbstractAction saveAction = new AbstractAction() {
			private static final long serialVersionUID = 2402574325660633126L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().saveFile();
			}
		};
		AbstractAction exitAction = new AbstractAction() {
			private static final long serialVersionUID = -4341574190339003938L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().closeFile();
			}
		};
		AbstractAction undoAction = new AbstractAction() {
			private static final long serialVersionUID = -3616496559494939846L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().undo();
			}
		};
		AbstractAction redoAction = new AbstractAction() {
			private static final long serialVersionUID = 9139963662385263502L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().redo();
			}
		};
		AbstractAction cutAction = new AbstractAction() {
			private static final long serialVersionUID = 5669912265714252198L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().cut();
			}
		};
		AbstractAction copyAction = new AbstractAction() {
			private static final long serialVersionUID = 971999228614062691L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().copy();
			}
		};
		AbstractAction pasteAction = new AbstractAction() {
			private static final long serialVersionUID = -33674576953639635L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().paste();
			}
		};
		AbstractAction deleteAction = new AbstractAction() {
			private static final long serialVersionUID = 3536417234833226565L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().delete();
			}
		};
		AbstractAction selectAllAction = new AbstractAction() {
			private static final long serialVersionUID = 1707735573479116814L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().selectAll();
			}
		};
		AbstractAction findAction = new AbstractAction() {
			private static final long serialVersionUID = -378201178793528739L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().showFind();
			}
		};

		// file菜单栏
		JMenu fileMenu = new JMenu("文件");
		JMenuItem saveItem = new JMenuItem(saveAction);
		saveItem.setText("保存");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(saveItem);
		JMenuItem exitItem = new JMenuItem(exitAction);
		exitItem.setText("退出");
		fileMenu.add(exitItem);
		// edit菜单栏
		JMenu editMenu = new JMenu("编辑");
		JMenuItem undoItem = new JMenuItem(undoAction);
		undoItem.setText("撤销");
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(undoItem);
		JMenuItem redoItem = new JMenuItem(redoAction);
		redoItem.setText("重做");
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(redoItem);
		editMenu.addSeparator();
		JMenuItem cutItem = new JMenuItem(cutAction);
		cutItem.setText("剪切");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(cutItem);
		JMenuItem copyItem = new JMenuItem(copyAction);
		copyItem.setText("复制");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(copyItem);
		JMenuItem pasteItem = new JMenuItem(pasteAction);
		pasteItem.setText("粘贴");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(pasteItem);
		JMenuItem deleteItem = new JMenuItem(deleteAction);
		deleteItem.setText("删除");
		editMenu.add(deleteItem);
		editMenu.addSeparator();
		JMenuItem selectAllItem = new JMenuItem(selectAllAction);
		selectAllItem.setText("全选");
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(selectAllItem);
		JMenuItem findItem = new JMenuItem(findAction);
		findItem.setText("查找/替换");
		findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(findItem);
		// format菜单栏
		JMenu formatMenu = new JMenu("显示");
		JCheckBoxMenuItem wrapItem = new JCheckBoxMenuItem("自动换行");
		wrapItem.setSelected(wrapped);
		wrapItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wrapped = wrapItem.isSelected();
				FileEditor.getInstance().setWrapped(wrapped);
			}
		});
		formatMenu.add(wrapItem);
		JMenuItem colorItem = new JMenuItem("颜色设置");
		colorItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showColor();
			}
		});
		formatMenu.add(colorItem);

		// 创建工具栏
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false); // 设置工具栏不可浮动

		// 添加退出按钮
		JButton exitButton = new JButton(exitAction);
		exitButton.setText("退出");

		// 设置工具栏按钮的边框
		exitButton.setFocusPainted(false);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);

		// 添加工具栏按钮
		toolBar.add(exitButton);

		// 将菜单和工具栏添加到菜单栏
		add(fileMenu);
		add(editMenu);
		add(formatMenu);
		add(Box.createHorizontalGlue()); // 将工具栏放在右侧
		add(toolBar);

		// 右键菜单栏
		JMenuItem copySaveItem = new JMenuItem(saveAction);
		copySaveItem.setText("保存");
		popupMenu.add(copySaveItem);
		JMenuItem copyExitItem = new JMenuItem(exitAction);
		copyExitItem.setText("退出");
		popupMenu.add(copyExitItem);
		popupMenu.addSeparator();
		JMenuItem copyUndoItem = new JMenuItem(undoAction);
		copyUndoItem.setText("撤销");
		popupMenu.add(copyUndoItem);
		JMenuItem copyRedoItem = new JMenuItem(redoAction);
		copyRedoItem.setText("重做");
		popupMenu.add(copyRedoItem);
		popupMenu.addSeparator();
		JMenuItem copyCutItem = new JMenuItem(cutAction);
		copyCutItem.setText("剪切");
		popupMenu.add(copyCutItem);
		JMenuItem copyCopyItem = new JMenuItem(copyAction);
		copyCopyItem.setText("复制");
		popupMenu.add(copyCopyItem);
		JMenuItem copyPasteItem = new JMenuItem(pasteAction);
		copyPasteItem.setText("粘贴");
		popupMenu.add(copyPasteItem);
		JMenuItem copyDeleteItem = new JMenuItem(deleteAction);
		copyDeleteItem.setText("删除");
		popupMenu.add(copyDeleteItem);
		popupMenu.addSeparator();
		JMenuItem copySelectAllItem = new JMenuItem(selectAllAction);
		copySelectAllItem.setText("全选");
		popupMenu.add(copySelectAllItem);
		JMenuItem copyFindItem = new JMenuItem(findAction);
		copyFindItem.setText("查找");
		popupMenu.add(copyFindItem);
	}

	/**
	 * 获取唯一的记事本编辑菜单栏实例
	 * 
	 * @return 唯一的记事本编辑菜单栏实例
	 */
	public static EditorMenuBar getInstance() {
		if (instance == null) { // "懒汉"加载
			instance = new EditorMenuBar();
		}
		return instance;
	}

	/**
	 * 显示右键菜单
	 * 
	 * @param mouseEvent 右键单击时的鼠标事件，用于确定右键菜单的显示位置
	 */
	public void showPopupMenu(MouseEvent mouseEvent) {
		popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * 显示颜色设置窗体
	 */
	public void showColor() {
		if (colorDialog == null) { // "懒汉"加载
			colorDialog = new JDialog(parentFrame, "颜色", false);
//			colorDialog.setAlwaysOnTop(true);
			colorDialog.setLayout(new FlowLayout());
			colorDialog.add(new JLabel("背景颜色:"));
			backgroundColorLabel = new ColoredRectangleLabel(FormatSetting.getInstance().getBackgroundColor(), 10, 10);
			colorDialog.add(backgroundColorLabel);
			JButton backgroundButton = new JButton("更改");
			backgroundButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color selectedColor = JColorChooser.showDialog(parentFrame, "更改背景颜色",
							backgroundColorLabel.getColor());
					if (selectedColor != null) {
						backgroundColorLabel.setColor(selectedColor);
						FileEditor.getInstance().setBackgroundColor(selectedColor);
					}
				}
			});
			colorDialog.add(backgroundColorLabel);
			colorDialog.add(backgroundButton);
			colorDialog.add(new JLabel("字体颜色:"));
			foregroundColorLabel = new ColoredRectangleLabel(FormatSetting.getInstance().getForegroundColor(), 10, 10);
			colorDialog.add(foregroundColorLabel);
			JButton foregroundButton = new JButton("更改");
			foregroundButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color selectedColor = JColorChooser.showDialog(parentFrame, "更改字体颜色",
							foregroundColorLabel.getColor());
					if (selectedColor != null) {
						foregroundColorLabel.setColor(selectedColor);
						FileEditor.getInstance().setForegroundColor(selectedColor);
					}
				}
			});
			colorDialog.add(foregroundColorLabel);
			colorDialog.add(foregroundButton);
			colorDialog.setSize(200, 120);
			colorDialog.setResizable(false);
			colorDialog.setLocationRelativeTo(parentFrame);
		}
		colorDialog.setVisible(true);
	}

	/**
	 * 关闭操作，主要用于关闭打开的颜色设置窗体
	 */
	public void close() {
		if (colorDialog != null) {
			colorDialog.setVisible(false);
		}
	}
}
