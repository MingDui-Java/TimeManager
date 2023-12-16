package Yang;

import java.util.ArrayList;

public class ToDoList {
    private String name;
    static ArrayList<ToDoList> toDoLists = new ArrayList<>();
    ArrayList<ToDos> toDos;
    public ToDoList(String name){
        this.name = name;
        toDos = new ArrayList<>();
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
