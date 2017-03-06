package com.gerverson.apps.horadoonibus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gerverson.apps.horadoonibus.DAO.Banco;
import com.gerverson.apps.horadoonibus.R;

import java.util.ArrayList;

public class ListaEmpresas extends AppCompatActivity {


    TextView TxtSemEmpresa;
    Toolbar toolbar;
    FloatingActionButton CadrasEmpr;
    ArrayList<String> ListaEmpresas;
    ArrayAdapter<String> adaptador;
    ListView EmpresasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empresa);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_bus);
        setSupportActionBar(toolbar);

        TxtSemEmpresa = (TextView) findViewById(R.id.txtSemEmpresa);
        EmpresasList = (ListView) findViewById(R.id.listview_empresa);
        ListarEmpresas(false);


        FloatingActionButton consulta = (FloatingActionButton) findViewById(R.id.fab_consulta);
        consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaEmpresas.this, Consulta.class));
            }
        });

        CadrasEmpr = (FloatingActionButton) findViewById(R.id.fab_adicianar_dados);
        CadrasEmpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaEmpresas.this, CadrastroEmpresa.class));
            }
        });

        EmpresasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                String NEmpresa = (String) EmpresasList.getItemAtPosition(position);
                Intent intentVaiPtoFormulario = new Intent(ListaEmpresas.this, ListaDados.class);
                intentVaiPtoFormulario.putExtra("NEmpresa", NEmpresa);
                startActivity(intentVaiPtoFormulario);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarEmpresas(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sobre) {
            AlertDialog.Builder builder = new AlertDialog.Builder(com.gerverson.apps.horadoonibus.Activity.ListaEmpresas.this);
            builder.setMessage("Aplicativo desenvolvidos por alunos do IFTO - Campus Paraíso");
            builder.show();
            return true;
        }
        if (id == R.id.action_limpar) {
            Banco BF = new Banco(this);
            BF.LimparTudo();
            ListarEmpresas(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void ListarEmpresas(Boolean apagar) {
        final Banco BF = new Banco(this);
        ListaEmpresas = BF.BuscarEmpresas();

        if (ListaEmpresas.size() > 0 || apagar) {
            adaptador = new ArrayAdapter<String>(ListaEmpresas.this, android.R.layout.simple_list_item_1, ListaEmpresas);
            EmpresasList.setAdapter(adaptador);
            registerForContextMenu(EmpresasList);
            TxtSemEmpresa.setText("");
            if (apagar) {
                TxtSemEmpresa.setText("Não há dados cadrastrados\nClique em + para adicionar");
            }
        } else {
            TxtSemEmpresa.setText("Não há dados cadrastrados\nClique em + para adicionar");
        }
    }
}
