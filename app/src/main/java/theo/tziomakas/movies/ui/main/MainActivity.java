package theo.tziomakas.movies.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import theo.tziomakas.movies.R;
import theo.tziomakas.movies.adapters.MoviesAdapter;
import theo.tziomakas.movies.data.FavouriteContract;
import theo.tziomakas.movies.models.MovieResponse;
import theo.tziomakas.movies.models.Result;
import theo.tziomakas.movies.ui.SettingsActivity;
import theo.tziomakas.movies.ui.detail.DetailActivity;

public class MainActivity extends AppCompatActivity implements MainViewInterface,MoviesAdapter.MoviesAdapterOnClickHandler,LoaderManager.LoaderCallbacks{
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final int FAV_MOVIES_ID = 1;

    /**********************************************
     * The column of the database we are querying *
     *********************************************/
    private static final String[] FAVOURITE_MOVIE_PROJECTION = {
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE
    };
    private static final String LOG_TAG = "MainActivity";

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    private String TAG = "MainActivity";
    MoviesAdapter adapter;
    MainPresenter mainPresenter;
    private SharedPreferences sharedPrefs;
    private String sortingCriteria;

    List<Result> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        if(savedInstanceState == null) {
            movieList = new ArrayList<>();

            setupMVP();
            setupViews();
            //getMovieList();
        }else{
            movieList = savedInstanceState.getParcelableArrayList("movies");

            setupMVP();
            setupViews();
        }

    }

    private void setupMVP() {

        mainPresenter = new MainPresenter(this);

    }

    private void setupViews(){
        rvMovies.setLayoutManager(new GridLayoutManager(this,2));

    }
    private void getMovieList() {

        mainPresenter.getPopularMovies();

    }

    private void getTopRatedMoviesList() {

        mainPresenter.getTopRatedMovies();

    }
    @Override
    public void showToast(String s) {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMovies(MovieResponse moviesResponse) {

        if(moviesResponse!=null) {
            Log.d(TAG, String.valueOf(moviesResponse.getResults().size()));
            movieList = moviesResponse.getResults();
            adapter = new MoviesAdapter(movieList, MainActivity.this,this);
            rvMovies.setAdapter(adapter);

        }else{

            Log.d(TAG,"Movies response null");

        }

    }

    @Override
    public void displayError(String s) {
        showToast(s);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){

            Context context = MainActivity.this;
            Class activityToBeLauched = SettingsActivity.class;

            Intent intent = new Intent(context,activityToBeLauched);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));

        if(sortingCriteria.equals("popular")) {

            getMovieList();

        }else if(sortingCriteria.equals("top_rated")){

            getTopRatedMoviesList();

        }else{
            Log.v(LOG_TAG,sortingCriteria);
            //adapter.clear();
            getSupportLoaderManager().restartLoader(FAV_MOVIES_ID, null,  this);
        }
    }

    @Override
    public void onClick(String movieId) {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra("movieId",movieId);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("movieId",movieId);
        editor.apply();
        startActivity(i);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        rvMovies.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        savedInstanceState.getParcelableArrayList("movies");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movieList);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, rvMovies.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {

        if (id == FAV_MOVIES_ID) {

            Uri movieUri = FavouriteContract.FavouriteEntry.CONTENT_URI;

            return new CursorLoader(
                    this,
                    movieUri,
                    FAVOURITE_MOVIE_PROJECTION,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        int id = loader.getId();
        if (id == FAV_MOVIES_ID) {
            // Cast Object to display data read from the db.
            Cursor dataCursor = (Cursor) data;

            //mLoadingIndicator.setVisibility(View.INVISIBLE);

            List<Result> results = new ArrayList<>();

            if (dataCursor != null && dataCursor.getCount() > 0) {

                while (dataCursor.moveToNext()) {
                    String cursorId = dataCursor.getString(0);
                    String title = dataCursor.getString(2);
                    String path = dataCursor.getString(1);
                    String overview = dataCursor.getString(3);
                    float rating = dataCursor.getFloat(4);
                    String date = dataCursor.getString(5);

                    Result result = new Result(cursorId, title, path, overview, rating, date);
                    results.add(result);
                }
                adapter.setMoviesData(results);

            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
