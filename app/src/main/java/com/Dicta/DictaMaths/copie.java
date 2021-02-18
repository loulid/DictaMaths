package com.Dicta.DictaMaths;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class copie extends AppCompatActivity {
    Button show;
    TextView result;
    RequestQueue requestQueue;
    String showUrl = "https://handiman.univ-paris8.fr/~meriam/Copie/showStudent.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copie);
        show = (Button) findViewById(R.id.show);
        //result = (TextView) findViewById(R.id.);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        showUrl, null,  new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray fichiers = response.getJSONArray("fichiers");
                            for (int i=0 ; i < fichiers.length(); i++){
                                JSONObject fichier = fichiers.getJSONObject(i);

                                String Copie = fichier.getString("Copie");
                              //  String idEleve = fichier.getString("idEleve");

                                result.append(Copie +"\n");
                            }
                            result.append("===\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}