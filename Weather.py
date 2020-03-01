# -*- coding: utf-8 -*-
"""
Created on Sat Mar  2 23:06:04 2019

@author: Nick Langan
CSC 5930

This program performs the following:
    
    1.  Using data supplied from the SC ACIS database, maintained by NOAA's Regional Climate Centers,
    8 separate CSV files containing 100 years of daily climate observations from 8 different climate
    sites across the United States are concatenated into one Pandas DataFrame.
    2. 7 columns are kept for each day from the original CSV file:
        - Date (Year/Month/Day)
        - Max (High temperature for the date)
        - Min (Low temperature for the date)
        - Avg (The mean temperature for the date)
        - AvgDeparture (The amount of degrees the Avg temperature for the date deviates +/- from the 
        30-year average mean temperature for that date as tabulated by NOAA)
        - Precip (The amount of recorded precipitation in liquid form that was recorded for the date)
        - Snowfall (The amount of snowfall measured for the date)
    All data is measured in U.S. customary units (fahrenheit, inches)
    3.  The 8 cities included in the dataset are:
        - Philadelphia, PA
        - Cleveland, OH
        - Tampa, FL
        - Dallas/Fort-Worth, TX
        - Grand Forks, ND
        - Boulder, CO
        - Seattle, WA
        - Los Angeles, CA
    4. This program tries to provide answers to the following questions using graphs of data via Pandas, Matplotlib, and Seaborn:
        WINTER
        Are winters getting warmer?  
        Is the duration of winter shortening?
        Are larger snowstorms becoming more frequent?
        SUMMER
        Are summers getting hotter?
        Is there more of an increase in warmer low temperatures (Urban Heat Island effect)?
        Is September becoming a summer month?
        
        Are there more 1” rainfalls in previous years or decades?
        Linear Regression:  Does a particular region experience more temperature variation?
        A few other oddities
    5. Known issues:
        - Multiple SettingWithCopyWarnings (needs troubleshooting, does not prevent functionality)
        - Folium's choropleth map method is depreceated (needs troubleshooting, does not prevent functionality)

"""

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from datetime import datetime
from datetime import timedelta
import seaborn as sns
sns.set()
import statsmodels.api as sm
import statsmodels.formula.api as smf
import matplotlib.dates as mdates
import folium 
from folium.features import DivIcon
import os
from os.path import join

cities = ['PHL', 'CLE', 'TAM', 'DFW', 'GF', 'BOU', 'SEA', 'LA', 'ANC']
pieces = []
columns = ['Date', 'Max', 'Min', 'Avg', 'AvgDeparture', 'Precip', 'Snowfall']
for city in cities:   
    path = '%s/%sdata.csv'% (city, city)
    frame = pd.read_csv(path,names=columns, skiprows=1, parse_dates=[0])    
    frame['City'] = city
    pieces.append(frame) 
#Concatenate into one DataFrame
climate = pd.concat(pieces)

#Split datetime column into year, month, day for each record
climate['Year'] = climate['Date'].dt.year
climate['Month'] = climate['Date'].dt.month
climate['Day'] = climate['Date'].dt.day
climate['DayOfYear'] = climate['Date'].dt.dayofyear

climate['WinterYear'] = np.where(climate['Month']>6, climate['Year'] + 1, climate['Year']) 
climate['WinterYear'][climate['WinterYear'] == 2019]=2018

#Determine the meteorlogical season for each month
conditions = [
    (climate['Month'] >= 3) & (climate['Month'] < 6),
    (climate['Month'] >= 6) & (climate['Month'] < 9),
    (climate['Month'] >= 9) & (climate['Month'] < 12),
    (climate['Month'] < 3) | (climate['Month'] == 12)]
choices = ['spring', 'summer', 'fall', 'winter']

#Create a season column in the dataframe
climate['Season'] =  np.select(conditions, choices)

conditions = [
    (climate['Year'] >= 1919) & (climate['Year'] < 1930),
    (climate['Year'] >= 1930) & (climate['Year'] < 1940),
    (climate['Year'] >= 1940) & (climate['Year'] < 1950),
    (climate['Year'] >= 1950) & (climate['Year'] < 1960),
    (climate['Year'] >= 1960) & (climate['Year'] < 1970),
    (climate['Year'] >= 1970) & (climate['Year'] < 1980),
    (climate['Year'] >= 1980) & (climate['Year'] < 1990),
    (climate['Year'] >= 1990) & (climate['Year'] < 2000),
    (climate['Year'] >= 2000) & (climate['Year'] < 2010),
    (climate['Year'] >= 2010) & (climate['Year'] < 2020)]
choices = ['1920s', '1930s', '1940s', '1950s', '1960s', '1970s', '1980s', '1990s', '2000s', '2010s']

climate['Decade'] =  np.select(conditions, choices)

#Create a month_year column
climate['month_year'] = climate.Date.dt.to_period('M')

