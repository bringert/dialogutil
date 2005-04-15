package se.chalmers.cs.gf.dialogutil.tts;

import se.chalmers.cs.gf.dialogutil.*;

import com.sri.oaa2.icl.*;

import java.io.*;

/**
 *  Outputs text to FreeTTS using the Java Speech API.
 */
public class NuanceWrapperSpeechOutput implements TextListener {

        private OAAClient client;

        public NuanceWrapperSpeechOutput(OAAClient client) {
                this.client = client;                
        }

        public void speakText(String text) {
                client.solve(new IclStruct("appendTTS", new IclStr(text)));
                client.solve(new IclStruct("nscPlay", new IclStr("false")));
        }

        public void textEvent(TextEvent e) {
                speakText(e.getText());
        }

        public static void main(String[] args) throws IOException {
                OAAClient client = new OAAClient("main", args);
                NuanceWrapperSpeechOutput o = new NuanceWrapperSpeechOutput(client);

                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = in.readLine()) != null) {
                        o.speakText(line);
                }
        }

}
