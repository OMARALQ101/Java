package org;

import java.time.Year;
import java.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Date
{
    
    private int year;
    private int month;
    private int day;
        
    @JsonCreator
    public Date(
        @JsonProperty("year") int year,
        @JsonProperty("month") int month,
        @JsonProperty("day") int day
    ) throws IllegalArgumentException
    {

        if(year > Year.now().getValue())
        {
            throw new IllegalArgumentException("Year cannot be greater than " + Year.now().getValue());
        }

        if (month > 12 || month < 1)
        {
            throw new IllegalArgumentException("Month cannot be greater than 12 or less than 1");
        }

        if(day < 1 || day > YearMonth.of(year, month).lengthOfMonth())
        {
            throw new IllegalArgumentException("Days cannot be less than 1 or greater than " + YearMonth.of(year, month).lengthOfMonth() + " for that month");
        }

        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    public int getYear()
    {
        return year;
    }
    
    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public void setYear(int year)
    {
        if(year > Year.now().getValue())
        {
            throw new IllegalArgumentException("Year cannot be greater than " + Year.now().getValue());
        }

        this.year = year;
    }
    
    public void setMonth(int month)
    {
        if (month > 12 || month < 1)
        {
            throw new IllegalArgumentException("Month cannot be greater than 12 or less than 1");
        }

        this.month = month;
    }

    public void setDay(int day)
    {
        if(day < 1 || day > YearMonth.of(year, month).lengthOfMonth())
        {
            throw new IllegalArgumentException("Days cannot be less than 1 or greater than " + YearMonth.of(year, month).lengthOfMonth() + " for that month");
        }

        this.day = day;
    }

    @Override
    public String toString()
    {
        return "" + year + "-" + month + "-" + day;
    } 
}