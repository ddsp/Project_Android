package com.pap.mddsp.gameconsole.jogos.PingPong;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Jogo.GameViewC;

import java.io.Serializable;
import java.util.ArrayList;

public class Ping_Pong_CS extends Activity {

    RelativeLayout relativeLayout;
    private GameViewC gameView;

    public String Username;

    public Ping_Pong_CS() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ping__pong__cj);

        ArrayList<Serializable> array_objectos = new ArrayList<Serializable>(3);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("username");
            array_objectos.add(extras.getSerializable("barra_player"));
            array_objectos.add(extras.getSerializable("barra_comp"));
            array_objectos.add(extras.getSerializable("bola"));
        }else{
            Username = "";
        }

        gameView = new GameViewC(this, this, array_objectos);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainviewcj);
        relativeLayout.addView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(gameView.thread != null) {
            if(!gameView.thread.getRun()) {
                gameView.resume();
            }
        }
    }
}
