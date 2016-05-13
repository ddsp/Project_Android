package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameViewMBC extends SurfaceView
{
    private Paint paint;
    public GameStateMBC state;
    SurfaceHolder holder;
    Context c;
    public int screenWidth;
    public int screenHeigth;
    private Barra_Com pai;
    public Boolean posso = false;

    public GameViewMBC(Context contextt, Barra_Com p) {
        super(contextt);

        c = contextt;
        paint = new Paint();
        pai = p;
        state = new GameStateMBC(c);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { posso = false; }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("VolteiC","Voltei2");
                pai.updateLimits();
                redrawn();
                posso = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                screenWidth = width-1;
                screenHeigth = height-1;
                Log.d("TesteO ", "Cheguei ao o tamanho");
                state.mudarT(screenWidth,screenHeigth);
                state.update();
                redrawn();
            }
        });

        Log.d("TesteI ", "terminei a criação");
    }

    public void redrawn(){
        Canvas c = null;
        state.update();
        try {
            try {
                c = getHolder().lockCanvas();
                synchronized (getHolder()) {
                    state.draw(c, paint);
                }
            } finally {
                if (c != null) {
                    getHolder().unlockCanvasAndPost(c);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
