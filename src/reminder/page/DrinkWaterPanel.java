/**
 * 提醒事项和喝水提醒的页面包
 *
 * @author DdddM
 * @version 1.0
 */
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

import reminder.entity.WaterDrink;
import reminder.tool.SerialOp;
import timemanager.TimeManagerFrame;

/**
 * 喝水提醒面板<BR/>
 * 使用了单例模式
 *
 * @author DdddM
 * @version 1.0
 **/
public class DrinkWaterPanel extends JPanel {
	/**
	 * 喝水提醒面板的单例
	 */
	private static DrinkWaterPanel instance = null;
	/**
	 * 当前运行时的日期
	 */
	LocalDate ld;
	/**
	 * 上一次打卡时的小时数
	 */
	int hourSet = 0;
	/**
	 * 上一次打卡时的分钟数
	 */
	int minSet = 0;
	/**
	 * 上一次打卡时的秒数
	 */
	int secondSet = 0;
	/**
	 * 当日喝水总次数（不可超过20）
	 */
	int drinkWaterTimes = 0;
	/**
	 * 自定义提醒间隔
	 */
	double remindInterval = 2;
	/**
	 * 标记位———是否导入今日记录
	 */
	int flag = 0;
	/**
	 * 标记位———是否需要提醒
	 */
	int remindTag = 0;
	/**
	 * 标记位———今日是否喝过水
	 */
	int isDrinkWater = 0;
	/**
	 * 时间格式字符串生成器
	 */
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	/**
	 * 显示的时间字符串
	 */
	String timeStr = "--:--:--";
	/**
	 * 展示时间的标签
	 */
	JLabel displayTime = new JLabel(timeStr);
	/**
	 * 展示喝水次数的标签
	 */
	JLabel displayRecords = new JLabel();
	/**
	 * 一个计时器
	 */
	Timer timer;
	/**
	 * 保存喝水记录的图
	 */
	Map<Integer, WaterDrink> waterDrinkMap;

	/**
	 * 喝水提醒面板的构造函数
	 */
	private DrinkWaterPanel() {
	}

	/**
	 * 获取喝水提醒面板的单例
	 *
	 * @return 喝水提醒面板的单例
	 */
	public static DrinkWaterPanel getInstance() {
		if (instance == null) {
			instance = new DrinkWaterPanel();
		}
		return instance;
	}

