package se.chalmers.cs.gf.dialogutil.sr;

import com.sri.oaa2.com.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

import java.io.IOException;
import java.util.*;

public class Recognizer {

        private LibOaa outOaa;

        public Recognizer(String agentName, String[] libcomargs) throws IOException {
                outOaa = new LibOaa(new LibCom(new LibComTcpProtocol(),libcomargs));

                if (!outOaa.oaaSetupCommunication(agentName))
                        throw new IOException("Couldnt connect to facilitator");

                if (!outOaa.oaaRegister("parent", agentName, new IclList(), new IclList()))
                        throw new IOException("Could not register");

                outOaa.oaaReady(true);
        }

        public Result recognize(String cat) {
                IclTerm context = new IclStr(cat);
                IclTerm goal = new IclStruct("nscPlayAndRecognize", context);

                if (simpleSolve(goal) == null) {
                        System.err.println(goal + " failed");
                        return null;
                }

                IclTerm getEventGoal = icl("nscGetEvent(Event)");
                IclTerm wanted = icl("nscGetEvent(recResult(Result))");

                IclList answers;
                while ((answers = simpleSolve(getEventGoal)) != null) {
                        for (IclTerm ans : toList(answers)) {
//                                System.err.println("Got event: " + ans);

                                IclList result = (IclList)matchAndGetVar(ans, wanted, "Result");
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

        private IclList simpleSolve(IclTerm goal) {
                IclList params = new IclList(); 
                IclList answers = new IclList();

                if (!outOaa.oaaSolve(goal, params, answers))
                        return null;

                return answers;
        }

        private static IclTerm matchAndGetVar(IclTerm t1, IclTerm t2, String var) {
                HashMap<String,IclTerm> vars = new HashMap<String,IclTerm>();
                if (Unifier.getInstance().matchTerms(t1, t2, vars))
                        return vars.get(var);
                return null;
        }

        private static IclTerm matchInListAndGetVar(IclTerm t, IclList l, String var) {
                HashMap<String,IclTerm> vars = new HashMap<String,IclTerm>();
                for (IclTerm m : toList(l)) {
                        if (Unifier.getInstance().matchTerms(m, t, vars))
                                return vars.get(var);
                        vars.clear();
                }
                return null;
        }

        private static IclTerm firstMatch(IclTerm t, IclList l) {
                for (IclTerm m : toList(l)) {
                        IclTerm r = Unifier.getInstance().unify(m,t);
                        if (r != null)
                                return r;
                }
                return null;
        }

        private static Result handleResult(IclList result) {
                IclTerm recogTerm = firstMatch(icl("type('RECOGNITION')"), result);
                if (recogTerm == null)
                        return null;
                IclList r = (IclList)matchInListAndGetVar(icl("results([Result])"), result, "Result");
                if (r == null) {
                        System.err.println("No single result in " + result);
                        return null;
                }                

                IclStr textTerm = (IclStr)matchInListAndGetVar(icl("text(Text)"), r, "Text");
                if (textTerm == null) {
                        System.err.println("No text in " + r);
                        return null;
                }

                IclInt confTerm = (IclInt)matchInListAndGetVar(icl("confidence(Conf)"), r, "Conf");
                if (confTerm == null) {
                        System.err.println("No confidence in " + r);
                        return null;
                }                

                String text = textTerm.toUnquotedString();
                int conf = confTerm.toInt();

                return new Result(text, conf);
        }

        private static IclTerm icl(String s) {
                try {
                        return IclTerm.fromString(s);
                } catch (Exception ex) {
                        throw new RuntimeException(ex);
                }
        }

        private static List<IclTerm> toList(IclList l) {
                int n = l.getNumChildren();
                List<IclTerm> r = new ArrayList<IclTerm>(n);
                for (int i = 0; i < n; i++)
                        r.add(l.getTerm(i));
                return r;
        }


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
