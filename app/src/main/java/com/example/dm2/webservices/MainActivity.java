package com.example.dm2.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText tvalor;
    private EditText torigen;
    private EditText tdestino;
    private EditText tresultado;
    private String res ="";
    private String num ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvalor=findViewById(R.id.tvalor);
        torigen=findViewById(R.id.torigen);
        tdestino=findViewById(R.id.tdestino);
        tresultado=findViewById(R.id.tresultado);
    }
    public void calcular(View v){
        String valor = tvalor.getText().toString();
        String origen = torigen.getText().toString();
        String destino = tdestino.getText().toString();
        AsyncPost task = new AsyncPost();
        task.execute(valor,origen,destino);
    }

    private class AsyncPost extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpURLConnection conn;
                URL url = new URL("http://www.webservicex.net/length.asmx/ChangeLengthUnit");
                String param ="LengthValue="+ URLEncoder.encode(params[0],"UTF-8")+"&fromLengthUnit="+URLEncoder.encode(params[1],"UTF-8")+"&toLengthUnit="+URLEncoder.encode(params[2],"UTF-8");

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(param.getBytes().length);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(param);
                out.close();
                String result = "";
                Scanner inStream = new Scanner(conn.getInputStream());
                boolean a=false;
                while (inStream.hasNextLine()) {
                    result = inStream.nextLine();
                    if (result.indexOf("double") > 0) {
                        a = true;
                    }
                    if (a) {
                        res += result;
                        if (result.indexOf("double") > 0)
                            num = result.replace("<double>", "").replace("</double>", "");
                    }
                }

            }catch (Exception e) {
                Log.e("Error", "excepcion Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
                Toast.makeText(MainActivity.this,"Resultado: " + res.substring(res.indexOf(">") + 1, res.length() - 9), Toast.LENGTH_LONG).show();
        }
    }
}
