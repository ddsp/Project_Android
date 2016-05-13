package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo.TiposDeJogos.RS;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

import java.util.Random;


public class GameStateRS {
    Context context;

    Boolean feito = false;

    // Para inputs
    private float previousX;

    //Marcadores
    public int PontT = 0;
    public int PontB = 0;

    //Tamanho e largura do ecra
    final int screenMinX = 0;
    final int screenMinY = 0;
    float screenWidth = 0;
    float screenHeight = 0;

    //A bola
    Bola ball;

    //As barras
    int batLengthTop = 75;	final int batHeight = 40;
    int topBatX = 40;
    final int topBatY = 20;

    int batSpeedTop = 7;
    int bottomBatX;
    int bottomBatY;
    int batLengthBot = 75;
    final int batSpeed = 20;

    public GameStateRS(Context contextt)
    {
        context = contextt;
        bottomBatX = 0;
        bottomBatY = 0;
    }

    //O metodo de update
    public void update() {

        if(feito) {
            //mover bola
            ball.mover();

            ball.verificarColi(bottomBatX, bottomBatY, batLengthBot, batHeight);
            ball.verificarColi(topBatX, topBatY, batLengthTop, batHeight);

            // Detecta colisoes e verifica marcação de ponto

            //ponto na barra de baixo
            if (ball.bot > screenHeight) {
                PontB += 1;
                ball.resetar("Top");
            } else {
                //ponto na barra de cima
                if (ball.topo < screenMinY) {
                    PontT += 1;
                    batSpeedTop += (batSpeedTop * 0.2);
                    ball.resetar("Baixo");
                }
            }

            //colisao com paredes
            if (ball.direita > screenWidth) {
                ball.direcaoX = -1;
            } else {
                if (ball.esque < screenMinX) {
                    ball.direcaoX = 1;
                }
            }


            //movimento Barra Topo
            if (ball.esque + ball.raio < topBatX) {
                topBatX += batSpeedTop;
            }
            if (ball.esque + ball.raio > topBatX + batLengthTop) {
                topBatX += -batSpeedTop;
            }

            if (topBatX + batLengthTop + batSpeedTop > screenWidth) {
                batSpeedTop = -batSpeedTop;
                topBatX = (int) screenWidth - batLengthTop;
            } else {
                if (topBatX < screenMinX) {
                    batSpeedTop = -batSpeedTop;
                    topBatX = screenMinX + 1;
                }
            }

            //Verificação de colisoes de bola com barras
            veriCBB();
        }
    }

    public boolean keyPressed(int keyCode)
    {
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) //esquerda
        {
            topBatX += batSpeed; bottomBatX -= batSpeed;
        }

        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) //direita
        {
            topBatX -= batSpeed; bottomBatX += batSpeed;
        }

        return true;
    }

    public boolean screenTouch(MotionEvent event) {
        float currentX = event.getX();
        float deltaX;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modifica o movimento da barra correspondente a input do touch no ecrã
                deltaX = currentX - previousX;
                if(!(bottomBatX + batLengthBot + deltaX >= screenWidth))
                {
                    if(!(bottomBatX + deltaX < 0))
                    {
                        bottomBatX += deltaX;
                    }
                }

                break;
        }
        // Guarda o X atual
        previousX = currentX;
        return true;  // Event handled
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

        //Cor do Ecra
        canvas.drawRGB(20, 20, 20);

        //Poe a cor da bola
        paint.setARGB(250, 0, 250, 0);

        //Desenha a bola
        canvas.drawOval(new RectF(ball.esque, ball.topo, ball.direita, ball.bot),
                paint);

        //Poe a cor da barra do topo
        paint.setARGB(250,250,0,0);

        //Desnha a barra do topo
        canvas.drawRect(new Rect(topBatX, topBatY, topBatX + batLengthTop,
                topBatY + batHeight), paint);

        //Poe a cor da barra do fundo
        paint.setARGB(200,50,0,200);

        //Desnha a barra do fundo
        canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLengthBot,
                bottomBatY - batHeight), paint);

        //Os numeros da Pontuação

        paint.setColor(Color.WHITE);
        paint.setTextSize(getDP(40f));

        canvas.drawText(""+PontB, screenWidth-70, (screenHeight/2) - 70, paint);
        canvas.drawText("-", screenWidth-70, (screenHeight/2) - 10, paint);
        canvas.drawText(""+PontT, screenWidth-70, (screenHeight/2) + 70, paint);
    }

    //funcoes de verificação

    //Verificação de colisoes de bola com barras
    public void veriCBB()
    {
        int randomBT = new Random().nextInt(100);
        int randomB = new Random().nextInt(100);
        int randomBB = new Random().nextInt(100);

        if(randomBT > 96){
            batLengthTop = new Random().nextInt(300 - 50);
        }

        float dezPerc = screenHeight / 20;

        if(randomB > 95 && !(ball.bot > screenHeight - dezPerc || ball.topo < dezPerc)){
            ball.raio = new Random().nextInt(150 - 50);
        }

        if(randomBB > 98){
            batLengthBot = new Random().nextInt(300 - 50);
        }
    }

    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        bottomBatX = ((int)screenWidth/2) - (batLengthBot / 2);

        bottomBatY = (int)screenHeight - 20;
        double t = screenHeight*0.02;

        int y = (int)screenHeight/2;
        int x = (int)screenWidth/2;

        ball = new Bola((int)t,4.5,6,0,"green",x,y,0.3,0.1);
        feito = true;
    }

    public int getDP(float dp){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
