package com.example.todolist;

import java.util.Date;

import io.realm.RealmObject;


public class ToDoItem extends RealmObject {
    private String id;
    private Date date;
    private String content;
    private boolean isImportant;
    private boolean isDDay;
    private Date dueDate;
    private boolean checked = false;
    private boolean isRepeat;
    private String repeatDate = "";  // true: 1, false: 0

    public ToDoItem(){}

    public ToDoItem(String id, Date date, String content, boolean isImportant, boolean isRepeat, boolean isDDay){
        this.id = id;
        this.date = date;
        this.content = content;
        this.isImportant = isImportant;
        this.isRepeat = isRepeat;
        this.isDDay = isDDay;
        this.checked = false;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDate(Date date) {
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

    public Date getDate() {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
