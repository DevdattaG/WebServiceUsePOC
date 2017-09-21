package dg.webserviceusepoc;

/**
 * Created by KZ-Tech on 9/2/2017.
 */

public class BarCodeClass {
    private String message;

    private String Acknowledgement;

    private String CountAck;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getAcknowledgement ()
    {
        return Acknowledgement;
    }

    public void setAcknowledgement (String Acknowledgement)
    {
        this.Acknowledgement = Acknowledgement;
    }

    public String getCountAck ()
    {
        return CountAck;
    }

    public void setCountAck (String CountAck)
    {
        this.CountAck = CountAck;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", Acknowledgement = "+Acknowledgement+", CountAck = "+CountAck+"]";
    }
}
