package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameStateMBP {

    Context context;

    //Tamanho e largura do ecra
    private float screenWidth = 0;
    float screenHeight = 0;

    //As barras e cor
    public int batLength = 75;	public int batHeight = 40;
    private int BatX = 40;
    private int BatY = 20;
    public String color = "Red";

    public GameStateMBP(Context contextt){ context = contextt; }

    //O metodo de update
    public void update() {
        BatX = ((int)screenWidth/2) - (batLength / 2);
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

        //Cor do Ecra
        canvas.drawRGB(20, 20, 20);

        //Cor da Barra
        paint.setColor(Color.parseColor(color));

        //Desnha a barra do topo
        canvas.drawRect(new Rect(BatX, BatY, BatX + batLength,
                BatY + batHeight), paint);
    }


    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        BatX = ((int)screenWidth/2) - (batLength / 2);
        BatY = ((int)screenHeight/2) - (batHeight / 2);
    }

    public void mudarLargura(int NLargura){
        batLength=NLargura;
    }

    public void mudarAltura(int NAltura){
        batHeight=NAltura;
    }

    public void mudarCor(String colorr){ color = colorr; }
}
