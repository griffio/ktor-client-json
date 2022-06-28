// Allows the Planet.serializer to be used
@file:UseSerializers(UnknownToNullPlanetSerializer::class)

package griffio.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.*

@Serializable
data class Planets(
    val next: String?,
    val results: List<Planet>
)
@Serializable
data class Planet(
    val climate: String?,
    val diameter: Int?,
    val gravity: String?,
    val name: String?,
    // FYI https://github.com/Kotlin/kotlinx.serialization/issues/33
    @SerialName("orbital_period")
    val orbitalPeriod: Int?,
    val population: Long?
)
class UnknownToNullPlanetSerializer : JsonTransformingSerializer<Planet>(Planet.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val newMap: Map<String, JsonElement> = element.jsonObject.toMutableMap().map {
            if (it.value == JsonPrimitive("unknown")) {
                it.key to JsonNull
            } else it.key to it.value
        }.toMap()
        return JsonObject(newMap)
    }
}
suspend fun main() {
    // Setup HttpClient - e.g use Java engine
    // io.ktor:ktor-client-java
    // io.ktor:ktor-client-content-negotiation
    // io.ktor:ktor-serialization-kotlinx-json
    val client = HttpClient(Java) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    flow {
        val resource = "https://swapi.dev/api/planets"
        var next: String? = resource
        while (next != null) {
            val response: HttpResponse = client.request(next)
            val planets: Planets = response.body()
            next = planets.next
            emit(planets.results)
        }
    }.collect {
        println(it)
    }
}
