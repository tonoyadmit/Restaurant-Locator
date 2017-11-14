package in.digitechlab.restaurantlocator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Acer on 06-Oct-17.
 */

public interface NearbyPlaceApiService {

    @GET()
    Call<NearbyPlaceResponse.WeatherResult>getNearbyResponse(@Url String urlString);
}
