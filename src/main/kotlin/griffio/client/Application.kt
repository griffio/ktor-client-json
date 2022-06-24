// avoid @Serializable(with=SomeSerializer::class) on each property with custom serializer.
@file:UseSerializers(UnknownStringSerializer::class, UnknownIntSerializer::class, UnknownLongSerializer::class)

package griffio.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

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
    val name: String,
    // FYI https://github.com/Kotlin/kotlinx.serialization/issues/33
    @SerialName("orbital_period")
    val orbitalPeriod: Int?,
    val population: Long?
)
// repeat the same Serializer until better way found
class UnknownLongSerializer : KSerializer<Long?> {
    override val descriptor =
        PrimitiveSerialDescriptor("unknown.Long?", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Long?) =
        encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Long? {
        val decoded = decoder.decodeString()
        return if (decoded.startsWith("unknown")) null else decoded.toLong()
    }
}
class UnknownIntSerializer : KSerializer<Int?> {
    override val descriptor =
        PrimitiveSerialDescriptor("unknown.Int?", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Int?) =
        encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Int? {
        val decoded = decoder.decodeString()
        return if (decoded.startsWith("unknown")) null else decoded.toInt()
    }
}
class UnknownStringSerializer : KSerializer<String?> {
    override val descriptor =
        PrimitiveSerialDescriptor("unknown.String?", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: String?) =
        encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): String? {
        val decoded = decoder.decodeString()
        return if (decoded.startsWith("unknown")) null else decoded
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
