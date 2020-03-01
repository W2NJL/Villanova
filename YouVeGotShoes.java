//***********************************************************************************************************
//  YouVeGotShoes.java       Author: Nick Langan 
//  Created:  October 29, 2018                     
//  Modified:  October 31, 2018
//  Demonstrates the creation and use of multiple Shoe objects.
//  Four shoes are created utilizing two constructors.  
//  Using a jFrame, images are produced for each of the shoes in a pop-up window for the initial definitions.
//  Information for each of the shoes is printed.
//  Utilizing the like, setPrice, getPrice, getName, and setPicture methods, modifications are made to the shoes.  
//  Information for each of the shoes is re-printed, and another jFrame for the shoes appears with images.
//  The sum of the price of the four shoes is calculated as is the average star (rating) for the four shoes.  
//*************************************************************************************************************

import java.text.NumberFormat;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class YouVeGotShoes
{
   public static void main (String[] args)
   {
      Scanner scan = new Scanner(System.in);  
      String starOutput = "";
      
//-----------------------------------------------------------------
// Welcome message is printed.  
// User provides price input for the fourth shoe.
//-----------------------------------------------------------------
            
      double shoe4PriceInput = 0;
      
      System.out.println("Welcome to DSW Shoe Warehouse's inventory system.  Today we have 4 styles on hand!");
      System.out.println("Please enter the price in US dollars for the fourth shoe (i.e. 99.99):");
         shoe4PriceInput = scan.nextDouble();
         
//-----------------------------------------------------------------
//  Creates four shoes, shoes 1-3 utilize first constructor that requires star value (shoe 4 utilizes second constructor, does not define a star rating nor image filename)
//-----------------------------------------------------------------
      
      Shoe shoe1 = new Shoe ("Under Armour", "Charged Escape 2", 50.00, 192007465484L, 4.0, "underarmour.jpeg");          
      Shoe shoe2 = new Shoe ("Adidas", "Energy Cloud 2", 100.00, 191028305052L, 4.0, "adidas.jpeg");
      Shoe shoe3 = new Shoe ("Cole Haan", "Grandexplore", 40.00, 190595718029L, 4.0, "colehaan.jpeg");
      Shoe shoe4 = new Shoe ("Nike", "Run Swift", shoe4PriceInput, 886059636809L);
      
//-----------------------------------------------------------------
//  Output for the four shoe objects is printed with initial definitions.
//-----------------------------------------------------------------
      
      System.out.println ("");
      System.out.println ("Shoe information before modifications:");
      System.out.println (shoe1);
      System.out.println (shoe2);
      System.out.println (shoe3);
      System.out.println (shoe4);
     
//-----------------------------------------------------------------
//  A JFrame is constructed and appears with images for 3 of the 4 shoes and their labels.  (Shoe 4 shows a placeholder image)
//-----------------------------------------------------------------
      
      JFrame frame = new JFrame ("Shoe pictures before modifications");
      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      
      ImageIcon shoeImg1 = new ImageIcon (shoe1.getPicture());
      ImageIcon shoeImg2 = new ImageIcon (shoe2.getPicture());
      ImageIcon shoeImg3 = new ImageIcon (shoe3.getPicture());
      ImageIcon shoeImg4 = new ImageIcon (shoe4.getPicture());
      
      JLabel label1, label2, label3, label4;
      
      label1 = new JLabel (shoe1.getName(), shoeImg1, SwingConstants.CENTER);  // I created a getName method for this purpose because I did not want to violate encapsulation!
      label1.setHorizontalTextPosition (SwingConstants.CENTER);
      label1.setVerticalTextPosition (SwingConstants.BOTTOM);
      label2 = new JLabel (shoe2.getName(), shoeImg2, SwingConstants.CENTER);
      label2.setHorizontalTextPosition (SwingConstants.CENTER);
      label2.setVerticalTextPosition (SwingConstants.BOTTOM);
      label3 = new JLabel (shoe3.getName(), shoeImg3, SwingConstants.CENTER);
      label3.setHorizontalTextPosition (SwingConstants.CENTER);
      label3.setVerticalTextPosition (SwingConstants.BOTTOM);
      label4 = new JLabel (shoe4.getName(), shoeImg4, SwingConstants.CENTER);
      label4.setHorizontalTextPosition (SwingConstants.CENTER);
      label4.setVerticalTextPosition (SwingConstants.BOTTOM);
      
      JPanel panel = new JPanel();
      panel.setBackground (Color.cyan);
      panel.setPreferredSize (new Dimension (800, 400));
      panel.add (label1);
      panel.add (label2);
      panel.add (label3);
      panel.add (label4);
      frame.getContentPane().add(panel);
      frame.pack();
      frame.setVisible(true);
      
//-----------------------------------------------------------------
//  Like and price parameters are modified.
//-----------------------------------------------------------------
      
      shoe4.like();
      
      shoe3.like(1.9);
      
      shoe2.like(-2.0);
      
      shoe1.setPrice(29.99);
      
      double shoe2Price = shoe2.getPrice();
      double shoe3Price = shoe3.getPrice();
      double shoe4Price = shoe4.getPrice(); 
      
      shoe2.setPrice(shoe2Price - (shoe2Price * 0.20));
      shoe3.setPrice(shoe3Price - (shoe3Price * 0.20));
      shoe4.setPrice(shoe4Price - (shoe4Price * 0.20));
      
//-----------------------------------------------------------------
//  A filename is designated for the image for shoe 4.
//-----------------------------------------------------------------
      
      shoe4.setPicture("nike.jpeg");
      
//-----------------------------------------------------------------
//  Output for the four shoe objects is printed with the modifications.
//-----------------------------------------------------------------
      
      System.out.println ("");
      System.out.println ("Shoe information after modifications:");
      System.out.println (shoe1);
      System.out.println (shoe2);
      System.out.println (shoe3);
      System.out.println (shoe4);    
      
//-----------------------------------------------------------------
//  A second jFrame window appears, showing images and labels for all four shoes.
//-----------------------------------------------------------------
      
      JFrame afterFrame = new JFrame ("Shoe pictures after modifications");
      afterFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      
      ImageIcon shoeImgAfter1 = new ImageIcon (shoe1.getPicture());
      ImageIcon shoeImgAfter2 = new ImageIcon (shoe2.getPicture());
      ImageIcon shoeImgAfter3 = new ImageIcon (shoe3.getPicture());
      ImageIcon shoeImgAfter4 = new ImageIcon (shoe4.getPicture());
      
      JLabel afterLabel1, afterLabel2, afterLabel3, afterLabel4;
      
      afterLabel1 = new JLabel (shoe1.getName(), shoeImgAfter1, SwingConstants.CENTER);
      afterLabel1.setHorizontalTextPosition (SwingConstants.CENTER);
      afterLabel1.setVerticalTextPosition (SwingConstants.BOTTOM);
      afterLabel2 = new JLabel (shoe2.getName(), shoeImgAfter2, SwingConstants.CENTER);
      afterLabel2.setHorizontalTextPosition (SwingConstants.CENTER);
      afterLabel2.setVerticalTextPosition (SwingConstants.BOTTOM);
      afterLabel3 = new JLabel (shoe3.getName(), shoeImgAfter3, SwingConstants.CENTER);
      afterLabel3.setHorizontalTextPosition (SwingConstants.CENTER);
      afterLabel3.setVerticalTextPosition (SwingConstants.BOTTOM);
      afterLabel4 = new JLabel (shoe4.getName(), shoeImgAfter4, SwingConstants.CENTER);
      afterLabel4.setHorizontalTextPosition (SwingConstants.CENTER);
      afterLabel4.setVerticalTextPosition (SwingConstants.BOTTOM);
      
      JPanel afterPanel = new JPanel();
      afterPanel.setBackground (Color.cyan);
      afterPanel.setPreferredSize (new Dimension (1024, 400));
      afterPanel.add (afterLabel1);
      afterPanel.add (afterLabel2);
      afterPanel.add (afterLabel3);
      afterPanel.add (afterLabel4);
      afterFrame.getContentPane().add(afterPanel);
      afterFrame.pack();
      afterFrame.setVisible(true);
      
//-----------------------------------------------------------------
//  Sum of price for the four shoes is computed.
//-----------------------------------------------------------------  

      System.out.println ("");
      System.out.println ("Summary");
      System.out.println ("________________________________");     
      NumberFormat fmt = NumberFormat.getCurrencyInstance();
      double sum = shoe1.getPrice() + shoe2.getPrice() + shoe3.getPrice() + shoe4.getPrice();
      System.out.println("Total price of shoes: " + fmt.format(sum));
      
//--------------------------------------------------------------------------------
//  A switch statement is utilized to output the average star rating for the 4 shoes.
//--------------------------------------------------------------------------------
      
      double starSum = shoe1.getStars() + shoe2.getStars() + shoe3.getStars() + shoe4.getStars();
      double starAvg = starSum / 4;
      int starAvgInt = (int) starAvg;
      
       switch (starAvgInt)
         {  
            case 0:  //0 stars
            starOutput = "";
            break;
            
            case 1:  //1 star
            starOutput = "*";
            break;
            
            case 2:  //2 stars
            starOutput = "**";
            break;
            
            case 3:  //3 stars
            starOutput = "***";
            break;
            
            case 4:  //4 stars
            starOutput = "****";
            break;
            
            case 5:  //5 stars
            starOutput = "*****";
            break;         
          }
          
//--------------------------------------------------------------------------------
//  Star average is printed.
//--------------------------------------------------------------------------------
         
      System.out.println("Average number of stars: " + starOutput + " (" + starAvg + ")");
      
    }
}
