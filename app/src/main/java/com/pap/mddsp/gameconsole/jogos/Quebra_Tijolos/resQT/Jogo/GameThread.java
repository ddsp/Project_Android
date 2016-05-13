package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo;

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
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.Block_Breacker_Menu;

public class GameThread extends Thread {

    /** Handle to the surface manager object we interact with */
    private Paint paint;
    private Context context;
    Boolean out = false;
    protected GameState state;
    private GameView Pai;
    public boolean running = false;
    public static final String nameK = "nameKey";
    public static final String MyPREFERENCES = "MyPrefs" ;

    public int[][][] l;

    public GameThread(GameView Paii,Context contextt)
    {
        paint = new Paint();
        Pai = Paii;
        state = new GameState(contextt,this);
        context = contextt;
    }

    @Override
    public void run() {
        while(!out) {
            while ((running) && (state.Vida > 0)) {
            //while (running) {
                state.mudarT(Pai.screenWidth, Pai.screenHeigth);
                Canvas c = null;

                state.update();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    c = Pai.getHolder().lockCanvas();
                    synchronized (Pai.getHolder()) {
                        try {
                            state.draw(c, paint);
                        }catch(NullPointerException ignored){
                        }
                    }
                } finally {
                    if (c != null) {
                        Pai.getHolder().unlockCanvasAndPost(c);
                    }
                }
            }
            if(state.Vida <= 0 ){
                out = true;
            }
        }

        try {
            Looper.prepare();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Game Over \n Chegaste até ao nivel " + state.nNivel);
            builder1.setCancelable(false);

            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                    Context.MODE_PRIVATE);

            if (sharedpreferences.getString(nameK, "").equals("")) {

                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent i = new Intent(context, Block_Breacker_Menu.class);
                                context.startActivity(i);
                            }
                        });

            } else {

                builder1.setPositiveButton("Não Gravar Pontuações",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent i = new Intent(context, Block_Breacker_Menu.class);
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
        }catch(Exception ignored){}
    }

    public void startThread() { running = true; }

    public void pause() { running = false; }

    public void resumeThread() {
        running = true;
    }

    public void stopThread() { running = false; }
}
