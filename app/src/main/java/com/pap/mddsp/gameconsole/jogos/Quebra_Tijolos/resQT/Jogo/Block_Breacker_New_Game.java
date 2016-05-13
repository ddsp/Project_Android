package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame.arrayHelper;

public class Block_Breacker_New_Game extends Activity {
    GameView gameView;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ping__pong__s);

        int[][][] layout;

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            arrayHelper objectArray = (arrayHelper) bundle.getSerializable("jogoC");
            layout = objectArray.l;
            gameView = new GameView(this, this, layout);
        }else {
            gameView = new GameView(this, this, null);
        }
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
