package theo.tziomakas.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by theodosiostziomakas on 29/01/2018.
 */

public class FavouriteContract {



    public static final String CONTENT_AUTHORITY = "theo.tziomakas.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";

    public static final class FavouriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE)
                .build();

        public static final String TABLE_NAME = "favourite";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";

        public static final String COLUMN_MOVIE_TITLE = "title";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static Uri buildMovieUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

    }

}
