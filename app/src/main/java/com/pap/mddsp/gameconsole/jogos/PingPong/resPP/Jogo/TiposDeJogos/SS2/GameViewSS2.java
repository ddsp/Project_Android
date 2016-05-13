package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameViewSS2 extends SurfaceView
{
    public GameThreadSS2 thread;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    protected Ping_Pong_SS2 Pai;

    public GameViewSS2(Context contextt, Ping_Pong_SS2 pai) {
        super(contextt);

        Pai = pai;

        thread = new GameThreadSS2(this, contextt);
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

    public GameThreadSS2 getGameThread() {
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
