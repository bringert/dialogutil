package se.chalmers.cs.gf.dialogutil;

import java.util.Iterator;

public class StringUtil {

        private StringUtil() {};

        public static String join(CharSequence glue, Iterable<?> xs) {
                StringBuilder sb = new StringBuilder();
                Iterator<?> it = xs.iterator();
                while (it.hasNext()) {
                        sb.append(it.next());
                        if (it.hasNext())
                                sb.append(glue);
                }
                return sb.toString();
        }

}
