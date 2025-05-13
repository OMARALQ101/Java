package org;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Expense
{

    
    private static int nextId = 1;
    private double value;
    private String desc;
    private int id;
    private Date date;
    private String category;

    public Expense(double value, String desc,int year, int month, int day, String category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        double rounded = (Math.round(value * 100.0))/100.0;
        this.value = rounded;
        this.desc = desc;
        this.id = nextId++;
        this.date = new Date(year, month, day);
        this.category = category;

    }

    @JsonCreator
    public Expense(@JsonProperty("id") int id, 
    @JsonProperty("value") double value,
    @JsonProperty("desc") String desc, 
    @JsonProperty("year") Date date,
    @JsonProperty("category") String category) throws IllegalArgumentException
    {
        if(value <= 0)
        {
            throw new IllegalArgumentException("Value cannot be less than or equal zero");
        }

        double rounded = (Math.round(value * 100.0))/100.0;
        this.value = rounded;
        this.desc = desc;
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

    public String getCategory()
    {
        return category;
    }

    public String getDescription()
    {
        return desc;
    }

    public void setDescription(String desc)
    {
        this.desc = desc;
    }

    public void setValue(double value)
    {
        this.value = (Math.round(value*100))/100;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int compareDate(Expense b)
    {
        if(this.date.getYear() > b.date.getYear())
        {
            return 1;
        }
        else if (this.date.getYear() == b.date.getYear())
        {
            if(this.date.getMonth() > b.date.getMonth())
            {
                return 1;
            }
            else if (this.date.getMonth() == b.date.getMonth())
            {
                if(this.date.getDay() > b.date.getDay())
                {
                    return 1;
                }
                else if (this.date.getDay() == b.date.getDay())
                {
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
            else
            {
                return -1;
            }

        }
        else
        {
            return -1;
        }
        

    }

    public int compareAmount(Expense b)
    {
        if(this.value > b.value)
        {
            return 1;
        }
        else if (this.value == b.value)
        {
            return 0;

        }
        else
        {
            return -1;
        }
        

    }

    @Override
    public String toString()
    {
        return ""+id+". value = "+value+ ", desc = "+ desc +", date = "+date.toString()+", category = "+ category;
    }

}