package pt.charroapps.beerup.network;

import java.util.List;

import pt.charroapps.beerup.model.Beer;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ivolopes on 05/02/16.
 */
public interface api {

    @GET("/search?q=2&type=beer&key=0c7792b9c2f05064fe6bd5ef919cc5f2&format=json")
    public void getData(Callback<Beer> response);

    @GET("/search?q=2&type=beer&key=0c7792b9c2f05064fe6bd5ef919cc5f2&format=json")
    public void getNextPage(@Query("p") int npage, Callback<Beer> response);
}
