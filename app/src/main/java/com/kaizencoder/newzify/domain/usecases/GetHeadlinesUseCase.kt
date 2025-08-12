package com.kaizencoder.newzify.domain.usecases

import android.util.Log
import androidx.paging.PagingData
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class GetHeadlinesUseCase @Inject constructor(private val headlinesRepository: HeadlinesRepository) {

    sealed class GetHeadlinesUseCaseResult {
        object Loading : GetHeadlinesUseCaseResult()
        data class Success(val articles: List<Article>) : GetHeadlinesUseCaseResult()
        data class Error(val message: String) : GetHeadlinesUseCaseResult()
    }

    fun execute(): Flow<GetHeadlinesUseCaseResult> = flow {
        emit(GetHeadlinesUseCaseResult.Loading)
        @Suppress("TooGenericExceptionCaught")
        try {
            headlinesRepository.getHeadlines()
                .collect { dataResult ->
                    when(dataResult){
                        is DataResult.Success -> emit(GetHeadlinesUseCaseResult.Success(dataResult.data))
                        is DataResult.NetworkError -> emit(GetHeadlinesUseCaseResult.Error("Network Error"))
                        is DataResult.CacheError -> emit(GetHeadlinesUseCaseResult.Error("Cache Error"))
                        is DataResult.GenericError -> emit(GetHeadlinesUseCaseResult.Error(dataResult.message))
                        is DataResult.UnknownError -> emit(
                            GetHeadlinesUseCaseResult.Error(
                                "Something is not right. Please try again."
                            )
                        )
                    }
                }
        }catch (ex: IllegalStateException){
            Timber.e(ex, "IllegalStateException in GetHeadlinesUseCase")
            emit(GetHeadlinesUseCaseResult.Error("Unexpected application state. Please try again."))
        }catch (ex: IllegalArgumentException){
            Timber.e(ex, "IllegalArgumentException in GetHeadlinesUseCase")
            emit(GetHeadlinesUseCaseResult.Error("Invalid data provided. Please try again."))
        } catch (ex: Exception){
            emit(GetHeadlinesUseCaseResult.Error(ex.localizedMessage ?: "Something is not right. Please try again."))
        }
    }

}
