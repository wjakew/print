# PrinterApp - Dokumentacja

Created: October 21, 2022 3:35 PM
Last Edited Time: February 1, 2023 10:22 AM
Status: Completed 🏁
Type: Documentation

> current build:`print-010223RC1`
> 

# Changelog:

`print-010223RC1`

- Dodano pole wyszukiwania na stronie głównej.
- Wymagana aktualizacja tabeli HEALTH kodem ze skryptu z nowej wersji 101 bazy danych.
- Dodano endpoint REST do pobierania aktualnych danych o drukarkach.

`print-160123RC5`

- Duża zmiana UI.
- Dodanie strony drukarki, możliwości wyszukiwania po numerze seryjnym.
- Łatwiejsze dodawanie elementów magazynu.
- Dodanie możliwości generowania template maila na uzupełnienie stanów magazynowych.
- Zmiany w komponentach typu grid.
- Utworzenie prostego layoutu na przestrzeni całej aplikacji webowej.

`print-050123RC4`

- Zmiana UI.
- Dodanie okna drukarek.
- Ułatwienie wyszukiwania obiektów.
- Dodanie statystyk pobieranych snapshotów.
- Aktualizacja parsera JSON - wykryta podatność.

`print-281122RC3`

- Dodano przycisk odświeżania strony głownej
- Poprawiono konfiguracje bazy danych - zwiększono czas sesji
- Dodano nowy element do magazynu: WASTE CONTAINER.
- Dodano strone wyświeltającą event log - dla zarządzania
- Małe poprawy interfejsu

`print-171122RC3`

- BUGFIX Automatyczny reload strony po aktualizacji statusów drukarek.
- Zmiana layoutów przycisków funkcyjnych.
- Dodanie funkcjonalności magazynu.
- Dodanie ostrzerzeń o brakujących tonerach w magazynie.
- Dodanie funkcjonalności PWA (Progressive Web Apps)

`print-051122RC`

- Zmiana layoutu UI na webie.
- Dodanie funkcjonalności ustawiania lokalizacji drukarek na bazie danych.
- Możliwość ustawienia nazwy instancji strony.

# Przeprowadzone testy integracyjne.

Modele drukarek na których została przetestowana aplikacja:

- Phaser 3600dn
- WC 7855
- VersaLink C405
- VersaLink B405DN
- AltaLink C8155
- VersaLink B600

# Podstawowe założenia aplikacji PrintApp oraz problemy na które odpowiada.

Aplikacja została stworzona w celu dokumentowania i przechowywania stanu materiałów eksploatacyjnych drukarek Xerox dostępnych na sieci lokalnej. Dzięki aplikacji użytkownik jest w stanie pobrać jednocześnie stany wszystkich urządzeń skonfigurowanych w sieci.

# Zastosowana technologia.

- MariaDB
- Spring-Boot
- Java 11
- Hibernate
- Vaadin

Za system przechowywania informacji pobranych od drukarek a także danych konfiguracyjnych odpowiada instancja opensourcowej bazy danych - MariaDB. Serwis dostarczający dane do drukarek korzysta z obiektu SMTP_Connector opartego o podstawowe biblioteki natywne takie jak: Java Sockets a także opensoursową bibliotekę SNMPJ4 umożliwiającą prosty parsing danych opartych na kodach OID. Frontend aplikacji jest serwowany za pomocą Tomcata i Spring-Boota. Komponenty wykorzystywane do aplikacji webowej zostały stworzone przy użyciu biblioteki Vaadin. Worker zasilający danymi bazę danych jest synchroniczny i wykorzystuje technologie Hibernate.

# Architektura rozwiązania.

![Untitled](PrinterApp%20-%20Dokumentacja%20a1bb5514d99f417bb8e3bb338b5919c0/Untitled.png)

Użytkownik obsługujący aplikacje poprzez stronę internetową ma dostęp jedynie do endpointu umożliwiającego wyświetlanie treści przygotowanej wcześniej przez objekt TonerPrinter_View. Dostęp do infrastruktury drukarek i możliwość pobierania z nich danych jest odseparowana od pozostałej funkcjonalności.

# Realizacja parsowania danych z drukarek.

