package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import javax.inject.Inject

class SaveArticlesUseCase @Inject constructor(
    private val savedArticlesRepository: SavedArticlesRepository
) {

    sealed class SaveHeadlinesUseCaseResult {
        object Success : SaveHeadlinesUseCaseResult()
        data class Error(val message: String) : SaveHeadlinesUseCaseResult()
    }

    fun execute(article: Article): SaveHeadlinesUseCaseResult {
        val dataResult =  savedArticlesRepository.saveArticle(article)
        return when(dataResult){
            is DataResult.Success -> SaveHeadlinesUseCaseResult.Success
            is DataResult.CacheError -> SaveHeadlinesUseCaseResult.Error("Cache Error")
            else -> SaveHeadlinesUseCaseResult.Error("Something is not right. Please try again.")
        }
    }
}
