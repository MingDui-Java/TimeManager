/**
 * 提醒事项和喝水提醒的页面包
 *
 * @author DdddM
 * @version 1.0
 */
package reminder.page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import notepad.FileInfo;
import notepad.NotepadPanel;
import reminder.entity.ReviewTask;
import reminder.tool.SerialOp;
import timemanager.TimeManagerFrame;

/**
 * 提醒事项面板<BR/>
 * 使用了单例模式
 *
 * @author DdddM
 * @version 1.0
 **/
public class ReviewPanel extends JPanel {
	/**
	 * 提醒事项面板的单例
	 */
	private static ReviewPanel instance = null;
	/**
	 * 提醒事项面板的构造函数
	 */
	private ReviewPanel() {
	}
	/**
	 * 提醒事项面板的中部面板
	 */
	JPanel centerPanel = new JPanel();
	/**
	 * 获取提醒事项面板的单例
	 *
	 * @return 提醒事项面板的单例
	 */
	public static ReviewPanel getInstance() {
		if (instance == null) {
			instance = new ReviewPanel();
		}
		return instance;
	}
	/**
	 * 从笔记模块添加提醒事项
	 *
	 * @param info 对应的复习文件
	 * @see FileInfo
	 */
	public void addFromNote(FileInfo info) {
		ReviewTask newTask = new ReviewTask();
		newTask.setReviewFileInfo(info);
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.add(newTask.addReview());
		centerPanel.revalidate();
	}
	/**
	 * 返回到显示提醒事项总览的面板
	 */
	public void backToList() {
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.add(listTask());
		centerPanel.revalidate();
	}
	/**
	 * 提醒事项面板初始化<BR/>
	 * 主要有两个子面板：添加提醒事项、提醒事项总览，通过菜单栏的按钮切换
	 *
	 * @author DdddM
	 * @return 整个提醒事项面板
	 * @exception RuntimeException 反序列化时的异常
	 */
	public JPanel initialize() throws Exception {
		File f = new File("./data/review.ser");
		if (f.exists()) {
			try {
				ReviewTask.setReviewTaskList(new SerialOp<List<ReviewTask>>().dSer(f));
			}catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			ReviewTask.setReviewTaskList(new ArrayList<>());
		}

		// 创建内容面板
		JPanel con = new JPanel();
		con.setLayout(new BorderLayout());

		// 菜单栏
		JMenuBar jmb = new JMenuBar();
		JButton jm1 = new JButton("新建提醒事项(A)");
		JButton jm2 = new JButton("提醒事项总览(C)");
		JButton jm3 = new JButton("说明(I)");
		jm1.setMnemonic('a');
		jm2.setMnemonic('c');
		jm3.setMnemonic('i');
		con.add(jmb, BorderLayout.NORTH);
		jmb.add(jm1);
		jmb.add(jm2);
		jmb.add(Box.createHorizontalGlue());
		jmb.add(jm3);

		// 中部标签
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(listTask(), BorderLayout.CENTER);
		con.add(centerPanel, BorderLayout.CENTER);

		jm1.addActionListener(e -> {
			ReviewTask newTask = new ReviewTask();
			centerPanel.removeAll();
			centerPanel.repaint();
			centerPanel.add(newTask.addReview());
			centerPanel.revalidate();
		});
		jm2.addActionListener(e -> {
			centerPanel.removeAll();
			centerPanel.repaint();
			centerPanel.add(listTask());
			centerPanel.revalidate();
		});
		jm3.addActionListener(e -> {
			String message = """
					不可把“提醒：”作为提醒事项名称的开头。
					按照艾宾浩斯遗忘曲线提醒会从开始日期起的1天、2天、4天、
					7天、15天进行提醒，以期形成长期记忆。
					当然您也可以自定义提醒的间隔天数，并设置提醒周期。
					在当此提醒任务结束后，您可以选择删除该记录或者再次进行。""";
			JOptionPane.showMessageDialog(new JPanel(), message, "说明", JOptionPane.INFORMATION_MESSAGE);
		});
		return con;
	}
	/**
	 * 展示提醒事项总览
	 *
	 * @author DdddM
	 * @return 提醒事项总览的子面板
	 * @serialData 更新提醒事项列表
	 * @throws RuntimeException 序列化中出现的异常
	 */
	JPanel listTask() {
		List<ReviewTask> lt = ReviewTask.getReviewtaskList();
		int lines = lt.size();
		if (lt.isEmpty()) {
			JPanel jp = new JPanel(new BorderLayout());
			JLabel jl = new JLabel("当前无提醒事项");
			jp.add(jl, BorderLayout.CENTER);
			return jp;
		}
		JPanel jp = new JPanel(new BorderLayout());
		Box contentBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(contentBox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height)); // 设置
																											// JScrollPane
																											// 的首选大小
		scrollPane.setViewportView(contentBox); // 将 contentBox 设置为视口的显示区域
		jp.add(scrollPane, BorderLayout.CENTER);

