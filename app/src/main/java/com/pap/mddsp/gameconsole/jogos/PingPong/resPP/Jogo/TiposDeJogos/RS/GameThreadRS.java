package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.RS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;
import android.util.Log;

import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong;

public class GameThreadRS extends Thread {

    /** Handle to the surface manager object we interact with */
    private Paint paint;
    private Context context;
    protected GameStateRS state;
    private GameViewRS Pai;
    private boolean out = false;
    public boolean running = false;

    public GameThreadRS(GameViewRS Paii, Context contextt)
    {
        paint = new Paint();
        Pai = Paii;
        state = new GameStateRS(contextt);
        context = contextt;
    }

    @Override
    public void run() {
        while(!out) {
        state.mudarT(Pai.screenWidth, Pai.screenHeigth);
        while((running) && (state.PontB < 5)) {
            Canvas c = null;

            state.update();
            try {
                c = Pai.getHolder().lockCanvas();
                synchronized (Pai.getHolder()) {
                    try {
                        state.draw(c, paint);
                    } catch (NullPointerException ignored) {
                    }
                }
            }finally {
                if(c != null) {
                    Pai.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
            if(state.PontB == 5) {
                Log.d("Teste", "cheguei");
                out = true;
            }
        }

        running = false;

        try {
            Looper.prepare();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Game Over \n A sua pontuação foi de " + state.PontT);
            builder1.setCancelable(false);

                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent i = new Intent(context, Ping_Pong.class);
                                context.startActivity(i);
                            }
                        });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            Looper.loop();
        }catch(Exception ignored){}
    }

    public void startThread() { running = true; }

    public void pause() { running = false; }

    public void resumeThread() {
        running = true;
    }

    public void stopThread() { running = false; }
}
