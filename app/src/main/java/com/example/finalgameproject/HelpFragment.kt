
package com.example.finalgameproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.fragment_help,
            container,
            false
        )

        // Find the Back to Welcome button
        val backToWelcomeButton =
            view.findViewById<Button>(R.id.backToWelcomeButton)

        // Return to the previous screen
        backToWelcomeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
