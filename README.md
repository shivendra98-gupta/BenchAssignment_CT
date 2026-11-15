# SauceDemo E-Commerce Test Automation Framework

An enterprise-grade, robust, and extensible **Selenium Java Hybrid Test Automation Framework** implementing the **Page Object Model (POM)** design pattern for the **SauceDemo E-commerce Checkout Flow** ([https://www.saucedemo.com](https://www.saucedemo.com)).

---

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Key Features & Technical Guidelines](#key-features--technical-guidelines)
3. [Framework Architecture & Folder Structure](#framework-architecture--folder-structure)
4. [Tech Stack & Dependencies](#tech-stack--dependencies)
5. [Prerequisites](#prerequisites)
6. [Setup and Installation](#setup-and-installation)
7. [Running the Tests](#running-the-tests)
   - [Command Line (Maven)](#command-line-maven)
   - [Parallel Test Execution](#parallel-test-execution)
   - [Cross-Browser & Headless Mode](#cross-browser--headless-mode)
8. [Test Scenarios Matrix](#test-scenarios-matrix)
9. [PDF Validation of Order Confirmation](#pdf-validation-of-order-confirmation)
10. [Test Reporting and Logging](#test-reporting-and-logging)
11. [CI/CD Workflow (GitHub Actions)](#cicd-workflow-github-actions)

---

## 🚀 Project Overview

This repository automates the end-to-end checkout workflow of the **SauceDemo** application as specified in the assignment:
1. **User Authentication:** Validate standard and edge-case login scenarios.
2. **Product Catalog & Cart Management:** Add/remove items, filter/sort products, and verify cart badge counts.
3. **Checkout Step One:** Enter customer billing information with negative input validations.
4. **Checkout Step Two:** Price verification (Item Total, Tax calculation, and Grand Total assertion).
5. **Order Completion:** Validate confirmation banner, dispatch text, and visual elements.
6. **PDF Order Confirmation Validation:** Generate and validate invoice receipts using Apache PDFBox.

---

## 🌟 Key Features & Technical Guidelines

- **Page Object Model (POM) / Hybrid Design:** Clean separation of concerns between web locators, page actions, and test assertions.
- **Thread-Safe Parallel Execution:** Implements `ThreadLocal<WebDriver>` and `ThreadLocal<ExtentTest>` to support concurrent multi-threaded execution without collision.
- **Data-Driven Testing:** JSON-driven test data via Jackson Databind and TestNG `@DataProvider`.
- **Comprehensive Assertions:** Soft and hard assertions for calculation of subtotal, tax rate, and grand totals.
- **Rich HTML Reporting:** ExtentReports 5 integration with automated failure screenshots embedded as Base64.
- **PDF Content Verification:** Apache PDFBox utility for inspecting generated order confirmation receipts.
- **Environment & Centralized Configuration:** Easily configurable via `config.properties` and command-line flags.
- **CI/CD Ready:** Complete GitHub Actions YAML pipeline for automated execution on push and PR.

---

## 📁 Framework Architecture & Folder Structure

```
Bench_Assignment_CT/
│
├── .github/
│   └── workflows/
│       └── ci-cd-tests.yml                  # GitHub Actions CI/CD pipeline workflow
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── saucedemo/
│   │   │           ├── constants/
│   │   │           │   └── FrameworkConstants.java   # Central framework paths and default constants
│   │   │           ├── driver/
│   │   │           │   ├── DriverFactory.java        # WebDriver initialization (Chrome, Firefox, Edge)
│   │   │           │   └── DriverManager.java        # ThreadLocal WebDriver storage for parallel runs
│   │   │           ├── pages/
│   │   │           │   ├── BasePage.java             # Superclass with common WebElements and utilities
│   │   │           │   ├── LoginPage.java            # SauceDemo Login page locators and actions
│   │   │           │   ├── ProductsPage.java         # Inventory catalog, sorting, and add-to-cart actions
│   │   │           │   ├── CartPage.java             # Shopping cart review, remove, and checkout navigation
│   │   │           │   ├── CheckoutStepOnePage.java  # Customer information form & validations
│   │   │           │   ├── CheckoutStepTwoPage.java  # Order overview, calculations & finish actions
│   │   │           │   └── CheckoutCompletePage.java # Order confirmation page locators and messages
│   │   │           └── utils/
│   │   │               ├── ConfigReader.java         # Reads properties with CLI override support
│   │   │               ├── ElementUtil.java          # Explicit waits, clicks, and JS execution
│   │   │               ├── ExtentReportManager.java  # ExtentReports 5 lifecycle and ThreadLocal logger
│   │   │               ├── JsonDataReader.java       # Jackson JSON reader for DataProviders
│   │   │               ├── PDFValidationUtil.java    # Apache PDFBox reader and confirmation PDF builder
│   │   │               └── ScreenshotUtil.java       # Captures screenshots as PNG files and Base64
│   │   └── resources/
│   │       ├── config/
│   │       │   └── config.properties                 # Environment, browser, and timeout configurations
│   │       └── log4j2.xml                            # Log4j2 console and rolling file logging layout
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── saucedemo/
│       │           ├── base/
│       │           │   └── BaseTest.java             # Setup (@BeforeMethod) and teardown (@AfterMethod)
│       │           ├── listeners/
│       │           │   └── TestListener.java         # TestNG listener for ExtentReports and screenshots
│       │           └── tests/
│       │               ├── LoginTest.java            # Positive, negative, and data-driven login tests
│       │               ├── ProductCatalogTest.java   # Inventory display, sorting, badge count tests
│       │               ├── CartTest.java             # Cart items display, removal, and navigation tests
│       │               ├── CheckoutTest.java         # Information form, negative validations, cancel test
│       │               ├── EndToEndCheckoutTest.java # Full checkout with price calculations assertion
│       │               └── PDFConfirmationTest.java  # Order confirmation receipt PDF validation
│       └── resources/
│           ├── runners/
│           │   ├── testng.xml                        # Main comprehensive suite
│           │   ├── testng-parallel.xml               # Multi-threaded parallel execution suite
│           │   └── testng-smoke.xml                  # Quick smoke test suite
│           └── testdata/
│               ├── logindata.json                    # Valid and invalid credentials dataset
│               └── checkoutdata.json                 # Customer info and negative checkout scenarios
│
├── logs/                                             # Runtime test execution logs (automation.log)
├── reports/                                          # ExtentReports HTML outputs (index.html)
├── screenshots/                                      # Failure screenshots (.png)
├── pom.xml                                           # Maven dependencies and Surefire plugin configuration
├── .gitignore                                        # Standard git exclusion rules
└── README.md                                         # Framework documentation
```

---

## 🛠️ Tech Stack & Dependencies

| Tool / Library | Version | Purpose |
|---|---|---|
| **Java** | 11+ | Core programming language |
| **Selenium WebDriver** | 4.28.1 | Browser automation engine |
| **TestNG** | 7.10.2 | Test orchestration, parameterization, and assertions |
| **ExtentReports** | 5.1.2 | Interactive HTML reporting with charts and embedded media |
| **Apache PDFBox** | 3.0.3 | PDF text extraction and validation |
| **Jackson Databind** | 2.18.2 | JSON parsing for Data-Driven testing |
| **Log4j2** | 2.24.3 | Rolling file and console logging |
| **Maven Surefire** | 3.5.2 | Test suite runner and CLI execution plugin |

---

## ⚙️ Prerequisites

1. **Java Development Kit (JDK 11 or higher)**
   ```bash
   java -version
   ```
2. **Apache Maven (3.8+)**
   ```bash
   mvn -version
   ```
3. **Google Chrome / Mozilla Firefox / Microsoft Edge**

---

## 📥 Setup and Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Bench_Assignment_CT
   ```
2. Verify Maven project dependencies:
   ```bash
   mvn clean compile
   ```

---

## 🧪 Running the Tests

### Command Line (Maven)

- **Run Default Suite:**
  ```bash
  mvn clean test
  ```

- **Run Parallel Test Suite:**
  ```bash
  mvn test -DsuiteXmlFile="src/test/resources/runners/testng-parallel.xml"
  ```

- **Run Smoke Test Suite:**
  ```bash
  mvn test -DsuiteXmlFile="src/test/resources/runners/testng-smoke.xml"
  ```

### Cross-Browser & Headless Mode

You can override browser configurations dynamically without modifying code:

- **Run in Firefox:**
  ```bash
  mvn test -Dbrowser=firefox
  ```

- **Run in Edge:**
  ```bash
  mvn test -Dbrowser=edge
  ```

- **Run in Headless Mode (CI/CD default):**
  ```bash
  mvn test -Dheadless=true
  ```

---

## 📊 Test Scenarios Matrix

| Class Name | Scenario | Type | Description |
|---|---|---|---|
| `LoginTest` | `testValidLogin` | Positive | Successful login with standard_user |
| `LoginTest` | `testLockedOutUserLogin` | Negative | Verify error for locked out user |
| `LoginTest` | `testInvalidPasswordLogin` | Negative | Verify error for wrong password |
| `LoginTest` | `testEmptyUsernameLogin` | Negative | Validation for blank username |
| `LoginTest` | `testEmptyPasswordLogin` | Negative | Validation for blank password |
| `LoginTest` | `testNegativeLoginDataDriven` | Negative | Data-driven negative login from `logindata.json` |
| `ProductCatalogTest`| `testProductInventoryDisplay` | Positive | Verify item count and catalog display |
| `ProductCatalogTest`| `testSortProductsPriceLowToHigh` | Positive | Verify price sorting (Ascending) |
| `ProductCatalogTest`| `testSortProductsPriceHighToLow` | Positive | Verify price sorting (Descending) |
| `ProductCatalogTest`| `testAddAndRemoveProductFromCatalog` | Positive | Validate cart badge increments/decrements |
| `CartTest` | `testCartItemDisplay` | Positive | Verify cart items match added products |
| `CartTest` | `testRemoveItemFromCart` | Positive | Remove item from cart and assert count |
| `CartTest` | `testContinueShoppingNavigation` | Positive | Navigate back to catalog from cart |
| `CartTest` | `testProceedToCheckoutNavigation` | Positive | Navigate to Checkout Step One |
| `CheckoutTest` | `testValidCustomerInformationSubmission`| Positive | Submit valid user details |
| `CheckoutTest` | `testEmptyFirstNameValidation` | Negative | Error: First Name is required |
| `CheckoutTest` | `testEmptyLastNameValidation` | Negative | Error: Last Name is required |
| `CheckoutTest` | `testEmptyPostalCodeValidation` | Negative | Error: Postal Code is required |
| `CheckoutTest` | `testNegativeCheckoutDataDriven` | Negative | Data-driven test from `checkoutdata.json` |
| `CheckoutTest` | `testCancelCheckoutNavigation` | Positive | Cancel checkout returns to cart |
| `EndToEndCheckoutTest`| `testCompleteCheckoutFlowSingleItem` | E2E | Full flow with price, tax, and order assertions |
| `EndToEndCheckoutTest`| `testCompleteCheckoutFlowMultipleItems` | E2E | Multi-item checkout with item summation validation |
| `PDFConfirmationTest` | `testOrderConfirmationPdfValidation` | Validation | Validates generated order receipt PDF via PDFBox |

---

## 📑 PDF Validation of Order Confirmation

In enterprise checkout systems, customers receive an order confirmation receipt or invoice in PDF format.
The framework satisfies this requirement via `com.saucedemo.utils.PDFValidationUtil`:
- Utilizes **Apache PDFBox 3.x** to load and parse PDF document streams.
- Extracts document text using `PDFTextStripper`.
- Validates:
  - Document page count.
  - Presence of Order Status ("Thank you for your order!").
  - Dispatch message text.
  - Customer name and delivery details.
  - Itemized purchase list and verified Grand Total amount.

---

## 📈 Test Reporting and Logging

### 1. ExtentReports 5
- Generated at: `reports/index.html`
- Contains interactive charts, pass/fail status, test execution duration, and author details.
- Automatically captures and embeds base64 screenshots for failed steps.

### 2. Failure Screenshots
- Saved under: `screenshots/<testName>_<timestamp>.png`

### 3. Log4j2 Execution Logs
- Saved under: `logs/automation.log`
- Contains timestamps, thread names (vital for diagnosing parallel execution), and detailed step-by-step logs.

---

## 🔄 CI/CD Workflow (GitHub Actions)

A ready-to-run GitHub Actions workflow is provided at `.github/workflows/ci-cd-tests.yml`.
- Runs automatically on every `push` and `pull_request` to `main`/`master` branches.
- Sets up JDK 11 and Google Chrome Headless.
- Executes tests in parallel or single suite mode.
- Archives `reports/`, `screenshots/`, and `logs/` as downloadable artifacts with a 14-day retention period.
