package com.pap.mddsp.gameconsole.jogos.Quebra_Tijolos.resQT.costumGame;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MDDSP on 27-05-2015.
 */
public class arrayHelper implements Serializable {
    public int[][][] l = new int[11][10][4];

    public arrayHelper (int[][][] l2){
        l = l2;
    }
}
