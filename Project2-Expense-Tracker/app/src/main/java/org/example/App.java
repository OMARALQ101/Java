package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
                     + space + "4. update -id [id] -value [value] -desc [description]\n"
                     + space + "5. delete -id [id]\n" 
                    + space + "6. summary -month [YYYY-MM-DD]\n"
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
                    addExpense(args);
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
            System.out.println(e.getMessage());
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
                    saveJson(args);
                    break;
                default:                
                  System.out.println(improper_use);
                    break;
            }
        }

        

    }
}
