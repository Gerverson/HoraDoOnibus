package com.gerverson.apps.horadoonibus.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.gerverson.apps.horadoonibus.Adapter.AdapterDados;
import com.gerverson.apps.horadoonibus.DAO.Banco;
import com.gerverson.apps.horadoonibus.Model.Dados;
import com.gerverson.apps.horadoonibus.R;

import java.util.ArrayList;
import java.util.List;

public class Consulta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_consulta);

        toolbar.setLogo(R.drawable.ic_bus);
        toolbar.setTitle("Consultar Rotas");


        final String[] ORIGEM = new String[1];
        final String[] DESTINO = new String[1];

        final Banco BD = new Banco(this);

        Spinner origem = (Spinner) findViewById(R.id.spi_origem);
        final Spinner destino = (Spinner) findViewById(R.id.spi_destino);
        final Spinner empresa = (Spinner) findViewById(R.id.spi_empresa);

        final ListView ListConsulta = (ListView) findViewById(R.id.list_consulta);

        List<String> ListDados = new ArrayList<>();

        ListDados = BD.SpinnerBuscaOrigem();
        ListDados.add(0, "Selecione a Origem...");


        ArrayAdapter<String> adaptador;
        adaptador = new ArrayAdapter<String>(Consulta.this, android.R.layout.simple_list_item_1, ListDados);
        origem.setAdapter(adaptador);

        origem.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        ORIGEM[0] = item.toString();
                        if (!(ORIGEM[0].equals("Selecione a Origem..."))) {
                            List<String> ListDados = new ArrayList<>();
                            ListDados = BD.SpinnerBuscaDestino(ORIGEM[0]);
                            ListDados.add(0, "Selecione o Destino...");
                            ArrayAdapter<String> adaptador;
                            adaptador = new ArrayAdapter<String>(Consulta.this, android.R.layout.simple_list_item_1, ListDados);
                            destino.setAdapter(adaptador);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        destino.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        DESTINO[0] = item.toString();
                        if (!(DESTINO[0].equals("Selecione o Destino..."))) {
                            List<String> ListDados = new ArrayList<>();

                            ListDados = BD.SpinnerBuscaEmpresa(ORIGEM[0], DESTINO[0]);
                            ListDados.add(0, "Selecione a Empresa...");

                            ArrayAdapter<String> adaptador;
                            adaptador = new ArrayAdapter<String>(Consulta.this, android.R.layout.simple_list_item_1, ListDados);


                            empresa.setAdapter(adaptador);

                            ArrayList<Dados> ListaDados = new ArrayList<Dados>();
                            ListaDados = BD.BuscarFiltrado(ORIGEM[0], DESTINO[0]);

                            AdapterDados adapterDados = new AdapterDados(Consulta.this, ListaDados, true);

                            ListConsulta.setAdapter(adapterDados);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        empresa.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        String EMPRESA = item.toString();
                        if (!(EMPRESA.equals("Selecione a Empresa..."))) {

                            ArrayList<Dados> ListaDados = new ArrayList<Dados>();

                            ListaDados = BD.BuscarFiltradoEmpresa(ORIGEM[0], DESTINO[0], EMPRESA);

                            AdapterDados adapterDados = new AdapterDados(Consulta.this, ListaDados, false);

                            ListConsulta.setAdapter(adapterDados);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }
}
