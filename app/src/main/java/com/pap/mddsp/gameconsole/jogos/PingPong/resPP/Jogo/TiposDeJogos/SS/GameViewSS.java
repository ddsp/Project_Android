package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pap.mddsp.gameconsole.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameViewSS extends SurfaceView
{
    public GameThreadSS thread;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    protected Ping_Pong_SS Pai;

    public GameViewSS(Context contextt, Ping_Pong_SS pai) {
        super(contextt);

        Pai = pai;

        thread = new GameThreadSS(this, contextt);
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

    public GameThreadSS getGameThread() {
        return thread;
    }

    public void pause() {
        thread.pause();
    }

    public void resume() {
        thread.resumeThread();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent msg) {
        return thread.state.keyPressed(keyCode);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return thread.state.screenTouch(event);
    }
}