	/**
	 * 初始化喝水提醒面板<BR/>
	 * 只有一个面板，显示距离上一次喝水的时间间隔，以及当日喝水次数
	 *
	 * @return 整个喝水提醒面板
	 *
	 * @serialData 更新waterDrinkMap
	 * @throws Exception 序列化中出现的异常
	 */
	public JPanel initialize() throws Exception {
		ld = LocalDate.now();
		// 读取
		int setTFlag = 1;
		File df = new File("./data/dw.ser");
		if (df.exists()) {
			waterDrinkMap = new SerialOp<java.util.Map<Integer, WaterDrink>>().dSer(df);
			isDrinkWater = 1;
			if (waterDrinkMap.isEmpty() || !waterDrinkMap.get(1).getDate().equals(ld)) {
				waterDrinkMap = new LinkedHashMap<>();
				setTFlag = 0;
				isDrinkWater = 0;
			}
			drinkWaterTimes = waterDrinkMap.size();
		} else {
			waterDrinkMap = new LinkedHashMap<>();
			drinkWaterTimes = 0;
		}
		displayRecords.setText(String.valueOf(drinkWaterTimes));
		if (setTFlag == 1) {
			File timeF = new File("./data/setTime.ser");
			if (timeF.exists()) {
				try {
					int[] time = new SerialOp<int[]>().dSer(timeF);
					hourSet = time[0];
					minSet = time[1];
					secondSet = time[2];
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
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
		setJMenu(jm1, jm2);
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
		displayTime.setHorizontalAlignment(SwingConstants.CENTER);
		runTimer();
		centerPanel.add(displayTime);
		Box ldBox = Box.createVerticalBox();
		JPanel leftDown = new JPanel();
		JLabel dr = new JLabel("今日喝水次数：");
		dr.setHorizontalAlignment(SwingConstants.CENTER);
		leftDown.add(dr);
		displayRecords.setHorizontalAlignment(SwingConstants.CENTER);
		leftDown.add(displayRecords);
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
			if (drinkWaterTimes < 20) {
				isDrinkWater = 1;
				stopTimer();
				onStart();
				if (flag == 1) {
					jm2.removeAll();
					flag = 0;
				}
				jm2.add(new JMenuItem(waterDrinkMap.get(drinkWaterTimes).toString()));
			} else {
				JOptionPane.showMessageDialog(null, "今天喝水也太多了趴", "无法喝水打卡", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		return con;
	}

	/**
	 * 设置提醒面板的菜单栏
	 *
	 * @param menu1 设置模式的菜单
	 * @param menu2 查看当日喝水记录的菜单
	 */
	void setJMenu(JMenu menu1, JMenu menu2) {
		JMenuItem h1p5 = new JMenuItem("1小时30分");
		JMenuItem h2 = new JMenuItem("2小时");
		JMenuItem h2p5 = new JMenuItem("2小时30分");
		JMenuItem h3 = new JMenuItem("3小时");
		JMenuItem custom = new JMenuItem("自定义");
		menu1.add(h1p5);
		menu1.add(h2);
		menu1.add(h2p5);
		menu1.add(h3);
		menu1.add(custom);
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
					remindTag = -1;
				} catch (NullPointerException ignored) {
				} catch (NumberFormatException ignored) {
					JOptionPane.showMessageDialog(new JPanel(), "请输入合法的数字！", "输入警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		if (waterDrinkMap.isEmpty()) {
			menu2.add(new JMenuItem("今日无喝水记录"));
			flag = 1;
		} else {
			for (int y = 1; y <= drinkWaterTimes; y++) {
				menu2.add(new JMenuItem(waterDrinkMap.get(y).toString()));
			}
		}
	}

	/**
	 * 停止计时器
	 */
	private void stopTimer() {
		if (timer != null) {
			timer.stop();
		}
	}

	/**
	 * 开始计时
	 *
	 * @serialData 更新waterDrinkMap
	 * @throws RuntimeException 序列化中出现的异常
	 */
	private void onStart() {
		drinkWaterTimes++;
		displayRecords.setText(String.valueOf(drinkWaterTimes));
		timeStr = "00:00:00";
		displayTime.setText(timeStr);
		LocalTime time = LocalTime.now();
		String timeSet = formatter.format(time);
		waterDrinkMap.put(drinkWaterTimes, new WaterDrink(timeSet, drinkWaterTimes));
		hourSet = getT(timeSet.charAt(0), timeSet.charAt(1));
		minSet = getT(timeSet.charAt(3), timeSet.charAt(4));
		secondSet = getT(timeSet.charAt(6), timeSet.charAt(7));
		// 保存
		File sf = new File("./data/dw.ser");
		try {
			new SerialOp<java.util.Map<Integer, WaterDrink>>().ser(sf, waterDrinkMap);
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

	/**
	 * 运行计时器
	 */
	void runTimer() {
		// 创建定时器每隔,每隔1000毫秒执行一次
		if (isDrinkWater == 1) {
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

	/**
	 * 随时间更新面板内容
	 *
	 * @serialData 更新waterDrinkMap
	 * @throws Exception 序列化中出现的异常
	 */
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
		displayTime.setText(timeStr);
		remind(hour, min, second);
	}

	/**
	 * 从字符串获取时间数字
	 *
	 * @param a 十位
	 * @param b 个位
	 * @return 对应时间数字
	 */
	int getT(char a, char b) {
		return (a - '0') * 10 + (b - '0');
	}

	/**
	 * 进行喝水提醒<BR/>
	 * 根据设定好的提醒间隔和现在未喝水时间发出不同的提醒
	 *
	 * @param h 未喝水的小时数
	 * @param m 未喝水的分钟数
	 * @param s 未喝水的秒数
	 */
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

		if (remindTag == -1) {
			if (rNow > r) {
				JOptionPane.showMessageDialog(new JPanel(), "该喝水了！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
			}
			remindTag = 0;
		}
	}
}
