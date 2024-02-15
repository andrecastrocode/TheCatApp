package ac.andrecastro.thecatapp.presentation.ui.activities

import ac.andrecastro.thecatapp.R
import ac.andrecastro.thecatapp.data.api.Fact
import ac.andrecastro.thecatapp.presentation.ui.theme.TheCatAppTheme
import ac.andrecastro.thecatapp.presentation.viewmodel.CatFactViewModel
import ac.andrecastro.thecatapp.presentation.viewmodel.UiState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey

@OptIn(ExperimentalGlideComposeApi::class)
class MainActivity : ComponentActivity() {
    companion object {
        const val cCatGifURL: String = "https://cataas.com/cat/gif"
    }

    private val catFactViewModel = CatFactViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheCatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val catFactValue = catFactViewModel.catFactLiveData.observeAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.weight(1f))

                        val stringSignature = System.currentTimeMillis().toString()
                        @Suppress("DEPRECATION")
                        GlideImage(
                            model = cCatGifURL,
                            contentDescription = "catGifs",
                            modifier = Modifier
                                .size(300.dp, 400.dp),
                            loading = placeholder {
                                CircularProgressIndicator(modifier = Modifier.size(300.dp, 400.dp))
                            },
                            failure = placeholder(R.drawable.default_cat),
                            transition = CrossFade
                        ) {
                            it.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .signature(ObjectKey(stringSignature))
                        }

                        val catFactText: String =

                            when (catFactValue.value) {
                                is UiState.Loading -> {
                                    "Loading"
                                }

                                is UiState.Display -> (catFactValue.value as UiState.Display<Fact>).data.fact

                                is UiState.Error -> getString(R.string.defaultCatFact)
                                else -> getString(R.string.defaultCatFact)

                            }
                        Spacer(Modifier.weight(1f))

                        Text(
                            text = catFactText,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .weight(8f)
                                .padding(start = 10.dp, end = 10.dp)
                        )

                        Button(
                            onClick = {
                                catFactViewModel.getCatFact()
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(bottom = 24.dp)
                        ) {
                            Text(text = "More cat facts!")
                        }
                    }
                }
            }
        }
    }

}
