package se.chalmers.cs.gf.dialogutil.sr;

import com.sri.oaa2.com.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

import se.chalmers.cs.gf.dialogutil.IclUtil;
import static se.chalmers.cs.gf.dialogutil.IclUtil.icl;
import se.chalmers.cs.gf.dialogutil.OAAClient;

import java.io.IOException;
import java.util.*;

/**
 *  Calls the Nuance speech recognizer using OAA.
 */
public class Recognizer extends OAAClient {

        public Recognizer(String agentName, String[] libcomargs) throws IOException {
                super(agentName, libcomargs);
        }

        public Result recognize(String cat) {
                IclTerm context = new IclStr(cat);
                IclTerm goal = new IclStruct("nscPlayAndRecognize", context);

                if (solve(goal) == null) {
                        System.err.println(goal + " failed");
                        return null;
                }

                IclTerm getEventGoal = icl("nscGetEvent(Event)");
                IclTerm wanted = icl("nscGetEvent(recResult(Result))");

                List<IclTerm> answers;
                while ((answers = solve(getEventGoal)) != null) {
                        for (IclTerm ans : answers) {
//                                System.err.println("Got event: " + ans);

                                IclList result = (IclList)IclUtil.matchAndGetVar(ans, wanted, "Result");
                                if (result != null) {
//                                        System.err.println("Result: " + result);

                                        Result r = handleResult(result);
                                        if (r != null)
                                                return r;
                                }
                        }
                }

                return null;
        }

        private static Result handleResult(IclList result) {
                IclTerm recogTerm = IclUtil.firstMatch(icl("type('RECOGNITION')"), result);
                if (recogTerm == null)
                        return null;
                IclList r = (IclList)IclUtil.matchInListAndGetVar(icl("results([Result])"), result, "Result");
                if (r == null) {
                        System.err.println("No single result in " + result);
                        return null;
                }                

                IclStr textTerm = (IclStr)IclUtil.matchInListAndGetVar(icl("text(Text)"), r, "Text");
                if (textTerm == null) {
                        System.err.println("No text in " + r);
                        return null;
                }

                IclInt confTerm = (IclInt)IclUtil.matchInListAndGetVar(icl("confidence(Conf)"), r, "Conf");
                if (confTerm == null) {
                        System.err.println("No confidence in " + r);
                        return null;
                }                

                String text = textTerm.toUnquotedString();
                int conf = confTerm.toInt();

                return new Result(text, conf);
        }

        /**
         *  The result of the speech recognizer.
         */
        public static class Result {
                private String text;
                private int confidence;

                public Result(String text, int confidence) {
                        this.text = text;
                        this.confidence = confidence;
                }

                public String getText() {
                        return text;
                }

                public int getConfidence() {
                        return confidence;
                }

                public String toString() {
                        return "Recognizer.Result(" + text + ", " + confidence + ")";
                }
        }


        public static void main(String[] args) throws IOException {

/*
                IclList result = (IclList)icl("[dtype(17),type('RECOGNITION'),results([[dtype(10),text('six seven eight nine'),probability(-4647),confidence(60),confidenceWithoutFiller(60),wordConfidences([63,55,54,69]),interp([[dtype(9),probability(0),slotValue([]),slotInfo([])]])]]),textRecresult(''),numFrames(161),firstPassRecognizerInfo(''),secondPassRecognizerInfo('')]");
                System.err.println(handleResult(result));
*/

                Recognizer r = new Recognizer("recognizer", args);

                while (true) {
                        System.err.println(r.recognize(".MAIN"));
                }
        }

}
