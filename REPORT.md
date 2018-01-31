# Final Report
[![BCHcompliance](https://bettercodehub.com/edge/badge/JessyBosman1/Programmeerproject?branch=master)](https://bettercodehub.com/)
**Create a report (REPORT.md), based on your design document, containing important decisions that you’ve made, e.g. where you changed your mind during the past weeks. This is how you show the reviewer that you actually understand what you have done.

Start with a short description of your application (like in the README.md, but very short, including a single screen shot).**

De applicatie maakt het mogelijk de verschillende cryptocurrencies naast elkaar te houden. De waardes van verschillende currencies kunnen in verschillende tijdsperiodes worden geplot in een grafiek. Ook is het mogelijk de grafieken samen te plotten met een percentage weergave om zo 2 currencies te vergelijken. De applicatie heeft ook de mogelijkheid om zelf een wallet bij te houden die de waarde geeft van de currencies die de gebruiker heeft ingevoerd.

![VisualSketchImage](/doc/23_1_image1.png?raw=true)


**Clearly describe the technical design: how is the functionality implemented in your code? This should be like your DESIGN.md but updated to reflect the final application. First, give a high level overview, which helps us navigate and understand the total of your code (which components are there?). Second, go into detail, and describe the modules/classes (apps) files/functions (data) and how they relate.**
### Technical Design
Allereerst een overview van welke schermen welke classes gebruiken, om duidelijk te maken hoe het totaalplaatje met elkaar samenhangt:
![VisualSketchImage](/doc/Class_Overview.png?raw=true)

### details
####**SplashScreen.java**
Dit script displayed het opstart scherm van de app en gaat vervolgens door naar de main activity (MainActivity.java)

####**MainActivity.java**
Dit is de fragment Container van de app. Dit script behandeld de navigation bottom bar en zorgt ervoor dat de juiste fragmenten worden ingeladen naarmate er wordt geklikt op de navigatiebalk.

Main Activity regelt de navigatie naar de andere fragmenten. Ook regelt MainActivity de overschakeling van de Dark en Light layout modes (day&night modus).

####**MainListFragment.java**
de MainListFragment handeld de verschillende listviews af die cryptocurrencies listen(standaard, favorites en zoeken). Het script neemt een argument (Method), die bepaald welke lijst gegenereerd moet worden. Dit wordt zo gedaan om dubbele code te voorkomen bij het genereren van de lijsten.

de rijen zelf worden vormgegeven door de CoinListAdapter, zie kopje.

Na het genereren van de juiste lijst worden de listeners gezet op de rows. Een enkele klik zorgt ervoor dat er naar de InfoActivity wordt overgeschakelt (via FragmentSwitcher.java).

Een longclick slaat het corresponderende item op als favorite, of verwijderd het item juist als deze al geregistreerd is als favorite. Dit wordt opgeslagen in de Shared Preferences.

De zoekbalk wordt hier ook geinitialiseerd. Deze werkt met een timer; als de user typt en vervolgens 500 milliseconde niet typt wordt de lijst geupdate naar de zoekterm. Het 500 milliseconde interval wordt gebruikt zodat niet bij iedere letter een update plaatsvindt wat het systeem overbelast. Hierdoor onstaat een dynamisch effect zonder dat de listview crashed door het grote aantal interacties.

####**CoinListAdapter.java _(helperclass)_**
Deze custom list adapter maakt iedere rij van het MainListFragment. Het object wordt meegestuurt en hier uit elkaar gehaald tot verschillende parameters. Het icoontje wordt ingeladen vanuit Coinmarketcap.com 's website. De andere waardes komen uit het Object zelf. De percentages worden gekleurt aan de hand van of het een positieve of negatieve waarde is.

Er wordt gekeken of het item een favorite is vanuit de shared prefs en hier wordt de toggle van de favorite icon bepaald.

####**FragmentSwitcher.java _(helperclass)_**
Deze class stuurt het object door vanuit een MainListFragment naar de InfoActivity, waarbij het object wordt doorgestuurt.

####**InfoActivity.java**
InfoActivity managed het scherm dat de uitgebreide informatie en de grafieken weergeeft.

De toggle buttons bepalen welke grafiek wordt gedisplayed. Er is een keuze tussen een advanced en een standaard weergave. Het is ook mogelijk om te wisselen tussen een 1 uur, 12 uur, 1 dag en 1 week weergave.

De percentage teksten worden aangepast qua kleur afhankelijk van of het positief of negatief is.

####**PlotSimpleGraph.java _(helperclass)_**
Hier wordt de standaard graph aangemaakt met behulp van de datapunten. De grafiek wordt weergegeven met behulp van [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart "MPAndroidChart"). De datapunten worden bepaald aan de hand van de keuze in tijdweergave (bijv 12 uurs weergave).

Verder wordt in dit script de opmaak van de grafiek afgesteld.

####**PlotCandleStickGraph.java _(helperclass)_**
De geavanceerde grafiek, ook wel Candle Stick Graph, wordt hier gemaakt, met behulp van [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart "MPAndroidChart"). De datapunten worden verwerkt in een Array, en de as labels worden gegenerereert aan de hand van de Unix Timestamp(zie UnixConverter.java).

####**UnixConverter.java _(helperclass)_**
De unix time is het aantal verstreken seconden vanaf 1 januari 1970. Om dit bruikbaar te maken moet het worden omgezet naar een leesbaar, handig formaat. Dit script rekent dat uit en formateert het. De unix tijd 1517428280 staat bijvoorbeeld voor 01/31/2018 7:51pm. De omgerekende waardes worden gebruikt als x-as labels voor de grafieken.

####**CompareFragment.java**

####**WalletFragment.java**

####**WalletAdapter.java _(helperclass)_**

**Clearly describe challenges that your have met during development. Document all important changes that your have made with regard to your design document (from the PROCESS.md). Here, we can see how much you have learned in the past month.**



**Defend your decisions by writing an argument of a most a single paragraph. Why was it good to do it different than you thought before? Are there trade-offs for your current solution? In an ideal world, given much more time, would you choose another solution?**


**Make sure the document is complete and reflects the final state of the application. The document will be an important part of your grade.**
