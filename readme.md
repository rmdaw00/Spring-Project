## Ticket Booking App

This Spring application runs MVC website on **localhost:8080**, it allows manages events, tickets and users, you can CRUD all of these entities, book tickets, do specific search on events or users. allows to display tickets on PDF, XML, and import XML ticket batch.

It also exposes REST APIs, folder with **Postman Collections** show all requests that can be made.

App is highy abstracted and uses facade to reach internal methods

The app allows to save data:
1. **Locally:** on a custom hashmap
2. **DB:** app is setup to work with postgres DB, you might need to edit application.propeties to make it work with your local instance. i have included a script to populate tables on db (**db setup** folder)

to toggle between the two modes:
change the boolean of **app.localData** set in application.properties

This document is just a abstract guide, not meant to point out features. please check code for proper feel.

Some components were done in basic form, More to be added in:
- Making MVC website work with postgres DB, *for now it was intended to work flawlessly with local hashmap "app.localData=true"*
- Spring Data 
- Spring Rest API
- Unit Tests with Spring 
