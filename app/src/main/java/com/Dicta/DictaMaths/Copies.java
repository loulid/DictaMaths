package com.Dicta.DictaMaths;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Dicta.math.MathView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Copies extends AppCompatActivity {
    Button show;
    //TextView result;
    String afficher ="";
    RequestQueue requestQueue;
    String showUrl = "https://handiman.univ-paris8.fr/~meriam/login/showStudent.php";
    @BindView(R.id.math_view)
    MathView mathView;
    @BindView(R.id.editTextTextPersonName)
    EditText note2;
    @BindView(R.id.Copie)
    EditText copie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copie);
        show = (Button) findViewById(R.id.show);
        //result = (TextView) findViewById(R.id.result);
        afficher =  ReadSettings(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ButterKnife.bind(this);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        showUrl, null,  new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        try {
                            String stock="";
                            ArrayList<String> listCopies = new ArrayList<String>();
                            JSONArray fichiers = response.getJSONArray("fichiers");
                            for (int i=0 ; i < fichiers.length(); i++) {
                                JSONObject fichier = fichiers.getJSONObject(i);

                                String Copie = fichier.getString("Copie");
                                String idEleve = fichier.getString("idEleve");
                                String id = fichier.getString("Id");
                                String note = fichier.getString("note");
                                stock = "je suis la";
                                // if(idEleve.equals(dernierConnexion(afficher))){
                                //   listCopies.add(Copie);
                                // listCopies.add(id);
                                String recup = copie.getText().toString();
                                if (idEleve.equals(dernierConnexion(afficher))){
                                    if (id.equals(recup)) {

                                        //for(int j =0; j<listCopies.size();j++ ){
                                        //  stock= "\n   "+stock+"\n   "+listCopies.get(j);
                                        //}
                                        mathView.setText(Copie);
                                        note2.setText(note);
                                    }
                            }else{
                                    Toast.makeText(Copies.this, "Ce n'est pas votre copie",Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put ("idEleve",dernierConnexion(afficher));
                        return parameters;
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });
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
            Toast.makeText(context, " "+data,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, "Settings not read",Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    public String dernierConnexion(String mot ){
        String caractere="";
        String newMot="";
        int compteur =0;

        for(int i=mot.length(); i>=1 ; i--) {
            caractere = mot.substring(i-1,i);

            if(compteur==1 && caractere.equals("#")){
                break;

            }
            if (compteur==1){

                newMot=caractere+newMot;
            }
            if(compteur==0 && caractere.equals("#")){
                compteur++;
            }
        }
        return newMot;
    }
}