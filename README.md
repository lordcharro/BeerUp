BeerUp
======

An Android app that lists a lot of beers.

## Info

A Simple, clean and robust application. With a beautiful design. Lost network connection? No problem, it has offline content.
Tested: bonus points for all unit or integration tests that make sense

## What is the app doing ?

The app is simple: a very long list of beers that one can scroll. Each item of the list contains a picture and also, the name, the brewery, the style and the created date of the beer.
The app for the content is using the REST API of the BreweryDB website.

## Technology Used

+ **Widgets:** RecyclerView, CardView
+ **Networking:** Retrofit, Picasso
+ **JSON:** GSON
+ **Persistence:** realm.io
+ **Testing:** Espresso

## Behavior

+ List the first 50 beers, when reaches the end of the list, it loads more beers, until there are no more pages.
+ If the beer does not have an image, it will be filled by a standard image.


## Limitations

+ As the REST API of BreweryDB only allows to obtain a list of all beers to the premium users, it was used the endpoint search.
+ Brewery is not present, since the list of beers doesn't return the brewery. 
+ It will be necessary make the same number of calls as the same number of items to catch the brewery for each beer.



