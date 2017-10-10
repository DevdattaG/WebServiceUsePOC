package dg.webserviceusepoc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
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
    SoapPrimitive syncResultString;
    String filename = "myfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.bt);
        nxt = (Button) findViewById(R.id.nxt);
        celcius = (EditText) findViewById(R.id.cel);

//        for(int i = 1000; i<= 9999  ; i++)
//        {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formattedDate = df.format(Calendar.getInstance().getTime());
//            writeToFile("1808171" + String.valueOf(i) + "~" + formattedDate, getApplicationContext());
//        }

       // code="0903644441";
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCel = celcius.getText().toString();
          //      writeToFile(getCel,getApplicationContext());
              //  getVenues();
//                AsyncCallWS task = new AsyncCallWS();
//                task.execute();

                AsyncCallSyncConfig task = new AsyncCallSyncConfig();
                task.execute();
                readFromFile(getApplicationContext());
                if(!isConfigFileEmpty())
                {
                    String a = readFromConfigFile(getApplicationContext());
                    String a1 = a.split("~")[0];
                    String a2= a.split("~")[1];
                }else
                {

                }

                getFileSize(getApplicationContext());
               //deleteFile(getApplicationContext());
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

    @Override
    public void onBackPressed() {
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

    public boolean isConfigFileEmpty(){
        String a = readFromConfigFile(getApplicationContext());
        if(a.equals(""))
        {
            return true;
        }else
        {
            return false;
        }
    }

    private String readFromConfigFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("configFile");

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
           // getVenues();
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

    public void showPasswordDialogue(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a  = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void syncOfflineCodes(){
        String SOAP_ACTION = "http://tempuri.org/SyncOfflineBarcode";
        String METHOD_NAME = "SyncOfflineBarcode";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice1/barcodescanner.asmx";

        try {
            Log.d("Start", "Start sync");
            String syncInput = readFromFile(getApplicationContext());
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("strScanString", syncInput);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            //resultString = (SoapPrimitive) soapEnvelope.getResponse();
            syncResultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = syncResultString.toString();
            if(response.equals("Success"))
            {
                deleteFile(getApplicationContext());
            }else
            {
                Log.d("Sync","unsuccessful");
            }
            Log.d("result" , response);
            Log.d("End", "End sync");


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

    private class AsyncCallSyncConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Pre", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Task", "doInBackground");
           // syncOfflineCodes();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("done", "onPostExecute");
        }
    }

}
