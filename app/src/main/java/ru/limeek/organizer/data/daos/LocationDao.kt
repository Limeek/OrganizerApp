package ru.limeek.organizer.data.daos

import androidx.room.*
import ru.limeek.organizer.domain.entities.location.Location

@Dao
interface LocationDao {
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
}