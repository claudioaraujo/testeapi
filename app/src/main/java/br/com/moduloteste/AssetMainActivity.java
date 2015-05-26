package br.com.moduloteste;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import br.com.moduloteste.objects.Perimeter;

public class AssetMainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private PerimterAdapter adapterListView;
    private ArrayList<Perimeter> itens;
    public final static String TOKEN = "TOKEN";
    public final static String PERIMETER = "PERIMETER";
    private static String token;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //carrega o layout onde contem o ListView

        token = this.getIntent().getExtras().getString("TOKEN");

        setContentView(R.layout.activity_asset_main);

        //Pega a referencia do ListView
        listView = (ListView) findViewById(R.id.list);
        //Define o Listener quando alguem clicar no item.
        listView.setOnItemClickListener(this);

        createListView();
    }

    private void createListView()
    {
        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<Perimeter>();
        Perimeter item1 = new Perimeter();
        item1.setIcon(R.drawable.ico_perimeter);
        item1.setName("API");
        Perimeter item2 = new Perimeter();
        item2.setIcon(R.drawable.ico_perimeter);
        item2.setName("API > Desktop");
        Perimeter item3 = new Perimeter();
        item3.setIcon(R.drawable.ico_perimeter);
        item3.setName("API > Fonte");
        Perimeter item4 = new Perimeter();
        item4.setIcon(R.drawable.ico_perimeter);
        item4.setName("API > Notebook");

        itens.add(item1);
        itens.add(item2);
        itens.add(item3);
        itens.add(item4);

        //Cria o adapter
        adapterListView = new PerimterAdapter(this, itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        //Cor quando a lista e selecionada para ralagem.
        //listView.setCacheColorHint(Color.TRANSPARENT);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        //Pega o item que foi selecionado.
        Perimeter item = adapterListView.getItem(arg2);
        //Demostracao
        TextView selectedPerimeter = (TextView) findViewById(R.id.selected_perimeter_info);
        selectedPerimeter.setText(item.getName());
    }

    public void showNewAsset(View view) {
        TextView selectedPerimeter = (TextView) findViewById(R.id.selected_perimeter_info);
        final Intent intent = new Intent(this, NewAssetActivity.class);
        intent.putExtra(TOKEN, token);
        intent.putExtra(PERIMETER, selectedPerimeter.getText());
        startActivity(intent);
    }
}