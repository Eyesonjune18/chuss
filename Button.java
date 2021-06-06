package chuss;

import javax.swing.*;
import java.awt.*;

public class  Button extends JButton {
        public int xPos;	///< x Position of Button
        public int yPos;
        private String id; //Location of clicked square


        public Button(final String id,int xPos, int yPos) {
            this.id = id;

            this.xPos = xPos;
            this.yPos = yPos;

        }
       public String ButtonClicked(String id) {
            return id;

       }


        public Point createPoint(){
            return new Point(xPos, yPos);
        }

    }
