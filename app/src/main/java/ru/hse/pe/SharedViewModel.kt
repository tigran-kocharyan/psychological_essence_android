package ru.hse.pe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.RecommendationEntity

class SharedViewModel : ViewModel() {
    private var _article = MutableLiveData<ArticleEntity?>(null)
    private var _fact = MutableLiveData<FactEntity?>(null)
    private var _recommendation = MutableLiveData(
        RecommendationEntity(
            36,
            "Константин Борисов",
            "Алиенист",
            "Series",
            "2002",
            "США",
            "Действие сериала происходит в 1896г., психолог-алиенист (алиенистами в то время называли психиатров и психологов) с командой расследуют убийства. Каждый главный герой имеет свои проблемы с психическим здоровьем, абьюзом, дискриминацией и другими трудностями: полезно увидеть и происследовать вместе с сериалом, как этот опыт повлиял на то, кем стали герои.Действие сериала происходит в 1896г., психолог-алиенист (алиенистами в то время называли психиатров и психологов) с командой расследуют убийства. Каждый главный герой имеет свои проблемы с психическим здоровьем, абьюзом, дискриминацией и другими трудностями: полезно увидеть и происследовать вместе с сериалом, как этот опыт повлиял на то, кем стали герои.Действие сериала происходит в 1896г., психолог-алиенист (алиенистами в то время называли психиатров и психологов) с командой расследуют убийства. Каждый главный герой имеет свои проблемы с психическим здоровьем, абьюзом, дискриминацией и другими трудностями: полезно увидеть и происследовать вместе с сериалом, как этот опыт повлиял на то, кем стали герои.",
            listOf(
                "https://psychological-essence.github.io/images/tech.jpg",
                "https://psychological-essence.github.io/images/books.jpg",
                "https://psychological-essence.github.io/images/consent_culture.jpg"
            ),
            false
        )
    )

    var article: LiveData<ArticleEntity?> = _article
    var recommendation: LiveData<RecommendationEntity?> = _recommendation
    var fact: LiveData<FactEntity?> = _fact

    fun setArticle(article: ArticleEntity) {
        _article.value = article
    }

    fun setRecommendation(recommendation: RecommendationEntity) {
        _recommendation.value = recommendation
    }

    fun setFact(fact: FactEntity) {
        _fact.value = fact
    }
}