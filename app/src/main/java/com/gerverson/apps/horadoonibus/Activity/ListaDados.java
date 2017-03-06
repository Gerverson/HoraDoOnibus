package com.gerverson.apps.horadoonibus.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerverson.apps.horadoonibus.Adapter.AdapterDados;
import com.gerverson.apps.horadoonibus.DAO.Banco;
import com.gerverson.apps.horadoonibus.Model.Dados;
import com.gerverson.apps.horadoonibus.R;

import java.util.ArrayList;

public class ListaDados extends AppCompatActivity {

    ListView ListViewDados;
    ArrayList<Dados> ArrayListDados;
    String NomeEmpresa;
    TextView TxtSemDados;
    Toolbar toolbar;
    Banco BD;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dados);

        ListViewDados = (ListView) findViewById(R.id.listview_dados);

        TxtSemDados = (TextView) findViewById(R.id.txtSemDados);
        CarregarDados();

        intent = getIntent();
        NomeEmpresa = (String) intent.getSerializableExtra("NEmpresa");

        toolbar = (Toolbar) findViewById(R.id.toolbarDados);
        toolbar.setLogo(R.drawable.ic_bus);
        toolbar.setTitle("  "+NomeEmpresa);
        setSupportActionBar(toolbar);

        BD = new Banco(this);
        String caminho = BD.BuscarFotoEmpresa(NomeEmpresa);

        ImageView campoFoto = (ImageView) findViewById(R.id.foto_dados);


        if(caminho != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminho);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminho);
        }

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab_adicianar_dados);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiPtoFormulario = new Intent(ListaDados.this, CadrastroDados.class);
                intentVaiPtoFormulario.putExtra("NEmpresa", NomeEmpresa);
                startActivity(intentVaiPtoFormulario);
            }
        });

        ListViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                final Dados dados = (Dados) ListViewDados.getItemAtPosition(pos);
                final PopupMenu popup = new PopupMenu(ListaDados.this, arg1);
                final MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_edit, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_editar:
                                Intent intentVaiPtoFormulario = new Intent(ListaDados.this, CadrastroDados.class);
                                intentVaiPtoFormulario.putExtra("IDDados", dados.getId());
                                intentVaiPtoFormulario.putExtra("NEmpresa", NomeEmpresa);
                                startActivity(intentVaiPtoFormulario);
                                return true;
                            case R.id.action_remover:
                                BD.DeletarDado(dados.getId());
                                CarregarDados();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        CarregarDados();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = getIntent();
        final Banco BD = new Banco(ListaDados.this);
        NomeEmpresa = (String) intent.getSerializableExtra("NEmpresa");
        if (id == R.id.action_editar){
            Intent intentVaiPtoFormulario = new Intent(ListaDados.this, CadrastroEmpresa.class);
            intentVaiPtoFormulario.putExtra("NEmpresa", NomeEmpresa);
            startActivityForResult(intentVaiPtoFormulario,002);
        }
        if (id == R.id.action_remover) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remover Empresa");
            builder.setMessage("Aviso: Ao deletar a empresa todos os dados serão apagados.");
            builder.setPositiveButton("Deleta", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    BD.DeletarEmpresa(NomeEmpresa);
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void CarregarDados(){
        Banco BD = new Banco(this);
        Intent intent = getIntent();
        NomeEmpresa = (String) intent.getSerializableExtra("NEmpresa");
        ArrayListDados = BD.BuscarDados(NomeEmpresa);
        if(ArrayListDados.size() > 0){
            AdapterDados adapterDados = new AdapterDados(this, ArrayListDados, false);
            ListViewDados.setAdapter(adapterDados); TxtSemDados.setText("");
        }else {
            AdapterDados adapterDados = new AdapterDados(this, ArrayListDados, false);
            ListViewDados.setAdapter(adapterDados);
            TxtSemDados.setText("Não há dados de rota de viagem cadrastrados\nClique em + para cadrastrar.");
        }
    }
}