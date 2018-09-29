package com.hanifkf.submission2.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.hanifkf.submission2.Model.Favorite
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx : Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteTeam.db", null, 1){

    companion object {
        private var instance: MyDatabaseOpenHelper?=null

        @Synchronized
        fun getInstance(ctx: Context) : MyDatabaseOpenHelper{
            if(instance==null){
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(Favorite.TABLE_FAVORITE,true,
                Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Favorite.EVENT_ID to TEXT + UNIQUE,
                Favorite.EVENT_DATE to TEXT,
                Favorite.HOME_TEAM to TEXT,
                Favorite.SCORE_HOME to TEXT,
                Favorite.AWAY_TEAM to TEXT,
                Favorite.AWAY_SCORE to TEXT,
                Favorite.ID_HOME to TEXT,
                Favorite.ID_AWAY to TEXT,
                Favorite.EVENT to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Favorite.TABLE_FAVORITE, true)
    }

}

val Context.database : MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)