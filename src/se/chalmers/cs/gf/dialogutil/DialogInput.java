package se.chalmers.cs.gf.dialogutil;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  Gets text input from a Swing dialog box.
 */
public class DialogInput extends JDialog implements TextInput {
        
        protected EventListenerList listenerList = new EventListenerList();

        private JTextField inputField;

        /**
         *  Creates and shows the dialog.
         */
        public DialogInput() {
                setTitle("Enter query");

                inputField = new JTextField(30);
                inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
                add(inputField, BorderLayout.CENTER);

                JButton doneButton = new JButton("Done");
                doneButton.addActionListener(new DoneListener());
                add(doneButton, BorderLayout.SOUTH);

                getRootPane().setDefaultButton(doneButton);

                pack();
                setVisible(true);
        }

        public void addTextListener(TextListener l) {
                listenerList.add(TextListener.class, l);
        }

        public void removeTextListener(TextListener l) {
                listenerList.remove(TextListener.class, l);
        }

        protected void fireTextInput(String text) {
                TextEvent textEvent = null; 
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==TextListener.class) {
                                if (textEvent == null)
                                        textEvent = new TextEvent(this, text);
                                ((TextListener)listeners[i+1]).textEvent(textEvent);
                        }
                }
        }

        private class DoneListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                        String inputString = inputField.getText();
                        fireTextInput(inputString);
                }
        }

}
