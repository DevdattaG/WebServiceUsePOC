package dg.webserviceusepoc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    String TAG = "Response";
    Button bt;
    EditText celcius;
    String getCel;
    String code = "";
   // SoapPrimitive resultString;
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
          //  Toast.makeText(MainActivity.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void calculate() {


        String url = "https://demo0600962.mockable.io/helloRestPostPOC";

// Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling
                        System.out.println(response);
                        Log.d(TAG, response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                Log.d(TAG, "Something went wrong!");
                error.printStackTrace();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);


//            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
//            final Pattern countAckPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
//            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
//            final Matcher countAckMatcher = acknowledgementPattern.matcher(response);
//            acknowledgementMatcher.find();
//            countAckMatcher.find();
//            Log.i(TAG, "Response caught : " + response);
//            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
//            System.out.println("CountAck Token  " +countAckMatcher.group(1));
//            if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "True")
//            {
//                Log.d(TAG,"Valid User... User not checked in yet");
//            }else if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "False")
//            {
//                Log.d(TAG,"Valid User... User has checked in already");
//            }else
//            {
//                Log.d(TAG,"Invalid User !!!");
//            }
//        } catch (Exception ex) {
//            Log.e(TAG, "Error: " + ex.getMessage());
//        }
    }
}
