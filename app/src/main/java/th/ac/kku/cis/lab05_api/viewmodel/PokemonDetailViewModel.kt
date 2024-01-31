package th.ac.kku.cis.lab05_api.viewmodel

import PokemonApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokemonDetailViewModel : ViewModel() {
    private val _pokemonDetail: MutableLiveData<PokemonDetail> = MutableLiveData()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error: MutableLiveData<String?> = MutableLiveData(null)
    val error: LiveData<String?> = _error

    fun fetchPokemonDetail(pokemonId: String) {
        _isLoading.postValue(true)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PokemonApi::class.java)
        val call: Call<PokemonDetail> = api.getPokemonDetail(pokemonId)

        call.enqueue(object : Callback<PokemonDetail> {
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    _pokemonDetail.postValue(response.body())
                } else {
                    _error.postValue("Failed to fetch details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                _isLoading.postValue(false)
                _error.postValue("Failed to fetch details: ${t.message}")
            }
        })
    }
}

