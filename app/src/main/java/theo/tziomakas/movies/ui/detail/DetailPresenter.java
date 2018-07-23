package theo.tziomakas.movies.ui.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import theo.tziomakas.movies.models.Movie;
import theo.tziomakas.movies.models.MovieResponse;
import theo.tziomakas.movies.models.ReviewsResponse;
import theo.tziomakas.movies.models.Trailers;
import theo.tziomakas.movies.models.TrailersResponse;
import theo.tziomakas.movies.network.ApiClient;
import theo.tziomakas.movies.network.ApiInterface;

public class DetailPresenter implements DetailPresenterInterface {

    DetailViewInteface detailViewInteface;
    private String TAG = "DetailPresenter";
    private SharedPreferences preferences;
    private String movieId;

    public DetailPresenter(DetailViewInteface detailViewInteface,Context context){
        this.detailViewInteface = detailViewInteface;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        movieId = preferences.getString("movieId", "");

    }




    @Override
    public void getMovieDetails() {

        getMovieDetailsObservable().subscribeWith(getObserver());
    }

    @Override
    public void getMovieTrailers() {
        getMovieTrailersObservable().subscribeWith(getTrailersObservers());
    }

    @Override
    public void getMovieReviews() {
        getMovieReviewsObservable().subscribeWith(getReviewObservers());
    }

    public Observable<Movie> getMovieDetailsObservable(){

        return ApiClient.getRetrofit().create(ApiInterface.class)
                .getMovieDetails(movieId,"004cbaf19212094e32aa9ef6f6577f22")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<TrailersResponse> getMovieTrailersObservable(){

        return ApiClient.getRetrofit().create(ApiInterface.class)
                .getMovieTrailers(movieId,"004cbaf19212094e32aa9ef6f6577f22")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<ReviewsResponse> getMovieReviewsObservable(){

        return ApiClient.getRetrofit().create(ApiInterface.class)
                .getMovieReviews(movieId,"004cbaf19212094e32aa9ef6f6577f22")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public DisposableObserver<Movie> getObserver(){
        return new DisposableObserver<Movie>() {
            @Override
            public void onNext(Movie movie) {
                Log.d(TAG,"OnNext"+movie.getTitle());
                detailViewInteface.dipsplayMovieDetails(movie);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                detailViewInteface.displayError("Error fetching Movie Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
    public DisposableObserver<TrailersResponse> getTrailersObservers(){
        return new DisposableObserver<TrailersResponse>() {
            @Override
            public void onNext(TrailersResponse trailers) {
                Log.d(TAG,"OnNext"+trailers.getTrailers());
                detailViewInteface.dipsplayMovieTrailers(trailers);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                detailViewInteface.displayError("Error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
    public DisposableObserver<ReviewsResponse> getReviewObservers(){
        return new DisposableObserver<ReviewsResponse>() {
            @Override
            public void onNext(ReviewsResponse reviewsResponse) {
                Log.d(TAG,"OnNext"+reviewsResponse.getReviews());
                detailViewInteface.displayMovieReviews(reviewsResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                detailViewInteface.displayError("Error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
}
