package com.hexlone.hexcalendar.entity;

public class DateInfo {


    public static final int LAST_MONTH=-1;
    public static final int NOW_MONTH=0;
    public static final int NEXT_MONTH=1;

    private int typeOfMonth;
    private String solarFestival;
    private int solarMonth;
    private int solarYear;
    private int solarDay;

    private int lunarYear;
    private String lunarMonth;
    private String lunarDay;
    private String lunarFestival;
    private String lunarTerm;

    public int getSolarMonth() {
        return solarMonth;
    }

    public void setSolarMonth(int solarMonth) {
        this.solarMonth = solarMonth;
    }

    public int getSolarYear() {
        return solarYear;
    }

    public void setSolarYear(int solarYear) {
        this.solarYear = solarYear;
    }

    public int getSolarDay() {
        return solarDay;
    }

    public void setSolarDay(int solarDay) {
        this.solarDay = solarDay;
    }

    public String getSolarFestival() {
        return solarFestival;
    }

    public void setSolarFestival(String solarFestival) {
        this.solarFestival = solarFestival;
    }

    public int getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(int lunarYear) {
        this.lunarYear = lunarYear;
    }

    public String getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(String lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public String getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(String lunarDay) {
        this.lunarDay = lunarDay;
    }

    public String getLunarFestival() {
        return lunarFestival;
    }

    public void setLunarFestival(String lunarFestival) {
        this.lunarFestival = lunarFestival;
    }

    public String getLunarTerm() {
        return lunarTerm;
    }

    public void setLunarTerm(String lunarTerm) {
        this.lunarTerm = lunarTerm;
    }


    public int getTypeOfMonth() {
        return typeOfMonth;
    }

    public void setTypeOfMonth(int typeOfMonth) {
        this.typeOfMonth = typeOfMonth;
    }
}
