package theo.tziomakas.movies.ui.main;



import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import theo.tziomakas.movies.models.Movie;
import theo.tziomakas.movies.models.MovieResponse;
import theo.tziomakas.movies.network.ApiClient;
import theo.tziomakas.movies.network.ApiInterface;

public class MainPresenter implements MainPresenterInterface {
    MainViewInterface mainViewInterface;
    private String TAG = "MainPresenter";
    Context context;
    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    //String movieId = preferences.getString("movieId", "");

    public MainPresenter(MainViewInterface mainViewInterface){
        this.mainViewInterface = mainViewInterface;
    }
    @Override
    public void getPopularMovies() {
        getPopularMoviesObservable().subscribeWith(getObserver());
    }

    @Override
    public void getTopRatedMovies() {
        getTopRatedObservable().subscribeWith(getObserver());
    }



    public Observable<MovieResponse> getPopularMoviesObservable(){

        return ApiClient.getRetrofit().create(ApiInterface.class)
                .getPopularMoviesResponse("004cbaf19212094e32aa9ef6f6577f22")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<MovieResponse> getTopRatedObservable(){

        return ApiClient.getRetrofit().create(ApiInterface.class)
                .getTopRatedMoviesResponse("004cbaf19212094e32aa9ef6f6577f22")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public DisposableObserver<MovieResponse> getObserver(){
        return new DisposableObserver<MovieResponse>() {
            @Override
            public void onNext(MovieResponse movieResponse) {
                Log.d(TAG,"OnNext"+movieResponse.getTotalResults());
                mainViewInterface.displayMovies(movieResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                mainViewInterface.displayError("Error fetching Movie Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
}
