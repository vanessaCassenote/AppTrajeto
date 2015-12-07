package com.example.vanessa.apptrajeto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView list;
    public final static String MSG = "com.example.vanessa.activities.MSG";
    private static final String TAG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private int posicao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        list = (ListView) findViewById(R.id.listView);

        DbHelper dbHelper = new DbHelper(this);
        Trajeto traj = new Trajeto();
        List<Trajeto> listaTrajetos = dbHelper.selectAllNames();

        /*for (Iterator iterator = listaTrajetos.iterator(); iterator.hasNext();) {
            traj = (Trajeto) iterator.next();
            System.out.println(traj.toString());
        }*/

        final ArrayAdapter<Trajeto> adapter = new ArrayAdapter<Trajeto>(this, android.R.layout.simple_list_item_1, listaTrajetos);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                posicao = position;
                showDialog(DLG_EXAMPLE1);
            }
        });

    }
    /**
     * Called to create a dialog to be shown.
     */
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DLG_EXAMPLE1:
                return createExampleDialog();
            default:
                return null;
        }
    }


    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createExampleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Olá");
        builder.setMessage("Selecione sua opção:");

        builder.setPositiveButton("Ver trajeto", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                Trajeto trajeto = (Trajeto) list.getItemAtPosition(posicao);

                Intent i = new Intent(getApplicationContext(), Mapa.class);
                i.putExtra(MSG, String.valueOf(trajeto.getId()));
                startActivity(i);

                return;
            }
        });

        builder.setNegativeButton("Excluir trajeto", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Trajeto trajeto = (Trajeto) list.getItemAtPosition(posicao);
                DbHelper dbHelper = new DbHelper(getApplicationContext());
                dbHelper.deleteById(trajeto.getId());
                Toast.makeText(getApplicationContext(), "Trajeto excluido!", Toast.LENGTH_LONG).show();

                return;
            }
        });

        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
