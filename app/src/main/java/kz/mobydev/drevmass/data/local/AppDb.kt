package kz.mobydev.drevmass.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverters
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Products

@Database(entities = [Lesson::class, Products.ProductsItem::class], version = 1)
@TypeConverters(RoomDataConverter::class)
abstract class AppDb: androidx.room.RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object{
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context:Context): AppDb{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context.applicationContext, AppDb::class.java, "app_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}