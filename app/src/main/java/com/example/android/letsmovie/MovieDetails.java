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

        setTitle(movie.getMovieTitle());

    }


}

