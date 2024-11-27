package com.ti4all.agendaapp

import android.app.Application
import androidx.room.Room

class MyBooksListApplication : Application() {

    lateinit var database : MyBooksListDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, MyBooksListDatabase::class.java
            , "MyBooksListDb")
            .build()
    }

    companion object {
        lateinit var instance: MyBooksListApplication
            private set
    }
}