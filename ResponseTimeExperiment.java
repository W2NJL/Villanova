//  ResponseTimeExperiment.java      Author: Nick Langan, based on a program by M A Papalaskari
//  Created:  October 16, 2018
//  Modified:  October 17-20, 2018 (Added questions and answers, changed program to loop entire 5-person experiment with 1 run of the program)
//
//  This program simulates a 5-person experiment, quizzing the accuracy and response time of 4 randomly generated questions regarding #1 songs on the Billboard
//  Hot 100 music charts, from the 4 decades of the 1960s, 1970s, 1980s, and 1990s.  
//
//  After inputting a first and last name, the user is asked to respond to a sequence of 4 questions as quickly as possible, with each question selected randomly 
//  out of 4 available questions for each era of music.  Each user's average response time is calculated, along with the question that the user took the most elapsed
//  time on.  The user is given their average response time and number of correct responses (out of 4 questions) at the end of their turn.  The program then advances
//  to the next subject.  After 5 different experiments are conducted, the program outputs the experiment results for each subject.  Each subject is given a grade
//  for their performance, based on a formula that multiplies the number of correct responses by itself, and then that number is subtracted by the response time.
//  The grades assessed range from excellent to failure, and each grade is also connected with a rank, a somewhat subjective ranking that is tied to a name of an artist.
//
//  The end of the experiment output also awards congratulations to the user who received the highest grade during the assessment.  

import java.util.Scanner;
import java.util.Random;

