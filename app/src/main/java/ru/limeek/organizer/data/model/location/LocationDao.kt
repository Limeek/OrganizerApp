package ru.limeek.organizer.data.model.location

import androidx.room.*

@Dao
abstract class LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(location : Location?) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(location: Location?)

    @Delete
    abstract suspend fun delete(location: Location?)

    @Query("select * from locations where created_by_user = 1")
    abstract suspend fun getUserCreatedLocations() : List<Location>

    @Query("select * from locations where location_id = :id")
    abstract suspend fun getLocationById(id: Long) : Location?

    suspend fun upsert(location: Location): Long{
        return if(getLocationById(location.id) == null)
            insert(location)
        else {
            update(location)
            location.id
        }
    }
}