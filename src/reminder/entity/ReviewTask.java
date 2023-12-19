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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Yang.CalendarPanel;
import notepad.FileInfo;
import notepad.NotepadPanel;
import reminder.page.Review;
import reminder.tool.JTextFieldHintListener;
import reminder.tool.SerialOp;

/**
 * @author 86155
 **/
public class ReviewTask implements Serializable {
	LocalDate setDate;
	LocalDate nextDate = LocalDate.now();
	int datesNow = 0;
	int remindDate = 0;
	String content = "";
	int type = 0;// 1 Ebbinghaus; 2 interval
	int[] ebb = new int[] { 1, 2, 4, 7, 15 };
	int interval = 0;
	int setProgress = 5;
	int progressNow = -1;
	int state = 0;// 0 为未在提醒日；1 为正在提醒；-1 为在提醒日且已打卡
	String s;
	FileInfo fi = null;
	static List<ReviewTask> RT_LIST = new ArrayList<ReviewTask>();

	public LocalDate getSetDate() {
		return setDate;
	}

	public int getDatesNow() {
		return datesNow;
	}

	public String getContent() {
		return content;
	}

	public int getType() {
		return type;
	}

	public int getInterval() {
		return interval;
	}

	public int getState() {
		return state;
	}

	public void fuxi() {
		LocalDate ld = LocalDate.now();
		state = -1;
		if (type == 2) {
			nextDate = ld.plusDays(interval);
//			System.out.println("test");
			CalendarPanel.getInstance().receiveTodoFromTip(content, nextDate.getYear(), nextDate.getMonthValue(),
					nextDate.getDayOfMonth());
			File f = new File("./data/review.ser");
			try {
				new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getRtList());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public int getSetProgress() {
		return setProgress;
	}

	public int getProgressNow() {
		return progressNow;
	}

	public static void setRtList(List<ReviewTask> tasks) {
		RT_LIST = tasks;
	}

	public static List<ReviewTask> getRtList() {
		return RT_LIST;
	}

	public void setFi(FileInfo fileInfo) {
		fi = fileInfo;
		content = fi.getName();
	}

	public FileInfo getFi() {
		return fi;
	}

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
		if (fi != null) {
			tt1.setText("当前已选择文件为 " + fi.getName());
			jj1.revalidate();
		}
		JButton jb = new JButton("导入现有文件");
		jb.addActionListener(e -> {
			FileInfo fileInfo = NotepadPanel.getInstance().selectFile();
			if (fileInfo != null) {
				fi = fileInfo;
				tt1.setText("当前已选择文件为 " + fi.getName());
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
				tf.addFocusListener(new JTextFieldHintListener(tf, "请输入提醒时间间隔(单位：天)"));
				mode.removeAll();
				mode.repaint();
				mode.add(jf);
				mode.add(tf);
				mode.revalidate();
			}
		});
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
			int z = 1;
			for (ReviewTask i : RT_LIST) {
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
					s = tf.getText();
					try {
						interval = Integer.parseInt(s);
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
					}
					RT_LIST.add(this);
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, RT_LIST);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					JOptionPane.showMessageDialog(new JPanel(), "已成功创建，起始日期为今天(" + setDate.toString() + "）", "创建成功",
							JOptionPane.INFORMATION_MESSAGE);
					try {
						Review.getInstance().backToList();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				} else {
					JOptionPane.showMessageDialog(new JPanel(), "创建失败，信息不全", "创建失败", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		no.addActionListener(e -> {
			Review.getInstance().backToList();
		});
		bottom.add(yes);
		bottom.add(no);
		jp.add(bottom, BorderLayout.SOUTH);

		jp.add(centerPanel, BorderLayout.CENTER);
		return jp;
	}

	boolean check(ReviewTask rt) {
		return rt.setDate != null && !"".equals(rt.content) && (rt.type == 1 || (rt.type == 2 && rt.interval > 0));
	}

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
							remindDate = datesNow;
							File f = new File("./data/review.ser");
							try {
								new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getRtList());
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
				if (remindDate < datesNow) {
					state = 0;
					for (int k = 0; k < 5; k++) {
						if (datesNow >= ebb[k]) {
							progressNow = k;
							if (datesNow == ebb[k]) {
								state = 1;
								remindDate = datesNow;
								File f = new File("./data/review.ser");
								try {
									new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getRtList());
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
						new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getRtList());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			} else {// state == -1
				if (datesNow % interval != 0) {
					state = 0;
					File f = new File("./data/review.ser");
					try {
						new SerialOp<List<ReviewTask>>().ser(f, ReviewTask.getRtList());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		}
	}

	public void reset() {
		setDate = LocalDate.now();
		datesNow = 0;
		remindDate = 0;
		progressNow = -1;
		state = 0;
		File f = new File("./data/review.ser");
		try {
			new SerialOp<List<ReviewTask>>().ser(f, RT_LIST);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
