package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.Jogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

import java.util.Random;

public class GameState {

    Context context;
    GameThread pai;
    boolean cost = false;

    //Nivel
    int nNivel = 1;
    int NBlocos = 0;

    // Para inputs
    private float previousX;

    //Vida
    int Vida = 3;

    //specials
    int vvelocidadeY = 1;

    //moreLife
    private Bitmap imgVida;
    int nVidasAct = 0;
    int[][] vidasAct;

    //moreBalls
    private Bitmap imgMBalls;
    int nBallvAct = 0;
    //figuras
    int[][] ballsAct;
    int nBallAct = 0;
    //objecto
    Bola[] bolasAct;

    //+sizeBar
    private Bitmap imgMoreSize;
    int nMoreSizeAct = 0;
    int[][] moreSizeAct;

    //-sizeBar
    private Bitmap imgMinusSize;
    int nMinusSizeAct = 0;
    int[][] minusSizeAct;

    //+speedBall
    private Bitmap imgMoreSpeed;
    int nMoreSpeedAct = 0;
    int[][] moreSpeedAct;

    //-speedBall
    private Bitmap imgMinusSpeed;
    int nMinusSpeedAct = 0;
    int[][] minusSpeedAct;

    //Exploderino
    private Bitmap imgExplode;
    int nExplodesAct = 0;
    int[][] explodeAct;
    boolean toExp = false;

    //grab
    private Bitmap imgGrab;
    int nGrabsAct = 0;
    int[][] grabsAct;
    boolean grabing = false;
    boolean catched = false;
    //int nToques = 0;

    //oposiont
    private Bitmap imgTroc;
    int nTrocAct = 0;
    int[][] trocAct;
    boolean activoTro = false;

    //fimspecials

    //Tamanho e largura do ecra
    final int screenMinX = 0;
    final int screenMinY = 0;
    float screenWidth = 0;
    float screenHeight = 0;

    //As barras
    int britLength;	int briHeight;
    int[][][] layout = new int[11][10][4];
    Boolean feito = false;

    int batLength = 75;
    private final int batHeight = 40;
    int bottomBatX;
    int bottomBatY;

    public GameState(Context contextt, GameThread p)
    {
        context = contextt;
        pai = p;

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

    //O metodo de update
    public void update() {
        for (int n = 0; n <= nBallAct; n++) {
            Bola ball = bolasAct[n];
            if (ball != null) {
                //mover bola
                if(!catched){
                    ball.mover();
                    if((ball.verificarColi(bottomBatX, bottomBatY, batLength, batHeight)) && (grabing)){
                        catched = true;
                        ball.mudarCC(bottomBatX + (batLength/2), bottomBatY - batHeight - ball.raio);
                    }
                }else{
                    ball.mudarCC(bottomBatX + (batLength/2), bottomBatY - batHeight - ball.raio);
                }


                // Detecta colisoes e verifica marcação de ponto

                //ponto na barra de baixo
                if (ball.bot > screenHeight) {
                    Vida--;
                    ball.resetar("Bot");
                    ball.mudarCC(bottomBatX + (batLength) / 2, bottomBatY - batHeight - ball.raio);
                    if(n != 0){
                        bolasAct[n] = null;
                    }

                } else {
                    //colisao na barra de cima
                    if (ball.topo < screenMinY) {
                        ball.direcaoY = 1;
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
            }
        }

        veriImg(imgVida, 1, vidasAct, nVidasAct);
        veriImg(imgMBalls, 2, ballsAct, nBallvAct);
        veriImg(imgMoreSize, 3, moreSizeAct, nMoreSizeAct);
        veriImg(imgMinusSize, 4, minusSizeAct, nMinusSizeAct);
        veriImg(imgMoreSpeed, 5, moreSpeedAct, nMoreSpeedAct);
        veriImg(imgMinusSpeed, 6, minusSpeedAct, nMinusSpeedAct);
        veriImg(imgExplode, 7, explodeAct, nExplodesAct);
        veriImg(imgGrab, 8, grabsAct, nGrabsAct);
        veriImg(imgTroc, 9, trocAct, nTrocAct);

        //Verificação de colisoes de bola com barras
        veriCBB();
    }

    private void cointTempo(){
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        activoTro = false;
                    }
                },10000
        );
    }

    public void veriImg(Bitmap img,int id, int[][] imgAct, int number) {
        if ((number != 0) && (imgAct.length > 0)) {
            for (int x = 0; x <= number-1; x++) {
                int[] d = imgAct[x];
                if (d[2] == 0) {
                    if ((d[0] < bottomBatX + batLength) && (d[0] > bottomBatX)) {
                        if ((d[1] + img.getHeight() > bottomBatY - batHeight) && (d[1] + img.getHeight() < bottomBatY)) {
                            identP(id,x);
                        } else {
                            if ((d[1] > bottomBatY - batHeight) && (d[1] < bottomBatY)) {
                                identP(id,x);
                            }
                        }
                    } else {
                        if ((d[0] + img.getWidth() < bottomBatX + batLength) && (d[0] + img.getWidth() > bottomBatX)) {
                            if ((d[1] + img.getHeight() > bottomBatY - batHeight) && (d[1] + img.getHeight() < bottomBatY)) {
                                identP(id,x);
                            } else {
                                if ((d[1] > bottomBatY - batHeight) && (d[1] < bottomBatY)) {
                                    identP(id,x);
                                }
                            }
                        }
                    }
                    imgAct[x][1] += vvelocidadeY;
                }
            }
        }
    }

