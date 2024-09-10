import com.example.eulerity_assessment.data.PetsAPI
import com.example.eulerity_assessment.domain.model.Pet
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PetsRepository {

    private val api: PetsAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://eulerity-hackathon.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PetsAPI::class.java)
    }

    suspend fun getPets(): List<Pet> {
        return api.getPets()
    }
}