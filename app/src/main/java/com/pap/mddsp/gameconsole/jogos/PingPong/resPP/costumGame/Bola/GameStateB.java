package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GameStateB {

    Context context;
    GameViewB Pai;

    //Tamanho e largura do ecra
    float screenWidth = 0;
    float screenHeight = 0;

    //A bola e cor
    public int raio = 75;
    public int BolaX = 40;
    public int BolaY = 20;
    public double speedX = 6;
    public double speedY = 4.5;
    public int speedLimit = 40;
    public String color = "Red";

    public GameStateB(Context contextt, GameViewB p){
        context = contextt;
        Pai = p;
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

        //Cor do Ecra
        canvas.drawRGB(20, 20, 20);

        //Cor da Barra
        paint.setColor(Color.parseColor(color));

        //Desenha a bola
        canvas.drawOval(new RectF(BolaX - raio, BolaY - raio, BolaX + raio, BolaY + raio),
                paint);
    }


    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        BolaX = ((int)screenWidth/2);
        BolaY = ((int)screenHeight/2);
    }

    public void mudarRaio(int NRaio){
        raio=NRaio;
    }

    public void mudarSpeedX(int NSpeedX){
        speedX=NSpeedX;
    }

    public void mudarSpeedY(int NSpeedY){
        speedY=NSpeedY;
    }

    public void mudarCor(String colorr){ color = colorr; }
}
