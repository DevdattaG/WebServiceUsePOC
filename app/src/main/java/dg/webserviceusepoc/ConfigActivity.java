package dg.webserviceusepoc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SoapPrimitive venueResultString;
    SoapPrimitive gateResultString;
    Spinner spinner;
    Spinner gateSpinner;
    String venueId = "";
    String filename = "configFile";
    ArrayList<Venue> venueList = new ArrayList<Venue>();
    ArrayList<String> gateList = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ConfigActivity.AsyncCallConfig task = new ConfigActivity.AsyncCallConfig();
        task.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinner)
        {
            String item = parent.getItemAtPosition(position).toString();
            Venue venue = (Venue) parent.getSelectedItem();
            venueId = String.valueOf(venue.getVenueId());
            ConfigActivity.AsyncCallGateConfig taskGate = new ConfigActivity.AsyncCallGateConfig();
            taskGate.execute();
        }
        else if(spinner.getId() == R.id.gatespinner)
        {
            String item = parent.getItemAtPosition(position).toString();
          //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }

        //venue.getVenueId();  works well to find venueid;
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void populateVenueSpinner()
    {
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<Venue> dataAdapter = new ArrayAdapter<Venue>(ConfigActivity.this, android.R.layout.simple_spinner_item, venueList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);
    }

    public void populateGateSpinner()
    {
        gateSpinner = (Spinner) findViewById(R.id.gatespinner);

        gateSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ConfigActivity.this, android.R.layout.simple_spinner_item, gateList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gateSpinner.setAdapter(dataAdapter);

        gateSpinner.setOnItemSelectedListener(this);
    }

    public void getVenues()
    {
        String SOAP_ACTION = "http://tempuri.org/GetVenueDetails";
        String METHOD_NAME = "GetVenueDetails";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice1/barcodescanner.asmx";

        try {
            // int abc = 4444;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            // Request.addProperty("barcode", "29081990645");
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            //resultString = (SoapPrimitive) soapEnvelope.getResponse();
            venueResultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = venueResultString.toString();


            JSONArray arr = new JSONArray(response);
           // ArrayList<Venue> venueList = new ArrayList<Venue>();
            for(int i = 0; i < arr.length(); i++){
                venueList.add(new Venue(arr.getJSONObject(i).getString("VenueId"),arr.getJSONObject(i).getString("Venue")));
            }

            Log.d("result" , response);


        } catch (Exception ex) {
            Log.e("Error", "Error: " + ex.getMessage());
        }
    }

    public void getGates()
    {
        String SOAP_ACTION = "http://tempuri.org/GetGateInfo";
        String METHOD_NAME = "GetGateInfo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice1/barcodescanner.asmx";

        try {
            // int abc = 4444;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("LayoutId", venueId);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            //resultString = (SoapPrimitive) soapEnvelope.getResponse();
            gateResultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = gateResultString.toString();


            JSONArray arr = new JSONArray(response);
            gateList.clear();
            // ArrayList<Venue> venueList = new ArrayList<Venue>();
            for(int i = 0; i < arr.length(); i++){
                gateList.add(arr.getJSONObject(i).getString("Gate"));
            }

            Log.d("result" , response);


        } catch (Exception ex) {
            Log.e("Error", "Error: " + ex.getMessage());
        }
    }

    private class AsyncCallConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Pre", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Task", "doInBackground");
            getVenues();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("done", "onPostExecute");
            populateVenueSpinner();
        }
    }


    private class AsyncCallGateConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Pre", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Task", "doInBackground");
            getGates();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("done", "onPostExecute");
            populateGateSpinner();
        }
    }

    public void saveConfig(View view){
        final EditText deviceId = (EditText)findViewById(R.id.deviceId);
        final EditText password = (EditText)findViewById(R.id.password);
        if(deviceId.getText().toString().equals(""))
        {
            Log.d("config : ","Invalid");
            Toast.makeText(ConfigActivity.this, "Enter Device ID", Toast.LENGTH_SHORT).show();
        }else if(password.getText().toString().equals(""))
        {
            Log.d("config : ","Invalid");
            Toast.makeText(ConfigActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(!password.getText().toString().equals("0000"))
        {
            Log.d("config : ","Invalid");
            Toast.makeText(ConfigActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("config",deviceId.getText().toString());
            writeToConfigFile(deviceId.getText().toString() + "~" + gateSpinner.getSelectedItem().toString(), getApplicationContext() );
            Toast.makeText(ConfigActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            readFromConfigFile(getApplicationContext());
//            Intent validationPage = new Intent("android.intent.action.ValidationPage");
//            validationPage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            validationPage.putExtra("Code",deviceId.getText().toString());
//            startActivity(validationPage);
        }
    }

    private void writeToConfigFile(String data, Context context)
    {
        FileOutputStream outputStream;
        try {
           // data +=",";
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFromConfigFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.d("File read",ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}