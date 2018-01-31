# Process Book
## week 1
 **note**: Design keuzes zijn niet beschreven in deze week omdat het huidige design slechts tijdelijk is om de data zichtbaar te maken, en later pas echt wordt gedaan qua opmaak.
##### Maandag 8-1-2018:
- Design
- Functionaliteiten bedenken
- zie link voor gedetailleerd document
- https://github.com/JessyBosman1/Programmeerproject/blob/master/DESIGN.md

##### Dinsdag 9-1-2018:
- Functies
- Objecten
- Wireflow
- zie link voor gedetailleerd document
- https://github.com/JessyBosman1/Programmeerproject/blob/master/README.md

##### Woensdag 10-1-2018:
- Wireflow afmaken
- Data uit api
- **KEUZE**: data naar een ObjectArray, en bij aparte alleen benodigde Object doorsturen. Zo hoeft de data niet steeds te worden opgehaald en hoeft er minder data rondgestuurd te worden. Ook is het makkelijker aparte parameters van een object aan te roepen.
- Grafiek met data uit api
- ![VisualSketchImage](/doc/11_1_image1.png?raw=true)

##### Donderdag 11-1-2018:
- Listview template
- Listview invullen met data
- Bug1: listview genereert lege velden als er geen data meer beschikbaar is
- ![VisualSketchImage](/doc/12_1_image1.png?raw=true)

##### Vrijdag 12-1-2018:
- Presentatie
- Fix Bug1: later data pas ophalen
- Infoscherm grafiek implementeren en onclick op listview
- Searchbar aanmaken
- Bug2: searchbar heeft moeite met goed door de custom listview zoeken (mogelijke oplossing; object doorzoeken en lijst opniew genereren)
- ![VisualSketchImage](/doc/13_1_image1.png?raw=true)

##### Zaterdag 13-1-2018:
- Data object van infoscherm verder invullen in de tekstviews
- Error correction (try, except, etc) voor object
- Bug3: teruggaan vanuit info naar lijst zorgt voor lege addities aan lijst
- ![VisualSketchImage](/doc/14_1_image1.png?raw=true)

##### Zondag 14-1-2018:
- Naar het bos met AJ
- Fix bug3: lijst legen en opnieuw genereren @Override onResume() als er terug wordt gegaan
- slide animatie naar info onclick listview item
- ![VisualSketchImage](/doc/15_1_image1.png?raw=true)

## week 2
##### Maandag 15-1-2018:
- Niewe plot met zogehete CandleSticks in het Infoscherm (advanced graph)
- **KEUZE**: Ik wilde de huidige simpele versie van de graph vervanger door een meer geavanceerde, maar heb deze uiteindelijk met een switch beide gehouden.
de gebruiker kan nu zelf wisselen tussen de simpel en advanced graph. Dit geeft extra gebruiksmogelijkheden en er wordt geen code weggegooid.
- Bug2 **status**: searchbar wil nog steeds niet naar behoren werken en een nieuwe lijst genereren geeft momenteel problemen.
- visuele sterren toegevoegd voor favorite (functionaliteit volgt later).
- ![VisualSketchImage](/doc/15_1_image2.png?raw=true)

##### Dinsdag 16-1-2018:
- as labels voor grafieken; de UNIX waardes worden geparsed, waardoor de asses dagen of daguren weergeeft.
- **keuze** simpele grafiek overgezet naar andere online resource (zelfde als candlestick graph). De oude stond geen labels toe, de nieuwe versie wel en is dus completer.
- as keuze toegevoegd: het is nu mogelijk om te wisselen tussen bijvoorbeeld een 1 uur of een 1 dag weergave.
- ![VisualSketchImage](/doc/16_1_image1.png?raw=true)

##### Woensdag 17-1-2018:
- de sterren achter de listview zijn nu clickable met een longclick. Ze veranderen dan van kleur om aan te geven dat ze zijn toegevoegd, de echt functionaliteit staat op de planning voor morgen.
- **keuze** De zoekbalk werkte, maar een listener om bij iedere change, dus bij iedere letter de lijst dynamisch aan te passen duurde soms te lang, wat resulteert in crashes als het typen sneller gaat dan het updaten. Daarom heb ik deze zoekbalk verwijderd en vervangen door een textveld en een knop om te zoeken. Dit is minder dynamisch, maar zorgt ervoor dat de applicatie wordt overbelast en crashed.
- ![VisualSketchImage](/doc/17_1_image1.png?raw=true)

#### Donderdag 18-1-2018:
- functionaliteiten toegevoegd voor favorites en werken naar behoren. **keuze** Deze worden lokaal opgeslagen omdat dit eenvoudiger is dan communiceren met een online database, en het is geen gevoelige informatie die persé achter een login moet zitten.
- ![Favorites](/doc/18_1_image1.png?raw=true)

#### Vrijdag 19-1-2018:
- Zoekbalk werkt
- zoekbalk resultaten updaten per letter, mits er geen volgende letter volgt binnen een halve seconde. (Dit voelt dynamisch goed aan en overbelast de updatesnelheid van de listview niet.)
- **NOTE** zoekbalk haalt nu data iedere keer opniew online op, dit is beter om te veranderen in het zoeken door het opgehaalde object, om data verbruik te verminderen, dit moet nog geïmplementeerd worden.
- opmaak select buttons grafiek geupdate, is nu meer in-line met design van pagina.
- ![VisualSketchImage](/doc/19_1_image1.png?raw=true)

