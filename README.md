# PrinterApp - Dokumentacja

Created: October 21, 2022 3:35 PM
Last Edited Time: November 17, 2022 9:47 AM
Status: Completed 馃弫
Type: Documentation

> current build:`print-171122RC3`
>

# Changelog:

`print-171122RC3`

- BUGFIX Automatyczny reload strony po aktualizacji status贸w drukarek.
- Zmiana layout贸w przycisk贸w funkcyjnych.
- Dodanie funkcjonalno艣ci magazynu.
- Dodanie ostrzerze艅 o brakuj膮cych tonerach w magazynie.
- Dodanie funkcjonalno艣ci PWA (Progressive Web Apps)

`print-051122RC`

- Zmiana layoutu UI na webie.
- Dodanie funkcjonalno艣ci ustawiania lokalizacji drukarek na bazie danych.
- Mo偶liwo艣膰 ustawienia nazwy instancji strony.

# Przeprowadzone testy integracyjne.

Modele drukarek na kt贸rych zosta艂a przetestowana aplikacja:

- Phaser 3600dn
- WC 7855
- VersaLink C405
- VersaLink B405DN
- AltaLink C8155
- VersaLink B600

# Podstawowe za艂o偶enia aplikacji PrintApp oraz problemy na kt贸re odpowiada.

Aplikacja zosta艂a stworzona w celu dokumentowania i przechowywania stanu materia艂贸w eksploatacyjnych drukarek Xerox dost臋pnych na sieci lokalnej. Dzi臋ki aplikacji u偶ytkownik jest w stanie pobra膰 jednocze艣nie stany wszystkich urz膮dze艅 skonfigurowanych w sieci.

# Zastosowana technologia.

- MariaDB
- Spring-Boot
- Java 11
- Hibernate
- Vaadin

Za system przechowywania informacji pobranych od drukarek a tak偶e danych konfiguracyjnych odpowiada instancja opensourcowej bazy danych - MariaDB. Serwis dostarczaj膮cy dane do drukarek korzysta z obiektu SMTP_Connector opartego o podstawowe biblioteki natywne takie jak: Java Sockets a tak偶e opensoursow膮 bibliotek臋 SNMPJ4 umo偶liwiaj膮c膮 prosty parsing danych opartych na kodach OID. Frontend aplikacji jest serwowany za pomoc膮 Tomcata i Spring-Boota. Komponenty wykorzystywane do aplikacji webowej zosta艂y stworzone przy u偶yciu biblioteki Vaadin. Worker zasilaj膮cy danymi baz臋 danych jest synchroniczny i wykorzystuje technologie Hibernate.

# Architektura rozwi膮zania.

![Untitled](PrinterApp%20-%20Dokumentacja%20a1bb5514d99f417bb8e3bb338b5919c0/Untitled.png)

U偶ytkownik obs艂uguj膮cy aplikacje poprzez stron臋 internetow膮 ma dost臋p jedynie do endpointu umo偶liwiaj膮cego wy艣wietlanie tre艣ci przygotowanej wcze艣niej przez objekt TonerPrinter_View. Dost臋p do infrastruktury drukarek i mo偶liwo艣膰 pobierania z nich danych jest odseparowana od pozosta艂ej funkcjonalno艣ci.

# Realizacja parsowania danych z drukarek.

