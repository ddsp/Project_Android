package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Block_Breacker_New_Game;

/**
 * Created by MDDSP on 20-05-2015.
 */
public class Block_Breacker_Costum_Game_Constr extends Activity{
    GameViewC gameView;
    RelativeLayout relativeLayout;
    ScrollView L;
    Boolean estado = true;
    ImageView seta;
    Button start;

    Boolean Feito = false;

    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.block_breacker_game_contr);

        gameView = new GameViewC(this, this);
        relativeLayout = (RelativeLayout)findViewById(R.id.previewBaseG);
        relativeLayout.addView(gameView);

        L = (ScrollView) findViewById(R.id.drwner);
        seta = (ImageView) findViewById(R.id.setaDR);
        start = (Button) findViewById(R.id.start);

        dropdown = (Spinner)findViewById(R.id.colorSeleBB);

        // Spinner click listener
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                if(item != " ") {
                    dropdown.setBackgroundColor(Color.parseColor(item));
                    gameView.stateC.mudarCor(item);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void eli(View v){
        gameView.stateC.eli();
    }

    public void AbrirFecharDR(View v){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) L.getLayoutParams();// gaveta de power ups
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) seta.getLayoutParams();// seta de Abrir/Fechar gaveta
        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) start.getLayoutParams();// seta de Abrir/Fechar gaveta

        params3.topMargin = relativeLayout.getHeight() - start.getHeight();
        if(estado){
            params.topMargin = -L.getHeight();
            L.setLayoutParams(params);
            params = (RelativeLayout.LayoutParams) L.getLayoutParams();
            params.topMargin -= L.getHeight();
            params2.topMargin = 0;
            L.setLayoutParams(params);
            estado = false;
            seta.setImageResource(R.drawable.arrow_down);
            params3.leftMargin = relativeLayout.getWidth() - start.getWidth();
        }else {
            params.topMargin = 0;
            L.setLayoutParams(params);
            if(relativeLayout.getHeight() <= L.getHeight()) {
                params2.topMargin = relativeLayout.getHeight() - params2.height;
                params3.leftMargin = relativeLayout.getWidth() - start.getWidth() - L.getWidth();
            }else{
                params2.topMargin = L.getHeight();
                //params3.leftMargin = relativeLayout.getWidth() - start.getWidth();
            }
            estado = true;
            seta.setImageResource(R.drawable.arrow_up);
        }
        seta.setLayoutParams(params2);
        start.setLayoutParams(params3);
    }

    public void preencherCor(){
        if(!Feito) {
            String[] Colorss = new String[]{"Green", "Red", "Blue", "Yellow", "White"};
            dropdown.setAdapter(new ArrayAdapter<String>(Block_Breacker_Costum_Game_Constr.this, R.layout.spiner_c_game_bb, Colorss));
            Feito = true;
        }
    }

    public int getCAct(){
        return Color.parseColor(dropdown.getSelectedItem().toString());
    }

    public void setColor(int c){
        dropdown.setSelection(c);
    }

    public void addP(View v){

        switch(v.getId()){
            case R.id.buttonVida:
                gameView.stateC.addPr(1);
                break;

            case R.id.buttonBalls:
                gameView.stateC.addPr(2);
                break;

            case R.id.buttonmoreSize:
                gameView.stateC.addPr(3);
                break;

            case R.id.buttonLessSize:
                gameView.stateC.addPr(4);
                break;

            case R.id.buttonLessSpeed:
                gameView.stateC.addPr(5);
                break;

            case R.id.buttonMoreSpeed:
                gameView.stateC.addPr(6);
                break;

            case R.id.buttonExlod:
                gameView.stateC.addPr(7);
                break;

            case R.id.buttonGrab:
                gameView.stateC.addPr(8);
                break;

            case R.id.buttonTroc:
                gameView.stateC.addPr(9);
                break;


            case R.id.retirarV:
                gameView.stateC.modV(-1);
                break;

            case R.id.addV:
                gameView.stateC.modV(1);
                break;

            default:
                break;
        }
    }

    public void iniciarJ(View v){
        int[][][] arraAEn = gameView.stateC.layout;

        arrayHelper h = new arrayHelper(arraAEn);

        Intent i = new Intent(Block_Breacker_Costum_Game_Constr.this, Block_Breacker_New_Game.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.putExtra("jogoC",h);
        startActivity(i);
    }
}
