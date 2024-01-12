package com.felina.fianreqres.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.felina.fianreqres.database.UserDatabase
import com.felina.fianreqres.network.ApiService
import com.felina.fianreqres.network.DataItem
import com.felina.fianreqres.network.Support
import com.felina.fianreqres.network.UserResponse

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class UserRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: UserDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        UserDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = UserRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, DataItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {

    override suspend fun getUsers(page: Int, size: Int): UserResponse {
        val items: MutableList<DataItem> = arrayListOf()

        for (i in 0..100) {
            val quote = DataItem(
                i.toString(),
                1,
                "quote $i",
                "sasd",
                "assas",

            )
            items.add(quote)
        }
        val user = UserResponse(
            1,
            1,
            items,
            1,
            1,
            Support("aa","aa")
        )

        return user
    }
}