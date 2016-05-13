package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.GameView;

public class Ping_Pong_S extends Activity {

    RelativeLayout relativeLayout;
    private GameView gameView;

    public String Username;

    public Ping_Pong_S() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ping__pong__s);
        gameView = new GameView(this, this);
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
