package ac.andrecastro.thecatapp.data.api

import io.reactivex.Single
import retrofit2.http.GET

data class Fact(
    val fact: String
)

interface CatFactApiInterface {

    @GET("fact")
    fun randomFact(): Single<Fact>
}
