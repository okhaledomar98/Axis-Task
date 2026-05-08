# Tealium Ecommerce Automation Framework

A production-grade Selenium + TestNG automation framework for [Tealium Ecommerce Demo](https://ecommerce.tealiumdemo.com).
Built with clean architecture, zero hardcoded data, and full Allure reporting.

---

## What This Framework Tests

| Feature | Test Class | Scenarios |
|---|---|---|
| User Registration | `RegistrationTests` | Valid registration, missing fields |
| User Login | `LoginTests` | Valid login, wrong password |
| Add to Cart (E2E) | `AddToCartTests` | Login → Browse → Sort → Select Product → Add to Cart |

---

## Framework Architecture

```
src/
├── main/java/com/axis/automation/
│   ├── core/               ← Foundation layer
│   ├── pages/              ← Page Objects
│   ├── components/         ← Reusable UI components
│   ├── data/               ← Test data models
│   ├── utils/              ← Helper utilities
│   ├── config/             ← Configuration reader
│   ├── constants/          ← Timeout constants
│   └── enums/              ← Browser type enum
│
└── test/java/com/axis/automation/
    ├── tests/              ← Test classes
    └── listeners/          ← TestNG listeners
```

---

## Layer Breakdown

### Core Layer — `core/`

#### `DriverFactory.java`
Creates and manages the WebDriver instance using **ThreadLocal** — each thread gets its own browser.
This makes the framework ready for parallel test execution without browser conflicts.
Supports Chrome, Firefox, Edge via `BrowserType` enum.

#### `BasePage.java`
The parent class for all Page Objects. Contains every Selenium interaction method used across the framework:
- `click()`, `type()`, `getText()`, `getAttribute()`
- `hover()`, `scrollToElement()`, `clickWithJs()`, `fireMouseClick()`
- `isDisplayed()`, `isEnabled()`, `selectByVisibleText()`
- `handleConsentModal()` — handles the cookie consent popup on every page load

All Page Objects extend `BasePage` and inherit these methods — no duplication.

#### `BaseTest.java`
The parent class for all Test classes.
- `@BeforeMethod` — launches the browser, opens the site, handles consent modal, initializes `HomePage`
- `@AfterMethod` — closes the browser and captures a screenshot on failure
- All test classes extend `BaseTest` and inherit browser setup/teardown automatically

---

### Page Object Layer — `pages/`

Each class represents one page in the application. Only contains locators and actions for that page.

| Class | Represents |
|---|---|
| `HomePage.java` | Landing page — provides access to `HeaderComponent` |
| `LoginPage.java` | Login form — fills credentials, submits, returns `AccountDashboardPage` |
| `RegisterPage.java` | Registration form — fills all fields, submits |
| `AccountDashboardPage.java` | Logged-in dashboard — verifies login success |
| `ShoesPage.java` | Shoes listing page — sorts by price, finds and navigates to a product by name |
| `ProductDetailsPage.java` | Product detail page — selects color/size, adds to cart |
| `CartPage.java` | Shopping cart page — verifies item was added successfully |

**Fluent Navigation Pattern:**
Methods that navigate to a new page return the next Page Object:
```java
homePage.getHeader().clickLogin().login(user)  // returns AccountDashboardPage
```

---

### Component Layer — `components/`

#### `HeaderComponent.java`
The site header appears on every page. Instead of duplicating header logic in every Page Object,
it is extracted into a reusable component:
- `clickLogin()` → opens dropdown, clicks Log In, returns `LoginPage`
- `clickRegister()` → opens dropdown, clicks Register, returns `RegisterPage`
- `hoverOverAccessories()` → hovers over Accessories menu
- `clickShoes()` → clicks Shoes submenu, returns `ShoesPage`
- `isAccessoriesDropdownVisible()` → verifies dropdown appeared after hover
- `isUserLoggedIn()` → checks if My Account link is visible

---

### Data Layer — `data/`

#### `User.java`
A Lombok `@Data` POJO that holds user data fields: `firstName`, `lastName`, `email`, `password`.
No hardcoded values — data comes from JSON files.

#### `TestDataReader.java`
Reads test data from JSON files using Jackson Databind.
```java
User user = TestDataReader.getExistingUser();    // reads from users.json
User user = TestDataReader.getValidUser();
User user = TestDataReader.getInvalidLoginUser();
```

#### `src/test/resources/testdata/users.json`
Single source of truth for all user data. To change test users, edit this file only — no code changes needed.

---

### Utilities — `utils/`

#### `WaitUtils.java`
All explicit wait methods in one place. **Never use `Thread.sleep()` in tests.**
- `waitForVisibility()` — waits until element is visible
- `waitForPresence()` — waits until element exists in DOM
- `waitForClickability()` — waits until element can be clicked
- `waitForUrlContains()` — waits until URL contains a string
- All methods have overloads with custom timeout support

#### `LoggerUtils.java`
A wrapper around Log4j2 that prefixes every log with the calling class name automatically.
Provides `info()`, `warn()`, `error()` methods used throughout the framework.

#### `ScreenshotUtils.java`
Captures a PNG screenshot and saves it to `target/screenshots/` on test failure.
Called automatically by `TestListener`.

---

### Listeners — `listeners/`

#### `TestListener.java`
Implements `ITestListener`. Runs automatically for every test:
- Logs test start, pass, fail, skip with emoji markers
- Captures a screenshot on every failure
- Attaches screenshot to the Allure report

#### `RetryAnalyzer.java`
Implements `IRetryAnalyzer`. Automatically retries a failed test up to **2 times** before marking it as failed.
Reduces flakiness caused by network delays or slow page loads.

---

### Configuration — `config/`

#### `ConfigReader.java`
Reads values from `src/test/resources/config.properties`.
Currently provides: `getBaseUrl()`, `getBrowser()`.
Add new config values here without touching test code.

#### `config.properties`
```properties
base.url=https://ecommerce.tealiumdemo.com
browser=chrome
```

---

### Constants — `constants/`

#### `TimeoutConstants.java`
```java
SHORT_WAIT  = 5   // seconds — for consent modals, quick checks
MEDIUM_WAIT = 10  // seconds — default for most waits
LONG_WAIT   = 20  // seconds — for slow pages and navigation
```

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- Google Chrome (latest)
- ChromeDriver (auto-managed by Selenium Manager)

### Run All Tests
```powershell
mvn clean test "-Dsurefire.suiteXmlFiles=testng.xml"
```

### Run a Specific Test Class
```powershell
mvn clean test "-Dtest=LoginTests"
```

### Run a Specific Test Method
```powershell
mvn clean test "-Dtest=LoginTests#tc01_validLogin"
```

---

## Allure Report

### Generate the report
```powershell
mvn allure:report
```
Report is saved to: `target/site/allure-maven-plugin/index.html`

### Open report in browser (recommended)
```powershell
mvn allure:serve
```
This generates the report AND opens it automatically in your browser.

---

## Project Structure (Full)

```
tealium-automation-framework/
├── src/
│   ├── main/java/com/axis/automation/
│   │   ├── components/
│   │   │   └── HeaderComponent.java
│   │   ├── config/
│   │   │   └── ConfigReader.java
│   │   ├── constants/
│   │   │   └── TimeoutConstants.java
│   │   ├── core/
│   │   │   ├── BasePage.java
│   │   │   ├── BaseTest.java
│   │   │   └── DriverFactory.java
│   │   ├── data/
│   │   │   ├── TestDataReader.java
│   │   │   └── User.java
│   │   ├── enums/
│   │   │   └── BrowserType.java
│   │   ├── pages/
│   │   │   ├── AccountDashboardPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── HomePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductDetailsPage.java
│   │   │   ├── RegisterPage.java
│   │   │   └── ShoesPage.java
│   │   └── utils/
│   │       ├── LoggerUtils.java
│   │       ├── ScreenshotUtils.java
│   │       └── WaitUtils.java
│   └── test/
│       ├── java/com/axis/automation/
│       │   ├── listeners/
│       │   │   ├── RetryAnalyzer.java
│       │   │   └── TestListener.java
│       │   └── tests/
│       │       ├── cart/
│       │       │   └── AddToCartTests.java
│       │       ├── login/
│       │       │   └── LoginTests.java
│       │       └── registration/
│       │           └── RegistrationTests.java
│       └── resources/
│           ├── config.properties
│           ├── log4j2.xml
│           └── testdata/
│               └── users.json
├── testng.xml
└── pom.xml
```

---

## Key Design Decisions

| Decision | Reason |
|---|---|
| Page Object Model | Each page is one class — changes in UI need only one file update |
| BasePage with helper methods | No duplicated Selenium code across page objects |
| ThreadLocal DriverFactory | Framework is parallel-execution ready |
| JSON test data | Test data is separated from test logic — easy to maintain |
| Fluent navigation | `clickLogin().login(user)` reads like a user story |
| JS click for blocked elements | Site has a fixed banner that blocks regular Selenium clicks |
| select element for swatches | Magento swatches use Prototype.js — select manipulation is more reliable |
| WaitUtils only | No `Thread.sleep()` anywhere — waits are smart and fast |
| RetryAnalyzer | Handles network flakiness without manual re-runs |
| Allure annotations | Every test has `@Epic`, `@Feature`, `@Story`, `@Severity` for rich reports |
