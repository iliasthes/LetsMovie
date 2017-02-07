package com.example.android.letsmovie;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onPosterClickHandler{

    private MoviesAdapter mAdapter;
    private RecyclerView moviesRecyclerView;
    private static String API_KEY;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private TextView mErrorMessageDisplay;
   private ProgressBar mLoadingIndicator;
    GridLayoutManager layoutManager;
    ArrayList<MovieJson> mMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getString(R.string.api_key);
        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        //Progress Bar used to display loading process
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        //Constructor of GridLayoutManager with  5 or 3 columns depending on the device orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 5);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);

        //The MoviesAdapter is responsible for linking our Movies data with the Views that
        // will end up displaying on our screen.
        mMovies = new ArrayList<>(20);
          mAdapter = new MoviesAdapter(mMovies );
        moviesRecyclerView.setAdapter(mAdapter);
     //   showMoviePosters();

    }
    @Override
    public void onStart() {
        super.onStart();
       // updateMovies();
    //    showMoviePosters();
    }


    //  This method will get the user's preferred Movie for preview, and then tell some
    //          background method to get the Movie data in the background.

    private void loadMoviePosters() {
        new FetchMovieJsonData();

    }

    private void showMoviePosters() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        //updateMovies();
       // loadMoviePosters();
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    private void updateMovies() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortingOrder = pref.getString(getResources().getString(R.string.sorting_key),getResources().getString(R.string.default_sorting));
        new FetchMovieJsonData().execute(sortingOrder);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.title) {
            Intent intent = new Intent(this, SortingSettings.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieJson movie) {

    }

    private class FetchMovieJsonData extends AsyncTask<String, Void, ArrayList<MovieJson>> {
        private ConnectivityManager mConnectivityManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Check network connection.
            if (isNetworkConnected() == false) {
                // Cancel request.
                Log.i(getClass().getName(), "Problem with internet connection");
                Toast.makeText(getApplicationContext(), "Please Check Internet connectivity", Toast.LENGTH_SHORT).show();
                cancel(true);
                return;
            }
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieJson> doInBackground(String... searchQuery) {
            // Stop if cancelled
            if (isCancelled()) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            try {
                final String BASE_URL = "https://api.themoviedb.org/3/authentication/token/new?"; //http://api.themoviedb.org/3/movie/
                final String key = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                  //      .appendPath(searchQuery[0])
                        .appendQueryParameter(key, API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.i(LOG_TAG + "search query", builtUri.toString());

                // Creates a request to TMDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    moviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    moviesJsonStr = null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                moviesJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Log.i("result", moviesJsonStr);
            try
            {
                //returns the movies arrayList
                return getMovieJsonData(moviesJsonStr);
            }catch (JSONException ex){
                Log.i(LOG_TAG, "Error in Json parsing");
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieJson> movies) {
            super.onPostExecute(movies);
            if(movies!=null){

              //  mAdapter.clear();
                mAdapter.addMovies(movies);
                showMoviePosters();
            } else {
                showErrorMessage();
            }
        }

        //get movie data from JsonString
        private ArrayList<MovieJson> getMovieJsonData(String moviesJsonStr) throws JSONException{
            final String  TAG_RESULTS = "results";
            final String TAG_TITLE = "original_title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER = "poster_path";
            final String TAG_RATING ="vote_average";
            final String TAG_RELEASE_DATE = "release_date";
            ArrayList<MovieJson> movies = new ArrayList<>();
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray(TAG_RESULTS);
            MovieJson movieJson_object;
            for(int i=0; i<movieArray.length();i++){
                JSONObject movieJsonObj = movieArray.getJSONObject(i);
                String title = movieJsonObj.getString(TAG_TITLE);
                String overview = movieJsonObj.getString(TAG_OVERVIEW);
                String poster = movieJsonObj.getString(TAG_POSTER);
                String rating = movieJsonObj.getString(TAG_RATING);
                String releaseDate = movieJsonObj.getString(TAG_RELEASE_DATE);
                movieJson_object = new MovieJson(title,overview,rating,releaseDate,poster);
                movies.add(movieJson_object);
            }
            return movies;

        }
        protected boolean isNetworkConnected() {

            // Instantiate mConnectivityManager if necessary
            if (mConnectivityManager == null) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            // Is device connected to the Internet?
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                showErrorMessage();
                return false;
            }
        }
    }



}
