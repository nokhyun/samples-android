package com.nokhyun.fakepaging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nokhyun.fakepaging.PassengerMapper.toEntity
import kotlinx.coroutines.flow.first
import java.net.UnknownHostException

internal class PassengerPagingSource(
    private val fakePagingRemoteDataSource: FakePagingRemoteDataSource
) : PagingSource<Int, com.nokhyun.passenger.Passenger>() {

    override fun getRefreshKey(state: PagingState<Int, com.nokhyun.passenger.Passenger>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.nokhyun.passenger.Passenger> {
        val key = params.key ?: 0

        return try {
            val result = fakePagingRemoteDataSource.fetchPassenger(
                PassengerData(
                    page = key,
                    size = 10
                )
            ).first()

            LoadResult.Page(
                data = result.data.map { it.toEntity() },
                prevKey = null,
                nextKey = key + 1
            )
        } catch (e: Exception) {
            Log.e("asdljlsa", "paging Error:$e")
            LoadResult.Error(e)
        }
    }
}