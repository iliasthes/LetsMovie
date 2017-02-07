package com.example.android.letsmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ILIAS on 28/1/2017.
 */

public class MovieDetails extends AppCompatActivity {
    public static final String MOVIE_OBJECT="movie_obj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        //get intent
        Intent intent = getIntent();
        MovieJson movie = intent.getParcelableExtra("Chosen Movie");

        ImageView moviePoster = (ImageView)findViewById(R.id.movie_poster_details);
        Picasso.with(getApplicationContext()).load(movie.getPosterPath()).into(moviePoster);

        TextView movieTitle = (TextView)findViewById(R.id.movie_title);
        movieTitle.setText(movie.getMovieTitle());
//Na thymithw na to ksanakoitaksw
        TextView movieRating = (TextView)findViewById(R.id.movie_rating);
        movieRating.setText(movie.getUserRating());

        TextView releaseDate = (TextView)findViewById(R.id.movie_date);
        releaseDate.setText(movie.getReleaseDate());

        TextView overview = (TextView)findViewById(R.id.movie_description);
        overview.setText(movie.getMovieOverview());

        setTitle(movie.getMovieTitle()); //sets the activity title as Movie name

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable the back button in detail activity
    }

   // @Override
  //  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
   //     return true;
 //   }

   // @Override
 //   public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    //    int id = item.getItemId();

        //noinspection SimplifiableIfStatement
   //     if (id == R.id.action_settings) {
    //        return true;
   //     }

    //    return super.onOptionsItemSelected(item);
 //   }
}

