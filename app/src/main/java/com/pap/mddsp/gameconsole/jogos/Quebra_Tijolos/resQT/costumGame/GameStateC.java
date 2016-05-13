package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola.GameViewB;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

import java.util.Random;

public class GameStateC {

    private Context context;
    private GameViewC Pai;

    //Nivel
    int nNivel = 0;
    int NBlocos = 0;

    //Vida
    int Vida = 3;

    //moreLife
    private Bitmap imgVida;

    //moreBalls
    private Bitmap imgMBalls;
    int nBallAct = 0;
    //objecto
    Bola[] bolasAct;

    //+sizeBar
    private Bitmap imgMoreSize;

    //-sizeBar
    private Bitmap imgMinusSize;

    //+speedBall
    private Bitmap imgMoreSpeed;

    //-speedBall
    private Bitmap imgMinusSpeed;

    //Exploderino
    private Bitmap imgExplode;

    //grab
    private Bitmap imgGrab;

    //oposiont
    private Bitmap imgTroc;

    //fimspecials

    //Tamanho e largura do ecra
    float screenWidth = 0;
    float screenHeight = 0;

    //As barras
    int britLength;	int briHeight;
    int[][][] layout = new int[11][10][4];
    Boolean feito = false;

    private int batSpeedTop = 20;
    int batLength = 75;
    private final int batHeight = 40;
    int bottomBatX;
    int bottomBatY;

    String PSelect = "";

    //quadradoSele
    int xSS = 0;
    public int ySS = 0;

    public GameStateC(Context contextt, GameViewC p)
    {
        context = contextt;

        Pai = p;

        bottomBatX = 0;
        bottomBatY = 0;

        imgVida = BitmapFactory.decodeResource(context.getResources(), R.drawable.dontbegivinmeaheart);
        imgMBalls = BitmapFactory.decodeResource(context.getResources(), R.drawable.ballllllssssss);
        imgMoreSize = BitmapFactory.decodeResource(context.getResources(), R.drawable.increasesize);
        imgMinusSize = BitmapFactory.decodeResource(context.getResources(), R.drawable.reducedsize);
        imgMinusSpeed = BitmapFactory.decodeResource(context.getResources(), R.drawable.tofast2furios);
        imgMoreSpeed = BitmapFactory.decodeResource(context.getResources(), R.drawable.slowdown);
        imgExplode = BitmapFactory.decodeResource(context.getResources(), R.drawable.exploderino);
        imgGrab = BitmapFactory.decodeResource(context.getResources(), R.drawable.grabsomeballs);
        imgTroc = BitmapFactory.decodeResource(context.getResources(), R.drawable.oposition);
    }

    public boolean screenTouch(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                for (int y = 0; y <= 10; y++) {
                    for (int x = 0; x <= 9; x++) {

                        if((currentX > x*britLength + 2) && (currentX < x*britLength + britLength - 2) &&
                                (currentY > y * briHeight + 2) && (currentY < y * briHeight + briHeight - 2)){
                            if(y != 0) {
                                if (layout[y][x][1] == 0){
                                    layout[y][x][1] = 1;
                                    layout[y][x][0] = Pai.Pai.getCAct();
                                }else{
                                    if(Color.parseColor("Green") == layout[y][x][0]) {
                                        Pai.Pai.setColor(0);
                                    }

                                    if(Color.parseColor("Red") == layout[y][x][0]) {
                                        Pai.Pai.setColor(1);
                                    }

                                    if(Color.parseColor("Blue") == layout[y][x][0]) {
                                        Pai.Pai.setColor(2);
                                    }

                                    if(Color.parseColor("Yellow") == layout[y][x][0]) {
                                        Pai.Pai.setColor(3);
                                    }

                                    if(Color.parseColor("White") == layout[y][x][0]) {
                                        Pai.Pai.setColor(4);
                                    }                                   

                                }
                                xSS = x;
                                ySS = y;
                            }
                        }
                    }
                }
           break;
        }
        Pai.redrawn();
        return true;  // Event handled
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

