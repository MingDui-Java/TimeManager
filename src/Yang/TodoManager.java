package Yang;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * @author 杨智方
 */
public class TodoManager {
    private final String DATA_FOLDER = "data/calendar";
    public void loadTodoMap(Map<Integer, Map<Integer, Map<Integer, EachDay>>> secondPanelMapByYear, Calendar currentCalendarCopy) {
        try {
            Calendar startCalendar = (Calendar) currentCalendarCopy.clone();
            Calendar endCalendar = (Calendar) currentCalendarCopy.clone();
            endCalendar.add(Calendar.YEAR, 3); // 往后推三年

            while (startCalendar.before(endCalendar)) {
                int year = startCalendar.get(Calendar.YEAR);
                int month = startCalendar.get(Calendar.MONTH);
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);

                Map<Integer, Map<Integer, EachDay>> yearMap = secondPanelMapByYear.get(year);
                if (yearMap != null) {
                    Map<Integer, EachDay> monthMap = yearMap.get(month);
                    if (monthMap != null) {
                        EachDay eachDay = monthMap.getOrDefault(day,null);
                        DayPanel panelForDay = null;
                        if(eachDay != null) {
                             panelForDay = eachDay.dayPanel;
                        }
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
                                                panelForDay.getList().add(toDoList);
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

    public void saveTodoMap(Map<Integer, Map<Integer, Map<Integer, EachDay>>> secondPanelMapByYear, Calendar currentCalendarCopy) {
        try {
            Calendar startCalendar = (Calendar) currentCalendarCopy.clone();
            Calendar endCalendar = (Calendar) currentCalendarCopy.clone();
            endCalendar.add(Calendar.YEAR, 3); // 往后推三年

            while (startCalendar.before(endCalendar)) {
                int year = startCalendar.get(Calendar.YEAR);
                int month = startCalendar.get(Calendar.MONTH);
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);

                Map<Integer, Map<Integer, EachDay>> yearMap = secondPanelMapByYear.get(year);
                if (yearMap != null) {
                    Map<Integer, EachDay> monthMap = yearMap.get(month);
                    if (monthMap != null) {
                        EachDay eachDay = monthMap.getOrDefault(day,null);
                        DayPanel panelForDay = null;
                        if(eachDay != null){
                            panelForDay = eachDay.dayPanel;
                        }
                        if (panelForDay != null ) {
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
