package dg.webserviceusepoc;

/**
 * Created by KZ-Tech on 9/2/2017.
 */

public class ScanResult {
    private BarCodeClass BarCodeClass;

    public BarCodeClass getBarCodeClass ()
    {
        return BarCodeClass;
    }

    public void setBarCodeClass (BarCodeClass BarCodeClass)
    {
        this.BarCodeClass = BarCodeClass;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [BarCodeClass = "+BarCodeClass+"]";
    }
}
