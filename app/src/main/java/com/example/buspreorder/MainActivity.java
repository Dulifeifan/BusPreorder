package com.example.buspreorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Connect connect;
    Button login, register;
    EditText username, password;
    String result="0";

    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String username = (String)params[1];
                String password = (String)params[2];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        setContentView(R.layout.activity_main);




        username= (EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);


        login=(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                String login_username=username.getText().toString();
                String login_password=password.getText().toString();
                if(login_username.length()==0||login_password.length()==0)
                {
                    Toast.makeText(MainActivity.this,"INPUT SOMETHING!", Toast.LENGTH_SHORT).show();
                }
                else {
                    connect.execute("loginp", login_username, login_password);
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
                    for (int i = 0; i < temp.length; i++) {
                        System.out.println(temp[i]);
                    }
                    if (temp[0].equals("Succeed")) {
                        Toast.makeText(MainActivity.this, "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();
                        if (temp[2].equals("p")) {
                            Intent intent_login_passenger = new Intent(MainActivity.this, Passenger.class);
                            intent_login_passenger.putExtra("username", temp[1]);
                            startActivity(intent_login_passenger);
                        }
                        if (temp[2].equals("d")) {
                            Intent intent_login_driver = new Intent(MainActivity.this, Driver.class);
                            intent_login_driver.putExtra("username", temp[1]);
                            startActivity(intent_login_driver);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                    }
                    connect = new Connect();
                }
            }
        });
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect=new Connect();
                String reg_username=username.getText().toString();
                String reg_password=password.getText().toString();
                if(reg_username.length()==0||reg_password.length()==0)
                {
                    Toast.makeText(MainActivity.this,"INPUT SOMETHING!", Toast.LENGTH_SHORT).show();
                }
                else {
                    connect.execute("register", reg_username, reg_password);
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
                        Toast.makeText(MainActivity.this, "REGISTER SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent intent_prof = new Intent(MainActivity.this, Passenger.class);
                        intent_prof.putExtra("username", temp[1]);
                        startActivity(intent_prof);
                    } else {
                        Toast.makeText(MainActivity.this, "REGISTER FAILED", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        connect=new Connect();


    }
}
