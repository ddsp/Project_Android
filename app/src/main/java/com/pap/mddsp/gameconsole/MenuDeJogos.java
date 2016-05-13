package com.pap.mddsp.gameconsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.Block_Breacker_Menu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MenuDeJogos extends Activity {

    private View login_layout = null;
    private View menu_jogos_layout = null;
    private View registo_layout = null;

    private EditText username = null;
    private EditText password = null;
    private EditText usernamer = null;
    private EditText passwordr = null;
    private TextView usernameL = null;
    protected TextView attempts = null;
    private EditText email = null;
    private ImageView mailI = null;
    private Button login = null;
    private Button entrarR = null;
    private Button sair = null;
    private Button registar = null;
    private Button sairr = null;
    private Button entrarL = null;
    private Button sairrl = null;

    private int sucesso = 2;
    private int nivel = 0;
    private String LOGIN_URL;
    private String SING_UP_URL;
    private JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NIVEL = "ad";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    int counter = 3;
    boolean impedido = false;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String nameK = "nameKey";
    public static final String adK = "nivelKey";
    SharedPreferences sharedpreferences;

    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_de_jogos);

        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        login_layout = findViewById(R.id.login);
        menu_jogos_layout = findViewById(R.id.layout_game);
        registo_layout = findViewById(R.id.registo);
        mailI = (ImageView) findViewById(R.id.imgM);
        username = (EditText) findViewById(R.id.TextUser);
        password = (EditText) findViewById(R.id.TextPass);
        login = (Button) findViewById(R.id.Login);
        entrarR = (Button) findViewById(R.id.RegistarA);
        entrarL = (Button) findViewById(R.id.button2);
        sair = (Button) findViewById(R.id.Sair);
        usernamer = (EditText) findViewById(R.id.TextUserr);
        passwordr = (EditText) findViewById(R.id.TextPassr);
        email = (EditText) findViewById(R.id.TextMailr);
        registar = (Button) findViewById(R.id.Registar);
        sairr = (Button) findViewById(R.id.Sairr);
        sairrl = (Button) findViewById(R.id.Sairrl);

        usernameL = (TextView) findViewById(R.id.username);

        attempts = (TextView) findViewById(R.id.attemps);
        attempts.setText(Integer.toString(counter));

        String server = getResources().getString(R.string.server);
        SING_UP_URL = server + "/gamesConsole/Fazer_registo.php";
        LOGIN_URL = server + "/gamesConsole/verificar_login.php";

        if (sharedpreferences.contains(nameK))
        {
            if(!sharedpreferences.getString(nameK,"").equals(""))
            {
                entrarL.setVisibility(View.GONE);
                usernameL.setVisibility(View.VISIBLE);
                usernameL.setText(sharedpreferences.getString(nameK, ""));
                Log.d("teste",sharedpreferences.getInt(adK,0) + "");
                /*if(sharedpreferences.getInt(adK,0) != 0){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mailI.getLayoutParams();// area de menu
                    params.topMargin = menu_jogos_layout.getHeight() - mailI.getHeight();
                    params.leftMargin = menu_jogos_layout.getWidth() - mailI.getWidth();
                    mailI.setLayoutParams(params);
                    mailI.setVisibility(View.VISIBLE);
                }*/
                sairrl.setVisibility(View.VISIBLE);
                Log.d("Teste",sharedpreferences.getString(nameK, ""));
            }
        }

        prepararActividadeLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_de_jogos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Menu de Jogos
    public void entrar_jogo(View v) {
        Intent i;
        switch(v.getId())
        {
            case R.id.imageView1:
                i = new Intent(MenuDeJogos.this, Ping_Pong.class);
                i.putExtra("username", usernameL.getText());
                startActivity(i);
                finish();
                break;

            case R.id.imageView2:
                i = new Intent(MenuDeJogos.this, Block_Breacker_Menu.class);
                startActivity(i);
                finish();
                break;
        }
    }

    // Login

    public void prepararActividadeLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if (activeNetworkInfo == null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                    builder1.setMessage("Por favor conecte-se a internet");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    try {
                        new AttemptLogin().execute();
                    } catch (Exception e) {
                        System.out.println("Exception : " + e.getMessage());
                    }
                }
            }});

        entrarL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!impedido) {
                    login_layout.setVisibility(View.VISIBLE);
                    menu_jogos_layout.setVisibility(View.GONE);
                }
            }});

        entrarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registo_layout.setVisibility(View.VISIBLE);
                login_layout.setVisibility(View.GONE);
            }});

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_jogos_layout.setVisibility(View.VISIBLE);
                login_layout.setVisibility(View.GONE);
            }});

        sairrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                sairrl.setVisibility(View.GONE);
                usernameL.setVisibility(View.GONE);
                entrarL.setVisibility(View.VISIBLE);
            }});

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String validacao = validarL();
                if(validacao == "") {

                    ConnectivityManager connectivityManager
                            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                    if (activeNetworkInfo == null) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                        builder1.setMessage("Por favor conecte-se a internet");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {

                        try {
                            new AttemptRegisto().execute();
                        } catch (Exception e) {
                            System.out.println("Exception : " + e.getMessage());
                        }
                        Log.d("Teste", "cheguei");
                    }
                    }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                    builder1.setMessage(validacao);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    }
            }});

        sairr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_jogos_layout.setVisibility(View.VISIBLE);
                registo_layout.setVisibility(View.GONE);
            }});


    }

    public String validarL(){
        String error = "";
        error += ValidarUsername(usernamer.getText().toString());
        error += ValidarPassword(passwordr.getText().toString());
        error += ValidarEmail(email.getText().toString());
        return error;
    }

    public String ValidarUsername(String usernameV){
        if(usernameV.length() < 5){
            return "Username tem de ter pelo menos 5 caracteres \n";
        }else {
            String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{5,}$";
            pattern = Pattern.compile(USERNAME_PATTERN);

            matcher = pattern.matcher(usernameV);
            if(!matcher.matches()){
                return "Username pode so conter a-z, A-Z, 0-9, - , @ e _, " +
                        "e deve conter pelo menos uma letra grande e uma " +
                        "pequena.\n";
            }else{
                return "";
            }
        }
    }



    public String ValidarPassword(String Password){
        if(Password.length() < 6){
            return "Password tem de ter pelo menos 6 caracteres \n";
        }else {
            String PASSWORD_PATTERN = "^\\S*(?=\\S*[a-zA-Z])(?=\\S*[0-9])\\S*$";//tb n deixa espaço
            pattern = Pattern.compile(PASSWORD_PATTERN);

            matcher = pattern.matcher(Password);
            if(!matcher.matches()){
                return "Password pode so conter a-z, A-Z e 0-9.\n";
            }else{
                return "";
            }
        }
    }

    public String ValidarEmail(String Email){

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            return "Introduza um email válido.\n";
        }else{
            return "";
        }
    }


    class AttemptRegisto extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuDeJogos.this);
            pDialog.setMessage("Attempting Registo...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            String usernamet = usernamer.getText().toString();
            String passwordt = passwordr.getText().toString();
            String emailt = email.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", usernamet));
                params.add(new BasicNameValuePair("password", passwordt));
                params.add(new BasicNameValuePair("email", emailt));
                System.out.println("request! starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        SING_UP_URL, "POST", params);
                // check your log for json response
                if(json == null){
                    sucesso = 0;
                    return null;
                }
                Log.d("Regist attempt : ", json.toString());
                // json success tag
                sucesso = json.getInt(TAG_SUCCESS);
                if (sucesso == 1) {
                    Log.d("Registo Sucedido!", json.toString());
                    nivel = Integer.getInteger(json.getString(TAG_NIVEL));
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registo Falhado", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());}
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Log.d("Teste", "Cheguei 2");
            pDialog.dismiss();
            if(sucesso == 0) {
                passwordr.setText("");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                builder1.setMessage("Ocurreu um erro ao registar");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                passwordr.setText("");
            }else{
                usernameL.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String user = usernamer.getText().toString();
                editor.putString(nameK,user);
                editor.putInt(adK, nivel);
                editor.apply();
                usernameL.setText(sharedpreferences.getString(nameK, ""));
                usernamer.setText("");
                passwordr.setText("");
                email.setText("");
                if(nivel == 1){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mailI.getLayoutParams();// area de menu
                    params.topMargin = menu_jogos_layout.getHeight() - mailI.getHeight();
                    params.leftMargin = menu_jogos_layout.getWidth() - mailI.getWidth();
                    mailI.setLayoutParams(params);
                    mailI.setVisibility(View.VISIBLE);
                }
                sairrl.setVisibility(View.VISIBLE);
                entrarL.setVisibility(View.GONE);
                menu_jogos_layout.setVisibility(View.VISIBLE);
                registo_layout.setVisibility(View.GONE);
            }
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuDeJogos.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            String usernamet = username.getText().toString();
            String passwordt = password.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", usernamet));
                params.add(new BasicNameValuePair("password", passwordt));
                System.out.println("request! starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);
                // check your log for json response
                if(json == null){
                    sucesso = 2;
                    return null;
                }
                Log.d("Login attempt : ", json.toString());
                // json success tag
                sucesso = json.getInt(TAG_SUCCESS);
                if (sucesso == 1) {
                    Log.d("Login Successful!", json.toString());
                    nivel = Integer.parseInt(json.getString(TAG_NIVEL));
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());}
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Log.d("Teste", "Cheguei 2");
            pDialog.dismiss();
            switch(sucesso){
                case 0:
                    counter--;
                    password.setText("");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                    builder1.setMessage("Username ou Password incorrecto");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    attempts.setText(Integer.toString(counter));
                    if(counter == 0){
                        impedido = true;
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        Log.d("Teste","hello2");
                                        impedido = false;
                                    }
                                },300000
                        );
                    }
                    break;

                case 1:
                    login_layout.setVisibility(View.GONE);
                    usernameL.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String user = username.getText().toString();
                    editor.putString(nameK,user);
                    editor.putInt(adK,nivel);
                    editor.commit();
                    Log.d("teste ", nivel+"");
                    if(nivel == 1){
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mailI.getLayoutParams();// area de menu
                        params.topMargin = menu_jogos_layout.getHeight() - mailI.getHeight();
                        params.leftMargin = menu_jogos_layout.getWidth() - mailI.getWidth();
                        mailI.setLayoutParams(params);
                        mailI.setVisibility(View.VISIBLE);
                    }
                    usernameL.setText(sharedpreferences.getString(nameK, null));
                    username.setText("");
                    password.setText("");
                    sairrl.setVisibility(View.VISIBLE);
                    entrarL.setVisibility(View.GONE);
                    menu_jogos_layout.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    builder1 = new AlertDialog.Builder(MenuDeJogos.this);
                    builder1.setMessage("Ocurreu um erro ao conectar ao servidor");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    alert11 = builder1.create();
                    alert11.show();
                    break;
                default:
                    break;
            }
        }
    }
}
