# Premier League Fantasy App
This is a Spring Boot application for fantasy premier league data. The project stores player statistics in a PostgreSQL database and exposes a REST API for retrieving, adding, updating, and deleting player records. It uses data from the 2023-2024 season.

## Features

- A Spring Boot backend built with Java
- PostgreSQL database integration for player data
- A REST API for player operations
- Support for filtering players by:
  - team
  - name
  - position
  - nation
  - team + position combination
- Player data model based on Premier League statistics:
  - name
  - nation
  - position
  - age
  - matches played
  - starts
  - minutes played
  - goals
  - assists
  - penalties scored
  - cards
  - expected goals 
  - expected assists
  - team name
- Simple Frontend to test the main feautures

## Data source

The application is designed to work with player statistics imported from the CSV file included in the repository. These records are mapped into a postgresql database and served through the API.


