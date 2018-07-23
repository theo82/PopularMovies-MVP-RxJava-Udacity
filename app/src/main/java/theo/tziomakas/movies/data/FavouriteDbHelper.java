package theo.tziomakas.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by theodosiostziomakas on 29/01/2018.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "favourite.db";

    private static final int DATABASE_VERSION = 5;

    public FavouriteDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
          /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_FAVOURITE_TABLE =

                "CREATE TABLE " + FavouriteContract.FavouriteEntry.TABLE_NAME + " (" +

                        FavouriteContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "      +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "           +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, "     +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"            +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "        +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "    +

                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, "    +


                        " UNIQUE (" + FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.FavouriteEntry.TABLE_NAME);
        onCreate(db);

    }

}
