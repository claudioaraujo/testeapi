package br.com.moduloteste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import br.com.moduloteste.controller.UserController;
import br.com.moduloteste.objects.User;

public class OptionFragment extends Fragment {

    private View rootView;
    private TextView txtFrag;
    public static final String OPTION = "option";
    public static final String TOKEN = "token";
    public static String tokenValue = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            onConfigFrag(inflater, container);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void onConfigFrag(LayoutInflater inflater, ViewGroup container) throws JSONException {
        int i = getArguments().getInt(OPTION); //Recupera o parametro passado como argumento para essa classe
        String tokenValue = getActivity().getIntent().getExtras().getString(LoginActivity.TOKEN);

        //Muda o texto de exibicao de acordo com o parametro passado.
        switch (i) {
            case 0:
                int infoUrlId = R.string.info_url;
                String infoUrl =  (String) this.getResources().getText(infoUrlId);
                User user = new User();

                UserController userController = new UserController();
                user = userController.getInfo(infoUrl, tokenValue);

                rootView = inflater.inflate(R.layout.my_space_fragment, container, false);

                TextView login = (TextView) rootView.findViewById(R.id.login_info);
                TextView email = (TextView) rootView.findViewById(R.id.email_info);
                TextView nome = (TextView) rootView.findViewById(R.id.nome_info);

                login.setText(user.getLogin());
                email.setText(user.getEmail());
                nome.setText(user.getName());
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
