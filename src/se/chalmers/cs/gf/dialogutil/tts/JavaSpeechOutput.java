package se.chalmers.cs.gf.dialogutil.tts;

/**
 * Copyright 2004 Bjorn Bringert.
 *
 * Do Text-To-Speech using the Java Speech API.
 * Based on code from the FreeTTS HelloWorld program, which 
 * bears the following header:
 */

/*
 * Copyright 2003 Sun Microsystems, Inc.
 * 
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL 
 * WARRANTIES.
 */

import java.util.Locale;
import java.io.File;

import javax.speech.Central;
import javax.speech.SpeechException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

import se.chalmers.cs.gf.dialogutil.*;

/**
 *  Outputs text to FreeTTS using the Java Speech API.
 */
public class JavaSpeechOutput implements TextListener {

        private Synthesizer synthesizer;

        public JavaSpeechOutput() {
                // the default audio player, JavaStreamingAudioPlayer blocks and
                // doesn't seem to output any sound under jdk 1.5.0 RC on linux
                // (at least on my machine) / Bjorn Bringert 2004-09-27
                System.setProperty("com.sun.speech.freetts.voice.defaultAudioPlayer", 
                                   "com.sun.speech.freetts.audio.JavaClipAudioPlayer");

                String voiceName = "kevin16"; // FIXME: parameter?

                SynthesizerModeDesc desc = new SynthesizerModeDesc(
                        null,          // engine name
                        "general",     // mode name
                        Locale.US,     // locale // FIXME: parameter?
                        null,          // running
                        null);         // voice

                try {

                        synthesizer = Central.createSynthesizer(desc);
                        
                        if (synthesizer == null) {
                                String message = "\nCan't find synthesizer.\n"
                                        + "Make sure that there is a \"speech.properties\" file "
                                        + "at either of these locations: \n"
                                        + "user.home    : "
                                        + System.getProperty("user.home") + "\n"
                                        + "java.home/lib: " 
                                        + System.getProperty("java.home")
                                        + File.separator + "lib\n";
                                
                                throw new RuntimeException(message);
                        }

                        System.err.println("JavaSpeechOutput: Calling allocate()");
                        synthesizer.allocate();
                        System.err.println("JavaSpeechOutput: Calling resume()");
                        synthesizer.resume();
                        
                        System.err.println("JavaSpeechOutput: Getting SynthesizerModeDesc");
                        desc = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
                        System.err.println("JavaSpeechOutput: Getting voices");
                        Voice[] voices = desc.getVoices();
                        Voice voice = null;
                        for (int i = 0; i < voices.length; i++) {
                                if (voices[i].getName().equals(voiceName)) {
                                        voice = voices[i];
                                        break;
                                }
                        }
                        
                        if (voice == null)
                                throw new RuntimeException(
                                        "Synthesizer does not have a voice named "
                                        + voiceName + ".");
                        
                        System.err.println("JavaSpeechOutput: Setting voice " + voiceName);
                        synthesizer.getSynthesizerProperties().setVoice(voice);

                        System.err.println("JavaSpeechOutput: loaded");

                } catch (SpeechException ex) {
                        throw new RuntimeException(ex);
                } catch (java.beans.PropertyVetoException ex) {
                        throw new RuntimeException(ex);
                }
        }
        
        /**
         *  Speaks a texts, blocking the calling thread until the entire text has been
         *  output or the thread has been interrupted.
         */
        public void blockingSpeakText(String text) {
                System.err.println("JavaSpeechOutput: Speaking...");
                synthesizer.speakPlainText(text, null);
                try {
                        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
                } catch (InterruptedException ex) { }
                System.err.println("JavaSpeechOutput: done");
        }

        /**
         *  Speaks a texts. Does not block.
         */
        public void speakText(String text) {
                synthesizer.speakPlainText(text, null);
        }

        public void textEvent(TextEvent e) {
                blockingSpeakText(e.getText());
        }

        public void finalize() {
                try {
                        System.err.println("JavaSpeechOutput: Calling deallocate()");
                        synthesizer.deallocate();
                } catch (SpeechException ex) {
                        throw new RuntimeException(ex);
                }
        }

}
