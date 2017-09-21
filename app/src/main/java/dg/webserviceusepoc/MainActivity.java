package dg.webserviceusepoc;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    String TAG = "Response";
    Button bt;
    Button nxt;
    EditText celcius;
    String getCel;
    String code = "";
    SoapPrimitive resultString;
    String filename = "myfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.bt);
        nxt = (Button) findViewById(R.id.nxt);
        celcius = (EditText) findViewById(R.id.cel);

//        for(long i = 2011347100; i<= 2011357100 ; i++)
//        {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formattedDate = df.format(Calendar.getInstance().getTime());
//            writeToFile(String.valueOf(i) + "~" + formattedDate, getApplicationContext());
//        }

       // code="0903644441";
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCel = celcius.getText().toString();
               // writeToFile(getCel,getApplicationContext());
              //  getVenues();
                AsyncCallWS task = new AsyncCallWS();
                task.execute();

                //readFromFile(getApplicationContext());
                //getFileSize(getApplicationContext());
             //  deleteFile(getApplicationContext());
            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), ConfigActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean deleteFile(Context context)
    {
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
        return deleted;
    }

    private long getFileSize(Context context)
    {
            File dir = getFilesDir();
            File file = new File(dir, filename);
            long length = file.length();
            Log.d("File size", String.valueOf(length));
            return length;
    }

    private void writeToFile(String data, Context context)
    {
        FileOutputStream outputStream;
        try {
            data +=",";
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFromFile(Context context) {

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

    public void playMySound(String soundName){
        Uri uri=Uri.parse("android.resource://" + getPackageName() + "/" +
                "raw" + "/" + soundName);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


//        switch (soundId)
//        {
//            case 1:
//            {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alreadyin);
//                break;
//            }
//            case 2:
//            {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alreadyin);
//                break;
//            }
//            case 3:
//            {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alreadyin);
//                break;
//            }
//        }

        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
               // mp.stop();
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
            getVenues();
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Toast.makeText(MainActivity.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
            double rand = Math.random() * 100;
            switch(((int)rand)%3)
            {
                case 1:
                {
                    playMySound("welcome");
                    break;
                }
                case 2:
                {
                    playMySound("alreadyin");
                    break;
                }
                case 0:
                {
                    playMySound("invalid");
                    break;
                }
            }


        }

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
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = resultString.toString();


            JSONArray arr = new JSONArray(response);
            List<Venue> list = new ArrayList<Venue>();
            for(int i = 0; i < arr.length(); i++){
                list.add(new Venue(arr.getJSONObject(i).getString("VenueId"),arr.getJSONObject(i).getString("Venue")));
            }

            Log.d("result" , response);


        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/BarcodeStatus";
        String METHOD_NAME = "BarcodeStatus";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice1/barcodescanner.asmx";

        try {
           // int abc = 4444;
            code = "29081990645";
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("barcode", "29081990645");
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            //resultString = (SoapPrimitive) soapEnvelope.getResponse();
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = resultString.toString();

            // REGEX case : 1

            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Pattern countAckPattern = Pattern.compile("<CountAck>(.+?)</CountAck>");
            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
            final Matcher countAckMatcher = countAckPattern.matcher(response);
            acknowledgementMatcher.find();
            countAckMatcher.find();
            Log.i(TAG, "Response caught : " + response);
            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
            System.out.println("CountAck Token  " +countAckMatcher.group(1));
            if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("True"))
            {
                Log.d(TAG,"Valid User... User not checked in yet");
            }else if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("False"))
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
