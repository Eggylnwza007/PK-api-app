package th.ac.kku.cis.lab05_api.viewmodel

data class PokemonDetail(
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    // Add other properties as needed
)

data class Type(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

