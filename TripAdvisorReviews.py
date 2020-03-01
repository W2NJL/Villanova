# -*- coding: utf-8 -*-
"""
Created on Tue Feb 19 16:26:32 2019

@author: Nick Langan
CSC 5930

This program performs the following:
    
    1. Takes the XLS file containing 4669 different TripAdvisor reviews for hotels across the United States and imports the data using the 
    pandas read_excel function in as a data frame.
    2. Using the unique method, provides a total of the unique hotels reviewed in this dataset.
    3. Using the mean method, provides an average rating (between 1 and 5) of all of the hotels reviewed in this dataset.
    4. Eliminating all hotels with only one review, for each of the four major U.S. timezones (Eastern, Central, Mountain, Pacific),
    data frames are created for each respective timezone, sorted by average rating for each unique hotel.  The program prints the top rated
    for hotels for each timezone, which equates to all hotels who had a rating of 5.0.  I manually inserted the names for each of these
    top rated hotels by locating them on TripAdvisor's website.
    5. Filtering the data frame to just the hotels located in Cleveland, Ohio, the program prints the highest rated hotel in that city.
    6. Tabulating a count for the number of review users from each state, the program provides the states with the highest amount of user 
    reviews.
    7. Utilizing a dictionary that stores the value of the average hotel review from each of the four U.S. timezones, the program prints
    the maximum value of the dict, or the timezone that had the highest average hotel review rating.
    8. QUESTION OF MY OWN:  Using the 'Trip Type' column, the program prints bar plots for each of the four timezones, showing what the total
    amounts were for each Trip Type within that timezone (Family, Couples, Business, Solo travel, Friends).
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

#Import the TripAdvisor XLS into Python.  File path is hard-coded to my laptop
hotels = pd.read_excel('C:/Users/nick.langan.NLANGAN-LT/OneDrive - Villanova University/Spring 19/CSC5930/Homework 5/Data_TripAdvisor_v1.xls')

hotels.columns = hotels.columns.get_level_values(0) #Resolves issue dataset appeared to have with multi-column indexes

#To later filter out hotels by the amount of times they appear in the dataset, a count column is added to the dataframe for each hotel
hotels['count'] = hotels.groupby(['ID_HOTEL'])['Rating'].transform('count')

#Series used to provide an appropriate index name for each of the TripType numeric values in the dataset
tripTypes = ("Family", "Couples", "Business", "Solo Travel", "Friends")

#This function is used to match the index name to each Trip Type value.  Inspiration was found at:
#https://stackoverflow.com/questions/27041724/using-conditional-to-generate-new-column-in-pandas-dataframe
def TypeName(c):
  if c['Trip Type'] == 1:
    return 'Family'
  elif c['Trip Type'] == 2:
    return 'Couples'
  elif c['Trip Type'] == 3:
    return 'Business'
  elif c['Trip Type'] == 4:
    return 'Solo Travel'
  elif c['Trip Type'] == 5:
    return 'Friends'
  else:
    return 'Undefined'

#Adds a TypeName column to the hotels data frame, matching with the TripType numeric value
hotels['TypeName'] = hotels.apply(TypeName, axis=1)

#HOMEWORK QUESTION NUMBER 1: How many hotels are rated?  
print("The number of hotels rated in this dataset are:")
print()
print(len(hotels['ID_HOTEL'].unique().tolist()))
print()

#The userStates series is created to hold the amount of review users from each state
userStates = hotels['USER_STATE'].value_counts()

#The hotelRatings data frame is created to group by each hotel with an average rating amongst the reviews for each hotel tabulated
hotelRatings = hotels.groupby(['ID_HOTEL', 'HOTEL_TIMEZONE'], as_index=False)['Rating'].mean().round(2)

#The average rating for all of the unique hotels reviewed in this dataset is calculated
hotelMean = hotelRatings['Rating'].mean()

#The average ratings are divided into each of the four timezones
hotelEastMean = hotelRatings[hotelRatings['HOTEL_TIMEZONE'] == 'Eastern']['Rating'].mean()
hotelCentralMean = hotelRatings[hotelRatings['HOTEL_TIMEZONE'] == 'Central']['Rating'].mean()
hotelMountainMean = hotelRatings[hotelRatings['HOTEL_TIMEZONE'] == 'Mountain']['Rating'].mean()
hotelPacificMean = hotelRatings[hotelRatings['HOTEL_TIMEZONE'] == 'Pacific']['Rating'].mean()

#The number for each trip type is tabulated for each of the four timezones
easternTripType = hotels[hotels['HOTEL_TIMEZONE'] == 'Eastern']['TypeName'].value_counts()
centralTripType = hotels[hotels['HOTEL_TIMEZONE'] == 'Central']['TypeName'].value_counts()
mountainTripType = hotels[hotels['HOTEL_TIMEZONE'] == 'Mountain']['TypeName'].value_counts()
pacificTripType = hotels[hotels['HOTEL_TIMEZONE'] == 'Pacific']['TypeName'].value_counts()

#HOMEWORK QUESTION NUMBER 2: What is the average rating for each hotel?
print("The average rating amongst the 1890 hotels in the dataset is:")
print()

print(round(hotelMean, ndigits=2))

#In my judgement, to properly judge the highest rated hotel, including hotels with only one review would not be wise, with the amount
#of "one-offs" that can be found on review sites like TripAdvisor and Yelp.  So, I eliminate any hotel with a count of less than 2 here.
#Note that this does not affect the total hotels caclulated in questions #1 or questions #2 for accuracy purposes.  
hotels = hotels[hotels['count'] >= 2]

#Calculate the average ratings for all hotels in the Eastern Timezone
hotelRatingsEast = hotels[hotels['HOTEL_TIMEZONE'] == 'Eastern'].groupby('ID_HOTEL', as_index=False)['Rating'].mean().round(2)

hotelRatingsEast = hotelRatingsEast.sort_values(by='Rating', ascending=False)

#Filter the data to include only those hotels with a 5.0 rating
hotelRatingsEast = hotelRatingsEast[hotelRatingsEast['Rating'] > 5.00]

#Here, I included the names of all of the top rated hotels in a list
eastNames = ["Hampton Inn Indianapolis Northwest", "Hampton Inn Jacksonville FL", "Fairfield Inn White Marsh MD", 
"Hampton Inn & Suites Indianapolis Airport", "Hilton Garden Inn Indianapolis Airport", "Crowne Plaza Jacksonville FL Airport",
"Drury Inn & Suites Atlanta Airport", "Hampton Inn Atlanta-Perimeter Center", "Wingate by Wyndham Louisville East", 
"Four Seasons Hotel Boston", "Drury Inn & Suites Louisville East", "Embassy Suites by Hilton Raleigh-Durham", 
"Courtyard Indianapolis Downtown", "JW Marriott Indianapolis", "Homewood Suites by Hilton Atlanta", "Unknown", "Residence Inn Charlotte Piper Glen"]

hotelRatingsEast["Name"] = eastNames

#HOMEWORK QUESTION NUMBER 3: What is the highest rated hotel in each timezone (Eastern):
print()
print("The highest rated hotels in the Eastern Time Zone (tied for a 5.0 rating), all with at least 2 reviews are:")
print()

print(hotelRatingsEast.to_string(index=False))

#del hotelRatingsEast

#Calculate the average ratings for all hotels in the Central Timezone
hotelRatingsCentral = hotels[hotels['HOTEL_TIMEZONE'] == 'Central'].groupby('ID_HOTEL', as_index=False)['Rating'].mean().round(2)

hotelRatingsCentral = hotelRatingsCentral.sort_values(by='Rating', ascending=False)

#Filter the data to include only those hotels with a 5.0 rating
hotelRatingsCentral = hotelRatingsCentral[hotelRatingsCentral['Rating'] == 5.00]

#Here, I included the names of all of the top rated hotels in a list
centralNames = ["Hampton Inn Oklahoma City Northwest", "Hampton Inn & Suites Austin Airport", "Holiday Inn Express & Suites Dallas", 
"Sleep Inn & Suites Tulsa", "Homewood Suites by Hilton Oklahoma City West", "Courtyard Dallas Arlington South",
"Hilton Garden Inn Milwaukee Airport", "TownePlace Suites Houston Intercontinental", "Staybridge Suites Memphis", 
"Homewood Suites by Hilton Houston", "Best Western Plus Westchase Houston", "Hotel Crescent Court Dallas", 
"Colcord Hotel Oklahoma City", "Residence Inn Houston Galleria", "Towneplace Suites Houston Brookhollow", "Holiday Inn Oklahoma City Airport", 
"Residence Inn Wichita East", "La Quinta Inn & Suites Houston West", "Super 8 by Wyndham Fort Worth South", "Holiday Inn Express Fort Worth",
"Hampton Inn Austin - Arboretum Northwest"]

hotelRatingsCentral["Name"] = centralNames

#HOMEWORK QUESTION NUMBER 3: What is the highest rated hotel in each timezone (Central):
print()
print("The highest rated hotels in the Central Time Zone (tied for a 5.0 rating), all with at least 2 reviews are:")
print()

print(hotelRatingsCentral.to_string(index=False))

del hotelRatingsCentral

#Calculate the average ratings for all hotels in the Mountain Timezone
hotelRatingsMountain = hotels[hotels['HOTEL_TIMEZONE'] == 'Mountain'].groupby('ID_HOTEL', as_index=False)['Rating'].mean().round(2)

hotelRatingsMountain = hotelRatingsMountain.sort_values(by='Rating', ascending=False)

#Filter the data to include only those hotels with a 5.0 rating
hotelRatingsMountain = hotelRatingsMountain[hotelRatingsMountain['Rating'] == 5.00]

#Here, I included the names of all of the top rated hotels in a list
mountainNames = ["Four Seasons Hotel Denver", "Homewood Suites by Hilton Phoenix Airport South"]

hotelRatingsMountain["Name"] = mountainNames

#HOMEWORK QUESTION NUMBER 3: What is the highest rated hotel in each timezone (Mountain):
print()
print("The highest rated hotels in the Mountain Time Zone (tied for a 5.0 rating), all with at least 2 reviews are:")
print()

print(hotelRatingsMountain.to_string(index=False))

del hotelRatingsMountain

#Calculate the average ratings for all hotels in the Pacific Timezone
hotelRatingsPacific = hotels[hotels['HOTEL_TIMEZONE'] == 'Pacific'].groupby('ID_HOTEL', as_index=False)['Rating'].mean().round(2)

hotelRatingsPacific = hotelRatingsPacific.sort_values(by='Rating', ascending=False)

#Filter the data to include only those hotels with a 5.0 rating
hotelRatingsPacific = hotelRatingsPacific[hotelRatingsPacific['Rating'] == 5.00]

#Here, I included the names of all of the top rated hotels in a list
pacificNames = ["The Westin San Jose", "La Quinta Inn San Diego - Miramar", "Laurel Inn San Francisco", "Staybridge Suites Sacramento Natomas",
"Ramada Hotel San Diego North", "Residence Inn Portland North"]

hotelRatingsPacific["Name"] = pacificNames

#HOMEWORK QUESTION NUMBER 3: What is the highest rated hotel in each timezone (Pacific):
print()
print("The highest rated hotels in the Pacific Time Zone (tied for a 5.0 rating), all with at least 2 reviews are:")
print()

print(hotelRatingsPacific.to_string(index=False))

del hotelRatingsPacific

#Filter the data to include only those hotels located in Cleveland, Ohio
cleveland = hotels[(hotels['HOTEL_CITY'] == 'Cleveland') & (hotels['HOTEL_STATE'] == 'OH')].groupby('ID_HOTEL', as_index=False)['Rating'].mean().round(2)
clevelandNames = ["Ritz-Carlton, Cleveland", "Unknown", "Unknown"]
cleveland["Name"] = clevelandNames

#HOMEWORK QUESTION NUMBER 4: What is the highest rated hotel in some particular city (you choose)?
print()
print("The highest rated hotel in the lovely city of Cleveland, Ohio is: ")
print()
print(cleveland.iloc[[0]])

del cleveland

#HOMEWORK QUESTION NUMBER 5: What state provided the most users?
print()
print("The states with the highest amount of review users are: ")
print(userStates.head())

#Create a key, value dictionary featuring the average rating for each hotel divided by timezone.
bestRatingDict = {'Eastern': round(hotelEastMean, 2), 'Central': round(hotelCentralMean, 2), 'Mountain': round(hotelMountainMean, 2),
'Pacific': round(hotelPacificMean, 2)}
print()

#HOMEWORK QUESTION NUMBER 6: What timezone had the highest average hotel rating?
print("The time zone with the best overall ratings for its hotels was:")
print()
print(max(bestRatingDict.items(), key = lambda x: x[1]))

print()

#HOMEWORK QUESTION NUMBER 7: Also construct at least one other question of your own and answer that also
#Using the TripType data frames, a bar plot is produced for each timezone showing the total amounts for each type of trip
#Inspiration found: https://stackoverflow.com/questions/37514686/how-to-plot-a-bar-graph-from-a-pandas-series
print("Below are bar graphs depicting the most common trip types for a hotel stay in the four timezones in the U.S.: ")

easternTripType.plot.bar(title = 'Trip Types for Eastern Timezone Hotels')

plt.show()

centralTripType.plot.bar(title = 'Trip Types for Central Timezone Hotels')

plt.show()

mountainTripType.plot.bar(title = 'Trip Types for Mountain Timezone Hotels')

plt.show()

pacificTripType.plot.bar(title = 'Trip Types for Pacific Timezone Hotels')

plt.show()