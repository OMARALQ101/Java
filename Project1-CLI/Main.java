
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main
{
    public static ArrayList<Entry> to_do_entries = new ArrayList<>();    

    public static void flush()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    public static void loadData(BufferedReader br) throws IOException
    {
        int size = Integer.parseInt(br.readLine());
        for(int i = 0; i < size; i++)
        {
            Entry entry = new Entry(br);
            to_do_entries.add(entry);
        }
    }

    public static void saveData(BufferedWriter bw) throws IOException
    {
        int size = to_do_entries.size();
        bw.write(""+size+'\n');
        for(int i = 0; i < size; i++)
        {
            bw.write(to_do_entries.get(i).getDescription() +'\n');
            bw.write(to_do_entries.get(i).getStatus().toString() +'\n');
        }
    }

    
    public static void main(String[] args) throws IOException
    {
        Scanner in = new Scanner(System.in);
        String filename;
        System.out.print("Please enter the file name where your your TO-DO list is stored or enter a new file name: ");
        filename = in.nextLine();
        flush();

        //add .txt if not there
        if(!filename.endsWith(".txt"))
        {
            filename = filename + ".txt";
        }


        //create file if file doesnt exist or load in data if file exists
        File file = new File(filename);  
        
        if (file.createNewFile())
        {
            System.out.println("Couldn't find a file with that name, creating new save file.");
        }
        else
        {

            try(BufferedReader br = new BufferedReader(new FileReader(filename)))
            {
                loadData(br);
            }
            catch(IOException e)
            {
                System.out.println("An error has occured" + e.toString());
                System.exit(0);
            }

            
        }

        
        //loop for responses
        boolean running = true;
        char letter;
        int number;
        String description;
        Entry entry;
        String message = null;

        while(running)
        {
            System.out.println("Welcome to your To-Do List!\n\n\n");
            
            for (int i = 0; i < to_do_entries.size(); i++) 
            {
                System.out.println( i+1 + ". " + to_do_entries.get(i).toString());
            }

            System.out.println("\n\n\n");


            System.out.println("a. Change entry status");
            System.out.println("b. add new entry");
            System.out.println("c. edit entry descriptiion");
            System.out.println("d. delete entry");
            System.out.println("e. exit program");

            if(message != null)
            {
                System.out.println(message);
                message = null;
            }

            System.out.print("What would you like to do? (just enter the letter): ");

            letter = Character.toLowerCase(in.next().charAt(0));


            switch (letter) {
                case 'a':
                    if(to_do_entries.isEmpty())
                    {
                        message = "No entries available";
                    }
                    else
                    {
                        System.out.print("enter entry number: ");
                        number = in.nextInt();
    
                        while( (number) > to_do_entries.size() || (number-1) < 0)
                        {
                            System.out.println("Invalid number: ");
                            System.out.print("enter entry number: ");
                            number = in.nextInt();
                        }
    
                        entry = to_do_entries.get(number-1);
    
                        if(entry.getStatus().equals(Entry.STATUS.COMPLETE))
                        {
                            entry.changeStatus(Entry.STATUS.INCOMPLETE);
                        }
                        else
                        {
                            entry.changeStatus(Entry.STATUS.COMPLETE);
                        }
                    }

                    break;
                case 'b':
                    in.nextLine(); //remove \n
                    System.out.print("enter entry's description: ");
                    description = in.nextLine();
                    Entry new_entry = new Entry(description);

                    to_do_entries.add(new_entry);
                    
                    break;
                case 'c':
                    if(to_do_entries.isEmpty())
                    {
                        message = "No entries available";
                    }
                    else
                    {
                        System.out.print("enter entry number: ");
                        number = in.nextInt();
    
                        while( (number) > to_do_entries.size() || (number-1) < 0)
                        {
                            System.out.println("Invalid number!");
                            System.out.print("enter entry number: ");
                            number = in.nextInt();
                        }
    
                        in.nextLine(); //remove \n
                        System.out.print("\nenter entry's new description: ");
                        description = in.nextLine();
    
                        entry = to_do_entries.get(number-1);
                        entry.changeDescription(description);
                    }

                    break;
                case 'd':
                    if(to_do_entries.isEmpty())
                    {
                        message = "No entries available";
                    }
                    else
                    {   
                        System.out.print("enter entry number: ");
                        number = in.nextInt();
    
                        while( (number) > to_do_entries.size() || (number-1) < 0)
                        {
                            System.out.println("Invalid number!");
                            System.out.print("enter entry number: ");
                            number = in.nextInt();
                        }
                        
                        to_do_entries.remove(number-1);
                    }
                    
                    break;  
                case 'e': 
                    running = false;

                    break;  
                default:
                    System.out.println("Invalid option!");
            }

            flush();

        }


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false)))
        {
            saveData(bw);
        }
        catch(IOException e)
        {
            System.out.println("An error has occured" + e.toString());
            System.exit(0);
        } 
    
    }
}