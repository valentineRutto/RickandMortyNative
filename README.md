
## 📹 Video Demo

> **Screen Recording — Portal Explorer walkthrough**

[▶ Watch Demo Video](file:///Users/valentinerutto/Desktop/Screen_recording_20260509_172723.webm)

> _To play locally: download the file and open it in a browser (Chrome/Firefox) or a media player that supports `.webm` format._

---

## 🏗️ Architecture
The app follows an MVVM architecture with a repository-driven data layer and Paging 3 for incremental loading and offline cache support.

```text
UI Layer
├── CharacterScreen
├── CharacterDetailScreen
└── AppNavGraph
        │
        ▼
ViewModel Layer
└── CharacterViewmodel
        │
        ▼
Repository Layer
└── CharacterRepository
        │
        ├── Local Data Source
        │   ├── Room Database
        │   ├── CharacterDao
        │   └── CharacterRemoteKeyDao
        │
        └── Remote Data Source
            ├── Retrofit ApiService
            └── Rick and Morty API
```

---

# Architecture Overview

## UI Layer

The UI is built entirely with **Jetpack Compose** using a reactive, state-driven approach.

### `CharacterScreen`

Responsible for:

* Displaying the paginated list of characters
* Search functionality
* Status filtering
* Loading and error states
* Retry actions for failed pagination requests

### `CharacterDetailScreen`

Responsible for:

* Displaying detailed character information
* Showing the first three episode appearances
* Rendering additional metadata such as species, gender, and status

### `AppNavGraph`

Handles navigation between:

* Character list screen
* Character detail screen

---

# ViewModel Layer

## `CharacterViewModel`

Owns and manages all UI-related state.

### Responsibilities

* Exposes paginated character data
* Maintains search and filter state
* Stores selected character details
* Fetches and exposes the first three episode details for a selected character
* Coordinates communication between UI and repository layers

---

# Repository Layer

## `CharacterRepository`

Acts as the **single source of truth** for application data.

### Responsibilities

* Retrieves cached character data from Room
* Coordinates paginated network loading using `RemoteMediator`
* Fetches episode details from the Rick and Morty API
* Exposes reactive streams of paged data to the ViewModel

---

# Local Data Layer

The app uses **Room Database** for offline caching and Paging 3 integration.

## `CharacterEntity`

Stores:

* Character metadata
* Cached episode URLs
* Image URLs
* Character details required for offline viewing

## `CharacterRemoteKey`

Stores pagination keys used by Paging 3.

## `CharacterDao`

Provides:

* Paged database queries
* Character lookup operations
* Search and filter queries

## `CharacterRemoteKeyDao`

Responsible for:

* Managing Paging 3 remote keys
* Inserting and clearing pagination state

---

# Remote Data Layer

The app uses **Retrofit** to communicate with the Rick and Morty API.

## API Endpoints

### `getCharacters()`

Loads paginated character data.

Example:

```text
https://rickandmortyapi.com/api/character?page=1
```

### `getEpisode()`

Loads a single episode.

Example:

```text
https://rickandmortyapi.com/api/episode/1
```

### `getEpisodes()`

Loads multiple episodes using the batch endpoint.

Example:

```text
https://rickandmortyapi.com/api/episode/1,2,3
```

---

# Paging Flow

```text
CharacterScreen
    ↓
CharacterViewModel
    ↓
Pager + CharacterRemoteMediator
    ↓
Rick and Morty API
    ↓
Room Database
    ↓
PagingData<CharacterEntity>
    ↓
CharacterScreen
```

The application reads from **Room first** and uses `CharacterRemoteMediator` to fetch additional pages when required.

This architecture provides:

* Offline-first behavior
* Smooth infinite scrolling
* Cached pagination support
* Reduced unnecessary network requests
* Improved resilience during network failures

---

# Dependency Injection

The app uses **Koin** for dependency injection.

Koin provides shared app dependencies such as:

* `RickandMortyDatabase`
* `CharacterDao`
* `CharacterRemoteKeyDao`
* `Retrofit`
* `ApiService`
* `CharacterRepository`
* `CharacterViewmodel`

## Koin Modules

```text
Koin Modules
├── appModule
│   ├── CharacterRepository
│   └── CharacterViewmodel
│
├── networkingModule
│   ├── OkHttpClient
│   ├── Retrofit
│   └── ApiService
│
└── databaseModule
    ├── RickandMortyDatabase
    ├── CharacterDao
    └── CharacterRemoteKeyDao
```

---

# DI Flow

```text
UI Layer
└── CharacterScreen / CharacterDetailScreen
        │
        ▼
Koin injects
        │
        ▼
CharacterViewmodel
        │
        ▼
CharacterRepository
        │
        ├── ApiService
        └── CharacterDao / RickandMortyDatabase
```

`CharacterViewmodel` is retrieved in Compose using Koin and reused across the navigation graph, allowing both the character list and detail screens to share the same source of UI state.
