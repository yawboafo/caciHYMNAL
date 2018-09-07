package com.truelight.cacihymn.ModelsDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.truelight.cacihymn.Models.FavoriteHymn;

import java.util.List;

@Dao
public interface FavoriteHymnDao {

    @Insert
    void insert(FavoriteHymn hymn);

    @Update
    void update(FavoriteHymn... hymns);

    @Delete
    void delete(FavoriteHymn... hymns);


    @Query("SELECT * FROM favoriteHymn")
    List<FavoriteHymn> getAllFavoriteHymns();

    /**@Query("SELECT * FROM Product WHERE ProductID=:productID")
    LiveData<List<Product>> findProductByID(final int productID);
     **/
    @Query("SELECT * FROM FavoriteHymn WHERE id=:id")
    FavoriteHymn findFavoriteHymnByIDNow(final int id);
}
