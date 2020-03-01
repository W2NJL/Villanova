# -*- coding: utf-8 -*-
"""
Created on Sun Feb 10 19:41:31 2019

@author: Nick Langan
CSC 5930
This program performs the following:
    1. Places baby names within the Social Security Administration dataset CSV whose births occurred first decade of the 2000s 
    (2000-2009) into a dataframe.  
    2. Groups the data by name, excludes those names with less than 5 occurrences
    in an effort to preserve useful data.  
    3. Creates a pivot table from the filtered names, the number of births for each
    sex is tallied.  

The steps are repeated from Homework #3 to devise a list of gender neutral names from 2000-2009:
    4. Copy the quantities of births by male and female into a quantity data frame.  
    5. We use the div function to return a decimal that reflects the ratio (or percentage) of 
    a name being either female or male. The female and male ratio columns are appended to original dataframe.
    6. A totalBirths column is introduced for each name, with those names netting less than 5,000 births
    being excluded.  
    7. We create a genderNeutral data frame, extracting those names from the names data frame that have a female name ratio
    between 1/3 and 2/3 (0.333 and 0.666).
    8. A new distance column is added to the genderNeutral data frame, reflecting how prominent the name is in either gender.
    The lower the value, the more gender neutral the name is.  

Then we eliminate the 2000-2009 gender-netural names from the data frame:    
    9. We create a list of names extracted from the genderNeutral data frame. This list is then compared with our original
    names data frame that was imported from the SSA.  We create a new dataset featuring unique names not contained in the genderNeutral data frame.
    10. The uniqueNames data frame is split up into two lists of names, one containing unique boys names and one containing unique girls names.
    Again, this excludes all names considered gender-neutral in the 2000-2009 decade.  The first 20 and last 20 boys and girls names from each
    list (alphabetically) are printed to the output.
    11. Two pivot tables are created using the boyNames and girlNames data frames.  They are pivoted by births, indexed by name, with the sum
    of the births for each name by year tabulated. A fill value of 0 is issued for those years with an NaN value for number of births.    
    12. A separate data frame is created (and eventually deleted) for each gender, using the diff function to calculate the increase or decrease
    of births for each name compared with the year prior.  The absolute value of each difference is taken and then placed back in the respective
    gender data frame.  Eventually, the 'Largest_Diff' column is appended to each gender data frame, using the idxmax method to show the year
    where the largest change compared with its prior year occurred for each of the names.
    13. The idxmax method is used to store the year that had the maximum amount of births for a baby name in a 'Max_Year' column.
    14. An 'Average' column is added to each data frame, showing the mean value for the birth totals from 2000-2009 for each name.
    15. Two new data frames (one for each gender) are created to sort the names by average births, highest to lowest.  
    16. A total number of births for each name is also shown, using the margins=true function upon pivot table creation.
    17. The Tabulate package is used to produce tables for each gender for the top 20 names in terms of
    average births.  The data produced is the total births from 2000-2004 and 2005-2009, as well as the total birth number for the decade,
    average births, maximum year, and largest difference values for each of the names.  The tables are split
    into 3 to be more friendly for output review.  Tabulate seems to be the updated version of the previously used PrettyTable package.
    18. Two plots are produced for each gender.  The first plot shows the totals each year for the Top 10 most used Boy names in the decade,
    with a subsequent plot also shown for Girl names.  The second plot shows the year with the highest amount of births for the Top 10 most used
    Boy names, with a subsequent plot also shown for Girl names.  
"""

import numpy as np
import pandas as pd

# Here, the Tabulate package is loaded through easy_install, so that the
# pre-requsite is in place regardless of the Python environment.  The source
# for the idea for this script can be found at:
# https://stackoverflow.com/a/5944496
from setuptools.command import easy_install
import pkg_resources
easy_install.main( ['tabulate'] )
pkg_resources.require('tabulate') #This line ensures the module can be imported

from tabulate import tabulate #Note, in assignment #2 I used the PrettyTable package to format a printed table.  Upon further investigation,
#it appears that package is "Abandonware", as it has not been updated since 2013.  I am using a reasonable facsimile, Tabulate, which is newly updated.
#Inspiration found here:  https://stackoverflow.com/questions/18528533/pretty-printing-a-pandas-dataframe/24079771

#The SSA dataset is imported.  This location is hard coded to a location on my laptop (this should be able to be safely amended).
years = range(2000,2010)
pieces = []
columns = ['name','sex','births']
for year in years:   
    path = 'C:/Users/nick.langan.NLANGAN-LT/OneDrive - Villanova University/Spring 19/CSC5930/Week 3/names/yob%d.txt'%year
    frame = pd.read_csv(path,names=columns)    
    frame['year'] = year
    pieces.append(frame)   

#The initial names data frame is created.
names = pd.concat(pieces, ignore_index=True)

#Group the data frame by name.
namesGrouped = names.groupby('name')

#We filter out all name occurrences with a quantity less than 100.
newNames = namesGrouped.filter(lambda x: len(x)>=5)

