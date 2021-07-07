package com.example.fitnessfitu;

public class WeekExerciseData {
    String days;
    int count;

    public WeekExerciseData(String days, int count) {
        this.days = days;
        this.count = count;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
