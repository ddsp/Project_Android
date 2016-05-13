package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player;

import android.content.Intent;
import android.os.Bundle;
import android.R.layout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pap.mddsp.gameconsole.R;
import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong_CS;
import com.pap.mddsp.gameconsole.jogos.PingPong.Ping_Pong_Cos;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp.Barra_Com;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp.GameStateMBC;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola.BBola;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola.GameStateB;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.desenhoJogo.Barra;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

/**
 * A simple {@link Fragment} subclass.
 */
public class Barra_Player extends Fragment {

    RelativeLayout relativeLayout;
    private RelativeLayout LP;
    private GameViewMBP gameView;
    public GameStateMBP gameState;
    private int alturaMax = 0;
    private int larguraMax = 0;
    private EditText alturaT;
    private Boolean jaFeitoAT = false;
    private EditText larguraT;
    private Boolean jaFeitoLT = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_barra__player, container, false);

        gameView = new GameViewMBP(getActivity().getApplicationContext(), this);
        relativeLayout = (RelativeLayout)rootView.findViewById(R.id.Demonst);
        LP = (RelativeLayout)rootView.findViewById(R.id.barra_p_L);
        relativeLayout.addView(gameView);

        gameState = gameView.state;

        // Spinner element
        Spinner dropdown = (Spinner)rootView.findViewById(R.id.colorSeleBP);

        // Spinner click listener
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // On selecting a spinner item
               String item = parent.getItemAtPosition(position).toString();

               String t = "";
               if(item.equals("Vermelho")){
                   t = "Red";
               }
               if(item.equals("Azul")){
                   t = "Blue";
               }
               if(item.equals("Cizento")){
                   t = "Gray";
               }
               if(item.equals("Verde")){
                   t = "Green";
               }
               if(item.equals("Branco")){
                   t = "White";
               }
               gameState.mudarCor(t);
               if(gameView.posso){ gameView.redrawn(); }
           }

           public void onNothingSelected(AdapterView<?> parent) { }
       });

        EditText caixa = (EditText)rootView.findViewById(R.id.TextLarguraBP);

        larguraT = caixa;

        caixa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!((s+"").equals(""))){
                    Integer nL = Integer.parseInt(s.toString());
                    if((nL <= larguraMax) && (larguraMax != 0) && (gameView.posso)) {
                        gameState.mudarLargura(nL);
                        gameView.redrawn();
                    }else{
                        if((larguraMax != 0) && (gameView.posso)) {
                            if (jaFeitoLT){
                                Log.d("N", "cheguei2");
                                jaFeitoLT = false;
                                return;
                            }
                            Log.d("N", "cheguei");
                            jaFeitoLT = true;
                            larguraT.setText(larguraMax + "", TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        alturaT = caixa = (EditText)rootView.findViewById(R.id.TextAlturaBP);

        caixa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!((s+"").equals(""))){
                    Integer nL = Integer.parseInt(s.toString());
                    if((nL <= alturaMax) && (alturaMax != 0) && (gameView.posso)){
                        gameState.mudarAltura(nL);
                        gameView.redrawn();
                    }else{
                        if((alturaMax != 0) && (gameView.posso)) {
                            if (jaFeitoAT){
                            jaFeitoAT = false;
                            return;
                            }
                            jaFeitoAT = true;
                            alturaT.setText(alturaMax + "", TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        });

        Log.d("Teste", getActivity().getSupportFragmentManager().getFragments().size() + "");

        Button butao = (Button) rootView.findViewById(R.id.JogarBP);
        butao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //executar novo jogo costum

                Barra barra_player = new Barra( gameState.batLength, gameState.batHeight
                        , gameState.color, 0);

                Barra_Com comp = (Barra_Com)((Ping_Pong_Cos)getActivity()).mAdapter.getItem(2);
                GameStateMBC compS = comp.gameState;
                Barra barra_comp;
                if(compS != null) {
                    barra_comp = new Barra(compS.batLength, compS.batHeight,
                            compS.color, compS.speed);
                }else{
                    barra_comp = new Barra(75, 40,
                            "Red", 20);
                }

                BBola bola = (BBola) getActivity().getSupportFragmentManager().getFragments().get(1);
                Log.d("b", bola.gamestate.color);
                GameStateB bolaS = bola.gamestate;
                Bola bola_obj = new Bola(bolaS.raio, bolaS.speedY, bolaS.speedX,
                        bolaS.speedLimit, bolaS.color,0,0,0.3,0.1);

                Intent i = new Intent(getActivity().getApplicationContext(), Ping_Pong_CS.class);
                i.putExtra("barra_player", barra_player);
                i.putExtra("barra_comp", barra_comp);
                i.putExtra("bola", bola_obj);
                startActivity(i);
                getActivity().finish();
            }
        });

        String[] Colorss = new String[]{"Vermelho", "Azul", "Cizento", "Verde", "Branco"};
        dropdown.setAdapter(new ArrayAdapter<String>(getActivity(), layout.simple_spinner_item, Colorss));

        return rootView;
    }

    public void updateLimits(){
        alturaMax = LP.getHeight()/2;
        larguraMax = LP.getWidth();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Barra_Player State","onPause");
        gameView.posso = false;
    }
}
