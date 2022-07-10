package ac.andrecastro.thecatapp.presentation.activities

import ac.andrecastro.thecatapp.R
import ac.andrecastro.thecatapp.data.api.CatFactApi
import ac.andrecastro.thecatapp.data.api.CatFactApiInterface
import ac.andrecastro.thecatapp.data.api.Fact
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var catFactTextView: TextView
    private lateinit var catImageImageView: ImageView
    private lateinit var moreFactsButton: Button
    private lateinit var catFactApi: CatFactApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catImageImageView = findViewById(R.id.cat_image)
        catFactTextView = findViewById(R.id.cat_fact)
        moreFactsButton = findViewById(R.id.more_facts_button)

        catFactApi = CatFactApi().catFactApi

        newImage()
        newFact()
        initClicks()
    }

    private fun newImage() {
        Glide.with(this)
            .load("https://cataas.com/cat/gif")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(catImageImageView)
    }

    private fun newFact() {
        val fact = catFactApi.randomFact()

        fact.enqueue(object : Callback<Fact> {
            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {

                if (response.body() != null) {
                    val factText = response.body()!!.fact
                    Log.d("TheCatApp", "YES! " + factText)
                    catFactTextView.text = factText
                }
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                Log.e("TheCatApp", "NOOOOOOOOOOOOOOOOOOOOOOOOOOO")
            }
        })
    }

    private fun initClicks(){
        moreFactsButton.setOnClickListener {
            Log.e("TheCatApp", "NEW FACT!")
            newFact()
            newImage()
        }
    }

}