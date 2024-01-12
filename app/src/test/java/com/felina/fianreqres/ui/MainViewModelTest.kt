package com.felina.fianreqres.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.felina.fianreqres.DataDummy
import com.felina.fianreqres.MainDispatcherRule
import com.felina.fianreqres.adapter.UserListAdapter
import com.felina.fianreqres.data.UserRepository
import com.felina.fianreqres.getOrAwaitValue
import com.felina.fianreqres.network.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
//        val data: PagingData<DataItem> = PagingData.from(dummyQuote)
        val data: PagingData<DataItem> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<DataItem>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getUsers()).thenReturn(expectedQuote)
        
        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<DataItem> = mainViewModel.user.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = UserListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<DataItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<DataItem>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getUsers()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<DataItem> = mainViewModel.user.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = UserListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class QuotePagingSource : PagingSource<Int, LiveData<List<DataItem>>>() {
    companion object {
        fun snapshot(items: List<DataItem>): PagingData<DataItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<DataItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<DataItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}