# -*- coding: utf-8 -*-
"""
Created on Tue Jan 22 10:20:50 2019

@author: Nick Langan
CSC 5930
This program generates a list of all prime numbers between 1 and a user 
provided integer.
"""
import math # math library is imported to use sqrt function.

# User is greeted and prompted to enter a prime number.
print ("Welcome to the prime number finder, where we identify all prime numbers "
       "between 1 and the number of your choosing!")

inputVar = input ("Please enter a number: ")
n = int(inputVar) # The input variable is converted into an integer.

 # The list is created which will later hold the contents of prime numbers.
primeNumbers = list()

# For loop utilized to generate prime numbers through given range.
for i in range (2, n+1):
    for g in range (2, (int)(math.sqrt(i))+1):
        if i % g == 0:
            break
    else:
        primeNumbers.append(i)

# If the user enters the integer 1 or below, no list is provided.      
if (n <= 1):
    print("Sorry, the number you entered is less than 2 and is not valid.")  
 
 # In all other cases, the primeNumbers list is printed via a for loop.      
else:     
        print("The prime numbers between 1 and", n, "are:")
        for m in primeNumbers:
            print(m)
        