    public void identP(int id,int x){
        double t;
        switch(id){
            case 1:
                vidasAct[x][2] = 1;
                Vida += 1;
                break;

            case 2:
                ballsAct[x][2] = 1;
                addBalls(x);
                break;

            case 3:
                moreSizeAct[x][2] = 1;
                t = screenWidth * 0.01;
                batLength += (int)t;
                break;

            case 4:
                minusSizeAct[x][2] = 1;
                t = screenWidth * 0.01;
                batLength -= (int)t;
                break;

            case 5:
                moreSpeedAct[x][2] = 1;
                for (int n = 0; n <= nBallAct; n++) {
                    Bola ball = bolasAct[n];
                    if (ball != null) {
                        t = ball.speedX * 1;
                        ball.speedX += (int)t;
                        t = ball.speedY * 0.5;
                        ball.speedY += (int)t;
                    }
                }
                break;

            case 6:
                minusSpeedAct[x][2] = 1;
                for (int n = 0; n <= nBallAct; n++) {
                    Bola ball = bolasAct[n];
                    if (ball != null) {
                        t = ball.speedX * 0.5;
                        ball.speedX -= (int)t;
                        t = ball.speedY * 0.25;
                        ball.speedY -= (int)t;
                    }
                }
                break;

            case 7:
                explodeAct[x][2] = 1;
                toExp = true;
                break;

            case 8:
                grabsAct[x][2] = 1;
                grabing = true;
                break;

            case 9:
                trocAct[x][2] = 1;
                activoTro = true;
                cointTempo();
                break;
        }
    }

    public void addBalls(int x) {
        int yB = bottomBatY - batHeight - bolasAct[0].raio;
        int xB = bottomBatX + (batLength) / 2;

        ballsAct[x][2] = 1;
        nBallAct += 1;
        Bola ball = new Bola(bolasAct[0].raio, 4.5, 6, 0, "green", xB, yB, 0.3, 0.1);
        ball.direcaoY = -1;
        bolasAct[nBallAct] = ball;
    }

    public boolean screenTouch(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modifica o movimento da barra correspondente a input do touch no ecrã
                deltaX = currentX - previousX;
                if(activoTro){
                    if (!(bottomBatX + batLength + -deltaX >= screenWidth)) {
                        if (!(bottomBatX + -deltaX < 0)) {
                            bottomBatX += -deltaX;
                        }
                    }
                }else {
                    if (!(bottomBatX + batLength + deltaX >= screenWidth)) {
                        if (!(bottomBatX + deltaX < 0)) {
                            bottomBatX += deltaX;
                        }
                    }
                }

                if(catched){
                    if((currentY <= bottomBatY) &&(currentY >= bottomBatY - batHeight)){
                        if((currentX <= bottomBatX + batLength) && (currentX >= bottomBatX)){
                            catched = false;
                            grabing = false;
                        }
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

            for (Bola b : bolasAct) {
                if (b != null) {
                    //Desenha a bola
                    canvas.drawOval(new RectF(b.esque, b.topo, b.direita, b.bot),
                            paint);
                }
            }

            canvas.drawOval(new RectF(1, screenHeight - (bolasAct[0].raio * 4) - 2, bolasAct[0].raio * 4 + 1, screenHeight - 2),
                    paint);

            paint.setColor(Color.WHITE);
            double hei = screenHeight/35.95;
            paint.setTextSize(getDP((float)hei));

            Rect bounds = new Rect();

            if(!cost) {
                paint.getTextBounds("Nivel "+ nNivel,0,("Nivel "+ nNivel).length(),bounds);
                canvas.drawText("Nivel" + nNivel, (screenWidth / 2) - bounds.width(), 50, paint);
            }else{
                paint.getTextBounds("Nivel costumatizado",0,("Nivel costumatizado").length(),bounds);
                canvas.drawText("Nivel costumatizado", (screenWidth / 2) - bounds.width(), 50, paint);
            }

            canvas.drawText("x"+Vida,bolasAct[0].raio/2 , screenHeight - (bolasAct[0].raio), paint);

            //paint.setARGB(200, 50, 0, 255);

            for (int y = 0; y <= 10; y++) {
                for (int x = 0; x <= 9; x++) {
                    if (layout[y][x][1] > 0) {

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
                            canvas.drawText("x"+layout[y][x][1], x*britLength + (britLength/2), y*briHeight + briHeight +10, paint);
                        }
                    }
                }
            }
        }

        desenharImg(canvas,imgVida,nVidasAct,vidasAct);
        desenharImg(canvas,imgMBalls,nBallvAct,ballsAct);
        desenharImg(canvas,imgMoreSize,nMoreSizeAct,moreSizeAct);
        desenharImg(canvas, imgMinusSize, nMinusSizeAct, minusSizeAct);
        desenharImg(canvas, imgMoreSpeed, nMoreSpeedAct, moreSpeedAct);
        desenharImg(canvas, imgMinusSpeed, nMinusSpeedAct, minusSpeedAct);
        desenharImg(canvas, imgExplode, nExplodesAct, explodeAct);
        desenharImg(canvas, imgGrab, nGrabsAct, grabsAct);
        desenharImg(canvas, imgTroc, nTrocAct, trocAct);
        //Poe a cor da barra do fundo
        paint.setARGB(255,50,0,200);

        //Desnha a barra do fundo
        canvas.drawRect(new Rect(bottomBatX, bottomBatY, bottomBatX + batLength,
                bottomBatY - batHeight), paint);
    }

    private void desenharImg(Canvas c,Bitmap img,int limit, int[][] array){
        if((limit != 0) && (array.length > 0)) {
            for (int x = 0; x < limit; x++) {
                int[] d = array[x];
                if (d[2] == 0) {
                    c.drawBitmap(img, d[0], d[1], null);
                }
            }
        }
    }

    //funcoes de verificação

