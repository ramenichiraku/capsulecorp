import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javax.swing.*;
import java.awt.*;

public class ArduinoApplication {
  private static String labelButtonOn  = "Remotely switch  ON LED";
  private static String labelButtonOff = "Remotely switch OFF LED";
  private int currentValue = 0;



  private static String labelPrefix = "Number of button clicks: ";

  private int numClicks = 0;

  public Component createComponents() {
    final JButton buttonOn  = new JButton(labelButtonOn);
    buttonOn.setPreferredSize(new Dimension(250, 30));
    buttonOn.setMnemonic(KeyEvent.VK_I);
    buttonOn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

	if (currentValue == 0)
	{
	// Here should be sent the serial command to switch LED on
		currentValue = 1;
		buttonOn.setText(labelButtonOff);
	}
	else
	{
	// Here should be sent the serial command to switch LED off
		currentValue = 0;
		buttonOn.setText(labelButtonOn );
	}

	
      }
    });
//    label.setLabelFor(buttonOn);

//    JButton buttonOff = new JButton("Switch OFF Led!");
    /*
     * An easy way to put space between a top-level container and its
     * contents is to put the contents in a JPanel that has an "empty"
     * border.
     */
    JPanel pane = new JPanel();
    pane.setBorder(BorderFactory.createEmptyBorder(30, //top
        30, //left
        30, //bottom
        30) //right
        );
    pane.setLayout(new GridLayout(0, 1));
    pane.add(buttonOn);
 //   pane.add(buttonOff);
 //   pane.add(label);

    return pane;
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
    }

    //Create the top-level container and add contents to it.
    JFrame frame = new JFrame("ArduinoApplication");
    ArduinoApplication app = new ArduinoApplication();
    Component contents = app.createComponents();
    frame.getContentPane().add(contents, BorderLayout.CENTER);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}

