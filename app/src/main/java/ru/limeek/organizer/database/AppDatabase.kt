package ru.limeek.organizer.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.model.location.LocationDao

@Database(entities = arrayOf(Event::class, Location::class),version = 6,exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table events add event_remind TEXT NOT NULL DEFAULT '' ")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table events rename to old_events")

                database.execSQL(
                        "CREATE TABLE `events` " +
                                "('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                " 'date_time' TEXT NOT NULL," +
                                " 'summary' TEXT NOT NULL," +
                                " 'remind' TEXT NOT NULL DEFAULT '')"
                )

                database.execSQL(
                        "INSERT INTO events(id,date_time,summary,remind) " +
                                "SELECT id,event_date,event_summary,event_remind " +
                                "FROM old_events"
                )

                database.execSQL("DROP TABLE old_events")

                database.execSQL(
                        "CREATE TABLE 'locations'"+
                                "('location_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "'latitude' REAL NOT NULL," +
                                "'longitude' REAL NOT NULL," +
                                "'event_id' INTEGER NOT NULL," +
                                "'location_name' TEXT NOT NULL, " +
                                "FOREIGN KEY(event_id) REFERENCES events(id) ON UPDATE CASCADE ON DELETE CASCADE) "
                )

                database.execSQL("CREATE INDEX index_locations_event_id ON locations(event_id)")
            }
        }
        val MIGRATION_4_5 = object : Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table locations add location_address TEXT NOT NULL DEFAULT ''")
            }
        }
        val MIGRATION_5_6 = object : Migration(5,6){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table locations rename to old_locations")
                database.execSQL("create table locations" +
                        "('location_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "'latitude' REAL NOT NULL," +
                        "'longitude' REAL NOT NULL," +
                        "'location_name' TEXT NOT NULL," +
                        "'location_address' TEXT NOT NULL, " +
                        "'created_by_user' INTEGER NOT NULL DEFAULT 0)"
                )
                database.execSQL("insert into locations(location_id,latitude,longitude,location_name,location_address) " +
                        "select location_id,latitude,longitude,location_name,location_address " +
                        "from old_locations")
                database.execSQL("drop table old_locations")
                database.execSQL("alter table events rename to old_events")
                database.execSQL("create table events(" +
                        " 'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        " 'date_time' TEXT NOT NULL," +
                        " 'summary' TEXT NOT NULL," +
                        " 'remind' TEXT NOT NULL DEFAULT ''," +
                        " 'event_location_id' INTEGER," +
                        " foreign key(event_location_id) references locations(location_id)"+
                        ")")
                database.execSQL("insert into events(id, date_time, summary, remind) " +
                        "select id, date_time, summary, remind " +
                        "from old_events")
                database.execSQL("drop table old_events")
                database.execSQL("create index index_events_event_location_id on events(event_location_id)")
            }
        }
    }

    abstract fun eventDao() : EventDao
    abstract fun locationDao() : LocationDao

    override fun close() {
        super.close()
    }
}