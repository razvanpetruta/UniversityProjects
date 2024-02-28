
# The Daily Report Mobile Application

## Description

The Daily Report Mobile Application is designed to help users reflect on their daily experiences, practice gratitude, and enhance self-awareness. This app allows users to create, update, and review their daily reports, which consist of several key components, including gratitude, accomplishments, shortcomings, areas for improvement, and an overall rating and feeling for the day. The application aims to promote positive self-reflection and personal growth.


## Entity Details

#### Daily Report:
- **Title**:
    - type: string
    - description: The user can give a title to each daily report to briefly summarize the day.
- **Gratitude**:
    - type: string
    - description: Users can express what they are grateful for on that particular day. This could be a place to capture positive moments and feelings.
- **Accomplishments**:
    - type: string
    - description: This section allows users to document their achievements and positive actions throughout the day. It can be personal or professional accomplishments.
- **Shortcomings**:
    - type: string
    - description: Users can note the areas where they feel they fell short or made mistakes. This is an opportunity for self-reflection and growth.
- **ImprovementAreas**:
    - type: string
    - description: Users can identify specific areas in their lives where they would like to see improvement and growth. It can be related to personal goals, habits, or skills.
- **Rating**:
    - type: numeric (1 to 5)
    - description: Users can rate their overall satisfaction or feeling for the day on a scale from 1 to 5, with 5 being the most positive and 1 being the least positive.
- **Date**:
    - type: date
    - description: The date associated with the daily report, indicating when it was created.


## CRUD

- READ:
    - When the user opens the application, he will be presented with the list of all the reports he wrote so far. When the user clicks on any report he will be redirect to a page where all the details are visible (like the update page).
- CREATE:
    - While in the list view, the user can create a new report by pressing the '+' button from the bottom of the page. He will be redirected to a new page where he can fill up all the necessary information and then press the 'Save' button from the top right corner.
- UPDATE: 
    - While in the list view, the user can click the update button specific to an element of the list and he will be redirected to a form page pre-filled with the report information such that he will be able to update any specific field and then press the 'Save' button.
- DELETE:
    - While in the list view, the user can click the delete button specific to an element of the list and he will be prompted with a confirmation pop-up. Then he can click the 'Yes' button if he's sure that he wants to delete the entity, or he will be able to cancel the action.


## Persistance Details

- READ:
    - The application fetches previously created daily reports from the local database. Users can view their reports even in offline mode. This local data ensures a seamless user experience when internet access is limited. When internet is available the local database will be synchronized with the server.
- CREATE:
    - When a user creates a new daily report, the information is stored locally on the device. This allows users to add and save reports even when they are offline. The data is synchronized with the server when an internet connection becomes available.
- UPDATE: 
    - When a user updates an existing daily report, the changes are first saved locally. These updates are immediately visible to the user, enhancing the responsiveness of the application. Later, when the user's device is online, the changes are synchronized with the server.
- DELETE:
    - Deletion of a daily report is first carried out locally, providing immediate feedback to the user. The deleted report is marked for removal from the server as soon as an internet connection is detected.


## Offline scenario

- READ:
    - Only the elements stored in the local database will be displayed and a toastr telling the user that 'No internet connection is available at the moment' will be showed.
- CREATE:
    - The entity will be saved in the local database and, when the internet is available the server, will also be synchronized. A toastr telling the user that 'No internet connection is available at the moment, the operation will be persisted when possible' will be showed.
- UPDATE: 
    - The entity will be updated in the local database and a toastr telling the user that 'No internet connection is available at the moment, the operation will be persisted when possible' will be showed. 
- DELETE:
    - The entity will be deleted from the local database and a toastr telling the user that 'No internet connection is available at the moment, the operation will be persisted when possible' will be showed. 