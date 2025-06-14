package com.project.finance.constants;

public enum Month {

    JAN("Jan", 1),
    FEB("Feb", 2),
    MAR("Mar", 3),
    APR("Apr", 4),
    May("May", 5),
    JUN("Jun", 6),
    JUL("Jul", 7),
    AUG("Aug", 8),
    SEP("Sep", 9),
    OCT("Oct", 10),
    NOV("Nov", 11),
    DEC("Dec", 12);


    private String s;
    private int value;

    Month(String s, int value) {
        this.s = s;
        this.value = value;
    }

    public static int strToInt(String s) {
        for (Month m : Month.values()) {
            if (m.s.equals(s)) {
                return m.value;
            }
        }

        return -1;
    }
}
