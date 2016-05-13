package com.pap.mddsp.gameconsole.jogos.PingPong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.JSONParser;
import com.pap.mddsp.gameconsole.MenuDeJogos;
import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.DialogBox_gamemode;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.Ping_Pong_S;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.DVR.Ping_Pong_DVR;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.IB.Ping_Pong_IB;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.RS.Ping_Pong_RS;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS.Ping_Pong_SS;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS2.Ping_Pong_SS2;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS3.Ping_Pong_SS3;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pap.mddsp.gameconsole.R.drawable.cell_shape;


public class Ping_Pong extends Activity
        implements DialogBox_gamemode.NoticeDialogListener{

    RelativeLayout menu;
    GridLayout gameModes;
    RelativeLayout menuP;
    RelativeLayout menuPP;
    RelativeLayout menuPR;

    TableLayout tl;
    TableLayout tlr;
    JSONArray pont;
    JSONArray rank;

    String gameModeSele;

    public static final String nameK = "nameKey";
    public static final String MyPREFERENCES = "MyPrefs";

    private String Pont_URL;
    private String Rank_URL;
    private JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping__pong);

        menu = (RelativeLayout)findViewById(R.id.Menu);
        gameModes = (GridLayout)findViewById(R.id.GameMods);
        menuP = (RelativeLayout)findViewById(R.id.PontRankMenu);
        menuPR = (RelativeLayout)findViewById(R.id.Rank);
        menuPP = (RelativeLayout)findViewById(R.id.Pont);
        tl = (TableLayout) findViewById(R.id.tbPont);
        tlr = (TableLayout) findViewById(R.id.tbRank);

        String server = getResources().getString(R.string.server);
        Pont_URL = server + "/gamesConsole/obter_Pontos.php";
        Rank_URL = server + "/gamesConsole/obter_rank.php";
    }

    public void Options_Game(View v) {

        Intent i;
        AlertDialog.Builder builder;
        LayoutInflater inflater;
        AlertDialog dialog;
        ImageView im;
        TextView desc;

        switch(v.getId())
        {
            //Botoes de Menu Principal
            case R.id.NewG:
                i = new Intent(Ping_Pong.this, Ping_Pong_S.class);
                startActivity(i);
                break;

            case R.id.CosG:
                i = new Intent(Ping_Pong.this, Ping_Pong_Cos.class);
                startActivity(i);
                break;

            case R.id.GameModB:
                menu.setVisibility(View.GONE);
                gameModes.setVisibility(View.VISIBLE);
                break;

            case R.id.PontV:
                SharedPreferences sharedpreferences=getSharedPreferences(MyPREFERENCES,
                        Context.MODE_PRIVATE);
                if(sharedpreferences.getString(nameK,null) != null) {
                    menu.setVisibility(View.GONE);
                    menuP.setVisibility(View.VISIBLE);
                    menuPR.setVisibility(View.GONE);
                    menuPP.setVisibility(View.VISIBLE);
                    new AttemptGetPont().execute();
                }else{
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

            case R.id.Exit:
                i = new Intent(Ping_Pong.this, MenuDeJogos.class);
                startActivity(i);
                finish();
                break;

            //Botoes Pont

            case R.id.close_RankPont:
                menuP.setVisibility(View.GONE);
                menu.setVisibility(View.VISIBLE);
                break;

            case R.id.Next_Rank:
                menuPP.setVisibility(View.GONE);
                menuPR.setVisibility(View.VISIBLE);
                new AttemptGetRank().execute();
                break;

            case R.id.prev_Pont:
                menuPR.setVisibility(View.GONE);
                menuPP.setVisibility(View.VISIBLE);
                new AttemptGetPont().execute();
                break;

            //Botoes de GameMode

            case R.id.DVSR:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_DVR.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodesdvf);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a bola, ao tocar na barra oponente fica " +
                        "muito rapida e ao tocar na barra do jogador esta fica mais lenta.");
                break;

            case R.id.RS:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_RS.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodesrs);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a barra e as bolas vao mudando de tamanho ao longo do jogo.");
                break;

            case R.id.IB:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_IB.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodesib);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a bola fica invisivel em momentos ao calhas no jogo.");
                break;

            case R.id.SS:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_SS.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodestb);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a bola teleporta-se ao calhas para partes ao acaso no campo.");
                break;

            case R.id.SS2:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_SS2.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodesvr);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a bola aumenta a sua velocidade repentinamente durante momentos acaso " +
                        "do jogo.");
                break;

            case R.id.SS3:
                builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialogbox_gamemode, null))
                        // Add action buttons
                        .setPositiveButton("Jogar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(Ping_Pong.this, Ping_Pong_SS3.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder.show();
                dialog = builder.create();

                dialog.show();

                im = (ImageView) dialog.findViewById(R.id.imgDial);
                desc = (TextView) dialog.findViewById(R.id.descDial);

                im.setImageResource(R.drawable.gamemodests);
                desc.setText("Descrição : \n" +
                        "Neste modo de jogo a barra do jogador troca de lugar com o oponente " +
                        "enquanto mantem os pontos a momentos acaso do jogo.");
                break;

            case R.id.ExitGM:
                gameModes.setVisibility(View.GONE);
                menu.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void addRow(int PontJ,int PontO,int p) {
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

        TextView Ponto = new TextView(this);
        Ponto.setText(PontO+"");
        Ponto.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        Ponto.setTextColor(Color.WHITE);
        tr.addView(Ponto);
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

    public void addRow(int rank,String Username, int Pont, int p) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        TableRow tr2 = (TableRow)findViewById(p);
        if(tr2 != null){
            Log.d("Teste",""+p);
            ViewGroup container = ((ViewGroup)tr2.getParent());
            // delete the row and invalidate your view so it gets redrawn
            container.removeView(tr2);
            container.invalidate();
        }

        TextView rankT = new TextView(this);
        rankT.setText(rank+"");
        rankT.setPadding(2, 0, 5, 0);
        rankT.setTextColor(Color.WHITE);
        rankT.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tr.addView(rankT);
        TextView usernameT2 = new TextView(this);
        usernameT2.setText(Username);
        usernameT2.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        usernameT2.setTextColor(Color.WHITE);
        tr.addView(usernameT2);
        TextView PontT = new TextView(this);
        PontT.setText(Pont+"");
        PontT.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        PontT.setTextColor(Color.WHITE);
        tr.addView(PontT);

        tr.setId(p);
        Log.d("Teste",tr.getId()+"");

        // finally add this to the table row
        tlr.addView(tr, new TableLayout.LayoutParams(
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
            pDialog = new ProgressDialog(Ping_Pong.this);
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
                Log.d("Teste", sharedpreferences.getString(nameK,null));
                params.add(new BasicNameValuePair("username", sharedpreferences.getString(nameK,null)));
                params.add(new BasicNameValuePair("jogo", "Ping Pong"));
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
                    JSONObject pontoa = null;
                    try {
                        pontoa = (JSONObject) pont.get(i);
                        addRow(pontoa.getInt("pJ"), pontoa.getInt("pO"), i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Ping_Pong.this);
                builder1.setMessage("Ouve um erro a tentar aceder ao servidor, por favor " +
                        "verifique a sua conexão ao servidor");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                menu.setVisibility(View.VISIBLE);
                                menuP.setVisibility(View.GONE);
                                menuPR.setVisibility(View.GONE);
                                menuPP.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder1.create();
                dialog.show();
            }
        }
    }

    class AttemptGetRank extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Ping_Pong.this);
            pDialog.setMessage("A buscar rankings...");
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
                params.add(new BasicNameValuePair("jogo", "Ping Pong"));
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        Rank_URL, "POST", params);
                // check your log for json response
                if(json == null){
                    Log.d("Teset2","Nao recebi nada");
                    rank = null;
                    return null;
                }
                Log.d("Login attempt : ", json.toString());
                // json success tag
                rank = json.getJSONArray("array");
                if(rank == null) {
                    Log.d("teste", "true");
                }else{
                    Log.d("teste","false");
                }
            } catch (JSONException e) {
                rank = null;
                e.printStackTrace();
                System.out.println(e.toString());}
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(rank != null) {
                for (int i = 0; i < rank.length(); i++) {
                    JSONObject pontoa;
                    try {
                        pontoa = (JSONObject) rank.get(i);
                        addRow(i + 1, pontoa.getString("username"), pontoa.getInt("pontoTotal"), i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Ping_Pong.this);
                builder1.setMessage("Ouve um erro a tentar aceder ao servidor, por favor " +
                        "verifique a sua conexão ao servidor");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                menu.setVisibility(View.VISIBLE);
                                menuP.setVisibility(View.GONE);
                                menuPR.setVisibility(View.GONE);
                                menuPP.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder1.create();
                dialog.show();
            }
        }
    }
}
