<div align="center">
  # 🛡️ Raksha

  *A comprehensive Android application for women's safety with robust automation testing.*

  [![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
  [![Espresso](https://img.shields.io/badge/Espresso-Testing-FF0000?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/training/testing/espresso)
</div>

---

Raksha is an Android-based mobile application developed to provide immediate assistance and emergency support to users in critical situations. In today's fast-paced world, ensuring personal safety has become a major concern, especially for women. Raksha addresses this issue by integrating essential safety features and validating them thoroughly using an automated testing framework.

## ✨ Features

- 🚨 **One-Tap SOS:** Immediately sends GPS coordinates to emergency contacts.
- 📍 **Real-time Location:** Live location tracking and sharing during emergencies.
- 🏥 **Nearby Assistance Search:** Quickly find nearby police stations, hospitals, and medical stores.
- 🤖 **Automated UI Testing:** Extensive functional validation using Android Espresso and UIAutomator.
- 📊 **Centralized Test Reporting:** Dynamic HTML-based master reports tracking pass/fail statistics for regression testing.

## 📸 Screenshots

Here is a look at the application and the testing reports in action:

| Dashboard View | Emergency Search |
| :---: | :---: |
| <img src="assets/images/placeholder.png" width="400" alt="Dashboard View"> | <img src="assets/images/placeholder.png" width="400" alt="Emergency Search"> |

| Automated Test Report | Tracking Location |
| :---: | :---: |
| <img src="assets/images/placeholder.png" width="400" alt="Automated Test Report"> | <img src="assets/images/placeholder.png" width="400" alt="Tracking Location"> |

*(Note: Replace the placeholder images with actual screenshots in the `assets/images` directory).*

## 🛠️ Architecture

- **Platform:** Native Android (Java)
- **APIs:** Google Maps SDK, Android Location Services, Twilio (for SMS alerts)
- **Testing Framework:** Android Espresso (for UI components), UIAutomator (for system-level interactions like permissions)
- **Software Testing Life Cycle (STLC):** Full integration of test planning, requirement analysis, and defect reporting.

## ⚙️ Local Setup Instructions

1. Clone this repository: `git clone https://github.com/ritesh-0608/Raksha-Women-Safety-App.git`
2. Open the project in **Android Studio**.
3. Add your required API keys (e.g., Google Maps) to the `local.properties` file:
   ```properties
   MAPS_API_KEY=your_api_key_here
   ```
4. Build and sync the project using Gradle.
5. Run the application on an emulator or physical device.
6. To run the automated tests, execute the **Espresso test suite** from the `androidTest` directory.

---
<div align="center">
  <i>Engineered with ❤️ by Ritesh.</i>
</div>
