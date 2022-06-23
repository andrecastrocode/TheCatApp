package ac.andrecastro.thecatapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

data class Fact(
    val fact: String
)

interface CatFactApi {

    @GET("fact")
    fun randomFact() : Call<Fact>
}

class MainActivity : AppCompatActivity() {
    private lateinit var catFactTextView: TextView
    private lateinit var catImageImageView: ImageView
    private lateinit var moreFactsButton: Button
    private lateinit var catFactApi: CatFactApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catImageImageView = findViewById(R.id.cat_image)
        catFactTextView = findViewById(R.id.cat_fact)
        moreFactsButton = findViewById(R.id.more_facts_button)

        catFactApi = adapter("https://catfact.ninja/").create(CatFactApi::class.java)

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
    private val okHttpClient : OkHttpClient by lazy {
        val builder = OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
        builder.build()
    }

    private fun adapter(endPoint : String) : Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
        return builder.build()
    }

    companion object {
        private const val TIMEOUT = 10L
    }
}