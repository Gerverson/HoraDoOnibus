package com.gerverson.apps.horadoonibus.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gerverson.apps.horadoonibus.Model.Dados;
import com.gerverson.apps.horadoonibus.Model.Empresa;

import java.util.ArrayList;
import java.util.List;

public class Banco extends SQLiteOpenHelper {
    private static final String NOME_BD = "Registros";
    private static final int VERSAO_BD = 2;


    public Banco(Context ctx){
        super(ctx, NOME_BD, null, VERSAO_BD);
    }


    SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table empresa(nome text primary key not null," +
                "foto text);");

        bd.execSQL("create table dados(id integer primary key autoincrement, " +
                "empresa text not null, " +
                "origem text not null, " +
                "destino text not null, " +
                "horas text not null, " +
                "valor text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
        bd.execSQL("drop table empresa");
        bd.execSQL("drop table dados");
        onCreate(bd);
    }


    public ArrayList<String> BuscarEmpresas(){
        db = getWritableDatabase();
        String sql = "SELECT * FROM empresa ORDER BY nome;";
        Cursor c = db.rawQuery(sql, null);

        ArrayList<String> empresa = new ArrayList<>();
        while (c.moveToNext()) {
            empresa.add(c.getString(c.getColumnIndex("nome")));
        }
        c.close();
        return empresa;
    }

    public void InserirEmpresa(Empresa empresa) {
        ContentValues valores = new ContentValues();
        db = getWritableDatabase();
        try {
            valores.put("nome", empresa.getEmpresa());
            valores.put("foto", empresa.getLogo());
            db.insert("empresa", null, valores);
        } catch (SQLiteAbortException e) {
        } catch (SQLException e) {
        } catch (Exception e) {
        }
    }

    public String BuscarFotoEmpresa(String Empresa){
        db = getWritableDatabase();
        String sql = "SELECT foto FROM empresa WHERE nome = '"+Empresa+"';";
        Cursor c = db.rawQuery(sql, null);
        String caminho = null;
        while (c.moveToNext()) {
            caminho = c.getString(c.getColumnIndex("foto"));
        }
        c.close();
        return caminho;
    }

    public void InserirDados(Dados dados) {
        ContentValues valores = new ContentValues();
        db = getWritableDatabase();
        try {
            valores.put("origem", dados.getOrigem());
            valores.put("destino", dados.getDestino());
            valores.put("horas", dados.getHorario());
            valores.put("valor", dados.getValor());
            valores.put("empresa", dados.getEmpresa());
            db.insert("dados", null, valores);
        } catch (SQLiteAbortException e) {
        } catch (SQLException e) {
        } catch (Exception e) {
        }
    }



    public ArrayList<Dados> BuscarDados(String Empresa){
        db = getWritableDatabase();
        ArrayList<Dados> dados = new ArrayList<>();
        try{
      //  String sql = "SELECT * FROM dados;";
            String sql = "SELECT * FROM dados WHERE empresa = '"+Empresa+"'";

        Cursor c = db.rawQuery(sql, null);


        while (c.moveToNext()) {
            Dados d = new Dados();
            d.setEmpresa(c.getString(c.getColumnIndex("empresa")));
            d.setOrigem(c.getString(c.getColumnIndex("origem")));
            d.setDestino(c.getString(c.getColumnIndex("destino")));
            d.setHorario(c.getString(c.getColumnIndex("horas")));
            d.setValor(c.getDouble(c.getColumnIndex("valor")));
            d.setId(c.getInt(c.getColumnIndex("id")));
            dados.add(d);
        }
        c.close();
    } catch (SQLiteAbortException e) {
    } catch (SQLException e) {
    } catch (Exception e) {
    }
        return dados;
    }

    public ArrayList<Dados> BuscarDadosoi(){
        db = getWritableDatabase();
        ArrayList<Dados> dados = new ArrayList<>(); int con = 0;
        try{
            String sql = "SELECT * FROM dados;";
            Cursor c = db.rawQuery(sql, null);

            while (c.moveToNext()) {
                Dados d = new Dados();
                d.setOrigem("oi"+con);   con++;
                d.setDestino("xh"+con);
                dados.add(d);
            }
            c.close();
        } catch (SQLiteAbortException e) {
            Dados d = new Dados();
            d.setDestino("1"+e);
            dados.add(d);
        } catch (SQLException e) {
            Dados d = new Dados();
            d.setDestino("2"+e);
            dados.add(d);
        } catch (Exception e) {
            Dados d = new Dados();
            d.setDestino("3"+e);
            dados.add(d);
        }
        return dados;
    }

