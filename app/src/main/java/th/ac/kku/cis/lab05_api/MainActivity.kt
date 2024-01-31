package th.ac.kku.cis.lab05_api

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import th.ac.kku.cis.lab05_api.ui.theme.Lab05apiTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import th.ac.kku.cis.lab05_api.model.Pokemon
import th.ac.kku.cis.lab05_api.viewmodel.PokemonDetailViewModel
import th.ac.kku.cis.lab05_api.viewmodel.PokemonViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab05apiTheme {
                PokemonApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonApp(navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    var currentScreen = backStackEntry?.destination?.route ?: "List"
    if(currentScreen.contains("/"))
        currentScreen = currentScreen.split("/")[0]

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokemon", style = MaterialTheme.typography.bodyLarge) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back Navigation"
                            )
                        }
                    }
                }
            )

        }
    ){
        paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "List",
                modifier = Modifier.padding(paddingValues)
            ){
                composable(route = "List"){
                    PokekonList(
                        onItemClick = {
                                      pokemonId -> navController.navigate(route = "Detail/" + pokemonId)
                        },
                        navigateUp = { navController.navigateUp()})
                }
                composable(route = "Detail/{pokemonId}"){
                        backStackEntry -> PokemonDetail(
                    navController = navController,
                    pokemonId = backStackEntry.arguments?.getString("pokemonId"))
                }
            }
    }
}
@Composable
fun PokekonList(
    navigateUp: () -> Unit,
    onItemClick: (String) -> Unit,
    pokemonViewModel: PokemonViewModel = viewModel()
){
    val pokemonList by pokemonViewModel.pokemonList.observeAsState(initial = emptyList())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pokemonList) { item: Pokemon ->
            PokemonItem(item, onClick = onItemClick)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClick:(id:String) -> Unit
){
    var context = LocalContext.current
    var imageUrl:String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    var urlSplited:List<String> = pokemon.url.split('/')
    var pokemonId = urlSplited[urlSplited.size - 2]
    var pokemonImage:String = imageUrl + pokemonId + ".png"
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        onClick = { onClick(pokemonId) },
        modifier = Modifier
            .size(width = 120.dp, height = 150.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = pokemonImage,
                contentDescription = "Pokemon Image",
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(CircleShape)
            )
            Text(
                text = pokemon.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .widthIn(max = 80.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }

}

@Composable
fun PokemonDetail(
    pokemonId: String?,
    pokemonDetailViewModel: PokemonDetailViewModel = viewModel(),
    navController: NavHostController
) {
    val pokemonDetail by pokemonDetailViewModel.pokemonDetail.observeAsState()

    LaunchedEffect(pokemonId) {
        if (pokemonId != null) {
            pokemonDetailViewModel.fetchPokemonDetail(pokemonId)
        }
    }

    if (pokemonDetail != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Name: ${pokemonDetail!!.name}")
            Text(text = "Height: ${pokemonDetail!!.height}")
            Text(text = "Weight: ${pokemonDetail!!.weight}")

            // Displaying Pokemon types
            pokemonDetail!!.types?.let { types ->
                Text(text = "Type: ${types.joinToString(", ")}")
            }

            // Add other details as needed
        }
    } else {
        // Handle loading state or error state if needed
        Text(text = "Loading...")
    }
}



@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Lab05apiTheme {
        PokemonApp()
    }
}