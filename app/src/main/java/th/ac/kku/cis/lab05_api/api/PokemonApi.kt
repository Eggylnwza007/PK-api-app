import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import th.ac.kku.cis.lab05_api.model.PokemonList
import th.ac.kku.cis.lab05_api.viewmodel.PokemonDetail

interface PokemonApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("pokemon")
    fun getPokemonList(): Call<PokemonList>

    @GET("pokemon/{id}")
    fun getPokemonDetail(@Path("id") id: String): Call<PokemonDetail>
}
