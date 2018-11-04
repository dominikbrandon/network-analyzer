# Network Analyzer

[![Build Status](https://travis-ci.org/dominikbrandon/network-analyzer.svg?branch=develop)](https://travis-ci.org/dominikbrandon/network-analyzer)

## Budowanie
`mvn clean install`

## Odpalanie
`mvn spring-boot:run`

Aplikacja dostępna pod adresem `http://localhost:8080`

## Baza H2
Dostęp do bazy H2 pod adresem `http://localhost:8080/h2console` (URL: `jdbc:h2:mem:testdb`, user: `sa`)

## Dokumentacja API
`http://localhost:8080/swagger-ui.html`

## Generowanie javadoc
`mvn javadoc:javadoc`

Lokalizacja: `target/site/apidocs/index.html`

### Autorzy
* Kacper Maciejewski
* Paweł Myszkowski
* Mateusz Wiśniewski
* Dominik Grzelak