package se.chalmers.cs.gf.dialogutil.sr;

import se.chalmers.cs.gf.dialogutil.*;

import java.io.IOException;

/**
 *  A text input source which gets input from a Recognizer.
 */
public class RecognizerInput implements TextInput {

        protected TextListenerList listeners = new TextListenerList();

        private boolean enabled = true;

        public RecognizerInput(Recognizer recog) {
                new RecogThread(recog).start();
        }
        
        /**
         *  Connect to the OAA facilitator and start the recognizer 
         *  asynchronously.
         */
        public RecognizerInput(final String agentName, final String[] libcomargs) {
                new Thread() {
                        public void run() {
                                try {
                                        OAAClient client = new OAAClient(agentName, libcomargs);
                                        Recognizer recog = new Recognizer(client);
                                        new RecogThread(recog).start();
                                } catch (IOException ex) {
                                        System.err.println("Recognizer input: " + ex);
                                }
                        }
                }.start();
        }

        private class RecogThread extends Thread {
                private Recognizer recog;

                private RecogThread(Recognizer recog) {
                        this.recog = recog;
                }

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