#Pivot the data frame to create birth quantities for each name by sex.
namesDF = pd.pivot_table(newNames, values='births', index='name', columns='sex', fill_value=0, aggfunc=sum)

#Male and female birth total columns are added.
namesDF.columns = ['F', 'M']
 
namesDF.reset_index(drop=False, inplace=True)

#The male and female birth quantities are copied into a new data frame to normalize.
quantityDF = namesDF[['M','F']].copy()

#Since we can safely assume the sum of a ratio of a name of female and male births equals to 1, we calculate
#the ratio for each name being used as a male or female in decimal format.
quantityDF = quantityDF.div(quantityDF.sum(axis=1), axis=0)

#The female and male ratios are appended to the namesDF.
namesDF['FemaleRatio'] = quantityDF['F']
namesDF['MaleRatio'] = quantityDF['M']

#Delete unnecessary data frames to relieve memory
del quantityDF

#A total births column is also appended to the namesDF.
namesDF['totalBirths'] = namesDF['M'] + namesDF['F']

#The names "Unknown" and "Baby" are not meaningful, in my opinion, and I am excluding them from the dataset.
namesDF = namesDF[(namesDF['name'] != 'Unknown') & (namesDF['name'] != 'Baby')]

#We exclude all names with a total birth quantity less than 5000.
namesDF = namesDF[namesDF['totalBirths'] >= 5000]

#A gender neutral data frame is created.  We consider a name gender neutral if its ratio for being used as a female
#name lies between one-third and two-thirds.
genderNeutral = namesDF[(namesDF['FemaleRatio'] >= 0.3333) & (namesDF['FemaleRatio'] <= 0.6666)].copy() 
#NOTE:  The copy() function was used to avoid chained assignment.  More:
#https://www.dataquest.io/blog/settingwithcopywarning/

#A variable distance is created, the difference between the name's use as a female versus a male.  The lower the distance,
#the more gender neutral the name is.  
genderNeutral['distance'] = np.abs(genderNeutral['FemaleRatio'] - 0.5)

genderNeutral = genderNeutral.sort_values(by='distance', ascending=True)

#We extract all names from the genderNeutral list.  This will be used to feed our master data frame.  
nameList = genderNeutral['name'].tolist()

del genderNeutral

#We create a new data frame for all of the unique boys and girls names.  This will use the names from our master data frame,
#but only those names that DO NOT appear in the list created from our genderNeutral data frame.  
uniqueNames = names[names['name'].isin(nameList) == False]

#Create two new data frames split by gender
boyNames = uniqueNames[uniqueNames.sex == 'M']
girlNames = uniqueNames[uniqueNames.sex == 'F']

#Here, we make a list of unique boy names and unique girl names from each data frame
boyNamesList = boyNames['name'].tolist()
girlNamesList = girlNames['name'].tolist()

#Ensure that the lists of names are sorted alphabetically
boyNamesList.sort()
girlNamesList.sort()

#The first and last 20 names from the boy and girl lists are printed to the output
print()
print("Here are the first 20 names in our list of unique boy baby names, 2000-2009:")
print()
print(boyNamesList[:20])
print()
print("Here are the last 20 names in our list of unique boy baby names, 2000-2009:")
print()
print(boyNamesList[-20:])
print()
print("Here are the first 20 names in our list of unique girl baby names, 2000-2009:")
print()
print(girlNamesList[:20])
print()
print("Here are the last 20 names in our list of unique girl baby names, 2000-2009:")
print()
print(girlNamesList[-20:])
print()

del uniqueNames
del boyNamesList
del girlNamesList

#We pivot the boyNames data frame, to create quantities of births for the dataset by year.  The sum of births for each name is calculated
#via margins=true.  We substitute NaN values for years without births with the fill value of 0.  
boy_births = boyNames.pivot_table('births', index='name', columns='year', aggfunc=sum, margins=True).fillna(0)

#A separate data frame is created.  This holds a differential value for the birth total of each name versus its previous year.
boyColumn_difference = boy_births.drop('All', axis=1).diff(axis=1)

#The absolute value of the differential value is substituted for the initial value.  This will help us find the greatest change between years.
boyColumn_difference = boyColumn_difference.abs()

#Not counting the sum of all 10 years, the idxmax method is used to store the year that had the maximum amount of births for a baby name in a separate column.
boy_births['Max_Year'] = boy_births.drop('All', axis=1).idxmax(axis=1)

#Ensure all numeric data in the data frame is type integer
boy_births = boy_births.astype(int)

#The average total births for each name is calculated by taking the mean of each row (with only the columns with the yearly totals included)
boy_births['Average'] = boy_births.iloc[:,0:10].mean(axis=1)
boy_births = boy_births.drop(['All'])

#A Largest_Diff column is created in the boy_births data frame from the differential.  Using the idxmax method, it holds the year that had the greatest change in terms
#of number of births for each name compared with its PREVIOUS year (consecutive)
boy_births['Largest_Diff'] = boyColumn_difference.idxmax(axis=1).astype(int)

del boyColumn_difference

