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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Passenger extends AppCompatActivity {
    Connect connect;
    TextView username,location;
    Spinner onbus, offbus;
    Button order,where,news;
    String result="0";
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
                String onstop = (String)params[1];
                String offstop = (String)params[2];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("onstop","UTF-8")+"="+URLEncoder.encode(onstop,"UTF-8");
                data+="&"+URLEncoder.encode("offstop","UTF-8")+"="+URLEncoder.encode(offstop,"UTF-8");
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
        setContentView(R.layout.activity_passenger);

        connect=new Connect();

        username = (TextView)findViewById(R.id.username);
        Intent intentMyProf=getIntent();
        username.setText("WOW, I AM "+intentMyProf.getStringExtra("username"));

        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,stops);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        onbus=(Spinner)findViewById(R.id.onbus);
        onbus.setAdapter(adapter);
        offbus=(Spinner)findViewById(R.id.offbus);
        offbus.setAdapter(adapter);
        final String[] onstop = new String[1];
        final String[] offstop = new String[1];
        onbus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onstop[0] =adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        offbus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                offstop[0] =adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        order=(Button)findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                connect.execute("order", onstop[0],offstop[0]);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(result.equals("0"))
                {
                    System.out.println("44444444");
                }
                Toast.makeText(Passenger.this,"ORDER SUCCESS", Toast.LENGTH_SHORT).show();

                connect=new Connect();
            }
        });
        location=(TextView)findViewById(R.id.location);
        where=(Button)findViewById(R.id.where);
        where.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                String locationtext="";
                connect.execute("location","1","1");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(result.equals("0"))
                {
                    System.out.println("44444444");
                }
                String[] temp;
                String delimeter = ";";
                System.out.println(result);
                temp = result.split(delimeter);
                for(int i=0;i<temp.length;i++)
                {
                    if(i==0)
                    {
                        locationtext=locationtext+temp[0];
                    }
                    else
                    {
                        locationtext=locationtext+"\n"+temp[i];
                    }
                }
                location.setText(locationtext);
                connect=new Connect();
            }
        });
        news=(Button)findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                connect.execute("news","1","1");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(result.equals("0"))
                {
                    System.out.println("44444444");
                }

                location.setText(result);
                connect=new Connect();
            }
        });

    }
}
