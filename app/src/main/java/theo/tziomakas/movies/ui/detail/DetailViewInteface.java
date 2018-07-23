package theo.tziomakas.movies.ui.detail;

import theo.tziomakas.movies.models.Movie;
import theo.tziomakas.movies.models.ReviewsResponse;
import theo.tziomakas.movies.models.Trailers;
import theo.tziomakas.movies.models.TrailersResponse;

public interface DetailViewInteface {
    void dipsplayMovieDetails(Movie movie);
    void dipsplayMovieTrailers(TrailersResponse trailersResponse);
    void displayMovieReviews(ReviewsResponse reviewsResponse);

    void displayError(String s);
    void showToast(String s);

}
