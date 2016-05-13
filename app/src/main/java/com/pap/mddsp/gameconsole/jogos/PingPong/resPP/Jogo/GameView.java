package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pap.mddsp.gameconsole.JSONParser;
import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView
{

    private int sucesso = 2;
    private static String SAVE_PONT_URL;
    private static final JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static ProgressDialog pDialog;
    private static Context context;

    public GameThread thread;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    public static final String nameK = "nameKey";
    public static final String MyPREFERENCES = "MyPrefs" ;

    protected Ping_Pong_S Pai;

    public GameView(Context contextt, Ping_Pong_S pai) {
        super(contextt);

        Pai = pai;

        context = contextt;
        String server = getResources().getString(R.string.server);
        SAVE_PONT_URL = server + "/gamesConsole/Registar_Pontuacoes.php";

        thread = new GameThread(this, contextt);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                thread.stopThread();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                Log.d("Teste Jogo", ""+getGameThread().running);
                try {
                    thread.start();
                    thread.startThread();
                }catch(IllegalThreadStateException ignored){}

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

    public GameThread getGameThread() {
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
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent msg) {
        return thread.state.keyPressed(keyCode);
    }

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
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // Check for success tag
            String pontoB = "" + thread.state.PontB;
            String pontoT = "" + thread.state.PontT;

            SharedPreferences sharedpreferences=context.getSharedPreferences(MyPREFERENCES,
                    Context.MODE_PRIVATE);

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", sharedpreferences.getString(nameK,null)));
                params.add(new BasicNameValuePair("PontoB", pontoB));
                params.add(new BasicNameValuePair("PontoT", pontoT));
                params.add(new BasicNameValuePair("Jogo", "Ping Pong"));
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            if(sucesso == 0) {
                builder1.setMessage("Ocurreu um erro ao registar a pontuação");
            }else{
                if(sucesso == 1) {
                    builder1.setMessage("Pontuação registada com sucesso");
                }else{
                    builder1.setMessage("Erro : Não foi possivel conectar com servidor");
                }
            }
            pDialog.dismiss();
            builder1.setCancelable(false);
            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent i = new Intent(context, Ping_Pong.class);
                            context.startActivity(i);
                        }
                    });

            AlertDialog alert1 = builder1.create();
            alert1.show();
        }
    }
}
