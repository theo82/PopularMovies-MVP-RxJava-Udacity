package theo.tziomakas.movies.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import theo.tziomakas.movies.models.Movie;
import theo.tziomakas.movies.models.MovieResponse;
import theo.tziomakas.movies.models.ReviewsResponse;
import theo.tziomakas.movies.models.Trailers;
import theo.tziomakas.movies.models.TrailersResponse;

public interface ApiInterface {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMoviesResponse(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMoviesResponse(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Observable<Movie> getMovieDetails(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Observable<TrailersResponse> getMovieTrailers(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
