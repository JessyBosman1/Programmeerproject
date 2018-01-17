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
- ![VisualSketchImage](/doc/16_1_image1.png?raw=true)

##### Woensdag 17-1-2018:
- de sterren achter de listview zijn nu clickable met een longclick. Ze veranderen dan van kleur om aan te geven dat ze zijn toegevoegd, de echt functionaliteit staat op de planning voor morgen. 
- **keuze** De zoekbalk werkte, maar een listener om bij iedere change, dus bij iedere letter de lijst dynamisch aan te passen duurde soms te lang, wat resulteert in crashes als het typen sneller gaat dan het updaten. Daarom heb ik deze zoekbalk verwijderd en vervangen door een textveld en een knop om te zoeken. Dit is minder dynamisch, maar zorgt ervoor dat de applicatie wordt overbelast en crashed. 
- ![VisualSketchImage](/doc/17_1_image1.png?raw=true)
