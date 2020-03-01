//***************************************************************************************************************************
//  Art.java       Author: Nick Langan 
//  Created:  November 7, 2018                   
//  Modified: November 11-13, 2018
//
//  Using the features of the JavaFX package, this program creates a map of the Villanova campus
//  with appropriate labels.  Buildings and roads were drawn with lines and ellipses.  Sections on campus
//  were placed into appropriate groups.  Text was used to label the buildings, roads, and create a map legend.
//  The ImageView component was used to inset images displaying people, road symbols, and the logos for SEPTA and Villanova.
//****************************************************************************************************************************

import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  
import java.util.*;

public class Art extends Application
{
    //--------------------------------------------------------------------
    // Villanova map is introduced.  File not found exception occurs if file used in ImageView does not exist.
    //--------------------------------------------------------------------
    public void start(Stage primaryStage)  throws FileNotFoundException
    {                          
        Random rand = new Random();
        
    //--------------------------------------------------------------------
    // Defined colors for the map are introduced.
    //--------------------------------------------------------------------
        Color railroad = Color.rgb(222, 184, 135), lightGreen = Color.rgb(51, 255, 153), darkBlue = Color.rgb(0, 0, 204),
        novaBlue = Color.rgb(0, 32, 91), novaLightBlue = Color.rgb(19, 181, 234);
        
        
    //--------------------------------------------------------------------
    // Line and label for Lancaster Avenue is created.  
    //--------------------------------------------------------------------            
        Line lancasterAve = new Line (0, 740, 1280, 855);        
        lancasterAve.setStrokeWidth(20); 
        Text lancasterName = new Text(150, 805, "Lancaster Avenue");
        lancasterName.setFill(Color.WHITE);
        lancasterName.setStyle("-fx-font: 20 arial;");
        lancasterName.setRotate(5);
        
    //--------------------------------------------------------------------
    // Line for the Blue Route is created.
    //--------------------------------------------------------------------    
        Line blueRoute = new Line (0, 300, 150, 0);    
        blueRoute.setStrokeWidth(50);   
     
    //--------------------------------------------------------------------
    // Line and label for Spring Mill Road is created.
    //--------------------------------------------------------------------    
        Line springMill = new Line (0, 800, 600, 0);
        springMill.setStrokeWidth(10);
        Text springMillName = new Text(200, 505, "Spring Mill Road");
        springMillName.setFill(Color.WHITE);
        springMillName.setStyle("-fx-font: 16 arial;");
        springMillName.setRotate(-50);
      
    //--------------------------------------------------------------------
    // Line and label for Ithan Avenue is created.  
    //--------------------------------------------------------------------    
        Line ithan = new Line (700, 1024, 1280, 280);
        ithan.setStrokeWidth(10);    
        Text ithanName = new Text(1010, 545, "Ithan Avenue");
        ithanName.setFill(Color.WHITE);
        ithanName.setStyle("-fx-font: 16 arial;");
        ithanName.setRotate(-50);
    
    //--------------------------------------------------------------------
    // Line and label for County Line Road is created.  
    //--------------------------------------------------------------------      
        Line countyLine = new Line (300, 0, 1280, 490);
        countyLine.setStrokeWidth(10);
        Text countyLineName = new Text(525, 180, "County Line Road");
        countyLineName.setFill(Color.WHITE);
        countyLineName.setStyle("-fx-font: 16 arial;");
        countyLineName.setRotate(28);
     
    //--------------------------------------------------------------------
    // Line and label for SEPTA Paoli-Thorndale line is created.  
    //--------------------------------------------------------------------   
        Line paoliThorndale = new Line (0, 240, 1280, 450);
        paoliThorndale.setStrokeWidth(8);
        paoliThorndale.setStroke(railroad);
        Text paoliThorndaleName = new Text(540, 380, "Paoli-Thorndale Line");
        paoliThorndaleName.setFill(Color.WHITE);
        paoliThorndaleName.setStyle("-fx-font: 20 arial;");
        paoliThorndaleName.setRotate(8);
    
    //--------------------------------------------------------------------
    // A group containing the roads and their labels is created.   
    //--------------------------------------------------------------------    
        Group roads = new Group(countyLine, countyLineName, ithan, ithanName, paoliThorndale, paoliThorndaleName,
        lancasterAve, lancasterName, blueRoute, springMill, springMillName);
 
    //--------------------------------------------------------------------
    // An ImageView representing the St. Thomas of Villanova Church is produced.
    //--------------------------------------------------------------------     
        Image churchImg = new Image(new FileInputStream("villanovaChurch.png")); //Villanova church image filename
        ImageView villanovaChurch = new ImageView(churchImg);
        villanovaChurch.setX(440);
        villanovaChurch.setY(610);       
        
    //--------------------------------------------------------------------
    // A group containing the Villanova church image is created. 
    //--------------------------------------------------------------------              
        Group church = new Group(villanovaChurch);
        
    //--------------------------------------------------------------------
    // Rectangle represents CEER (label #12)
    //--------------------------------------------------------------------      
        Rectangle ceer = new Rectangle (230, 630, 60, 30);
        ceer.setRotate(10);
        ceer.setFill(lightGreen);
        Text ceerName = new Text(247, 655, "12");        
        ceerName.setStyle("-fx-font: 22 arial;");
        ceerName.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangle represents Chemical Engineering Building (label #10)
    //--------------------------------------------------------------------     
        Rectangle chemEngineering = new Rectangle (300, 510, 20, 50);
        chemEngineering.setRotate(40);
        chemEngineering.setFill(lightGreen);
        Text chemName = new Text(300, 540, "10");        
        chemName.setStyle("-fx-font: 18 arial;");
        chemName.setRotate(40);
     
    //--------------------------------------------------------------------
    // Rectangle represents John Barry Hall (label #9)
    //--------------------------------------------------------------------    
        Rectangle johnBarry = new Rectangle (350, 440, 30, 55);
        johnBarry.setRotate(40);
        johnBarry.setFill(lightGreen);
        Text johnBarryName = new Text(360, 470, "9");        
        johnBarryName.setStyle("-fx-font: 22 arial;");
        johnBarryName.setRotate(40);
     
    //--------------------------------------------------------------------
    // Rectangle represents White Hall (label #11)
    //--------------------------------------------------------------------     
        Rectangle whiteHall = new Rectangle (350, 530, 20, 50);
        whiteHall.setRotate(-40);
        whiteHall.setFill(lightGreen);
        Text whiteName = new Text(350, 560, "11");        
        whiteName.setStyle("-fx-font: 17 arial;");
        whiteName.setRotate(-40);
        
    //--------------------------------------------------------------------
    // Rectangle represents Tollentine Hall (label #13)
    //-------------------------------------------------------------------- 
        Rectangle tollentine = new Rectangle (320, 650, 60, 30);
        tollentine.setRotate(10);
        tollentine.setFill(lightGreen);
        Text tollentineName = new Text(335, 670, "13");        
        tollentineName.setStyle("-fx-font: 22 arial;");
        tollentineName.setRotate(10);
   
    //--------------------------------------------------------------------
    // Rectangle represents the Villanova Monastery (label #14)
    //--------------------------------------------------------------------      
        Rectangle monastery = new Rectangle (400, 650, 30, 30);
        monastery.setRotate(10);
        monastery.setFill(lightGreen);
        Text monasteryName = new Text(405, 670, "14");        
        monasteryName.setStyle("-fx-font: 18 arial;");
        monasteryName.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangle represents Austin Hall (label #15)
    //--------------------------------------------------------------------     
        Rectangle austin = new Rectangle (520, 680, 50, 20);
        austin.setRotate(10);
        austin.setFill(lightGreen);
        Text austinName = new Text(535, 695, "15");        
        austinName.setStyle("-fx-font: 17 arial;");
        austinName.setRotate(10);
    
    //--------------------------------------------------------------------
    // Group section1 is created
    //--------------------------------------------------------------------     
        Group section1 = new Group (church, ceer, ceerName, tollentine, tollentineName, monastery, monasteryName, austin, austinName, chemEngineering, 
        chemName, johnBarry, johnBarryName, whiteHall, whiteName);
   
    //--------------------------------------------------------------------
    // Rectangle represents Mendel Science Center (label #3)
    //--------------------------------------------------------------------      
        Rectangle mendel = new Rectangle (400, 410, 140, 45);
        mendel.setFill(darkBlue);
        Text mendelName = new Text(465, 440, "3");        
        mendelName.setStyle("-fx-font: 22 arial;");
        mendelName.setFill(Color.WHITE);
    
    //--------------------------------------------------------------------
    // Rectangle represents St. Augustine Center (label #4)
    //--------------------------------------------------------------------             
        Rectangle stAugustine = new Rectangle (600, 420, 70, 45);
        stAugustine.setFill(darkBlue);
        Text stAugName = new Text(630, 450, "4");        
        stAugName.setStyle("-fx-font: 22 arial;");
        stAugName.setFill(Color.WHITE);
    
    //--------------------------------------------------------------------
    // Rectangle represents Falvey Memorial Library (label #5)
    //--------------------------------------------------------------------             
        Rectangle falvey = new Rectangle (470, 480, 70, 55);
        falvey.setFill(darkBlue);
        Text falveyName = new Text(500, 515, "5");        
        falveyName.setStyle("-fx-font: 22 arial;");
        falveyName.setFill(Color.WHITE);
   
    //--------------------------------------------------------------------
    // Rectangle represents Alumni Hall (label #6)
    //--------------------------------------------------------------------              
        Rectangle alumniHall = new Rectangle (470, 570, 50, 20);
        alumniHall.setFill(darkBlue);
        Text alumniName = new Text(490, 585, "6");        
        alumniName.setStyle("-fx-font: 17 arial;");
        alumniName.setFill(Color.WHITE);
     
    //--------------------------------------------------------------------
    // Rectangle represents Corr Hall (label #7)
    //--------------------------------------------------------------------             
        Rectangle corrHall = new Rectangle (580, 540, 50, 20);
        corrHall.setFill(darkBlue);
        Text corrName = new Text(600, 555, "7");        
        corrName.setStyle("-fx-font: 17 arial;");
        corrName.setFill(Color.WHITE);
     
    //--------------------------------------------------------------------
    // Rectangle represents Kennedy Hall (label #8)
    //--------------------------------------------------------------------            
        Rectangle kennedyHall = new Rectangle (650, 520, 65, 25);
        kennedyHall.setFill(darkBlue);
        Text kennedyName = new Text(677, 538, "8");        
        kennedyName.setStyle("-fx-font: 18 arial;");
        kennedyName.setFill(Color.WHITE);
       
    //--------------------------------------------------------------------
    // Group section2 is created, and each element is rotated 10 degrees.
    //--------------------------------------------------------------------           
        Group section2 = new Group (mendel, mendelName, stAugustine, stAugName, falvey, falveyName,
        alumniHall, alumniName, corrHall, corrName, kennedyHall, kennedyName);
        section2.setRotate(10);
     
    //--------------------------------------------------------------------
    // Rectangle represents Dougherty Hall (label #19)
    //--------------------------------------------------------------------      
        Rectangle dougherty = new Rectangle (600, 680, 50, 35);
        dougherty.setRotate(10);
        dougherty.setFill(Color.ORANGE);
        Text doughertyName = new Text(615, 705, "19");        
        doughertyName.setStyle("-fx-font: 18 arial;");
        doughertyName.setRotate(10);
     
    //--------------------------------------------------------------------
    // Rectangle represents Bartley Hall (label #21)
    //--------------------------------------------------------------------            
        Rectangle bartley = new Rectangle (850, 580, 70, 160);
        bartley.setRotate(40);
        bartley.setFill(Color.ORANGE);
        Text bartleyName = new Text(880, 660, "21");        
        bartleyName.setStyle("-fx-font: 22 arial;");
        bartleyName.setRotate(40);
    
    //--------------------------------------------------------------------
    // Rectangle represents Vasey Hall (label #20)
    //--------------------------------------------------------------------      
        Rectangle vasey = new Rectangle (670, 680, 50, 20);
        vasey.setRotate(10);
        vasey.setFill(Color.ORANGE);
        Text vaseyName = new Text(685, 695, "20");        
        vaseyName.setStyle("-fx-font: 17 arial;");
        vaseyName.setRotate(10);
   
    //--------------------------------------------------------------------
    // Rectangle represents Connelly Center (label #18)
    //--------------------------------------------------------------------       
        Rectangle connelly = new Rectangle (680, 590, 80, 30);
        connelly.setRotate(10);
        connelly.setFill(Color.ORANGE);
        Text connellyName = new Text(705, 612, "18");        
        connellyName.setStyle("-fx-font: 22 arial;");
        connellyName.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangles represents Driscoll Hall (label #16)
    //--------------------------------------------------------------------      
        Rectangle driscoll = new Rectangle (770, 450, 80, 30);
        driscoll.setRotate(10);
        driscoll.setFill(Color.ORANGE);
        Text driscollName = new Text(820, 480, "16");        
        driscollName.setStyle("-fx-font: 22 arial;");
        driscollName.setRotate(10);
        
        Rectangle driscoll2 = new Rectangle (815, 480, 30, 40);
        driscoll2.setRotate(10);
        driscoll2.setFill(Color.ORANGE);
   
    //--------------------------------------------------------------------
    // Rectangle represents Health Services (label #17)
    //--------------------------------------------------------------------       
        Rectangle healthServices = new Rectangle (950, 520, 50, 20);
        healthServices.setRotate(40);
        healthServices.setFill(Color.ORANGE);
        Text healthName = new Text(964, 536, "17");        
        healthName.setStyle("-fx-font: 17 arial;");
        healthName.setRotate(40);
    
    //--------------------------------------------------------------------
    // Group section3 is created.
    //--------------------------------------------------------------------      
        Group section3 = new Group (bartley, bartleyName, dougherty, doughertyName, vasey, vaseyName,
        connelly, connellyName, driscoll, driscoll2, driscollName, healthServices, healthName);
     
    //--------------------------------------------------------------------
    // Ellipse representing Villanova Stadium (label #23)
    //--------------------------------------------------------------------   
        Ellipse stadium = new Ellipse (1080, 750, 120, 50);
        stadium.setRotate(10);
        stadium.setFill(Color.MAROON);
        Text stadiumName = new Text(1070, 760, "23");        
        stadiumName.setStyle("-fx-font: 22 arial;");
        stadiumName.setFill(Color.WHITE);
        stadiumName.setRotate(10);
    
    //--------------------------------------------------------------------
    // Ellipse representing Finneran Pavillion (label #22)
    //--------------------------------------------------------------------     
        Ellipse finneran = new Ellipse (1140, 570, 30, 80);
        finneran.setRotate(40);
        finneran.setFill(Color.MAROON);
        Text finnName = new Text(1135, 570, "22");
        finnName.setStyle("-fx-font: 22 arial;");
        finnName.setFill(Color.WHITE);
        finnName.setRotate(40);
    
    //--------------------------------------------------------------------
    // Group section4 is created.
    //--------------------------------------------------------------------     
        Group section4 = new Group (stadium, stadiumName, finneran, finnName);
    
    //--------------------------------------------------------------------
    // Rectangle representing Garey Hall (label #1)
    //--------------------------------------------------------------------     
        Rectangle garey = new Rectangle (480, 180, 70, 55);
        garey.setRotate(30);
        garey.setFill(Color.PURPLE);
        Text gareyName = new Text(510, 213, "1");        
        gareyName.setStyle("-fx-font: 22 arial;");
        gareyName.setRotate(30);
    
    //--------------------------------------------------------------------
    // Rectangle representing Villanova Law School (label #2)
    //--------------------------------------------------------------------     
        Rectangle lawSchool = new Rectangle (590, 240, 80, 30);
        lawSchool.setRotate(30);
        lawSchool.setFill(Color.PURPLE);
        Text lawName = new Text(625, 262, "2");        
        lawName.setStyle("-fx-font: 22 arial;");
        lawName.setRotate(30);
     
    //--------------------------------------------------------------------
    // Rectangle representing West Campus
    //--------------------------------------------------------------------    
        Rectangle westCampus = new Rectangle (360, 80, 70, 170);
        westCampus.setRotate(35);
        westCampus.setFill(Color.PURPLE);
        Text westName = new Text(330, 180, "West Campus");        
        westName.setStyle("-fx-font: 22 arial;");
        westName.setRotate(-55);
    
    //--------------------------------------------------------------------
    // Group section5 is created.
    //--------------------------------------------------------------------               
        Group section5 = new Group (garey, gareyName, lawSchool, lawName, westCampus, westName);
    
    //--------------------------------------------------------------------
    // Rectangle representing I-1 parking lot
    //--------------------------------------------------------------------       
        Rectangle i1Lot = new Rectangle (870, 840, 50, 50);
        i1Lot.setRotate(5);
        i1Lot.setFill(Color.GRAY);
        Text i1Name = new Text(880, 870, "I-1");        
        i1Name.setStyle("-fx-font: 20 arial;");
        i1Name.setRotate(5);
    
    //--------------------------------------------------------------------
    // Rectangle representing S-2 parking lot
    //--------------------------------------------------------------------      
        Rectangle s2Lot = new Rectangle (320, 585, 50, 50);
        s2Lot.setRotate(10);
        s2Lot.setFill(Color.GRAY);
        Text s2Name = new Text(330, 615, "S-2");        
        s2Name.setStyle("-fx-font: 20 arial;");
        s2Name.setRotate(10);
   
    //--------------------------------------------------------------------
    // Rectangle representing S-3 parking lot
    //--------------------------------------------------------------------       
        Rectangle s3Lot = new Rectangle (380, 320, 50, 50);
        s3Lot.setRotate(10);
        s3Lot.setFill(Color.GRAY);
        Text s3Name = new Text(390, 350, "S-3");        
        s3Name.setStyle("-fx-font: 20 arial;");
        s3Name.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangle representing S-4 parking lot
    //--------------------------------------------------------------------      
        Rectangle s4Lot = new Rectangle (440, 230, 50, 50);
        s4Lot.setRotate(30);
        s4Lot.setFill(Color.GRAY);
        Text s4Name = new Text(450, 260, "S-4");        
        s4Name.setStyle("-fx-font: 20 arial;");
        s4Name.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangle representing S-5 parking lot
    //--------------------------------------------------------------------      
        Rectangle s5Lot = new Rectangle (570, 270, 50, 50);
        s5Lot.setRotate(30);
        s5Lot.setFill(Color.GRAY); 
        Text s5Name = new Text(580, 300, "S-5");        
        s5Name.setStyle("-fx-font: 20 arial;");
        s5Name.setRotate(10);    
     
    //--------------------------------------------------------------------
    // Rectangle representing M-1 parking lot
    //--------------------------------------------------------------------     
        Rectangle m1Lot = new Rectangle (990, 440, 50, 50);
        m1Lot.setRotate(40);
        m1Lot.setFill(Color.GRAY);
        Text m1Name = new Text(1000, 470, "M-1");        
        m1Name.setStyle("-fx-font: 20 arial;");
        m1Name.setRotate(40);
    
    //--------------------------------------------------------------------
    // Rectangle representing M-2 parking lot
    //--------------------------------------------------------------------         
        Rectangle m2Lot = new Rectangle (700, 430, 50, 50);
        m2Lot.setRotate(10);
        m2Lot.setFill(Color.GRAY);
        Text m2Name = new Text(710, 460, "M-2");        
        m2Name.setStyle("-fx-font: 20 arial;");
        m2Name.setRotate(10);
    
    //--------------------------------------------------------------------
    // Rectangle representing M-3 parking lot
    //--------------------------------------------------------------------      
        Rectangle m3Lot = new Rectangle (700, 485, 30, 30);
        m3Lot.setRotate(10);
        m3Lot.setFill(Color.GRAY); 
        Text m3Name = new Text(705, 500, "M-3");        
        m3Name.setStyle("-fx-font: 14 arial;");
        m3Name.setRotate(10);  
        
    //--------------------------------------------------------------------
    // Group section6 is created, containing all elements related to the parking lots.
    //--------------------------------------------------------------------   
        
        Group section6 = new Group (i1Lot, i1Name, s2Lot, s2Name, s3Lot, s3Name, s4Lot, s4Name, s5Lot, s5Name, m1Lot, m1Name, 
        m2Lot, m2Name, m3Lot, m3Name);     
    
    //--------------------------------------------------------------------
    // Image printed via ImageView of the U.S. Route 30 shield
    //--------------------------------------------------------------------      
        Image route30Img = new Image(new FileInputStream("US_30.png")); //U.S. 30 shield image filename
        ImageView route30 = new ImageView(route30Img);
        route30.setX(500);
        route30.setY(730);   
     
    //--------------------------------------------------------------------
    // Image printed via ImageView of the I-476 shield
    //--------------------------------------------------------------------     
        Image i476Img = new Image(new FileInputStream("i476.png")); //I-476 shield image filename
        ImageView i476 = new ImageView(i476Img);
        i476.setX(30);
        i476.setY(50);
    
    //--------------------------------------------------------------------
    // Image printed via ImageView of the SEPTA logo
    //--------------------------------------------------------------------      
        Image septaImg = new Image (new FileInputStream("septa.png")); //SEPTA image filename
        ImageView septa = new ImageView(septaImg);
        septa.setX(70);
        septa.setY(270);
        
    //--------------------------------------------------------------------
    // roadSymbols group is created.
    //--------------------------------------------------------------------         
        Group roadSymbols = new Group(route30, i476, septa);   
    
    //--------------------------------------------------------------------
    // Group students is created.  20 student figures are added to an ImageView array via a loop.
    // The student figures are plotted at a random location confined to Villanova's main campus on the map.
    //--------------------------------------------------------------------     
        Group students = new Group();
        Image student = new Image(new FileInputStream("student.png")); //student image filename
        ImageView[] imageViewArray = new ImageView[20];
        
        for (int i = 1; i<20; i++)
        {
            imageViewArray[i] = new ImageView(student);
            int randX = rand.nextInt(600)+380;
            int randY = rand.nextInt(300)+350;
            imageViewArray[i].setX(randX); 
            imageViewArray[i].setY(randY);
            students.getChildren() .add(imageViewArray[i]);
         }
   
    //--------------------------------------------------------------------
    // Rectangle containing legend for map is printed.
    //--------------------------------------------------------------------     
               
        Rectangle legendRect = new Rectangle (800, 10, 470, 350);
        legendRect.setStroke(novaBlue);
        legendRect.setStrokeWidth(2);
        legendRect.setFill(Color.GREEN);  
        Image novaLogo = new Image(new FileInputStream("nova.png")); //Villanova logo filename
        ImageView novaLegend = new ImageView(novaLogo);
        novaLegend.setX(810);
        novaLegend.setY(30);

    //--------------------------------------------------------------------
    // Legend header is printed.  Identifiers for all campus locations are listed in numerical order.
    //--------------------------------------------------------------------     
        Text legendHeader = new Text(910, 60, "Villanova Campus Map");
        legendHeader.setFill(novaBlue);
        legendHeader.setStyle("-fx-font: 20 arial;" + "-fx-font-weight: bold;");
        
        Text legend1 = new Text(810, 110, "1 - Garey Hall");
        legend1.setFill(novaBlue);
        legend1.setStyle("-fx-font: 16 arial;");
        
        Text legend2 = new Text(810, 130, "2 - Law School Building");
        legend2.setFill(novaBlue);
        legend2.setStyle("-fx-font: 16 arial;");
        
        Text legend3 = new Text(810, 150, "3 - Mendel Science Center");
        legend3.setFill(novaBlue);
        legend3.setStyle("-fx-font: 16 arial;");
        
        Text legend4 = new Text(810, 170, "4 - St. Augustine Center");
        legend4.setFill(novaBlue);
        legend4.setStyle("-fx-font: 16 arial;");
        
        Text legend5 = new Text(810, 190, "5 - Falvey Memorial Library ");
        legend5.setFill(novaBlue);
        legend5.setStyle("-fx-font: 16 arial;");
        
        Text legend6 = new Text(810, 210, "6 - Alumni Hall");
        legend6.setFill(novaBlue);
        legend6.setStyle("-fx-font: 16 arial;");
        
        Text legend7 = new Text(810, 230, "7 - Corr Hall");
        legend7.setFill(novaBlue);
        legend7.setStyle("-fx-font: 16 arial;");
        
        Text legend8 = new Text(810, 250, "8 - Kennedy Hall");
        legend8.setFill(novaBlue);
        legend8.setStyle("-fx-font: 16 arial;");
        
        Text legend9 = new Text(810, 270, "9 - John Barry Hall");
        legend9.setFill(novaBlue);
        legend9.setStyle("-fx-font: 16 arial;");
        
        Text legend10 = new Text(810, 290, "10 - Chem Engineering Building");
        legend10.setFill(novaBlue);
        legend10.setStyle("-fx-font: 16 arial;");
        
        Text legend11 = new Text(810, 310, "11 - White Hall");
        legend11.setFill(novaBlue);
        legend11.setStyle("-fx-font: 16 arial;");
        
        Text legend12 = new Text(810, 330, "12 - CEER");
        legend12.setFill(novaBlue);
        legend12.setStyle("-fx-font: 16 arial;");
        
        Text legend13 = new Text(810, 350, "13 - Tollentine Hall");
        legend13.setFill(novaBlue);
        legend13.setStyle("-fx-font: 16 arial;");
        
        Text legend14 = new Text(1080, 110, "14 - Monastery");
        legend14.setFill(novaBlue);
        legend14.setStyle("-fx-font: 16 arial;");
        
        Text legend15 = new Text(1080, 130, "15 - Austin Hall");
        legend15.setFill(novaBlue);
        legend15.setStyle("-fx-font: 16 arial;");
        
        Text legend16 = new Text(1080, 150, "16 - Driscoll Hall");
        legend16.setFill(novaBlue);
        legend16.setStyle("-fx-font: 16 arial;");
        
        Text legend17 = new Text(1080, 170, "17 - Health Services");
        legend17.setFill(novaBlue);
        legend17.setStyle("-fx-font: 16 arial;");
        
        Text legend18 = new Text(1080, 190, "18 - Connelly Center");
        legend18.setFill(novaBlue);
        legend18.setStyle("-fx-font: 16 arial;");
        
        Text legend19 = new Text(1080, 210, "19 - Dougherty Hall");
        legend19.setFill(novaBlue);
        legend19.setStyle("-fx-font: 16 arial;");
        
        Text legend20 = new Text(1080, 230, "20 - Vasey Hall");
        legend20.setFill(novaBlue);
        legend20.setStyle("-fx-font: 16 arial;");
        
        Text legend21 = new Text(1080, 250, "21 - Bartley Hall");
        legend21.setFill(novaBlue);
        legend21.setStyle("-fx-font: 16 arial;");
        
        Text legend22 = new Text(1080, 270, "22 - Finneran Pavillion");
        legend22.setFill(novaBlue);
        legend22.setStyle("-fx-font: 16 arial;");
        
        Text legend23 = new Text(1080, 290, "23 - Stadium");
        legend23.setFill(novaBlue);
        legend23.setStyle("-fx-font: 16 arial;");

    //--------------------------------------------------------------------
    // Explanation for parking lot identifiers is displayed.
    //--------------------------------------------------------------------         
        Text legend24 = new Text(1040, 320, "Parking lots:  I-1, M-2, M-3, S-2, S-3,\nS-4, S-5");
        legend24.setFill(novaBlue);
        legend24.setStyle("-fx-font: 12 arial;");
    
    //--------------------------------------------------------------------
    // Group created pertaining to the legend and all of its elements.  
    //--------------------------------------------------------------------     
        Group legend = new Group(legendRect, novaLegend, legendHeader, legend1, legend2, legend3, legend4, legend5, legend6, 
        legend7, legend8, legend9, legend10, legend11, legend12, legend13, legend14, legend15, legend16, legend17, legend18, 
        legend19, legend20, legend21, legend22, legend23, legend24);
     
    //--------------------------------------------------------------------
    // Root group is created containing all previously defined groups. 
    //--------------------------------------------------------------------       
        Group root = new Group(roads, roadSymbols, section1, section2, section3, section4, section5, section6, legend, students);          
     
    //--------------------------------------------------------------------
    // Scene and stage are created with root group, size, title, and green background color. 
    //--------------------------------------------------------------------             
        Scene scene = new Scene(root, 1280, 900, Color.GREEN);        
              
        primaryStage.setTitle("Villanova Campus Map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}