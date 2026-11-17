
package com.example.finalgameproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Find the welcome screen views
        val startGameButton = view.findViewById<Button>(R.id.startGameButton)
        val howToPlayButton = view.findViewById<Button>(R.id.howToPlayButton)
        val shopNameInput = view.findViewById<EditText>(R.id.shopNameInput)
        val gameTitleText = view.findViewById<TextView>(R.id.gameTitleText)

        // Count how many times the player taps the title
        var titleTapCount = 0
        gameTitleText.setOnClickListener {
            titleTapCount++
            // Unlock the Easter egg after five taps
            if (titleTapCount == 5) {
                Toast.makeText(
                    requireContext(),
                    "Secret Smoothie Club unlocked! You are a berry special employee! 🍓",
                    Toast.LENGTH_LONG
                ).show()
                titleTapCount = 0
            }
        }

        // Start game button
        startGameButton.setOnClickListener {
            val shopName = shopNameInput.text.toString().trim()

            if (shopName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a shop name!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val gameFragment = GameFragment()

                val bundle = Bundle()
                bundle.putString("shopName", shopName)
                gameFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main, gameFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // Open the how to play screen
        howToPlayButton.setOnClickListener {
            val helpFragment = HelpFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, helpFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
