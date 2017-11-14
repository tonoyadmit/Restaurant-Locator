package in.digitechlab.restaurantlocator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Acer on 06-Oct-17.
 */

public interface RestApiService {

    @GET()
    Call<DetailsModel>getDetailResponse(@Url String urlString);
}
