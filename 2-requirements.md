**Mobile Development 2024/25 Portfolio**
# Requirements

Student ID: `23031398`

### Functional Requirements

- **Home Page**
    - A bottom navigation bar must allow users to switch between key screens.
    - A menu icon must provide access to settings and education pages.
    - The overview card displays current weather, days logged and a progress bar for the daily goals and updates dynamically
    - Users can add, complete, and delete goals.
    - Completing goals updates the progress bar and grants experience points.
- **Symptom & Flare-up/Mood Tracking**
    - Users can log their daily symptoms and mood with a simple UI with categories and icons to select e.g under Physical Sensations can select Itchy icon
    - Users can log symptoms (e.g., redness, itching, swelling) with photos  attached.
    - Users can categorise flare-ups by severity (mild, moderate, severe).
    - Option to attach images for better symptom tracking.
    - Users can select and log their mood daily.
    - Users can flip between dates with a data picker in the header
- **Food & Skincare Diary**
    - Users can log meals, skincare products, and other potential irritants (e.g., fabrics).
    - Users can select a date using a date picker.
    - Users can filter entries by food, product, or reaction severity.
    - The diary screen displays daily entries as cards containing:
        - Food/Product name
        - Reaction & severity (color-coded)
        - Edit button
    - Tapping an entry expands it to display detailed logs, uploaded photos, and timestamps.
    - Users can log new entries from this screen.
    - The app provides an overview of potential irritants based on logged reactions.
- **Medication & Treatment Reminders**
    - Users can set recurring reminders for medication and skincare routines.
    - Reminders for daily tasks appear in the home page as tickable tasks.
    - Notifications appear in-app and via push alerts.
    - Completing reminders updates progress streaks.
- **Smart Environmental Alerts**
    - The app fetches real-time weather and pollen data.
    - Users receive alerts for environmental conditions that may trigger flare-ups
- **SOS First-Aid Kit**
    - Users can access emergency care steps based on symptom severity.
    - Users can access a first-aid action plan based on symptom severity.
    - The action plan includes recommended treatments for different situations (e.g., severe itching, skin infections, swelling).
    - Users can create custom emergency action plans for personalized care.
    - The section includes links to medical resources for professional guidance.
- **Educational Resources**
    - The app signposts to resources on eczema.
- **Doctor’s PDF Report**
    - Users can generate a detailed PDF summary of symptoms, treatments, and logs.
    - The report can be exported or shared with doctors.
- **Progress Streaks & Analytical Insights**
    - The app tracks user activity and encourages consistency.
    - Charts and statistics help users identify eczema patterns over time.

### **Non-Functional Requirements**

### **1**. Performance & Efficiency

- The app must load the home page within 2 seconds under normal conditions.
- Navigation between screens should be smooth and responsive, with transitions taking no longer than 1 second.
- Environmental alerts and weather updates must fetch real-time data within 10 seconds.

### 2. Usability & Accessibility

- The app must have an intuitive UI, making it easy for users of all ages to log symptoms, track progress, and navigate features.
- The interface must support large text modes and high contrast settings for accessibility.
- Icons and colour codes should be clear, especially for severity levels in symptom tracking.

### 3. Compatibility & Scalability

- The UI should be responsive and adapt to various screen sizes
- The database should be designed to handle large logs efficiently without performance degradation.

### **4. Security & Data Privacy**

- User data must be encrypted both in transit and at rest to ensure privacy (especially photos)
- The app must comply with GDPR and other relevant data protection regulations.
- Users should be able to export and delete their data upon request.
- Authentication must be secure, with options like biometric login (fingerprint/face recognition) if supported by the device.

### **5. Reliability & Availability**

- The app should work offline, allowing users to log symptoms and entries without an internet connection. Data should sync once the device is online.
- Push notifications must be delivered reliably, even when the app is in the background.
- The app should gracefully handle API failures (e.g., if weather data is unavailable, it should display the last fetched value).

### **6. Maintainability & Extensibility**

- The app must be modular, allowing for future updates and feature expansions (e.g., additional tracking options, AI-based insights).
- Logs should be stored efficiently, ensuring smooth performance even with high data volume over time.
- The app should be well-documented for ease of maintenance and troubleshooting.