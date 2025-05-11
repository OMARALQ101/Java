package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.Expense;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {

    public static ArrayList<Expense> expenses;

    // this is going to be printed whenever there isnt enough arguments or misuse of application
    public static String improper_use = "To use the Expense Tracker Application you have the following options:\n"+
                    "java -jar app/build/libs/app-all.jar open [filename] [command]\n "
                    + "Here are the availabe commands:\n" + 
                    "1. add\n2. list\n3. filter\n4. update\n5. delete\n6. summary"
                ;


    // this method is to extract all the lines from the json file
    public static String readAllLines(BufferedReader br) throws IOException
    {
        StringBuilder builder = new StringBuilder();

        String str;

        while((str = br.readLine()) != null)
        {
            builder.append(str).append("\n");
        }

        return builder.toString();
        
    }


    // this method is used to open the file and turn all the json info into objects. 
    public static void loadJson(String[] args) 
    {

        try(BufferedReader br = new BufferedReader(new FileReader(args[1])))
        {
            String json_str = readAllLines(br);

            ObjectMapper mapper = new ObjectMapper();
            try
            {
                expenses = mapper.readValue(json_str, new TypeReference<ArrayList<Expense>>(){});
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }

        }
        catch(FileNotFoundException e)
        {
            // if file not found, try to create the file 
            System.out.println(e+"\n"+"creating new file called " + args[1]);
            File file = new File(args[1]);
            try
            {
                if(file.createNewFile())
                {
                    System.out.println("File has been successfully created");
                }
            }
            catch(IOException e2)
            {
                System.out.println(""+e2.getMessage());
                System.exit(2);
            }
        }
        catch(IOException e)
        {
            System.out.println(""+e.getMessage());
            System.exit(2);
        }

    }


    // once json info is extracted this method then calls different methods depending on the command
    public static void runOptions(String[] args)
    {
        if(args.length == 2)
        {
            System.out.println(improper_use);
        }
        else
        {
            switch(args[2])
            {
                case "add":
                    addExpense();
                    break;
                case "list":
                    break;
                case "filter":
                    break;
                case "update":
                    break;          
                case "delete":
                    break;
                case "summary":
                    break; 
                default:
                    System.err.println("That option doesnt exist");  
            }
        }
    }

    //add a new expense
    public static void addExpense()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the data for the expense in this format: [Value] [Year|YYYY] [Month|MM] [Day|DD] [Category]");
        System.out.println("Make sure to only enter one of the following categories:\nHOUSING, UTILITIES, TRANSPORTATION, FOOD, HEALTHCARE, DEBT_PAYMENT, SAVINGS_AND_INVESTMENTS, PERSONAL_SPENDING");
        double value = in.nextDouble();
        int year = in.nextInt();
        int month = in.nextInt();
        int day = in.nextInt();
        String str = in.nextLine().trim();

        try{
            Expense.CATEGORY category = Expense.CATEGORY.valueOf(str);
            Expense expense = new Expense(value, year, month, day, category);
            expenses.add(expense);
        } catch(IllegalArgumentException e)
        {
            System.out.println("Invalid category: " + e.getMessage());
            System.exit(1);
        }

    }

    // save json to file
    public static void saveJson(String[] args)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.writeValue(new File(args[1]), expenses);
        }
        catch(IOException e)
        {
            System.out.println(""+e.getMessage());
            System.exit(2);
        }
    }

    public static void main(String[] args) {

        

        if(args.length == 0)
        {
            System.out.println(improper_use);
        }
        else
        {

            switch(args[0])
            {
                case "open":
                    loadJson(args);
                    runOptions(args);
                    break;
                default:                
                  System.out.println(improper_use);
                    break;
            }
        }

        saveJson(args);

    }
}