#Set the date column to be the climate dataframe's index
climate.set_index('Date', inplace=True)


#Clean the data of 'M' and 'T' values
climate['Min'] = climate['Min'].replace('M', pd.np.nan)
climate['Min'] = climate['Min'].replace(' M', pd.np.nan)
climate['Min'] = pd.to_numeric(climate['Min'], errors='coerce')

climate['Max'] = climate['Max'].replace('M', pd.np.nan)
climate['Max'] = climate['Max'].replace(' M', pd.np.nan)
climate['Max'] = pd.to_numeric(climate['Max'], errors='coerce')

climate.loc[climate['Precip'] == 'T', 'Precip'] = 0
climate.loc[climate['Precip'] == ' T', 'Precip'] = 0
climate.loc[climate['Precip'] == ' S', 'Precip'] = 0
climate['Avg'] = climate['Avg'].replace('M', pd.np.nan)
climate['Avg'] = climate['Avg'].replace(' M', pd.np.nan)
climate['Avg'] = pd.to_numeric(climate['Avg'], errors='coerce')  
climate['AvgDeparture'] = climate['AvgDeparture'].replace('M', pd.np.nan)
climate['AvgDeparture'] = climate['AvgDeparture'].replace(' M', pd.np.nan)    
climate['AvgDeparture'] = pd.to_numeric(climate['AvgDeparture'], errors='coerce')

#Create 'SnowObserved' column on dates where snowfall is observed but not necessarily measured (T) values
climate.loc[(climate.Snowfall != '0') & (climate.Snowfall != 'M') & (climate.Snowfall != ' M') & (climate.Min <= 40), 'SnowObserved'] = 1

#Cleanse the Snowfall and Precip data of 'M' and 'T' values
climate['Snowfall'] = climate['Snowfall'].replace('M', pd.np.nan)
climate['Snowfall'] = climate['Snowfall'].replace(' M', pd.np.nan) 
climate['Snowfall'] = climate['Snowfall'].replace('T', pd.np.nan)
climate['Snowfall'] = climate['Snowfall'].replace(' T', pd.np.nan)  
climate['Snowfall'] = pd.to_numeric(climate['Snowfall'], errors='coerce')
climate['Precip'] = climate['Precip'].replace('M', pd.np.nan)
climate['Precip'] = climate['Precip'].replace(' M', pd.np.nan)   
climate['Precip'] = climate['Precip'].replace('T', pd.np.nan)
climate['Precip'] = climate['Precip'].replace(' T', pd.np.nan)
climate['Precip'] = pd.to_numeric(climate['Precip'], errors='coerce')

####HEATWAVES####
N = 3

# contition-1  df.temp >= 90
c1 = climate.Max.ge(90)

# group label (each group forms a streak)
g = (c1 != c1.shift()).cumsum()

df1 = climate.assign(
    cnt=climate.groupby(g).DayOfYear.transform('count')
  , n=climate.groupby(g).agg('cumcount')
  , g=g
)

# condition-2: cnt >= N , a streak must have at least N rows
c2 = df1.cnt.ge(N)

# condition-3: (n%N)==0 and (n+N) <= cnt
# the last n%N==0 might not have enough dates for a N-day streak
c3 = df1.n.mod(N).eq(0) & df1.n.le(df1.cnt-N)

#####WINTER QUESTIONS#######

