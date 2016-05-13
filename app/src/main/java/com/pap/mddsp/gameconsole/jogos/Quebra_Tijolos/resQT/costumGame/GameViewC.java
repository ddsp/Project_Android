package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.JSONParser;
import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Block_Breacker_New_Game;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.GameThread;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameViewC extends SurfaceView
{
    LinearLayout optMenu;
    TextView nVidasB;

    public GameStateC stateC;
    SurfaceHolder holder;
    public int screenWidth;
    public int screenHeigth;

    public Paint paint;

    protected Block_Breacker_Costum_Game_Constr Pai;

    public GameViewC(Context contextt, Block_Breacker_Costum_Game_Constr pai) {
        super(contextt);

        Pai = pai;
        paint = new Paint();

        optMenu = (LinearLayout)Pai.findViewById(R.id.propBB);
        nVidasB = (TextView)Pai.findViewById(R.id.caixaV);

        stateC = new GameStateC(contextt, this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                redrawn();
                Pai.AbrirFecharDR(null);
                Pai.preencherCor();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                screenWidth = width;
                screenHeigth = height-1;
                stateC.mudarT(screenWidth,screenHeigth);
                redrawn();
                Log.d("TesteO ", "Cheguei ao o tamanho");
            }
        });
        Log.d("TesteI ", "terminei a criação");
    }

    public void redrawn(){
        Canvas c = null;

        if(optMenu != null) {
            if (stateC.ySS == 0) {
                optMenu.setVisibility(GONE);
            } else {
                optMenu.setVisibility(VISIBLE);
                if(stateC.layout[stateC.ySS][stateC.xSS][3] != 10) {
                    nVidasB.setText(stateC.layout[stateC.ySS][stateC.xSS][1] + "");
                }else{
                    nVidasB.setText("6");
                }
            }
        }

        try {
            try {
                c = getHolder().lockCanvas();
                synchronized (getHolder()) {
                    stateC.draw(c, paint);
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
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return stateC.screenTouch(event);
    }
}