    public void LimparTudo(){
        db = getWritableDatabase();
        db.execSQL("delete from empresa");
        db.execSQL("delete from dados");
    }

    public void AtualizarEmpresa(Empresa empresa, String nome){
        db = getWritableDatabase();
        ContentValues valores = new ContentValues();
            valores.put("nome", empresa.getEmpresa());
            valores.put("foto", empresa.getLogo());
        String[] params = {nome};
        db.update("empresa", valores, "nome = ?", params);
        valores = new ContentValues();
        valores.put("empresa", empresa.getEmpresa());
        db.update("dados", valores, "empresa = ?", params);
    }

    public void DeletarEmpresa(String empresa){
        db = getWritableDatabase();
        db.delete("empresa", "nome = '" +empresa+"'", null);
        db.delete("dados", "empresa = '" +empresa+"'", null);
    }

    public void DeletarDado(int id){
        db = getWritableDatabase();
        db.delete("dados", "id = " +id+"", null);
    }

    public List<String> SpinnerBuscaOrigem(){
        db = getWritableDatabase();
        String sql = "SELECT DISTINCT origem FROM dados;";
        Cursor c = db.rawQuery(sql, null);
        List<String> origens = new ArrayList<>();
        while (c.moveToNext()) {
            origens.add(c.getString(c.getColumnIndex("origem")));
        }
        c.close();
        return origens;
    }
    public List<String> SpinnerBuscaDestino(String Origem){
        db = getWritableDatabase();
        String sql = "SELECT DISTINCT destino FROM dados where origem = '"+Origem+"';";
        Cursor c = db.rawQuery(sql, null);
        List<String> origens = new ArrayList<>();
        while (c.moveToNext()) {
            origens.add(c.getString(c.getColumnIndex("destino")));
        }
        c.close();
        return origens;
    }

    public List<String> SpinnerBuscaEmpresa(String Origem, String Destino){
        db = getWritableDatabase();
        String sql = "SELECT DISTINCT empresa FROM dados where origem = '"+Origem+"' and destino ='"+Destino+"';";
        Cursor c = db.rawQuery(sql, null);
        List<String> origens = new ArrayList<>();
        while (c.moveToNext()) {
            origens.add(c.getString(c.getColumnIndex("empresa")));
        }
        c.close();
        return origens;
    }
    public ArrayList<Dados> BuscarFiltrado(String Origem, String Destino){
        db = getWritableDatabase();
        String sql = "SELECT * FROM dados where origem = '"+Origem+"' and destino ='"+Destino+"';";
        Cursor c = db.rawQuery(sql, null);

        ArrayList<Dados> dados = new ArrayList<>();
        while (c.moveToNext()) {
            Dados d = new Dados();
            d.setEmpresa(c.getString(c.getColumnIndex("empresa")));
            d.setOrigem(c.getString(c.getColumnIndex("origem")));
            d.setDestino(c.getString(c.getColumnIndex("destino")));
            d.setHorario(c.getString(c.getColumnIndex("horas")));
            d.setValor(c.getDouble(c.getColumnIndex("valor")));
            d.setId(c.getInt(c.getColumnIndex("id")));
            dados.add(d);
        }
        c.close();
        return dados;
    }

    public ArrayList<Dados> BuscarFiltradoEmpresa(String Origem, String Destino, String Empresa){
        db = getWritableDatabase();
        String sql = "SELECT * FROM dados where origem = '"+Origem+"' and destino ='"+Destino+"' and empresa = '"+Empresa+"';";
        Cursor c = db.rawQuery(sql, null);

        ArrayList<Dados> dados = new ArrayList<>();
        while (c.moveToNext()) {
            Dados d = new Dados();
            d.setEmpresa(c.getString(c.getColumnIndex("empresa")));
            d.setOrigem(c.getString(c.getColumnIndex("origem")));
            d.setDestino(c.getString(c.getColumnIndex("destino")));
            d.setHorario(c.getString(c.getColumnIndex("horas")));
            d.setValor(c.getDouble(c.getColumnIndex("valor")));
            d.setId(c.getInt(c.getColumnIndex("id")));
            dados.add(d);
        }
        c.close();
        return dados;
    }
}
