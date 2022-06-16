package ac.andrecastro.thecatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

data class CatFact(var fact: String = "", var length: Int = 0) {

    class Deserializer : ResponseDeserializable<CatFact> {
        override fun deserialize(content: String): CatFact =
            Gson().fromJson(content, CatFact::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            Fuel.get("https://catfact.ninja/fact").appendHeader("accept: application/json")
                .responseObject(CatFact.Deserializer()) { _, _, result ->
                    println("FACT: " + result.component1()?.fact)
                    println("LENGTH: " + result.component1()?.length)
                }
        }
        setContentView(R.layout.activity_main)
    }
}