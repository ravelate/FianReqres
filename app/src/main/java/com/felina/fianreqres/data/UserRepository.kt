package com.felina.fianreqres.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.felina.fianreqres.database.UserDatabase
import com.felina.fianreqres.network.ApiService
import com.felina.fianreqres.network.DataItem

class UserRepository(private val userDatabase: UserDatabase, private val apiService: ApiService) {
    fun getUsers(): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = UserRemoteMediator(userDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                userDatabase.quoteDao().getAllUsers()
            }
        ).liveData
    }
}