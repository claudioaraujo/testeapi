package br.com.moduloteste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.moduloteste.controller.UserController;

public class OptionFragment extends Fragment {

    private View rootView;
    private TextView txtFrag;
    public static final String OPTION = "option";
    public static final String TOKEN = "token";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initViews(inflater, container);

        onConfigFrag(inflater, container);
        return rootView;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container){

        //rootView = inflater.inflate(R.layout.my_space_fragment, container, false);
        //txtFrag = (TextView) rootView.findViewById(R.id.option_fragment_txt_frag);
    }

    private void onConfigFrag(LayoutInflater inflater, ViewGroup container){
        int i = getArguments().getInt(OPTION); //Recupera o parametro passado como argumento para essa classe
        String tokenValue = getArguments().getString(TOKEN);

        //Muda o texto de exibicao de acordo com o parametro passado.
        switch (i) {
            case 0:
                UserController userController = new UserController();
                userController.getInfo(tokenValue);
                rootView = inflater.inflate(R.layout.my_space_fragment, container, false);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.organization_fragment, container, false);
                break;
            case 2:

                break;
            case 3:

                break;
            default:
                txtFrag.setText(getResources().getString(R.string.option_frag_txt01));
        }

    }

}
