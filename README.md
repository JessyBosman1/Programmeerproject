# **PROJECT PROPOSAL**
##### ***Crypto***
##### **Jessy Bosman** - 11056045

# Problem statement
Cryptocurrencies zijn steeds bekender en het bestaan ondertussen ontelbare soorten, met een steeds groter wordende doelgroep van mensen die erin geïntresseerd zijn en erin handelen. Het is lastig om de stijgingen en dalingen bij te houden en met elkaar te vergelijken, om te kijken wat de waarde is en of de ze beter wordt of niet. Kortom, het is niet overzichtelijk wat er gebeurd tussen de verschillende currencies.
# Solution
Een tracker app die de currencies bijhoudt en het mogelijk maakt ze te vergelijken met elkaar. 
#### main features 
- een lijst met beschikbare currencies
- Een zoekmogelijkheid
- favorites toevoegen
- geselecteerde favorites kunnen bekijken
- vergelijken van 2 currencies
- uitgebreide informatie van een currency bekijken bij click
- lijst sorteren op een eigenschap, zoals prijs of rank.

#### optional features
- login
- bijhouden hoeveel je van een coin zelf hebt en wat de waarde is
- een calculator die uitrekent hoeveel coins hoeveel dollars waard is

# Prerequisites
#### data sources
JSON API
https://coinmarketcap.com/api/
De open dataset van coinmarketcap. 
Hier staat informatie over de verschillende cryptocurrencies, zoals de total supply en hoeveel dollar het momenteel waard is. Ook is de rank van de currency beschikbaar.

**sample data:**
Alle waardes zijn strings en sommige moeten omgezet worden naar floats.
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

#### external sources
**Google Firebase** - maakt het mogelijk om gebruikers te registreren en in te loggen. Andere gebruikers data kan worden opgeslagen, zoals de favorieten van de gebruiker in dit geval.

#### similar
**CryptoCurrency - Bitcoin Altcoin Price**
https://play.google.com/store/apps/details?id=com.crypter.cryptocurrency
functionaliteiten
- houdt de waarde van de currenties bij
- statistieken weergeven per coin
- meldingen als een coin stijgt of daalt
- calculator om te bekijken hoeveel iets waard is

de meeste functionaliteiten zijn qua functionaliteiten goed zelf te maken. Uitgebreide statistieken zoals grafieken zijn moeilijker mogelijk, omdat de data dan moet worden bijgehouden. Dit is niet onmogelijk, maar kost te veel tijd om in de korte periode zelf te implementeren. De andere functionaliteiten zijn naar mijn mening zelf goed te maken.
#### hardest part
- Gebruikers moeten favorieten kunnen opslaan. Dit kan met behulp van Firebase. Wat hier lastig aan is is dat het op verschillende manieren gedaan kan worden. bijvoorbeeld 1 grote lijst per gebruiker, of verschillende entries gekoppelt aan de gebruiker. 
- Sorteren op verschillende eigenschappen. De lijst moet hergestructureerd worden op een eigenschap. Op zich is dit niet zo moeilijk, maar de functie overzichtelijk houden kan nog lastig zijn. Als er op meerdere eigenschappen tegelijk gesorteerd kan worden geeft dit mogelijk wel een probleem. 
