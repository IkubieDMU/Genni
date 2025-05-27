## State Management Documentation 🚦

This documentation outlines the various state definitions within the Genni application, which are crucial for managing UI updates and logical flows for authentication, password resets, and workout sessions. These states provide clear indicators of the current operation's status, enabling robust error handling and user feedback.

### Authentication States (`AuthState.kt`) 🔒

The `AuthState` sealed class defines the possible states during a user's authentication process. This allows the application to react appropriately to different stages of authentication, from initial setup to successful login or encountering an error.

* **`Idle`**: This is the initial state, indicating that no authentication action has been taken yet.
* **`Loading`**: This state signifies that an authentication operation is currently in progress, such as logging in or signing up.
* **`Success(message: String)`**: This state indicates that the authentication process has completed successfully. It carries a `message` to provide feedback to the user (e.g., "Login successful").
* **`Error(message: String)`**: This state is activated when an error occurs during authentication. It includes a `message` detailing the error encountered, which can be displayed to the user for troubleshooting (e.g., "Invalid credentials").

### Password Reset States (`ResetState.kt`) 🔑

The `ResetState` sealed class mirrors the structure of `AuthState` but specifically manages the different phases of a password reset operation. This ensures users receive clear updates throughout the process of recovering or changing their password.

* **`Idle`**: The initial state, meaning no password reset action has been initiated.
* **`Loading`**: Indicates that the password reset process is currently underway (e.g., sending a reset email, updating the password).
* **`Success(message: String)`**: Signifies that the password reset has been successful, accompanied by a `message` for user confirmation.
* **`Error(message: String)`**: Represents an error during the password reset, with a `message` explaining the failure.

### Workout Session States (`WorkoutState.kt`) 🏋️‍♀️

The `WorkoutState` enum class defines the distinct phases a user goes through during a workout session in the simulator. This helps the application control the UI and logic pertinent to each part of an exercise routine.

* **`Exercise`**: This state indicates that the user is currently performing an exercise within the workout.
* **`Rest`**: This state signifies that the user is in a rest period between exercises.
* **`Completed`**: This state indicates that the entire workout session has been finished.