#### Zondag 21-1-2018:
- AM/PM in grafieken veranderd naar 24 uurs modes
- tweaks in layout en performance

#### Maandag 22-1-2018:
- **keuze** Day/NightMode :
  - Fijner lezen 's avonds
  - Minder energie verbruik
  - Is minder vermoeiend voor de ogen
- Dag en Nacht modes optie gemaakt
- Wisselen met de klik van een knop, preference wordt onthouden voor de volgende keren
- FadeIn en FadeOut animatie tussen de modes
- 2 Logo's gemaakt
- ![VisualSketchImage](/doc/22_1_image1.png?raw=true) ![VisualSketchImage](/doc/22_1_image2.png?raw=true)

#### Dinsdag 23-1-2018
- Wallet toegevoegd, maakt het mogelijk om in te kunnen vullen hoeveel je van een currency hebt, waarna de waarde wordt berekent.
- **keuze** Wallet: de coin en hoeveelheden worden lokaal (shared preferences) opgeslagen. Firebase zou er langer over doen om de data op te halen, en het geeft extra datagebruik terwijl het niet echt nodig is. Door het lokaal op te slaan hoeft er niet bij iedere bewerking contact gemaakt te worden met een online service, en is er ook geen internet verbinding nodig voor deze functionaliteit. De waarde van de coin wordt ook offline opgeslagen en dus berekent met de laatste opgehaalde data. Hierdoor is ook zonder internet beschikbaar hoeveel de coin waard was de laatste keer dat het uit de API is opgehaald.
- ![VisualSketchImage](/doc/23_1_image1.png?raw=true)

#### Woensdag 24-1-2018
- Het is nu mogelijk om uit de wallet te kunnen verwijderen en items te kunnen bewerken.
- Compare functie toegevoegd
- Meldingen aan compare toegevoegd als er een veld niet is ingevuld.
- **keuze** In de compare de waardes in de grafiek omgezet van Real values naar percentages. Dit zorgt ervoor dat beide lijnen zichtbaar zijn, omdat de verschillen soms enorm zijn. (bijv bitcoin heeft waardes rond 10.000, terwijl andere waardes hebben rond de 0.001).
- Invoervelden omgezet naar Autocomplete; als er wordt getypt komen er suggesties, voor gebruiksgemak.
- ![VisualSketchImage](/doc/24_1_image1.png?raw=true)

#### Donderdag 25-1-2018
- Opstart scherm en icoon toegevoegd.
- **keuze** Day en Nightmode switch omgezet van switch naar afwisselende icoontjes van een zon en maan, dit ziet er mooier uit in het design, omdat er geen tekst gebruikt hoeft te worden om duidelijk te maken wat de knop doet.
- **BUG FIX** Bij het verwijderen van een favorite vanuit de favorites lijst werd de verkeerde verwijderd, dit kwam door een probleem in de dictionary. Het werkt nu naar behoren.
- **BUG FIX** De app crashed als er geen data is van de api om de grafiek te vullen. Dit is opgelost met een Try en Except. Als er nu geen data is geeft de grafiek "geen data" weer in plaats van dat er een null reference ontstaat op de lege datapunten.
- ![VisualSketchImage](/doc/25_1_image1.png?raw=true)

#### Vrijdag 26-1-2018
- **Performance** De afbeeldingen van de zon en maan kleiner gemaakt qua pixels, omdat het naar beneden scalen van de HD afbeeldingen zorgde voor een vertraging/lag in de FadeIn en FadeOut, waardoor deze niet meer soepel verliepen.

#### Weekend 27/28 -1-2018
- Testen app medestudent en vice versa, om zo bugs te vinden die je zelf niet altijd vind.
- Zoeken naar bugs en verbeterpunten.

De volgende punten komen naar voren en worden deze week verbeterd:
- Backbutton in Infoscherm werkt niet goed.
- Navigatie crashed als je snel klikt achter elkaar, disable timer toevoegen
- Orientatie fixed zetten
- Logo wordt op geklikt maar heeft geen functie, misschien door laten verwijzen naar home button
- Titel balk in infoscherm een andere kleur omdat deze niet goed overloopt.
- ListViews lopen over elkaar heen van favorites en normaal in bepaalde situaties.

Verder:
- Code netjes maken.
- Comments toevoegen.

## week 4
#### 29-1-2018
Bug fixes van de gevonden problemen (format: ~~_probleem_~~ _oplossing_)
- ~~Backbutton in Infoscherm werkt niet goed.~~ button weggehaald
- ~~Navigatie crashed als je snel klikt achter elkaar~~ disable timer toevoegen
- ~~Crash bij draaien~~ Orientatie fixed zetten op Portrait
- ~~**Usability** Logo wordt op geklikt maar heeft geen functie~~ door laten verwijzen naar startscherm
- ~~Titel balk in infoscherm een andere kleur omdat deze niet goed overloopt.~~ Kleur iets lichter gemaakt
- ~~ListViews lopen over elkaar heen van favorites en normaal in bepaalde situaties.~~ onbackpress aangepast.

#### 30-1-2018
- Code commenten
- Code verplaatsen voor een betere structuur
- Helper Classes aanmaken

#### 31-1-2018
- Afmaken van code opruiming
- Better Code Hub gebruiken om de lange code om te schrijven
- REPORT.md schrijven
- overzicht schermen en bijbehorende files maken
- ![VisualSketchImage](/doc/Class_Overview.png?raw=true)