		int i = 0;
		for (ReviewTask k : lt) {
			i++;
			k.update();
			JPanel geJp = new JPanel(new GridLayout(3, 1));
			Box rtJp1 = Box.createHorizontalBox();
			JPanel rtJp2 = new JPanel();
			JPanel rtJp3 = new JPanel();
			JLabel label1 = new JLabel("  序号" + i);
			JLabel label1p5 = new JLabel("名称：" + k.getContent());
			JLabel label2 = new JLabel("起始时间：" + k.getSetDate().toString());
			String mode = "";
			if (k.getType() == 1) {
				mode = "按艾宾浩斯记忆曲线规律";
			} else {
				mode = "提醒间隔" + k.getInterval() + "天";
			}
			JLabel label3 = new JLabel("提醒模式：" + mode);
			String me = "";
			switch (k.getState()) {
			case 0 -> {
				me = "今天不在提醒日";
			}
			case -1 -> {
				me = "今日已提醒";
			}
			case 1 -> {
				me = "今日需打卡";
			}
			}
			JLabel label4 = new JLabel(me);
			JButton rev5 = new JButton("打卡");
			rev5.addActionListener(e -> {
				if (k.getState() == 1) {
					k.clockIn();
					label4.setText("太棒了！今日已打卡，继续坚持");
					label4.revalidate();
					if (k.getType() == 1) {
						if (k.getProgressNow() + 1 == k.getSetProgress()) {
							Object[] ending = { "重复该任务", "删除该任务" };
							int op = JOptionPane.showOptionDialog(null, "恭喜您！建立了长期记忆", "结束任务",
									JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, ending,
									ending[0]);

							if (op == 0) {
								k.reset();
								centerPanel.removeAll();
								centerPanel.repaint();
								centerPanel.add(listTask());
								centerPanel.revalidate();
							} else if(op == 1 || op == JOptionPane.CLOSED_OPTION){
								lt.remove(k);
								File f = new File("./data/review.ser");
								try {
									new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
								} catch (IOException ex) {
									throw new RuntimeException(ex);
								}
								jp.remove(geJp);
								jp.revalidate();
							}
						}
					}
				}
			});
			JButton openF = new JButton("打开复习文件");
			openF.addActionListener(e -> {
				if (!NotepadPanel.getInstance().openFile(k.getReviewFileInfo())) {
					JOptionPane.showMessageDialog(null, "文件不存在.", "错误", JOptionPane.WARNING_MESSAGE);
				} else {
					TimeManagerFrame.showNotepad();
				}
			});
			JLabel label6 = new JLabel();
			JProgressBar jpb7 = new JProgressBar();
			if (k.getType() == 1) {
				label6.setText("  当前进度");
				jpb7.setMaximum(k.getSetProgress());
				jpb7.setStringPainted(true);
				jpb7.setBorderPainted(true);
				jpb7.setValue(k.getProgressNow() + 1);
				jpb7.revalidate();
			} else {
				label6.setText("  当前处于第" + (k.getSetProgress() + 1) + "个提醒周期！");
				jpb7.setMaximum(k.getInterval());
				jpb7.setStringPainted(true);
				jpb7.setBorderPainted(true);
				jpb7.setString("距离下一次提醒还有" + (k.getInterval() - k.getProgressNow()) + "天");
				jpb7.setValue(k.getProgressNow());
				jpb7.revalidate();
			}

			JLabel label8 = new JLabel("距离开始已过去" + k.getDatesNow() + "天");
			JButton end9 = null;
			if (k.getType() == 2) {
				end9 = new JButton("结束任务");
				end9.addActionListener(e -> {
					lt.remove(k);
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					jp.remove(geJp);
					jp.revalidate();
					getInstance().backToList();
				});
			}
			rtJp1.add(label1);
			rtJp1.add(Box.createHorizontalGlue());
			rtJp1.add(label1p5);
			rtJp1.add(Box.createHorizontalGlue());
			rtJp1.add(label2);
			rtJp2.add(Box.createHorizontalBox());
			rtJp2.add(label4);
			rtJp2.add(rev5);
			if (k.getReviewFileInfo() != null) {
				rtJp2.add(openF);
			}
			rtJp2.add(Box.createHorizontalBox());
			rtJp3.add(label3);
			rtJp3.add(Box.createHorizontalBox());
			rtJp3.add(label6);
			rtJp3.add(jpb7);
			rtJp3.add(label8);
			if (k.getType() == 2) {
				rtJp3.add(end9);
			}
			geJp.add(rtJp1);
			geJp.add(rtJp2);
			geJp.add(rtJp3);
			geJp.setPreferredSize(new Dimension(783, geJp.getPreferredSize().height));
			geJp.setMaximumSize(new Dimension(783, geJp.getPreferredSize().height));
			geJp.setMinimumSize(new Dimension(783, geJp.getPreferredSize().height));
			geJp.setBorder(new BevelBorder(BevelBorder.RAISED));
			contentBox.add(geJp);
		}
		return jp;
	}
}
