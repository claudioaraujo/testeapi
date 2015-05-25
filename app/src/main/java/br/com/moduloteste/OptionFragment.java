package br.com.moduloteste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
                //buscando no strings.xml a url que vai ser utilizada para consulta dos dados.
                int infoUrlId = R.string.info_url;
                int infoUrlProfileId = R.string.info_profile_url;
                String infoUrl =  (String) this.getResources().getText(infoUrlId);
                String infoProfileUrl =  (String) this.getResources().getText(infoUrlProfileId);

                User user = null;

                //consultando as informacoes do usuario
                UserController userController = new UserController();
                user = userController.getInfo(infoUrl, tokenValue);
                userController.getInfoProfiles(infoProfileUrl, tokenValue, user);

                //setando o my_space como layout a ser exibido
                rootView = inflater.inflate(R.layout.my_space_fragment, container, false);

                //populando os dados pessoais
                TextView login = (TextView) rootView.findViewById(R.id.login_info);
                TextView email = (TextView) rootView.findViewById(R.id.email_info);
                TextView nome = (TextView) rootView.findViewById(R.id.nome_info);
                TextView perfil = (TextView) rootView.findViewById(R.id.grupos_de_acesso_info);

                login.setText(user.getLogin());
                email.setText(user.getEmail());
                nome.setText(user.getName());

                //populando o perfil
                for (int p = 0, tam = user.getMember_of().size(); p < tam; p++){
                    perfil.setText(perfil.getText() + "- " + user.getMember_of().get(p) + "\n");
                }

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
