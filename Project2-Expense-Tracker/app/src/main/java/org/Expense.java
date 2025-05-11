package org;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"value","year","month","day","category"})

public class Expense
{
    enum CATEGORY{
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
    private float value;
    @JsonProperty("id")
    private int id;
    private Date date;
    private CATEGORY category;

    public Expense(float value, int year, int month, int day, CATEGORY category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        this.value = (Math.round(value*100))/100;
        this.id = nextId++;
        this.date = new Date(year, month, day);
        this.category = category;

    }

    @JsonCreator
    public Expense(@JsonProperty("id") int id, 
    @JsonProperty("value") float value, 
    @JsonProperty("year") int year,
    @JsonProperty("month") int month,
    @JsonProperty("day") int day,
    @JsonProperty("category") CATEGORY category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        this.value = (Math.round(value*100))/100;
        this.id = id;
        this.date = new Date(year, month, day);
        this.category = category;

    }

    public float getValue()
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


    public void setValue(float value)
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