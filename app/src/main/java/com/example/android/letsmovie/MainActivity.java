package com.example.android.letsmovie;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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

import static com.example.android.letsmovie.R.xml.preferences;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onPosterClickHandler {

    private MoviesAdapter mAdapter;
    private RecyclerView moviesRecyclerView;
    private static String API_KEY;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    GridLayoutManager layoutManager;
    ArrayList<MovieJson> mMovies;
    String sortingOrder;
    private boolean preferencesChanged = true;
      public SharedPreferences mSharedPreferences;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = "dd416063f200188616650c00ff321d88";
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
      //  mSharedPreferences.edit().putString("sorting_order_key", "popular").apply();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferencesChangeListener);
        //The MoviesAdapter is responsible for linking our Movies data with the Views that
        // will end up displaying on our screen.
        mMovies = new ArrayList<>(20);
        mAdapter = new MoviesAdapter(mMovies, this);
        moviesRecyclerView.setAdapter(mAdapter);
       // updateMovies();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onResume() {
        super.onResume();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (preferencesChanged) {
        updateMovies();
        }

        // updateMovies();
      //  showMoviePosters();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
        }

    // Remember to delete if i will not use it
  //  private void loadMoviePosters() {
  //      new FetchMovieJsonData();
///
  //  }
   // @Override
 //   public void onDestroy() {
  //      mSharedPreferences.edit().remove(getString(R.string.sorting_key)).apply();
  //      super.onDestroy();

 //   }
    private void showMoviePosters() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        //updateMovies();
        // loadMoviePosters();
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    private void updateMovies() {

         mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sortingOrder = mSharedPreferences.getString(getResources().getString(R.string.sorting_key),"popular");// getResources().getString(R.string.default_sorting));//(SortingSettings.KEY_SORTING_ORDER, "");
           mMovies.clear();
        new FetchMovieJsonData().execute(sortingOrder);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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

    private OnSharedPreferenceChangeListener preferencesChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            preferencesChanged = true;
        }
    };

    @Override
    public void onClick(MovieJson movie) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class FetchMovieJsonData extends AsyncTask<String, Void, ArrayList<MovieJson>> {
        //  private ConnectivityManager mConnectivityManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieJson> doInBackground(String... params) {

            if (isCancelled()) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String moviesJsonStr = null;
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String key = "api_key";
                Uri builtUri = Uri.parse(BASE_URL + sortingOrder).buildUpon()
                        .appendQueryParameter(key, API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                // Connecting to the Movie Database and send Get request to take the Movies Data
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // All data are read in a string
                InputStream inStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inStream == null) {

                    moviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inStream));
                //While string is not null keep reading and adding it to the builder
                String line;
                while ((line = reader.readLine()) != null) {

                    builder.append(line + "\n");
                }

                if (builder.length() == 0) {
                    // If builder got nothing then we don't need have to parse anything
                    moviesJsonStr = null;
                }
                // Obligated exceptions i dont get anything!!!!!!!!! Android studio completed these
                moviesJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                moviesJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Closing Stream failed", e);
                    }
                }
            }

            try {//Gives us the movies arrayList
                return getMovieJsonData(moviesJsonStr);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                return null;

            } catch (JSONException ex) {
                Log.i(LOG_TAG, "We didn't made to parse MovieJson data");
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieJson> movies) {
            super.onPostExecute(movies);
            if (movies != null) {

                //  mAdapter.clear(); is it really necessary;
                mAdapter.addMovies(movies);
                showMoviePosters();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            } else {
                showErrorMessage();
            }
        }

        //get movie data from JsonString
        private ArrayList<MovieJson> getMovieJsonData(String moviesJsonStr) throws JSONException {
            final String Results = "results";
            final String Title = "original_title";
            final String Overview = "overview";
            final String Poster_Path = "poster_path";
            final String Vote_Rating = "vote_average";
            final String Release_Date = "release_date";
            ArrayList<MovieJson> movies = new ArrayList<>();
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray(Results);
            MovieJson movieJson_object;
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJsonObj = movieArray.getJSONObject(i);
                String title = movieJsonObj.getString(Title);
                String overview = movieJsonObj.getString(Overview);
                String poster = movieJsonObj.getString(Poster_Path);
                String rating = movieJsonObj.getString(Vote_Rating);
                String releaseDate = movieJsonObj.getString(Release_Date);
                movieJson_object = new MovieJson(title, overview, rating, releaseDate, poster);
                movies.add(movieJson_object);
            }
            return movies;

        }

    }


}
