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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    String TAG = "Response";
    Button bt;
    EditText celcius;
    String getCel;
    String code = "";
    SoapPrimitive resultString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.bt);
        celcius = (EditText) findViewById(R.id.cel);
        code = "236111082677";

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
           // int abc = 4444;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("barcode", code);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = resultString.toString();
            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Pattern countAckPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
            final Matcher countAckMatcher = acknowledgementPattern.matcher(response);
            acknowledgementMatcher.find();
            countAckMatcher.find();
            Log.i(TAG, "Response caught : " + response);
            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
            System.out.println("CountAck Token  " +countAckMatcher.group(1));
            if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "True")
            {
                Log.d(TAG,"Valid User... User not checked in yet");
            }else if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "False")
            {
                Log.d(TAG,"Valid User... User has checked in already");
            }else
            {
                Log.d(TAG,"Invalid User !!!");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
