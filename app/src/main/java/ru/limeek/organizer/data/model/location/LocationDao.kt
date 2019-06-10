package ru.limeek.organizer.data.model.location

import androidx.room.*
import io.reactivex.Flowable

@Dao
abstract class LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(location : Location?) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(location: Location?)

    @Delete
    abstract fun delete(location: Location?)

    @Query("select * from locations where created_by_user = 1")
    abstract fun getUserCreatedLocations() : Flowable<List<Location>>

    @Query("select * from locations where location_id = :id")
    abstract fun getLocationById(id: Long) : Flowable<Location>

    fun upsert(location: Location) : Long{
        var id = insert(location)
        if(id < 0){
            update(location)
            id = location.id
        }
        return id
    }
}