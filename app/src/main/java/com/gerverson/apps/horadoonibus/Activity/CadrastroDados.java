package com.gerverson.apps.horadoonibus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gerverson.apps.horadoonibus.DAO.Banco;
import com.gerverson.apps.horadoonibus.Model.Dados;
import com.gerverson.apps.horadoonibus.R;

import java.util.ArrayList;
import java.util.List;

public class CadrastroDados extends AppCompatActivity {

    Spinner minutos;
    Spinner horas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadrastro_dados);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);

        Intent intent = getIntent();
        final String NomeEmpresa = (String) intent.getSerializableExtra("NEmpresa");


        final Banco BF = new Banco(this);

        horas = (Spinner) findViewById(R.id.spinner_hora);
        minutos = (Spinner) findViewById(R.id.spinner_minuto);



        Button cancelar = (Button) findViewById(R.id.btn_cancelar_cadrastar_dados);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        final EditText origem = (EditText) findViewById(R.id.campo_cadrastrar_origem);
        final EditText destino = (EditText) findViewById(R.id.campo_cadrastrar_destino);
        final EditText valor = (EditText) findViewById(R.id.campo_cadrastrar_valor);

        PreecherSpinner();
        Button cadrastrar = (Button) findViewById(R.id.btn_cadrastrar_dados);
        cadrastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dados dados = new Dados();
                dados.setHorario((String) horas.getSelectedItem() + ":"+ (String) minutos.getSelectedItem());
                dados.setOrigem(String.valueOf(origem.getText()));
                dados.setDestino(String.valueOf(destino.getText()));
                dados.setValor(Double.valueOf(valor.getText().toString()));
                dados.setEmpresa(NomeEmpresa);
                BF.InserirDados(dados);
                finish();
            }
        });
}

    public void PreecherSpinner(){
        List<String> hora = new ArrayList<>();
        hora.add("00"); hora.add("01"); hora.add("02"); hora.add("03"); hora.add("04"); hora.add("05");
        hora.add("06"); hora.add("07"); hora.add("08"); hora.add("09"); hora.add("10"); hora.add("11");
        hora.add("12"); hora.add("13"); hora.add("14"); hora.add("15"); hora.add("16"); hora.add("17");
        hora.add("18"); hora.add("19"); hora.add("20"); hora.add("21"); hora.add("22"); hora.add("23");

        List<String> minuto = new ArrayList<>();
        minuto.add("00"); minuto.add("05"); minuto.add("10"); minuto.add("15"); minuto.add("20"); minuto.add("25");
        minuto.add("30"); minuto.add("35"); minuto.add("40"); minuto.add("45"); minuto.add("50"); minuto.add("55");

        ArrayAdapter<String> adaptador;

        adaptador = new ArrayAdapter<String>(CadrastroDados.this, android.R.layout.simple_spinner_item, hora);
        horas.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(CadrastroDados.this, android.R.layout.simple_spinner_item, minuto);
        minutos.setAdapter(adaptador);
    }

}