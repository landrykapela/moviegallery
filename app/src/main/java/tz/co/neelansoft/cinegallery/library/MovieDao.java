package tz.co.neelansoft.cinegallery.library;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<Movie> getMovieWithId(int movieId);
    @Insert
    void addToFavorites(Movie movie);

    @Delete
    void removeFromFavorites(Movie movie);


}