        if(feito) {
            //Cor do Ecra
            canvas.drawRGB(20, 20, 20);

            //Poe a cor da bola
            paint.setARGB(250, 0, 250, 0);

            for (int n = 0; n <= nBallAct; n++) {
                Bola ball = bolasAct[n];
                if (ball != null) {
                    //Desenha a bola
                    canvas.drawOval(new RectF(ball.esque, ball.topo, ball.direita, ball.bot),
                            paint);
                }
            }

            canvas.drawOval(new RectF(1, screenHeight - (bolasAct[0].raio * 4) - 2, bolasAct[0].raio * 4 + 1, screenHeight - 2),
                    paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(getDP(20f));
            canvas.drawText("x"+Vida,10 , screenHeight-20, paint);

            //paint.setARGB(200, 50, 0, 255);

            for (int y = 0; y <= 10; y++) {
                for (int x = 0; x <= 9; x++) {

                    paint.setColor(Color.BLUE);
                    if((xSS == x) &&(ySS == y) && (ySS != 0)){
                        canvas.drawRect(new Rect(x*britLength, y * briHeight + 12, x*britLength + britLength,
                                y * briHeight + briHeight + 12), paint);
                    }

                    if(layout[y][x][1] > 1) {
                        paint.setColor(Color.GRAY);
                    }else{
                        if(layout[y][x][3] != 10) {
                            paint.setColor(layout[y][x][0]);
                        }else{
                            paint.setColor(Color.parseColor("#404040"));
                        }
                    }

                    canvas.drawRect(new Rect(x*britLength + 2, y * briHeight + 2 + 12, x*britLength + britLength - 2,
                            y * briHeight + briHeight - 2 + 12), paint);

                    if(layout[y][x][1] > 2){
                        paint.setTextSize(getDP(20f));
                        paint.setColor(Color.WHITE);
                        canvas.drawText("x"+layout[y][x][1], x*britLength + (britLength/2)-("x" + layout[y][x][1]).length(), y*briHeight + briHeight +10, paint);
                    }

                    desenharImg(canvas,layout[y][x][3],x*britLength,y*briHeight);
                }
            }
        }
        //Poe a cor da barra do fundo
        paint.setARGB(255,50,0,200);

        //Desnha a barra do fundo
        canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLength,
                bottomBatY - batHeight), paint);
    }

    private void desenharImg(Canvas c,int p,int x, int y){
        y += briHeight/2;
        x += britLength/2;

        switch (p) {
            case 1:
                y -= 5;
                x -= imgVida.getWidth()/2;
                c.drawBitmap(imgVida, x, y, null);
                break;

            case 2:
                y -= 5;
                x -= imgMBalls.getWidth()/2;
                c.drawBitmap(imgMBalls, x, y, null);
                break;

            case 3:
                y -= 5;
                x -= imgMoreSize.getWidth()/2;
                c.drawBitmap(imgMoreSize, x, y, null);
                break;

            case 4:
                y -= 5;
                x -= imgMinusSize.getWidth()/2;
                c.drawBitmap(imgMinusSize, x, y, null);
                break;

            case 5:
                y -= 5;
                x -= imgMoreSpeed.getWidth()/2;
                c.drawBitmap(imgMoreSpeed, x, y, null);
                break;

            case 6:
                y -= 5;
                x -= imgMinusSpeed.getWidth()/2;
                c.drawBitmap(imgMinusSpeed, x, y, null);
                break;

            case 7:
                y -= 5;
                x -= imgExplode.getWidth()/2;
                c.drawBitmap(imgExplode, x, y, null);
                break;

            case 8:
                y -= 5;
                x -= imgGrab.getWidth()/2;
                c.drawBitmap(imgGrab, x, y, null);
                break;

            case 9:
                y -= 5;
                x -= imgTroc.getWidth()/2;
                c.drawBitmap(imgTroc, x, y, null);
                break;

            default:
                break;
        }
    }

    public void mudarT(int width, int heigth)
    {
        if(!feito) {
            screenHeight = heigth;
            screenWidth = width;

            bottomBatX = ((int) screenWidth / 2) - (batLength / 2);
            bottomBatY = (int) screenHeight - 20;

            britLength = (int) screenWidth / 10;
            briHeight = (int) (screenHeight / 2) / 9;

            double t = screenHeight * 0.02;
            batSpeedTop = 3;

        /*ballX = bottomBatX + batLength/2;
        ballY = bottomBatY - batHeight+1;*/

            int yB = (int) screenHeight / 2;
            int xB = (int) screenWidth / 2;

            //A bola
            Bola ball;

            ball = new Bola((int) t, 4.5, 6, 0, "green", xB, yB, 0.3, 0.1);
            ball.mudarCC(bottomBatX + (batLength) / 2, bottomBatY - batHeight - ball.raio);
            ball.direcaoY = -1;

            int[][] layoutNC = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

            int[][] layoutU = layoutNC;

            for (int y = 1; y <= 10; y++) {
                for (int x = 0; x <= 9; x++) {
                    if (layoutU[y][x] != 0) {
                        layout[y][x][0] = Color.argb(100,255,255,255);

                        NBlocos += 1;
                        layout[y][x][1] = 0;

                        if (x == 9) {
                            layout[y][x][2] = 1;
                        } else {
                            layout[y][x][2] = 0;
                        }
                    } else {
                        layout[y][x][1] = 0;
                    }
                }
            }

            bolasAct = new Bola[1];

            bolasAct[0] = ball;

            feito = true;
        }
    }

    public void mudarCor(String noC){
        layout[ySS][xSS][0] = Color.parseColor(noC);
        Pai.redrawn();
    }

    public int getDP(float dp){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

    public void addPr(int p){

        if(ySS != 0) {
            layout[ySS][xSS][3] = p;
            Pai.redrawn();
        }
    }

    public void eli(){
        if(ySS != 0) {
            layout[ySS][xSS][0] = Color.argb(100, 255, 255, 255);
            layout[ySS][xSS][1] = 0;
            layout[ySS][xSS][3] = 0;
            ySS = 0;
            Pai.redrawn();
        }
    }

    public void modV(int v){
        if((layout[ySS][xSS][3] == 10) && (v < 0)){
            layout[ySS][xSS][3] = 0;
            layout[ySS][xSS][1] = 5;
            Pai.redrawn();
        }else {
            if(layout[ySS][xSS][3] != 10) {
                if ((layout[ySS][xSS][1] + v > 0)
                        && (layout[ySS][xSS][1] + v < 6)) {
                    layout[ySS][xSS][1] += v;
                    Pai.redrawn();
                } else {
                    if (layout[ySS][xSS][1] + v >= 6) {
                        layout[ySS][xSS][3] = 10;
                        layout[ySS][xSS][1] = 1;
                        Pai.redrawn();
                    }
                }
            }
        }
    }
}
