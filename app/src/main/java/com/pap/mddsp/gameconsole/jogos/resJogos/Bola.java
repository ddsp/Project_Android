package com.pap.mddsp.gameconsole.jogos.resJogos;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Random;

public class Bola implements Serializable{

    public int raio;

    public double speedY;
    public double speedX;
    public double speedXF;
    public double speedYF;

    public int speedLimit;

    public int direcaoX = 1;
    public int direcaoY = 1;

    public String cor;


    //pontos principais
    private int pX;
    private int pXR;
    private int pY;
    private int pYR;
    public int topo;
    public int bot;
    public int esque;
    public int direita;

    //pontos secundarios
    public int[][] Q1 = new int[3][3];
    public int[][] Q2 = new int[3][3];
    public int[][] Q3 = new int[3][3];
    public int[][] Q4 = new int[3][3];


    public Bola(int raioo, double speedYY, double speedXX, int speedLimitt,
                String corr, int x, int y, double mutiplicadory, double mutiplicadorx){
        raio = raioo;

        speedY = speedYY;
        speedYF = speedY;
        speedX = speedXX;
        speedXF = speedX;
        speedLimit = speedLimitt;

        cor = corr;

        pX = x;
        pXR = pX;
        pY = y;
        pYR = pY;

        actualizarF();

        actualizarQs();
    }

    private void actualizarQs(){

        Q1[0][0] = (int)(pX - (Math.cos(68) * raio));
        Q1[1][0] = (int)(pX - (Math.cos(45) * raio));
        Q1[2][0] = (int)(pX - (Math.cos(23) * raio));

        Q1[0][1] = (int)(pY - (Math.sin(68) * raio));
        Q1[1][1] = (int)(pY - (Math.sin(45) * raio));
        Q1[2][1] = (int)(pY - (Math.sin(23) * raio));

        Q2[0][0] = (int)(pX + (Math.cos(68) * raio));
        Q2[1][0] = (int)(pX + (Math.cos(45) * raio));
        Q2[2][0] = (int)(pX + (Math.cos(23) * raio));

        Q2[0][1] = (int)(pY - (Math.sin(68) * raio));
        Q2[1][1] = (int)(pY - (Math.sin(45) * raio));
        Q2[2][1] = (int)(pY - (Math.sin(23) * raio));

        Q3[0][0] = (int)(pX - (Math.cos(68) * raio));
        Q3[1][0] = (int)(pX - (Math.cos(45) * raio));
        Q3[2][0] = (int)(pX - (Math.cos(23) * raio));

        Q3[0][1] = (int)(pY + (Math.sin(68) * raio));
        Q3[1][1] = (int)(pY + (Math.sin(45) * raio));
        Q3[2][1] = (int)(pY + (Math.sin(23) * raio));

        Q4[0][0] = (int)(pX + (Math.cos(68) * raio));
        Q4[1][0] = (int)(pX + (Math.cos(45) * raio));
        Q4[2][0] = (int)(pX + (Math.cos(23) * raio));

        Q4[0][1] = (int)(pY + (Math.sin(68) * raio));
        Q4[1][1] = (int)(pY + (Math.sin(45) * raio));
        Q4[2][1] = (int)(pY + (Math.sin(23) * raio));
    }

    public void mover(){
        pY += speedY * direcaoY;
        pX += speedX * direcaoX;
        actualizarF();
        actualizarQs();
    }

    private void actualizarF(){
        topo = pY - raio;
        bot = pY + raio;
        esque = pX - raio;
        direita = pX + raio;
    }

    public void resetar(String vence){
        int r = new Random().nextInt(2)+1;
        pX = pXR;
        pY = pYR;

        speedY = speedYF;
        speedX = speedXF;

        if(vence == "Top"){
            direcaoY = 1;
            if(r == 1){
                direcaoX = -1;
            }else{
                direcaoX = 1;
            }
        }else{
            direcaoY = -1;
            if(r == 1){
                direcaoX = -1;
            }else{
                direcaoX = 1;
            }
        }
    }

    public void mudarDirecao(int d,String m){
        if(m.equals("x")){
            direcaoX = d;
        }else{
            direcaoY = d;
        }
    }

    public Boolean verificarColi(int bx, int by, int length, int alt){
        //Colisao com barras

        if (((pY > by) && (pY < by + alt))) {
            if ((esque < bx + length) && (esque > bx)) {
                direcaoX = -1;
                direcaoY = 1;
                mover();
                return true;
            } else {
                if ((direita < bx + length) && (direita > bx)) {
                    direcaoX = 1;
                    direcaoY = 1;
                    mover();
                    return true;
                }
            }
        }else {
            if ((pY < by) && (pY > by - alt)) {
                if ((esque < bx + length) && (esque > bx)) {
                    direcaoX = 1;
                    direcaoY = -1;
                    mover();
                } else {
                    if ((direita < bx + length) && (direita > bx)) {
                        direcaoX = -1;
                        direcaoY = -1;
                        mover();
                        return true;
                    }
                }
            }else{
                if ((topo > by) && (topo < by + alt)) {
                    if ((esque < bx + length) && (esque > bx)) {
                        Log.d("teste", "1");
                        for (int x = 0; x <= 2; x++) {
                            if ((Q1[x][0] < bx + length) && (Q1[x][0] > bx)) {
                                direcaoX = 1;
                                direcaoY = 1;

                                mover();
                                return true;
                            }
                        }
                    } else {
                        if ((direita < bx + length) && (direita > bx)) {
                            for (int x = 0; x <= 2; x++) {
                                if ((Q2[x][0] < bx + length) && (Q2[x][0] > bx)) {
                                    direcaoX = -1;
                                    direcaoY = 1;

                                    mover();
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    if ((bot > by - alt) && (bot < by)) {
                        if ((direita < bx + length) && (direita > bx)) {
                            for (int x = 0; x <= 2; x++) {
                                if ((Q3[x][0] < bx + length) && (Q3[x][0] > bx)) {
                                    direcaoX = -1;
                                    direcaoY = -1;

                                    mover();
                                    return true;
                                }
                            }
                        } else {
                            if ((esque < bx + length) && (esque > bx)) {
                                for (int x = 0; x <= 2; x++) {
                                    if ((Q4[x][0] < bx + length) && (Q4[x][0] > bx)) {
                                        direcaoX = 1;
                                        direcaoY = -1;

                                        mover();
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void mudarCC(int x, int y){
        pX = x;
        pY = y;
        actualizarF();
        actualizarQs();
    }

    public void mudarCCR(int x, int y){
        pXR = x;
        pYR = y;
    }
}
