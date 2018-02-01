# Final Report
## desciption
[![BCHcompliance](https://bettercodehub.com/edge/badge/JessyBosman1/Programmeerproject?branch=master)](https://bettercodehub.com/)

De applicatie maakt het mogelijk de verschillende cryptocurrencies naast elkaar te houden. De waardes van verschillende currencies kunnen in verschillende tijdsperiodes worden geplot in een grafiek. Ook is het mogelijk de grafieken samen te plotten met een percentage weergave om zo 2 currencies te vergelijken. De applicatie heeft ook de mogelijkheid om zelf een wallet bij te houden die de waarde geeft van de currencies die de gebruiker heeft ingevoerd.

![VisualSketchImage](/doc/23_1_image1.png?raw=true)

## Technical Design
Allereerst een overview van welke schermen welke classes gebruiken, om duidelijk te maken hoe het totaalplaatje met elkaar samenhangt:
![VisualSketchImage](/doc/Class_Overview.png?raw=true)

## details
### **SplashScreen.java**
Dit script displayed het opstart scherm van de app en gaat vervolgens door naar de main activity (MainActivity.java)


### **MainActivity.java**
Dit is de fragment Container van de app. Dit script behandeld de navigation bottom bar en zorgt ervoor dat de juiste fragmenten worden ingeladen naarmate er wordt geklikt op de navigatiebalk.

Main Activity regelt de navigatie naar de andere fragmenten. Ook regelt MainActivity de overschakeling van de Dark en Light layout modes (day&night modus).


### **MainListFragment.java**
de MainListFragment handeld de verschillende listviews af die cryptocurrencies listen(standaard, favorites en zoeken). Het script neemt een argument (Method), die bepaald welke lijst gegenereerd moet worden. Dit wordt zo gedaan om dubbele code te voorkomen bij het genereren van de lijsten.

de rijen zelf worden vormgegeven door de CoinListAdapter, zie kopje.

Na het genereren van de juiste lijst worden de listeners gezet op de rows. Een enkele klik zorgt ervoor dat er naar de InfoActivity wordt overgeschakelt (via FragmentSwitcher.java).

Een longclick slaat het corresponderende item op als favorite, of verwijderd het item juist als deze al geregistreerd is als favorite. Dit wordt opgeslagen in de Shared Preferences.

De zoekbalk wordt hier ook geinitialiseerd. Deze werkt met een timer; als de user typt en vervolgens 500 milliseconde niet typt wordt de lijst geupdate naar de zoekterm. Het 500 milliseconde interval wordt gebruikt zodat niet bij iedere letter een update plaatsvindt wat het systeem overbelast. Hierdoor onstaat een dynamisch effect zonder dat de listview crashed door het grote aantal interacties.


### **CoinListAdapter.java _(helperclass)_**
Deze custom list adapter maakt iedere rij van het MainListFragment. Het object wordt meegestuurt en hier uit elkaar gehaald tot verschillende parameters. Het icoontje wordt ingeladen vanuit Coinmarketcap.com 's website. De andere waardes komen uit het Object zelf. De percentages worden gekleurt aan de hand van of het een positieve of negatieve waarde is.

Er wordt gekeken of het item een favorite is vanuit de shared prefs en hier wordt de toggle van de favorite icon bepaald.


### **FragmentSwitcher.java _(helperclass)_**
Deze class stuurt het object door vanuit een MainListFragment naar de InfoActivity, waarbij het object wordt doorgestuurt.


### **InfoActivity.java**
InfoActivity managed het scherm dat de uitgebreide informatie en de grafieken weergeeft.

De toggle buttons bepalen welke grafiek wordt gedisplayed. Er is een keuze tussen een advanced en een standaard weergave. Het is ook mogelijk om te wisselen tussen een 1 uur, 12 uur, 1 dag en 1 week weergave.

De percentage teksten worden aangepast qua kleur afhankelijk van of het positief of negatief is.


### **PlotSimpleGraph.java _(helperclass)_**
Hier wordt de standaard graph aangemaakt met behulp van de datapunten. De grafiek wordt weergegeven met behulp van [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart "MPAndroidChart"). De datapunten worden bepaald aan de hand van de keuze in tijdweergave (bijv 12 uurs weergave).

Verder wordt in dit script de opmaak van de grafiek afgesteld.


### **PlotCandleStickGraph.java _(helperclass)_**
De geavanceerde grafiek, ook wel Candle Stick Graph, wordt hier gemaakt, met behulp van [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart "MPAndroidChart"). De datapunten worden verwerkt in een Array, en de as labels worden gegenerereert aan de hand van de Unix Timestamp(zie UnixConverter.java).


### **UnixConverter.java _(helperclass)_**
De unix time is het aantal verstreken seconden vanaf 1 januari 1970. Om dit bruikbaar te maken moet het worden omgezet naar een leesbaar, handig formaat. Dit script rekent dat uit en formateert het. De unix tijd 1517428280 staat bijvoorbeeld voor 01/31/2018 7:51pm. De omgerekende waardes worden gebruikt als x-as labels voor de grafieken.


### **CompareFragment.java**
Dit fragment wordt gebruikt om 2 verschillende currencies met elkaar te kunnen vergelijken. De Autocomplete velden worden vanuit het geheugen voorzien van suggesties. Uniek aan dit fragment is dat er 2 lijnen in de grafiek worden geplot, waardoor coins met elkaar kunnen worden vergeleken. Het probleem bij cryptocurrencies is dat waardes enorm uit elkaar liggen (e.g. 10.000 - 13.000 en 0.01 - 0.03). Daarom worden de datapunten eerst omgerekent naar een percentage t.o.v. de eerste waarde.


### **WalletFragment.java**
De wallet maakt het mogelijk om zelf bij te houden hoeveel een bepaalde hoeveelheid van een currency waard is op het moment. Zo kan de gebruiker bijvoorbeeld snel zien voor hoeveel hij zijn cryptomunt kan inruilen. Het is mogelijk om toe te voegen, en met een onclick te items te verwijderen of wijzigen. Alle velden zijn Autocomplete en geven een melding als er een niet verwachte input wordt gegeven.


### **WalletAdapter.java _(helperclass)_**
De walletAdapter regelt de invulling per row van de lijst in de walletfragment. De objecten worden uit de shared preferences gehaald en ingevuld. De waarde van het aantal van een coin wordt berekent en ingevuld.


## Challenges
- De eerste challenge was niet zozeer de data ophalen, maar de data in het goede formaat krijgen. Om dit goed te doen moest het worden omgezet naar een object. Het moeilijkste hieraan was dat de 2 data API's elkaar moesten kunnen aanvullen met data, maar natuurlijk niet dezelfde soort structuur gebruiken. Om dit op te lossen gebruik ik de Symbol parameter uit de eerste data om een query te sturen naar de 2e dataset en zo alleen dat object terug te krijgen. Dit voorkomt teveel data ophalen en geeft altijd alleen het juiste object terug.

- De custom listview goed werkend krijgen werkte in het begin niet helemaal vloeiend, omdat nadat de data was ingeladen er lege velden achteraan werden gegenereert. Dit is opgelost door de data eerst in zn geheel op te halen voordat de list adapter aan het werk gaat.

- De zoekbalk heeft ook veel moeite gekost om correct te laten werken. Eerst gaf deze steeds "bitcoin" terug, ongeacht de input. Toen dit eenmaal werkte ontstond het probleem dat de app crashete als er te snel werd getypt, omdat de listview de veranderingen niet kon bijhouden (het doel was om een dynamische listview te maken die update terwijl je typt). Dit is opgelost door een timer die wacht totdat de gebruiker een halve seconde niet typt voordat de lijst wordt geupdate. Zo verloopt het vloeiend en wordt de listview niet overload.

- De grafieken correct laten werken koste even moeite, omdat het een plugin was die we nog niet eerder hadden gebruikt. Nadat de data eenmaal in het goede formaat was ging de rest verder soepel.

- Light & Dark/Day & Night mode heeft ook enige moeite met zich meegebracht. Ik vond het leuk om te implementeren voor de usability, omdat het fijner is voor je ogen. Allereerst moest de standaard layout worden aangepast naar een ander layout type, en moesten er 2 kleurenpalletten worden gemaakt voor de verschillende modus. Tussen de modes wisselen zorgt veelal voor crashes, omdat de wisseling in layout ervoor zorgde dat de listViews de Context niet meer herkende en daarom niet meer konden genereren. Dit is opgelost door de MainActivity zichzelf te laten resetten met de nieuwe layout.

- In het compare fragment worden 2 grafieken samen geplot. Dit gaf enige moeite omdat het niet echt de bedoeling is van de grafiek plugin dat je 2 grafieken samen plot. Om dit op te lossen wordt met behulp van een parameter de index gereset en de kleur van de datalijn aangepast.

- Custom Animations: De animaties van de fragments, zoals bijvoorbeeld fade in en fade out zijn zelf geschreven, en dit was even lastig uitzoeken hoe het precies werkt, maar met resultaat.


## Changes & Decisions
de belangrijkste verschillen in het Design en de eindversie zijn als volgt:
- Het login systeem is weggehaald:
  - De app maakt geen gebruik van persoonlijke data van de gebruiker. Ook zijn sociale interacties met andere gebruikers (nog) niet mogelijk. Daarom zou het niet echt een toevoeging hebben. Ook zou het meer voelen als een plicht dan een voordeel.
  - In plaats hiervan wordt de kleine hoeveelheid benodigde info opgeslagen in de shared preferences.
  - _Ideaal_: gebruikers wel erin houden en daadwerkelijk functionaliteiten geven.

- De grafieken zijn uitgebreider dan in het design.
  - Voor een cryptocurrency app waarbij de kleine veranderingen een grote rol spelen is het belangrijk dat dit goed gevolgd kan worden. Een grafiek waarin amper informatie te zien is is hierdoor compleet niet nuttig. Daarom zijn er nu 2 grafieken geimplementeerd; één voor een snelle weergave en één zeer gedetailleerde voor de gebruikers die dit prefereren.
  - _Ideaal_: nog meer opties voor de grafieken, bijvoorbeeld dat je preciese data kan geven die worden gedisplayed.

 - Light en Dark layout toegevoegd, zodat de user de layout kan kiezen die hij/zij fijner vindt. De voordelen van een dark layout zijn:
   - Fijner lezen 's avonds
   - Minder energie verbruik
   - Is minder vermoeiend voor de ogen

 - De Wallet stond in de optionele items, maar is geïmplementeerd.
   - _Ideaal_: De wallet is nu offline, een optie om bijvoorbeeld online interactie met andere gebruikers toe te staan of bijvoorbeeld statistieken bij te houden zou interessant zijn.
