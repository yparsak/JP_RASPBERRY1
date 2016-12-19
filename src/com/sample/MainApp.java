/*                                  REFERENCE
*****************************************************************************************
*
*  WiringPi Pin |    BCM GPIO     | Name   ||  Header || Name   | BCM GPIO | WiringPi Pin
*               |                 | 3.3 v  ||  1 | 2  ||  5v    |          |
*  8            | Rv1:0 - Rv2:2   | SDA    ||  3 | 4  ||  5v    |          |
*  9            | Rv1:1 - Rv2:3   | SCL    ||  5 | 6  ||  0v    |          |
*  7            |       4         | GPIO 7 ||  7 | 8  || TxD    |  14      |  15
*               |                 | 0 v    ||  9 | 10 || RxD    |  15      |  16
*  0            |      17         | GPIO0  || 11 | 12 || GPIO1  |  18      |  1
*  2            | Rv1:21 - Rv2:27 | GPIO2  || 13 | 14 || 0V     |          |
*  3            |      22         | GPIO3  || 15 | 16 || GPIO4  |  23      |  4
*               |                 | 3.3 v  || 17 | 18 || GPIO5  |  24      |  5
*  12           |      10         | MOSI   || 19 | 20 || 0V     |          |
*  13           |       9         | MISO   || 21 | 22 || GPIO6  |  25      |  6
*  14           |      11         | SCLK   || 23 | 24 || CE0    |  8       |  10
*               |                 | 0 v    || 25 | 26 || CE1    |  7       |  11
*
****************************************************************************************
*/

package com.sample;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioUtil;


public class MainApp {

    private static int window_height;
    private static int window_width;
    private static JFrame main ;
    
    private static final int GPIO7 = 7;                       //GPIO7
    private static final int fail  = -1;
	
    private static JTextField textfield;
    
    private static int count = 0;
    
	public static void main(String[] args) {

        // window_height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        // window_width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width		
		window_width = window_height = 400;       


        main = new JFrame();
        main.setTitle("Java Project Sample Raspberry");
        main.setLayout(new FlowLayout());
        
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       

        main.setBounds(0,0,window_width, window_height);
        main.setLocationRelativeTo(null);
        
        textfield = new JTextField();
        textfield.setEditable(false);
        textfield.setBounds(10, 10, 200, 50);
        textfield.setPreferredSize(new Dimension(200,50));
        textfield.setMaximumSize(new Dimension(200,50));
        main.add(textfield);
        HideCursor();
        InitPI();
        main.setVisible(true);
	}
	
	private static void InitPI() {
	   // create and add GPIO listener 
	   GpioInterrupt.addListener(new GpioInterruptListener() {
	   @Override
	   public void pinStateChange(GpioInterruptEvent event) {           	
	      	if(event.getPin() == GPIO7) {
	         	  button_down();
	        	}
	        }
	  });
	        
	  // setup wiring pi
	  if (Gpio.wiringPiSetup() == fail) {
	      System.out.println("Error: GPIO SETUP FAILED");
	      return;
	  }

	  // export all the GPIO pins that we will be using
	  GpioUtil.export(GPIO7, GpioUtil.DIRECTION_IN);

	  // set the edge state on the pins we will be listening for
	  GpioUtil.setEdgeDetection(GPIO7, GpioUtil.EDGE_BOTH);
	                
	  // configure GPIO7 as an INPUT pin; enable it for callback
	  Gpio.pinMode(GPIO7, Gpio.INPUT);
	  Gpio.pullUpDnControl(GPIO7, Gpio.PUD_DOWN);        
	  GpioInterrupt.enablePinStateChangeCallback(GPIO7);		
	}

	private static void button_down() {
		count ++;
		textfield.setText("Button is pushed " + count);
	}

	private static void HideCursor() {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");
		main.getContentPane().setCursor(blankCursor);		
	}
}
