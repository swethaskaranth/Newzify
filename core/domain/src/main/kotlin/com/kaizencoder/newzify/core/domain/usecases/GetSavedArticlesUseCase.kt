package com.kaizencoder.newzify.core.domain.usecases

import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.repository.Logger
import com.kaizencoder.newzify.core.domain.repository.SavedArticlesRepository
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(
    private val savedArticlesRepository: SavedArticlesRepository,
    private val logger: Logger
) {

    sealed class GetSavedArticlesUseCaseResult {
        data class Success(val articles: List<Article>) : GetSavedArticlesUseCaseResult()
        data class Error(val message: String) : GetSavedArticlesUseCaseResult()
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun execute(): GetSavedArticlesUseCaseResult {
        try {
            val result = savedArticlesRepository.getSavedArticles()
            return when (result) {
                is DataResult.Success -> {
                    GetSavedArticlesUseCaseResult.Success(result.data)
                }

                is DataResult.CacheError -> {
                    GetSavedArticlesUseCaseResult.Error("Cache Error")
                }

                is DataResult.GenericError -> {
                    GetSavedArticlesUseCaseResult.Error(result.message)
                }
                else -> {
                    GetSavedArticlesUseCaseResult.Error("Something is not right. Please try again.")
                }
            }
        }catch (ex: Exception){
            logger.e(ex,"Error getting saved articles")
            return GetSavedArticlesUseCaseResult.Error("Something is not right. Please try again.")
        }
    }
}
