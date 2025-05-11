package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.Expense;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {

    public static ArrayList<Expense> expenses;

    public static String improper_use = "To use the Expense Tracker Application you have the following options:\n"+
                    "java -jar app/build/libs/app-all.jar open [filename] [command]\n "
                    + "Here are the availabe commands:\n" + 
                    "1. add\n2. list\n3. filter\n4. update\n5. delete\n6. summary"
                ;

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


    public static void openFile(String[] args) 
    {

        try(BufferedReader br = new BufferedReader(new FileReader(args[1])))
        {
            String json_str = readAllLines(br);
            // System.out.println(json_str);

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
        catch(IOException e)
        {
            System.out.println(""+e.getMessage());
            System.exit(2);
        }

        if(args.length == 2)
        {
            System.out.println(improper_use);
        }
        else
        {

            switch(args[2])
            {
                case "add":
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
                    openFile(args);    
                    break;
                default:                
                  System.out.println(improper_use);
    
                    break;
            }
        }

    }
}
