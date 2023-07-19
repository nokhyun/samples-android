package com.nokhyun.fakepaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nokhyun.fakepaging.PassengerMapper.toEntity
import kotlinx.coroutines.flow.first

internal class PassengerPagingSource(
    private val fakePagingRemoteDataSource: FakePagingRemoteDataSource
) : PagingSource<Int, com.nokhyun.passenger.Passenger>() {

    override fun getRefreshKey(state: PagingState<Int, com.nokhyun.passenger.Passenger>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.nokhyun.passenger.Passenger> {
        val key = params.key ?: 0

        val result = fakePagingRemoteDataSource.fetchPassenger(
            PassengerData(
                page = key,
                size = 10
            )
        ).first()

        return try {
            LoadResult.Page(
                data = result.data.map { it.toEntity() },
                prevKey = null,
                nextKey = key + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}