# Expiry Date Tracker Challenge (3th way task)

## Challenge Details:
### The application should contain:
#### A. 3 screens (Home, Barcode Scanner, and Expired Items screen).
1) The Home screen provide a way to scan item’s barcode and a recycler view contains a card view for each scanned item which lists item details as follows:
Item name.
Item type/category like (Food, Drink, …etc).
Item expiry date.
2) The Barcode scanner screen which will be opened to scan the item’s barcode.
3) The Expired items screen which displays expired items in recycler view.
### II. The application should do the following:
#### A. Upon clicking on the scan barcode button, the app should scan the barcode of the selected item, then this item will be added to the recycler view in the main screen (the recycler view length should be at least 4 items).
#### B. The items in the main screen should be sorted by expiry date (the time left before the item is considered expired).
#### C. The app should show a descriptive notification when the item has been expired and the expired item should be deleted from the main screen and added to the expired items screen.
#### D. If the real expiry date period is more than 1 day, the app should provide a way to allow the user to mock setting the expiry date of the selected item to be as follows [round robin]:
Expiry date of the first item is 6 hours.
Expiry date of the second item is 12 hours.
Expiry date of the third item is 18 hours.
Expiry date of the fourth item is 24 hours.
#### E. The app should be able to keep displaying the scanned items even if the app is closed or the phone is rebooted.
#### F. The app should keep tracking of the expiry date of each item even if the app is closed or the phone is rebooted.
#### G. Ask for the proper permissions when needed

## Tools :       
* Kotlin 
* MVVM (Model,View,ViewModel)
* viewModel , LiveData
* View Binding
* Kotlin Flow 
* Coroutines
* Lifecycle
* LiveData
* Room DB
* Hilt
* List Adapter
* Work manager
* Broad Cast receiver
* local notification
* Basics unit test

## Done features:
* Display the weather data(temp, humidity, wind,...) for the user's current location.Display the weather data(temp, humidity, wind,...) for the user's current location.
* List of weather data for X number of days (Scrollable).
* Search weather data for another city.
* Add a city to your favorite list.
* View a list of favorite cities.
* Clicking on a favorite city should open weather details view.
* Settings view to change from Celsius to Fahrenheit and vice versa.
* Caching weather info in local db and syncing it for user experience.

## Improvments
* Add more cases for unit test (time was not enough)
* Add UI test (still learning it)

https://drive.google.com/file/d/121Rv8SokKrGpHayy8wNROcSq7RT7vteI/view?usp=sharing

https://drive.google.com/file/d/1VhnWG8hGCjUKRO2cqAmZ2UBnADzc34Jq/view?usp=sharing

<img src="/Images/1.png" width=250 height=500  t> <img src="/Images/2.png" width=250 height=500 > <img src="/Images/3.png" width=250 height=500  > <img src="/Images/4.png" width=250 height=500 > <img src="/Images/5.png" width=250 height=500 > <img src="/Images/6.png" width=250 height=500 >

