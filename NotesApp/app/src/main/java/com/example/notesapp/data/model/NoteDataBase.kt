package com.example.notesapp.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NoteData::class], version = 1 , exportSchema = false)
@TypeConverters(Converter::class)
 abstract class NoteDataBase : RoomDatabase(){

     abstract fun noteDao() : NoteDao

     companion object{
         @Volatile
         private var INSTACE : NoteDataBase? = null
         fun getDataBase(context: Context) : NoteDataBase{
             val tempIntance = INSTACE
             if (tempIntance != null){
                 return tempIntance
             }

             synchronized(this){
                 val instance = Room.databaseBuilder(
                     context.applicationContext,
                     NoteDataBase::class.java,
                     "note_database"
                 ).build()
                 INSTACE = instance
                 return instance
             }
         }
     }
}