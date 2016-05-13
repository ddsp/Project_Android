package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.JSONParser;
import com.pap.mddsp.gameconsole.MenuDeJogos;
import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Block_Breacker_New_Game;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame.Block_Breacker_Costum_Game_Constr;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Block_Breacker_Menu extends Activity {

    public static final String nameK = "nameKey";
    public static final String MyPREFERENCES = "MyPrefs";

    private String Pont_URL;
    private String Rank_URL;
    private JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;

    private RelativeLayout menuIni;
    private RelativeLayout menuPontR;
    private RelativeLayout menuP;
    private RelativeLayout menuR;
    TableLayout tl;

    JSONArray pont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block__breacker__menu);

        menuIni = (RelativeLayout) findViewById(R.id.Menu);
        menuPontR = (RelativeLayout) findViewById(R.id.PontRankMenu);
        menuP = (RelativeLayout) findViewById(R.id.Pont);
        menuR = (RelativeLayout) findViewById(R.id.Rank);

        tl = (TableLayout) findViewById(R.id.tbPont);

        String server = getResources().getString(R.string.server);
        Pont_URL = server + "/gamesConsole/obter_Pontos.php";
        Rank_URL = server + "/gamesConsole/obter_rank.php";
    }

    public void Options_Game(View v) {

        Intent i;
        AlertDialog dialog;

        switch(v.getId())
        {
            //Botoes de Menu Principal
            case R.id.NewG:
                i = new Intent(Block_Breacker_Menu.this, Block_Breacker_New_Game.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;

            case R.id.CosG:
                i = new Intent(Block_Breacker_Menu.this, Block_Breacker_Costum_Game_Constr.class);
                startActivity(i);
                break;

            case R.id.Exit:
                i = new Intent(Block_Breacker_Menu.this, MenuDeJogos.class);
                startActivity(i);
                finish();
                break;

            case R.id.close_RankPont:
                menuIni.setVisibility(View.VISIBLE);
                menuPontR.setVisibility(View.GONE);
                menuR.setVisibility(View.GONE);
                menuP.setVisibility(View.GONE);
                break;

            case R.id.PontV:
                SharedPreferences sharedpreferences=getSharedPreferences(MyPREFERENCES,
                        Context.MODE_PRIVATE);
                if(sharedpreferences.getString(nameK,null) != null) {
                    menuIni.setVisibility(View.GONE);
                    menuPontR.setVisibility(View.VISIBLE);
                    menuR.setVisibility(View.GONE);
                    menuP.setVisibility(View.VISIBLE);
                    new AttemptGetPont().execute();
                }else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Precisa de fazer login para aceder as pontuações");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    dialog = builder1.create();
                    dialog.show();
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_block__breacker__menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addRow(String d,int PontJ,int p) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TableRow tr2 = (TableRow)findViewById(p);
        if(tr2 != null){
            ViewGroup container = ((ViewGroup)tr2.getParent());
            // delete the row and invalidate your view so it gets redrawn
            container.removeView(tr2);
            container.invalidate();
        }

        TextView PontD = new TextView(this);
        PontD.setText(d);
        PontD.setPadding(2, 0, 5, 0);
        PontD.setTextColor(Color.WHITE);
        PontD.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tr.addView(PontD);

        TextView Pontj = new TextView(this);
        Pontj.setText(PontJ+"");
        Pontj.setPadding(2, 0, 5, 0);
        Pontj.setTextColor(Color.WHITE);
        Pontj.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tr.addView(Pontj);

        tr.setId(p);

        // finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }

    class AttemptGetPont extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Block_Breacker_Menu.this);
            pDialog.setMessage("A buscar Pontuações...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                System.out.println("request! starting");
                SharedPreferences sharedpreferences=getSharedPreferences(MyPREFERENCES,
                        Context.MODE_PRIVATE);
                Log.d("Teste", sharedpreferences.getString(nameK, null));
                params.add(new BasicNameValuePair("username", sharedpreferences.getString(nameK,null)));
                params.add(new BasicNameValuePair("jogo", "Brick Breacker"));
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        Pont_URL, "POST", params);
                // check your log for json response
                if(json == null){
                    Log.d("Teset2","Nao recebi nada");
                    pont = null;
                    return null;
                }
                Log.d("Login attempt : ", json.toString());
                // json success tag
                pont = json.getJSONArray("array");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());}
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(pont != null) {
                for (int i = 0; i < pont.length(); i++) {
                    JSONObject pontoa;
                    try {
                        pontoa = (JSONObject) pont.get(i);
                        addRow(pontoa.getString("data"), pontoa.getInt("pO"), i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Block_Breacker_Menu.this);
                builder1.setMessage("Ouve um erro a tentar aceder ao servidor, por favor " +
                        "verifique a sua conexão ao servidor");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                menuIni.setVisibility(View.VISIBLE);
                                menuPontR.setVisibility(View.GONE);
                                menuR.setVisibility(View.GONE);
                                menuP.setVisibility(View.GONE);
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder1.create();
                dialog.show();
            }
        }
    }
}
