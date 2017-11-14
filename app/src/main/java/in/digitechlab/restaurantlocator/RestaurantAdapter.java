package in.digitechlab.restaurantlocator;

/**
 * Created by DELL on 10/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 10/11/2017.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{

    private static final String IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=256&photoreference=";

    private LayoutInflater rInflater;
    private List<NearbyPlaceResponse> RestList;
    private Context mContext;
    Double rest_lat1;
    Double rest_long1;

    public void setRest_lat1(Double rest_lat1) {
        this.rest_lat1 = rest_lat1;
    }

    public void setRest_long1(Double rest_long1) {
        this.rest_long1 = rest_long1;
    }

    public RestaurantAdapter(Context context)
    {
        this.mContext = context;
        this.rInflater = LayoutInflater.from(context);
        this.RestList = new ArrayList<>();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = rInflater.inflate(R.layout.row_restaurant, parent, false);
        final RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra("rest_details", RestList.get(position).getReference());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {



/*
            String urlBuilder = new StringBuilder()
                    .append(IMAGE_URL)
                    .append(RestList.get(position).getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.NEARBY_API_KEY).toString();

            // This is how we use Picasso to load images from the internet.
                Picasso.with(mContext)
                        .load(urlBuilder)
                        .placeholder(R.drawable.rest_ph)
                        .error(R.drawable.rest_ph)
                        .into(holder.img1);
*/

        String rest_name = RestList.get(position).getName().toUpperCase();
        Double rest_lat = RestList.get(position).getGeometry().getLocation().getLat();
        Double rest_long = RestList.get(position).getGeometry().getLocation().getLng();


                    Location loc1 = new Location("");
                    loc1.setLatitude(rest_lat);
                    loc1.setLongitude(rest_long);

                    Location loc2 = new Location("");
                    loc2.setLatitude(rest_lat1);
                    loc2.setLongitude(rest_long1);

                    float distanceInMeters = loc1.distanceTo(loc2);


        if(RestList.get(position).getRating()!=null) {
            Float rest_rating = RestList.get(position).getRating().floatValue();
            //holder.tv3.setText("Rating: "+rest_rating);
            holder.rb.setRating(rest_rating);
        }else if(RestList.get(position).getRating()==null)  {
            Double rest_rating_demo = 0.1;
            Float rest_rating = rest_rating_demo.floatValue();
            //holder.tv3.setText("Rating: "+rest_rating);
            holder.rb.setRating(rest_rating);
        }


       // Boolean rest_open = RestList.get(position).getOpeningHours().getOpenNow();
        String rest_address = RestList.get(position).getVicinity();

        holder.tv1.setText(rest_name);
        holder.tv6.setText(rest_address);
        holder.tv2.setText("Distance "+distanceInMeters+" Meters");
        //holder.tv4.setText(rest_name);

    }

    @Override
    public int getItemCount() {
        return (RestList == null) ? 0 : RestList.size();
    }

    public void setRestList(List<NearbyPlaceResponse> RestList)
    {
        this.RestList.clear();
        this.RestList.addAll(RestList);
        notifyDataSetChanged();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView img1;
        public TextView tv1, tv2, tv3, tv4, tv5, tv6;
        public RatingBar rb;
        public RestaurantViewHolder(View itemView)
        {
            super(itemView);
             img1 = (ImageView) itemView.findViewById(R.id.rest_image);
             tv1 =  (TextView) itemView.findViewById(R.id.rest_name);
             tv2 =  (TextView) itemView.findViewById(R.id.rest_dist);
             //tv3 =  (TextView) itemView.findViewById(R.id.rest_);
             //tv5 =  (TextView) itemView.findViewById(R.id.rest_status);
             tv6 =  (TextView) itemView.findViewById(R.id.rest_address);
             rb = (RatingBar) itemView.findViewById(R.id.rest_rate);
        }
    }


}
