# Ktor client with json example

Basic setup for [ktor client](https://ktor.io/docs/getting-started-ktor-client.html)
with Json request from Star Wars API (https://swapi.dev/)

Demonstrates

* Gradle build
* Client setup
* Json serialization
* Custom serialization for variant data types to nullable
    * Some planets have "unknown" in various fields
    * e.g "population": "200000" or "population": "unknown"

``` json

{
	"count": 60,
	"next": "https://swapi.dev/api/planets/?page=2",
	"previous": null,
	"results": [
		{
			"name": "Tatooine",
			"rotation_period": "23",
			"orbital_period": "304",
			"diameter": "10465",
			"climate": "arid",
			"gravity": "1 standard",
			"terrain": "desert",
			"surface_water": "1",
			"population": "200000",
			"residents": [
				"https://swapi.dev/api/people/1/",
			],
			"films": [
				"https://swapi.dev/api/films/1/",
			],
			"created": "2014-12-09T13:50:49.641000Z",
			"edited": "2014-12-20T20:58:18.411000Z",
			"url": "https://swapi.dev/api/planets/1/"
		},
	
```
