package com.Dicta.DictaMaths;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    EditText mail, pass, nom, prenom;
    Button b;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        b = findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"blabla",Toast.LENGTH_SHORT).show();
                new Log().execute();
            }
        });


    }

    class Log extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(signup.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

             HashMap<String, String> map = new HashMap<>();
            map.put("nom", nom.getText().toString());
            map.put("prenom", prenom.getText().toString());
            map.put("mail", mail.getText().toString());
            map.put("pass", pass.getText().toString());


            JSONObject object = parser.makeHttpRequest("http://handiman.univ-paris8.fr/~meriam/login/signup.php", "POST", map);
            try {
                Toast.makeText(getApplicationContext(),"blabla",Toast.LENGTH_SHORT).show();
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

            if (success == 1) {
                AlertDialog.Builder alert = new AlertDialog.Builder(signup.this);
                alert.setMessage("Inscription effectuée ");
                alert.setNeutralButton("ok", null);
                alert.show();
            } else {

                AlertDialog.Builder alert = new AlertDialog.Builder(signup.this);
                alert.setMessage("L'inscription a echouée");
                alert.setNeutralButton("ok", null);
                alert.show();
            }

        }
    }
}
