/**
 * 提醒事项和喝水提醒的实体包
 *
 * @author DdddM
 * @version 1.0
 */
package reminder.entity;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Yang.CalendarPanel;
import notepad.FileInfo;
import notepad.NotepadPanel;
import reminder.page.ReviewPanel;
import reminder.tool.JTextFieldHintListener;
import reminder.tool.SerialOp;

/**
 * 提醒事项
 *
 * @author DdddM
 * @version 1.0
 * @serial
 */
public class ReviewTask implements Serializable {
	/**
	 * 提醒事项构造函数
	 */
	public ReviewTask() {

	}
	/**
	 * 提醒事项被设定时的时间
	 */
	LocalDate setDate;
	/**
	 * 下一次需要提醒的日期
	 */
	LocalDate nextDate = LocalDate.now();
	/**
	 * 目前已经过去的天数
	 */
	int datesNow = 0;
	/**
	 * 要提醒的过去天数
	 */
	int remindDates = 0;
	/**
	 * 提醒事项的标题
	 */
	String content = "";
	/**
	 * 提醒模式<BR/>
	 * 1为按艾宾浩斯记忆曲线规律提醒<BR/>
	 * 2为按自定义时间间隔提醒
	 */
	int type = 0;
	/**
	 * 艾宾浩斯记忆曲线提醒时间表
	 */
	int[] ebb = new int[] { 1, 2, 4, 7, 15 };
	/**
	 * 自定义提醒的时间间隔
	 */
	int interval = 0;
	/**
	 * 总提醒次数
	 */
	int setProgress = 5;
	/**
	 * 当前提醒次数
	 */
	int progressNow = -1;
	/**
	 * 当前提醒状态<BR/>
	 * 0 为未在提醒日<BR/>
	 * 1 为正在提醒<BR/>
	 * -1 为在提醒日且已打卡
	 */
	int state = 0;
	/**
	 * 获取提醒间隔的字符串
	 */
	String intervalStr;
	/**
	 * 本条提醒事项对应的复习文件
	 */
	FileInfo reviewFileInfo = null;
	/**
	 * 保存所有提醒事项的列表
	 *
	 * @serialField
	 */
	static List<ReviewTask> REVIEWTASK_LIST = new ArrayList<ReviewTask>();
	/**
	 * 获取提醒事项被设定时的时间
	 *
	 * @return 被设定时的时间
	 */
	public LocalDate getSetDate() {
		return setDate;
	}
	/**
	 * 获取目前已经过去的天数
	 *
	 * @return 已经过去的天数
	 */
	public int getDatesNow() {
		return datesNow;
	}
	/**
	 * 获取提醒事项的名称
	 *
	 * @return 提醒事项的名称
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 获取提醒模式
	 *
	 * @return 提醒模式
	 */
	public int getType() {
		return type;
	}
	/**
	 * 获取自定义的提醒间隔
	 *
	 * @return 提醒间隔
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * 获取当前提醒状态
	 *
	 * @return 当前提醒状态
	 */
	public int getState() {
		return state;
	}
	/**
	 * 打卡<BR/>
	 * 将该提醒事项的状态变为-1
	 *
	 * @serialData 更新提醒事项列表
	 * @throws RuntimeException 序列化中出现的异常
	 */
	public void clockIn() {
		LocalDate ld = LocalDate.now();
		state = -1;
		if (type == 2) {
			nextDate = ld.plusDays(interval);
			CalendarPanel.getInstance().receiveTodoFromTip(content, nextDate.getYear(), nextDate.getMonthValue(),
					nextDate.getDayOfMonth());
			File f = new File("./data/review.ser");
			try {
				new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	/**
	 * 获取总提醒次数
	 *
	 * @return 总提醒次数
	 */
	public int getSetProgress() {
		return setProgress;
	}
	/**
	 * 获取当前提醒次数
	 *
	 * @return 当前提醒次数
	 */
	public int getProgressNow() {
		return progressNow;
	}
	/**
	 * 更新总提醒事项列表
	 *
	 * @param tasks 提醒事项列表
	 */
	public static void setReviewTaskList(List<ReviewTask> tasks) {
		REVIEWTASK_LIST = tasks;
	}
	/**
	 * 获取总提醒事项列表
	 *
	 * @return 总提醒事项列表
	 */
	public static List<ReviewTask> getReviewtaskList() {
		return REVIEWTASK_LIST;
	}
	/**
	 * 设置对应的复习文件<BR/>
	 * 并将复习文件的名字设为提醒事项的名称
	 *
	 * @param fileInfo 复习文件
	 */
	public void setReviewFileInfo(FileInfo fileInfo) {
		reviewFileInfo = fileInfo;
		content = reviewFileInfo.getName();
	}
	/**
	 * 获取对应的复习文件
	 *
	 * @return 对应的复习文件
	 */
	public FileInfo getReviewFileInfo() {
		return reviewFileInfo;
	}
	/**
	 * 添加提醒事项<BR/>
	 * 进行事项的初始化并添加到提醒事项列表中
	 *
	 * @author DdddM
     * @return 添加提醒事项对应的面板
	 * @serialData 更新提醒事项列表
	 * @throws RuntimeException 序列化中出现的异常
	 */
	public JPanel addReview() {
		this.setDate = LocalDate.now();
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		JPanel mode = new JPanel(new FlowLayout());
		JTextField jf = new JTextField("当前未选择模式", 28);
		mode.add(jf);

		// 中部标签
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(4, 1, 0, 0));

		JPanel j1 = new JPanel(new FlowLayout());
		JLabel t1 = new JLabel("请输入你的提醒事项名称（不超过10个字）：");
		JTextField t2 = new JTextField(content, 20);
		j1.add(t1);
		j1.add(t2);
		centerPanel.add(j1);

		JPanel jj1 = new JPanel(new FlowLayout());
		JLabel tt1 = new JLabel("请添加复习文件：");
		if (reviewFileInfo != null) {
			tt1.setText("当前已选择文件为 " + reviewFileInfo.getName());
			jj1.revalidate();
		}
		JButton jb = new JButton("导入现有文件");
		jb.addActionListener(e -> {
			FileInfo fileInfo = NotepadPanel.getInstance().selectFile();
			if (fileInfo != null) {
				reviewFileInfo = fileInfo;
				tt1.setText("当前已选择文件为 " + reviewFileInfo.getName());
				jj1.revalidate();
			}
		});
		jj1.add(tt1);
		jj1.add(jb);
		centerPanel.add(jj1);

		JPanel j2 = new JPanel();
		j2.setLayout(new GridLayout(4, 1));
		JLabel t3 = new JLabel("请确定你的提醒计划：");
		JRadioButton jrb1 = new JRadioButton("按艾宾浩斯记忆曲线规律");
		JRadioButton jrb2 = new JRadioButton("按自定义时间提醒");
		jrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.setText("当前选择的提醒模式是：" + e.getActionCommand());
				type = 1;
				mode.removeAll();
				mode.repaint();
				mode.add(jf);
				mode.revalidate();
			}
		});
		JTextField tf = new JTextField(17);
		jrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.setText("当前选择的提醒模式是：" + e.getActionCommand());
				type = 2;
				tf.addFocusListener(new JTextFieldHintListener(tf, "请输入提醒间隔(>1天)"));
				mode.removeAll();
				mode.repaint();
				mode.add(jf);
				mode.add(tf);
				mode.revalidate();
			}
		});
		t3.setHorizontalAlignment(SwingConstants.CENTER);
		jrb1.setHorizontalAlignment(SwingConstants.CENTER);
		jrb2.setHorizontalAlignment(SwingConstants.CENTER);
		j2.add(t3);
		j2.add(jrb1);
		j2.add(jrb2);
		ButtonGroup bg = new ButtonGroup();
		bg.add(jrb1);
		bg.add(jrb2);
		centerPanel.add(j2);

		jf.setEditable(false);
		centerPanel.add(mode);

		// 底部标签
		JPanel bottom = new JPanel(new FlowLayout());
		JButton yes = new JButton("确定");
		JButton no = new JButton("取消");
		yes.addActionListener(e -> {
			content = t2.getText();
			if (content.charAt(0) == '提' && content.charAt(1)=='醒'&&content.charAt(2)=='：') {
				JOptionPane.showMessageDialog(new JPanel(), "不可把\"提醒：\"作为名称的开头！", "输入警告", JOptionPane.WARNING_MESSAGE);
				content = "";
				t2.setText(content);
				t2.revalidate();
			}
			int z = 1;
			for (ReviewTask i : REVIEWTASK_LIST) {
				if (i.content.equals(this.content)) {
					z = 0;
					JOptionPane.showMessageDialog(new JPanel(), "该名称已被占用！", "输入警告", JOptionPane.WARNING_MESSAGE);
					content = "";
					t2.setText(content);
					t2.revalidate();
					break;
				}
			}
			if (z == 1) {
				if (type == 2) {
					intervalStr = tf.getText();
					try {
						interval = Integer.parseInt(intervalStr);
						if(interval <= 1) {
							JOptionPane.showMessageDialog(new JPanel(), "提醒间隔请大于1天！", "输入警告", JOptionPane.WARNING_MESSAGE);
							tf.setText("");
							tf.revalidate();
						}
					} catch (NumberFormatException ignored) {
						JOptionPane.showMessageDialog(new JPanel(), "请输入合法的数字！", "输入警告", JOptionPane.WARNING_MESSAGE);
					}
				}
				if (check(this)) {
					if (type == 1) {
						for (int x = 0; x < 5; x++) {
							nextDate = setDate.plusDays(ebb[x]);
							CalendarPanel.getInstance().receiveTodoFromTip(content, nextDate.getYear(),
									nextDate.getMonthValue(), nextDate.getDayOfMonth());
						}
					}
					if (type == 2) {
						setProgress = 0;
						progressNow = 0;
						CalendarPanel.getInstance().receiveTodoFromTip(content, setDate.getYear(),
								setDate.getMonthValue(), setDate.getDayOfMonth());
					}
					REVIEWTASK_LIST.add(this);
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, REVIEWTASK_LIST);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					JOptionPane.showMessageDialog(new JPanel(), "已成功创建，起始日期为今天(" + setDate.toString() + "）", "创建成功",
							JOptionPane.INFORMATION_MESSAGE);
					try {
						ReviewPanel.getInstance().backToList();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				} else {
					JOptionPane.showMessageDialog(new JPanel(), "创建失败，信息不全", "创建失败", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		no.addActionListener(e -> {
			ReviewPanel.getInstance().backToList();
		});
		bottom.add(yes);
		bottom.add(no);
		jp.add(bottom, BorderLayout.SOUTH);

		jp.add(centerPanel, BorderLayout.CENTER);
		return jp;
	}
	/**
	 * 检查信息是否完整
	 *
	 * @author DdddM
	 * @param taskJudge 待判断的提醒事项
	 * @return 信息完整返回true，不完整返回false
	 */
	boolean check(ReviewTask taskJudge) {
		return taskJudge.setDate != null && !"".equals(taskJudge.content) && (taskJudge.type == 1 || (taskJudge.type == 2 && taskJudge.interval > 0));
	}
	/**
	 * 更新提醒事项的信息
	 *
	 * @author DdddM
	 * @serialData 更新提醒事项列表
	 * @throws RuntimeException 序列化中出现的异常
	 */
	public void update() {
		LocalDate ld = LocalDate.now();
		datesNow = (int) setDate.until(ld, ChronoUnit.DAYS);
		if (type == 1) {
			if (state == 0) {
				for (int k = 0; k < 5; k++) {
					if (datesNow >= ebb[k]) {
						progressNow = k;
						if (datesNow == ebb[k]) {
							state = 1;
							remindDates = datesNow;
							File f = new File("./data/review.ser");
							try {
								new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
							} catch (IOException ex) {
								throw new RuntimeException(ex);
							}
							break;
						}
					} else {
						break;
					}
				}
			} else {// state == -1
				if (remindDates < datesNow) {
					state = 0;
					for (int k = 0; k < 5; k++) {
						if (datesNow >= ebb[k]) {
							progressNow = k;
							if (datesNow == ebb[k]) {
								state = 1;
								remindDates = datesNow;
								File f = new File("./data/review.ser");
								try {
									new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
								} catch (IOException ex) {
									throw new RuntimeException(ex);
								}
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		} else {// type == 2
			progressNow = datesNow % interval;// 还差多少天提醒
			if (state == 0) {
				if (datesNow % interval == 0) {// 是整数倍->提醒日
					state = 1;
					setProgress = datesNow / interval;// 已经提醒的次数
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			} else {// state == -1
				if (datesNow % interval != 0) {
					state = 0;
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getReviewtaskList());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		}
	}
	/**
	 * 重置该提醒事项
	 *
	 * @author DdddM
	 * @serialData 更新提醒事项列表
	 * @throws RuntimeException 序列化中出现的异常
	 */
	public void reset() {
		setDate = LocalDate.now();
		datesNow = 0;
		remindDates = 0;
		progressNow = -1;
		state = 0;
		File f = new File("./data/review.ser");
		try {
			new SerialOp<List<ReviewTask>>().ser(f, REVIEWTASK_LIST);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
