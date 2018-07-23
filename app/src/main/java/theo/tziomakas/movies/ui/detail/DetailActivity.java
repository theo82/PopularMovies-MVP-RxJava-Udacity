package theo.tziomakas.movies.ui.detail;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import theo.tziomakas.movies.R;
import theo.tziomakas.movies.adapters.MoviesAdapter;
import theo.tziomakas.movies.adapters.ReviewAdapter;
import theo.tziomakas.movies.adapters.TrailersAdapter;
import theo.tziomakas.movies.data.FavouriteContract;
import theo.tziomakas.movies.models.Movie;
import theo.tziomakas.movies.models.Reviews;
import theo.tziomakas.movies.models.ReviewsResponse;
import theo.tziomakas.movies.models.Trailers;
import theo.tziomakas.movies.models.TrailersResponse;
import theo.tziomakas.movies.ui.MyDividerItemDecoration;
import theo.tziomakas.movies.ui.main.MainActivity;

public class DetailActivity extends AppCompatActivity implements DetailViewInteface, TrailersAdapter.TrailersAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Object>{
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_185 = "w185";
    private static final String TAG = "DetailActivity";

    private static final String TRAILERS_LAYOUT = "DetailsActivity.trailer.layout";
    private static final String REVIEWS_LAYOUT = "DetailsActivity.review.layout";
    private static final String LOG_TAG = "DetailActivity";


    private Cursor favoriteCursor;


    @BindView(R.id.movie_image)
    ImageView movieImageTv;

    String mImage;

    @BindView(R.id.movie_title)
    TextView movieTitleTv;

    String mTitle;

    @BindView(R.id.movie_rating)
    TextView movieRatingTv;

    double mRating;

    @BindView(R.id.movie_date)
    TextView movieDateTv;

    String mReleaseDate;

    @BindView(R.id.movie_overview)
    TextView movieOverviewTv;

    String mMovieOverview;

    @BindView(R.id.recyclerview_trailers)
    RecyclerView trailersRecyclerView;

    TrailersAdapter trailersAdapter;

