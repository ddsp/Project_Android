package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Jogo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.desenhoJogo.Barra;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

public class GameStateC {

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
    int batLengthTop;	int batHeightTop;
    int topBatX = 40;
    final int topBatY = 20;  String batTopColor;

    int batSpeedTop = 20;
    int batLengthBot; int batHeightBot;
    int bottomBatX;
    int bottomBatY;
    final int batSpeed = 20;  String batBotColor;

    public GameStateC(Context contextt, Barra barra_player, Barra barra_comp, Bola bola)
    {
        context = contextt;
        bottomBatX = 0;
        bottomBatY = 0;

        batLengthBot = barra_player.largura;
        batHeightBot = barra_player.altura;
        batBotColor = barra_player.cor;

        batHeightTop = barra_comp.altura;
        batLengthTop = barra_comp.largura;
        batTopColor = barra_comp.cor;
        batSpeedTop = barra_comp.speed;

        ball = bola;

    }

    //O metodo de update
    public void update() {

        if(feito) {
            //mover bola
            ball.mover();

            // Detecta colisoes e verifica marcação de ponto

            ball.verificarColi(bottomBatX, bottomBatY, batLengthBot, batHeightBot);
            ball.verificarColi(topBatX, topBatY, batLengthTop, batHeightTop);

            // Detecta colisoes e verifica marcação de ponto

            //ponto na barra de baixo
            if (ball.bot > bottomBatY - batHeightBot) {
                PontB += 1;
                ball.resetar("Top");
            } else {
                //ponto na barra de cima
                if (ball.topo < topBatY + batHeightTop) {
                    PontT += 1;
                    batSpeedTop += (batSpeedTop * 0.2);
                    ball.resetar("Bot");
                }
            }

            //colisao com paredes
            if (ball.direita > screenWidth) {
                ball.mudarDirecao(-1, "x");
            } else {
                if (ball.esque < screenMinX) {
                    ball.mudarDirecao(1, "x");
                }
            }

            //movimento Barra Topo
            if (ball.esque < topBatX) {
                if (topBatX - batSpeedTop > ball.esque) {
                    topBatX = ball.esque;
                } else {
                    topBatX += -batSpeedTop;
                }
            } else {
                if (ball.direita > topBatX + batLengthTop) {
                    if (topBatX + batLengthTop + batSpeedTop > ball.direita) {
                        topBatX = batLengthTop - ball.direita;
                    } else {
                        topBatX += batSpeedTop;
                    }
                }
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
        paint.setColor(Color.parseColor(ball.cor));

        //Desenha a bola
        canvas.drawOval(new RectF(ball.esque, ball.topo, ball.direita, ball.bot),
                paint);

        //Poe a cor da barra do topo
        paint.setColor(Color.parseColor(batTopColor));

        //Desnha a barra do topo
        canvas.drawRect(new Rect(topBatX, topBatY, topBatX + batLengthTop,
                topBatY + batHeightTop), paint);

        //Poe a cor da barra do fundo
        paint.setColor(Color.parseColor(batBotColor));

        //Desnha a barra do fundo
        canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLengthBot,
                bottomBatY - batHeightBot), paint);

        //Os numeros da Pontuação

        paint.setColor(Color.WHITE);
        paint.setTextSize(getDP(40f));

        canvas.drawText(""+PontB, screenWidth-70, (screenHeight/2) - 70, paint);
        canvas.drawText("-", screenWidth-70, (screenHeight/2) - 10, paint);
        canvas.drawText(""+PontT, screenWidth-70, (screenHeight/2) + 70, paint);
    }

    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        topBatX = ((int)screenWidth/2) - (batLengthTop / 2);
        bottomBatX = ((int)screenWidth/2) - (batLengthBot / 2);

        bottomBatY = (int)screenHeight - 20;
        resetarPosB("Top");
        feito = true;
    }

    public void resetarPosB(String Vence){
        int ballY = (int)screenHeight/2;
        int ballX = (int)screenWidth/2;
        ball.mudarCC(ballX,ballY);
        ball.mudarCCR(ballX,ballY);
    }

    public int getDP(float dp){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;

        return (int) (fpixels + 0.5f);
    }
}