public class ResponseTimeExperiment
{
 public static void main(String[] args)
 {
//    *********************************************************************************************************************************************************************
//                                                                       COMMENTS ON VARIABLES USED
//    The constant NUM_USERS declares the number of participants that will be surveyed in the experiment, while the constant NUM_QUESTIONS declares the number of questions
//    asked in the experiment of each participant.      
//
//    The integer numCorrect is the rolling total of number of correct answers for each run of the experiment, while the variables numCorrectRun1, numCorrectRun2..., etc., 
//    are assigned to each of the 5 users that are part of the experiment, as are the variables maxQuestionRun1, maxQuestionRun2..., etc. (designating the question that had
//    the longest response time for each of the participants.  The rankInt integers contain the number that is used to determine the grade and rank for each participant.  
//
//    Note that the long variables totalTime, reactionTime, and max are initially calculated in milliseconds, but are converted to seconds in the program.  
//
//    The double averageTime calculates the reactionTime divided by the totalTime for each response, while the variables avgTimeRun1, avgTimeRun2..., etc., are assigned to 
//    each participant to determine their average response time.  The maxTime variables determine the length of the longest answer to a question in the experiment was for
//    each participant.  
//
//    The double minAvgTime is used as a tiebreaker to determine which of the 5 participants had the lowest average response time in the experiment, IF
//    two or more participants had the same highest ranking in the experiment (the initial variable to determine the variable bestUser).  
//
//    The String variables question and answer are used to output the question to the participant and its respective answer choices.  The name1, name2, etc... and the 
//    respective gradeRun and rankRun variables are used during the final output of the experiment results to store all of the participant data.  
//    ********************************************************************************************************************************************************************* 

   Scanner in = new Scanner(System.in);
   Random rand = new Random();
   final int NUM_USERS = 5, NUM_QUESTIONS = 4;
   int numCorrect = 0, correctAnswer = 0, questionNum = 0, maxQuestion = 0, numCorrectRun1 = 0, numCorrectRun2 = 0, numCorrectRun3 = 0, 
   numCorrectRun4 = 0, numCorrectRun5 = 0, maxQuestionRun1 = 0,  maxQuestionRun2 = 0, maxQuestionRun3 = 0, maxQuestionRun4 = 0, maxQuestionRun5 = 0, 
   rankInt1 = 0, rankInt2 = 0, rankInt3 = 0, rankInt4 = 0, rankInt5 = 0, maxRankNum = 0;
   long totalTime = 0, reactionTime = 0, max = 0;
   double averageTime = 0, maxTime = 0, avgTimeRun1 = 0, avgTimeRun2 = 0, avgTimeRun3 = 0, avgTimeRun4 = 0, avgTimeRun5 = 0, maxTimeRun1 = 0, 
   maxTimeRun2 = 0, maxTimeRun3 = 0, maxTimeRun4 = 0, maxTimeRun5 = 0, minAvgTime = 100;
   String name = "", question ="", answer="",  grade ="", rank = "", firstName = "", lastName = "", name1 = "", name2 = "",
   name3 = "", name4 = "", name5 = "", gradeRun1 = "", gradeRun2 = "", gradeRun3 = "", gradeRun4 = "", gradeRun5 = "", rankRun1 = "", 
   rankRun2 = "", rankRun3 = "", rankRun4 = "", rankRun5="", bestUser = "",
   
//   *********************************************************************************************************************************************************************
//                                                                             1960s era questions 
//   ********************************************************************************************************************************************************************* 
   
   question1 = "Which song by The Beatles was #1 for 7 weeks in the Winter of 1964?", 
   question2 = "Which group's song \"Light My Fire\" was #1 for 3 weeks in the Summer of 1967?",   
   question3 = "Which song by Marvin Gaye opened the year 1969 as #1 on the charts?",
   question4 = "Which group's song \"Yesterday\" was #1 for 4 weeks in Fall of 1965?", 
   
//   *********************************************************************************************************************************************************************
//                                                                             1970s era questions 
//   *********************************************************************************************************************************************************************   
   question5 = "Which song by Bill Withers was #1 for 3 weeks in July of 1972?",
   question6 = "Which Eagles song was #1 the week of May 7, 1977?",
   question7 = "Simon & Garfunkel's \"Bridge Over Troubled Water\" was #1 for 6 weeks beginning in February of which year?",
   question8 = "Which group's song \"December, 1963 (Oh, What a Night)\" was #1 for 3 weeks in March of 1976?",
   
//   *********************************************************************************************************************************************************************
//                                                                             1980s era questions 
//   ********************************************************************************************************************************************************************* 
   question9 = "Which song by Wham! was #1 for 3 weeks in February of 1985?",
   question10 = "Bon Jovi's \"Livin' On A Prayer\" was #1 for 4 weeks in which year?", 
   question11 = "Survivor's \"Eye of the Tiger\" topped the charts for 6 weeks in the summer of which year?",
   question12 = "Which group's song \"Africa\" was #1 the week of February 5, 1983?",
   
//   *********************************************************************************************************************************************************************
//                                                                             1990s era questions 
//   *********************************************************************************************************************************************************************   
   question13 = "Los Del Rio's \"Macarena\" was #1 for 14 weeks beginning in August of which year?",
   question14 = "Which singer's song \"I Will Always Love You\" was #1 for 14 weeks beginning in November of 1992?",
   question15 = "Which song by Santana was #1 for the final 10 weeks of 1999?",
   question16 = "Which singer's song \"Baby Baby\" was #1 for 2 weeks in the Spring of 1991?",  
  
//   *********************************************************************************************************************************************************************
//                                                           1960s era answer choices (each corresponds with its respective question number) 
//   *********************************************************************************************************************************************************************       
   answer1 = "1)Come Together\t2)Hard Day's Night\t3)I Feel Fine\t4)I Want To Hold Your Hand", 
   answer2 = "1)Sly & The Family Stone\t2)The Beach Boys\t3)The Doors\t4)The Temptations",
   answer3 = "1)What's Going On\t2)I Heard It Through The Grapevine\t3)Mercy Mercy Me\t4)Ain't No Mountain High Enough",
   answer4 = "1)The Rolling Stones\t2)The Supremes\t3)Sonny & Cher\t4)The Beatles",

//   *********************************************************************************************************************************************************************
//                                                           1970s era answer choices (each corresponds with its respective question number) 
//   *********************************************************************************************************************************************************************    
   answer5 = "1)Ain't No Sunshine\t2)Lean on Me\t3)Lovely Day\t4)Just The Two Of Us",  
   answer6 = "1)Hotel California\t2)Best of My Love\t3)Lyin' Eyes\t4)I Can't Tell You Why",
   answer7 = "1)1970\t2)1973\t3)1978\t4)1972",
   answer8 = "1)The Miracles\t2)The 4 Seasons\t3)Wings\t4)Bee Gees",
   
//   *********************************************************************************************************************************************************************
//                                                           1980s era answer choices (each corresponds with its respective question number) 
//   *********************************************************************************************************************************************************************       
   answer9 = "1)Everything She Wants\t2)Wake Me Up Before You Go\t3)Careless Whisper\t4)I'm Your Man",
   answer10 = "1)1987\t2)1981\t3)1985\t4)1980",
   answer11 = "1)1988\t2)1982\t3)1985\t4)1980", 
   answer12 = "1)The Police\t2)Culture Club\t3)Foreigner\t4)Toto",
   
//   *********************************************************************************************************************************************************************
//                                                           1990s era answer choices (each corresponds with its respective question number) 
//   *********************************************************************************************************************************************************************       
   answer13 = "1)1998\t2)1994\t3)1996\t4)1999",
   answer14 = "1)Peabo Bryson\t2)Whitney Houston\t3)Janet Jackson\t4)Mariah Carey",
   answer15 = "1)Maria Maria\t2)Evil Ways\t3)Smooth\t4)The Game of Love",
   answer16 = "1)Amy Grant\t2)Madonna\t3)Michael Jackson\t4)Vanessa Williams"; 

//    Prints welcome message
   System.out.println("Welcome to #1 song trivia, where we quiz you on your knowledge of #1 hits from the 1960s - 1990s on the Billboard Hot 100 singles chart.");
   System.out.println("Your number of correct answers and response time is tracked in this assessment to prove your worth as a music aficionado!");
   System.out.println("");
   
//    Outer loop begins, which runs 5 times for a total of 5 experiment participants (userCount is compared against constant of 5 NUM_USERS)
   for (int userCount = 1; userCount <=NUM_USERS; userCount++)
      {
   
         System.out.print("Please enter your first name: ");  //input first name 
            firstName = in.nextLine();
    
         System.out.print("Please enter your last name: "); //input last name
            lastName = in.nextLine();
    
         name = firstName + " " + lastName;     //String name concatenates first and last name
    
    
         System.out.println("");
         System.out.println("Hello " + firstName + "!");
         System.out.println("");
         System.out.println("Make your answers as fast as you can as your response time is part of the assessment!"
         + "\nChoose the answer you believe is correct by typing its corresponding number and hitting enter." 
         + "\n\nHit <ENTER> when ready for the 4 questions.");
            in.nextLine(); // wait for user to hit <ENTER> to begin the 4 question sequence


//   *********************************************************************************************************************************************************************
//                                                           4 question experiment begins
//   *********************************************************************************************************************************************************************   
         for (int questionCount = 1; questionCount <=NUM_QUESTIONS; questionCount++)  //A loop is set up to run 4 times for each of the 4 decades of music
           {
               if (questionCount == 1)   //On 1st run, the subject is asked a 1960s question
               {
                  System.out.println("");
                  System.out.println("***************************************");
                  System.out.println("\tQuestion #1:  The 1960s");
                  System.out.println("");
      
                  questionNum = rand.nextInt(4) + 1;    //randomly generated integer determines which of the 4 available 1960s questions is to be assigned     
         
                  switch (questionNum)    //The switch statement declares a question and answer sequence for each case and what is deemed a correct answer from the user
                      {
                         case 1:
                           question = question1;
                           answer = answer1;
                           correctAnswer = 4;
                           break;
                        case 2:
                           question = question2; 
                           answer = answer2;
                           correctAnswer = 3; 
                           break;
                        case 3:
                           question = question3;
                           answer = answer3;
                           correctAnswer = 2;
                           break;
                        case 4:
                           question = question4;
                           answer = answer4;
                           correctAnswer = 4;
                           break;                        
                        
                     }
                  }       
                 
               if (questionCount == 2)   //On 2nd run, the subject is asked a 1970s question
                  {
                     System.out.println("");
                     System.out.println("***************************************");
                     System.out.println("\tQuestion #2:  The 1970s");
                     System.out.println("");    
                       
                     questionNum = rand.nextInt(4) + 1;    //randomly generated integer determines which of the 4 available 1970s questions is to be assigned      
         
                     switch (questionNum)
                      {
                         case 1:
                            question = question5;
                            answer = answer5;
                            correctAnswer = 2;
                            break;
                         case 2:
                            question = question6; 
                            answer = answer6;
                            correctAnswer = 1; 
                            break;
                         case 3:
                            question = question7;
                            answer = answer7;
                            correctAnswer = 1;
                            break;
                         case 4:
                            question = question8;
                            answer = answer8;
                            correctAnswer = 2;
                            break;                        
                        
                       }
                    }  
              
               if (questionCount == 3)   //On 3rd run, the subject is asked a 1980s question
                   {
                     System.out.println("");
                     System.out.println("***************************************");
                     System.out.println("\tQuestion #3:  The 1980s");
                     System.out.println("");  
                         
                     questionNum = rand.nextInt(4) + 1;  //randomly generated integer determines which of the 4 available 1980s questions is to be assigned            
         
                     switch (questionNum)
                      {
                         case 1:
                           question = question9;
                           answer = answer9;
                           correctAnswer = 3;
                           break;
                        case 2:
                           question = question10; 
                           answer = answer10;
                           correctAnswer = 1; 
                           break;
                        case 3:
                           question = question11;
                           answer = answer11;
                           correctAnswer = 2;
                           break;
                        case 4:
                           question = question12;
                           answer = answer12;
                           correctAnswer = 4;
                           break;                        
                        
                     }
                  }    
              
               if (questionCount == 4)   //On 4th run, the subject is asked a 1990s question
                  {
                    System.out.println("");
                    System.out.println("***************************************");
                    System.out.println("\tQuestion #4:  The 1990s");
                    System.out.println("");  
                        
                    questionNum = rand.nextInt(4) + 1;      //randomly generated integer determines which of the 4 available 1990s questions is to be assigned       
         
                     switch (questionNum)
                      {
                         case 1:
                           question = question13;
                           answer = answer13;
                           correctAnswer = 3;
                           break;
                         case 2:
                           question = question14; 
                           answer = answer14;
                           correctAnswer = 2; 
                           break;
                         case 3:
                           question = question15;
                           answer = answer15;
                           correctAnswer = 3;
                           break;
                         case 4:
                           question = question16;
                           answer = answer16;
                           correctAnswer = 1;
                           break;                        
                        
                     }
                  }        
    
      
            long startTime = System.currentTimeMillis();   //startTime  = current time
            
//   *********************************************************************************************************************************************************************
//                                                           Question and answer choices are printed to the participant
//   *********************************************************************************************************************************************************************                        
         
            System.out.println(question);       //prints question determined in switch statement
            System.out.println(answer);         //prints answer choices determined in switch statement       
              String response = in.nextLine();      //input user response to current question                                
              int number = Integer.parseInt(response);                        

            long endTime = System.currentTimeMillis();     //endTime = current time 

            int outcome = (number == correctAnswer?  1: 0);        //outcome = 1 or 0 (answer is correct or incorrect)

            reactionTime = endTime - startTime;       //reactionTime = endTime - startTime
 
            totalTime = totalTime + reactionTime;     //determines totalTime for each of the 4 responses
              
            System.out.println(outcome == 1? "Correct!" : "Incorrect.");     //print outcome as "Correct" or "Incorrect"
 
            numCorrect = numCorrect + outcome;        //calculates number of correct answers for the participant
 
            if (reactionTime > max)       //determines which question had the highest response time
            {
               max = reactionTime;
               maxQuestion = questionCount;
 
            }

//    Converts average response time for the participant, converts output from milliseconds to seconds 
            long averageTimeMs = totalTime / 4;
            averageTime = (double) averageTimeMs / 1000.0;
            maxTime = (double) max / 1000.0;
         }

//   *********************************************************************************************************************************************************************
//                                                           Assign a rank and grade for the participant's performance
//   *********************************************************************************************************************************************************************    
      int rankInt = (numCorrect * numCorrect) * 2 - (int) averageTime;     //Formula used to assign a numeric ranking 

//    Switch statement is used to assign a String output of a grade and rank for the subject's performance based on their rankInt
      switch (rankInt)
         {
            case 32: case 31: case 30: case 29: case 28: case 27: case 26:  
            grade = "Excellent";
            rank = "Aretha Franklin";
            break;
      
            case 25: case 24: case 23: case 22: case 21: case 20: case 19: case 18:
            grade = "Great";
            rank = "Michael Jackson";
            break;
      
            case 17: case 16: case 15: case 14: case 13:
            grade = "Good";
            rank = "Stevie Wonder";
            break;
      
            case 12: case 11: case 10: case 9: case 8:  
            grade = "Satisfactory";
            rank = "Bruce Springsteen";
            break;
      
            case 7: case 6: case 5: 
            grade = "Mediocre";
            rank = "Michael Mcdonald";
            break;
      
            case 4: case 3:  
            grade = "Poor";
            rank = "Barbra Streisand";
            break;           
                 
            default:
            grade = "Failure";
            rank = "The Backstreet Boys";    //Who could argue?
            break;
         } 

//    A brief synopsis of the subject's average response time, longest question response, and number of correct answers is printed.
      System.out.println("Your average time was " + averageTime + " seconds"); 
      System.out.println("You took longest on question " + maxQuestion + " taking " + maxTime + " seconds"); 
      System.out.println("You had " + numCorrect + " correct answer" + (numCorrect != 1? "s." : "."));
 
      System.out.println("Thank you " + firstName + " for participating in the experiment.");          //Thanks the user for participating.

//    If we have not reached the end of the assessment for subject 5, the program moves onto the next participant.   
      if (userCount >= 1 && userCount <5)
      {
         System.out.println("");
         System.out.println("Next experiment participant.");
         System.out.println("");
      
      } 

//   *********************************************************************************************************************************************************************
//                                                           Assign a rank and grade for the participant's performance
//   *********************************************************************************************************************************************************************    

//    Compile the name, grade, rank, maximum question number and response time, number of correct answers, and the rank number for each of the participants.
      if (userCount == 1)
      {
         avgTimeRun1 = averageTime;
         name1 = name;
         gradeRun1 = grade;
         rankRun1 = rank;
         maxQuestionRun1 = maxQuestion;
         maxTimeRun1 = maxTime; 
         numCorrectRun1 = numCorrect;
         rankInt1 = rankInt;
      }
   
      if (userCount == 2)
      {
         avgTimeRun2 = averageTime;
         name2 = name;
         gradeRun2 = grade;
         rankRun2 = rank;
         maxQuestionRun2 = maxQuestion;
         maxTimeRun2 = maxTime; 
         numCorrectRun2 = numCorrect;
         rankInt2 = rankInt;
      }
   
      if (userCount == 3)
      {
         avgTimeRun3 = averageTime;
         name3 = name;
         gradeRun3 = grade;
         rankRun3 = rank;
         maxQuestionRun3 = maxQuestion;
         maxTimeRun3 = maxTime; 
         numCorrectRun3 = numCorrect;
         rankInt3 = rankInt;
      }
   
      if (userCount == 4)
      {
         avgTimeRun4 = averageTime;
         name4 = name;
         gradeRun4 = grade;
         rankRun4 = rank;
         maxQuestionRun4 = maxQuestion;
         maxTimeRun4 = maxTime; 
         numCorrectRun4 = numCorrect;
         rankInt4 = rankInt;
      }
   
      if (userCount == 5)
      {
         avgTimeRun5 = averageTime;
         name5 = name;
         gradeRun5 = grade;
         rankRun5 = rank;
         maxQuestionRun5 = maxQuestion;
         maxTimeRun5 = maxTime; 
         numCorrectRun5 = numCorrect;
         rankInt5 = rankInt;
      }
      
//    Reset the rolling counters for each experiment participant. 
      maxQuestion = 1;
      maxTime = 0;
      numCorrect = 0;
      averageTime = 0;
      totalTime = 0;
      reactionTime = 0;
      max = 0; 
   }

//    If statements used to determine the participant who had the best performance.  The numeric ranking is used as the first comparative metric
//    and if there are more than one participants with the same ranking, the lowest average response time is used as the tiebreaker.  
   if (avgTimeRun1 < minAvgTime && rankInt1 >= maxRankNum)
   {
      bestUser = name1;
      minAvgTime = avgTimeRun1;
      maxRankNum = rankInt1;
   }
   
   if (rankInt2 == maxRankNum)
      if (avgTimeRun2 < minAvgTime)      
         {
           bestUser = name2;
           minAvgTime = avgTimeRun2;
           maxRankNum = rankInt2;
         }
         
    if (rankInt2 > maxRankNum)
         {
           bestUser = name2;
           minAvgTime = avgTimeRun2;
           maxRankNum = rankInt2;
         }
      
   if (rankInt3 == maxRankNum)
      if (avgTimeRun3 < minAvgTime)      
         {
           bestUser = name3;
           minAvgTime = avgTimeRun3;
           maxRankNum = rankInt3;
         }
         
     if (rankInt3 > maxRankNum)
         {
           bestUser = name3;
           minAvgTime = avgTimeRun3;
           maxRankNum = rankInt3;
         }   
         
    if (rankInt4 == maxRankNum)
      if (avgTimeRun4 < minAvgTime)      
         {
           bestUser = name4;
           minAvgTime = avgTimeRun4;
           maxRankNum = rankInt4;
         }
         
     if (rankInt4 > maxRankNum)
         {
           bestUser = name4;
           minAvgTime = avgTimeRun4;
           maxRankNum = rankInt4;
         }   
         
    if (rankInt5 == maxRankNum)
      if (avgTimeRun5 < minAvgTime)      
         {
           bestUser = name5;
           minAvgTime = avgTimeRun5;
           maxRankNum = rankInt5;
         }
         
     if (rankInt5 > maxRankNum)
         {
           bestUser = name5;
           minAvgTime = avgTimeRun5;
           maxRankNum = rankInt5;
         }   
         
   System.out.println("\n\n***************************************");
   System.out.println("\tExperiment results");        //Print experiment results for each participant
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                      Participant #1 results
//   *********************************************************************************************************************************************************************       
   System.out.println("\tIndividual #1");
   System.out.println("");
   System.out.println("Name : " + name1);
   System.out.println("Average response time: " + avgTimeRun1 + " seconds");
   System.out.println("Number of correct answers: " + numCorrectRun1 + " correct");
   System.out.println("Rank : " + rankRun1);
   System.out.println("Grade : " + gradeRun1);
   System.out.println("Question with the longest response : " + maxQuestionRun1 + " taking " + maxTimeRun1 + " seconds");
   System.out.println("");
   System.out.println("\n***************************************");
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                      Participant #2 results
//   *********************************************************************************************************************************************************************     
   System.out.println("\tIndividual #2");
   System.out.println("");
   System.out.println("Name : " + name2);
   System.out.println("Average response time: " + avgTimeRun2 + " seconds");
   System.out.println("Number of correct answers: " + numCorrectRun2 + " correct");
   System.out.println("Rank : " + rankRun2);
   System.out.println("Grade : " + gradeRun2);
   System.out.println("Question with the longest response : " + maxQuestionRun2 + " taking " + maxTimeRun2 + " seconds");
   System.out.println("");
   System.out.println("\n***************************************");
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                      Participant #3 results
//   *********************************************************************************************************************************************************************     
   System.out.println("\tIndividual #3");
   System.out.println("");
   System.out.println("Name : " + name3);
   System.out.println("Average response time: " + avgTimeRun3 + " seconds");
   System.out.println("Number of correct answers: " + numCorrectRun3 + " correct");
   System.out.println("Rank : " + rankRun3);
   System.out.println("Grade : " + gradeRun3);
   System.out.println("Question with the longest response : " + maxQuestionRun3 + " taking " + maxTimeRun3 + " seconds");
   System.out.println("");
   System.out.println("\n***************************************");
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                      Participant #4 results
//   *********************************************************************************************************************************************************************     
   System.out.println("\tIndividual #4");
   System.out.println("");
   System.out.println("Name : " + name4);
   System.out.println("Average response time: " + avgTimeRun4 + " seconds");
   System.out.println("Number of correct answers: " + numCorrectRun4 + " correct");
   System.out.println("Rank : " + rankRun4);
   System.out.println("Grade : " + gradeRun4);
   System.out.println("Question with the longest response : " + maxQuestionRun4 + " taking " + maxTimeRun4 + " seconds");
   System.out.println("");
   System.out.println("\n***************************************");
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                      Participant #5 results
//   *********************************************************************************************************************************************************************     
   System.out.println("\tIndividual #5");
   System.out.println("");
   System.out.println("Name : " + name5);
   System.out.println("Average response time: " + avgTimeRun5 + " seconds");
   System.out.println("Number of correct answers: " + numCorrectRun5 + " correct");
   System.out.println("Rank : " + rankRun5);
   System.out.println("Grade : " + gradeRun5);
   System.out.println("Question with the longest response : " + maxQuestionRun5 + " taking " + maxTimeRun5 + " seconds");
   System.out.println("");

//   *********************************************************************************************************************************************************************
//                                                                 Hearty praise is lauded to the best performer
//   *********************************************************************************************************************************************************************     
   System.out.println("Congrats to " + bestUser + " who had the highest ranking in the assessment!");

  }
}