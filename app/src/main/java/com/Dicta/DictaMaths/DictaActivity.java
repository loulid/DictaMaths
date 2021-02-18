package com.Dicta.DictaMaths;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictaActivity extends AppCompatActivity {

  public static final Integer RecordAudioRequestCode = 1;
  
  RequestQueue requestQueue;
  String  insertUrl = "https://handiman.univ-paris8.fr/~meriam/login/insertStudent.php";
  String showUrl = "https://handiman.univ-paris8.fr/~meriam/login/showStudent.php";
  int stock3=0;
  String debutMot ;
  String stockmot;
  String motFin;
  int nombre1;
  int nombre2;
  ArrayList<String> propostion = new ArrayList<String>();
  private String afficher;
  private String stock;
  private int stock2;
  private InputStream is;
  private String expressionMatheux;
  ArrayList<String> listExpressionMatheux = new ArrayList<String>();
  private ArrayList<String> listeStock = new ArrayList<String>();
  private static final int REQUEST_CODE = 100;


  @BindView(R.id.activity_dicta)
  LinearLayout linearLayout;

  @BindView(R.id.math_view)
  MathView mathView;

  @BindView(R.id.input_view)
  EditText inputView;

  @BindView(R.id.button)
  Button supprimer;
  @BindView(R.id.imageButton)
  Button ajout;



  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dicta);
      afficher =  ReadSettings(this);


    android.util.Log.d("le compte:", dernierConnexion(afficher));
    ButterKnife.bind(this);
    requestQueue = Volley.newRequestQueue(getApplicationContext());


    ajout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            System.out.println(response.toString());
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
        }) {

          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> parameters  = new HashMap<String, String>();
            parameters.put ("Copie",mathView.getText());
            Log.d("dicta :", dernierConnexion(afficher));
            parameters.put ("idEleve",dernierConnexion(afficher));

            return parameters;
          }
        };
        requestQueue.add(request);
      }

    });

    supprimer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        stock = "";
        if (listeStock.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Suppression impossible", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getApplicationContext(), "Suppression", Toast.LENGTH_SHORT).show();
          listeStock.remove(listeStock.size() - 1);
          for (int i = 0; i < listeStock.size(); i++) {
            stock = stock + " " + listeStock.get(i);
          }

          mathView.setText(stock);
        }
      }
    });
    getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    setTitle("DictaMaths");
    stock="";

    inputView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {


      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        //String mot1 = editText.getText().toString();
       // Log.d("blabla", mot1);

        String mot = inputView.getText().toString()+".";
        char lettre = mot.charAt(mot.length()-1);
        String mot2=mot;
        String caractere2;
        String cpt10="";
        String cpt2="";
        for( int i=0; i<mot2.length(); i++){
          caractere2=mot2.substring(i,i+1);
          if (caractere2.equals(" ") || caractere2.equals(".")) {
            if(cpt2.equals("étoile") ||cpt2.equals("étoiles") ){
              cpt2="*";
            }

            if(cpt2.equals("plus")){
              cpt2="+";
            }
            if(cpt2.equals("égal") ||cpt2.equals("égales") ){
              cpt2="=";
            }
            if(cpt2.equals("moins")){
              cpt2="-";
            }
            cpt10=cpt10+" "+cpt2;
            cpt2="";

          }else{
            cpt2= cpt2+caractere2;
          }


        }
        mot=cpt10+".";


        Log.d(" expression ",mot2);

        char point='.';
        if(lettre==point ) {
          ArrayList<ArrayList<String>> resultat = new ArrayList<ArrayList<String>>();
          resultat = conversion(mot);
          mot=resultat.get(0).get(0);

          if( resultat.get(1).size()>0) {
            ArrayList<String> expressionMatheuse = new ArrayList<String>();

            expressionMatheuse = resultat.get(1);
            Log.d("les expressions :", String.valueOf(expressionMatheuse.size()));
            propostion = listeDeproposition(resultat.get(1).get(0));
            nombre1 = Integer.parseInt(resultat.get(1).get(1));
             nombre2 = Integer.parseInt(resultat.get(1).get(2));
            for (int i = 0; i < propostion.size(); i++) {
              Log.d("les propositions :", propostion.get(i));
            }
            Log.d("le debut :", String.valueOf(nombre1));
            Log.d("le debut :", String.valueOf(nombre2));
            String caractere = "";
            String motNew = "\n";
            String propAffiche = "";

            for (int i = 0; i < propostion.size(); i++) {
              for (int j = 1; j < propostion.get(i).length() - 1; j++) {
                caractere = propostion.get(i).substring(j, j + 1);
                motNew = motNew + caractere;
              }

              propostion.set(i, motNew);
              motNew = "";
            }

            final AlertDialog.Builder alert = new AlertDialog.Builder(DictaActivity.this);
            alert.setTitle("Oups ... il y a plusieurs propositions possibles ici :");
            String tab[] = new String[propostion.size()];
            for (int i = 0; i < propostion.size(); i++) {
              tab[i] = propostion.get(i);
            }
            alert.setItems(tab, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                stockmot="";
                Log.d("l'indice", "which : "+ which);
                String stock3 = listeStock.get(listeStock.size()-1);
                Log.d("blabla",stock3 );
                debutMot= stock3.substring(0, Integer.parseInt(String.valueOf(nombre1))+1);
                Log.d("blabla1", ""+Integer.parseInt(String.valueOf(nombre1)));
                Log.d("blabla1", ""+Integer.parseInt(String.valueOf(nombre2)));
                Log.d("blabla1", ""+ stock3.length());
                motFin= stock3.substring(Integer.parseInt(String.valueOf(nombre2+nombre1+5)), stock3.length());
                Log.d("blabla", String.valueOf(propostion.size()));

                stockmot="`"+propostion.get(which)+"`";
                listeStock.remove(listeStock.size()-1);
                String mot= debutMot+stockmot+motFin;
                listeStock.add(mot);
                stock="";
                for( int i=0; i<listeStock.size(); i++){
                  stock = stock+" "+listeStock.get(i);
                }
                mathView.setText(stock);


              }
            });

            //alert.setMessage(propAffiche);
            alert.setNeutralButton("ok", null);
            alert.show();
           /* builder.setSingleChoiceItems(tab, checkedItem, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Log.d("check", String.valueOf(checkedItem));

              }
            });

            Log.d("blabla", String.valueOf(stock3));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                String stock3 = listeStock.get(listeStock.size()-1);
                Log.d("blabla",stock3 );
                debutMot= stock3.substring(0, Integer.parseInt(String.valueOf(nombre1)));
                Log.d("blabla1", ""+Integer.parseInt(String.valueOf(nombre1)));
                Log.d("blabla1", ""+Integer.parseInt(String.valueOf(nombre2)));
                Log.d("blabla1", ""+ stock3.length());
                motFin= stock3.substring(Integer.parseInt(String.valueOf(nombre2+nombre1+4)), stock3.length());
                Log.d("blabla", String.valueOf(propostion.size()));

                stockmot="`"+propostion.get(1)+"`";
                listeStock.remove(listeStock.size()-1);
                String mot= debutMot+stockmot+motFin;
                listeStock.add(mot);
                stock="";
                for( int i=0; i<listeStock.size(); i++){
                  stock = stock+" "+listeStock.get(i);
                }
                mathView.setText(stock);


              }
            });
            builder.setNegativeButton("Cancel", null);*/
            Log.d("blabla", String.valueOf(stock3));
            AlertDialog dialog = alert.create();
            dialog.show();
          }
          listeStock.add(mot);
          stock="";
         // stock="\\\\begin{multiline}";
          for( int i=0; i<listeStock.size(); i++){
            stock = stock+" "+listeStock.get(i);
          }
          //stock = stock+ "\\\\end{multiline}";


          mathView.setText(stock);
        }else {

          listeStock.add(mot);
          String motMatheux ="";

          /*listExpressionMatheux=caractereMatheux(mot);
          for(int i=0; i<listExpressionMatheux.size();i++ ) {
            motMatheux  = motMatheux +listExpressionMatheux .get(i);
          }*/

          Log.d("les expressions :", motMatheux);
          stock="";
          for( int i=0; i<listeStock.size(); i++){
            stock = stock+" "+listeStock.get(i);
          }


          mathView.setText(stock);
        }
      }
    });

  }
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()){
      case R.id.nav_home:
        Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.copie:
            Intent i =  new Intent(this,Copies.class);
            startActivity(i);
        return true;
      case R.id.nav_Policy:
        Intent y =  new Intent(this,MainActivity.class);
        startActivity(y);
        return true;
      case R.id.nav_aboutus:
        AlertDialog.Builder notice = new AlertDialog.Builder(this);
        notice.setTitle("Notice");
        notice.setMessage("Une fois sur l’application Dictamaths, appuyer sur « commencer » pour commencer a dicter.\n" +
                "Le texte s’affichera alors dans l’interface qui récupéra le texte.\n" +
                "Un bouton supprimer supprimera toute la dernière dictée de texte.\n" +
                "\n" +
                "Mots clés:\n" +
                "Entrer ou Ok: Permet d’aller a la ligne \n" +
                "Appartiens: ∈\n" +
                "Si et seulement si: <=>\n" +
                "Droite AB : (AB)\n" +
                "F de x : f(x)\n" +
                "Divisé: /\n" +
                "Plus: +\n" +
                "Moins: -\n" +
                "Etoile: *\n" +
                "Perpendiculaire: ⊥\n");
        notice.setPositiveButton("ok", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
          }
        });
        notice.create().show();
        return true;
      default:
        return super.onOptionsItemSelected(item);

    }
  }
  public static ArrayList<ArrayList<String>> conversion(String phrase4){
    //mot.substring(cpt,cpt+1);
    ArrayList<ArrayList<String>> resultat = new ArrayList<ArrayList<String>>();
    ArrayList <String> caractereMaths = new ArrayList<String>();
    ArrayList <String> fonction = new ArrayList<String>();
    ArrayList <String> nombre = new ArrayList<String>();
    ArrayList <String> comparateur = new ArrayList<String>();
    nombre=nombre2 ();
    ArrayList <Signe> signe = new ArrayList<Signe>();
    Signe plus = new Signe("plus","+");
    Signe moins = new Signe("moins","-");
    Signe egal = new Signe("egal","=");
    Signe fois= new Signe("fois","x");
    Signe divise= new Signe("divisé","/");
    Signe sur= new Signe("sur","/");
    Signe ssi= new Signe("si et seulement si","<=>");
    Signe implique= new Signe("implique","=>");
    ArrayList <String> signe3 = new ArrayList<String>();
    signe3.add("+");
    signe3.add("-");
   // signe3.add("=");
    signe3.add("x");
    signe3.add("*");
    signe3.add("^");
    signe3.add("/");
    signe3.add("si");
    signe3.add("implique");
    signe3.add("plus");
    signe3.add("moins");
    //signe3.add("egal");
    signe3.add("fois");
    signe3.add("divisé");
    signe3.add("sur");
    signe.add(plus);
    signe.add(moins);
   // signe.add(egal);
    signe.add(fois);
    signe.add(divise);
    signe.add(sur);
    signe.add(ssi);
    signe.add(implique);
    String newcaractere ="";
    fonction.add("f");
    fonction.add("g");
    fonction.add("h");
    fonction.add("p");
    fonction.add("y");
    fonction.add("x");
    fonction.add("z");
    fonction.add("m");
    boolean vrai = true;
    int boucle =0;
    int cpt5=0;
    int debut=0;
    String cpt ="";
    int boucle3=1;
    boolean finMot=false;
    String caractere ="";
    int taille =0;
    ArrayList <String> a = new ArrayList<String>();

    while(vrai==true) {
     cpt = "";
      while (boucle != 1) {
        if (phrase4.length() > taille) {
          caractere = phrase4.substring(taille, taille + 1);
        }
        if (caractere.equals(".")) {

          vrai = false;
          Log.d("",vrai+"");
          if (finMot == true) {
            caractere = "`";
            finMot = false;
          }


          cpt = cpt + caractere;
          //if()
          break;
        }
        if (caractere.equals(" ")) {

          if (phrase4.length() > taille + 2) {

            caractere = phrase4.substring(taille + 1, taille + 2);

           if ((nombre.contains(caractere) || signe3.contains(caractere) || caractere.equals(")") || caractere.equals("("))) {

             cpt5++;
             if(cpt5==1){
               Log.d("5: ", cpt);
               debut= taille;
             }
             taille++;

            } else {

              boucle = 1;
              caractere = "";

              if (finMot == true) {

                caractere = "`";
              }
            }
          }


        }

        if((nombre.contains(caractere) || signe3.contains(caractere) ||caractere.equals(")") ||caractere.equals("(")) && boucle3==1) {
          a.add(cpt);
          cpt=" "+"`";
          boucle3=0;
          finMot =true;
        }
        cpt= cpt+caractere;
        taille++;
        caractere="";
      }

      finMot=false;
      boucle =0;
      boucle3=1;
      if(phrase4.length()>taille) {
        caractere=phrase4.substring(taille,taille+1);
      }


      if(cpt5>=3) {


        caractereMaths.add(cpt);
        caractereMaths.add(""+debut);
        caractereMaths.add(""+cpt5);

      }
      if(cpt.equals("ok")|| cpt.equals("OK")) {
        cpt = "*";
        a.remove(a.size() - 1);
        a.add(cpt);

      }
      cpt5=0;
      a.add(cpt);
      if(cpt.equals("appartient")) {
        Log.d("je suis la","" );
        cpt="`in`";
        a.remove(a.size()-1);
        a.add(cpt);

      }
      if(cpt.equals("implique")) {
        cpt="`implies`";
        a.remove(a.size()-1);
        a.add(cpt);

      }

      if(cpt.equals("perpendiculaire")) {
        cpt = "`bot`";
        a.remove(a.size() - 1);
        a.add(cpt);

      }

      if(cpt.equals("entrée") ) {
        cpt = "\\\\begin{split}  \\\\end{split} ";
        //cpt="\\\\vspace{1cm}";
        //cpt ="\\\\[\\\\]";
       // cpt = "\\\\begink ";
        a.remove(a.size() - 1);
        a.add(cpt);

      }
      if(cpt.equals("si")) {

        caractere="";
        int boucle2=0;
        int taille2 =0;
        cpt=cpt+" ";
        if(!phrase4.substring(taille,taille+1).equals(".")){

          while(boucle2 !=3){
            if(phrase4.length()>taille) {
              caractere=phrase4.substring(taille,taille+1);
            }
            if(caractere.equals(".")) {
              vrai =false;
              break;
            }
            if(caractere.equals(" ")) {
              boucle2++;
              caractere=" ";


            }
            cpt= cpt+caractere;
            taille2++;
            taille++;
            caractere="";
          }

        }

        boucle2=0;
          if(cpt.equals("si et seulement si ") ) {
            cpt="`<=>`";
            a.remove(a.size()-1);
            a.add(cpt);


          }else {
            taille = taille-taille2;
          }

      }

      if(fonction.contains(a.get(a.size()-1))) {

        cpt="";
        caractere="";
        if(!phrase4.substring(taille,taille+1).equals(".")){
          int boucle2=0;
          while(boucle2!=1){
            if(phrase4.length()>taille) {
              caractere=phrase4.substring(taille,taille+1);
            }
            if(caractere.equals(".")) {
              vrai =false;
              break;
            }
            if(caractere.equals(" ")) {
              boucle2 =1;
              caractere="";


            }
            cpt= cpt+caractere;

            taille++;
            caractere="";

          }
          boucle2 =0;


          if(cpt.equals("de") && fonction.contains(a.get(a.size()-1))) {
            cpt="";


            if(!phrase4.substring(taille,taille+1).equals(".")){
              while(boucle2!=1){
                if(phrase4.length()>taille) {
                  caractere=phrase4.substring(taille,taille+1);
                }
                if(caractere.equals(".")) {
                  vrai =false;
                  break;
                }
                if(caractere.equals(" ")) {
                  boucle2 =1;
                  caractere="";


                }

                if((nombre.contains(caractere) || signe3.contains(caractere) ||caractere.equals(")") ||caractere.equals("(")) && boucle3==1) {
                  cpt = cpt + caractere;
                }
                taille++;
                caractere="";

              }
            }
            boucle2 =0;
            if(cpt.equals("-")) {
             // cpt= conversionSigne(signe, cpt);
              String cpt3="";
              while(boucle2!=1){
                if(phrase4.length()>taille) {
                  caractere=phrase4.substring(taille,taille+1);
                }
                if(nombre.contains(cpt3)) {
                  Log.d("",cpt3);

                  int enlettre = nombre.indexOf(cpt3);
                  cpt3=enlettre +"";

                }
                if(caractere.equals(".")) {
                  vrai =false;
                  break;
                }
                if(caractere.equals(" ")) {
                  boucle2 =1;
                  caractere="";


                }
                if(caractere.equals("=")) {
                  caractere="";
                  taille--;

                }
                cpt3 = cpt3 + caractere;

                taille++;
                caractere = "";
              }
              cpt= cpt+cpt3;

            }
            newcaractere ="`"+a.get(a.size()-1)+"("+cpt+")`";
            a.remove(a.size()-1);
            a.add(newcaractere);
          }else {
            a.add(cpt);
          }
        }
      }


      if(cpt.equals("droite")) {
        cpt="";
        caractere="";
        if(!phrase4.substring(taille,taille+1).equals(".")){
          int boucle2=0;
          while(boucle2!=1){
            if(phrase4.length()>taille) {
              caractere=phrase4.substring(taille,taille+1);
            }
            if(caractere.equals(".")) {
              vrai =false;
              break;
            }
            if(caractere.equals(" ")) {
              boucle2 =1;
              caractere="";


            }
            cpt= cpt+caractere;

            taille++;
            caractere="";

          }
          boucle2 =0;

          if(cpt.length()<=2 && cpt.length()>=1) {

            newcaractere = "`("+cpt+")`";
            a.add(newcaractere);
          }
        }
      }
    }


    if(signe3.contains(cpt)){
      cpt= conversionSigne(signe, cpt);
      a.remove(a.size()-1);
      a.add(cpt);


    }
    String newPhrase ="";

    for(int i=0; i<a.size();i++) {
      newPhrase = newPhrase+" "+a.get(i);

    }
    ArrayList <String> motFinal2 = new ArrayList<String>();
    motFinal2.add( newPhrase );

     for( int i =0 ; i<caractereMaths.size(); i++){
       Log.d(" la valeur : ", caractereMaths.get(i));
     }


    /*Log.d(" la valeur : ", String.valueOf(caractereMaths.size()));
    Log.d(" la valeur : ", newPhrase);*/
    resultat.add(motFinal2);
    resultat.add(caractereMaths);

    return resultat;
  }

  public static String conversionSigne(ArrayList<Signe> signe2,String mot) {

    for(int i =0; i<signe2.size(); i++) {
      if(mot.equals(signe2.get(i).getSigne1())) {
        mot=signe2.get(i).getConversionSigne1();

      }
    }
    return mot;
  }

  public static ArrayList <String> nombre2 (){
    ArrayList <String> nombre = new ArrayList<String>();
    for(int i=0; i<1000; i++) {
      nombre.add(""+i);
    }
    return nombre;

  }

  public void onClick(View v)
  {
       Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {

        }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_CODE: {
        if (resultCode == RESULT_OK && null != data) {
          ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
          inputView.setText(result.get(0));
        }
        break;
      }

    }
  }

  /*public String supprimer ( String mot ){
    String newMot= "";
    Toast.makeText(getApplicationContext(),"Suppression 66",Toast.LENGTH_SHORT).show();
    if(mot.length()>0) {
      for (int i = 0; i < mot.length() - 3; i++) {
        char lettre = mot.charAt(i);
        newMot = newMot + lettre;
      }
      newMot= newMot+"'.";
      Toast.makeText(getApplicationContext(),"Suppression effectuée",Toast.LENGTH_SHORT).show();
    }else{
      Toast.makeText(getApplicationContext(),"impossible de supprimer",Toast.LENGTH_SHORT).show();
    }
  return newMot;
  }*/

  public static ArrayList<String> combinaison( ArrayList<ArrayList<String>> newlist, ArrayList<String> signe){
    ArrayList<String> listfinal = new ArrayList<String>();
    int cpt3=1;
    if(newlist.size()==1) {
      listfinal = new ArrayList<String>();
      for(int k=0; k<newlist.get(0).size(); k++) {
        listfinal.add(newlist.get(0).get(k));
      }
    }else {

      for(int g= 0; g <newlist.size()-1; g++) {
        listfinal = new ArrayList<String>();
        for(int k=0; k<newlist.get(0).size(); k++) {
          for(int n=0; n<newlist.get(1).size(); n++) {

            listfinal.add(newlist.get(0).get(k)+""+signe.get(0)+newlist.get(cpt3).get(n));

          }

        }
        //newlist.add();
        //newlist.remove(1);
        //cpt3++;
        newlist.remove(0);
        newlist.add(0,listfinal);
        //newlist.remove(0);
      }
    }


    return listfinal;
  }
  public static int compteSigne(String expression) {
    int taille=0;
    String caractere="";
    int cpt=0;
    for(int i=0; i<expression.length(); i++) {
      caractere=expression.substring(taille,taille+1);
      if(caractere.equals("+") ||caractere.equals("-")) {
        cpt++;
      }
      taille++;
    }
    return cpt;
  }
  public static String fonctionParenthese2(String expression, int compteur2) {
    ArrayList<String> listeSuggestion = new ArrayList<String>();
    ArrayList<String> expression2 = new ArrayList<String>();
    String expressionFinal="";
    int taille =0;
    boolean var =true ;
    String cpt ="";
    int valeur1=0;
    int compteur =0;
    String caractere="";
    String caractereDapres="";

    while(var) {
      if(taille==expression.length()) {
        var=false;
      }
      if(taille<=expression.length()) {
        if(taille+1<expression.length()) {
          caractere=expression.substring(taille,taille+1);



          if((caractere.equals("+") ||caractere.equals("-") ) && compteur==compteur2) {


            expression2.add("("+cpt+")");
            //System.out.println(cpt+"je suis la2");
            cpt="";
            expression2.add(caractere);
            //compteur=0;



          }else {

            if((caractere.equals("+") ||caractere.equals("-"))) {
              compteur ++;
              //System.out.println(compteur);
            }
            //System.out.println(cpt);
            cpt=cpt+caractere;
            //System.out.println(cpt+"je suis la");
            //System.out.println(compteur);
          }
          taille++;
        }
        if(taille+1==expression.length()) {
          caractere=expression.substring(taille,taille+1);
          expression2.add("("+cpt+caractere+")");
          taille++;
        }
      }
      else {
        var=false;

      }
    }
    for(int i=0; i<expression2.size();i++) {
      expressionFinal= expressionFinal+expression2.get(i);
    }


    return expressionFinal;
  }

  public ArrayList<String> listeDeproposition(String expression){
    boolean var =true;
    String caractere="";
    String expressionFinal="";
    String expressionAfter="";
    ArrayList<String> suggestion =  new ArrayList<String>();
    ArrayList<ArrayList<String>> listFinal= new ArrayList<ArrayList<String>>();
    ArrayList<String> signeExpression = new ArrayList<String>();
    ArrayList<String> formule2 = new ArrayList<String>();
    int nombreDeSigne = compteSigne(expression);
    int tailleExpression=0;

    int taille=0;
    while(tailleExpression!=nombreDeSigne) {

      while(var) {
        caractere=expression.substring(taille,taille+1);
        if(caractere.equals("/")||caractere.equals("*")) {
          ArrayList<String> formule = new ArrayList<String>();

          int cpt3=compteSigne(expressionAfter);
          String expressionBefore= expressionAfter;
          for(int k=0; k<cpt3+1; k++) {
            expressionAfter=fonctionParenthese2(expressionBefore,k);

            formule.add(expressionAfter);

          }

          signeExpression.add(caractere);
          expressionAfter="";
          //System.out.println(formule.size());
          listFinal.add(formule);
          //System.out.println(listFinal.get(listFinal.size()-1).size());

          //
          //System.out.println(listFinal.get(listFinal.size()-1).size());
          tailleExpression = tailleExpression+cpt3;
          taille++;

        }else {
          if(taille+1==expression.length()) {
            ArrayList<String> formule = new ArrayList<String>();
            caractere=expression.substring(taille,taille+1);
            expressionAfter = expressionAfter + caractere;
            String expressionBefore= expressionAfter;
            int cpt5 =compteSigne(expressionAfter);

            for(int k=0; k<cpt5+1; k++) {
              expressionAfter=fonctionParenthese2(expressionBefore,k);
              formule.add(expressionAfter);
            }
						/*for(int k=0; k<formule.size(); k++) {
							 System.out.println(formule.get(k));
						}*/
            tailleExpression = tailleExpression+cpt5;
            listFinal.add(formule);

            var=false;
            //formule.clear();

          }
          expressionAfter = expressionAfter + caractere;
          taille++;
        }
        //formule.clear();

      }
      //System.out.println(listFinal.get(0).size());
      //System.out.println(listFinal.get(1).size());
    }
		/*for(int i=0; i<listFinal.size();i++) {
			for ( int j=0; j<listFinal.get(i).size(); j++) {
				System.out.println(listFinal.get(i).get(j));

			}
		}*/
    suggestion= combinaison(listFinal, signeExpression);
    ArrayList <String> expression3 = new ArrayList<String>();
    String newmot="";
    String caractere2 ="";
    for(int i=0; i<suggestion.size();i++) {
      newmot="";
      for(int j=0; j <suggestion.get(i).length()-1; j++){
        caractere2=suggestion.get(i).substring(j,j+1);
        if(j==0){
          newmot= "`";
        }
        if(j==suggestion.get(i).length()-1){
          newmot= newmot+"`";
        }
        if(caractere2.equals("`")){
          j++;
          caractere2=suggestion.get(i).substring(j,j+1);
        }
        newmot=newmot+caractere2;
      }
      expression3.add(newmot+"`");
    }
    return expression3 ;

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
