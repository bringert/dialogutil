package se.chalmers.cs.gf.dialogutil.tts;

import se.chalmers.cs.gf.dialogutil.*;
import static se.chalmers.cs.gf.dialogutil.IclUtil.icl;

import com.sri.oaa2.icl.*;

import java.io.*;
import java.util.List;

/**
 *  Outputs text to Nuance using NuanceWrapper.
 */
public class NuanceWrapperSpeechOutput implements TextListener {

        private OAAClient client;

        public NuanceWrapperSpeechOutput(OAAClient client) {
                this.client = client;                
        }

        public void speakText(String text) {
                IclTerm append = new IclStruct("nscAppendTTS", new IclStr(text));
                if (client.solve(append).isEmpty()) {
                        System.err.println(append + " failed");
                }
                IclTerm play = icl("nscPlay(false)");
                if (client.solve(play).isEmpty()) {
                        System.err.println(play + " failed");
                }

                waitForEvent(icl("playbackStoppedEvent(Reason,Tones)"));
        }

        public void textEvent(TextEvent e) {
                speakText(e.getText());
        }

        private IclTerm waitForEvent (IclTerm wantedEvent) {
                IclTerm getEventGoal = icl("nscGetEvent(Event)");
                IclTerm wanted = new IclStruct("nscGetEvent", wantedEvent);

                List<IclTerm> answers;
                while (!((answers = client.solve(getEventGoal)).isEmpty())) {
                        for (IclTerm ans : answers) {
                                if (Unifier.getInstance().unify(ans, wanted) != null) {
                                        return ((IclStruct)ans).getTerm(0);
                                }
                        }
                }

                return null;
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
