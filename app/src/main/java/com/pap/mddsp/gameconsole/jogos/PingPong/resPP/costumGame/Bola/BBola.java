package com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola;

import android.content.Intent;
import android.os.Bundle;
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
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player.Barra_Player;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player.GameStateMBP;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp.Barra_Com;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp.GameStateMBC;
import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.desenhoJogo.Barra;
import com.pap.mddsp.gameconsole.jogos.resJogos.Bola;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class BBola extends Fragment {

    private GameViewB gameView;
    public GameStateB gamestate;
    private EditText raioT;
    private EditText velocTX;
    private EditText velocTY;
    RelativeLayout relativeLayout;
    private RelativeLayout LP;
    private boolean jaFeitoR = false;
    private boolean jaFeitoV = false;
    private int raioMax;
    private int velocMax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bola, container, false);

        gameView = new GameViewB(getActivity().getApplicationContext(), this);
        gamestate = gameView.state;

        // Spinner element
        Spinner dropdown = (Spinner)rootView.findViewById(R.id.colorSeleB);

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
                gamestate.mudarCor(t);
                if(gameView.posso){ gameView.redrawn(); }
            }

            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        LP = (RelativeLayout)rootView.findViewById(R.id.FBola);
        relativeLayout = (RelativeLayout)rootView.findViewById(R.id.Demonst);
        relativeLayout.addView(gameView);

        EditText caixa = (EditText)rootView.findViewById(R.id.TextRaio);

        raioT = caixa;

        caixa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!((s+"").equals(""))){
                    Integer nL = Integer.parseInt(s.toString());

                    if((nL <= raioMax) && (raioMax != 0) && (gameView.posso)){
                        gamestate.mudarRaio(nL);
                        gameView.redrawn();
                    }else{
                        if((raioMax != 0) && (gameView.posso)) {
                            if (jaFeitoR){
                                jaFeitoR = false;
                                return;
                            }
                            jaFeitoR = true;
                            raioT.setText(raioMax + "", TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        });

        caixa = (EditText)rootView.findViewById(R.id.TextVelocidadeX);

        velocTX = caixa;

        caixa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!((s+"").equals(""))){
                    Integer nL = Integer.parseInt(s.toString());

                    if((nL <= velocMax) && (velocMax != 0) && (gameView.posso)){
                        gamestate.mudarSpeedX(nL);
                        Log.d("Velocity", nL+"");
                        Log.d("Velocity", gamestate.speedY+"");
                    }else{
                        if((raioMax != 0) && (gameView.posso)) {
                            if (jaFeitoV){
                                jaFeitoV = false;
                                return;
                            }
                            jaFeitoV = true;
                            velocTX.setText(velocMax + "", TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        });

        caixa = (EditText)rootView.findViewById(R.id.TextVelocidadeY);

        velocTY = caixa;

        caixa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!((s+"").equals(""))){
                    Integer nL = Integer.parseInt(s.toString());

                    if((nL <= velocMax) && (velocMax != 0) && (gameView.posso)){
                        gamestate.mudarSpeedY(nL);
                        Log.d("Velocity", nL+"");
                        Log.d("Velocity", gamestate.speedY+"");
                    }else{
                        if((raioMax != 0) && (gameView.posso)) {
                            if (jaFeitoV){
                                jaFeitoV = false;
                                return;
                            }
                            jaFeitoV = true;
                            velocTY.setText(velocMax + "", TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        });

        Button butao = (Button) rootView.findViewById(R.id.JogarB);
        butao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //executar novo jogo costum
            Barra_Player player = (Barra_Player)getActivity().getSupportFragmentManager().getFragments().get(0);
            GameStateMBP playerS = player.gameState;
            Barra barra_player = new Barra( playerS.batLength, playerS.batHeight
                    , playerS.color, 0);

            Barra_Com comp = (Barra_Com)getActivity().getSupportFragmentManager().getFragments().get(2);
            GameStateMBC compS = comp.gameState;
            Barra barra_comp = new Barra( compS.batLength, compS.batHeight,
                    compS.color, compS.speed);

            Bola bola = new Bola(gamestate.raio, gamestate.speedY, gamestate.speedX,
                    gamestate.speedLimit, gamestate.color,0,0,0.3,0.1);

            Intent i = new Intent( getActivity().getApplicationContext() ,Ping_Pong_CS.class);
            i.putExtra("barra_player", barra_player);
            i.putExtra("barra_comp", barra_comp);
            i.putExtra("bola", bola);
            startActivity(i);
            getActivity().finish();
            }
        });

        String[] Colorss = new String[]{"Vermelho", "Azul", "Cizento", "Verde", "Branco"};
        dropdown.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Colorss));

        return rootView;

    }

    public void updateLimit(){
        velocMax = raioMax = LP.getWidth()/2;
    }


    @Override
    public void onPause(){
        super.onPause();
        Log.d("Bola State","onPause");
        gameView.posso = false;
    }

}
