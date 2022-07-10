package ac.andrecastro.thecatapp.presentation

sealed class UiState<out T : Any> {
    object Loading : UiState<Nothing>()
    data class Display<out T : Any>(val data: T) : UiState<T>()
    data class Error(val code: Int = -1, val message: String) : UiState<Nothing>()
}