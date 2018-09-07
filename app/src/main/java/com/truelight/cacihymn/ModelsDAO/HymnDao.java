package com.truelight.cacihymn.ModelsDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.truelight.cacihymn.Models.FavoriteHymn;
import com.truelight.cacihymn.Models.Hymn;

import java.util.List;



@Dao
public interface HymnDao {

    @Insert
    void insert(Hymn hymn);

    @Update
    void update(Hymn... hymns);

    @Delete
    void delete(Hymn... hymns);


    @Query("SELECT * FROM hymn")
    List<Hymn> getAllHymns();

    /**@Query("SELECT * FROM Product WHERE ProductID=:productID")
    LiveData<List<Product>> findProductByID(final int productID);
     **/
    @Query("SELECT * FROM Hymn WHERE id=:id")
    Hymn findHymnByIDNow(final int id);







}
