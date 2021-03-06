package se.chalmers.cs.gf.dialogutil;

import com.sri.oaa2.icl.*;

import java.util.*;

/**
 *  Some general ICL utilities.
 */
public class IclUtil {

        /**
         *  Prevents instantiation.
         */
        private IclUtil() {}

        /**
         *  Unify two terms and get the value of a variable bound by the 
         *  unifications.
         *  @return The value of var, or null if the unification failed
         *  or did not bind var.
         */
        public static IclTerm matchAndGetVar(IclTerm t1, IclTerm t2, String var) {
                Map<String,IclTerm> vars = matchAndGetVars(t1, t2);
                return vars == null ? null : vars.get(var);
        }

        /**
         *  Unify two terms and get the values of all variable bound by the 
         *  unifications.
         *  @return A map of variable names to values, or null if the unification failed.
         */
        public static Map<String,IclTerm> matchAndGetVars(IclTerm t1, IclTerm t2) {
                HashMap<String,IclTerm> vars = new HashMap<String,IclTerm>();
                if (Unifier.getInstance().matchTerms(t1, t2, vars))
                        return vars;
                return null;
        }

        /**
         *  Try unifying a term which each element in a list. For the the first
         *  term that unifies, return the value of a variable bound by the
         *  unification.
         */
        public static IclTerm matchInListAndGetVar(IclTerm t, IclList l, String var) {
                HashMap<String,IclTerm> vars = new HashMap<String,IclTerm>();
                for (IclTerm m : toList(l)) {
                        if (Unifier.getInstance().matchTerms(m, t, vars))
                                return vars.get(var);
                        vars.clear();
                }
                return null;
        }

        /**
         *  Get the first term from a list which unifies with a given term.
         */
        public static IclTerm firstMatch(IclTerm t, IclList l) {
                for (IclTerm m : toList(l)) {
                        IclTerm r = Unifier.getInstance().unify(m,t);
                        if (r != null)
                                return r;
                }
                return null;
        }

        /**
         *  Convert a string to an IclTerm.
         */
        public static IclTerm icl(String s) {
                try {
                        return IclTerm.fromString(s);
                } catch (Exception ex) {
                        throw new RuntimeException(ex);
                }
        }

        /**
         *  Convert an IclList to a list of IclTerms.
         */
        public static List<IclTerm> toList(IclList l) {
                int n = l.getNumChildren();
                List<IclTerm> r = new ArrayList<IclTerm>(n);
                for (int i = 0; i < n; i++)
                        r.add(l.getTerm(i));
                return r;
        }

        /**
         *  Convert a list of IclTerms to an IclList.
         */
        public static IclList fromList(List<IclTerm> l) {
                return new IclList(l);
        }

        /**
         *  Gets the string value of an ICL string. 
         *  @throws ClassCastException if the term is not a string.
         */
        public static String fromStr(IclTerm t) {
                return ((IclStr)t).toUnquotedString();
        }

}