#Average winter temperature by year, 8 cities
winterAvgByCity = climate[climate['Season']=='winter'].pivot_table(['Avg'], index=['WinterYear'], columns=['City'])
ax = winterAvgByCity.plot(figsize=(12,10), xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 8 location aggregate')
ax.legend(["Boulder", "Cleveland", "Dallas", "Grand Forks", "Los Angeles", "Philadelphia", "Seattle", "Tampa"]);
ax.set_ylabel("Degrees (F)")

#Average winter temperature by decade, 8 city aggregate
fig, ax = plt.subplots(figsize=(24, 8))
winterAvgDecade = climate[climate['Season']=='winter'].pivot_table(['Avg'], index=['Decade'])
winterAvgDecade.plot(ax=ax, kind='bar', ylim=(37, 42), title = 'Average winter temperature by decade, 1919-2018 for 8 surveyed US locations')
ax.set_ylabel("Degrees (F)")

#Average winter temperature by year, 8 city aggregate
fig, ax = plt.subplots(figsize=(24, 8))
winterAvgAgg = climate[climate['Season']=='winter'].pivot_table(['Avg'], index=['WinterYear'])
winterAvgAgg.plot(ax = ax, xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 for 8 US location aggregate')
winterAvgAgg.rolling(window=5).mean().plot(style='k--', ax=ax, label='Rolling Mean')
ax.text(1923.5, 39.1, "39.23F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax.text(1928, 43.5, "Winter of 1931-32", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(1968, 35.05, "Winters of 1977-78 & 1978-79", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(1978, 44.8, "Winter of 1982-83", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(2016, 41.55, "41.95F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax.set_ylabel("Degrees (F)")
ax.set_xlabel("Year")
ax.legend(["Average", "Rolling 5-year Average"]);

#Average winter temperature by year, Philadelphia
fig2, ax2 = plt.subplots(figsize=(24, 8))
winterAvgAggPHL = climate[(climate['Season']=='winter') & (climate['City']=='PHL')].pivot_table(['Avg'], index=['WinterYear'])
winterAvgAggPHL.plot(ax = ax2, xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 for Philadelphia, PA')
winterAvgAggPHL.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1923, 34.5, "34.74F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.text(1928, 43.7, "Winter of 1931-32", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(1973, 27.2, "Winter of 1976-77", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(2011, 41.55, "Winter of 2015-16", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(2016.5, 37.3, "37.05F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.set_xlabel("Year")
ax2.legend(["Average", "Rolling 5-year Average"]);

#Average winter temperature by year, Cleveland
fig2, ax2 = plt.subplots(figsize=(12, 10))
winterAvgAggCLE = climate[(climate['Season']=='winter') & (climate['City']=='CLE')].pivot_table(['Avg'], index=['Year'])
winterAvgAggCLE.plot(ax = ax2, xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 for Cleveland, OH')
winterAvgAggCLE.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1922, 29.7, "29.75F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.text(1928, 36.9, "Winter of 1931-32", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(1958, 18.9, "Winter of 1962-63", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(1994, 36.8, "Winter of 1997-98", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.text(2016, 30.6, "30.59F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.legend(["Average", "Rolling 5-year Average"]);

#Top 10 warmest winters, 8 location aggregate
fig5, ax5 = plt.subplots(figsize=(12, 10))
winterAvgAgg.agg('Avg').sort_values(ascending=False)[:10].plot(ax = ax5, kind='bar', ylim=(40, 45), title = 'Top 10 warmest winters 8 location aggregate')
ax5.set_ylabel("Degrees (F)")

#Top 10 coldest winters, 8 location aggregate
fig6, ax6 = plt.subplots(figsize=(12, 10))
winterAvgAgg.agg('Avg').sort_values(ascending=True)[:10].plot(ax = ax6, kind='bar', ylim=(33, 38), title = 'Top 10 coldest winters 8 location aggregate' )
ax6.set_ylabel("Degrees (F)")

#Top 10 warmest winters, Philadelphia
fig7, ax7 = plt.subplots(figsize=(12, 10))
winterAvgAggPHL.agg('Avg').sort_values(ascending=False)[:10].plot(ax = ax7, kind='bar', ylim=(37, 43), title = 'Top 10 warmest winters Philadelphia, PA')
ax7.set_ylabel("Degrees (F)")

#Top 10 coldest winters, Philadelphia
fig8, ax8 = plt.subplots(figsize=(12, 10))
winterAvgAggPHL.agg('Avg').sort_values(ascending=True)[:10].plot(ax = ax8, kind='bar', ylim=(26, 32), title = 'Top 10 coldest winters Philadelphia, PA' )
ax8.set_ylabel("Degrees (F)")

#Average winter temperature by year, Seattle
fig2, ax2 = plt.subplots(figsize=(12, 10))
winterAvgAggSEA = climate[(climate['Season']=='winter') & (climate['City']=='SEA')].pivot_table(['Avg'], index=['Year'])
winterAvgAggSEA.plot(ax = ax2, xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 for Seattle, WA')
winterAvgAggSEA.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1920, 39.9, "40.00F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.text(2016, 43.09, "43.09F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.legend(["Average", "Rolling 5-year Average"]);

#Average winter temperature by year, Los Angeles
fig2, ax2 = plt.subplots(figsize=(24, 8))
winterAvgAggLA = climate[(climate['Season']=='winter') & (climate['City']=='LA')].pivot_table(['Avg'], index=['Year'])
winterAvgAggLA.plot(ax = ax2, xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average winter temperature by year, 1919-2018 for Los Angeles, CA')
winterAvgAggLA.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1923, 56.5, "56.94F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.text(2016, 60.4, "60.31F", horizontalalignment='left', size='medium', color='red', weight='semibold')
ax2.text(1945, 51.4, "Winter of 1948-49", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.legend(["Average", "Rolling 5-year Average"]);


#Number of days with snow observed over 100 years, 8 cities
total = sum(climate[climate['SnowObserved'] == True]['City'].value_counts(sort=True))

snowTotals = climate[climate['SnowObserved'] == True]['City'].value_counts()

didItSnow = climate[climate['SnowObserved'] == True]['City'].value_counts(sort=False)
fig11, ax11 = plt.subplots(figsize=(12, 12))
snowTotals .plot(kind='bar', ax = ax11, title = 'Number of days with observed snow falling, 1919-2018')
for p in ax11.patches:
    ax11.annotate(str(p.get_height()), (p.get_x() * 1.035, p.get_height() * 1.005), weight='bold')
ax11.set_ylabel("Days with snow")
ax11.set_xlabel("Cities")

#Number of days with snow observed over 100 years, Los Angeles
fig12, ax12 = plt.subplots(figsize=(12, 10))
didItSnowInLA = climate[(climate['SnowObserved'] == True) & (climate['City'] == 'LA')]
didItSnowInLA.reset_index().plot(ax = ax12, x='month_year', y='Snowfall', kind='bar', title = 'Days in Los Angeles, CA with observed snowfall')
ax12.set_ylabel("Snowfall (inches)")  
ax12.set_xlabel("Month and year (values of 0 equal a trace)")
ax12.get_legend().remove()
  
#Winter monthly departure from average, 1919-2018 8 location aggregate
winterAvgByMoYear = climate[climate['Season']=='winter'].pivot_table(['AvgDeparture'], index=['month_year'])
winterAvgByMoYear['Truth'] = winterAvgByMoYear['AvgDeparture'] > 0
ax = winterAvgByMoYear.plot(kind='bar', width=1, legend=None, figsize=(12,10), xlim=(1916, 2022),  title = 'Winter monthly departure from average, 1919-2018 8 location aggregate', color=[winterAvgByMoYear.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average temperature (F)")
ax.set_xlabel("Month & year")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        

#Winter monthly departure from average winter temperature by the decades
avg1920s = winterAvgByMoYear[(winterAvgByMoYear.index>=1920) & (winterAvgByMoYear.index<1930)]

ax = avg1920s.plot(kind='bar', width=1, legend=None, figsize=(12,10), xlim=(1916, 2022),  title = 'Winter monthly departure from average winter temperature, 1919-1929 8 location aggregate', color=[avg1920s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average temperature (F)")
ax.set_xlabel("Month & year")


every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg1930s = winterAvgByMoYear[(winterAvgByMoYear.index>=1930) & (winterAvgByMoYear.index<1940)]

ax = avg1930s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1930-1939 8 location aggregate', color=[avg1930s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        
avg1940s = winterAvgByMoYear[(winterAvgByMoYear.index>=1940) & (winterAvgByMoYear.index<1950)]

ax = avg1940s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1940-1949 8 location aggregate', color=[avg1940s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        
avg1950s = winterAvgByMoYear[(winterAvgByMoYear.index>=1950) & (winterAvgByMoYear.index<1960)]
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

ax = avg1950s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1950-1959 8 location aggregate', color=[avg1950s.Truth.map({True: 'r', False: 'b'})])

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        
avg1960s = winterAvgByMoYear[(winterAvgByMoYear.index>=1960) & (winterAvgByMoYear.index<1970)]

ax = avg1960s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1960-1969 8 location aggregate', color=[avg1960s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        
avg1970s = winterAvgByMoYear[(winterAvgByMoYear.index>=1970) & (winterAvgByMoYear.index<1980)]

ax = avg1970s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1970-1979 8 location aggregate', color=[avg1970s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg1980s = winterAvgByMoYear[(winterAvgByMoYear.index>=1980) & (winterAvgByMoYear.index<1990)]

ax = avg1980s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1980-1989 8 location aggregate', color=[avg1980s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg1990s = winterAvgByMoYear[(winterAvgByMoYear.index>=1990) & (winterAvgByMoYear.index<2000)]

ax = avg1990s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 1990-1999 8 location aggregate', color=[avg1990s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg2000s = winterAvgByMoYear[(winterAvgByMoYear.index>=2000) & (winterAvgByMoYear.index<2010)]

ax = avg2000s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 2000-2009 8 location aggregate', color=[avg2000s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg2010s = winterAvgByMoYear[(winterAvgByMoYear.index>=2010) & (winterAvgByMoYear.index<2020)]

ax = avg2010s.plot(kind='bar', width=1, legend=None, figsize=(12,10), title = 'Winter monthly departure from average, 2010-2019 8 location aggregate', color=[avg2010s.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average in (F)")  
ax.set_xlabel("Month and year ")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

avg2010s['Truth'].value_counts()

#Winter monthly departure from average winter temperature, Philadelphia

phlAvgDept = climate[(climate['Season']=='winter') & (climate['City']=='PHL')].pivot_table(['AvgDeparture'], index=['month_year'])
phlAvgDept['Truth'] = phlAvgDept['AvgDeparture'] > 0

ax = phlAvgDept.plot(kind='bar', width=1, legend=None, figsize=(12,10), xlim=(1916, 2022),  title = 'Winter monthly departure from average, 1919-2018 Philadelphia, PA', color=[phlAvgDept.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average temperature (F)")
ax.set_xlabel("Month & year")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
phlAvgDept.apply(lambda x: pd.Series(x.nlargest(5).index))        
phlAvgDept[phlAvgDept.index>=1990]['Truth'].value_counts()

#Winter monthly departure from average winter temperature, Philadelphia

laAvgDept = climate[(climate['Season']=='winter') & (climate['City']=='LA')].pivot_table(['AvgDeparture'], index=['month_year'])
laAvgDept ['Truth'] = laAvgDept['AvgDeparture'] > 0

ax = laAvgDept.plot(kind='bar', width=1, legend=None, figsize=(12,10), xlim=(1916, 2022),  title = 'Winter monthly departure from average, 1919-2018 Los Angeles, CA', color=[laAvgDept.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average temperature (F)")
ax.set_xlabel("Month & year")

every_nth = 8
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)
        
#Date of first freeze per year, Philadelphia
        
yo = climate[(climate.Min <= 32) & (climate.Month >=7) & (climate.City == 'PHL')].groupby(['Year']).first()
yo2 = climate[(climate.Min <= 32) & (climate.Month <=6) & (climate.City == 'PHL')].groupby(['Year']).last()
yo['Last32'] = yo2['DayOfYear']

yo['DaysBetween32'] = yo['DayOfYear'].sub(yo['Last32'], axis = 0) 
fig, ax = plt.subplots(figsize=(12, 12))
labels = ['Oct. 7', 'Oct. 7', 'Oct. 17', 'Oct. 27', 'Nov. 6', 'Nov. 16', 'Nov. 26', 'Dec. 6']
yo.plot(y='DayOfYear', ax=ax, legend=None, title = 'Date of first freeze over the past 100 years, Philadelphia')
ax.text(1930, 288, "PHL International opens", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.set_yticklabels(labels)

#Days between freezes per year, Philadelphia

fig, ax = plt.subplots(figsize=(12, 12))
yo.plot(y='DaysBetween32', ax=ax, legend=None, title = 'Days between last and first freeze, Philadelphia, PA')

yo = climate[(climate.Min <= 32) & (climate.Month >=7) & (climate.City == 'DFW')].groupby(['Year']).first()
yo2 = climate[(climate.Min <= 32) & (climate.Month <=6) & (climate.City == 'DFW')].groupby(['Year']).last()

yo['Last32'] = yo2['DayOfYear']

yo['DaysBetween32'] = yo['DayOfYear'].sub(yo['Last32'], axis = 0) 

#Days between freezes per year, Dallas

fig, ax = plt.subplots(figsize=(15,7))
yo.plot(y='DaysBetween32', ax=ax, legend=None, figsize=(12,12), title = 'Days between last and first freeze, Dallas-Fort Worth, TX')

yo = climate[(climate.Min <= 32) & (climate.Month >=7) & (climate.City == 'GF')].groupby(['Year']).first()
yo2 = climate[(climate.Min <= 32) & (climate.Month <=6) & (climate.City == 'GF')].groupby(['Year']).last()
yo['Last32'] = yo2['DayOfYear']
yo['DaysBetween32'] = yo['DayOfYear'].sub(yo['Last32'], axis = 0) 

#Date of first freeze per year, Grand Forks

fig, ax = plt.subplots(figsize=(12, 12))
labels = ['Aug. 28', 'Aug. 28', 'Sep. 7', 'Sep. 17', 'Sep. 27', 'Oct. 7', 'Oct. 17']
yo.plot(y='DayOfYear', ax=ax, legend=None, title = 'Date of first freeze over the past 100 years, Grand Forks ND')
ax.set_yticklabels(labels)

#Days between freezes per year, Grand Forks

fig, ax22 = plt.subplots(figsize=(15,7))
yo.plot(y='DaysBetween32', ax=ax22, legend=None, figsize=(12,12), title = 'Days between last and first freeze, Grand Forks, ND')

#Date of first freeze over the past 100 years, Boulder, CO

yo = climate[(climate.Min <= 32) & (climate.Month >=7) & (climate.City == 'BOU')].groupby(['Year']).first()
fig, ax = plt.subplots(figsize=(15,7))
labels = ['Sep.7', 'Sep.7', 'Sep, 17', 'Sep. 27', 'Oct. 7', 'Oct. 17', 'Oct. 27']
yo.plot(y='DayOfYear', ax=ax, legend=None, figsize=(12,10), title = 'Date of first freeze over the past 100 years, Boulder, CO')
ax.set_yticklabels(labels)

#Snow by winter season in Philadelphia

fig, ax = plt.subplots(figsize=(15,7))
snow = climate[climate['City']=='PHL'].groupby(['WinterYear'])['Snowfall'].sum().plot(ax=ax, color='purple', title='Snow by winter season in Philadelphia')
ax.set_ylabel("Snow in inches")
ax.set_xlabel("Year")

#Days in Philadelphia with 10 inches or more of snowfall

fig, ax = plt.subplots(figsize=(12,12))
bigStorms = climate[(climate['City']=='PHL') & (climate['Snowfall']>10)]
ye = bigStorms.groupby(['Decade']).sum()
ye.plot(kind='bar', y='SnowObserved', ax=ax, legend=None, title='Days in Philadelphia with 10 inches or more of snowfall')

#Days in Boulder with 10 inches or more of snowfall

bigStorms = climate[(climate['City']=='BOU') & (climate['Snowfall']>10)]
fig, ax = plt.subplots(figsize=(10,10))
ye = bigStorms.groupby(['Decade']).sum()
ye.plot(kind='bar', y='SnowObserved', ax=ax, legend=None, title='Days in Boulder with 10 inches or more of snowfall')

#Days in Grand Forks with 5 inches or more of snowfall

bigStorms = climate[(climate['City']=='GF') & (climate['Snowfall']>5)]
fig, ax = plt.subplots(figsize=(10,10))
ye = bigStorms.groupby(['Decade']).sum()
ye.plot(kind='bar', y='SnowObserved', ax=ax, legend=None, title='Days in Grand Forks with 5 inches or more of snowfall')

#Days in Cleveland with 5 inches or more of snowfall

bigStorms = climate[(climate['City']=='CLE') & (climate['Snowfall']>5)]
fig, ax = plt.subplots(figsize=(10,10))
ye = bigStorms.groupby(['Decade']).sum()
ye.plot(kind='bar', y='SnowObserved', ax=ax, legend=None, title='Days in Cleveland with 5 inches or more of snowfall')


#####SUMMER QUESTIONS#######


#Average summer temperature by year, 8 cities
summerAvgByCity = climate[climate['Season']=='summer'].pivot_table(['Avg'], index=['Year'], columns=['City'])
ax = summerAvgByCity.plot(figsize=(12,10), xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average summer temperature by year, 1919-2018 8 location aggregate')
ax.legend(["Boulder", "Cleveland", "Dallas", "Grand Forks", "Los Angeles", "Philadelphia", "Seattle", "Tampa"]);
ax.set_ylabel("Degrees (F)")

#Average summer temperature by decade, 8 city aggregate
fig, ax = plt.subplots(figsize=(24, 8))
summerAvgDecade = climate[climate['Season']=='summer'].pivot_table(['Avg'], index=['Decade'])
summerAvgDecade.plot(ax=ax, kind='bar', color='red', ylim=(68, 76), title = 'Average summer temperature by decade, 1919-2018 for 8 surveyed US locations')
ax.set_ylabel("Degrees (F)")

#Average summer temperature by year, 8 city aggregate
fig, ax = plt.subplots(figsize=(24, 8))
summerAvgAgg = climate[climate['Season']=='summer'].pivot_table(['Avg'], index=['Year'])
summerAvgAgg.plot(ax = ax, color='red', xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average summer temperature by year, 1919-2018 for 8 US location aggregate')
summerAvgAgg.rolling(window=5).mean().plot(style='k--', ax=ax, label='Rolling Mean')
ax.text(1923.5, 71.8, "72.14F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(1923, 70.6, "Summer of 1927", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(2000, 71.7, "Summer of 2004", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(2012, 76.0, "Summer of 2018", horizontalalignment='left', size='medium', color='black', weight='semibold')
ax.text(2017, 75.0, "75.01F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.set_ylabel("Degrees (F)")
ax.legend(["Average", "Rolling 5-year Average"]);

#Average summer temperature by year, Philadelphia
fig2, ax2 = plt.subplots(figsize=(12, 10))
summerAvgAggPHL = climate[(climate['Season']=='summer') & (climate['City']=='PHL')].pivot_table(['Avg'], index=['Year'])
summerAvgAggPHL.plot(ax = ax2, color='red', xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average summer temperature by year, 1919-2018 for Philadelphia, PA')
summerAvgAggPHL.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1918, 74.3, "74.35F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax2.text(2016, 76.9, "77.12F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.legend(["Average", "Rolling 5-year Average"]);

#Average summer temperature by year, Seattle
fig2, ax2 = plt.subplots(figsize=(24, 8))
summerAvgAggSEA = climate[(climate['Season']=='summer') & (climate['City']=='SEA')].pivot_table(['Avg'], index=['Year'])
summerAvgAggSEA.plot(ax = ax2, color='red', xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Average summer temperature by year, 1919-2018 for Seattle, WA')
summerAvgAggSEA.rolling(window=5).mean().plot(style='k--', ax=ax2, label='Rolling Mean')
ax2.text(1922, 62.0, "62.04", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax2.text(2017, 67.3, "67.35F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax2.set_ylabel("Degrees (F)")
ax2.legend(["Average", "Rolling 5-year Average"]);

#Average min/max/avg Summer temperature by year, 8 city aggregate
fig, ax = plt.subplots(figsize=(24, 8))
summerAllAgg = climate[(climate['Season']=='summer')].pivot_table(['Max', 'Min', 'Avg'], index=['Year'])
summerAllAgg.plot(ax = ax, style=['green', 'red', 'blue'], xlim=(1916, 2022), xticks=range(1920, 2021, 10), title = 'Summer average max/min/avg, 1919-2018 for 8 US location aggregate')
summerAllAgg.rolling(window=5).mean().plot(style='k--', ax=ax, label='Rolling Mean')
ax.text(1920, 72.0, "72.14F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(1920, 82.0, "81.61F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(1920, 63.5, "62.66F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2016, 74.4, "75.01F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2016, 84.21, "84.87F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2016, 64.50, "65.14F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.set_ylabel("Degrees (F)")
ax.legend(["Avg", "Max", "Min", "Rolling 5-year Average"]);

#Average autumn temperature 1990-2018, 8 city aggregate
fig, ax = plt.subplots(figsize=(12,12))
fallAvgAgg = climate[(climate['Season']=='fall') & (climate['Year']>1990)].pivot_table(['Avg'], index=['Year'], columns=['Month']).plot(ax=ax, title = 'Autumn months average temperature, 1990-2018 for 8 US location aggregate')
ax.text(1991, 68.0, "68.62F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(1991, 59.5, "59.57F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(1991, 47.3, "47.3F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2018, 70.9, "70.93F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2018, 59.4, "59.47F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.text(2018, 47.5, "47.51F", horizontalalignment='left', size='medium', color='blue', weight='semibold')
ax.legend(["September", "October", "November"]);

#September departure from average, 1919-2018 in Philadelphia     
september = climate[(climate['City']=='PHL')  & (climate['Month']==9)].pivot_table(['AvgDeparture'], index=['month_year'])
september['Truth'] = september['AvgDeparture'] > 0
ax = september.plot(kind='bar', width=1, legend=None, figsize=(12,12), xlim=(1916, 2022),  title = 'September departure from average, 1919-2018 in Philadelphia', color=[winterAvgByMoYear.Truth.map({True: 'r', False: 'b'})])
ax.set_ylabel("Departure from average temperature (F)")
ax.set_xlabel("Month & year")

every_nth = 16
for n, label in enumerate(ax.xaxis.get_ticklabels()):
    if n % every_nth != 0:
        label.set_visible(False)

#Days per year with 1" or more rainfalls across 8 U.S. locations       
haveYouEverSeenTheRain = climate[climate['Precip'] >1]['Year'].value_counts()
fig2, ax13 = plt.subplots(figsize=(12, 12))
haveYouEverSeenTheRain.nlargest(10).plot(kind='bar', ylim=(60, 85), ax=ax13, title = 'Days per year with 1" or more rainfalls across 8 U.S. locations')
ax13.set_ylabel("Number of days")
ax13.set_xlabel("Year")

#Days per year with 1" or more rainfalls, Philadelphia
fig2, ax14 = plt.subplots(figsize=(12, 12))
phlRain = climate[(climate['Precip'] >1) & (climate['City']=='PHL')]['Year'].value_counts()
phlRain.nlargest(10).plot(kind='bar', ylim=(13, 23), ax=ax14, title = 'Days per year with 1" or more rainfalls, Philadelphia')
ax14.set_ylabel("Number of days")
ax14.set_xlabel("Year")


#Decades with the most days of 90+ degree temperatures, Grand Forks
fig12, ax33 = plt.subplots(figsize=(12, 10))
GF90 = climate[(climate['Max']>=90) & (climate['City']=='GF')]['Decade'].value_counts().plot(kind='bar', ax=ax33, title='Decades with the most days of 90+ degree temperatures, Grand Forks')
ax33.set_ylabel("Number of days")
ax33.set_xlabel("Decade")


#Days with max of >=100, Grand Forks
GF100 = climate[(climate['Max']>=100) & (climate['City']=='GF')]

#LINEAR REGRESSION ATTEMPT
climate['AvgAbsolute'] = climate['AvgDeparture'].abs()

avgDeptRegression = smf.ols(formula='AvgAbsolute ~ C(Season)+ C(City) + Precip + Snowfall', data=climate).fit()
avgDeptRegression.summary()
avgWarming = smf.ols(formula='Avg ~ C(Decade)', data=climate).fit()
avgWarming.summary()

#Categorical plot of average temperature by the decades
fig2, ax33 = plt.subplots(figsize=(18, 6))
sns.catplot(x="Decade", y="Avg", ax=ax33, data=climate.sort_values("Decade"), kind="boxen");
plt.close(2)

#Categorical plot of absolute value of average departure by the cities
fig2, ax33 = plt.subplots(figsize=(12, 12))
sns.catplot(x="City", y="AvgAbsolute", data=climate.sort_values("City"), kind="boxen", ax=ax33);
plt.close(2)

#Categorical plot of absolute value of average departure by the seasons
fig2, ax33 = plt.subplots(figsize=(18, 6))
sns.catplot(x="Season", y="AvgAbsolute", data=climate.sort_values("City"), kind="boxen", ax=ax33);
plt.close(2)

#Categorical plot of absolute value of average departure by the decades
fig2, ax33 = plt.subplots(figsize=(18, 6))
sns.catplot(x="Decade", y="AvgAbsolute", data=climate.sort_values("Decade"), kind="boxen", ax=ax33);
plt.close(2)

#Create folium map showing the location of the 8 cities of the dataset
state_geo = os.path.join('', 'us-states.json')
citydata = pd.read_csv('citydata.csv')

cityMap = folium.Map(location=[37, -102], zoom_start=5)

folium.TileLayer('Mapbox Bright').add_to(cityMap)

cityMap.choropleth(
 geo_data=state_geo,
 name='choropleth',
 data=citydata,
 columns=['State', 'Unemployment'],
 key_on='feature.id',
 fill_color='YlGn',
 fill_opacity=0.7,
 line_opacity=0.2,
 
)
folium.LayerControl().add_to(cityMap)

folium.map.Marker(
    [35.45, -120],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Los Angeles</div>',
        )
    ).add_to(cityMap)
    
folium.map.Marker(
    [33.05, -97.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Dallas</div>',
        )
    ).add_to(cityMap)
    
    
folium.map.Marker(
    [47.65, -122],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Seattle</div>',
        )
    ).add_to(cityMap)
    
folium.map.Marker(
    [41.49, -82.09],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Cleveland</div>',
        )
    ).add_to(cityMap)   
    
folium.map.Marker(
    [28.25, -82.47],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Tampa</div>',
        )
    ).add_to(cityMap)   
    
folium.map.Marker(
    [40.82, -105.67],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Boulder</div>',
        )
    ).add_to(cityMap)    
    
folium.map.Marker(
    [41.05, -76.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Philadelphia</div>',
        )
    ).add_to(cityMap)
    
folium.map.Marker(
    [47.92, -98.03],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 18pt; font-family: calibri; color: blue;">Grand Forks</div>',
        )
    ).add_to(cityMap)

cityMap.save('citydata.html')

#Create folium map showing the amount of 100 degree days over 100 years for the 8 cities of the dataset

state_geo = os.path.join('', 'us-states.json')
state_data = pd.read_csv('US_Unemployment_Oct2012.csv')

m = folium.Map(location=[37, -102], zoom_start=5)

m.choropleth(
 geo_data=state_geo,
 name='choropleth',
 data=state_data,
 columns=['State', 'Unemployment'],
 key_on='feature.id',
 fill_color='OrRd',
 fill_opacity=0.7,
 line_opacity=0.2,
 
)
folium.LayerControl().add_to(m)


folium.map.Marker(
    [35.45, -120],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">177</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [33.25, -97.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">1761</div>',
        )
    ).add_to(m)
    
    
folium.map.Marker(
    [47.65, -122],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">3</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [41.89, -82.09],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">12</div>',
        )
    ).add_to(m)   
    
folium.map.Marker(
    [28.25, -82.47],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">0</div>',
        )
    ).add_to(m)   
    
folium.map.Marker(
    [40.52, -105.27],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">43</div>',
        )
    ).add_to(m)    
    
folium.map.Marker(
    [41.15, -76.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">52</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [47.92, -98.03],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">50</div>',
        )
    ).add_to(m)

m.save('100degreedays.html')

#Create folium map showing the amount days with snow observed over 100 years for the 8 cities of the dataset

state_geo = os.path.join('', 'us-states.json')
snow_data = pd.read_csv('SnowLevels.csv')

m = folium.Map(location=[37, -102], zoom_start=5)

m.choropleth(
 geo_data=state_geo,
 name='choropleth',
 data=snow_data,
 columns=['State', 'Unemployment'],
 key_on='feature.id',
 fill_color='PuBu',
 fill_opacity=0.7,
 line_opacity=0.2,
 
)
folium.LayerControl().add_to(m)


folium.map.Marker(
    [35.45, -120],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">11</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [33.25, -97.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">555</div>',
        )
    ).add_to(m)
    
    
folium.map.Marker(
    [47.65, -122],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">1540</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [41.89, -82.09],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">8031</div>',
        )
    ).add_to(m)   
    
folium.map.Marker(
    [28.25, -82.47],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">7</div>',
        )
    ).add_to(m)   
    
folium.map.Marker(
    [40.52, -105.27],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">4250</div>',
        )
    ).add_to(m)    
    
folium.map.Marker(
    [41.15, -76.25],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">2917</div>',
        )
    ).add_to(m)
    
folium.map.Marker(
    [47.92, -98.03],
    icon=DivIcon(
        icon_size=(150,36),
        icon_anchor=(0,0),
        html='<div style="font-size: 24pt; font-family: calibri; color: black;">6508</div>',
        )
    ).add_to(m)

m.save('snowdata.html')

june90s = climate[(climate['Max'] >=90) & (climate['Month']==6) & (climate['City']=='PHL')]['Year'].value_counts().reindex(climate.Year.unique(), fill_value=0)

june90s.nsmallest(10)