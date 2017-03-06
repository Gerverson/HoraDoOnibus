package com.gerverson.apps.horadoonibus.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gerverson.apps.horadoonibus.DAO.Banco;
import com.gerverson.apps.horadoonibus.Model.Empresa;
import com.gerverson.apps.horadoonibus.R;

import java.io.File;

public class CadrastroEmpresa extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 001;
    private String caminhoFoto;
    ImageView campoFoto;
    EditText NomeEmpresa;
    boolean Atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadrastro_empresa);

        final Banco BD = new Banco(this);

        NomeEmpresa = (EditText) findViewById(R.id.campo_nome_empresa);
        campoFoto = (ImageView) findViewById(R.id.foto_empresa);

        final Button Cadrastrar = (Button) findViewById(R.id.btn_cadrastrar_empresa);
        Cadrastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Empresa empresa = new Empresa();
                empresa.setEmpresa(NomeEmpresa.getText().toString().toUpperCase());
                empresa.setLogo((String) campoFoto.getTag());
                if(Atualizar){
                    Intent intent = getIntent();
                    String NEmpresa = (String) intent.getSerializableExtra("NEmpresa");
                    BD.AtualizarEmpresa(empresa,NEmpresa);
                    finishActivity(002);
                }else{
                BD.InserirEmpresa(empresa);

                } finish();

            }
        });

        Atualizar = false;
        AtualizarEmpresa();
        Button Cancelar = (Button) findViewById(R.id.btn_cancelar_cadrastro_empresa);
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton BTNFoto = (ImageButton) findViewById(R.id.btn_cadrastro_foto_empresa);
        BTNFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {

                if (caminhoFoto != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    campoFoto.setImageBitmap(bitmapReduzido);
                    campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
                    campoFoto.setTag(caminhoFoto);
                }
            }
        }

    }

    public void AtualizarEmpresa(){
      String NEmpresa;
        Intent intent = getIntent();
        try {NEmpresa = (String) intent.getSerializableExtra("NEmpresa");
        } catch (Exception e) {NEmpresa = null;}

        if(!(NEmpresa == null)){
            NomeEmpresa.setText(NEmpresa);
            Banco BD = new Banco(this);
            String caminho = BD.BuscarFotoEmpresa(NomeEmpresa.getText().toString());

            Atualizar = true;
            if(caminho != null){
                Bitmap bitmap = BitmapFactory.decodeFile(caminho);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                campoFoto.setImageBitmap(bitmapReduzido);
                campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
                campoFoto.setTag(caminho);
            }
        }
    }

}
