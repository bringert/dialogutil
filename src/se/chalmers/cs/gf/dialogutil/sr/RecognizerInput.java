package se.chalmers.cs.gf.dialogutil.sr;

import se.chalmers.cs.gf.dialogutil.*;

import java.io.IOException;

/**
 *  A text input source which gets input from a Recognizer.
 */
public class RecognizerInput implements TextInput {

        protected TextListenerList listeners = new TextListenerList();

        private boolean enabled = true;

        public RecognizerInput(String agentName, String[] libcomargs) {
                new RecogThread(agentName, libcomargs). start();
        }
        
        private class RecogThread extends Thread {
                private String agentName;
                private String[] libcomargs;

                private RecogThread(String agentName, String[] libcomargs) {
                        this.agentName = agentName;
                        this.libcomargs = libcomargs;
                }

                public void run() {
                        try {
                                Recognizer recog = new Recognizer(agentName, libcomargs);
                                
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
                        } catch (IOException ex) {
                                System.err.println("RecognixerInput: Error : " + ex);
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
