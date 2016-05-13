package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.desenhoJogo;

import java.io.Serializable;

public class Barra implements Serializable {

    public int largura;
    public int altura;
    public int speed;
    public String cor;

    public Barra(int larguraa,int alturaa,String corr, int speedd){
        largura = larguraa;
        altura = alturaa;
        cor = corr;
        speed = speedd;
    }
}
