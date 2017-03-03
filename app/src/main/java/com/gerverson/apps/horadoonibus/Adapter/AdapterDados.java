package com.gerverson.apps.horadoonibus.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerverson.apps.horadoonibus.Model.Dados;
import com.gerverson.apps.horadoonibus.Model.Empresa;
import com.gerverson.apps.horadoonibus.R;

import java.util.ArrayList;

public class AdapterDados extends BaseAdapter {

    private Context context;
    private ArrayList<Dados> dado;
    private boolean tipo;

    public AdapterDados(Context contx, ArrayList<Dados> dados,boolean tipo){
        this.dado = dados;
        this.context = contx;
        this.tipo = tipo;
    }


    @Override
    public int getCount() {
        return dado.size();
    }

    @Override
    public Object getItem(int i) {
        return dado.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_dados, null);
        if(view != null){
            TextView origem_destino = (TextView) view.findViewById(R.id.v_txt_origrm_destino);
            TextView valor = (TextView) view.findViewById(R.id.v_txt_valor);
            TextView hora = (TextView) view.findViewById(R.id.v_txt_hora);
            TextView empresa = (TextView) view.findViewById(R.id.v_txt_empresa);

            Dados DADOS = dado.get(i);
            origem_destino.setText(DADOS.getOrigem() + " -> " + DADOS.getDestino());
            valor.setText(DADOS.getValor() != null ? "R$ "+String.format("%.2f", DADOS.getValor()) : null);
            hora.setText(DADOS.getHorario());
            empresa.setText(tipo ? DADOS.getEmpresa(): null);
        }
        return view;
    }
}