Drukarki udost臋pniaj膮 serwer SNMP daj膮cy dost臋p do wszystkich aktualnych danych drukarki. Kluczem do otrzymania konkretnej infromacji s膮 kody OID. OID to unikatowy聽[identyfikator](https://pl.wikipedia.org/wiki/Identyfikator) obiektu, s艂u偶y do odr贸偶nienia obiektu od innych obiekt贸w oraz do tworzenia odwo艂a艅 do tego obiektu przez system. U偶ytkownik pos艂uguje si臋 nazw膮 obiektu, natomiast system zamienia j膮 na identyfikator. Skonfigurowane kody OID w aplikacji:

**Yellow Cartige Current**

```jsx
.1.3.6.1.2.1.43.11.1.1.9.1.4
```

**Yellow Cartige Max**

```jsx
.1.3.6.1.2.1.43.11.1.1.8.1.4
```

**Cyan Cartrige Max**

```jsx
.1.3.6.1.2.1.43.11.1.1.8.1.2
```

Cyan Cartige Current

```jsx
.1.3.6.1.2.1.43.11.1.1.9.1.2
```

Magenta Cartige Max

```jsx
.1.3.6.1.2.1.43.11.1.1.8.1.3
```

Magenta Cartige Current

```jsx
.1.3.6.1.2.1.43.11.1.1.9.1.3
```

Black Cartige Max

```jsx
.1.3.6.1.2.1.43.11.1.1.8.1.1
```

Black Cartrige Current

```jsx
.1.3.6.1.2.1.43.11.1.1.9.1.1
```

Kody te zosta艂y pozyskane z 3 藕r贸de艂 - jednym z podstawowych by艂a dost臋pna konfiguracja wtyczki Zabbix. Parsowanie danych z drukarki rozpoczyna si臋 od po艂膮czenia z urz膮dzeniem za pomoc膮 obiektu SNMP_Connector:

```jsx
public SNMP_Connector(String ip_address){
        this.ip_address = ip_address;
        server_address = "udp:"+this.ip_address+"/161";
    }
```

Po pod艂膮czeniu do drukarki za pomoc膮 metody connect() zostaje pobierana ca艂a zawarto艣膰 odpowiedzi na request do serwera SNMP.

```jsx
public void connect() throws IOException {
        PrintApplication.database.nl.add("SNMPCONNECTOR","Connector object starting for "+ip_address);
        client = new SNMP_Manager(server_address);
        client.start();
    }
```

Dane pobrane przez obiekt s膮 przechowywane w obiekcie SNMP_Manager:

```jsx
/**
     * Constructor
     * @param address
     */
    public SNMP_Manager(String address){
        ip_address = address;
    }

    /**
     Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous
     * and the listen() method listens for answers.
     @throws IOException
     **/
    public void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp (transport);
        // Do not forget this line!
        transport.listen();
    }
```

A nast臋pnie s膮 parsowane i udost臋pniane jako string:

```jsx
/**
     * Function for getting parameter result from snmp connection
     * @param snmp_oid
     * @return String
     */
    public String get_parameter(String snmp_oid){
        try{
            PrintApplication.database.nl.add("SNMPCONNECTOR","Loading parameter "
                    +snmp_oid+"="+client.getAsString(new OID(snmp_oid)));
            return client.getAsString(new OID(snmp_oid));
        }
        catch(Exception e){
            return null;
        }
    }
```

W przypadku danych dotycz膮cych toner贸w drukarki przechowuj膮 jedynie warto艣ci MAX oraz CURRENT. Aby wyliczy膰 konkretn膮 warto艣膰 nale偶y wykorzysta膰 przeliczenie na procent.

# Aktualizacja danych toner贸w.

Aktualizacja toner贸w jest realizowana poprzez obiekt UpdateTonerData_Scenario:

```jsx
/**
 * Object for creating scenario for Updating Toner data
 */
public class UpdateTonerData_Scenario {

    int printer_jobMAX_id;
    int printer_jobMIN_id;

    ArrayList<Integer> printer_ids;

    Database_Connector database;
    Database_Manager dm;

    /**
     * Constructor
     */
    public UpdateTonerData_Scenario(){
        this.database = PrintApplication.database;
        printer_jobMAX_id = 1;
        printer_jobMIN_id = 2;
        dm = new Database_Manager(database);
        printer_ids = dm.get_printer_ids();
        database.nl.add("SCENARIO","Running update toner data scenario");
    }
}
```

W trakcie aktualizacji jest wykorzystywany obiekt typu Job pozwalaj膮cy na aktualizacje danych:

```jsx
PrinterJob_Engine pje_max = new PrinterJob_Engine(printer_id,printer_jobMAX_id);
pje_max.run();
PrinterJob_Engine pje_min = new PrinterJob_Engine(printer_id,printer_jobMIN_id);
pje_min.run();
ArrayList<Float> max_result = pje_max.get_float_result();
ArrayList<Float> min_result = pje_min.get_float_result();
```

Przyk艂adowe wyliczenie stanu tonera:

```jsx
String cyan_data = "";
try{
    cyan_value = calculate(max_result.get(0),min_result.get(0));
    if ( cyan_value != -69){
        cyan_data = Float.toString(cyan_value);
    }
    else{
        cyan_data = "NaN";
    }
}catch(NullPointerException e){
    cyan_data = "NaN";
}

/**
   * Function for calculating toner %
   * @param max_result
   * @param min_result
   * @return Integer
   */
  float calculate(float max_result,float min_result){
      if ( max_result != -69 && min_result != -69){
          float result = min_result/max_result*100;
          return result;
      }
      else{
          return -69f;
      }
  }
```

# UI Administratora.

G艂贸wne funkcje administracyjne realizowane s膮 przez terminal. Podstawowe komendy:

- job
  - job add job_name *dodanie workera na update konkretnego elementu konfiguracji*
  - job runp print_job_name printer_id *uruchomienie konrketnego workera dla danej drukarki po printer_id*
- elementadd *dodanie elementu eksploatacyjnego*
- updatetoner *aktualizacja danych toner贸w*
- printeradd *dodanie nowej drukarki*
- help *wy艣wietlenie pomocy*
- instance *aktualizacja nazwy instancji wy艣wietlanej na g艂贸wnej stronie*
- info *wy艣wietlanie informacji dotycz膮cych aplikacji*
- exit *wyj艣cie z aplikacji*

# UI U偶ytkownika.

Aplikacja umo偶liwia aktualizacje stanu toner贸w przez u偶ytkownika a tak偶e dodanie nowej drukarki. Z poziomu strony mo偶na doda膰 dane lokalizacyjne ka偶dego urz膮dzenia kt贸re nast臋pnie ustawi膮 si臋 na widoku g艂贸wnym.

# Funkcjonalno艣膰 magazynu.

Aplikacja ofreruje zarz膮dzanie zawarto艣ci膮 magazynu. Istnieje mo偶liwo艣膰 dodawania materia艂贸w eksploatacyjnych. Do ka偶dej drukarki mo偶na dodawa膰 i odejmowa膰 elementy. Aplikacja regularnie sprawdza zawarto艣膰 i daje ostrze偶enia w przypadku braku elementu na drukarce.

# Deploy aplikacji na 艣rodowisku produkcyjnym.

### Wymagania systemowe:

- baza danych MariaDB
- Java Runtime w wersji 11

## Instrukcja deployu

1. Instalacja 艣rodowiska MariaDB.
2. Instalacja Java JRE11.
3. Uruchomienie skryptu na bazie danych **printapp_database_make.sql**
4. Uruchomienie skryptu na bazie danych **server_configuration.sql**
5. Wypakowanie print.zip do lokalizacji uruchomieniowej
6. Uruchomienie aplikacji

```jsx
java -jar printer-1.0.0.jar
```

1. Wykonanie komendy *updatetoner* - pierwsza aktualizacja stanu toner贸w.

Administrator mo偶e wykona膰 r贸wnie偶 uruchomienie aplikacji jako serwis u偶ywaj膮c pliku .service oraz .mount. Aplikacja obs艂uguje uruchomienie w zewn臋trznym shellu i przekierowanie outputu na plik.

by Jakub Wawak 2022 / kubawawak@gmail.com / j.wawak@usp.pl