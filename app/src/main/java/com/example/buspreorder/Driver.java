package com.example.buspreorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Driver extends AppCompatActivity {
    Connect connect;
    TextView username,statetext,nextstop;
    String usernametext;
    EditText news;
    Button publishnews,publishnow,start;
    Spinner stopspinner;
    String result="0";
    String[] location = new String[1];
    String[] stops={"SouthSide","Farrington Square(to Saucon Village)","Whitaker Lab","Williams Hall","Drown Hall",
            "Taylor College(to Saucon Village)","Sigma Phi Epsilon"
            ,"House 93","Alpha Phi","IMBT Labs(to Saucon Village)","Iacocca Hall(to Saucon Village)",
            "Iacocca C Wing(to Saucon Village)","Building C(to Saucon Village)","Saucon Village"
            ,"Goodman","Building C(to SouthSide)","Iacocca C Wing(to SouthSide)","Iacocca Hall(to SouthSide)",
            "IMBT Labs(to SouthSide)","Alpha Tau Omega","Gamma Phi Beta","Pi Beta phi","Taylor College(to SouthSide)",
            "Alumni Memorial Building","STEPS","Farrington Square(to SouthSide)"};

    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String stop = (String)params[1];
                String username = (String)params[2];
                String news=(String)params[3];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("stop","UTF-8")+"="+URLEncoder.encode(stop,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("news","UTF-8")+"="+URLEncoder.encode(news,"UTF-8");
                String link="http://10.0.2.2:8888/demo/index.php";
                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                Log.d("My Result: ", sb.toString());
                result=sb.toString();
            }
            catch (Exception e){
                result="Exception: " + e.getMessage();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        connect=new Connect();
        username = (TextView)findViewById(R.id.username);
        Intent intentMyProf=getIntent();
        usernametext=intentMyProf.getStringExtra("username");
        username.setText("WOW, I AM "+intentMyProf.getStringExtra("username"));
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,stops);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopspinner=(Spinner)findViewById(R.id.location);
        stopspinner.setAdapter(adapter);

        stopspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location[0] =adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        start=(Button)findViewById(R.id.start);
        statetext=(TextView)findViewById(R.id.statetext);
        nextstop=(TextView)findViewById(R.id.nextstop);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                connect.execute("driving", location[0],usernametext,"1");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(result.equals("0"))
                {
                    System.out.println("44444444");
                }
                //Toast.makeText(D.this,"Order Success!", Toast.LENGTH_SHORT).show();

                stopspinner.setVisibility(View.GONE);
                statetext.setText("NEXT STOP I AM GOING TO: ");
                start.setText("CONTINUE DRIVING");

                String[] temp;
                String delimeter = ";";
                temp = result.split(delimeter);
                if (temp[0].equals("Succeed")) {
                    //Toast.makeText(MainActivity.this, "REGISTER SUCCESS", Toast.LENGTH_SHORT).show();
                    //Intent intent_prof = new Intent(MainActivity.this, Passenger.class);
                    //intent_prof.putExtra("username", temp[1]);
                    //startActivity(intent_prof);
                    nextstop.setVisibility(View.VISIBLE);
                    location[0]=temp[1];
                    nextstop.setText(temp[1]);
                } else {
                    //Toast.makeText(MainActivity.this, "REGISTER FAILED", Toast.LENGTH_SHORT).show();
                    statetext.setText("NOW I CAN HAVE A REST.");
                    nextstop.setVisibility(View.INVISIBLE);

                }

                connect=new Connect();

            }

        });

        publishnews=(Button)findViewById(R.id.publishnews);
        news=(EditText)findViewById(R.id.news);
        publishnow=(Button)findViewById(R.id.publishnow);
        publishnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                news.setVisibility(View.VISIBLE);
                publishnow.setVisibility(View.VISIBLE);
            }
        });
        publishnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                String newstext=news.getText().toString();
                if(newstext.length()==0)
                {
                    Toast.makeText(Driver.this,"INPUT SOMETHING!", Toast.LENGTH_SHORT).show();
                }
                else {

                    connect.execute("publish","1","1", newstext);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (result.equals("0")) {
                        System.out.println("44444444");
                    }
                    String[] temp;
                    String delimeter = ";";
                    temp = result.split(delimeter);
                    if (temp[0].equals("Succeed")) {
                        Toast.makeText(Driver.this, "PUBLISH SUCCESS", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Driver.this, "PUBLISH FAILED", Toast.LENGTH_SHORT).show();
                    }


                    connect=new Connect();
                    news.setVisibility(View.INVISIBLE);
                    publishnow.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
