package org;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"value","year","month","day","category"})

public class Expense
{
    public enum CATEGORY{
        HOUSING,
        UTILITIES,
        TRANSPORTATION,
        FOOD,
        HEALTHCARE,
        DEBT_PAYMENT,
        SAVINGS_AND_INVESTMENTS,
        PERSONAL_SPENDING
    }
    
    private static int nextId = 1;
    @JsonProperty("value")
    private double value;
    @JsonProperty("id")
    private int id;
    private Date date;
    private CATEGORY category;

    public Expense(double value, int year, int month, int day, CATEGORY category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        double rounded = (Math.round(value * 100.0))/100.0;
        this.value = rounded;
        this.id = nextId++;
        this.date = new Date(year, month, day);
        this.category = category;

    }

    @JsonCreator
    public Expense(@JsonProperty("id") int id, 
    @JsonProperty("value") double value, 
    @JsonProperty("year") Date date,
    @JsonProperty("category") CATEGORY category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        double rounded = (Math.round(value * 100.0))/100.0;
        this.value = rounded;
        this.id = id;
        this.date = date;
        this.category = category;

        if (id >= nextId)
        {
            nextId = id+1;
        }

    }

    public double getValue()
    {
        return value;
    }

    public int getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public CATEGORY getCategory()
    {
        return category;
    }


    public void setValue(double value)
    {
        this.value = (Math.round(value*100))/100;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setCategory(CATEGORY category)
    {
        this.category = category;
    }

    @Override
    public String toString()
    {
        return ""+id+". value = "+value+", date = "+date.toString()+", category = "+ category.toString();
    }

}