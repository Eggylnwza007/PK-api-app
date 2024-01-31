# Add new dependencies

add new dependencies to `build.gradle.kts` in app
```kotlin
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
```

# Add User permission

add new user permission to `AndroidManifest.xaml`

```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```