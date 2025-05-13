package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.Date;

import org.Expense;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    public static ArrayList<Expense> expenses;
    //Just some spaces
    public static String space = "                                                     ";

    // this is going to be printed whenever there isnt enough arguments or misuse of application
    public static String improper_use = """
                                        To use the Expense Tracker Application you have the following options:
                                        java -jar app/build/libs/app-all.jar open [filename] [command]
                                         """
                    + space + "1. add -value [value] -desc [description] -date [YYYY-MM-DD] -category [category]\n" 
                     + space + "2. list -sort_asc [amount or date] || -sort_desc [amount or date]\n"
                     + space + "3. filter -category [category] -from [YYYY-MM-DD] -to [YYYY-MM-DD] -min [value] -max[value]\n"
                     + space + "4. update -id [id] -value [value] -desc [description] -date [YYYY-MM-DD] -category [category]\n"
                     + space + "5. delete -id [id]\n" 
                    + space + "6. summary -month [YYYY-MM]\n"
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
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
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
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
                System.exit(2);
            }
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
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
                    addExpense(args);
                    break;
                case "list":
                    listExpense(args);
                    break;
                case "filter":
                    filterExpense(args);
                    break;
                case "update":
                    updateExpense(args);
                    break;          
                case "delete":
                    deleteExpense(args);
                    break;
                case "summary":
                    summaryExpense(args);
                    break; 
                default:
                    System.err.println("That option doesnt exist");
                    System.out.println(improper_use);  
            }
        }
    }

    //summarize the expenses
    public static void summaryExpense(String[] args)
    {
        Options options = new Options();

        options.addOption("month", true, "month for the expense in YYYY-MM format");

        try
        {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            ArrayList<Expense> expenseSummary = new ArrayList<>();
            expenseSummary.addAll(expenses);

            if(cmd.hasOption("month"))
            {
                
                String[] date = cmd.getOptionValue("month").split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);

                Iterator<Expense> iter = expenseSummary.iterator();
                while (iter.hasNext())
                {
                    Expense x = iter.next();
                    if(!(x.getDate().getYear() == year) || !(x.getDate().getMonth() == month))
                    {
                        iter.remove();
                    }

                }
            }

            for (var x : expenseSummary)
            {
                System.out.println(x);
            }



        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    //delete an expense
    public static void deleteExpense(String[] args)
    {
        Options options = new Options();
        options.addOption("id", true, "id for the expense you want to delete");

        try
        {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("id"))
            {
                throw new IllegalArgumentException("Please, when deleting an expense, include the id of the expense you would like to delete");
            }

            int id = Integer.parseInt(cmd.getOptionValue("id"));
            boolean idExist = false;
            Iterator<Expense> iter = expenses.iterator();

            while (iter.hasNext())
            {
                if(iter.next().getId() == id)
                {
                    idExist = true;
                    System.out.println("deleted the expense with id " + id);
                    iter.remove();
                }

            }    
            
            if (!idExist)
            {
                throw new NoSuchElementException("The expense with id "+ id + " does not exist");
            }

        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    //update an expense
    public static void updateExpense(String[] args)
    {
        Options options = new Options();

        options.addOption("id", true, "id for the expense you want to change");
        options.addOption("value", true, "amount used for the expense");
        options.addOption("desc", true, "description for the expense");
        options.addOption("date", true, "date for the expense in YYYY-MM-DD format");
        options.addOption("category", true, "category for the expense");

        try
        {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("id"))
            {
                throw new IllegalArgumentException("Please, when adding an expense, include the id of the expense you would like to change");
            }

            if (!cmd.hasOption("value") && !cmd.hasOption("desc") && !cmd.hasOption("date") && !cmd.hasOption("category"))
            {
                throw new IllegalArgumentException("Please, when updating an expense, include one of the 4 options: -value -desc -date -category");
            }

            int id = Integer.parseInt(cmd.getOptionValue("id"));
            boolean idExist = false;

            for (Expense x : expenses)
            {
                if(x.getId() == id)
                {
                    Expense expenseUpdate = x;
                    idExist = true;

                    // update value if option available
                    if(cmd.hasOption("value"))
                    {
                        double value = Double.parseDouble(cmd.getOptionValue("value"));
                        expenseUpdate.setValue(value);
                    }

                    // update description if option available
                    if(cmd.hasOption("desc"))
                    {
                        String desc = cmd.getOptionValue("desc");
                        expenseUpdate.setDescription(desc);
                    }

                    // update date if option available
                    if(cmd.hasOption("date"))
                    {
                        String[] date = cmd.getOptionValue("date").split("-");
                        Date dateUpdate = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));   
                        expenseUpdate.setDate(dateUpdate);
                    }

                    // update category if option available
                    if(cmd.hasOption("category"))
                    {
                        String category = cmd.getOptionValue("category");  
                        expenseUpdate.setCategory(category);
                    }

                    System.out.println("Updated the expense: " + expenseUpdate.toString());
                }

            }
            
            if (!idExist)
            {
               throw new NoSuchElementException("The expense with id "+ id + " does not exist");
            }

        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // filter expenses
    public static void filterExpense(String[] args)
    {
        Options options = new Options();

        options.addOption("category", true, "category for the expense");
        options.addOption("from", true, "starting date for the expense");
        options.addOption("to", true, "ending date for the expense");
        options.addOption("min", true, "minimum value for the expense");
        options.addOption("max", true, "maximum value for the expense");

        try
        {
            ArrayList<Expense> expenseFilter = new ArrayList<>();
            expenseFilter.addAll(expenses);

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            // If the option "category" exists, remove every expense that isnt in the same category as "category"
            if(cmd.hasOption("category"))
            {
                String category = cmd.getOptionValue("category").trim().toLowerCase();

                Iterator<Expense> iter = expenseFilter.iterator();
                while(iter.hasNext())
                {
                    if (!iter.next().getCategory().equalsIgnoreCase(category))
                    {
                        iter.remove();
                    }
                }

            }

            // If the option "from" exists, remove every expense that has a date before "from"
            if(cmd.hasOption("from"))
            {
                String[] from = cmd.getOptionValue("from").toLowerCase().trim().split("-");

                Date from_date = new Date(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(from[2]));
                
                Iterator<Expense> iter = expenseFilter.iterator();
                while(iter.hasNext())
                {
                    if (iter.next().compareDate(from_date) < 0)
                    {
                        iter.remove();
                    }
                }

            }

            // If the option "to" exists, remove every expense that has a date after "to"
            if(cmd.hasOption("to"))
            {
                String[] to = cmd.getOptionValue("to").toLowerCase().trim().split("-");

                Date to_date = new Date(Integer.parseInt(to[0]), Integer.parseInt(to[1]), Integer.parseInt(to[2]));
                
                Iterator<Expense> iter = expenseFilter.iterator();
                while(iter.hasNext())
                {
                    if (iter.next().compareDate(to_date) > 0)
                    {
                        iter.remove();
                    }
                }

            }

            // If the option "min" exists, remove every expense that is less than "min"
            if(cmd.hasOption("min"))
            {
                Double min = Double.parseDouble(cmd.getOptionValue("min"));

                Iterator<Expense> iter = expenseFilter.iterator();
                while(iter.hasNext())
                {
                    if (iter.next().compareAmount(min) < 0)
                    {
                        iter.remove();
                    }
                }

            }

            // If the option "max" exists, remove every expense that is greater than "max"
            if(cmd.hasOption("max"))
            {
                Double max = Double.parseDouble(cmd.getOptionValue("max"));

                Iterator<Expense> iter = expenseFilter.iterator();
                while(iter.hasNext())
                {
                    if (iter.next().compareAmount(max) > 0)
                    {
                        iter.remove();
                    }
                }

            }

            //Print whatever is left from expenseFilter
            if(expenseFilter.isEmpty())
            {
                System.out.println("No expense found");
            }
            else
            {
                for (Expense x : expenseFilter)
                {
                    System.out.println(x.toString());
                }
            }

        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    //list expenses by sorting them by either amount or date in ascending or descending order
    public static void listExpense(String[] args)
    {
        Options options = new Options();

        options.addOption("sort_asc", true, "to sort an amount or date in ascending order");
        options.addOption("sort_desc", true, "to sort an amount or date in descending order");

        try
        {
            ArrayList<Expense> expenseList = expenses; 

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("sort_asc") && !cmd.hasOption("sort_desc"))
            //default option is to sort date in ascending order
            {
                expenseList.sort((a, b) -> { return a.compareDate(b); });
                for(var x : expenseList)
                {
                    System.out.println(x.toString());
                }
            }
            else if (cmd.hasOption("sort_asc") && cmd.hasOption("sort_desc"))
            {
                throw new IllegalArgumentException("Please, when listing expenses, you can not sort them by ascending and descending order at the same time");
            }
            else if (cmd.hasOption("sort_asc"))
            {
                String sortBy = cmd.getOptionValue("sort_asc");
                if (!sortBy.toLowerCase().trim().equals("date") && !sortBy.toLowerCase().trim().equals("amount"))
                {
                    throw new IllegalArgumentException("Please, when listing expenses, you can only sort by the amount or by the date");
                }
                else if (sortBy.toLowerCase().equals("date"))
                {
                    expenseList.sort((a, b) -> { return a.compareDate(b); });
                    for(var x : expenseList)
                    {
                        System.out.println(x.toString());
                    }
                }
                else
                {
                    expenseList.sort((a, b) -> { return a.compareAmount(b); });
                    for(var x : expenseList)
                    {
                        System.out.println(x.toString());
                    }
                }
            }
            else
            {
                String sortBy = cmd.getOptionValue("sort_desc");
                if (!sortBy.toLowerCase().trim().equals("date") && !sortBy.toLowerCase().trim().equals("amount"))
                {
                    throw new IllegalArgumentException("Please, when listing expenses, you can only sort by the amount or by the date");
                }
                else if (sortBy.toLowerCase().equals("date"))
                {
                    expenseList.sort((a, b) -> { return -1* a.compareDate(b); });
                    for(var x : expenseList)
                    {
                        System.out.println(x.toString());
                    }
                }
                else
                {
                    expenseList.sort((a, b) -> { return -1* a.compareAmount(b); });
                    for(var x : expenseList)
                    {
                        System.out.println(x.toString());
                    }
                }
            }

        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    //add a new expense
    public static void addExpense(String[] args)
    {
        
        Options options = new Options();

        options.addOption("value", true, "amount used for the expense");
        options.addOption("desc", true, "description for the expense");
        options.addOption("date", true, "date for the expense in YYYY-MM-DD format");
        options.addOption("category", true, "category for the expense");

        try
        {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("value") || !cmd.hasOption("desc") || !cmd.hasOption("date") || !cmd.hasOption("category"))
            {
                throw new IllegalArgumentException("Please, when adding an expense, include the 4 options: -value -desc -date -category");
            }

            double value = Double.parseDouble(cmd.getOptionValue("value"));
            String desc = cmd.getOptionValue("desc");
            String[] date = cmd.getOptionValue("date").split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);            
            String category = cmd.getOptionValue("category");

            Expense exp = new Expense(value, desc, year, month, day, category);
            System.out.println("Added the expense: " + exp.toString());
            expenses.add(exp);
        }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
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
                    saveJson(args);
                    break;
                default:                
                  System.out.println(improper_use);
                    break;
            }
        }

        

    }
}
