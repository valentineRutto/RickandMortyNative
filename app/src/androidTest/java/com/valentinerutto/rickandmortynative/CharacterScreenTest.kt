package com.valentinerutto.rickandmortynative

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class CharacterScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun characterScreen_showsSearchAndFilters() {
        composeRule.onNodeWithText("Portal Explorer").assertIsDisplayed()
        composeRule.onNodeWithText("Search by name...").assertIsDisplayed()
        composeRule.onNodeWithText("STATUS").assertIsDisplayed()
        composeRule.onNodeWithText("Alive").assertIsDisplayed()
        composeRule.onNodeWithText("Dead").assertIsDisplayed()
        composeRule.onNodeWithText("Unknown").assertIsDisplayed()
        composeRule.onNodeWithText("SPECIES").assertIsDisplayed()
        composeRule.onNodeWithText("Human").assertIsDisplayed()
        composeRule.onNodeWithText("Alien").assertIsDisplayed()
    }
}
