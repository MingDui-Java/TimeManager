package reminder.page;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import reminder.entity.DW;
import reminder.tool.SerialOp;
import timemanager.TimeManagerFrame;

/**
 * @author 86155
 **/
public class DrinkWaterPanel extends JPanel {
	private static DrinkWaterPanel instance = null;
	LocalDate ld;
	int hourSet = 0;
	int minSet = 0;
	int secondSet = 0;
	int dwTimes = 0;
	double remindInterval = 2;
	int flag = 0, ff = 0, isDW = 0;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	String timeStr = "--:--:--";
	JLabel display = new JLabel(timeStr);
	JLabel disp = new JLabel();
	Timer timer;
	Map<Integer, DW> dwMap;

	private DrinkWaterPanel() {
	}

	public static DrinkWaterPanel getInstance() {
		if (instance == null) {
			instance = new DrinkWaterPanel();
		}
		return instance;
	}

	public JPanel initialize() throws Exception {
		ld = LocalDate.now();
		// 读取
		int setTFlag = 1;
		File df = new File("./data/dw.ser");
		if (df.exists()) {
			dwMap = new SerialOp<java.util.Map<Integer, DW>>().dSer(df);
			isDW = 1;
			if (dwMap.isEmpty() || !dwMap.get(1).getDate().equals(ld)) {
				dwMap = new LinkedHashMap<>();
				setTFlag = 0;
				isDW = 0;
			}
			dwTimes = dwMap.size();
		} else {
			dwMap = new LinkedHashMap<>();
			dwTimes = 0;
		}
		disp.setText(String.valueOf(dwTimes));
		if (setTFlag == 1) {
			File timeF = new File("./data/setTime.ser");
			if (timeF.exists()) {
				int[] time = new SerialOp<int[]>().dSer(timeF);
				hourSet = time[0];
				minSet = time[1];
				secondSet = time[2];
			} else {
				hourSet = 0;
				minSet = 0;
				secondSet = 0;
			}
		}

		// 创建内容面板
		JPanel con = new JPanel();
		con.setLayout(new BorderLayout());

		// 菜单栏
		JMenuBar jmb = new JMenuBar();
		JMenu jm1 = new JMenu("设置提醒间隔(S)"), jm2 = new JMenu("当日喝水记录(D)");
		JButton jb3 = new JButton("说明(I)");
		jb3.setFocusPainted(false);
		jb3.setBorderPainted(false);
		jb3.setContentAreaFilled(false);
		jm1.setMnemonic('s');
		jm2.setMnemonic('d');
		jb3.setMnemonic('i');
		jm1.addSeparator();
		jm2.addSeparator();
		con.add(jmb, BorderLayout.NORTH);
		jmb.add(jm1);
		jmb.add(jm2);
		jmb.add(Box.createHorizontalGlue());
		jmb.add(jb3);
		setJm(jm1, jm2);
		jb3.addActionListener(e -> {
			String message = """
					该提醒将会以设定好的时间(默认为2小时)进行提醒，分别在
					达到设定时间的1.0、1.5、1.75倍时发出紧急度不同的提醒，
					以引起您的重视。请勿将提醒时间设置过短。
					(参考30min=0.5h，1min≈0.017h)""";
			JOptionPane.showMessageDialog(new JPanel(), message, "说明", JOptionPane.INFORMATION_MESSAGE);
		});

		// 中部标签
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2, 2, 0, 0));
		centerPanel.setFont(new Font("黑体", Font.PLAIN, 40));
		con.add(centerPanel, BorderLayout.CENTER);

		// 计时标签
		JLabel d = new JLabel("距离上一次喝水已经过去了：");
		d.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(d);
		display.setHorizontalAlignment(SwingConstants.CENTER);
		runTimer();
		centerPanel.add(display);
		Box ldBox = Box.createVerticalBox();
		JPanel leftDown = new JPanel();
		JLabel dr = new JLabel("今日喝水次数：");
		dr.setHorizontalAlignment(SwingConstants.CENTER);
		leftDown.add(dr);
		disp.setHorizontalAlignment(SwingConstants.CENTER);
		leftDown.add(disp);
		ldBox.add(Box.createVerticalGlue());
		ldBox.add(leftDown);
		centerPanel.add(ldBox);
		JLabel rightDown = new JLabel(new ImageIcon(TimeManagerFrame.class.getResource("/image/waterbg.png")));
		rightDown.setHorizontalAlignment(SwingConstants.CENTER);
		rightDown.setVerticalAlignment(SwingConstants.CENTER);
		centerPanel.add(rightDown);

		// 底部标签
		Box southBox = Box.createHorizontalBox();
		JButton startButton = new JButton("点击打卡喝水");
		startButton.setSize(2 * getPreferredSize().width, 120);
		startButton.setHorizontalAlignment(SwingConstants.CENTER);
		southBox.add(Box.createHorizontalGlue());
		southBox.add(startButton);
		southBox.add(Box.createHorizontalGlue());
		ldBox.add(southBox);
		ldBox.add(Box.createVerticalGlue());
		startButton.addActionListener((e) -> {
			if (dwTimes < 20) {
				isDW = 1;
				stopTimer();
				onStart();
				if (flag == 1) {
					jm2.removeAll();
					flag = 0;
				}
				jm2.add(new JMenuItem(dwMap.get(dwTimes).toString()));
			} else {
				JOptionPane.showMessageDialog(null, "今天喝水也太多了趴", "无法喝水打卡", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		return con;
	}

	void setJm(JMenu jm1, JMenu jm2) {
		JMenuItem h1p5 = new JMenuItem("1小时30分");
		JMenuItem h2 = new JMenuItem("2小时");
		JMenuItem h2p5 = new JMenuItem("2小时30分");
		JMenuItem h3 = new JMenuItem("3小时");
		JMenuItem custom = new JMenuItem("自定义");
		jm1.add(h1p5);
		jm1.add(h2);
		jm1.add(h2p5);
		jm1.add(h3);
		jm1.add(custom);
		h1p5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remindInterval = 1.5;
			}
		});
		h2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remindInterval = 2;
			}
		});
		h2p5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remindInterval = 2.5;
			}
		});
		h3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remindInterval = 3;
			}
		});
		custom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String interval = JOptionPane.showInputDialog(null, "输入自定义喝水提醒间隔：\n(小数形式，单位小时，大于0.1)");
					remindInterval = Double.parseDouble(interval);
					ff = -1;
				} catch (NullPointerException ignored) {
				} catch (NumberFormatException ignored) {
					JOptionPane.showMessageDialog(new JPanel(), "请输入合法的数字！", "输入警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		if (dwMap.isEmpty()) {
			jm2.add(new JMenuItem("今日无喝水记录"));
			flag = 1;
		} else {
			for (int y = 1; y <= dwTimes; y++) {
				jm2.add(new JMenuItem(dwMap.get(y).toString()));
			}
		}
	}

	private void stopTimer() {
		if (timer != null) {
			timer.stop();
		}
	}

	private void onStart() {
		dwTimes++;
		disp.setText(String.valueOf(dwTimes));
		timeStr = "00:00:00";
		display.setText(timeStr);
		LocalTime time = LocalTime.now();
		String timeSet = formatter.format(time);
		dwMap.put(dwTimes, new DW(timeSet, dwTimes));
		hourSet = getT(timeSet.charAt(0), timeSet.charAt(1));
		minSet = getT(timeSet.charAt(3), timeSet.charAt(4));
		secondSet = getT(timeSet.charAt(6), timeSet.charAt(7));
		// 保存
		File sf = new File("./data/dw.ser");
		try {
			new SerialOp<java.util.Map<Integer, DW>>().ser(sf, dwMap);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		File f = new File("./data/setTime.ser");
		try {
			int[] ts = new int[] { hourSet, minSet, secondSet };
			new SerialOp<int[]>().ser(f, ts);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		runTimer();
	}

	void runTimer() {
		// 创建定时器每隔,每隔1000毫秒执行一次
		if (isDW == 1) {
			timer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						updateTime();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			});
			timer.start();
		}
	}

	private void updateTime() throws Exception {
		LocalTime t = LocalTime.now();
		String tNow = formatter.format(t);
		File timeF = new File("./data/setTime.ser");
		if (timeF.exists()) {
			int[] time = new SerialOp<int[]>().dSer(timeF);
			hourSet = time[0];
			minSet = time[1];
			secondSet = time[2];
		} else {
			hourSet = 0;
			minSet = 0;
			secondSet = 0;
		}
		int sNow, mNow, hNow;
		hNow = getT(tNow.charAt(0), tNow.charAt(1));
		mNow = getT(tNow.charAt(3), tNow.charAt(4));
		sNow = getT(tNow.charAt(6), tNow.charAt(7));
		int second = sNow - secondSet, min = mNow - minSet, hour = hNow - hourSet;
		if (second < 0) {
			second += 60;
			min--;
		}
		if (min < 0) {
			min += 60;
			hour--;
		}
		timeStr = String.format("%02d:%02d:%02d", hour, min, second);
		display.setText(timeStr);
		remind(hour, min, second);
	}

	int getT(char a, char b) {
		return (a - '0') * 10 + (b - '0');
	}

	void remind(int h, int m, int s) {
		double as = 1.0 / 3600;
		double r = remindInterval;
		double rNow = (((double) s / 60) + (double) m) / 60 + (double) h;

		if (Math.abs(rNow - r) < as) {
			JOptionPane.showMessageDialog(new JPanel(), "该喝水了！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
		} else if (Math.abs(rNow - r * 1.5) < as) {
			JOptionPane.showMessageDialog(new JPanel(), "快去喝水！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
		} else if (Math.abs(rNow - r * 1.75) < as) {
			JOptionPane.showMessageDialog(new JPanel(), "嗓子已经冒烟了！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
		}

		if (ff == -1) {
			if (rNow > r) {
				JOptionPane.showMessageDialog(new JPanel(), "该喝水了！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
			}
			ff = 0;
		}
	}
}
