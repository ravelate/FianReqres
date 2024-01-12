package com.felina.fianreqres.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felina.fianreqres.network.DataItem

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(quote: List<DataItem>)

    @Query("SELECT * FROM user")
    fun getAllUsers(): PagingSource<Int, DataItem>

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}