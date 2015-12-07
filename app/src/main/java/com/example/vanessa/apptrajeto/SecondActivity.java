package com.example.vanessa.apptrajeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {
    private Button irMapa;
    private Button cancelar;
    private EditText larguraPulverizador;
    public final static String MENSAGEM = "com.example.vanessa.activities.MENSAGEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        irMapa = (Button) findViewById(R.id.button);
        cancelar = (Button) findViewById(R.id.button3);
        larguraPulverizador = (EditText) findViewById(R.id.editText);
        irMapa.setOnClickListener(IrParaMapa);
        cancelar.setOnClickListener(Cancelar);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }
    View.OnClickListener IrParaMapa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(larguraPulverizador.getText() != null){
                Intent i = new Intent(getApplicationContext(), Mapa.class);
                i.putExtra(MENSAGEM,String.valueOf(larguraPulverizador.getText()));
                startActivity(i);
            }
        }
    };
    View.OnClickListener Cancelar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
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
