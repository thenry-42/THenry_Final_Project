package com.example.finalgameproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.fragment_results,
            container,
            false
        )

        // Retrieve the information passed from GameFragment
        val shopName = arguments?.getString("shopName") ?: "Your Shop"
        val finalScore = arguments?.getInt("finalScore") ?: 0
        val gameWon = arguments?.getBoolean("gameWon") ?: false

        // Find the Results screen TextViews
        val resultsTitle = view.findViewById<TextView>(R.id.resultsTitle)
        val resultsShopName = view.findViewById<TextView>(R.id.resultsShopName)
        val finalScoreText = view.findViewById<TextView>(R.id.finalScoreText)
        val resultsMessage = view.findViewById<TextView>(R.id.resultsMessage)
        val resultsIcon = view.findViewById<TextView>(R.id.resultsIcon)

        // Display the player's shop name and final score
        resultsShopName.text = shopName
        finalScoreText.text = "Final Score: $finalScore"

        // Display a different title depending on how the game ended
        if (gameWon) {
            resultsIcon.text = "˗ˏˋ ★ ˎˊ˗"
            resultsTitle.text = "Shift Complete!"
        } else {
            resultsIcon.text = "☹"
            resultsTitle.text = "Game Over!"
        }

        // Display a message based on the player's final score
        resultsMessage.text = when (finalScore) {
            50 -> "Amazing! You're a smoothie superstar!"
            30, 40 -> "Great job! Your customers are happy!"
            10, 20 -> "Nice effort! Keep practicing!"
            else -> "Every smoothie expert starts somewhere!"
        }

        // Find the Play Again button
        val playAgainButton = view.findViewById<Button>(R.id.playAgainButton)


        // Find the Share Results button
        val shareResultsButton =
            view.findViewById<Button>(R.id.shareResultsButton)

        // Share the player's final score
        shareResultsButton.setOnClickListener {
            val shareMessage =
                "I scored $finalScore points at $shopName in Snack Shack Shuffle!"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(
                Intent.createChooser(shareIntent, "Share your results")
            )
        }

        // Return to the Welcome screen
        playAgainButton.setOnClickListener {
            val welcomeFragment = WelcomeFragment()
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, welcomeFragment)
                .commit()
        }
        return view
    }
}