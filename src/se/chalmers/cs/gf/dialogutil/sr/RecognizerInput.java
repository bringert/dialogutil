package se.chalmers.cs.gf.dialogutil.sr;

import se.chalmers.cs.gf.dialogutil.*;

/**
 *  A text input source which gets input from a Recognizer.
 */
public class RecognizerInput implements TextInput {

        protected TextListenerList listeners = new TextListenerList();

        private Recognizer recog;

        private boolean enabled;

        public RecognizerInput(Recognizer recog) {
                this.recog = recog;
                this.enabled = true;
                new RecogThread(). start();
        }
        
        private class RecogThread extends Thread {
                public void run() {
                        while (true) {
                                
                                synchronized (RecognizerInput.this) {
                                        while (!enabled)
                                                try {
                                                        RecognizerInput.this.wait();
                                                } catch (InterruptedException ex) {}
                                }


                                Recognizer.Result r = recog.recognize(".MAIN");
                                if (r != null) {
                                        final String text = r.getText();
                                        fireTextEvent(text);
                                }
                        }
                        
                }
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

        public synchronized void setEnabled(boolean enabled) {
                System.err.println("RecognizerInput: " + (enabled ? "enabled" : "disabled"));

                this.enabled = enabled;
                notifyAll();
        }

}
