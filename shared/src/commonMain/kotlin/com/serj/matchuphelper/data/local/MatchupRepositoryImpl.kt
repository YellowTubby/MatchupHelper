package com.serj.matchuphelper.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.serj.matchuphelper.db.MatchupDatabase
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.GamePhase
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.InsightCategory
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.MatchupReview
import com.serj.matchuphelper.domain.model.Outcome
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.MatchupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class MatchupRepositoryImpl(
    private val database: MatchupDatabase,
    private val json: Json,
) : MatchupRepository {

    private val matchupQueries get() = database.matchupQueries
    private val reviewQueries get() = database.matchupReviewQueries
    private val insightQueries get() = database.insightQueries

    override fun getMatchupsForChampion(championId: String): Flow<List<Matchup>> {
        return matchupQueries.selectByChampion(championId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getMatchupsByRole(role: Role): Flow<List<Matchup>> {
        return matchupQueries.selectByRole(role.name)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getRecentReviews(limit: Int): Flow<List<MatchupReview>> {
        return reviewQueries.selectRecent(limit.toLong())
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getOrCreateMatchup(
        yourChampionId: String,
        enemyChampionId: String,
        role: Role,
    ): Matchup {
        val existing = matchupQueries
            .selectByPairing(yourChampionId, enemyChampionId, role.name)
            .executeAsOneOrNull()

        if (existing != null) return existing.toDomain()

        matchupQueries.insert(yourChampionId, enemyChampionId, role.name)

        return matchupQueries
            .selectByPairing(yourChampionId, enemyChampionId, role.name)
            .executeAsOne()
            .toDomain()
    }

    override suspend fun saveReview(review: MatchupReview): Long {
        reviewQueries.insert(
            matchupId = review.matchupId,
            timestamp = review.timestampEpochMillis,
            outcome = review.outcome.name,
            difficultyRating = review.difficultyRating.name,
            conversationJson = review.conversationJson,
            personalNotes = review.personalNotes,
        )

        val reviewId = reviewQueries.selectLastInsertId().executeAsOne()

        val count = reviewQueries.countByMatchup(review.matchupId).executeAsOne()
        matchupQueries.updateAggregated(
            aggregatedDifficulty = review.difficultyRating.name,
            keyTipsJson = "[]",
            reviewCount = count,
            id = review.matchupId,
        )

        return reviewId
    }

    override suspend fun getInsightsForMatchup(matchupId: Long): List<Insight> {
        return insightQueries.selectByMatchup(matchupId)
            .executeAsList()
            .map { it.toDomain() }
    }

    override suspend fun saveInsights(insights: List<Insight>) {
        database.transaction {
            insights.forEach { insight ->
                insightQueries.insert(
                    reviewId = insight.reviewId,
                    matchupId = insight.matchupId,
                    category = insight.category.name,
                    text = insight.text,
                    gamePhase = insight.gamePhase?.name,
                )
            }
        }
    }

    override suspend fun getReviewsForMatchup(matchupId: Long): List<MatchupReview> {
        return reviewQueries.selectByMatchup(matchupId)
            .executeAsList()
            .map { it.toDomain() }
    }

    private fun com.serj.matchuphelper.db.Matchup.toDomain(): Matchup {
        return Matchup(
            id = id,
            yourChampionId = yourChampionId,
            enemyChampionId = enemyChampionId,
            role = Role.valueOf(role),
            aggregatedDifficulty = aggregatedDifficulty?.let { Difficulty.valueOf(it) },
            keyTips = emptyList(),
            reviewCount = reviewCount.toInt(),
        )
    }

    private fun com.serj.matchuphelper.db.MatchupReview.toDomain(): MatchupReview {
        return MatchupReview(
            id = id,
            matchupId = matchupId,
            timestampEpochMillis = timestamp,
            outcome = Outcome.valueOf(outcome),
            difficultyRating = Difficulty.valueOf(difficultyRating),
            conversationJson = conversationJson,
            personalNotes = personalNotes,
        )
    }

    private fun com.serj.matchuphelper.db.Insight.toDomain(): Insight {
        return Insight(
            id = id,
            reviewId = reviewId,
            matchupId = matchupId,
            category = InsightCategory.valueOf(category),
            text = text,
            gamePhase = gamePhase?.let { GamePhase.valueOf(it) },
        )
    }
}
