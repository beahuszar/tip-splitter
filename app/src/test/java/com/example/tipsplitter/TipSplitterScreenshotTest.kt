package com.example.tipsplitter

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xxhdpi")
class TipSplitterScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val testName = TestName()

    // <classname>__<name>__<label>.<ext>, the file name convention the Bitrise Export step links by.
    private fun attachmentPath(label: String) =
        "build/outputs/roborazzi/${javaClass.name}__${testName.methodName}__$label.png"

    @Test
    fun emptyState() {
        composeRule.setContent { MaterialTheme { TipSplitterScreen() } }

        composeRule.onNodeWithTag("perPerson").assertTextEquals("Each pays: 0.00")
        composeRule.onRoot().captureRoboImage(attachmentPath("1"))
    }

    @Test
    fun billSplitBetweenThree() {
        composeRule.setContent { MaterialTheme { TipSplitterScreen() } }

        composeRule.onNodeWithTag("bill").performTextInput("120")
        composeRule.onNodeWithTag("plus").performClick()
        composeRule.onNodeWithTag("plus").performClick()

        composeRule.onNodeWithTag("perPerson").assertTextEquals("Each pays: 46.00")
        composeRule.onRoot().captureRoboImage(attachmentPath("1"))
    }
}
