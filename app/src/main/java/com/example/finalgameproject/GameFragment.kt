package com.example.finalgameproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class GameFragment : Fragment() {

    //declare score and heart variables
    private var score = 0
    private var hearts = 3
    private var ordersCompleted = 0
    private var currentRecipeIndex = -1
    private val selectedIngredients = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // Restore the score after a screen rotation
        score = savedInstanceState?.getInt("savedScore") ?: score
        view.findViewById<TextView>(R.id.scoreText).text = "Score: $score"

        // Restore the hearts after a screen rotation
        hearts = savedInstanceState?.getInt("savedHearts") ?: hearts
        view.findViewById<TextView>(R.id.heartsText).text = "Hearts: " + "♥ ".repeat(hearts)

        // Restore orders completed after a screen rotation
        ordersCompleted = savedInstanceState?.getInt("savedOrdersCompleted") ?: ordersCompleted
        view.findViewById<TextView>(R.id.orderProgressText).text = "Order ${ordersCompleted + 1} of 5"

        // Restore recipe after screen rotation
        currentRecipeIndex = savedInstanceState?.getInt("savedRecipeIndex") ?: currentRecipeIndex


        // Restore ingredients selected before rotation
        savedInstanceState
            ?.getStringArrayList("savedSelectedIngredients")
            ?.let { savedIngredients ->
                selectedIngredients.clear()
                selectedIngredients.addAll(savedIngredients)
            }

        // Retrieve the shop name passed from WelcomeFragment
        val shopName = arguments?.getString("shopName") ?: "Your Shop"

        // Find the shop name TextView
        val shopNameText = view.findViewById<TextView>(R.id.shopNameText)

        // Display the shop name
        shopNameText.text = "Welcome to $shopName!"

        //order tracking variables
        val maxOrders = 5

        // Stores the smoothie recipes customers can order
        val recipes = listOf(
            SmoothieRecipe(
                "Berry Buddy",
                listOf("Strawberry", "Banana", "Blueberry")
            ),
            SmoothieRecipe(
                "Tropical Twist",
                listOf("Banana", "Mango")
            ),
            SmoothieRecipe(
                "Berry Blast",
                listOf("Strawberry", "Blueberry")
            )
        )

        // Randomly select customer order
        if (currentRecipeIndex == -1) {
            currentRecipeIndex = recipes.indices.random()
        }
        var currentRecipe = recipes[currentRecipeIndex]


        // Find the customer order TextViews
        val orderDescription = view.findViewById<TextView>(R.id.orderDescription)
        val orderIngredients = view.findViewById<TextView>(R.id.orderIngredients)

        // Find progress textview
        val orderProgressText = view.findViewById<TextView>(R.id.orderProgressText)

        // Display the current customer order
        orderDescription.text = currentRecipe.name
        orderIngredients.text = currentRecipe.ingredients.joinToString(", ")

        // Open the Results screen when the game ends
        fun showResults(gameWon: Boolean) {
            val resultsFragment = ResultsFragment()
            val bundle = Bundle()
            bundle.putString("shopName", shopName)
            bundle.putInt("finalScore", score)
            bundle.putBoolean("gameWon", gameWon)

            resultsFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, resultsFragment)
                .commit()
        }

        val strawberryButton = view.findViewById<Button>(R.id.strawberryButton)
        val bananaButton = view.findViewById<Button>(R.id.bananaButton)
        val blueberryButton = view.findViewById<Button>(R.id.blueberryButton)
        val mangoButton = view.findViewById<Button>(R.id.mangoButton)
        val selectedIngredientsText = view.findViewById<TextView>(R.id.selectedIngredientsText)
        val submitOrderButton = view.findViewById<Button>(R.id.submitOrderButton)
        val orderFeedbackText = view.findViewById<TextView>(R.id.orderFeedbackText)

        val hideFeedback = Runnable {
            orderFeedbackText.visibility = View.GONE
        }

        // Display ingredients restored after rotation
        selectedIngredientsText.text =
            if (selectedIngredients.isEmpty()) {
                "Your Smoothie: Empty"
            } else {
                "Your Smoothie: ${selectedIngredients.joinToString(", ")}"
            }

        // Function to add an ingredient to the smoothie
        fun addIngredient(ingredient: String) {
            if (!selectedIngredients.contains(ingredient)) {
                selectedIngredients.add(ingredient)
            }

            selectedIngredientsText.text =
                "Your Smoothie: " + selectedIngredients.joinToString(", ")

        }

        // Ingredient button click listeners
        strawberryButton.setOnClickListener {
            addIngredient("Strawberry")
        }

        bananaButton.setOnClickListener {
            addIngredient("Banana")
        }

        blueberryButton.setOnClickListener {
            addIngredient("Blueberry")
        }

        mangoButton.setOnClickListener {
            addIngredient("Mango")
        }

        submitOrderButton.setOnClickListener {
            submitOrderButton.isEnabled = false

            val correctIngredients = currentRecipe.ingredients

            if (selectedIngredients.toSet() == correctIngredients.toSet()) {
                //updates score
                score += 10
                view.findViewById<TextView>(R.id.scoreText).text = "Score: $score"

                //displays feedback
                orderFeedbackText.text = "Perfect Smoothie! +10 Points!"
                orderFeedbackText.visibility = View.VISIBLE

                //animate the feedback display
                orderFeedbackText.scaleX = 1f
                orderFeedbackText.scaleY = 1f
                orderFeedbackText.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(200)
                    .withEndAction {
                        orderFeedbackText.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start()
                    }
                    .start()

                //message disappears after animation
                orderFeedbackText.removeCallbacks(hideFeedback)
                orderFeedbackText.postDelayed(hideFeedback, 1000)

            } else {
                //updates heart
                if (hearts > 0) {
                    hearts -= 1
                }

                view.findViewById<TextView>(R.id.heartsText).text =
                    "Hearts: " + "♥ ".repeat(hearts)

                //displays feedback
                orderFeedbackText.text = "Oops! Wrong Smoothie! -1 Heart!"
                orderFeedbackText.visibility = View.VISIBLE

                //animate the feedback display
                orderFeedbackText.animate().cancel()
                orderFeedbackText.translationX = 0f
                orderFeedbackText.animate()
                    .translationX(20f)
                    .setDuration(100)
                    .withEndAction {
                        orderFeedbackText.animate()
                            .translationX(-20f)
                            .setDuration(100)
                            .withEndAction {
                                orderFeedbackText.animate()
                                    .translationX(0f)
                                    .setDuration(100)
                                    .start()
                            }
                            .start()
                    }
                    .start()


                //message disappears after animation
                orderFeedbackText.removeCallbacks(hideFeedback)
                orderFeedbackText.postDelayed(hideFeedback, 1000)
            }

            //ends game if hearts run out
            if (hearts == 0) {
                submitOrderButton.isEnabled = false

                // Open the Results screen because the player lost
                showResults(false)
                return@setOnClickListener
            }

            // Count the order that was just submitted
            ordersCompleted += 1

            // Update progress and finish the shift after five orders
            if (ordersCompleted < maxOrders) {
                orderProgressText.text = "Order ${ordersCompleted + 1} of $maxOrders"
            } else {
                orderProgressText.text = "Shift Complete!"

                submitOrderButton.isEnabled = false

                // Open Results screen if the player still has hearts
                if (hearts > 0) {
                    showResults(true)
                }
            }

            //clear selected ingredient list
            selectedIngredients.clear()
            selectedIngredientsText.text = "Your Smoothie: Empty"

            // Select a new recipe only if the game is still active
            if (hearts > 0 && ordersCompleted < maxOrders) {
                currentRecipeIndex = recipes.indices.random()
                currentRecipe = recipes[currentRecipeIndex]

                orderDescription.text = currentRecipe.name
                orderIngredients.text = currentRecipe.ingredients.joinToString(", ")

                submitOrderButton.postDelayed({
                    submitOrderButton.isEnabled = true
                }, 600)
            }
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("savedScore", score)
        outState.putInt("savedHearts", hearts)
        outState.putInt("savedOrdersCompleted", ordersCompleted)
        outState.putInt("savedRecipeIndex", currentRecipeIndex)
        outState.putStringArrayList(
            "savedSelectedIngredients",
            ArrayList(selectedIngredients)
        )
    }


}