    //Verificação de colisoes de bola com barras
    public void veriCBB()
    {
        //Verificação do embate com os blocos
        if(feito) {
            for (int n = 0; n <= nBallAct; n++) {
                Bola ball = bolasAct[n];
                if ((ball != null) && (ball.bot < 12 * briHeight) && (ball.topo < 12 * briHeight)) {
                //if (ball != null) {
                    //propriedades da bola
                    int topo = ball.topo;
                    int bot = ball.bot;
                    int esque = ball.esque;
                    int dir = ball.direita;
                    int pX = ball.direita - ball.raio;
                    int pY = ball.topo + ball.raio;

                    for (int y = 0; y <= 10; y++) {
                        for (int x = 0; x <= 9; x++) {
                            if (layout[y][x][1] > 0) {
                                int actX = x * britLength;

                                //propriedades do bloco a verificar colisao
                                // y = y*briheiht

                                int BlyT = y * briHeight + 12;
                                int BlyB = (y * briHeight) + briHeight + 12;
                                int BlxE = actX;
                                int BlxD = actX + britLength;

                                Boolean bateu = false;

                                if ((pY < BlyB) && (pY > BlyT)) {
                                    if ((esque < BlxD) && (esque > BlxE)) {
                                        ball.direcaoX = 1;
                                        ball.mover();
                                        bateu = true;
                                    } else {
                                        if ((dir < BlxD) && (dir > BlxE)) {
                                            ball.direcaoX = -1;
                                            ball.mover();
                                            bateu = true;
                                        }
                                    }
                                } else {
                                    if ((topo < BlyB) && (BlyT < topo)) {
                                        if ((pX > actX) && (pX < BlxD)) {
                                            ball.direcaoY = 1;
                                            ball.mover();
                                            bateu = true;
                                        } else {
                                            if ((esque > BlxE) && (BlxD < esque)) {
                                                for (int c = 0; c <= 2; c++) {
                                                    if ((ball.Q1[c][0] > BlxE) && (ball.Q1[c][0] < esque)) {
                                                        ball.direcaoY = 1;
                                                        ball.direcaoX = -1;
                                                        ball.mover();
                                                        bateu = true;
                                                    }
                                                }
                                            }
                                            if ((dir > BlxE) && (BlxD < dir)) {
                                                for (int c = 0; c <= 2; c++) {
                                                    if ((ball.Q2[c][0] > BlxE) && (ball.Q2[c][0] < esque)) {
                                                        ball.direcaoY = 1;
                                                        ball.direcaoX = 1;
                                                        ball.mover();
                                                        bateu = true;
                                                    }
                                                }
                                            }

                                        }
                                    } else {
                                        if ((bot < BlyB) && (BlyT < bot)) {
                                            if ((pX > actX) && (pX < BlxD)) {
                                                ball.direcaoY = -1;
                                                bateu = true;
                                            } else {
                                                if ((esque > BlxE) && (BlxD < esque)) {
                                                    for (int c = 0; c <= 2; c++) {
                                                        if ((ball.Q3[c][0] > BlxE) && (ball.Q3[c][0] < esque)) {
                                                            ball.direcaoY = -1;
                                                            ball.direcaoX = -1;
                                                            ball.mover();
                                                            bateu = true;
                                                        }
                                                    }
                                                }
                                                if ((dir > BlxE) && (BlxD < dir)) {
                                                    for (int c = 0; c <= 2; c++) {
                                                        if ((ball.Q4[c][0] > BlxE) && (ball.Q4[c][0] < esque)) {
                                                            ball.direcaoY = -1;
                                                            ball.direcaoX = 1;
                                                            ball.mover();
                                                            bateu = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if ((bateu) && (layout[y][x][3] != 10)) {
                                    int BDest = 0;
                                    layout[y][x][1] -= 1;
                                    if (toExp) {
                                        if (y - 1 != 0) {
                                            if ((layout[y - 1][x][1] != 0) && (layout[y - 1][x][3] != 10)){
                                                layout[y - 1][x][1] -= 1;
                                                if (layout[y - 1][x][1] == 0) {
                                                    BDest += 1;
                                                }
                                            }
                                        }

                                        if (y + 1 != 11) {
                                            if ((layout[y + 1][x][1] != 0) && (layout[y + 1][x][3] != 10)) {
                                                layout[y + 1][x][1] -= 1;
                                                if (layout[y + 1][x][1] == 0) {
                                                    BDest += 1;
                                                }
                                            }
                                        }

                                        if (x - 1 != -1) {
                                            if ((layout[y][x - 1][1] != 0) && (layout[x - 1][x][3] != 10)) {
                                                layout[y][x - 1][1] -= 1;
                                                if (layout[y][x - 1][1] == 0) {
                                                    BDest += 1;
                                                }
                                            }
                                        }

                                        if (x + 1 != 10) {
                                            if ((layout[y][x + 1][1] != 0) && (layout[x + 1][x][3] != 10)) {
                                                layout[y][x + 1][1] -= 1;
                                                if (layout[y][x + 1][1] == 0) {
                                                    BDest += 1;
                                                }
                                            }
                                        }
                                        toExp = false;
                                    }
                                    if (layout[y][x][1] == 0) {
                                        BDest += 1;
                                    }

                                    NBlocos -= BDest;

                                    int l;
                                    if (NBlocos == 0) {
                                        nNivel += 1;
                                        feito = false;
                                    } else {
                                        switch (layout[y][x][3]) {
                                            case 1:
                                                l = nVidasAct;
                                                vidasAct[l][0] = BlxE + (britLength / 2);
                                                vidasAct[l][1] = BlyT + (briHeight / 2);
                                                nVidasAct += 1;
                                                break;

                                            case 2:
                                                l = nBallvAct;
                                                ballsAct[l][0] = BlxE + (britLength / 2);
                                                ballsAct[l][1] = BlyT + (briHeight / 2);
                                                nBallvAct += 1;
                                                break;

                                            case 3:
                                                l = nMoreSizeAct;
                                                moreSizeAct[l][0] = BlxE + (britLength / 2);
                                                moreSizeAct[l][1] = BlyT + (briHeight / 2);
                                                nMoreSizeAct += 1;
                                                break;

                                            case 4:
                                                l = nMinusSizeAct;
                                                minusSizeAct[l][0] = BlxE + (britLength / 2);
                                                minusSizeAct[l][1] = BlyT + (briHeight / 2);
                                                nMinusSizeAct += 1;
                                                break;

                                            case 5:
                                                l = nMoreSpeedAct;
                                                moreSpeedAct[l][0] = BlxE + (britLength / 2);
                                                moreSpeedAct[l][1] = BlyT + (briHeight / 2);
                                                nMoreSpeedAct += 1;
                                                break;

                                            case 6:
                                                l = nMinusSpeedAct;
                                                minusSpeedAct[l][0] = BlxE + (britLength / 2);
                                                minusSpeedAct[l][1] = BlyT + (briHeight / 2);
                                                nMinusSpeedAct += 1;
                                                break;

                                            case 7:
                                                l = nExplodesAct;
                                                explodeAct[l][0] = BlxE + (britLength / 2);
                                                explodeAct[l][1] = BlyT + (briHeight / 2);
                                                nExplodesAct += 1;
                                                break;

                                            case 8:
                                                l = nGrabsAct;
                                                grabsAct[l][0] = BlxE + (britLength / 2);
                                                grabsAct[l][1] = BlyT + (briHeight / 2);
                                                nGrabsAct += 1;
                                                break;

                                            case 9:
                                                l = nTrocAct;
                                                trocAct[l][0] = BlxE + (britLength / 2);
                                                trocAct[l][1] = BlyT + (briHeight / 2);
                                                nTrocAct += 1;
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

            //A bola
            Bola ball;

            ball = new Bola((int) t, 4.5, 6, 0, "green", 0, 0, 0.3, 0.1);
            ball.mudarCC(bottomBatX + (batLength) / 2, bottomBatY - batHeight - ball.raio);
            ball.direcaoY = -1;

            int[][][] layoutN0 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}}};

            int[][][] layoutN1 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,1},{1,1},{1,1},{0,0},{0,0},{0,0},{0,0},{1,1},{1,1},{1,1}},
                    {{1,1},{1,1},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,1},{1,1}},
                    {{1,1},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,1}},
                    {{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{11,2},{11,2},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{11,2},{11,2},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0}},
                    {{1,1},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,1}},
                    {{1,1},{1,1},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{8,1},{9,1}},
                    {{4,1},{5,1},{3,1},{0,0},{0,0},{0,0},{0,0},{10,1},{6,1},{7,1}}};

            int[][][] layoutN2 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,0},{1,0},{1,0},{1,0},{1,0},{3,0},{1,0},{1,0},{1,0},{1,0}},
                    {{1,0},{1,0},{1,0},{1,0},{0,0},{0,0},{1,0},{1,0},{1,0},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{0,0},{1,0},{1,0},{0,0},{0,0},{1,0},{1,0},{0,0},{1,0}},
                    {{1,0},{0,0},{1,0},{1,0},{0,0},{0,0},{1,0},{1,0},{0,0},{1,0}},
                    {{1,0},{0,0},{1,0},{1,0},{0,0},{0,0},{1,0},{1,0},{0,0},{1,0}},
                    {{1,0},{0,0},{1,0},{2,0},{0,0},{0,0},{2,0},{1,0},{0,0},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{1,0},{1,0},{1,0},{0,0},{0,0},{1,0},{1,0},{1,0},{1,0}},
                    {{1,0},{1,0},{1,0},{1,0},{20,0},{20,0},{1,0},{1,0},{1,0},{1,0}}};

            int[][][] layoutN3 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,0},{1,0},{2,0},{1,0},{1,0},{1,0},{1,0},{1,3},{1,1},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{0,0},{2,0},{1,0},{1,0},{1,0},{1,0},{1,0},{1,0},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{1,0},{1,0},{1,0},{2,0},{1,0},{1,0},{2,0},{0,0},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{0,0},{2,0},{1,0},{1,0},{1,0},{1,0},{1,0},{1,0},{1,0}},
                    {{1,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,0}},
                    {{1,0},{1,0},{0,0},{0,0},{1,0},{3,0},{0,0},{0,0},{1,0},{1,0}},
                    {{1,0},{1,0},{1,0},{1,5},{1,0},{1,0},{1,0},{1,0},{1,0},{1,0}}};

            int[][][] layoutN4 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{3,2},{1,2},{0,0},{0,0},{6,3},{1,3},{0,0},{1,4},{1,4},{5,4}},
                    {{1,2},{0,0},{1,2},{0,0},{1,3},{1,3},{0,0},{1,4},{1,4},{1,4}},
                    {{1,2},{0,0},{1,2},{0,0},{1,3},{1,3},{0,0},{1,4},{0,0},{0,0}},
                    {{1,2},{0,0},{1,2},{0,0},{1,3},{6,3},{0,0},{5,4},{0,0},{0,0}},
                    {{1,2},{0,0},{3,2},{0,0},{1,3},{1,3},{0,0},{1,4},{1,4},{0,0}},
                    {{3,2},{0,0},{1,2},{0,0},{1,3},{1,3},{0,0},{1,4},{1,4},{0,0}},
                    {{1,2},{0,0},{1,2},{0,0},{6,3},{1,3},{0,0},{5,4},{0,0},{0,0}},
                    {{1,2},{0,0},{1,2},{0,0},{1,3},{1,3},{0,0},{1,4},{0,0},{0,0}},
                    {{1,2},{0,0},{1,2},{0,0},{1,3},{1,3},{0,0},{1,4},{1,4},{1,4}},
                    {{1,2},{3,2},{0,0},{0,0},{1,3},{6,3},{0,0},{1,4},{1,4},{5,4}}};

