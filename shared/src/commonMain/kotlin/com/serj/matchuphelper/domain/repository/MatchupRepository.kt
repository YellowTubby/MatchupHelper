package com.serj.matchuphelper.domain.repository

import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.MatchupReview
import com.serj.matchuphelper.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface MatchupRepository {
    fun getMatchupsForChampion(championId: String): Flow<List<Matchup>>
    fun getMatchupsByRole(role: Role): Flow<List<Matchup>>
    fun getRecentReviews(limit: Int): Flow<List<MatchupReview>>
    suspend fun getOrCreateMatchup(yourChampionId: String, enemyChampionId: String, role: Role): Matchup
    suspend fun saveReview(review: MatchupReview): Long
    suspend fun getInsightsForMatchup(matchupId: Long): List<Insight>
    suspend fun saveInsights(insights: List<Insight>)
    suspend fun getReviewsForMatchup(matchupId: Long): List<MatchupReview>
}