Drukarki udostępniają serwer SNMP dający dostęp do wszystkich aktualnych danych drukarki. Kluczem do otrzymania konkretnej infromacji są kody OID. OID to unikatowy [identyfikator](https://pl.wikipedia.org/wiki/Identyfikator) obiektu, służy do odróżnienia obiektu od innych obiektów oraz do tworzenia odwołań do tego obiektu przez system. Użytkownik posługuje się nazwą obiektu, natomiast system zamienia ją na identyfikator. Skonfigurowane kody OID w aplikacji:

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

Kody te zostały pozyskane z 3 źródeł - jednym z podstawowych była dostępna konfiguracja wtyczki Zabbix. Parsowanie danych z drukarki rozpoczyna się od połączenia z urządzeniem za pomocą obiektu SNMP_Connector:

```jsx
public SNMP_Connector(String ip_address){
        this.ip_address = ip_address;
        server_address = "udp:"+this.ip_address+"/161";
    }
```

Po podłączeniu do drukarki za pomocą metody connect() zostaje pobierana cała zawartość odpowiedzi na request do serwera SNMP.

```jsx
public void connect() throws IOException {
        PrintApplication.database.nl.add("SNMPCONNECTOR","Connector object starting for "+ip_address);
        client = new SNMP_Manager(server_address);
        client.start();
    }
```

Dane pobrane przez obiekt są przechowywane w obiekcie SNMP_Manager:

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

A następnie są parsowane i udostępniane jako string:

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

W przypadku danych dotyczących tonerów drukarki przechowują jedynie wartości MAX oraz CURRENT. Aby wyliczyć konkretną wartość należy wykorzystać przeliczenie na procent.

# Aktualizacja danych tonerów.

Aktualizacja tonerów jest realizowana poprzez obiekt UpdateTonerData_Scenario:

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

W trakcie aktualizacji jest wykorzystywany obiekt typu Job pozwalający na aktualizacje danych:

```jsx
PrinterJob_Engine pje_max = new PrinterJob_Engine(printer_id,printer_jobMAX_id);
pje_max.run();
PrinterJob_Engine pje_min = new PrinterJob_Engine(printer_id,printer_jobMIN_id);
pje_min.run();
ArrayList<Float> max_result = pje_max.get_float_result();
ArrayList<Float> min_result = pje_min.get_float_result();
```

Przykładowe wyliczenie stanu tonera:

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

Główne funkcje administracyjne realizowane są przez terminal. Podstawowe komendy:

- job
    - job add job_name *dodanie workera na update konkretnego elementu konfiguracji*
    - job runp print_job_name printer_id *uruchomienie konrketnego workera dla danej drukarki po printer_id*
- elementadd *dodanie elementu eksploatacyjnego*
- updatetoner *aktualizacja danych tonerów*
- printeradd *dodanie nowej drukarki*
- help *wyświetlenie pomocy*
- instance *aktualizacja nazwy instancji wyświetlanej na głównej stronie*
- info *wyświetlanie informacji dotyczących aplikacji*
- exit *wyjście z aplikacji i wyłączenie serwera*

# UI Użytkownika.

Aplikacja umożliwia aktualizacje stanu tonerów przez użytkownika a także dodanie nowej drukarki. Z poziomu strony można dodać dane lokalizacyjne każdego urządzenia które następnie ustawią się na widoku głównym.

# Funkcjonalność magazynu.

Aplikacja ofreruje zarządzanie zawartością magazynu. Istnieje możliwość dodawania materiałów eksploatacyjnych. Do każdej drukarki można dodawać i odejmować elementy. Aplikacja regularnie sprawdza zawartość i daje ostrzeżenia w przypadku braku elementu na drukarce.

# Obsługa loga.

Aplikacja umożliwia wyświetlenie wszystkich zaistniałych eventów w aplikacji. 

# Deploy aplikacji na środowisku produkcyjnym.

### Wymagania systemowe:

- baza danych MariaDB
- Java Runtime w wersji 11

## Instrukcja deployu

1. Instalacja środowiska MariaDB.
2. Instalacja Java JRE11.
3. Uruchomienie skryptu na bazie danych **printapp_database_make.sql**
4. Uruchomienie skryptu na bazie danych **server_configuration.sql**
5. Wypakowanie print.zip do lokalizacji uruchomieniowej
6. Uruchomienie aplikacji

```jsx
java -jar printer-1.0.0.jar
```

1. Wykonanie komendy *updatetoner* - pierwsza aktualizacja stanu tonerów.

Administrator może wykonać również uruchomienie aplikacji jako serwis używając pliku .service oraz .mount. Aplikacja obsługuje uruchomienie w zewnętrznym shellu i przekierowanie outputu na plik.

by Jakub Wawak 2022 / kubawawak@gmail.com / j.wawak@usp.pl
