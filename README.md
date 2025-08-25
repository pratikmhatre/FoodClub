# FoodClub Android App

FoodClub is an Android application designed to help users discover restaurants, view their details,
and find attractive deals.

## Screenshots
<div style="display: flex; flex-direction: row;">  
<img src="https://github.com/pratikmhatre/LocalBites/blob/master/images/Light_1.png" alt="Dashboard Light" width="200"/>
<img src="https://github.com/pratikmhatre/LocalBites/blob/master/images/Light_2.png" alt="Details Light" width="200"/>

<img src="https://github.com/pratikmhatre/LocalBites/blob/master/images/Dark_1.png" alt="Dashboard Dark" width="200"/>
<img src="https://github.com/pratikmhatre/LocalBites/blob/master/images/Dark_2.png" alt="Details Dark" width="200"/>



## Features

* **Restaurant Discovery:** Browse a list of available restaurants.
* **Search Functionality:** Quickly find restaurants by name or other criteria.
* **Restaurant Details:** View comprehensive details for each restaurant, including:
    * Images
    * Cuisine types
    * Address and location
    * Operating hours
    * Available deals and discounts
* **Deal Listings:** See current deals offered by restaurants, including discount percentages and
  validity.
* **Modern UI:** Built with Jetpack Compose for a declarative and modern user interface.

## Technologies & Libraries Used

This project leverages a modern Android tech stack:

* **Programming Language:** [Kotlin](https://kotlinlang.org/) (Primarily)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - For building
  the entire UI declaratively.
    * `androidx.compose.ui`
    * `androidx.compose.material3` (for Material Design 3 components)
    * `androidx.compose.foundation`
    * `androidx.compose.runtime`
    * `androidx.compose.animation`
    * `androidx.navigation:navigation-compose` (for navigation between screens)
* **Architecture:**
    * Likely MVVM (Model-View-ViewModel) or a similar modern architectural pattern.
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (
      `androidx.lifecycle:lifecycle-viewmodel-compose`) - To store and manage UI-related data in a
      lifecycle-conscious way.
* **Asynchronous Programming:**
    * [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For managing
      background threads and asynchronous operations.
    * [Kotlin Flows](https://kotlinlang.org/docs/flow.html) - For handling streams of data
      asynchronously (e.g., for search debounce, observing data changes).
* **Networking:**
    * [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
    * [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) (
      `com.squareup.retrofit2:converter-gson`) - For serializing and deserializing JSON data with
      Retrofit.
* **Dependency Injection:**
    * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (
      `androidx.hilt:hilt-navigation-compose`, potentially `dagger.hilt.android.plugin`) - For
      managing dependencies throughout the application.
* **Image Loading:**
    * [Coil](https://coil-kt.github.io/coil/) or [Glide](https://github.com/bumptech/glide) (Common
      choices; you're using `AsyncImage`, which often relies on Coil by default in many Compose
      projects, or you might have configured a specific image loading library).
* **Testing:**
    * [JUnit](https://junit.org/junit5/) (Likely for unit tests).
    * [Truth](https://truth.dev/) (Potentially for assertions in tests).
    * [Robolectric](http://robolectric.org/) (Potentially for instrumented unit tests that require
      Android framework classes).
    * Compose UI Tests (`androidx.compose.ui:ui-test-junit4`)
* **Build System:** [Gradle](https://gradle.org/)

## Testing

The project includes the following types of tests to ensure code quality and correctness:

### Unit Tests

*   Located in `app/src/test/java/`.
*   **Transformation Functions (`core/utils/TransformationTest.kt`):**
    *   `test_restaurant_to_restaurant_details_model_conversion`: Verifies correct mapping from network `Restaurant` model to `RestaurantDetailsModel`.
    *   `test_deals_sorting_and_transformation`: Checks if deals are correctly sorted by discount and transformed into the `Deal` domain model.
    *   `test_restaurant_list_to_display_model_conversion`: Ensures the list of network `Restaurant` models is correctly mapped to `RestaurantListDisplayModel`.
    *   `test_get_cuisines_text_formatting`: Validates the formatting of cuisine lists into a comma-separated string.
    *   `test_get_best_deal_data_logic`: Tests the logic for extracting the best deal from a list of deals.
    *   *(Please add more specific descriptions of your other unit tests here as you create or identify them).*

### UI Tests (Instrumented Tests)

*   Located in `app/src/androidTest/java/`.
*   **Restaurant List Screen:**
    *   *(e.g., `test_restaurant_list_displays_items`: Verifies that restaurants are loaded and displayed).*
    *   *(e.g., `test_search_filters_restaurants`: Checks if the search functionality correctly filters the list).*
    *   *(e.g., `test_navigation_to_details_screen`: Ensures tapping a restaurant item navigates to the details screen).*
*   **Restaurant Details Screen:**
    *   *(e.g., `test_restaurant_details_are_displayed`: Verifies all relevant information is shown).*
    *   *(e.g., `test_deals_section_is_populated`: Checks if deals are correctly displayed).*
*   *(Please add more specific descriptions of your UI tests here as you create or identify them).*

*(Remember to update the placeholder test case descriptions above with actual details from your test files.)*

## Getting Started

1.  Clone the repository:
    bash git clone <your-repository-url>
2.  Open the project in Android Studio (latest stable version recommended).
3.  Let Gradle sync and download the necessary dependencies.
4.  Run the application on an Android emulator or a physical device.

## Future Enhancements (Ideas)

*   User authentication
*   Bookmarking favorite restaurants
*   Filtering restaurants by cuisine, location, etc.
*   Map integration to show restaurant locations.
*   User reviews and ratings.

## Contribution

Currently, this is a solo project, but contributions/suggestions are welcome. Please open an issue to discuss potential changes.
