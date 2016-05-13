package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;
import android.util.Log;

import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong;

public class GameThread extends Thread {

    /** Handle to the surface manager object we interact with */
    private Paint paint;
    private Context context;
    protected GameState state;
    private GameView Pai;
    private boolean out = false;
    public boolean running = false;
    public static final String nameK = "nameKey";
    public static final String MyPREFERENCES = "MyPrefs" ;

    public GameThread(GameView Paii,Context contextt)
    {
        paint = new Paint();
        Pai = Paii;
        state = new GameState(contextt);
        context = contextt;
    }

    @Override
    public void run() {
        while(!out) {
            state.mudarT(Pai.screenWidth, Pai.screenHeigth);
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
                Log.d("Teste", "cheguei");
                out = true;
            }
        }

        running = false;

        if (state.PontB >= 5) {
            try {
                Looper.prepare();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Game Over \n A sua pontuação foi de " + state.PontT);
                builder1.setCancelable(false);

                SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                        Context.MODE_PRIVATE);

                if (sharedpreferences.getString(nameK, "").equals("")) {

                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent i = new Intent(context, Ping_Pong.class);
                                    context.startActivity(i);
                                }
                            });

                } else {

                    builder1.setPositiveButton("Não Gravar Pontuações",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent i = new Intent(context, Ping_Pong.class);
                                    context.startActivity(i);
                                }
                            });
                    builder1.setNeutralButton("Gravar Pontuações",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Pai.savePont();
                                }
                            });
                }

                AlertDialog alert11 = builder1.create();
                alert11.show();

                Looper.loop();
            } catch (Exception ignored) {
            }
        }
    }

    public void startThread() { running = true; }

    public void pause() { running = false; }

    public void resumeThread() {
        running = true;
    }

    public void stopThread() { running = false; }
}
