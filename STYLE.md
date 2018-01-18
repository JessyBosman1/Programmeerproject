## Styleguide

- Camelcasing gebruiken (classes met Uppercase start)
- Zorgen dat er support functies zijn i.p.v. loops
- Zorg dat dingen juist gedocumenteerd zijn, goeie variable namen
- Zorgen voor goede comments, geen onnodige, gebruik doc strings
- Efficiente code, niet omslachtig
- Zorg dat errors goed opgevangen worden
- Goede headers, verwijzingen naar andere sites mocht je deze gebruikt hebben
- Goede indentatie
- Onnodige imports/code eruit.
- Goed gestructureerd, files in duidelijke mapjes.
- Geen onnodige verbintenis tussen code. 


#### Example (android): 
```
public class MainMenu{

     /* code runs on start of screen;  */
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }
    
    /* get the data from the online API */
    private void prepareData(){
      private JSONObject dataObject
      // get the data
      >> Code to get data <<
      
      // parse data to correct format
      >> Code to parse <<
     
    }
}
```
