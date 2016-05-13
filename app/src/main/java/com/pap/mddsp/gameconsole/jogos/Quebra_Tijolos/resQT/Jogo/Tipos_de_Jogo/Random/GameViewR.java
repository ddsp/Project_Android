package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Tipos_de_Jogo.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pap.mddsp.gameconsole.JSONParser;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Block_Breacker_New_Game;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameViewR extends SurfaceView
{

    private int sucesso = 2;
    private static final String SAVE_PONT_URL = "http://10.0.3.2:80/gamesConsole/Registar_Pontuacoes.php";
    private JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;

    public GameThreadR thread;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    protected Block_Breacker_New_Game Pai;

    public GameViewR(Context contextt, Block_Breacker_New_Game pai) {
        super(contextt);

        Pai = pai;

        thread = new GameThreadR(this, contextt);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                thread.stopThread();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                Log.d("Teste Jogo", ""+getGameThread().running);
                thread.start();
                thread.startThread();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                screenWidth = width-1;
                screenHeigth = height-1;
                Log.d("TesteO ", "Cheguei ao o tamanho");
            }
        });

        Log.d("TesteI ", "terminei a criação");
    }

    public GameThreadR getGameThread() {
        return thread;
    }

    public void pause() {
        thread.pause();
    }

    public void resume() {
        thread.resumeThread();
    }

    public void savePont() { new AttemptSave().execute(); }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return thread.state.screenTouch(event);
    }

    class AttemptSave extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // Check for success tag
            String pontoB = "" + thread.state.PontB;
            String pontoT = "" + thread.state.PontT;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("username", Pai.Username));
                params.add(new BasicNameValuePair("PontoB", pontoB));
                params.add(new BasicNameValuePair("PontoT", pontoT));
                System.out.println("request! starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        SAVE_PONT_URL, "POST", params);
                // check your log for json response
                if(json == null){
                    return null;
                }
                Log.d("Save attempt : ", json.toString());
                // json success tag
                sucesso = json.getInt(TAG_SUCCESS);
                if (sucesso == 1) {
                    Log.d("Login Successful!", json.toString());
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            if(sucesso == 0) {
                builder1.setMessage("Ocurreu um erro ao registar a pontuação");
            }else{
                builder1.setMessage("Pontuação registada com sucesso");
            }
            pDialog.dismiss();
            builder1.setCancelable(false);
            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert1 = builder1.create();
            alert1.show();
        }
    }
}
