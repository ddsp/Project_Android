package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Jogo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;
import android.util.Log;

import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.desenhoJogo.Barra;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

import java.io.Serializable;
import java.util.ArrayList;

public class GameThreadC extends Thread {

    /** Handle to the surface manager object we interact with */
    private Paint paint;
    private Context context;
    private GameStateC state;
    private GameViewC Pai;
    private boolean out = false;
    private boolean running = false;

    public GameThreadC(GameViewC Paii, Context contextt, ArrayList<Serializable> array_objectos)
    {
        paint = new Paint();
        Pai = Paii;
        state = new GameStateC(contextt, (Barra)array_objectos.get(0),
                (Barra)array_objectos.get(1), (Bola)array_objectos.get(2));
        context = contextt;
    }

    @Override
    public void run() {
        state.mudarT(Pai.screenWidth, Pai.screenHeigth);
        while(!out) {
            while ((running) && (state.PontB < 5)) {
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
                } finally {
                    if (c != null) {
                        Pai.getHolder().unlockCanvasAndPost(c);
                    }
                }
            }
            if(state.PontB == 5) {
                Log.d("Teste","cheguei");
                out = true;
            }
        }

        running = false;

        if(state.PontB >= 5) {
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
            } catch (Exception e) {
                Log.d("Teste", "final foi erro");
            }
        }
    }

    public void startThread() { running = true; }

    public void pause() { running = false; }

    public void resumeThread() {
        running = true;
    }

    public void stopThread() { running = false; }

    public Boolean getRun(){
        return running;
    }

    public GameStateC getGameState()
    {
        return state;
    }
}
