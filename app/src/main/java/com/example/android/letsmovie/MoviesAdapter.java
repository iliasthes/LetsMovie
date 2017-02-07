package com.example.android.letsmovie;

/**
 * Created by ILIAS on 24/1/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.squareup.picasso.Picasso;
import java.lang.String;
import java.util.ArrayList;




public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
   private ArrayList<MovieJson> mMovies;
    private Context mContext;
//Perhaps instead of Image Arraylist i should use Movie Array list. See it later
    public MoviesAdapter ( ArrayList<MovieJson> movies) {
        mMovies = movies;
    }
    interface onPosterClickHandler {
        void onClick(MovieJson movie);
    }

    void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }
    void addMovies(ArrayList<MovieJson> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }
    public class PosterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView mItemImage;
         Context mContext;
        int position;

        public PosterHolder(View v) {
            super(v);
            mItemImage = (ImageView) v.findViewById(R.id.movie_poster);
            v.setOnClickListener(this);
        }
        void takeImage(MovieJson movieJson) {
            String posterPath = movieJson.getPosterPath();
            Picasso.with(mContext).load(posterPath).into(mItemImage);
        }
        //5
       @Override
       public void onClick(View v) {

           Context context = v.getContext();
           MovieJson currentMovie = mMovies.get(position);
           Intent intent = new Intent(context, MovieDetails.class);
           intent.putExtra("Chosen Movie",currentMovie);
           context.startActivity(intent);
        }

    }
    @Override
    public MoviesAdapter.PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new PosterHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.PosterHolder holder, int position) {
     //  MovieJson currentMovie = mMovies.get(position);
     //   String poster_path = currentMovie.getPosterPath();
        holder.takeImage(mMovies.get(position));
 //       Picasso.with(mContext).load(poster_path).into(holder.mItemImage);
    }



    @Override
    public int getItemCount() {
        return mMovies.size();
    }
    //    this.context = context;


}
