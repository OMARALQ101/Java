import java.io.BufferedReader;
import java.io.IOException;

public class Entry
{
    enum STATUS{
        COMPLETE,
        INCOMPLETE
    }

    private STATUS status;
    private String description;


    public Entry(String description)
    {
        this.description = description;
        this.status = STATUS.INCOMPLETE;
    }

    public Entry(BufferedReader br) throws IOException
    {
        this.description = br.readLine();
        this.status = STATUS.valueOf(br.readLine());
    }

    public STATUS getStatus()
    {
        return status;
    }

    public String getDescription()
    {
        return description;
    }

    public void changeStatus(STATUS status)
    {
        this.status = status;
    }

    public void changeDescription(String description)
    {
        this.description = description;
    }

    
    @Override
    public String toString()
    {
        return description + "  ||  " + status.toString();
    }


}