package se.chalmers.cs.gf.dialogutil.sr;

import se.chalmers.cs.gf.dialogutil.*;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

public class RecognizerInput implements TextInput {

        protected EventListenerList listenerList = new EventListenerList();

        private Recognizer recog;

        public RecognizerInput(Recognizer recog) {
                this.recog = recog;
                new RecogThread(). start();
        }
        
        private class RecogThread extends Thread {
                public void run() {
                        while (true) {
                                Recognizer.Result r = recog.recognize(".MAIN");
                                if (r != null) {
                                        final String text = r.getText();
                                        SwingUtilities.invokeLater(
                                                new Runnable() {
                                                        public void run() {  
                                                                fireTextInput(text);
                                                        }
                                                });
                                }
                        }
                        
                }
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

}
