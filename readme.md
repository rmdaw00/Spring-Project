## Ticket Booking App

This Spring application runs MVC website locally, it manages events, tickets and users, you can CRUD all of these entities, book tickets, do specific search on events or users. it also allows to display tickets on PDF, XML, and import XML ticket batch.

It also exposes REST APIs, folder with **Postman Collections** show all requests that can be made.

App is highy abstracted and uses facade to reach internal methods

Please note this was part of training, and was not meant to a fully working and fully tested product rather a demonstration of skill thats why some functionality would be skipped as detailed below


The app allows to save data:
1. **Locally:** on a custom hashmap (complete integeration)
2. **DB:** app is setup to work with postgres DB, you might need to edit application.propeties to make it work with your local instance. i have included a script to populate tables on db (**db setup** folder)

to toggle between the two modes:
change the boolean of **app.localData** set in application.properties

This document is just a abstract guide, not meant to point out features. please check code for proper feel.

Project Includes:
- Making MVC website work with local map, postgres DB, *for now it was intended to work flawlessly with local hashmap "app.localData=true"*
- Spring Data 
- Spring Rest API
- Unit Tests with Spring 
- Cucumber Testing with MockMvc for API Controllers and Selenium for MVC web access (%100 local storage, DB storage some failing due to some unimplemented features, check commit for further details)