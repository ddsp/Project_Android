package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.SS;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.pap.mddsp.gameconsole.R;

public class Ping_Pong_SS extends Activity {

    RelativeLayout relativeLayout;
    private GameViewSS gameView;

    public String Username;

    public Ping_Pong_SS() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ping__pong__s);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("username");
        }else{
            Username = "";
        }

        gameView = new GameViewSS(this, this);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainview);
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
            if(!gameView.thread.running) {
                gameView.resume();
            }
        }
    }
}
