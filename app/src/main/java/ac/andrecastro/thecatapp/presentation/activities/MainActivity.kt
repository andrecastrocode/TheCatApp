package ac.andrecastro.thecatapp.presentation.activities

import ac.andrecastro.thecatapp.R
import ac.andrecastro.thecatapp.presentation.UiState
import ac.andrecastro.thecatapp.presentation.viewmodel.CatFactViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MainActivity : AppCompatActivity() {
    private lateinit var catFactTextView: TextView
    private lateinit var catImageImageView: ImageView
    private lateinit var moreFactsButton: Button
    private lateinit var defaultCatFact: String

    private val viewModel: CatFactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initClicks()
        initObservers()

        newImage()
    }

    private fun initViews() {
        catImageImageView = findViewById(R.id.cat_image)
        catFactTextView = findViewById(R.id.cat_fact)
        moreFactsButton = findViewById(R.id.more_facts_button)
        defaultCatFact = getString(R.string.defaultCatFact)
    }

    private fun initObservers() {
        viewModel.catFactLiveData.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    // TODO: show loading replacement?
                }
                is UiState.Display -> {
                    Log.d("TheCatApp", "YES! ${it.data.fact}")
                    catFactTextView.text = it.data.fact
                }
                is UiState.Error -> {
                    catFactTextView.text = defaultCatFact
                }
            }
        }
        viewModel.getCatFact()
    }

    // TODO: refactor this call
    private fun newImage() {
        Glide.with(this)
            .load("https://cataas.com/cat/gif")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(catImageImageView)
    }

    private fun initClicks() {
        moreFactsButton.setOnClickListener {
            Log.d("TheCatApp", "Requesting new fact/gif")
            viewModel.getCatFact()
            newImage()
        }
    }

}