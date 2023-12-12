package reminder.page;
import reminder.entity.DW;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

/**
 * @author 86155
 **/
public class DrinkWater extends JPanel{
    int hourSet = 0;
    int minSet = 0;
    int secondSet = 0;
    int dwTimes = 0;
    double remindInterval = 2;
    int flag = 0, ff = 0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String timeStr = "--:--:--";
    JLabel display = new JLabel(timeStr);
    JLabel disp = new JLabel(String.valueOf(dwTimes));
    Timer timer;
    Map<Integer, DW> dwMap = new LinkedHashMap<>();
    public JPanel initialize(JFrame fa){
        fa.setTitle("喝水提醒");
        //创建内容面板
        JPanel con = new JPanel();
        con.setLayout(new BorderLayout());

        //菜单栏
        JMenuBar jmb = new JMenuBar();
        JMenu jm1 = new JMenu("设置提醒间隔(S)"), jm2 = new JMenu("当日喝水记录(D)"), jm3 = new JMenu("说明(I)");
        jm1.setMnemonic('s'); jm2.setMnemonic('d'); jm3.setMnemonic('i');
        jm1.addSeparator(); jm2.addSeparator();
        con.add(jmb,BorderLayout.NORTH);
        jmb.add(jm1);
        jmb.add(jm2);
        jmb.add(jm3);
        setJm(jm1,jm2,jm3);

        //中部标签
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2,2,0,0));
        centerPanel.setFont(new Font("黑体",Font.PLAIN,40));
        con.add(centerPanel,BorderLayout.CENTER);

        //计时标签
        JLabel d = new JLabel("距离上一次喝水已经过去了：");
        d.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(d);
        display.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(display);
        JLabel dr = new JLabel("今日喝水次数：");
        dr.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(dr);
        disp.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(disp);

        //底部标签
        JButton startButton = new JButton("点击打卡喝水");
        startButton.setHorizontalAlignment(SwingConstants.CENTER);
        con.add(startButton,BorderLayout.SOUTH);
        startButton.addActionListener((e) ->{
            stopTimer();
            dwTimes++;
            disp.setText(String.valueOf(dwTimes));
            onStart();
            if (flag == 1) {
                jm2.removeAll();
                flag = 0;
            }
            jm2.add(new JMenuItem(dwMap.get(dwTimes).toString()));
        });

        return con;
    }

    void setJm(JMenu jm1, JMenu jm2, JMenu jm3) {
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
                try{
                    String interval = JOptionPane.showInputDialog(null, "输入自定义喝水提醒间隔：\n(小数形式，单位小时，大于0.1)");
                    remindInterval = Double.parseDouble(interval);
                    ff = -1;
                }catch (NullPointerException ignored) {}
                catch (NumberFormatException ignored) {
                    JOptionPane.showMessageDialog(new JPanel(),"请输入合法的数字！", "输入警告", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        if (dwMap.isEmpty()) {
            jm2.add(new JMenuItem("今日无喝水记录"));
            flag = 1;
        }
        JMenuItem info = new JMenuItem("点击查看喝水提醒详细说明");
        info.addActionListener(e -> {
            String message = """
                    该提醒将会以设定好的时间(默认为2小时)进行提醒，分别在
                    达到设定时间的1.0、1.5、1.75倍时发出紧急度不同的提醒，
                    以引起您的重视。请勿将提醒时间设置过短。
                    (参考30min=0.5h，1min≈0.017h)""";
            JOptionPane.showMessageDialog(new JPanel(), message, "说明",JOptionPane.INFORMATION_MESSAGE);
        });
        jm3.add(info);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void onStart()
    {
        LocalTime time = LocalTime.now();
        String timeSet = formatter.format(time);
        System.out.println("timeSet="+timeSet);
        dwMap.put(dwTimes, new DW(timeSet, dwTimes));

        hourSet = getT(timeSet.charAt(0),timeSet.charAt(1));
        minSet = getT(timeSet.charAt(3),timeSet.charAt(4));
        secondSet = getT(timeSet.charAt(6),timeSet.charAt(7));
        timeStr = "00:00:00";
        display.setText(timeStr);
        //创建定时器每隔,每隔1000毫秒执行一次
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
    }

    private void updateTime() {
        LocalTime t = LocalTime.now();
        String tNow = formatter.format(t);
        int sNow, mNow, hNow;
        hNow = getT(tNow.charAt(0),tNow.charAt(1));
        mNow = getT(tNow.charAt(3),tNow.charAt(4));
        sNow = getT(tNow.charAt(6),tNow.charAt(7));
        int second = sNow-secondSet, min = mNow-minSet, hour = hNow-hourSet;
        if (second<0) {
            second += 60;
            min--;
        }
        if (min < 0) {
            min += 60;
            hour--;
        }
        timeStr = String.format("%02d:%02d:%02d",hour,min,second);
        display.setText(timeStr);
        remind(hour, min, second);
    }

    int getT(char a, char b) {
        return (a-'0') * 10 + (b-'0');
    }

    void remind(int h, int m, int s) {
        double as = 1.0/3600;
        double r = remindInterval;
        double rNow = (((double)s / 60) + (double)m) / 60 + (double)h;

        if (Math.abs(rNow-r) < as) {
            JOptionPane.showMessageDialog(new JPanel(), "该喝水了！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
        }else if (Math.abs(rNow-r*1.5) < as) {
            JOptionPane.showMessageDialog(new JPanel(), "快去喝水！", "喝水提醒", JOptionPane.INFORMATION_MESSAGE);
        }else if (Math.abs(rNow-r*1.75) < as) {
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
