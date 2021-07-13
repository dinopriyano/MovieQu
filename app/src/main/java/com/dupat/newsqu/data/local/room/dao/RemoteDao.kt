package com.dupat.newsqu.data.local.room.dao

import android.icu.text.CaseMap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dupat.newsqu.data.local.entities.RemoteKeyEntity

@Dao
interface RemoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE title = :title")
    suspend fun remoteKeyByQuery(title: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteByQuery()

}