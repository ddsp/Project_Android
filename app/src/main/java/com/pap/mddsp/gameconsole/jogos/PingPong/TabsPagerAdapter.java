package com.pap.mddsp.gameconsole.jogos.PingPong;

    import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_Player.Barra_Player;
    import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Barra_comp.Barra_Com;
    import com.pap.mddsp.gameconsole.jogos.PingPong.resPP.costumGame.Bola.BBola;

    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {"Barra Player", "Bola", "Barra Computador" };
    Fragment fBP, fB, fBC;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        fBP = new Barra_Player();
        fB = new BBola();
        fBC = new Barra_Com();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return fBP;
            case 1:
                // Games fragment activity
                return fB;
            case 2:
                // Movies fragment activity
                return fBC;
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
