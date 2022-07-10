package ac.andrecastro.thecatapp.data.api

import retrofit2.Call
import retrofit2.http.GET

data class Fact(
    val fact: String
)

interface CatFactApiInterface {

    @GET("fact")
    fun randomFact(): Call<Fact>
}