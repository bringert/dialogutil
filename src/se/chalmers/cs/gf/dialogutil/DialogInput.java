package se.chalmers.cs.gf.dialogutil;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  Gets text input from a Swing dialog box.
 */
public class DialogInput extends JDialog implements TextInput {
        
        protected TextListenerList listeners = new TextListenerList();

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
                listeners.add(l);
        }

        public void removeTextListener(TextListener l) {
                listeners.remove(l);
        }

        protected void fireTextEvent(String text) {
                listeners.fireTextEvent(text);
        }

        private class DoneListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                        String inputString = inputField.getText();
                        fireTextEvent(inputString);
                }
        }

}
