package ru.hse.pe.deprecated.model;

public class TestItem {
    private String name, desc, advice, time;
    private int countQuestions;

    public TestItem(String name, String desc, int countQuestions, String time){
        this.name = name;
        this.desc = desc;
        this.countQuestions = countQuestions;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCountQuestions() {
        return countQuestions;
    }

    public void setCountQuestions(int countQuestions) {
        this.countQuestions = countQuestions;
    }
}