    @BindView(R.id.recyclerview_reviews)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.add_favourite)
    ToggleButton toggleButton;
    private Uri uri;

    ReviewAdapter reviewAdapter;

    int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

    List<Trailers> trailersList;
    List<Reviews> reviewsList;


    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager1;

    private String mMovieId;
    DetailPresenter detailPresenter;
    Context context;
    private static final String TRAILERS_LIST = "trailers_list";
    private static final String REVIEWS_LIST = "reviews_list";
    private Parcelable savedRecyclerLayoutState;
    private Parcelable savedRecyclerLayoutState1;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);



        if(savedInstanceState == null) {
            trailersList = new ArrayList<>();
            reviewsList = new ArrayList<>();

            setMVP();

            setUpViews();
            detailPresenter.getMovieDetails();
            detailPresenter.getMovieTrailers();
            detailPresenter.getMovieReviews();

        }else{



            if (savedInstanceState.containsKey("title")
                    && savedInstanceState.containsKey("rating")
                    && savedInstanceState.containsKey("date")
                    && savedInstanceState.containsKey("overview")
                    && savedInstanceState.containsKey("image"))
                {

                String oldTitle = savedInstanceState.getString("title");
                String oldRating = savedInstanceState.getString("rating");
                String oldDate = savedInstanceState.getString("date");
                String oldOverview = savedInstanceState.getString("overview");
                String oldImage = savedInstanceState.getString("image");

                movieTitleTv.setText(oldTitle);
                movieRatingTv.setText(oldRating);
                movieDateTv.setText(oldDate);
                movieOverviewTv.setText(oldOverview);
                Picasso.with(getApplicationContext()).load(IMAGE_URL + IMAGE_SIZE_185 + oldImage).into(movieImageTv);

            }
            trailersList = savedInstanceState.getParcelableArrayList(TRAILERS_LIST);
            reviewsList = savedInstanceState.getParcelableArrayList(REVIEWS_LIST);




            setMVP();

            setUpViews();
            detailPresenter.getMovieDetails();
            detailPresenter.getMovieTrailers();
            detailPresenter.getMovieReviews();


        }


    }

    public void setMVP(){
        detailPresenter = new DetailPresenter(this,this);
    }

    public void setUpViews(){
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);

        mLayoutManager = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(mLayoutManager);
        trailersRecyclerView.addItemDecoration(new MyDividerItemDecoration(this,recyclerViewOrientation,16));

        mLayoutManager1 = new LinearLayoutManager(this);

        reviewsRecyclerView.setLayoutManager(mLayoutManager1);
        reviewsRecyclerView.addItemDecoration(new MyDividerItemDecoration(this,recyclerViewOrientation,16));
    }
    @Override
    public void dipsplayMovieDetails(Movie movie) {
          //movieTitleTv.setText(movieTitle);
          //movieDescriptionTv.setText(movieDesc);

          mMovieId = movie.getId();

          mImage = movie.getPosterPath();
          Picasso.with(this).load(IMAGE_URL + IMAGE_SIZE_185 + mImage).into(movieImageTv);

          mTitle = movie.getTitle();
          movieTitleTv.setText(mTitle);

          mRating = movie.getVoteAverage();
          movieRatingTv.setText("Rating: " + mRating);

          mReleaseDate = movie.getReleaseDate();
          movieDateTv.setText("Date: " + mReleaseDate);

          mMovieOverview = movie.getOverview();
          movieOverviewTv.setText(mMovieOverview);


        favoriteCursor = getContentResolver().query(FavouriteContract.FavouriteEntry.CONTENT_URI,
                null,
                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{mMovieId},
                null);


        if (favoriteCursor.getCount() > 0) {
            try {
                toggleButton.setChecked(true);
            }finally {
                favoriteCursor.close();
            }
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    Log.v(LOG_TAG, String.valueOf(isChecked));
                    /************************************************
                     * An AsyncTask for adding a movie as favourite *
                     ***********************************************/

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, mMovieId);
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH, mImage);
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE, mTitle);
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, mMovieOverview);
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE, mReleaseDate);
                            contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE, mRating);
                            //The actual insertion in the db.
                            uri = getContentResolver().insert(FavouriteContract.FavouriteEntry.CONTENT_URI, contentValues);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (uri != null) {
                                Toast.makeText(DetailActivity.this, "Movie:  " + mTitle + " was added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                } else {


                    Uri moviedIdOfFavMovie = FavouriteContract.FavouriteEntry.buildMovieUriWithId(mMovieId);


                    getContentResolver().delete(
                            moviedIdOfFavMovie,
                            null,
                            null);
                    Toast.makeText(DetailActivity.this, "Movie deleted from favourites ", Toast.LENGTH_SHORT).show();

                }

            }

        });
    }



    @Override
    public void dipsplayMovieTrailers(TrailersResponse trailersResponse) {
        Log.d(TAG, "Trailers size: " + String.valueOf(trailersResponse.getTrailers().size()));
        trailersList = trailersResponse.getTrailers();
        trailersAdapter = new TrailersAdapter(trailersList,DetailActivity.this,this);
        trailersRecyclerView.setAdapter(trailersAdapter);
    }

    @Override
    public void displayMovieReviews(ReviewsResponse reviewsResponse) {

        if(reviewsResponse != null) {
            Log.d(TAG, String.valueOf("Reviews size: " + reviewsResponse.getReviews().size()));
            reviewsList = reviewsResponse.getReviews();
            reviewAdapter = new ReviewAdapter(reviewsResponse.getReviews());
            reviewsRecyclerView.setAdapter(reviewAdapter);

        }
    }

    @Override
    public void displayError(String s) {
        showToast(s);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(DetailActivity.this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(String trailerKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        String currentTitle = movieTitleTv.getText().toString();
        String currentRating = movieRatingTv.getText().toString();
        String currentDate = movieDateTv.getText().toString();
        String currentOverview = movieOverviewTv.getText().toString();



        outState.putString("title", currentTitle);
        outState.putString("rating", String.valueOf(currentRating));
        outState.putString("date", currentDate);
        outState.putString("overview", currentOverview);
        outState.putString("image", mImage);

        outState.putParcelable(TRAILERS_LAYOUT,mLayoutManager.onSaveInstanceState());
        outState.putParcelable(REVIEWS_LAYOUT, mLayoutManager1.onSaveInstanceState());

        outState.putParcelableArrayList(TRAILERS_LIST, (ArrayList<? extends Parcelable>) trailersList);
        outState.putParcelableArrayList(REVIEWS_LIST, (ArrayList<? extends Parcelable>) reviewsList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){

            savedRecyclerLayoutState = savedInstanceState.getParcelable(REVIEWS_LAYOUT);
            reviewsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

            savedRecyclerLayoutState1 = savedInstanceState.getParcelable(TRAILERS_LAYOUT);
            trailersRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState1);

        }
    }


    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }
}
