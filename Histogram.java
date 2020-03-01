//***************************************************************************************************************************
//  Histogram.java       Author: Nick Langan 
//  Created:  November 21, 2018                   
//  Modified: November 22-25, 2018
//
//  This program creates a histogram via JavaFX to visually inspect the frequency distribution of set values.
//  The user is provided two options to create a JavaFX histogram.  In the first option, the user provides an arbitrary amount of integers
//  in the range 1 to 100.  The program then organizes each of the provided integers in ranges 1-10, 11-20, 21-30 and so on, up to 
//  91-100.  The frequency of each instance in the ten ranges is plotted in the histogram.  Any input provided that falls outside the range
//  of 1...100 is discarded.   
//
//  In the second option, a call queue simulation, using a portion of the program I designed in Project 5, for a Sportsradio WIP call-in show,
//  is ran, and the number of calls per hour in a 5-hour show are tallied and plotted on the JavaFX histogram.  The show that is simulated is the evening show
//  with Glen Macnow (the program prefills the arrivalProbability and processingTime values for ease of use).  
//****************************************************************************************************************************

import java.util.*;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Histogram extends Application
{
   //-----------------------------------------------------------------
   //  Constant variable MAX_VALUE is declared to establish the amount of ranges (10) that will be produced for the user provided integer option.
   //-----------------------------------------------------------------
      
    public void start (Stage primaryStage)
    {
      final int MAX_VALUE = 10;
      int selection = 0;
      Random rand = new Random();

      Scanner scan = new Scanner (System.in);
      
    //--------------------------------------------------------------------
    // User is prompted to choose one of two options, the 1st for an input provided histogram, the 2nd for a historgram via a call queue simulation.
    //--------------------------------------------------------------------
               
      System.out.print("Choose your histogram!  Enter 1 to provide a sequence of numbers.  Enter 2 to produce a call queue simulation. ");
         selection = scan.nextInt();
         
    //--------------------------------------------------------------------
    // A while loop accounts for erroneous integer input by the user (other than the numbers 1 or 2)
    //--------------------------------------------------------------------
         
      while (selection < 1 || selection > 2)
      {
         System.out.println("Incorrect selection.  Enter 1 to provide a sequence of numbers.  Enter 2 to produce a call queue simulation."); 
            selection = scan.nextInt();
       }
       
    //--------------------------------------------------------------------
    // An if statement for whether the user selects the input provided histogram option.  A program follows.
    //--------------------------------------------------------------------
            
      if (selection == 1)
      {
      
      int[] histogramData = new int[MAX_VALUE];  //Array is created to store the integers provided by the user
      
    //--------------------------------------------------------------------
    // The user is prompted for integers, with the sentinel of -1 to signal completion.
    //--------------------------------------------------------------------
     
      System.out.print ("Please enter your numbers (in range 1... 100), terminated by -1");
         int answer = scan.nextInt();
      
      int counter1 = 1; //counters are set up, used to compute the ranges that evaluate the integers
      int counter2 = 10;
      
    //--------------------------------------------------------------------
    // An initial while loop accounts for erroneous integer input by the user (if the FIRST integer they enter is outside the range of values or is not equal to -1)
    //--------------------------------------------------------------------
     
     
     while (answer < -1 || answer > 100 || answer == 0)
     {
         System.out.println("This value falls outside the specified range!  Please enter your numbers (in range 1... 100), terminated by -1"); 
            answer = scan.nextInt();
     }  
     
    //--------------------------------------------------------------------
    // While the answer is within the range of 1...100, a for loop evaluates the integer provided and is compared against the counters to 
    // tally the integer in its respective range (1-10, 11-20, 21-30...91-100)
    //--------------------------------------------------------------------      

     
     while (answer >= 1 && answer <= 100)
        {         
         for (int i = 0; i < MAX_VALUE; i++)   
         {      
            if (answer >= counter1 && answer <= counter2)  
            {
               histogramData[i]++;     
               counter1 +=10;
               counter2 +=10; 
            }       
            else             
               counter1 +=10;
               counter2 +=10;                
          }
         
            answer = scan.nextInt(); 
            counter1 = 1;
            counter2 = 10;
          
            while (answer < -1 || answer > 100 || answer == 0) //A second while loop accounts for erroneous input if provided AFTER a correct integer is given
               {
                  System.out.println("This value falls outside the specified range!  Please enter your numbers (in range 1... 100), terminated by -1"); 
                     answer = scan.nextInt();
               }
         }
         
    //--------------------------------------------------------------------
    // Once the sentinel value of -1 is provided by the user, the output JavaFX histogram is generated 
    //--------------------------------------------------------------------      
            
      
      if (answer == -1)
      {
         Text xLabelName = new Text(-20, 355, "Number of instances");    //Label for x-axis is generated     
         xLabelName.setStyle("-fx-font: 16 arial;");
         xLabelName.setRotate(-90);
        
         Text topLabel = new Text(400, 25, "Histogram via user input");  //Title of histogram is generated      
         topLabel.setStyle("-fx-font: 16 arial;");
        
         Text bottomLabel = new Text(400, 805, "Range of number values");   //Label for y-axis is generated     
         bottomLabel.setStyle("-fx-font: 16 arial;");
               
         Group labels = new Group(xLabelName, topLabel, bottomLabel);    //Group for respective labels is created    
         
    //--------------------------------------------------------------------
    // An array that stores the y-axis labels is created.  The labels indicate the increase of their respective range (1-10, 11-20...91-100), generated by a for loop.
    //--------------------------------------------------------------------      
         
         Text[] textArray = new Text[10];
         int labelX = 125;
         int labelY = 775;
         counter1 = 1;
         counter2 = 10;
        
         for (int x = 0; x < MAX_VALUE; x++)
         {
            textArray[x] = new Text(labelX, labelY, counter1 + " - " + counter2);  
            textArray[x].setStyle("-fx-font: 14 arial;");        
            counter1 +=10;
            counter2 +=10;
            labelX +=75;
            labels.getChildren() .add(textArray[x]);
         }
       
        int max = 0;
                
        for (int data: histogramData)  //The maximum value of the amount of times a range is tallied is generated.  This is used to determine the highest number displayed
                                       // on the x-axis.
            if (data > max)
             max = data;

    //--------------------------------------------------------------------
    // An array that stores the x-axis labels is created.  The labels are incremented by 1, up to the maximum tally (plus one), subtracting 60 from the y-axis at each placement.
    //--------------------------------------------------------------------               
        
        Text[] textArray2 = new Text[max+1];
        int labelX2 = 85;
        int labelY2 = 775;
        
        for (int y = 0; y < max+1; y++)
        {
          textArray2[y] = new Text(labelX2, labelY2, y + "");  
          textArray2[y].setStyle("-fx-font: 14 arial;");      
          labelY2 -=60;
          labels.getChildren() .add(textArray2[y]);
        }
        
        Rectangle border = new Rectangle(102, 40, 760, 720);  //A rectangular border for the histogram is generated.
        border.setFill(Color.GREEN);
        border.setStroke(Color.BLACK);
        border.setFill(null);

        Group rectangles = new Group(border);  //The group rectangles is created for the border and the visual representation (bar) for the tallies of the ranges.
       
        int counter3 = 1;
        int histogramDataSample = 0;
        int rectX = 130;
        int rectY = 705;
        int rectHeight = 50;
        int rectWidth = 55;
        
    //--------------------------------------------------------------------
    // An array that stores the coordinates for each rectangles produced as a visual reprsentation for the tally for each range is created.
    //--------------------------------------------------------------------               
        
        Rectangle[] results = new Rectangle[MAX_VALUE];
              
        for (int z = 0; z < MAX_VALUE; z++)
        {
            counter3 = 1;
         
            rectY = 705;   
            rectWidth = 30;
            rectHeight = 55;
                                              
            histogramDataSample = histogramData[z];    
            
         //Each range is compared to the histogramData array via a counter.  If the array value is 0, no rectangle is seen.  If the array value is not equal to the counter,
         //the size of the rectangle is increased.  If the counter is equal to the array value, a rectangle is produced on the histogram.                     
 
          while (histogramDataSample != 0 && histogramDataSample != counter3)  //Size of rectangle continues to increase until counter is met
            {
                counter3++;
                rectY -=60;
                rectHeight +=60;               
            }
           
           if (histogramDataSample == 0)  //No rectangle is produced for an array value of 0
           {
               results[z] = new Rectangle (0, 0, 0, 0);
           }
           
           if (histogramDataSample == counter3)  //The rectangle is produced and ultimately added to the Group rectangles
            {
               results[z] = new Rectangle (rectX, rectY, rectWidth, rectHeight);
               results[z].setFill(Color.GREEN);               
            } 
                                 
            rectangles.getChildren() .add(results[z]); 
            rectX += 75;                  
                                 
        }      
         
    //--------------------------------------------------------------------
    // The JavaFX stage is created.
    //--------------------------------------------------------------------               
        
        Group root = new Group(labels, rectangles);
        
        Scene scene = new Scene(root, 900, 850, Color.WHITE);
        
        primaryStage.setTitle("Histogram via user input");
        primaryStage.setScene(scene);
        primaryStage.show();
        }
       } 

    //--------------------------------------------------------------------
    // An if statement for whether the user selects the call queue simulation histogram option.  A program follows.
    //--------------------------------------------------------------------
      
     if (selection == 2)
     
     {

    //--------------------------------------------------------------------
    // Hours, arrivalProbability, and processingTime are declared as opposed to user provided in Project 5.  In an effort to ensure a seamless
    // transition, I preserved most of the code from Project 5, thus some might be extraneous and unncessary for the simple histogram simulation.
    //--------------------------------------------------------------------     
        String queue = "";  
        double arrivalProbability = 0, processingTime = 0;           
        int minutes = 1, segment = 1, segmentMinutes = 0, answer = 0, minAirtime = 0, maxAirtime = 0, 
        queueEmpty = 0, max = 0, callCount = 0, lostCalls = 0, guest = 0, interviewNum = 0, 
        segmentCount = 1, sum = 0, avgQueueSize = 0, processingInt = 0;                         
        final int HOURS = 5;   
        arrivalProbability = 0.10;
        processingTime = 4;                
        minutes = 45;
           
        char personLetter = (char)(rand.nextInt(26) + 'A');    // symbolizes each caller to 610 WIP
                     
        int count = 0;                //Initializes counter utilized to count number of hours   
        
    //--------------------------------------------------------------------
    // A new addition to the Project 5 code.  An array is created to store the number of calls placed per hour.  The results are ultimately
    // placed on the histogram.  
    //--------------------------------------------------------------------
        int[] callersPerHour = new int [HOURS];   
          
        while (count < HOURS)               
           {
               int hourCount = count+1;  
                  
               if (hourCount > 2)            //In hour 3 of the show and beyond, the probability of a call is multiplied by 1.4
                  arrivalProbability = arrivalProbability * 1.4;
                   
               while (segment <= 4)      //Loops for each of the 4 segments within a given hour
                    {                    
                    while (segmentMinutes <= minutes/4)    // main loop of simulation - lasts based on the amount of minutes in each segment
                      {
                         if (rand.nextFloat() < arrivalProbability) 
                         {
                             int n = 0;                                               
                             callCount++;  
                             callersPerHour[count]++;      // for each caller, the count for callersPerHour is incremented
                             while (n < processingTime)    // randomly decide if a customer has arrived and add to queue  
                              {                                                                                                                            
                                    queue += personLetter;
                                     n++;                                                                                                                                               
                              }
                          }
                                  
                      char newPersonLetter = (char)(rand.nextInt(26) + 'A');
                      
                      while (newPersonLetter == personLetter)
                      {
                        newPersonLetter = (char)(rand.nextInt(26) + 'A');
                      }
                        
                        personLetter = newPersonLetter; // change symbol for next caller                            
                                
                     if (queue.length() == 0)      //Compiles the amount of minutes that had no callers
                        queueEmpty++;
                     
                     if (queue.length() > max)     //Calculates the maximum queue length
                        max = queue.length();                  
                                             
                      segmentMinutes++;                       //each minute of the segment is incremented
                  
               }
            segment++;                                  //segment is incremented
            segmentCount++;                             //count for segment is incremented
            segmentMinutes=0;  
          }
          segment = 1;                                  //segment count is reset at the start of a new hour
          
          count++;
          }
          
         int counter1 = 1;        //counter set up to produce the hour number on the histogram
              
         Text topLabel = new Text(250, 15, "WIP evenings with Glen Macnow");   //Title of histogram is generated      
         topLabel.setStyle("-fx-font: 16 arial;");
       
     
         Text xLabelName = new Text(-10, 405, "Number of Calls");     //Title for x-axis is generated    
         xLabelName.setStyle("-fx-font: 16 arial;");
         xLabelName.setRotate(-90);
               
         Group labels = new Group(xLabelName, topLabel);    //Group for respective labels is created  
         
    //--------------------------------------------------------------------
    // An array that stores the y-axis labels is created.  There is one label for each hour, generated by a for loop (Hour 1, Hour 2...Hour 5).
    //--------------------------------------------------------------------            
         Text[] textArray = new Text[10];
         int labelX = 125;
         int labelY = 800;         
        
         for (int x = 0; x < HOURS; x++)
         {
            textArray[x] = new Text(labelX, labelY, "Hour " + counter1);  
            textArray[x].setStyle("-fx-font: 14 arial;");        
            counter1++;          
            labelX +=125;
            labels.getChildren() .add(textArray[x]);
         }
       
        int hourCallMax = 0;
                
        for (int data: callersPerHour)    //The maximum amount of calls in one of the hours of the show is determined.  
                                          //This value is used to determine the highest number displayedon the x-axis (the maximum plus 3).

         if (data > hourCallMax)
            hourCallMax = data;        
                
    //--------------------------------------------------------------------
    // An array that stores the x-axis labels is created.  The labels are incremented by 3, up to the maximum call amount (plus three), 
    // subtracting 120 from the y-axis at each placement.
    //--------------------------------------------------------------------               
        Text[] textArray2 = new Text[hourCallMax+3];
        int labelX2 = 70;
        int labelY2 = 775;
        
        for (int y = 0; y < hourCallMax+3; y= y+3)
        {
          textArray2[y] = new Text(labelX2, labelY2, y + "");  
          textArray2[y].setStyle("-fx-font: 14 arial;");      
          labelY2 -=120;
          labels.getChildren() .add(textArray2[y]);
        }
        
        Rectangle border = new Rectangle(87, 40, 700, 745);    //A rectangular border for the histogram is generated.
        border.setFill(Color.GREEN);
        border.setStroke(Color.BLACK);
        border.setFill(null);

        Group rectangles = new Group(border);   //The group rectangles is created for the border and the visual representation (bar) for the amount of calls for each hour.
        int counter3 = 1;
        int queueSample = 0;
        int rectX = 130;
        int rectY = 730;
        int rectHeight = 50;
        int rectWidth = 55;

    //--------------------------------------------------------------------
    // An array that stores the coordinates for each rectangles produced as a visual reprsentation for the number of calls for each hour is created.
    //--------------------------------------------------------------------               
        
        Rectangle[] results = new Rectangle[HOURS];       
                     
        for (int z = 0; z < HOURS; z++)
        {
            counter3 = 1;
         
            rectY = 730;
            rectWidth = 30;
            rectHeight = 55;
                                  
            queueSample = callersPerHour[z];       
            
         //Each hour is compared to the callersPerHour array via a counter.  If the array value is 0, no rectangle is seen.  If the array value is not equal to the counter,
         //the size of the rectangle is increased.  If the counter is equal to the array value, a rectangle is produced on the histogram.
                  
            while (queueSample != 0 && queueSample != counter3)   //Size of rectangle continues to increase until counter is met
             {
                counter3++;
                rectY -=40;
                rectHeight +=40;               
             }
           
            if (queueSample == 0)   //No rectangle is produced for an array value of 0
            {
               results[z] = new Rectangle (0, 0, 0, 0);
            }
           
            if (queueSample == counter3)  //The rectangle is produced and ultimately added to the Group rectangles
            {
               results[z] = new Rectangle (rectX, rectY, rectWidth, rectHeight);
               results[z].setFill(Color.RED);               
            } 
                                 
            rectangles.getChildren() .add(results[z]); 
              rectX += 125;                  
                                
        }
        
    //--------------------------------------------------------------------
    // The JavaFX stage is created.
    //--------------------------------------------------------------------                  
        
        Group root = new Group(labels, rectangles);
        
        Scene scene = new Scene(root, 800, 850, Color.WHITE);
        
        primaryStage.setTitle("Shapes");
        primaryStage.setScene(scene);
        primaryStage.show();                 
        
      }     
   }
   
    //--------------------------------------------------------------------
    // The JavaFX stage appears and a thank you message is delivered upon exit of the window.  The program terminates.
    //--------------------------------------------------------------------   
      
    public static void main (String[] args)
   {
            
     launch(args);
     System.out.println("");
     System.out.println("Thank you for using my program!");
     
   }


}
