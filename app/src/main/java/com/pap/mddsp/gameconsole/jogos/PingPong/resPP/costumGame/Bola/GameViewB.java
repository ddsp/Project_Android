package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameViewB extends SurfaceView
{
    private BBola Pai;
    public GameStateB state;
    private Paint paint;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;
    public Boolean posso = false;

    public GameViewB(Context contextt, BBola p) {
        super(contextt);
        Pai = p;
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { posso = false; }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                posso = true;
                Pai.updateLimit();
                redrawn();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                screenWidth = width-1;
                screenHeigth = height-1;
                Log.d("teste123",""+ screenHeigth+ "    " + screenWidth );
                state.mudarT(screenWidth,screenHeigth);
                redrawn();
            }
        });

        state = new GameStateB(contextt,this);
        paint = new Paint();

        Log.d("TesteI ", "terminei a criação");
    }

    public void redrawn(){
        Canvas c = null;
        try {
            try {
                c = this.getHolder().lockCanvas();
                synchronized (this.getHolder()) {
                    state.draw(c, paint);
                }
            } finally {
                if (c != null) {
                    this.getHolder().unlockCanvasAndPost(c);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
