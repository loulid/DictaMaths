package com.Dicta.DictaMaths;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    EditText mail,pass;
    Button b;

    TextView textViewLogin;
   //Button inscrire;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String afficher = ReadSettings(this);

            setContentView(R.layout.activity_main);


            //String fileNameStr="mot.txt";
            // String fileContentStr="The content of the file …";
       /* try {

            // To open you can choose the mode MODE_PRIVATE, MODE_APPEND,
            // MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE
            // This is the creation mode (Private, World Readable et World Writable),
            // Append is used to open the file and write at its end
            FileOutputStream fos= openFileOutput(fileNameStr, this.MODE_PRIVATE);
            // Open the writer
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fos);
            // Write
            outputStreamWriter.write(fileContentStr);
            // Close streams
            outputStreamWriter.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


            //textViewLogin=findViewById(R.id.signUpText);
            mail = findViewById(R.id.mail);
            pass = findViewById(R.id.pass);
            b = findViewById(R.id.login);

            // inscrire.setOnClickListener((View.OnClickListener) this);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Log().execute();
                }
            });
       /* textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),signup.class);
                startActivity(intent);
                finish();
            }
        });*/



    }

    class Log extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WriteSettings(MainActivity.this,"\n"+enleverLesEspaces("#"+ mail.getText().toString()+"#"));
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String,String> map=new HashMap<>();

            map.put("mail",mail.getText().toString());
            map.put("pass",pass.getText().toString());


            JSONObject object=parser.makeHttpRequest("http://handiman.univ-paris8.fr/~meriam/login/login.php","POST",map);
            try {
                success = object.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if(success==1)
            {
                Intent intent = new Intent(MainActivity.this, DictaActivity.class);
                startActivity(intent);
            }
            else
            {

                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Le nom de l'utilisateur ou/et le mot de passe sont incorrectes");
                alert.setNeutralButton("ok",null);
                alert.show();
            }

        }
    }

    public String ReadSettings(Context context){
        FileInputStream fIn = null;
        InputStreamReader isr = null;

        char[] inputBuffer = new char[255];
        String data = null;

        try{

            fIn = context.openFileInput("users5.txt");
            isr = new InputStreamReader(fIn);
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            //affiche le contenu de mon fichier dans un popup surgissant

        }
        catch (Exception e) {

        }

        return data;
    }
    public String enleverLesEspaces( String mot){
        String caractere="";
        String newMot="";
        int taille=0;
        for(int i=0; i <mot.length(); i++){
            caractere = mot.substring(taille, taille + 1);
            if(!caractere.equals(" ")){
                newMot=newMot+caractere;

            }
            taille ++;

        }
        return newMot;
    }

    public String dernierConnexion(String mot ){
        String caractere="";
        String newMot="";
        int compteur =0;

        for(int i=mot.length(); i>=1 ; i--) {
            caractere = mot.substring(i-1,i);
            if (compteur==1){

                newMot=caractere+newMot;
            }
            if(compteur==1 && caractere.equals("#")){
                break;

            }

            if(compteur==0 && caractere.equals("#")){
                compteur++;

            }



        }
        return newMot;


    }
    public void WriteSettings(Context context, String data){
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try{
            Boolean vider = deleteFile("users5.txt");
            fOut = context.openFileOutput("users5.txt",MODE_APPEND);
            osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();

        }
        catch (Exception e) {

        }
        finally {
            try {
                osw.close();
                fOut.close();
            } catch (IOException e) {

            }
        }
    }




}