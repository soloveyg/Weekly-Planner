package com.gloriasolovey.planner.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static LocalDate getStartOfWeek(LocalDate date) {
    	return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }
    
    public static LocalDate getEndOfWeek(LocalDate date) {
    	return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
    }

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = getStartOfWeek(today);
        LocalDate endOfWeek = getEndOfWeek(today);

        System.out.println("Today: " + today);
        System.out.println("Start of the Week: " + startOfWeek);
        System.out.println("End of the Week: " + endOfWeek);
    }
}
