package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo.Tipos_de_Jogo.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class GameStateR {

    Context context;

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
    float ballRadius;
    int ballX; 	int ballY;
    int ballVelocitySX = 6; 	double ballVelocitySY = 4.5;
    int ballVelocityX = 6; 	double ballVelocityY = 4.5;

    //As barras
    int britLength;	int briHeight;
    int[][][] layout = new int[10][9][2];
    Boolean feito = false;

    int batSpeedTop = 20;
    int batLength = 75;
    int batHeight = 40;
    int bottomBatX;
    int bottomBatY;

    public GameStateR(Context contextt)
    {
        context = contextt;
        bottomBatX = 0;
        bottomBatY = 0;
    }

    //O metodo de update
    public void update() {

        //mover bola
        ballX += ballVelocityX;
        ballY += ballVelocityY;

        // Detecta colisoes e verifica marcação de ponto

        //ponto na barra de baixo
        if (ballY + ballRadius > screenHeight) {
            PontB += 1;
            resetarPosB("Top");
        } else {
            //colisao na barra de cima
            if (ballY - ballRadius < screenMinY) {
                ballVelocityY = ballVelocitySY;
            }
        }

        //colisao com paredes
        if (ballX + ballRadius > screenWidth) {
            ballVelocityX = -ballVelocityX;
            ballX = (int)screenWidth-(int)ballRadius;
        } else {
            if (ballX - ballRadius < screenMinX) {
                ballVelocityX = -ballVelocityX;
                ballX = screenMinX+(int)ballRadius;
            }
        }

        //Verificação de colisoes de bola com barras
        veriCBB();
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

        //Cor do Ecra
        canvas.drawRGB(20, 20, 20);

        //Poe a cor da bola
        paint.setARGB(250, 0, 250, 0);

        //Desenha a bola
        canvas.drawOval(new RectF(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius),
                paint);

        paint.setARGB(200,50,0,200);

        if(feito){
            for(int y = 0; y <= 9; y++){
                for(int x = 0; x <= 8; x++) {
                    int l1 = new Random().nextInt(6);
                    if (layout[y][x][1] != 999) {
                       paint.setColor(layout[y][x][0]);
                        canvas.drawRect(new Rect(layout[y][x][1] + 2, y*briHeight + 2, layout[y][x][1] + britLength - 2,
                                y*briHeight + briHeight - 2), paint);
                    }
                }
            }
        }

        //Poe a cor da barra do fundo
        paint.setARGB(200,50,0,200);

        //Desnha a barra do fundo
        canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLength,
                bottomBatY - batHeight), paint);
    }

    //funcoes de verificação

    //Verificação de colisoes de bola com barras
    public void veriCBB()
    {

        //Verificação doembate com os blocos
        if(feito){
            for(int y = 0; y <= 9; y++){
                for (int x = 0; x <= 8; x++) {
                    if (layout[y][x][1] != 999) {
                        int actX = layout[y][x][1];
                        if((ballY - ballRadius < y*briHeight + briHeight) &&(ballY - ballRadius > y*briHeight)){
                            if((ballX > actX) && (ballX < actX + britLength)) {
                                ballVelocityY = ballVelocitySY;
                                layout[y][x][1] = 999;
                            }
                        }
                        if((ballY + ballRadius < y*briHeight + briHeight)&&(ballY + ballRadius > y*briHeight)){
                            if((ballX > actX) && (ballX < actX + britLength)) {
                                ballVelocityY = -ballVelocitySY;
                                layout[y][x][1] = 999;
                            }
                        }

                        if((ballX - ballRadius < actX + britLength)&&(ballX - ballRadius > actX)){
                            if((ballY < y*briHeight + briHeight) && (ballY > y*briHeight)) {
                                ballVelocityX = ballVelocitySX;
                                layout[y][x][1] = 999;
                            }
                        }

                        if((ballX + ballRadius < actX + britLength)&&(ballX + ballRadius > actX)){
                            if((ballY < y*briHeight + briHeight) && (ballY > y*briHeight)) {
                                ballVelocityX = -ballVelocitySX;
                                layout[y][x][1] = 999;
                            }
                        }
                    }
                }
            }
        }

        //Verificação de colisão de Barra de baixo com bola
        if(ballY + ballRadius+ballVelocityY > bottomBatY-batHeight)
        {
            if ((ballX > bottomBatX) && (ballX < bottomBatX + batLength)){
                ballVelocityY = -ballVelocityY;//Mundança de direção vertical
                ballY = bottomBatY - batHeight - (int) ballRadius - 1;//reposicionamento da bola por cima da barra
                if ((int) ballVelocityY < 40) { ballVelocityY += (ballVelocityY * 0.01); }//Aumento de Velocidade para maior desafio
            }else {
                if (ballX + ballRadius > bottomBatX && ballX + ballRadius < bottomBatX + batLength) {
                    ballVelocityY = -ballVelocityY;//Mundança de direção vertical
                    ballVelocityX = -ballVelocityX;//Mundança de direção horizontal
                    if((int)ballVelocityY<40){ballVelocityY += (ballVelocityY * 0.01);}//Aumento de Velocidade para maior desafio
                } else {
                    if (ballX - ballRadius < bottomBatX + batLength && ballX - ballRadius > bottomBatX) {
                        ballVelocityY = -ballVelocityY;//Mundança de direção vertical
                        ballVelocityX = -ballVelocityX;//Mundança de direção horizontal
                        if((int)ballVelocityY<40){ballVelocityY += (ballVelocityY * 0.01);}//Aumento de Velocidade para maior desafio
                    }
                }
            }
        }
    }

    public void mudarT(int width, int heigth)
    {
        screenHeight = heigth;
        screenWidth = width;
        bottomBatX = ((int)screenWidth/2) - (batLength / 2);

        bottomBatY = (int)screenHeight - 20;
        double t = screenHeight*0.02;
        ballRadius = (float)t;
        batSpeedTop = 3;
        ballX = bottomBatX + batLength/2;
        ballY = bottomBatY - batHeight+1;
        resetarPosB("Top");

        for(int y = 0; y <= 9; y++){
            for(int x = 0; x <= 8; x++) {
                int l1 = new Random().nextInt(6);
                if (l1 > 2) {
                    int randomC = new Random().nextInt(6);
                    switch(randomC){
                        case 0:
                            layout[y][x][0] = Color.WHITE;
                            break;

                        case 1:
                            layout[y][x][0] = Color.RED;
                            break;

                        case 2:
                            layout[y][x][0] = Color.BLUE;
                            break;

                        case 3:
                            layout[y][x][0] = Color.GRAY;
                            break;

                        case 4:
                            layout[y][x][0] = Color.GREEN;
                            break;

                        case 5:
                            layout[y][x][0] = Color.YELLOW;
                            break;

                        default:
                            Log.d("Bug", "Bug");
                            break;
                    }
                    layout[y][x][1] = x*britLength;
                }else{
                    layout[y][x][1] = 999;
                }
            }
        }
        feito = true;
    }

    public void resetarPosB(String Vence){
        ballX = bottomBatX + batLength/2;
        ballY = bottomBatY - batHeight+1;
        britLength = (int)screenWidth/10;
        briHeight = (int)(screenHeight/2)/9;
        if(Vence.equals("Top")){ ballVelocityY = ballVelocitySY; }
        else{ ballVelocityY = -ballVelocitySY; }
    }

    public int getDP(float dp){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
