package com.kaizencoder.newzify.core.domain.usecases

import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.repository.HeadlinesRepository
import com.kaizencoder.newzify.core.domain.repository.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHeadlinesUseCase @Inject constructor(
    private val headlinesRepository: HeadlinesRepository,
    private val logger: Logger) {

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
            logger.e(ex, "IllegalStateException in GetHeadlinesUseCase")
            emit(GetHeadlinesUseCaseResult.Error("Unexpected application state. Please try again."))
        }catch (ex: IllegalArgumentException){
            logger.e(ex, "IllegalArgumentException in GetHeadlinesUseCase")
            emit(GetHeadlinesUseCaseResult.Error("Invalid data provided. Please try again."))
        } catch (ex: Exception){
            logger.e(ex, "Exception in GetHeadlinesUseCase")
            emit(GetHeadlinesUseCaseResult.Error("Something is not right. Please try again."))
        }
    }

}
