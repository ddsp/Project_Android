package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.Jogo;

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

public class GameState {

    Context context;

    //verificação de construção concluida
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
    private Bola ball;

    //As barras
    final int batLength = 75;	final int batHeight = 40;
    int topBatX = 40;
    final int topBatY = 20;

    int batSpeedTop = 20;
    int bottomBatX = 0;
    int bottomBatY = 0;
    final int batSpeed = 20;

    public GameState(Context contextt)
    {
        context = contextt;
    }

    //A função de update
    public void update() {

        if(feito) {
            //mover bola
            ball.mover();

            ball.verificarColi(bottomBatX, bottomBatY, batLength, batHeight);
            ball.verificarColi(topBatX, topBatY, batLength, batHeight);

            // Detecta colisoes e verifica marcação de ponto

            //ponto na barra de baixo
            if (ball.bot > bottomBatY - batHeight) {
                PontB += 1;
                ball.resetar("Top");
            } else {
                //ponto na barra de cima
                if (ball.topo < topBatY + batHeight) {
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
                if (ball.direita > topBatX + batLength) {
                    if (topBatX + batLength + batSpeedTop > ball.direita) {
                        topBatX = batLength - ball.direita;
                    } else {
                        topBatX += batSpeedTop;
                    }
                }
            }

            if (topBatX + batLength + batSpeedTop > screenWidth) {
                batSpeedTop = -batSpeedTop;
                topBatX = (int) screenWidth - batLength;
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
                if(!(bottomBatX + batLength + deltaX >= screenWidth))
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

        if(feito) {
            //Cor do Ecra
            canvas.drawRGB(20, 20, 20);

            //Poe a cor da bola
            paint.setARGB(250, 0, 250, 0);

            //Desenha a bola
            canvas.drawOval(new RectF(ball.esque, ball.topo, ball.direita, ball.bot),
                    paint);

            //Poe a cor da barra do topo
            paint.setARGB(250, 250, 0, 0);

            //Desnha a barra do topo
            canvas.drawRect(new Rect(topBatX, topBatY, topBatX + batLength,
                    topBatY + batHeight), paint);

            //Poe a cor da barra do fundo
            paint.setARGB(200, 50, 0, 200);

            //Desnha a barra do fundo
            canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLength,
                    bottomBatY - batHeight), paint);

            //Os numeros da Pontuação

            paint.setColor(Color.WHITE);
            paint.setTextSize(getDP(40f));

            canvas.drawText("" + PontB, screenWidth - 70, (screenHeight / 2) - 70, paint);
            canvas.drawText("-", screenWidth - 70, (screenHeight / 2) - 10, paint);
            canvas.drawText("" + PontT, screenWidth - 70, (screenHeight / 2) + 70, paint);
        }
    }


    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        bottomBatX = ((int)screenWidth/2) - (batLength / 2);

        bottomBatY = (int)screenHeight - 20;
        double t = screenHeight*0.02;
        batSpeedTop = 3;

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
