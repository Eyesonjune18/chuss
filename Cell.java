package chuss;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Color.*;

public class Cell extends JButton {
    private Color darkColor;
    private Color lightColor;
    private Color selectedColor;
    private Color destColor;
    private Color targetColor;
    private Color battleColor;
    private int row;
    private int col;

    public Cell(ActionListener container) {
        addActionListener(container);
        setSize(40, 40);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorder(new LineBorder(BLACK));
        row = 0;
        col = 0;
        battleColor = PINK;
        darkColor = BLACK;
        lightColor = RED;
        selectedColor = GREEN;
        destColor = YELLOW;
        targetColor = BLUE;
    }

    private void setCellColor(String theme, String cellState) {
        if (theme.equals("night"))
            lightColor = GRAY;

        String cellColor = cellState.substring(cellState.length()-1);
        if (cellColor.equals("~")) setBackground(selectedColor);
        else if (cellColor.equals(".")) setBackground(destColor);
        else if (cellColor.equals("?")) setBackground(targetColor);
        else if (cellColor.equals("x")) setBackground(battleColor);
        else if (row % 2 == col % 2) setBackground(darkColor);
        else setBackground(lightColor);
    }

    public void setCellGraphics(String theme, String cellState) {
        setCellColor(theme, cellState);

        if (cellState.charAt(0) == '-') {
            // eventually text here will get replaced with an image
            setText("");
        } else {
            setText(cellState.substring(1,2));
            setForeground(WHITE);
        }
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        setActionCommand(Integer.toString(row) + Integer.toString(col));
    }
}
