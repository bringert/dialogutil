package se.chalmers.se.gf.dialogutil;

public class ConsoleOutput implements TextListener {
        
        public void textEvent(TextEvent e) {
                System.err.println(e.getText());
        }

}
