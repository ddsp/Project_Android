package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Tipos_de_Jogo.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameThreadR extends Thread {

    /** Handle to the surface manager object we interact with */
    private Paint paint;
    private Context context;
    protected GameStateR state;
    private GameViewR Pai;
    public boolean running = false;

    public GameThreadR(GameViewR Paii, Context contextt)
    {
        paint = new Paint();
        Pai = Paii;
        state = new GameStateR(contextt);
        context = contextt;
    }

    @Override
    public void run() {
        state.mudarT(Pai.screenWidth, Pai.screenHeigth);
        while((running) && (state.PontB < 5)) {
            Canvas c = null;

            state.update();
            try {
                c = Pai.getHolder().lockCanvas();
                synchronized (Pai.getHolder()) {
                    state.draw(c, paint);
                }
            }finally {
                if(c != null) {
                    Pai.getHolder().unlockCanvasAndPost(c);
                }
            }
        }

        running = false;

        /*try {
            Looper.prepare();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Game Over \n A sua pontuação foi de " + state.PontT);
            builder1.setCancelable(false);

            if (Pai.Pai.Username.equals("")) {

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

                                Intent i = new Intent(context, Ping_Pong.class);
                                context.startActivity(i);
                            }
                        });
            }

            AlertDialog alert11 = builder1.create();
            alert11.show();

            Looper.loop();
        }catch(Exception ignored){}*/
    }

    public void startThread() { running = true; }

    public void pause() { running = false; }

    public void resumeThread() {
        running = true;
    }

    public void stopThread() { running = false; }
}
