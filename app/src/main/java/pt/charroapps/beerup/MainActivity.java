package pt.charroapps.beerup;

import android.app.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pt.charroapps.beerup.model.Beer;
import pt.charroapps.beerup.model.Datum;
import pt.charroapps.beerup.network.api;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MainActivity extends AppCompatActivity {

    BeerAdapter adapter = null;
    DatumAdapter adapterOff = null;
    RecyclerView rv;
    LinearLayoutManager llm;
    RestAdapter restadapter;
    api beerapi;
    Beer allBeers;
    ArrayList<Beer> algo;
    Boolean controlo = true;
    int numPages, pageToLoad=2;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //algo = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        realm = Realm.getInstance(getApplicationContext());



        if (isNetworkAvailable(this)) {

            // Configure Retrofit to use the proper GSON converter
            restadapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.brewerydb.com/v2")
                    .setConverter(new GsonConverter(CustomGsonParser.returnCustomParser()))
                    .build();

            //restadapter = new RestAdapter.Builder().setEndpoint("https://api.brewerydb.com/v2").build();

            beerapi = restadapter.create(api.class);

            beerapi.getData(new Callback<Beer>() {
                @Override
                public void success(Beer beers, Response response) {

                    numPages = beers.getNumberOfPages();

                    allBeers = beers;
                    adapter = new BeerAdapter(allBeers);
                    rv.setAdapter(adapter);
                    Log.d("TAG2", "Tamanho Inicio" + allBeers.getData().size());


                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("TAG2", "Error " + error);
                }
            });


        } else {

            RealmResults<Datum> allBeersOff = realm.where(Datum.class).findAll();
            adapterOff = new DatumAdapter(allBeersOff);
            rv.setAdapter(adapterOff);
            Log.d("TAG2", "No internet");

        }





    }

    protected void onResume(){
        super.onResume();

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if ((llm.getChildCount() + llm.findFirstVisibleItemPosition()) >= llm.getItemCount()) {

                        //Only when load all the new info from the new page, it can load more info when it
                        //reaches the bottom. Verify if it has more pages to load.
                        if(controlo){
                            if(pageToLoad<=numPages){
                                loadMore(pageToLoad);
                                controlo=false;
                                pageToLoad++;
                            }

                        }
                    }
                }
            }
        });

    }

    protected void onPause(){
        super.onPause();


        if (allBeers!=null) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(allBeers.getData());
            realm.commitTransaction();
            Log.d("TAG2", "Entrou em Pause");
        } else {
            Log.d("TAG2", "Entrou em Pause mas nao tem Internet");
        }


    }

    public void loadMore(int pageToLoad){

        beerapi.getNextPage(pageToLoad, new Callback<Beer>() {
            @Override
            public void success(Beer beers, Response response) {


                List<Datum> listData = beers.getData();
                Datum newData;

                //Populate the allBeers object with new objects
                for (int i = 0; i < listData.size(); i++) {
                    newData = listData.get(i);
                    allBeers.getData().add(newData);
                }

                adapter.notifyItemInserted(allBeers.getData().size() - 1);
                Log.d("TAG2", "Tamanho" + allBeers.getData().size());
                controlo = true;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed 2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
