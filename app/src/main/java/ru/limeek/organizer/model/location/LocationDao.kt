package ru.limeek.organizer.model.location

import android.arch.persistence.room.*
import io.reactivex.Flowable
import ru.limeek.organizer.app.App
import ru.limeek.organizer.model.event.Event

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