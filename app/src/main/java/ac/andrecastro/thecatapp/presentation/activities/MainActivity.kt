package ac.andrecastro.thecatapp.presentation.activities

import ac.andrecastro.thecatapp.R
import ac.andrecastro.thecatapp.presentation.UiState
import ac.andrecastro.thecatapp.presentation.viewmodel.CatFactViewModel
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    companion object {
        const val cCatGifURL: String = "https://cataas.com/cat/gif"
    }

    private lateinit var catImageImageView: ImageView
    private lateinit var catFactTextView: TextView
    private lateinit var catFactProgressBar: ProgressBar
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
        catFactProgressBar = findViewById(R.id.cat_fact_progress_bar)
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
        catFactProgressBar.visibility = View.VISIBLE
        Glide.with(this)
            .load(cCatGifURL)
            .error(R.drawable.default_cat)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    catFactProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    catFactProgressBar.visibility = View.GONE
                    return false
                }

            })
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