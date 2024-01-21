import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.fake.FakeDataSource
import com.example.pokedex.fake.FakePokemonApiService
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals

class NetworkPokemonRepositoryTest {

    @Test
    fun networkPokemonPhotosRepository_getPokemon_Photos_verifyPhotoList() =
        runTest{
        val repository = NetworkPokemonRepository(
            pokemonApiService = FakePokemonApiService()
        )

        assertEquals(FakeDataSource.photosList, repository.getPokemonPhotos())
    }
}
