package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameStateMBC {

    Context context;

    //Tamanho e largura max do ecra
    private float screenWidth = 0;
    private float screenHeight = 0;

    //A barra + cor
    public int batLength = 75;	public int batHeight = 40;
    private int BatX = 40;
    private int BatY = 20;
    public String color = "Red";
    public int speed = 20;

    public GameStateMBC(Context contextt){ context = contextt; }

    //O metodo de update
    public void update() {
        BatX = ((int)screenWidth/2) - (batLength / 2);
        BatY = ((int)screenHeight/2) - (batHeight / 2);
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

    public void mudarSpeed(int NSpeed) { speed = NSpeed; }

    public void mudarCor(String colorr){ color = colorr; }
}
