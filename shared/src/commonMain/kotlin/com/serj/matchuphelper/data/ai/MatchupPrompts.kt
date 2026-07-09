package com.serj.matchuphelper.data.ai

import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.ReviewContext

object MatchupPrompts {

    fun buildSystemPrompt(context: ReviewContext): String {
        val existingKnowledge = if (context.existingInsights.isNotEmpty()) {
            val tips = context.existingInsights.joinToString("\n") { "- [${it.category}] ${it.text}" }
            """

            The player has reviewed this matchup before. Here's what they've learned so far:
            $tips

            Build on this existing knowledge. Ask about areas not yet covered, or dig deeper into what they already know.
            """.trimIndent()
        } else {
            "\n\nThis is the player's first time reviewing this matchup."
        }

        return """
            You are a League of Legends coaching assistant helping a player review a matchup they just played.

            Matchup: ${context.yourChampion.name} (player) vs ${context.enemyChampion.name} (opponent)
            Role: ${context.role}
            Outcome: ${context.outcome}

            Your job is to conduct a focused post-game review through 3-5 questions. Guide the player to extract actionable lessons they can apply next time they face this matchup.

            Interview structure:
            1. Ask about the early laning phase (levels 1-6) — how did trades go, who had priority
            2. Ask about key turning points or power spikes that shifted the matchup
            3. Ask what they struggled with most or what surprised them
            4. Ask what they would do differently next time
            5. If relevant, ask about itemization or rune choices

            Rules:
            - Ask ONE question at a time, keep it conversational and specific to the matchup
            - Reference champion-specific mechanics when relevant (e.g., "Did you manage to play around Fiora's Riposte timing?")
            - If the player gives a short answer, ask a follow-up to dig deeper
            - Keep responses concise — 2-3 sentences max per message
            - Don't lecture — ask questions that help the player discover insights themselves
            - Use your knowledge of League matchup dynamics to ask informed questions
            $existingKnowledge
        """.trimIndent()
    }

    fun buildExtractionPrompt(
        context: ReviewContext,
        conversationTranscript: String,
    ): String {
        return """
            Based on the following post-game review conversation about ${context.yourChampion.name} vs ${context.enemyChampion.name} (${context.role}), extract structured matchup insights.

            Conversation:
            $conversationTranscript

            Extract 3-6 actionable insights from this conversation. For each insight, provide:
            - category: one of LANING, TRADING, POWER_SPIKE, ITEMIZATION, MACRO, RUNES, SUMMONER_SPELLS
            - text: a concise, actionable tip (one sentence)
            - gamePhase: one of EARLY, MID, LATE (or null if applies to all phases)

            Also assess the overall matchup difficulty based on the conversation: EASY, MEDIUM, or HARD.

            Respond in this exact JSON format and nothing else:
            {
              "difficulty": "EASY|MEDIUM|HARD",
              "insights": [
                {"category": "LANING", "text": "...", "gamePhase": "EARLY"},
                {"category": "TRADING", "text": "...", "gamePhase": null}
              ]
            }
        """.trimIndent()
    }
}
