package org;

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
    private float value;
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

 

}