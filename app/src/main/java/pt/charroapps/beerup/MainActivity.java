package pt.charroapps.beerup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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
    Boolean controlo = true;
    int numPages, pageToLoad=2;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        * Toolbar, RecyclerView and initialize the the Realm DB
        * */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        realm = Realm.getInstance(getApplicationContext());


        /*
        * Verify if the device has network, if it's OK then call the REST Web service
        * if NOT call the DB
        * */
        if (isNetworkAvailable(this)) {

            // Configure Retrofit to use the proper GSON converter
            restadapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.brewerydb.com/v2")
                    .setConverter(new GsonConverter(CustomGsonParser.returnCustomParser()))
                    .build();

            beerapi = restadapter.create(api.class);

            beerapi.getData(new Callback<Beer>() {
                @Override
                public void success(Beer beers, Response response) {

                    numPages = beers.getNumberOfPages();
                    allBeers = beers;
                    adapter = new BeerAdapter(allBeers);
                    rv.setAdapter(adapter);

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "Failed to connect the Web service", Toast.LENGTH_SHORT).show();
                }
            });


        } else {

            if(realm.isEmpty())
                Toast.makeText(getApplicationContext(), "No Web service neither DB", Toast.LENGTH_SHORT).show();
            else{
                RealmResults<Datum> allBeersOff = realm.where(Datum.class).findAll();
                adapterOff = new DatumAdapter(allBeersOff);
                rv.setAdapter(adapterOff);
                Toast.makeText(getApplicationContext(), "No Web service, using DB", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onResume(){
        super.onResume();

        /*
        * When the recyclerView reaches the end of the list, it is made the call for new results
        * */
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

        /*
        * The user is away from the activity, then it's a good moment to save the results to de DB
        * if the allBeers isn't empty
        * First remove the old DB and make a new one
        * Then populate the DB with the new results
        * */
        if (allBeers!=null) {

            realm.close();
            RealmConfiguration config = realm.getConfiguration();
            Realm.deleteRealm(config);

            realm = Realm.getInstance(getApplicationContext());
            realm.beginTransaction();
            realm.copyToRealm(allBeers.getData());
            realm.commitTransaction();
            realm.close();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        if (realm != null)
            realm.close();
    }
    /*
    * Function to call more results from the Web services, using the api getNextPage
    * and add to the existing object
    * */
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
                controlo = true;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed to load more results", Toast.LENGTH_SHORT).show();
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

    /*
    * Function to verify the network
    * */
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
