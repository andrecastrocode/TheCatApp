package ac.andrecastro.thecatapp.presentation.viewmodel

import ac.andrecastro.thecatapp.data.api.CatFactApi
import ac.andrecastro.thecatapp.data.api.CatFactApiInterface
import ac.andrecastro.thecatapp.data.api.Fact
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class CatFactViewModel : ViewModel() {

    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider()
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val catFactApi: CatFactApiInterface = CatFactApi().catFactApi

    private val catFactMutableLiveData = MutableLiveData<UiState<Fact>>()
    val catFactLiveData: LiveData<UiState<Fact>>
        get() = catFactMutableLiveData

    init {
        getCatFact()
    }

    fun getCatFact() {
        catFactApi.randomFact()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .doOnSubscribe {
                catFactMutableLiveData.value = UiState.Loading
            }
            .subscribe(
                {
                    catFactMutableLiveData.value = UiState.Display(it)
                },
                {
                    catFactMutableLiveData.value =
                        UiState.Error(message = "Your cat fact is not available right now")
                }).also {
                disposables.add(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}