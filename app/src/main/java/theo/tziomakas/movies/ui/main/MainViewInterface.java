package theo.tziomakas.movies.ui.main;

import theo.tziomakas.movies.models.MovieResponse;

public interface MainViewInterface {
    void showToast(String s);
    void displayMovies(MovieResponse moviesResponse);
    void displayError(String s);
}
