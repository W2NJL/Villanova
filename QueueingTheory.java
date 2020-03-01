//********************************************************************
//  QueueingTheory.java       Author: Nick Langan, based on a program by M A Papalaskari
//  Created:  September 30, 2018
//  Modified:  October 1-2, 2018 (Modified program to print minutes, changed formulas, inserted switch loop to generate interview subject)
//
//  This program simulates calls into a radio program made by listeners, in an effort to simulate insight into Queueing Theory.
//
//  Running the program will prompt you to select one of 5 separate weekday shows on Sportsradio 610 WIP AM, utilizing their broadcast lineup circa 2003.
//  Upon selecting a show, the user will be prompted to input a minimum on-air time (giving a defined range), a maximum on-air time, and to indicate via an integer
//  if there is to be an interview on the program.
//  Each one of the 5 Sportsradio 610 WIP AM programs has its arrivalProbability defined by a preset value reflective of the portion of the day its show is aired at.
//  For example, the midday show (Anthony Gargano & Mike Missanelli) has the highest arrivalProbability, due to its daypart during the midday.
//  The overnight show with Big Daddy Graham is least likely to receive listener calls, thus, it has the lowest arrivalProbability.
//
//  The program executes for the amount of hours the show spans, looping 4 segments each hour, and a defined amount of minutes out of 60 per those segments based on a defined value.
//  There are less availble minutes in the morning due to the amount of commercials that are aired (25 minutes worth).
//  The overnight show also has the least amount of commericals, only 10 minutes worth.  
//
//  Once the simulation finishes, a report is printed, featuring the probability of a call per minute, call duration, total number of calls for the show, segments without calls,
//  callers that remained on hold at the completion of the show, callers that hung up during the show, maximum queue length, average queue size, and who the interviews were with on the show.
//********************************************************************
import java.util.*;

public class QueueingTheory {

