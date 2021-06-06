package chuss.listeners;

import chuss.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UndoListener extends MouseAdapter {
    public CommandManager commandManager;					///< Keeps track of undos and redos

    /**
     * Constructor that saves CommandManager information
     * @param controller	The Controller that has access to CommandManager
     */
    public UndoListener(Controller controller){
        this.commandManager = controller.commandManager;
    }

    /**
     * Undos a move if an undo is available. Beeps otherwise
     */
    public void mouseClicked(MouseEvent event){
        if (commandManager.undoAvailable())
            commandManager.undo();
        else
            java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
