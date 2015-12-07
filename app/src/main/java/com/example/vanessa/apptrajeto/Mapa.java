package com.example.vanessa.apptrajeto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mapa extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //Novos
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = Mapa.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private static byte contagem = 0;
    double currentLatitude = 0;
    double currentLongitude = 0;
    byte repet = 0;
    boolean haveFirstPoint = false;
    boolean first = true;
    LatLng oldCoord;
    double totalArea = 0;
    Location oldLocation;
    float testArea = 0;
    List<Double> listaPontos = new ArrayList<Double>();
    double LatAnterior = 0, LongAnterior = 0;
    Button parar;
    int pararPontos = 0;
    int update = 0;
    String nomeTrajeto;
    Trajeto trajInsertUpdate;
    private TextView area;
    private int largura;

    private static final String TAGG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;

    int valor = 0;
    List<Integer> val = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setUpMapIfNeeded();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 1 second, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        area = (TextView) findViewById(R.id.textView3);
        parar = (Button) findViewById(R.id.parar);
        parar.setOnClickListener(buttonParar);


        //MSG recebe o id do item selecionado, quando o usuario restaurar um trajeto
        Intent i = getIntent();
        String restauraTrajeto = i.getStringExtra(ListaActivity.MSG);

        //MENSAGEM recebe a largura do equipamento inserida pelo usuario
        Intent j = getIntent();
        String larg = j.getStringExtra(SecondActivity.MENSAGEM);
        //Teste necessario para a situação onde o cliente esta restaurando um trajeto
        //no caso larg sera nulo.
        if(larg != null){
            largura = Integer.parseInt(larg);
        }

        //Testa se o usuario esta restaurando um trajeto
        if(restauraTrajeto != null){
            //Busca no banco os dados do trajeto que o usuario quer restaurar.
            DbHelper dbHelper = new DbHelper(this);
            trajInsertUpdate = dbHelper.selectById(Integer.parseInt(restauraTrajeto));

            //Inicializa vetor que controla a ordem em que os pontos foram sendo guardados,
            //para que os pontos sejam salvos no banco na ordem em que o trajeto foi percorrido.
            val.add(0,0);
            val.add(1,0);
            val.add(2,0);
            val.add(3,0);

            //Inicializa a area com o valor guardado no banco(no caso o trajeto esta sendo restaurado)
            totalArea = trajInsertUpdate.getArea();
            //Seta o valor da area para o textView
            area.setText("ÁREA: " + String.format("%.2f", totalArea) + "m²");
            //Inicializa a variavel de largura do equipamento com o valor guardado no banco
            largura = trajInsertUpdate.getLargura();

            //Inicializa a lista(que possui os pontos que serao salvos no banco)
            //com os pontos que estavam salvos(no caso o trajeto esta sendo restaurado)
            initPontos(trajInsertUpdate.getPonto0Latitude(), trajInsertUpdate.getPonto1Longitude(),
                    trajInsertUpdate.getPonto2Latitude(), trajInsertUpdate.getPonto3Longitude(),
                    trajInsertUpdate.getPonto4Latitude(), trajInsertUpdate.getPonto5Longitude(),
                    trajInsertUpdate.getPonto6Latitude(), trajInsertUpdate.getPonto7Longitude(),
                    trajInsertUpdate.getPonto8Latitude(), trajInsertUpdate.getPonto9Longitude(),
                    trajInsertUpdate.getPonto10Latitude(), trajInsertUpdate.getPonto11Longitude());

            //Pinta na tela o trajeto que estava salvo no banco
            pintaTrajeto(trajInsertUpdate);

            //update = 0 :o usuario esta criando um novo trajeto
            //update = 1 :o usuario esta restaurando um trajeto
            update = 1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Cria mapa com visao de satelite.
            // Para visao mais simples, comentar esta linha
            //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            handleNewLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        if (haveFirstPoint == true){
            if (repet == 0){
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                repet++;
            } else if (repet > 0 && repet < 5 ){
                currentLatitude = currentLatitude + location.getLatitude();
                currentLongitude = currentLongitude + location.getLongitude();
                repet++;
            } else {
                repet = 0;
                currentLatitude = currentLatitude / 5;
                currentLongitude = currentLongitude / 5;
                LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                contagem++;
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title("" + contagem);
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 22));
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(oldCoord)
                        .add(latLng);
                Polyline polyline = mMap.addPolyline(rectOptions);

                //Faz o calculo da area de acordo com a distancia entre os pontos e a largura do equipamento.
                setTotalArea(location, oldCoord);
                //Seta o valor atualizado da área
                area.setText("ÁREA: " + String.format("%.2f", totalArea) + "m²");
                oldCoord = latLng;

                //Guarda os pontos(em uma lista) que serao inseridos no banco.
                guardaPosicao(currentLatitude, currentLongitude);
            }
        }
        else {
            getStartPoint(location);
        }
    }

    public void initPontos(double lat1, double long1, double lat2, double long2, double lat3, double long3, double lat4, double long4, double lat5, double long5, double lat6, double long6){

        //Inicializa os pontos da lista.
        //Se for um trajeto novo, todos serao inicializados com o primeiro ponto.
        //Se for um trajeto restaurado, todos serao inicializados com os pontos que estavam no banco.

        //primeiro ponto(0,1)
        listaPontos.add(lat1);
        listaPontos.add(long1);
        //ponto + ao norte(2,3)
        listaPontos.add(lat2);
        listaPontos.add(long2);
        //ponto + ao sul(4,5)
        listaPontos.add(lat3);
        listaPontos.add(long3);
        //ponto + ao leste(6,7)
        listaPontos.add(lat4);
        listaPontos.add(long4);
        //ponto + ao oeste(8,9)
        listaPontos.add(lat5);
        listaPontos.add(long5);
        //ultimo ponto(10,11)
        listaPontos.add(lat6);
        listaPontos.add(long6);
    }

    //Codigo do pop up, onde o usuario insere o nome do trajeto a ser salvo
    View.OnClickListener buttonParar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Se for um trajeto novo, o pop up abre
            if(update == 0) {
                showDialog(DLG_EXAMPLE1);
                //Se for um trajeto restaurado o nome ja existe, portanto o pop up nao abre
            }else if(update == 1){
                //Esta flag indica que o trajeto deve ser inserido ou atualizado no banco.
                pararPontos = 1;
            }
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DLG_EXAMPLE1:
                return createExampleDialog();
            default:
                return null;
        }
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DLG_EXAMPLE1:
                // Clear the input box.
                EditText text = (EditText) dialog.findViewById(TEXT_ID);
                text.setText("");
                break;
        }
    }
    private Dialog createExampleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Olá!");
        builder.setMessage("Digite um nome para o seu trajeto:");

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(TEXT_ID);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                nomeTrajeto = input.getText().toString();
                pararPontos = 1;
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        return builder.create();
    }

    //Funcao que busca os maiores valores dentro de uma lista
    //usada com a lista que ordena os pontos.
    public int buscaMaior(List l, int teste){
        int maior = teste;
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
            int val = (Integer) iterator.next();
            if(val > maior){
                maior = val;
            }
        }
        return l.lastIndexOf(maior);
    }

    //Funcao retorna a posicao da latitude da lista de coordenadas
    //dependendo da posicao da lista que ordena os pontos.
    public int buscaCoordenada(int index){
        if(index == 0){
            return 2;
        }else if(index == 1){
            return 4;
        }else if(index == 2){
            return 6;
        }else if(index == 3){
            return 8;
        }
        return 0;
    }

    //Seleciona os pontos a serem salvos ou alterados no banco.
    public void guardaPosicao(double Lat, double Long){

        //Se a lista de pontos estiver vazia ela e a lista
        //de ordenacao sao inicializadas.
        if(listaPontos.isEmpty()){

            //Inicializa a lista que testa a posicao dos pontos
            val.add(0,0);
            val.add(1,0);
            val.add(2,0);
            val.add(3,0);

            //Inicializa todos os pontos com a primeira coordenada.
            initPontos(Lat, Long, Lat, Long, Lat, Long, Lat, Long, Lat, Long, Lat, Long);

            //Se pararPontos = 1 , o usuario clicou no botao parar
            //aqui o ultimo ponto e salvo na lista.
        }else if(pararPontos == 1){

            //Salva o ultimo ponto
            listaPontos.set(10, Lat);
            listaPontos.set(11, Long);

            //Inserir ou modificar os pontos no banco.
            pararPontos = 2;

            //Testa os pontos a serem salvos.
        }else if(pararPontos == 0){

            //Busca o ponto mais ao norte
            if (Lat > LatAnterior) {

                listaPontos.set(2, Lat);
                listaPontos.set(3, Long);
                valor++;
                val.set(0,valor);

                //Busca o ponto mais ao sul
            } else if (Lat < LatAnterior) {

                listaPontos.set(4, Lat);
                listaPontos.set(5, Long);
                valor++;
                val.set(1,valor);
            }
            //Busca o ponto mais ao leste
            if (Long > LongAnterior) {

                listaPontos.set(6, Lat);
                listaPontos.set(7, Long);
                valor++;
                val.set(2,valor);

                //Busca o ponto mais ao oeste
            } else if (Long < LongAnterior) {

                listaPontos.set(8, Lat);
                listaPontos.set(9,Long);
                valor++;
                val.set(3,valor);
            }
        }

        //Insere ou modifica os pontos no banco.
        if(pararPontos == 2){

            //Ordena pontos de acordo com os valores da lista val
            int index1 = buscaMaior(val, val.get(0));
            int pos1 = buscaCoordenada(index1);
            val.set(index1, -1);
            int index2 = buscaMaior(val, val.get(0));
            int pos2 = buscaCoordenada(index2);
            val.set(index2, -1);
            int index3 = buscaMaior(val, val.get(0));
            int pos3 = buscaCoordenada(index3);
            val.set(index3, -1);
            int index4 = buscaMaior(val, val.get(0));
            int pos4 = buscaCoordenada(index4);
            val.set(index4, -1);

            DbHelper dbHelper = new DbHelper(this);
            if(update == 0){
                //Se for um novo trajeto, ele e criado.
                trajInsertUpdate = new Trajeto();
            }
            //Seta os valores para serem inseridos ou modificados no banco.
            trajInsertUpdate.setArea(totalArea);
            trajInsertUpdate.setPonto0Latitude(listaPontos.get(0));
            trajInsertUpdate.setPonto1Longitude(listaPontos.get(1));
            trajInsertUpdate.setPonto2Latitude(listaPontos.get(pos4));
            trajInsertUpdate.setPonto3Longitude(listaPontos.get(pos4+1));
            trajInsertUpdate.setPonto4Latitude(listaPontos.get(pos3));
            trajInsertUpdate.setPonto5Longitude(listaPontos.get(pos3+1));
            trajInsertUpdate.setPonto6Latitude(listaPontos.get(pos2));
            trajInsertUpdate.setPonto7Longitude(listaPontos.get(pos2+1));
            trajInsertUpdate.setPonto8Latitude(listaPontos.get(pos1));
            trajInsertUpdate.setPonto9Longitude(listaPontos.get(pos1+1));
            trajInsertUpdate.setPonto10Latitude(listaPontos.get(10));
            trajInsertUpdate.setPonto11Longitude(listaPontos.get(11));

            if(update == 0) {
                //Salva trajeto no banco.
                trajInsertUpdate.setLargura(largura);
                trajInsertUpdate.setNome(nomeTrajeto);

                dbHelper.insert(trajInsertUpdate);
                Toast.makeText(getApplicationContext(),"Trajeto inserido no banco!", Toast.LENGTH_LONG).show();

            }else if(update == 1){
                //Altera trajeto no banco.
                dbHelper.updateById(trajInsertUpdate);
                Toast.makeText(getApplicationContext(),"Trajeto alterado com sucesso!", Toast.LENGTH_LONG).show();
            }
            pararPontos = 3;
        }
        //Recebe o ponto anterior para testar com o ponto atual.
        LatAnterior = Lat;
        LongAnterior = Long;
    }

    //Desenha um poligono na tela de acordo com os pontos.
    public void pintaTrajeto(Trajeto traj){

        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(traj.getPonto0Latitude(), traj.getPonto1Longitude()),
                        new LatLng(traj.getPonto2Latitude(), traj.getPonto3Longitude()),
                        new LatLng(traj.getPonto4Latitude(), traj.getPonto5Longitude()),
                        new LatLng(traj.getPonto6Latitude(), traj.getPonto7Longitude()),
                        new LatLng(traj.getPonto8Latitude(), traj.getPonto9Longitude()),
                        new LatLng(traj.getPonto10Latitude(), traj.getPonto11Longitude()))
                .strokeColor(Color.rgb(255,0,0))
                .fillColor(Color.argb(100, 255, 0, 0)));
        polygon.setVisible(true);

        /*PolylineOptions line=
                new PolylineOptions().add(new LatLng(traj.getPonto0Latitude(), traj.getPonto1Longitude()),
                        new LatLng(traj.getPonto2Latitude(), traj.getPonto3Longitude()),
                        new LatLng(traj.getPonto4Latitude(), traj.getPonto5Longitude()),
                        new LatLng(traj.getPonto6Latitude(), traj.getPonto7Longitude()),
                        new LatLng(traj.getPonto8Latitude(), traj.getPonto9Longitude()),
                        new LatLng(traj.getPonto10Latitude(), traj.getPonto11Longitude())
                )
                        .width(5).color(Color.RED);

        mMap.addPolyline(line);*/
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /*public float getArea (){
        currentLocation = new Location();
        currentLocation.
    }*/

    //Calcula a distancia entre os pontos e calcula a area.
    public void setTotalArea (Location location, LatLng oldCoord){
        Location oldLocation = new Location("");
        oldLocation.setLatitude(oldCoord.latitude);
        oldLocation.setLongitude(oldCoord.longitude);
        if(totalArea == 0){
            totalArea = largura * location.distanceTo(oldLocation);
        }else{
            totalArea = totalArea + (largura * location.distanceTo(oldLocation));
        }
        //System.out.println("Area: " + totalArea);
    }

    public void getStartPoint (Location location){
        if (first == true) {
            testArea = 1;
            oldLocation = location;
            first = false;
        } else {
            testArea = location.distanceTo(oldLocation);
            if (testArea < 0.03){
                haveFirstPoint = true;
                LatLng tempCoord = new LatLng(location.getLatitude(), location.getLongitude());
                oldCoord = tempCoord;
                MarkerOptions options = new MarkerOptions()
                        .position(oldCoord)
                        .title("Start");
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(oldCoord));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oldCoord, 21));
            } else {
                oldLocation = location;
            }
        }
    }

    /*public Double calculaDistancia(List lista, Double nextLat, Double nextLong){
        double Lat = 0, Long = 0;
        double distanceInMeters = 0, distance = 1000;

        for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
            Lat = (Double) iterator.next();
            Long = (Double) iterator.next();

            Location loc1 = new Location("");
            loc1.setLatitude(nextLat);
            loc1.setLongitude(nextLong);

            Location loc2 = new Location("");
            loc2.setLatitude(Lat);
            loc2.setLongitude(Long);

            distanceInMeters = loc1.distanceTo(loc2);

            if(distanceInMeters < distance){
                nextLat = Lat;
                nextLong = Long;
                distance = distanceInMeters;
            }
        }
        return nextLat;
    }*/
}