   public static void main(String[] args)  {
//    *********************************************************************************************************************************************************************
//                                                                       COMMENTS ON VARIABLES USED
//    The integers minutes, hours, and segment are the primary variables of measurement used in the program.  
//    The integer segmentMinutes will define how many minutes there are per each segment, produced by the variable minutes being subtracted from 60 (60 minutes in an hour).  
//    Input values provided by the user are answer (which show do they wish to simulate), minAirtime (minimum amount of time a caller should be on air) and maxAirtime (maximum
//    amount of time a caller should be on air).   
//
//    Each interview subject is assigned with a String, whose use will  be determined randomly later in the program.  
//    *********************************************************************************************************************************************************************
      Scanner scan = new Scanner(System.in);
      Random rand = new Random();   
      String queue = "";           
      int minutes = 1, hours = 1, segment = 1, segmentMinutes = 0, answer = 0, minAirtime = 0, maxAirtime = 0, queueEmpty = 0, max = 0, callCount = 0,
      lostCalls = 0, guest = 0, interviewNum = 0, segmentCount = 1, sum = 0, avgQueueSize = 0, processingInt = 0;                 
      double arrivalProbability = 0, processingTime = 0;      
      String show = "", interviewSubject ="", interview1 = "Doug Pederson", interview2 = "Jay Wright", interview3 = "Gabe Kapler",
      interview4 = "Jason Kelce", interview5 = "Brett Brown", interview6 = "Howie Roseman", interviewOutput = "";      

//    Prints welcome message
     
      System.out.println("Welcome to the Sportsradio 610 WIP listener call simulator, Philly's sports leader!");  
      System.out.println("");
      
//   *********************************************************************************************************************************************************************
//                                                                             User question #1 (Enter minimum amount of airtime for callers)
//   ********************************************************************************************************************************************************************* 
           
      System.out.print("What is the minimum amount of minutes a caller to the show should be placed on air? (Minutes must be greater than 0 and less than 6) ");
         minAirtime = scan.nextInt();
         
            while (minAirtime < 1 || minAirtime > 6)  //610 WIP does not permit any calls greater than 5 minutes on air.  Values less than 1 are also looped out.  
              {
               System.out.print("Please try your answer again. (Minutes must be greater than 0 and less than 6) ");
                  minAirtime = scan.nextInt();
              }
              
//   *********************************************************************************************************************************************************************
//                                                                             User question #2 (Enter maximum amount of airtime for callers)
//   ********************************************************************************************************************************************************************* 
         
      System.out.print("What is the maximum amount of minutes a caller to the show should be placed on air? (Answer must be greater than minimum amount and less than 6) ");
         maxAirtime = scan.nextInt(); 
         
            while (maxAirtime <= minAirtime || maxAirtime > 6) //The maximum airtime value must at least be equal to the minimum value but still cannot be greater than 5.  
              {
               System.out.print("Please try your answer again. (Minutes must be greater than your minimum value and less than 6) ");
                  maxAirtime = scan.nextInt();
              }   
              
//   *********************************************************************************************************************************************************************
//                                                                             User question #3 (Should there be a guest interview on the program)
//   ********************************************************************************************************************************************************************* 
              
      System.out.print("Should there be a guest interview on the program today?  Enter 1 for yes, 0 for no: ");
         guest = scan.nextInt();
         
         while (guest > 1) //Requests another answer for any value greater than 1. 
              {
               System.out.print("Please try your answer again. Should there be a guest interview on the program today?  Enter 1 for yes, 0 for no: ");
                  guest = scan.nextInt();
              }  
              
//   *********************************************************************************************************************************************************************
//                                                                             User question #4 (Select program daypart)
//   *********************************************************************************************************************************************************************               
         
      System.out.println("Please select which program daypart you would like to simulate");
      System.out.println("");
      System.out.println("Enter 1 for Angelo Cataldi & The Morning Team (6:00 AM to 10:00 AM)");
      System.out.println("Enter 2 for Anthony Gargano & Mike Missanelli (10:00 AM to 3:00 PM)");
      System.out.println("Enter 3 for Howard Eskin (3:00 PM to 7:00 PM)");          //a.k.a. "The KING"
      System.out.println("Enter 4 for Glen Macnow (7:00 PM to 12:00 AM)");
      System.out.println("Enter 5 for Big Daddy Graham (12:00 AM to 6:00 AM)");
         answer = scan.nextInt();             
      System.out.println("");   

//    Parameters defined for each program (show name, arrivalProbability, processingTime, length (hours & minutes)
        
         switch (answer)
         {     
            case 1:
                  show = "Angelo Cataldi and The Morning Team";
                  arrivalProbability = 0.75;   //The chance of a caller making it on the air in a given minute
                  processingTime = rand.nextInt(maxAirtime) + minAirtime;     //Formula used to calculate the amount of time the caller remains on the air  
                  hours = 4;        //How many hours the show runs for        
                  minutes = 35;     //How many minutes per hour the show is on the air (the remainder of the hour is filled by commercials)      
                  break;
            case 2:
                  show = "Middays with Anthony Gargano and Mike Missanelli";
                  arrivalProbability = 10;
                  processingTime = rand.nextInt(maxAirtime) + minAirtime;    
                  hours = 4;          
                  minutes = 45;
                  break;
            case 3:
                  show = "Afternoon Drive with Howard Eskin";
                  arrivalProbability = 2;
                  processingTime = rand.nextInt(maxAirtime) + minAirtime;    
                  hours = 4;          
                  minutes = 40;
                  break;
            case 4:
                  show = "Evenings with Glen Macnow";
                  arrivalProbability = 0.10;
                  processingTime = rand.nextInt(maxAirtime) + minAirtime;  
                  hours = 5;          
                  minutes = 45;
                  break;
            case 5:
                  show = "Overnights with Big Daddy Graham";
                  arrivalProbability = 0.02;
                  processingTime = rand.nextInt(maxAirtime) + minAirtime;    
                  hours = 6;          
                  minutes = 50;
                  break;  
            default:            
                  System.out.println("That is not a valid entry.  Please run the program again.");   //The program terminates for any answer other than the range 1-5                            
                  break; 
         
           }                   
                 
      
         if (answer >0 && answer <=5)
         
//   *********************************************************************************************************************************************************************
//                                                                             Queue Theory Simulation begins
//   *********************************************************************************************************************************************************************    
         
            {           
               char personLetter = (char)(rand.nextInt(26) + 'A');    // symbolizes each caller to 610 WIP

               System.out.println("\n\n***************************************");
               System.out.println(show);      //Prints the name of the show being simulated
      
               int count = 0;                //Initializes counter utilized to count number of hours             
                     
               while (count < hours)
               
               {
                  int hourCount = count+1;      //The current hour that is printed by the program during the simulation
                  System.out.println("");
                  System.out.println("**************************Hour " + hourCount + "**************************");
                  
                  
                  if (hourCount > 2)            //In hour 3 of the show and beyond, the probability of a call is multiplied by 1.5
                        arrivalProbability = arrivalProbability * 1.5;                       
                                          
                                                      
                    while (segment <= 4)      //Loops for each of the 4 segments within a given hour
                    {                  
                     int commercials = (60 - minutes)/4;    //Calculates the length of each commercial break in a given segment 
 
 //      Utilized to replace selected segments with an interview subject if the user has input the value for an interview.                       
                     if (guest == 1)                             
                        if (hourCount == 2 || hourCount == 4)                        
                           if (segment == 1)
                           {
                              System.out.println("Segment " + segment + "\t Interview with " + interviewSubject);
                              System.out.println("");
                              System.out.println("Commercial break " + commercials + " minutes");
                              System.out.println("");                         
                                           
                              segment++;
                              segmentCount++;
                              interviewOutput = interviewSubject + " " + interviewOutput;   //String to output the names of the subjects being interviewed in the final report                                                 
               
                           }
                         
                      System.out.println("");
                      System.out.println("\t\t\t\t\t\tSegment " + segment);    //Prints number of current segment in given hour
                      System.out.println("");
                        
                      while (segmentMinutes <= minutes/4)    // main loop of simulation - lasts based on the amount of minutes in each segment
                      {
                         if (rand.nextFloat() < arrivalProbability) 
                         {
                             int n = 0;                                               
                             callCount++;  
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
                          
 //      Utilized to choose interview subjects at random.         
                     interviewNum = rand.nextInt(6) + 1;
         
                     switch (interviewNum)
                      {
                         case 1:
                           interviewSubject = interview1;
                           break;
                        case 2:
                           interviewSubject = interview2;  
                           break;
                        case 3:
                           interviewSubject = interview3;
                           break;
                        case 4:
                           interviewSubject = interview4;
                           break;
                        case 5:        
                           interviewSubject = interview5;
                           break;
                        case 6:
                           interviewSubject = interview6;
                           break;
                        default:
                     }
                     
//   *********************************************************************************************************************************************************************
//                                                                             Minute by minute queue status is displayed
//   *********************************************************************************************************************************************************************
                  
                  System.out.println("Minute " + segmentMinutes + "\t queue length " + queue.length() + "\t queue contents: <<<" + queue + ">>>");
                  System.out.println("");                  

//    serve customer at front of queue (if any) for 1 minute
                  if (queue.length() > 0)
                      queue = queue.substring(1); 
                  
                  processingInt = (int)(processingTime);    //converts Processing Time from double to int

//    Caller hangs up due to excessive wait time                              
                  if (queue.length() > 35)
                  {
                     queue = queue.substring(processingInt); 
                     lostCalls++;                           //hung up calls are counted
                  }   
                  
                 segmentMinutes++;                       //each minute of the segment is incremented
                  
               }
               
             sum += queue.length();
             avgQueueSize = Math.round(sum/segmentCount);      //calculates average queue length
             
             commercials = (60 - minutes)/4;       
             System.out.println("Commercial break " + commercials + " minutes");    //prints commercial break and its length
                                            
                   
             segment++;                                  //segment is incremented
             segmentCount++;                             //count for segment is incremented
             segmentMinutes=0;                           //segment minutes count is reset at the start of a new segment
                  
                  
           
             }
         
          segment = 1;                                  //segment count is reset at the start of a new hour
          
          count++;                                      //count of hour is incremented 
                    
         }
      
//   *********************************************************************************************************************************************************************
//                                                                             Prints simulation report
//   *********************************************************************************************************************************************************************      
         
      System.out.println("\n\n***************************************");
      System.out.println("WIP show report  " + show);
      System.out.println("");
      System.out.println("Calls placed with probability per minute: " + arrivalProbability);
      System.out.println("Call duration:  " + processingInt + " minutes");
      System.out.println("Number of calls for show: " + callCount);
      System.out.println("Minutes without calls: " + queueEmpty);
            
      String temp="";
      
      int queueCounter = 0;
      if (queue.length() != 0)           
      {
      queueCounter = 1;
      for (int i = 1; i < queue.length(); i++)        //for loop used to determine amount of callers who remained on hold at end of the program
      {
         if (queue.charAt(i) != queue.charAt(i - 1))
            {
             queueCounter++;
            }
      }
      }
      
           
            
      System.out.println("Callers that were on hold at the end of the program: " + queueCounter);
      System.out.println("Callers that hung up: " + lostCalls);
      System.out.println("Maximum queue length: " + max);      
      System.out.println("Average queue size: " + avgQueueSize);
      System.out.println("");
      if (guest ==1)                               //prints names of subjects interviewed if the show had interviews
         System.out.println("Interviews today with: " + interviewOutput);
      System.out.println("\n\n***************************************");
      }   
    }
  }   

         
         
  
