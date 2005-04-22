package se.chalmers.cs.gf.dialogutil.tts;

import se.chalmers.cs.gf.dialogutil.*;
import static se.chalmers.cs.gf.dialogutil.IclUtil.icl;

import com.sri.oaa2.icl.*;

import java.io.*;

/**
 *  Outputs text to FreeTTS using FreeTTSAgent.
 */
public class FreeTTSAgentSpeechOutput implements TextListener {

        private OAAClient client;

        public FreeTTSAgentSpeechOutput(OAAClient client) {
                this.client = client;                
        }

        public void speakText(String text) {
                client.solve(new IclStruct("speakText", new IclStr(text)));
        }

        public void textEvent(TextEvent e) {
                speakText(e.getText());
        }

        public static void main(String[] args) throws IOException {
                OAAClient client = new OAAClient("main", args);
                FreeTTSAgentSpeechOutput o = new FreeTTSAgentSpeechOutput(client);

                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = in.readLine()) != null) {
                        o.speakText(line);
                }
        }

}
