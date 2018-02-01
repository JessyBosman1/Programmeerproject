![Banner](/doc/Banner.png?raw=true)
##### **Jessy Bosman** - 11056045

# Problem statement
Cryptocurrencies zijn steeds bekender en er bestaan ondertussen ontelbare soorten, met een steeds groter wordende doelgroep van mensen die erin geïntresseerd zijn en erin handelen. Het is lastig om de stijgingen en dalingen bij te houden en met elkaar te vergelijken, om te kijken wat de waarde is en of de ze beter wordt of niet. Kortom, het is niet overzichtelijk wat er gebeurd tussen de verschillende currencies.

# Features
- Een lijst met de top 100 crypto currencies van CoinMarketCap.
- favorieten kunnen worden bekeken en toegevoegd(__door een currency ingedrukt te houden__).
- Er kan door de lijst gezocht worden met een dynamische zoekbalk.
- Elke currency heeft een eigen pagina die gedatailleerde informatie laat zien van de coin.
    - Hier zijn ook grafieken zichtbaar die in verschillende tijdseenheden in te delen zijn.
- Het vergelijken van 2 currencies in 1 grafiek.
- Een wallet waarin currencies en hoeveelheden kunnen worden ingevuld om zo de waarde te zien.
- Een Light en Dark layout, om zo een layout te kiezen die past bij de gebruiker.
- Alle invoervelden zijn autocomplete, waardoor er sneller het juiste kan worden ingevuld.
- De state van de lists wordt onthouden; als je back drukt kom je waar je bent gebleven.

# Overzicht
![Overview](/doc/Class_Overview.png?raw=true)


# data sources
- JSON API
https://coinmarketcap.com/api/
De open dataset van coinmarketcap. 
Hier staat informatie over de verschillende cryptocurrencies, zoals de total supply en hoeveel dollar het momenteel waard is. Ook is de rank van de currency beschikbaar.

**sample data:**
```
[
    {
        "id": "bitcoin",
        "name": "Bitcoin",
        "symbol": "BTC",
        "rank": "1",
        "price_usd": "573.137",
        "price_btc": "1.0",
        "24h_volume_usd": "72855700.0",
        "market_cap_usd": "9080883500.0",
        "available_supply": "15844176.0",
        "total_supply": "15844176.0",
        "percent_change_1h": "0.04",
        "percent_change_24h": "-0.3",
        "percent_change_7d": "-0.57",
        "last_updated": "1472762067"
    },
```

- JSON API
https://www.cryptocompare.com/api:
    - https://www.cryptocompare.com/api#-api-data-histominute-
    - https://www.cryptocompare.com/api#-api-data-histohour-
    - https://www.cryptocompare.com/api#-api-data-histoday-

Hier kunnen datapunten voor verschillende tijdsduren worden uitgehaald.

# external sources
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart "MPAndroidChart") - plot grafieken
- [Glide](https://github.com/bumptech/glide "Glide") - laad images in
- [Volley](https://github.com/google/volley "Android Volley") - maakt data requests mogelijk van online sources

# BetterCodeHub
[![BCHcompliance](https://bettercodehub.com/edge/badge/JessyBosman1/Programmeerproject?branch=master)](https://bettercodehub.com/)

Copyright 2018 Jessy Bosman

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
