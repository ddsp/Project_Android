package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameViewMBP extends SurfaceView
{
    SurfaceHolder holder;
    private Paint paint;
    public GameStateMBP state;
    public int screenWidth;
    public int screenHeigth;
    private Barra_Player pai;
    public Boolean posso = false;

    public GameViewMBP(Context contextt, Barra_Player p) {
        super(contextt);

        pai = p;
        paint = new Paint();
        state = new GameStateMBP(contextt);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { posso = false; }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                posso = true;
                pai.updateLimits();
                redrawn();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                screenWidth = width-1;
                screenHeigth = height-1;
                state.mudarT(screenWidth, screenHeigth);
                state.update();
                redrawn();
            }
        });
    }

    public void redrawn(){
        state.update();
        Canvas c = null;
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