#Before we print the table values, we sort the values by the highest average amount of births for the name.
boyBirthsSort = boy_births.sort_values(by='Average', ascending=False)

del boy_births

#We pivot the girlNames data frame, to create quantities of births for the dataset by year.  The sum of births for each name is calculated
#via margins=true.  We substitute NaN values for years without births with the fill value of 0. 
girl_births = girlNames.pivot_table('births', index='name', columns='year', aggfunc=sum, margins=True).fillna(0)

#A separate data frame is created.  This holds a differential value for the birth total of each name versus its previous year.
girlColumn_difference = girl_births.drop('All', axis=1).diff(axis=1)

#The absolute value of the differential value is substituted for the initial value.  This will help us find the greatest change between years.
girlColumn_difference = girlColumn_difference.abs()

#Not counting the sum of all 10 years, the idxmax method is used to store the year that had the maximum amount of births for a baby name in a separate column.
girl_births['Max_Year'] = girl_births.drop('All', axis=1).idxmax(axis=1)

#Ensure all numeric data in the data frame is type integer
girl_births = girl_births.astype(int)

#The average total births for each name is calculated by taking the mean of each row (with only the columns with the yearly totals included)
girl_births['Average'] = girl_births.iloc[:,0:10].mean(axis=1)
girl_births = girl_births.drop(['All'])

#A Largest_Diff column is created in the boy_births data frame from the differential.  Using the idxmax method, it holds the year that had the greatest change in terms
#of number of births for each name compared with its PREVIOUS year (consecutive)
girl_births['Largest_Diff'] = girlColumn_difference.idxmax(axis=1).astype(int)

del girlColumn_difference

#Before we print the table values, we sort the values by the highest average amount of births for the name.
girlBirthsSort = girl_births.sort_values(by='Average', ascending=False)

del girl_births

girlBirthsSort.rename(columns={'All':'Total'}, inplace=True)

print()

#Plot is printed to the output showing the top 10 boy names from 2000-2009 and their totals for each of the years
boyBirthsSort.iloc[:10,:10].plot(kind='bar', figsize=(10, 10), title="The totals for the Top 10 most used Boy Names, 2000-2009")

#Plot is printed to the output showing the year which each of the top 10 names had their most amount of births
maxBoyPlot = boyBirthsSort.iloc[:10].plot(y='Max_Year', kind='bar', ylim=(1999, 2010), figsize=(10,10), yticks=range(2000, 2010, 1), legend=False,
title="The year with the highest amount of births for most used Boy Names, 2000-2009")
print()

#Tabulate is utilized to print yearly totals, a sum, max_year, average, and the largest difference fromt he previous year for the top 10 boys names.  
#To help formatting, I've split the tables into 3 entries (data from 2000-2004, data from 2005-2009, overall data for the decade)
print("A table showing the the totals of the top 10 boys names from the decade in the years 2000-2004:")
print(tabulate(boyBirthsSort.iloc[:10, :5], headers='keys', tablefmt='fancy_grid'))
print()
print("A table showing the the totals of the top 10 boys names from the decade in the years 2005-2009:")
print(tabulate(boyBirthsSort.iloc[:10, 5:10], headers='keys', tablefmt='fancy_grid'))
print()
print("A table showing the decade total, maximum year, average, and the year that had the greatest change from the year prior for the top 10 boys names 2000-2009:")
print(tabulate(boyBirthsSort.iloc[:10, 10:], headers='keys', tablefmt='fancy_grid'))
print()

#Plot is printed to the output showing the top 10 girl names from 2000-2009 and their totals for each of the years
girlBirthsSort.iloc[:10,:10].plot(kind='bar', figsize=(10, 10), title="The totals for the Top 10 most used Girl Names, 2000-2009")

#Plot is printed to the output showing the year which each of the top 10 names had their most amount of births
maxGirlPlot = girlBirthsSort.iloc[:10].plot(y='Max_Year', kind='bar', figsize=(10,10), ylim=(1999, 2010), yticks=range(2000, 2010, 1), legend=False,
title="The year with the highest amount of births for most used Girl Names, 2000-2009")
print()

#Tabulate is utilized to print yearly totals, a sum, max_year, average, and the largest difference fromt he previous year for the top 10 girls names.  
#To help formatting, I've split the tables into 3 entries (data from 2000-2004, data from 2005-2009, overall data for the decade)
print("A table showing the the totals of the top 10 girls names from the decade in the years 2000-2004:")
print(tabulate(girlBirthsSort.iloc[:10, :5], headers='keys', tablefmt='fancy_grid'))
print()
print("A table showing the the totals of the top 10 girls names from the decade in the years 2005-2009:")
print(tabulate(girlBirthsSort.iloc[:10, 5:10], headers='keys', tablefmt='fancy_grid'))
print()
print("A table showing the decade total, maximum year, average, and the year that had the greatest change from the year prior for the top 10 girls names 2000-2009:")
print(tabulate(girlBirthsSort.iloc[:10, 10:], headers='keys', tablefmt='fancy_grid'))

