package br.com.moduloteste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.moduloteste.objects.Perimeter;

/**
 * Created by claudio.araujo on 25/05/2015.
 */
public class PerimterAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<Perimeter> itens;

    public PerimterAdapter(Context context, ArrayList<Perimeter> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public Perimeter getItem(int position)
    {
        return itens.get(position);
    }

    /**
     * Sem implementacao
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        //Pega o item de acordo com a posicao.
        Perimeter item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.perimeter_row, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informacoes.
        ((TextView) view.findViewById(R.id.text)).setText(item.getName());
        ((ImageView) view.findViewById(R.id.imagemview)).setImageResource(item.getIcon());

        return view;
    }
}