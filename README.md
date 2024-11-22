# PrinterApp - Documentation

Created: October 21, 2022, 3:35 PM  
Last Edited Time: February 1, 2023, 10:22 AM  
Status: Completed 🏁  
Type: Documentation  

> Current build: `print-070623RC1`

---

# Changelog:

### `print-070623RC1`
- Added auto reconnect for the database.
- Added auto toner updater.
- Updated dependencies.
- Security and UI improvements.

### `print-010223RC1`
- Added a search bar on the homepage.
- Database HEALTH table update required using a script from the new version 101 database.
- Added REST endpoint to fetch current printer data.

### `print-160123RC5`
- Major UI overhaul.
- Added printer page with the ability to search by serial number.
- Easier inventory management.
- Added email template generation for stock replenishment.
- Changes to grid components.
- Created a simple layout for the entire web application.

### `print-050123RC4`
- UI changes.
- Added printer window.
- Simplified object searching.
- Added statistics for snapshots retrieved.
- Updated JSON parser to fix a detected vulnerability.

### `print-281122RC3`
- Added refresh button for the homepage.
- Improved database configuration by increasing session timeout.
- Added a new inventory item: WASTE CONTAINER.
- Added a page displaying event logs for management.
- Minor interface fixes.

### `print-171122RC3`
- BUGFIX: Automatic page reload after updating printer statuses.
- Changed functional button layouts.
- Added inventory management functionality.
- Added warnings for missing toners in inventory.
- Added PWA (Progressive Web App) functionality.

### `print-051122RC`
- Updated web UI layout.
- Added functionality to set printer locations in the database.
- Ability to set the name of the instance on the main page.

---

# Conducted Integration Tests

Printer models tested with the application:
- Phaser 3600dn
- WC 7855
- VersaLink C405
- VersaLink B405DN
- AltaLink C8155
- VersaLink B600

---

# Core Objectives and Challenges Addressed by PrintApp

The application was designed to document and track the consumable states of Xerox printers available on a local network. It enables users to simultaneously retrieve the statuses of all devices configured on the network.

---

# Technologies Used

- **MariaDB**
- **Spring-Boot**
- **Java 11**
- **Hibernate**
- **Vaadin**

The application uses an open-source MariaDB database for storing information retrieved from printers and configuration data. The service that fetches printer data uses an SMTP_Connector object built on native Java libraries such as Java Sockets and the open-source SNMPJ4 library for simple parsing of OID-based data. The application's frontend is served via Tomcat and Spring-Boot. Web application components are built using the Vaadin library. A synchronous worker powered by Hibernate updates the database.

---

# Solution Architecture

Users accessing the application through the web page can only interact with an endpoint displaying content prepared by the TonerPrinter_View object. Access to printer infrastructure and data retrieval is isolated from other functionalities.

---

# Parsing Printer Data

Printers provide an SNMP server that offers access to all current printer data. The key to obtaining specific information lies in OID codes. OIDs are unique identifiers that differentiate objects and allow systems to reference them. Configured OIDs in the application include:

- **Yellow Cartridge Current:** `.1.3.6.1.2.1.43.11.1.1.9.1.4`
- **Yellow Cartridge Max:** `.1.3.6.1.2.1.43.11.1.1.8.1.4`

And so on for Cyan, Magenta, and Black cartridges.

The data parsing process begins with connecting to the device via the SNMP_Connector object. Once connected, responses from the SNMP server are retrieved, parsed, and exposed as strings.

---

# Toner Data Updates

Toner updates are managed using the `UpdateTonerData_Scenario` object. This object calculates toner levels by comparing current and maximum values. Example calculation:

```java
float calculate(float maxResult, float minResult) {
    if (maxResult != -69 && minResult != -69) {
        return (minResult / maxResult) * 100;
    }
    return -69f;
}
```

---

# Administrator UI

Administrative functions are performed via a terminal with basic commands such as:
- `job` – manage workers for specific configuration updates.
- `updatetoner` – update toner data.
- `printeradd` – add a new printer.

---

# User UI

The application allows users to update toner statuses and add new printers. Location data for devices can also be added from the user interface.

---

# Inventory Management

The app offers inventory management, enabling the addition and removal of consumables for each printer. It also issues warnings when supplies are low.

---

# Event Logs

The application displays all recorded events for auditing and monitoring purposes.

---

# Deployment to Production Environment

### System Requirements:
- MariaDB database
- Java Runtime Environment (JRE) 11

### Deployment Steps:
1. Install MariaDB.
2. Install Java JRE11.
3. Execute `printapp_database_make.sql` on the database.
4. Execute `server_configuration.sql` on the database.
5. Extract `print.zip` to the deployment location.
6. Run the application:

```bash
java -jar printer-1.0.0.jar
```

7. Perform the initial toner update using the `updatetoner` command.

The administrator can also configure the application as a service.

---

*By Jakub Wawak (2022)*  
kubawawak@gmail.com / j.wawak@usp.pl
