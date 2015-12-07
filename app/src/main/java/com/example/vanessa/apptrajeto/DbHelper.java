package com.example.vanessa.apptrajeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper{

    private static final String NOME_BASE = "BancoTrajetos";
    private static final int VERSAO_BASE = 1;
    private static final String TABELA = "Trajeto";

    public DbHelper(Context context) {
        super(context, NOME_BASE, null, VERSAO_BASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCreateTabelaLivro = "CREATE TABLE "+TABELA+"("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT,"
                + "largura int,"
                + "area double,"
                + "ponto0Latitude TEXT,"
                + "ponto1Longitude TEXT,"
                + "ponto2Latitude TEXT,"
                + "ponto3Longitude TEXT,"
                + "ponto4Latitude TEXT,"
                + "ponto5Longitude TEXT,"
                + "ponto6Latitude TEXT,"
                + "ponto7Longitude TEXT,"
                + "ponto8Latitude TEXT,"
                + "ponto9Longitude TEXT,"
                + "ponto10Latitude TEXT,"
                + "ponto11Longitude TEXT"
                + ")";
        db.execSQL(sqlCreateTabelaLivro);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlDropTrajetos = "DROP TABLE "+TABELA+"";
        db.execSQL(sqlDropTrajetos);

        onCreate(db);
    }

    public void deleteDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        String sqlDropTrajetos = "DROP TABLE "+TABELA+"";
        db.execSQL(sqlDropTrajetos);

        onCreate(db);
    }


    public void insert(Trajeto trajeto){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("nome", trajeto.getNome());
        cv.put("largura", trajeto.getLargura());
        cv.put("area", trajeto.getArea());
        cv.put("ponto0Latitude", trajeto.getPonto0Latitude());
        cv.put("ponto1Longitude", trajeto.getPonto1Longitude());
        cv.put("ponto2Latitude", trajeto.getPonto2Latitude());
        cv.put("ponto3Longitude", trajeto.getPonto3Longitude());
        cv.put("ponto4Latitude", trajeto.getPonto4Latitude());
        cv.put("ponto5Longitude", trajeto.getPonto5Longitude());
        cv.put("ponto6Latitude", trajeto.getPonto6Latitude());
        cv.put("ponto7Longitude", trajeto.getPonto7Longitude());
        cv.put("ponto8Latitude", trajeto.getPonto8Latitude());
        cv.put("ponto9Longitude", trajeto.getPonto9Longitude());
        cv.put("ponto10Latitude", trajeto.getPonto10Latitude());
        cv.put("ponto11Longitude", trajeto.getPonto11Longitude());

        db.insert(TABELA, null, cv);
        db.close();
    }


    public List<Trajeto> selectAll(){

        List<Trajeto> listTrajetos = new ArrayList<Trajeto>();
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodosLivros = "SELECT * FROM "+TABELA+"";
        Cursor c = db.rawQuery(sqlSelectTodosLivros, null);

        if(c.moveToFirst()){
            do{
                Trajeto trajeto = new Trajeto();
                trajeto.setId(c.getInt(0));
                trajeto.setNome(c.getString(1));
                trajeto.setLargura(c.getInt(2));
                trajeto.setArea(c.getDouble(3));
                trajeto.setPonto0Latitude(c.getDouble(4));
                trajeto.setPonto1Longitude(c.getDouble(5));
                trajeto.setPonto2Latitude(c.getDouble(6));
                trajeto.setPonto3Longitude(c.getDouble(7));
                trajeto.setPonto4Latitude(c.getDouble(8));
                trajeto.setPonto5Longitude(c.getDouble(9));
                trajeto.setPonto6Latitude(c.getDouble(10));
                trajeto.setPonto7Longitude(c.getDouble(11));
                trajeto.setPonto8Latitude(c.getDouble(12));
                trajeto.setPonto9Longitude(c.getDouble(13));
                trajeto.setPonto10Latitude(c.getDouble(14));
                trajeto.setPonto11Longitude(c.getDouble(15));

                listTrajetos.add(trajeto);
            }while(c.moveToNext());
        }

        db.close();
        return listTrajetos;
    }

    public List<Trajeto> selectAllNames(){

        List<Trajeto> listTrajetos = new ArrayList<Trajeto>();
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTraj = "SELECT id, nome FROM "+TABELA+"";
        Cursor c = db.rawQuery(sqlSelectTraj, null);

        if(c.moveToFirst()){
            do{
                Trajeto trajeto = new Trajeto();
                trajeto.setId(c.getInt(0));
                trajeto.setNome(c.getString(1));
                listTrajetos.add(trajeto);
            }while(c.moveToNext());
        }

        db.close();
        return listTrajetos;
    }

    public Trajeto selectById(int id){

        Trajeto trajeto = new Trajeto();
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodosLivros = "SELECT * FROM "+TABELA+" WHERE id = "+id;
        Cursor c = db.rawQuery(sqlSelectTodosLivros, null);

        if(c.moveToFirst()){
            do{
                trajeto.setId(c.getInt(0));
                trajeto.setNome(c.getString(1));
                trajeto.setLargura(c.getInt(2));
                trajeto.setArea(c.getDouble(3));
                trajeto.setPonto0Latitude(c.getDouble(4));
                trajeto.setPonto1Longitude(c.getDouble(5));
                trajeto.setPonto2Latitude(c.getDouble(6));
                trajeto.setPonto3Longitude(c.getDouble(7));
                trajeto.setPonto4Latitude(c.getDouble(8));
                trajeto.setPonto5Longitude(c.getDouble(9));
                trajeto.setPonto6Latitude(c.getDouble(10));
                trajeto.setPonto7Longitude(c.getDouble(11));
                trajeto.setPonto8Latitude(c.getDouble(12));
                trajeto.setPonto9Longitude(c.getDouble(13));
                trajeto.setPonto10Latitude(c.getDouble(14));
                trajeto.setPonto11Longitude(c.getDouble(15));

            }while(c.moveToNext());
        }

        db.close();
        return trajeto;
    }


    public void deleteById(int id){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABELA, "id = "+id+"", null);
        db.close();
    }

    public void deleteAll(){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABELA, null, null);
        db.close();
    }

    public void updateById(Trajeto trajeto){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", trajeto.getNome());
        cv.put("largura", trajeto.getLargura());
        cv.put("area", trajeto.getArea());
        cv.put("ponto0Latitude", trajeto.getPonto0Latitude());
        cv.put("ponto1Longitude", trajeto.getPonto1Longitude());
        cv.put("ponto2Latitude", trajeto.getPonto2Latitude());
        cv.put("ponto3Longitude", trajeto.getPonto3Longitude());
        cv.put("ponto4Latitude", trajeto.getPonto4Latitude());
        cv.put("ponto5Longitude", trajeto.getPonto5Longitude());
        cv.put("ponto6Latitude", trajeto.getPonto6Latitude());
        cv.put("ponto7Longitude", trajeto.getPonto7Longitude());
        cv.put("ponto8Latitude", trajeto.getPonto8Latitude());
        cv.put("ponto9Longitude", trajeto.getPonto9Longitude());
        cv.put("ponto10Latitude", trajeto.getPonto10Latitude());
        cv.put("ponto11Longitude", trajeto.getPonto11Longitude());

        db.update(TABELA, cv, "id = " + trajeto.getId(), null);
        db.close();
    }

}