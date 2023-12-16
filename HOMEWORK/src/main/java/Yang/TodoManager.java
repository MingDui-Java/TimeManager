package Yang;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * @author 杨智方
 */
public class TodoManager {
    private final String DATA_FOLDER = "data";
    public void loadTodoMap(Map<Integer, Map<Integer, Map<Integer, SecondPanel>>> secondPanelMapByYear, Calendar currentCalendarCopy) {
        try {
            Calendar startCalendar = (Calendar) currentCalendarCopy.clone();
            Calendar endCalendar = (Calendar) currentCalendarCopy.clone();
            endCalendar.add(Calendar.YEAR, 3); // 往后推三年

            while (startCalendar.before(endCalendar)) {
                int year = startCalendar.get(Calendar.YEAR);
                int month = startCalendar.get(Calendar.MONTH);
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);

                Map<Integer, Map<Integer, SecondPanel>> yearMap = secondPanelMapByYear.get(year);
                if (yearMap != null) {
                    Map<Integer, SecondPanel> monthMap = yearMap.get(month);
                    if (monthMap != null) {
                        SecondPanel panelForDay = monthMap.getOrDefault(day, null);
                        if (panelForDay != null) {
                            String fileName = String.format("%s/%04d-%02d-%02d.ser", DATA_FOLDER, year, month + 1, day);
                            File file = new File(fileName);
                            if (file.exists()) {
                                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
                                    Object obj = ois.readObject();
                                    if (obj instanceof ArrayList) {
                                        ArrayList<Object> todoList = (ArrayList<Object>) obj;
                                        for (Object todo : todoList) {
                                            if(todo instanceof ToDoList toDoList){
                                                JPanel bigListPanel = toDoList.getBigListPanel();
                                                bigListPanel.revalidate();
                                                bigListPanel.repaint();
                                                panelForDay.ToDoListPanel.add(bigListPanel);
                                                panelForDay.getList().add(toDoList);
                                                JScrollPane scrollPane = new JScrollPane(panelForDay.ToDoListPanel);
                                                panelForDay.setScrollPane(scrollPane);
                                            }
                                        }
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                startCalendar.add(Calendar.DAY_OF_MONTH, 1); // 往后推一天
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTodoMap(Map<Integer, Map<Integer, Map<Integer, SecondPanel>>> secondPanelMapByYear, Calendar currentCalendarCopy) {
        try {
            Calendar startCalendar = (Calendar) currentCalendarCopy.clone();
            Calendar endCalendar = (Calendar) currentCalendarCopy.clone();
            endCalendar.add(Calendar.YEAR, 3); // 往后推三年

            while (startCalendar.before(endCalendar)) {
                int year = startCalendar.get(Calendar.YEAR);
                int month = startCalendar.get(Calendar.MONTH);
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);

                Map<Integer, Map<Integer, SecondPanel>> yearMap = secondPanelMapByYear.get(year);
                if (yearMap != null) {
                    Map<Integer, SecondPanel> monthMap = yearMap.get(month);
                    if (monthMap != null) {
                        SecondPanel panelForDay = monthMap.getOrDefault(day, null);
                        if (panelForDay != null && !panelForDay.getList().isEmpty()) {
                            String folderPath = DATA_FOLDER;
                            File folder = new File(folderPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }

                            String fileName = String.format("%s/%04d-%02d-%02d.ser", folderPath,year,month+1,day);
                            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                                oos.writeObject(panelForDay.getList());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                startCalendar.add(Calendar.DAY_OF_MONTH, 1); // 往后推一天
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
