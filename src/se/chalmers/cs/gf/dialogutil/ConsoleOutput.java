package se.chalmers.cs.gf.dialogutil;

/**
 *  A text event handler that outputs the text to the standard
 *  error stream.
 */
public class ConsoleOutput implements TextListener {
        
        public void textEvent(TextEvent e) {
                System.err.println(e.getText());
        }

}
