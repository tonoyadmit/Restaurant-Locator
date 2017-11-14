package in.digitechlab.restaurantlocator;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 10/21/2017.
 */
public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String restRef;
    private RestApiService ras;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/";
    private static final String IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=256&photoreference=";
    private TextView tv21,tv22, tv23;
    private ImageView img21, img22, img23, img24;
    private String rv1, rv2, rv3, rt1, phn, web, rv4, mapLink;
    Button getMap;
    private Boolean isBackPressed=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_details);
        if (getIntent().hasExtra("rest_details")) {
            restRef = getIntent().getStringExtra("rest_details");
        } else {
            throw new IllegalArgumentException("Detail activity must receive a Serializable");
        }


        tv21 = (TextView) findViewById(R.id.tv101);
        tv22 = (TextView) findViewById(R.id.tv102);
        tv23 = (TextView) findViewById(R.id.tv103);
        img21 = (ImageView) findViewById(R.id.img101);
        img22 = (ImageView) findViewById(R.id.img102);
        img23 = (ImageView) findViewById(R.id.img103);
        img24 = (ImageView) findViewById(R.id.img104);
        getMap = (Button) findViewById(R.id.btnMap);


        //Toast.makeText(getApplicationContext(),restRef, Toast.LENGTH_LONG ).show();

/*        title.setText(mMovie.getTitle());
        description.setText(mMovie.getDescription());
        user_rating.setText("Movie Rating: "+mMovie.getUserRating()+"/10");
        release_date.setText(String.format("%.4s", mMovie.getReleaseDate()));

        String urlBuilder1 = new StringBuilder()
                .append(BASE_POSTER_URL)
                .append(mMovie.getPoster()).toString();

        String urlBuilder2 = new StringBuilder()
                .append(BASE_BACKDROP_URL)
                .append(mMovie.getBackdrop()).toString();

        Picasso.with(this)
                .load(urlBuilder1)
                .into(poster);
        Picasso.with(this)
                .load(urlBuilder2)
                .into(backdrop);*/

        initializeNetworkLibrary();

        getRestResponse();

        getMap.setOnClickListener(this);
    }


    private void getRestResponse() {

        String urlString = String.format("json?reference=%s&key=%s",restRef, BuildConfig.NEARBY_API_KEY);

        Call<DetailsModel> rCall = ras.getDetailResponse(urlString);

        rCall.enqueue(new Callback<DetailsModel>() {
            @Override
            public void onResponse(Call<DetailsModel> call, Response<DetailsModel> response) {
                if(response.code()==200){
                    DetailsModel rp = response.body();

                    //Spanned htmlAsSpanned = Html.fromHtml(rp.getResult().getAdrAddress());
                    tv22.setText("Welcome To "+rp.getResult().getName().toString().toUpperCase());

                    if(rp.getResult().getReviews().size()>0) {
                        rv1 = rp.getResult().getReviews().get(0).getText();
                    }else{
                        rv1 = "No Review Found!";
                    }
                    if(rp.getResult().getReviews().size()>1) {
                        rv2 = rp.getResult().getReviews().get(1).getText();
                    }else{
                        rv2 = "No Review Found!";
                    }if(rp.getResult().getReviews().size()>2) {
                        rv3 = rp.getResult().getReviews().get(2).getText();
                    }else{
                        rv3 = "No Review Found!";
                    }if(rp.getResult().getReviews().size()>3) {
                        rv4 = rp.getResult().getReviews().get(3).getText();
                    }else{
                        rv4 = "No Review Found!";
                    }
                    if(rp.getResult().getRating()!=null) {
                        rt1 = rp.getResult().getRating().toString();
                    }else{
                        rt1 = "N/A";
                    }

                    if(rp.getResult().getWebsite()!=null) {
                        web = rp.getResult().getWebsite();
                    }else {
                        web = "Not Available";
                    }

                    if(rp.getResult().getFormattedPhoneNumber()!=null) {
                        phn = rp.getResult().getFormattedPhoneNumber();
                    }else{
                        phn = "Not Available";
                    }
                    tv23.setText("CUSTOMER REVIEWS:\nRated "+rt1+" out of 5\n\n —[1] "+rv1+"\n —[2] "+rv2+"\n —[3] "+rv3+"\n —[4] "+rv4);

                    tv21.setText("ADDRESS: "+ rp.getResult().getFormattedAddress()+"\nPHONE NO: "+phn
                    +"\nWEBSITE: "+web);

                    if(rp.getResult().getPhotos().size()>0) {

                        String urlBuilder = new StringBuilder()
                                .append(IMAGE_URL)
                                .append(rp.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.NEARBY_API_KEY).toString();

                        // This is how we use Picasso to load images from the internet.
                        Picasso.with(getApplicationContext())
                                .load(urlBuilder)
                                .placeholder(R.drawable.loading1)
                                .error(R.drawable.loading1)
                                .into(img21);
                    }

                    if(rp.getResult().getPhotos().size()>1) {

                        String urlBuilder2 = new StringBuilder()
                                .append(IMAGE_URL)
                                .append(rp.getResult().getPhotos().get(1).getPhotoReference() + "&key=" + BuildConfig.NEARBY_API_KEY).toString();

                        // This is how we use Picasso to load images from the internet.
                        Picasso.with(getApplicationContext())
                                .load(urlBuilder2)
                                .placeholder(R.drawable.loading1)
                                .error(R.drawable.loading1)
                                .into(img22);
                    }

                    if(rp.getResult().getPhotos().size()>2) {

                        String urlBuilder3 = new StringBuilder()
                                .append(IMAGE_URL)
                                .append(rp.getResult().getPhotos().get(2).getPhotoReference() + "&key=" + BuildConfig.NEARBY_API_KEY).toString();

                        // This is how we use Picasso to load images from the internet.
                        Picasso.with(getApplicationContext())
                                .load(urlBuilder3)
                                .placeholder(R.drawable.loading1)
                                .error(R.drawable.loading1)
                                .into(img23);
                    }

                    if(rp.getResult().getPhotos().size()>3) {

                        String urlBuilder4 = new StringBuilder()
                                .append(IMAGE_URL)
                                .append(rp.getResult().getPhotos().get(3).getPhotoReference() + "&key=" + BuildConfig.NEARBY_API_KEY).toString();

                        // This is how we use Picasso to load images from the internet.
                        Picasso.with(getApplicationContext())
                                .load(urlBuilder4)
                                .placeholder(R.drawable.loading1)
                                .error(R.drawable.loading1)
                                .into(img24);
                        mapLink = rp.getResult().getUrl();
                    }

                    Toast.makeText(getApplicationContext(), "Images are loading...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Query Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailsModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Forcast Failed : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                tv21.setText(t.getMessage());

            }
        });

    }

    private void initializeNetworkLibrary() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ras = retrofit.create(RestApiService.class);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(RestaurantDetailActivity.this, RestMapActivity.class);
        intent.putExtra("map_link", mapLink);
        startActivity(intent);
    }
}
