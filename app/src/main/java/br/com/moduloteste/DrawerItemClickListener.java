package br.com.moduloteste;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DrawerItemClickListener implements OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private Fragment currentFragment;
    public static final String OPTION = "option";
    private Activity activity;

    public DrawerItemClickListener(DrawerLayout drawerLayout, ListView drawerList,Activity activity){

        this.drawerLayout = drawerLayout;
        this.drawerList = drawerList;
        this.activity = activity;

    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        selectItem(position);

    }

    private void selectItem(int position){

        currentFragment = new OptionFragment();
        //Seta como argumento a posicao do item do menu escolhido
        Bundle args = new Bundle();
        args.putInt(OPTION, position);
        currentFragment.setArguments(args);
        ((MainActivity)activity).replaceFragment(currentFragment);
        drawerLayout.closeDrawer(drawerList); //Fecha o menu

    }

}
