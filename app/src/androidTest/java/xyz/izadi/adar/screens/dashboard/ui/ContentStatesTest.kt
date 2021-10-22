package xyz.izadi.adar.screens.dashboard.ui

import androidx.compose.material.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class ContentStatesTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_no_transaction_state_displayed() {
        // Start the app
        composeTestRule.setContent {
            Surface {
                NoTransactionsState()
            }
        }
        composeTestRule.onNodeWithText("No transactions").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Piggy Icon").assertIsDisplayed()
    }

    @Test
    fun test_loading_state_displayed() {
        // Start the app
        composeTestRule.setContent {
            Surface {
                LoadingState()
            }
        }
        composeTestRule.onNodeWithTag("ProgressIndicator").assertIsDisplayed()
    }

    @Test
    fun test_error_state_displayed() {
        // Start the app
        composeTestRule.setContent {
            Surface {
                ErrorState("Error Message")
            }
        }
        composeTestRule.onNodeWithText("Error Message").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Error Icon").assertIsDisplayed()
    }
}