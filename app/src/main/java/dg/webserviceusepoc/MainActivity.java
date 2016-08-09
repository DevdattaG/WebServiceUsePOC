package dg.webserviceusepoc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends ActionBarActivity {

    String TAG = "Response";
    Button bt;
    EditText celcius;
    String getCel;
   // SoapPrimitive resultString;
   // SoapObject resultString;
    String resultString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.bt);
        celcius = (EditText) findViewById(R.id.cel);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCel = celcius.getText().toString();
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Toast.makeText(MainActivity.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/BarcodeStatus";
        String METHOD_NAME = "BarcodeStatus";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice/barcodescanner.asmx";

        try {
            int abc = 4444;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("barcode", Integer.parseInt(getCel));
            //Request.addProperty("CountryName", abc);

//            PropertyInfo pi4 = new PropertyInfo();
//            pi4.setName("ElementName");
//            pi4.setValue("Gold");// get the string that is to be sent to the webservice
//            pi4.setType(String.class);
//            Request.addProperty(pi4);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
           // resultString = (SoapPrimitive) soapEnvelope.getResponse();
            resultString = soapEnvelope.getResponse().toString();
            Log.i(TAG, "Result Celsius: " + resultString.toString());
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
