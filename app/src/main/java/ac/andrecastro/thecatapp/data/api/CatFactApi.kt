package ac.andrecastro.thecatapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CatFactApi {

    val catFactApiImpl by lazy {
        initCatFactApi()
    }

    private fun initCatFactApi(): CatFactApiInterface =
        adapter().create(CatFactApiInterface::class.java)

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
        builder.build()
    }

    private fun adapter(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
        return builder.build()
    }

    companion object {
        private const val ENDPOINT = "https://catfact.ninja/"
        private const val TIMEOUT = 10L
    }
}

