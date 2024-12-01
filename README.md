# currency_convertor
Currency Converter project that uses Redis Caching

## Prequisites

Redis server needs to be running on your machine or in a docker container.

if your redis connection differs from default setting then update application.properties

I used REDIS for windows from github: https://github.com/tporadowski/redis/releases

```
#spring.redis.host=localhost
#spring.redis.port=6379
```

## Running

application runs on `localhost:8080` with the endpoint `/convert`

Example

```localhost:8080/convert?currencyFrom=USD&currencyTo=GBP&amount=1```

Will convert 1 USD to its GBP equivalent

---

Currently the only currencies supported is GBP, EUR and USD as the external API is mocked by the API service.

--- 

The are a few notes in comments where I discuss things I couldn't do due to time availablity.

## TODO

Implement Rate Limiting

Parallel Processing