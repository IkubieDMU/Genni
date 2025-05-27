## Theme Documentation 🎨

This documentation details the theming aspects of the Genni application, including its color palette and typography, which are essential for maintaining a consistent and visually appealing user interface across the application.

### Theme Definition (`Theme.kt`) 🌙💡

The `Theme.kt` file defines the overall `GenniTheme` for the application, leveraging Jetpack Compose's Material Design 3 capabilities. It supports both light and dark themes and can dynamically adjust colors based on the device's system settings on Android S and above.

* **`DarkColorScheme`**: This color scheme is used for the dark theme. It sets `primary` to `Purple80`, `secondary` to `PurpleGrey80`, and `tertiary` to `Pink80`.
* **`LightColorScheme`**: This color scheme is used for the light theme. It sets `primary` to `Purple40`, `secondary` to `PurpleGrey40`, and `tertiary` to `Pink40`.
* **Dynamic Color**: The theme supports dynamic coloring for Android S (API level 31) and higher, adapting the color scheme based on the user's wallpaper.
* **System Dark Theme**: The `GenniTheme` composable checks if the system is currently in dark theme mode (`isSystemInDarkTheme()`) to apply the appropriate color scheme.

### Color Palette (`Color.kt`) 🌈

The `Color.kt` file defines the custom color palette used throughout the Genni application. This includes both standard Material Design colors and a specific "Genni Color Scheme" to establish a unique brand identity.

* **Material Design Base Colors**:
    * `Purple80`: `0xFFD0BCFF`
    * `PurpleGrey80`: `0xFFCCC2DC`
    * `Pink80`: `0xFFEFB8C8`
    * `Purple40`: `0xFF6650a4`
    * `PurpleGrey40`: `0xFF625b71`
    * `Pink40`: `0xFF7D5260`
* **Genni Specific Colors**:
    * `emeraldGreen`: `0xFF2ECC71`
    * `royalPurple`: `0xFF7D3C98`
    * `darkGreen`: `0xFF1E8449`
    * `deepPurple`: `0xFF512E5F`
    * `softLavender`: `0xFFD7BDE2`
    * `mintGreen`: `0xFFA9DFBF`
    * `darkGray`: `0xFF2C3E50`
    * `lightGray`: `0xFFECF0F1`
    * `white`: `0xFFFFFFFF`

### Typography (`Type.kt`) ✒️

The `Type.kt` file sets the typography for the Genni application, defining text styles based on Material Design's `Typography` system.

* **`bodyLarge`**: This style is defined with `FontFamily.Default`, `FontWeight.Normal`, `fontSize` of `16.sp`, `lineHeight` of `24.sp`, and `letterSpacing` of `0.5.sp`.
* Other default text styles (e.g., `titleLarge`, `labelSmall`) are commented out, indicating that they could be overridden as needed for more granular control over the application's text appearance.
