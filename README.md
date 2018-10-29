# Tweets to Choropleth
An application that pulls tweets based on a keyword and plots a choropleth indicating the number of tweets per country.

## Preface
The idea is to have an indication about the activity in each country related to Manchester United over any given period of time, such as during a match. A simple glance of the map will tell us which countries are actively tweeting about Manchester United.

## Functional Overview
Currently, the application pulls a Twitter stream using Twitter4j which filters for the hashtag *#MUFC*. With the help of Kafka these tweets are stored in HBase.

An Oozie coordinator job runs to fetch only the locations from HBase and convert those locations into their corresponding country codes. These locations are fetched every 5 minutes.

Finally, a Python script checks for the availability of these country codes, counts number of tweets per country and plots a choropleth. The choropleth is refreshed every minute.

## Result
*< incoming choropleth >*

### Note
*The current application is stable, however I will be updating and enhancing it further. Hence, it is still WIP.*