            int[][][] layoutN5 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,2},{1,2},{1,2},{1,2},{1,2},{0,0},{0,0},{0,0}},
                    {{0,0},{1,2},{1,3},{1,3},{1,2},{1,3},{1,3},{1,2},{0,0},{0,0}},
                    {{1,2},{1,3},{3,3},{1,3},{1,2},{1,3},{1,3},{2,3},{1,2},{0,0}},
                    {{1,2},{1,2},{1,1},{1,2},{8,2},{1,2},{9,1},{1,2},{1,2},{1,2}},
                    {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,5}},
                    {{1,2},{1,2},{1,2},{1,2},{1,2},{5,2},{1,2},{1,2},{1,2},{1,2}},
                    {{20,6},{6,4},{6,4},{20,6},{20,6},{20,6},{20,6},{6,4},{6,4},{20,6}},
                    {{20,6},{20,6},{8,6},{20,6},{20,6},{20,6},{20,6},{20,6},{20,6},{20,6}},
                    {{20,6},{20,6},{20,6},{20,6},{8,6},{20,6},{20,6},{20,6},{20,6},{8,6}}};

            int[][][] layoutN6 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,4},{0,0},{0,0},{9,5},{0,0},{0,0},{1,5},{0,0},{0,0},{1,4}},
                    {{0,0},{6,4},{0,0},{0,0},{1,2},{1,2},{0,0},{0,0},{7,4},{0,0}},
                    {{0,0},{0,0},{1,1},{0,0},{1,1},{1,1},{0,0},{1,1},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,1},{1,3},{1,3},{1,1},{0,0},{0,0},{0,0}},
                    {{3,4},{1,4},{1,1},{1,1},{8,3},{8,3},{1,1},{1,0},{1,4},{3,4}},
                    {{0,0},{0,0},{0,0},{1,1},{1,3},{2,3},{1,1},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{6,1},{1,1},{1,1},{7,1},{1,1},{0,0},{0,0}},
                    {{0,0},{7,4},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{6,4},{0,0}},
                    {{1,4},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,4}}};

            int[][][] layoutN7 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{11,0},{0,0},{0,0},{1,2},{2,2},{9,2},{1,2},{0,0},{0,0},{11,0}},
                    {{1,2},{11,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{11,0},{1,2}},
                    {{1,2},{1,2},{11,0},{0,0},{0,0},{0,0},{0,0},{11,0},{1,2},{1,2}},
                    {{1,2},{1,2},{1,2},{11,0},{0,0},{0,0},{11,0},{1,2},{1,2},{1,2}},
                    {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
                    {{1,2},{8,2},{1,2},{1,2},{4,2},{5,2},{2,2},{1,2},{8,2},{1,2}},
                    {{1,2},{1,2},{1,2},{11,0},{0,0},{0,0},{11,0},{1,2},{1,2},{1,2}},
                    {{1,2},{1,2},{11,0},{0,0},{0,0},{0,0},{0,0},{11,0},{1,2},{1,2}},
                    {{1,2},{11,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{11,0},{1,2}},
                    {{1,2},{0,0},{0,0},{6,2},{6,2},{3,2},{3,2},{0,0},{0,0},{1,2}}};

            int[][][] layoutN8 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,2},{1,2},{1,2},{1,2},{4,2},{1,2},{4,2},{11,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{11,0},{0,0},{0,0}},
                    {{1,3},{3,3},{1,3},{1,3},{1,3},{3,3},{1,3},{11,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{11,0},{0,0},{0,0}},
                    {{1,1},{1,1},{6,1},{1,1},{6,1},{1,1},{1,1},{11,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{11,0},{0,0},{0,0}},
                    {{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{11,0},{0,0},{0,0}},
                    {{1,4},{1,4},{1,4},{8,4},{1,4},{1,4},{2,4},{11,0},{0,0},{0,0}},
                    {{11,0},{11,0},{11,0},{11,0},{11,0},{11,0},{11,0},{11,0},{0,0},{0,0}}};

            int[][][] layoutN8b = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,2},{0,0},{0,0},{0,0},{11,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,2},{2,2},{1,2},{0,0},{11,0},{2,2},{11,0},{0,0}},
                    {{0,0},{11,0},{2,2},{1,2},{1,2},{1,2},{2,2},{1,2},{2,2},{11,0}},
                    {{0,0},{11,0},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{11,0}},
                    {{0,0},{11,0},{6,2},{6,2},{6,2},{6,2},{6,2},{6,2},{6,2},{11,0}},
                    {{0,0},{0,0},{11,0},{1,2},{1,2},{1,2},{1,2},{1,2},{11,0},{0,0}},
                    {{0,0},{0,0},{0,0},{11,0},{1,2},{1,2},{1,2},{11,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{11,0},{2,2},{11,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{1,2},{0,0},{0,0},{0,0},{0,0}}};

            int[][][] layoutN9 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,3},{1,3},{1,3},{1,3},{0,0},{0,0},{1,3},{1,3},{1,3},{1,3}},
                    {{1,3},{11,6},{11,6},{1,3},{0,0},{0,0},{1,3},{11,6},{11,0},{1,3}},
                    {{1,3},{11,6},{11,6},{1,3},{0,0},{0,0},{1,3},{11,6},{11,0},{1,3}},
                    {{1,3},{1,3},{1,3},{1,3},{0,0},{0,0},{1,3},{1,3},{1,3},{1,3}},
                    {{0,0},{0,0},{0,0},{0,0},{11,0},{11,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{11,0},{11,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,3},{1,3},{1,3},{1,3},{0,0},{0,0},{1,3},{1,3},{1,3},{1,3}},
                    {{1,3},{11,0},{11,0},{1,3},{0,0},{0,0},{1,3},{11,0},{11,0},{1,3}},
                    {{1,3},{11,0},{11,0},{1,3},{0,0},{0,0},{1,3},{11,0},{11,0},{1,3}},
                    {{1,3},{1,3},{1,3},{1,3},{0,0},{0,0},{1,3},{1,3},{1,3},{1,3}}};

            int[][][] layoutN10 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,4},{1,4},{1,4},{0,0},{0,0},{0,0},{0,0},{1,4},{1,4},{1,4}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,4},{0,0},{0,0},{0,0},{0,0},{1,4},{0,0},{0,0}},
                    {{0,0},{0,0},{1,4},{0,0},{0,0},{0,0},{0,0},{1,4},{0,0},{0,0}},
                    {{0,0},{0,0},{1,4},{0,0},{1,4},{1,4},{0,0},{1,4},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{1,4},{1,4},{0,0},{0,0},{0,0},{0,0}},
                    {{1,4},{0,0},{0,0},{0,0},{1,4},{1,4},{0,0},{0,0},{0,0},{1,4}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{0,0}},
                    {{0,0},{0,0},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{0,0},{0,0}}};

            int[][][] layoutN11 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
                    {{7,2},{1,2},{11,6},{1,2},{1,2},{1,2},{1,2},{11,6},{1,2},{6,2}},
                    {{1,2},{1,2},{11,6},{1,2},{1,2},{1,2},{1,2},{11,6},{1,2},{1,2}},
                    {{1,2},{1,2},{11,6},{11,6},{0,0},{0,0},{11,6},{11,6},{1,2},{1,2}},
                    {{1,2},{1,2},{11,6},{0,0},{0,0},{0,0},{0,0},{11,6},{1,2},{1,2}},
                    {{1,2},{1,2},{11,6},{0,0},{11,6},{11,6},{0,0},{11,6},{1,2},{1,2}},
                    {{1,2},{8,2},{1,2},{0,0},{0,0},{0,0},{0,0},{1,2},{8,2},{1,2}},
                    {{1,2},{1,2},{11,6},{0,0},{0,0},{0,0},{0,0},{11,6},{1,2},{1,2}},
                    {{1,2},{1,2},{11,6},{0,0},{11,6},{11,6},{0,0},{11,6},{1,2},{1,2}},
                    {{11,6},{11,6},{11,6},{0,0},{0,0},{0,0},{0,0},{11,6},{11,6},{11,6}}};
					
			int[][][] layoutN12 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{4,1},{0,0},{1,1},{0,0},{1,1},{0,0},{1,1},{0,0},{4,1}},
                    {{0,0},{0,0},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{1,5},{1,5},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,5},{1,5},{1,5},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,5},{1,5},{1,5},{8,1},{0,0},{1,1},{0,0},{1,1},{0,0},{1,1}},
                    {{1,5},{1,5},{1,5},{1,1},{0,0},{1,1},{0,0},{1,1},{0,0},{1,1}},
                    {{1,5},{1,5},{1,5},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{1,5},{0,0},{0,0},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{6,1},{0,0},{6,1},{0,0},{4,1},{0,0},{6,1},{0,0},{6,1}}};					

			int[][][] layoutN13 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{0,0}},
                    {{0,0},{0,0},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
                    {{0,0},{0,0},{5,1},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
                    {{4,1},{1,1},{1,1},{1,1},{1,2},{1,2},{1,2},{1,2},{1,2},{1,0}},
                    {{0,0},{0,0},{1,2},{1,2},{1,2},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{0,0},{1,1},{1,1},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{0,0},{8,1},{9,1},{0,0},{1,1},{1,1},{0,0}},
                    {{0,0},{0,0},{10,1},{1,1},{1,1},{1,1},{3,1},{1,1},{1,1},{0,0}},
                    {{0,0},{0,0},{0,0},{1,1},{3,1},{1,1},{1,1},{1,1},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}}};
								
			int[][][] layoutN14 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4}},
                    {{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4}},
                    {{1,4},{8,4},{20,0},{20,0},{8,4},{1,4},{20,0},{20,0},{8,4},{1,4}},
                    {{1,4},{1,4},{20,0},{20,0},{1,4},{8,4},{20,0},{20,0},{1,4},{1,4}},
                    {{1,4},{1,4},{1,4},{1,4},{20,0},{20,0},{1,4},{1,4},{4,4},{1,4}},
                    {{1,4},{1,4},{1,4},{20,0},{20,0},{20,0},{20,0},{1,4},{1,4},{1,4}},
                    {{1,4},{8,4},{1,4},{20,0},{20,0},{20,0},{20,0},{1,4},{8,4},{1,4}},
                    {{1,4},{1,4},{1,4},{20,0},{1,4},{1,4},{20,0},{1,4},{1,4},{1,4}},
                    {{1,4},{1,4},{1,4},{1,4},{8,4},{8,4},{1,4},{1,4},{1,4},{1,4}},
                    {{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4},{1,4}}};
					
			int[][][] layoutN15 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{1,5},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,5},{2,5},{1,5},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,5},{1,5},{1,5},{0,0},{0,0},{0,0},{0,0}},
                    {{1,5},{1,5},{1,5},{1,5},{1,5},{1,5},{1,5},{1,5},{1,5},{0,0}},
                    {{0,0},{1,5},{1,5},{20,0},{1,5},{20,0},{1,5},{1,5},{0,0},{0,0}},
                    {{0,0},{0,0},{1,5},{20,0},{1,5},{20,0},{1,5},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{4,5},{4,5},{1,5},{4,5},{4,5},{0,0},{0,0},{0,0}},
                    {{0,0},{6,5},{4,5},{4,5},{0,0},{4,5},{4,5},{6,5},{0,0},{0,0}},
                    {{0,0},{6,5},{6,5},{0,0},{0,0},{0,0},{6,5},{6,5},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}}};

			int[][][] layoutN16 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{0,0},{0,0}},
                    {{0,0},{1,7},{1,7},{1,7},{9,7},{9,7},{1,7},{1,7},{1,7},{0,0}},
                    {{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7}},
                    {{1,7},{3,1},{0,0},{1,7},{1,7},{1,7},{1,7},{3,1},{0,0},{1,7}},
                    {{1,7},{0,0},{0,0},{1,7},{1,7},{1,7},{1,7},{0,0},{0,0},{1,7}},
                    {{1,7},{0,0},{0,0},{1,7},{6,7},{6,7},{1,7},{0,0},{0,0},{1,7}},
                    {{1,7},{3,1},{3,1},{0,0},{0,0},{0,0},{0,0},{3,1},{3,1},{1,7}},
                    {{0,0},{3,1},{1,7},{0,0},{0,0},{0,0},{0,0},{1,7},{3,1},{0,0}},
                    {{0,0},{0,0},{1,7},{1,7},{1,7},{1,7},{1,7},{1,7},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{1,7},{1,7},{1,7},{1,7},{0,0},{0,0},{0,0}}};

			int[][][] layoutN17 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{0,0},{0,0}},
                    {{0,0},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{0,0}},
                    {{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
                    {{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
                    {{1,1},{0,0},{0,0},{1,1},{1,1},{1,1},{1,1},{0,0},{0,0},{1,1}},
                    {{1,1},{0,0},{2,2},{0,0},{1,1},{1,1},{0,0},{1,2},{0,0},{1,1}},
                    {{1,1},{1,1},{0,0},{1,1},{1,1},{1,1},{1,1},{0,0},{1,1},{1,1}},
                    {{0,0},{1,1},{1,1},{1,1},{0,0},{0,0},{1,1},{1,1},{1,1},{0,0}},
                    {{0,0},{0,0},{1,1},{1,1},{4,1},{4,1},{1,1},{1,1},{0,0},{0,0}},
                    {{0,0},{0,0},{10,1},{0,0},{0,0},{0,0},{0,0},{10,1},{0,0},{0,0}}};
								
			int[][][] layoutN18 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{0,0},{0,0},{0,0},{20,8},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{1,9},{1,9},{1,9},{20,8},{1,9},{1,9},{1,9},{1,9},{0,0}},
                    {{1,9},{1,9},{1,9},{1,9},{20,8},{1,9},{1,9},{1,9},{1,9},{1,9}},
                    {{1,9},{20,8},{20,8},{20,8},{20,8},{20,8},{20,8},{20,8},{1,9},{1,9}},
                    {{20,8},{1,7},{11,0},{1,7},{20,8},{1,7},{11,0},{1,7},{20,8},{1,9}},
                    {{1,9},{20,8},{1,7},{20,8},{20,8},{20,8},{1,7},{20,8},{1,9},{1,9}},
                    {{1,9},{1,9},{20,8},{20,8},{1,7},{20,8},{20,8},{1,9},{1,9},{1,9}},
                    {{1,9},{20,8},{1,7},{0,0},{0,0},{0,0},{1,7},{20,8},{1,9},{20,8}},
                    {{0,0},{20,8},{1,7},{3,7},{4,7},{3,7},{1,7},{20,8},{20,8},{0,0}}};
								
			int[][][] layoutN19 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{0,0},{5,1},{1,1},{0,0},{0,0},{0,0},{7,1},{1,1},{0,0},{0,0}},
                    {{0,0},{1,1},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{1,1},{0,0}},
                    {{0,0},{1,1},{0,0},{5,1},{0,0},{0,0},{1,1},{0,0},{7,1},{0,0}},
                    {{0,0},{1,1},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{1,1},{0,0}},
                    {{0,0},{5,1},{1,1},{0,0},{0,0},{0,0},{7,1},{1,1},{0,0},{0,0}},
                    {{0,0},{1,3},{1,3},{6,3},{0,0},{0,0},{3,3},{1,3},{1,3},{0,0}},
                    {{0,0},{1,3},{0,0},{0,0},{0,0},{0,0},{1,3},{0,0},{8,3},{0,0}},
                    {{0,0},{6,3},{1,3},{1,3},{0,0},{0,0},{1,3},{1,3},{1,3},{0,0}},
                    {{0,0},{0,0},{0,0},{1,3},{0,0},{0,0},{1,3},{0,0},{0,0},{0,0}},
                    {{0,0},{6,3},{1,3},{1,3},{0,0},{0,0},{3,3},{0,0},{0,0},{0,0}}};
								
			int[][][] layoutN20 = {{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
                    {{1,1},{1,1},{6,1},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{0,0}},
                    {{1,1},{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{0,0}},
                    {{6,1},{1,1},{1,1},{0,0},{7,1},{0,0},{0,0},{1,1},{0,0},{0,0}},
                    {{0,0},{0,0},{1,1},{0,0},{1,1},{0,0},{0,0},{1,1},{0,0},{0,0}},
                    {{1,1},{1,1},{6,1},{0,0},{1,1},{1,1},{0,0},{10,1},{0,1},{0,1}},
                    {{1,3},{0,0},{1,3},{0,0},{1,3},{1,3},{0,0},{2,3},{1,3},{0,0}},
                    {{8,3},{9,3},{8,3},{0,0},{1,3},{0,0},{0,0},{1,3},{0,0},{1,3}},
                    {{0,0},{1,3},{0,0},{0,0},{8,3},{1,3},{0,0},{1,3},{1,3},{0,0}},
                    {{0,0},{1,3},{0,0},{0,0},{1,3},{0,0},{0,0},{1,3},{0,0},{1,3}},
                    {{0,0},{1,3},{0,0},{0,0},{1,3},{1,3},{0,0},{1,3},{0,0},{1,3}}};

            int[][][] layoutU;

            cost = false;
            int[][][] l = pai.l;

            if(pai.l == null) {
                switch (nNivel) {
                    case 1:
                        layoutU = layoutN1;
                        break;
                    case 2:
                        layoutU = layoutN2;
                        break;
                    case 3:
                        layoutU = layoutN3;
                        break;
                    case 4:
                        layoutU = layoutN4;
                        break;
                    case 5:
                        layoutU = layoutN5;
                        break;
                    case 6:
                        layoutU = layoutN6;
                        break;
                    case 7:
                        layoutU = layoutN7;
                        break;
                    case 8:
                        layoutU = layoutN8;
                        if(Vida == 1){
                            layoutU = layoutN8b;
                        }
                        break;
                    case 9:
                        layoutU = layoutN9;
                        break;
                    case 10:
                        layoutU = layoutN10;
                        break;
                    case 11:
                        layoutU = layoutN11;
                        break;
					case 12:
					layoutU = layoutN12;
						break;
					case 13:
					layoutU = layoutN13;
						break;
					case 14:
					layoutU = layoutN14;
						break;
					case 15:
					layoutU = layoutN15;
						break;
					case 16:
					layoutU = layoutN16;
						break;
					case 17:
					layoutU = layoutN17;
						break;
					case 18:
					layoutU = layoutN18;
						break;
					case 19:
					layoutU = layoutN19;
						break;
					case 20:
					layoutU = layoutN20;
						break;
                    default:
                        layoutU = layoutN0;
                        break;
                }
            }else{
                int[][][] nl = new int[11][10][3];
                for (int i = 0; i <= 10; i++) {
                    for (int j = 0; j <= 9; j++) {
                        nl[i][j][0] = l[i][j][1];
                        nl[i][j][1] = 0;
                        nl[i][j][2] = l[i][j][3];
                    }
                }
                layoutU = nl;
                cost = true;
            }

            int nVidasc = 0;
            int nBolasc = 1;
            int nMoreSizec = 0;
            int nMinusSizec = 0;
            int nMoreSpeedc = 0;
            int nMinusSpeedc = 0;
            int nEsploderinoc = 0;
            int nGrabBalsc = 0;
            int nTrocc = 0;

            for (int y = 1; y <= 10; y++) {
                for (int x = 0; x <= 9; x++) {
                    if (layoutU[y][x][0] != 0) {
                        int randomC;
                        if(layoutU[y][x][1] == 0) {
                            randomC = new Random().nextInt(6) + 1;
                        }else{
                            randomC = layoutU[y][x][1];
                        }
                        switch (randomC) {
                            case 1:
                                layout[y][x][0] = Color.WHITE;
                                break;

                            case 2:
                                layout[y][x][0] = Color.RED;
                                break;

                            case 3:
                                layout[y][x][0] = Color.BLUE;
                                break;

                            case 4:
                                layout[y][x][0] = Color.GREEN;
                                break;

                            case 5:
                                layout[y][x][0] = Color.YELLOW;
                                break;

                            case 6:
                                layout[y][x][0] = Color.GRAY;
                                break;

                            case 7:
                                layout[y][x][0] = Color.rgb(230,185,184);
                                break;

                            case 8:
                                layout[y][x][0] = Color.rgb(90,90,90);
                                break;

                            case 9:
                                layout[y][x][0] = Color.rgb(165,165,165);
                                break;

                            default:
                                Log.d("Bug", "Bug");
                                break;
                        }

                        if(cost){
                            layout[y][x][0] = l[y][x][0];
                            layout[y][x][1] = l[y][x][1];
                        }else{
                            layout[y][x][1] = 1;
                        }

                        if(cost) {
                            if (layoutU[y][x][2] != 11) {
                                NBlocos += 1;
                            }
                        }else{
                            if(layoutU[y][x][0] != 11){
                                NBlocos += 1;
                            }
                        }

                        if (x == 9) {
                            layout[y][x][2] = 1;
                        } else {
                            layout[y][x][2] = 0;
                        }

                        if(cost){
                            layout[y][x][3] = l[y][x][3];
                            switch (l[y][x][3]) {
                                case 1:
                                    nVidasc += 1;
                                    break;

                                case 2:
                                    nBolasc += 1;
                                    break;

                                case 3:
                                    nMoreSizec += 1;
                                    break;

                                case 4:
                                    nMinusSizec += 1;
                                    break;

                                case 5:
                                    nMoreSpeedc += 1;
                                    break;

                                case 6:
                                    nMinusSpeedc += 1;
                                    break;

                                case 7:
                                    nEsploderinoc += 1;
                                    break;

                                case 8:
                                    nGrabBalsc += 1;
                                    break;

                                case 9:
                                    nTrocc += 1;
                                    break;

                                default:
                                    break;
                            }
                        }else {
                            switch (layoutU[y][x][0]) {
                                case 2:
                                    layout[y][x][3] = 1;
                                    nVidasc += 1;
                                    break;

                                case 3:
                                    layout[y][x][3] = 2;
                                    nBolasc += 1;
                                    break;

                                case 4:
                                    layout[y][x][3] = 3;
                                    nMoreSizec += 1;
                                    break;

                                case 5:
                                    layout[y][x][3] = 4;
                                    nMinusSizec += 1;
                                    break;

                                case 6:
                                    layout[y][x][3] = 5;
                                    nMoreSpeedc += 1;
                                    break;

                                case 7:
                                    layout[y][x][3] = 6;
                                    nMinusSpeedc += 1;
                                    break;

                                case 8:
                                    layout[y][x][3] = 7;
                                    nEsploderinoc += 1;
                                    break;

                                case 9:
                                    layout[y][x][3] = 8;
                                    nGrabBalsc += 1;
                                    break;

                                case 10:
                                    layout[y][x][3] = 9;
                                    nTrocc += 1;
                                    break;

                                case 11:
                                    layout[y][x][3] = 10;
                                    break;

                                default:
                                    if (layoutU[y][x][0] < 20) {
                                        layout[y][x][3] = 0;
                                    } else {
                                        layout[y][x][1] += 1 + (layoutU[y][x][0] - 20);
                                    }
                                    break;
                            }
                        }
                    } else {
                        layout[y][x][1] = 0;
                    }
                }
            }

            vidasAct = new int[nVidasc][3];
            ballsAct = new int[nBolasc-1][3];
            bolasAct = new Bola[nBolasc];
            moreSizeAct = new int[nMoreSizec][3];
            minusSizeAct = new int[nMinusSizec][3];
            moreSpeedAct = new int[nMoreSpeedc][3];
            minusSpeedAct = new int[nMinusSpeedc][3];
            explodeAct = new int[nEsploderinoc][3];
            grabsAct = new int[nGrabBalsc][3];
            trocAct = new int[nTrocc][3];

            bolasAct[0] = ball;

            feito = true;
            Log.d("Teste", NBlocos+"");
        }
    }

    public int getDP(float dp){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
