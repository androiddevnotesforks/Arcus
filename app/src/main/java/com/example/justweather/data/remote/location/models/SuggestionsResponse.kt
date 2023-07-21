package com.example.justweather.data.remote.location.models

import com.example.justweather.domain.models.LocationAutofillSuggestion
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This data class represents a response containing a list of place [suggestions] for a specific
 * query.
 */
@JsonClass(generateAdapter = true)
data class SuggestionsResponse(@Json(name = "results") val suggestions: List<Suggestion> = emptyList()) {

    /**
     * This data class represents a single place suggestion for a specific query.
     *
     * @property idOfPlace The ID of the place.
     * @property nameOfPlace The name of the place.
     * @property country The country that the place is situated in.
     */
    @JsonClass(generateAdapter = true)
    data class Suggestion(
        @Json(name = "id") val idOfPlace: String,
        @Json(name = "name") val nameOfPlace: String,
        @Json(name = "country") val country: String,
        @Json(name = "admin1") val state: String?,
        val latitude: String,
        val longitude: String
    )
}

/**
 * A mapper function used to map a list of type [SuggestionsResponse.Suggestion] to a list of
 * of type [LocationAutofillSuggestion].
 *
 * Note: This method **filters out all instances of [SuggestionsResponse.Suggestion] that have
 * the [SuggestionsResponse.Suggestion.state] set to null.**
 */
fun List<SuggestionsResponse.Suggestion>.toLocationAutofillSuggestionList(): List<LocationAutofillSuggestion> =
    this.filter { it.state != null }
        .map {
            LocationAutofillSuggestion(
                idOfLocation = it.idOfPlace,
                nameOfLocation = it.nameOfPlace,
                addressOfLocation = "${it.state}, ${it.country}",
                coordinatesOfLocation = LocationAutofillSuggestion.Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            )
        }
