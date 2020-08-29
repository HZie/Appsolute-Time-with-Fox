package com.example.todolist;

import io.realm.RealmObject;

// TODO: DB에 저장될 객체의 클래스 (D-Day여부: isDDay를 통해 알 수 있음)

public class ToDoItem extends RealmObject {
    private String date;
    private String content;
    private boolean isImportant;
    private boolean isDDay;
    private String dueDate ="";
    private boolean checked = false;
    private boolean isRepeat;
    private String repeatDate = "";  // true: 1, false: 0

    public ToDoItem(){}

    public ToDoItem(String date, String content, boolean isImportant, boolean isRepeat, boolean isDDay){
        this.date = date;
        this.content = content;
        this.isImportant = isImportant;
        this.isRepeat = isRepeat;
        this.isDDay = isDDay;
        this.checked = false;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDDay(boolean DDay) {
        isDDay = DDay;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public boolean isDDay() {
        return isDDay;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public void setRepeatDate(String repeatDate) {
        this.repeatDate = repeatDate;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public String getRepeatDate() {
        return repeatDate;
    }
}
