package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import timber.log.Timber
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(
    private val savedArticlesRepository: SavedArticlesRepository
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
            Timber.e(ex,"Error getting saved articles")
            return GetSavedArticlesUseCaseResult.Error("Something is not right. Please try again.")
        }
    }
}
