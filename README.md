# ABN AMRO repositories

This app loads all the repositories of the ABN AMRO Github's username (`abnamrocoesd`).
I have opted for a similar architecture than the one suggested in the [Guide to app architecture](https://developer.android.com/topic/architecture) with three layers:
- Data layer
- Domain layer
- Presentation or UI layer

### Data layer
This layer contains all related to fetching repositories from either a remote data source (Github) or the local data source (local database).
The repository is in charge of doing this, returning a [Pager](https://developer.android.com/reference/androidx/paging/Pager) object, which contains the paginated data.
This pager object will be responsible of choosing the data source, via the [GithubRemoteMediator](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/data/repository/GithubRemoteMediator.kt).
For the database, the [Room persistence library](https://developer.android.com/jetpack/androidx/releases/room) has been the way to go.

### Domain layer
This layer contains the business logic, and more concretely, the use cases that will invoke the repository.

### Presentation layer
This contains everything related to the UI and the user interactions. In this case, a [MainActivity](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/ui/MainActivity.kt) and two fragments: [RepositoryListFragment](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/ui/RepositoryListFragment.kt) and [DetailFragment](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/ui/DetailFragment.kt).
The UI will be listening to the data changes and reacting accordingly via the [RemoteListViewModel](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/ui/RepositoryListViewModel.kt) which will load and observe the data loading process and will also update the [RepositoryListAdapter](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/ui/RepositoryListAdapter.kt)
For the data pagination, this app uses the [Jetpack paging library v3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) in order to paginate the list of repositories.

### Testing
The app contains a mix of unit and instrumented testing.
The instrumented part exists because the app makes heavy use of the Room persistence library, and thus in order to test the DAOs for example, a context needs to be provided.
The same is true for the [GithubRemoteMediator](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/data/repository/GithubRemoteMediator.kt): as this coordinates the data loading process and distinguishes between the local and the remote data source, it also needs a database object, so it was indeed necessary to create instrumented tests.
I have also created some UI tests using Espresso, just to test the proper UI in the adapter rows. This is a very basic test, but I have decided not to focus on the UI testing and I have included it also to show the way to use Espresso+Hilt, which is not trivial.
Although it has been tested, I could not test the [RepositoriesRepository](https://github.com/noloman/abnrepos/blob/main/app/src/main/java/com/nulltwenty/abnrepos/data/repository/RepositoriesRepositoryImpl.kt) because the [PagingData](https://developer.android.com/reference/kotlin/androidx/paging/PagingData) is object that wraps the data that will be showed to the users (the repository list) and it doesn't expose its contents so easily (or at least I couldn't find a way to do it).