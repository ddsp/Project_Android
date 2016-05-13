package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Jogo;

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
import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong_CS;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameViewC extends SurfaceView
{

    public GameThreadC thread;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    public Ping_Pong_CS Pai;

    public GameViewC(Context contextt, Ping_Pong_CS pai, ArrayList<Serializable> array_objectos) {
        super(contextt);

        Pai = pai;

        thread = new GameThreadC(this, contextt, array_objectos);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                thread.stopThread();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
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

    public void pause() {
        thread.pause();
    }

    public void resume() {
        thread.resumeThread();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent msg) {
        return thread.getGameState().keyPressed(keyCode);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return thread.getGameState().screenTouch(event);
    }
}
