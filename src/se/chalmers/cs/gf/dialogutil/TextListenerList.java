package se.chalmers.se.gf.dialogutil;

import java.util.ArrayList;
import java.util.List;

public class TextListenerList {

        private List<TextListener> listeners;

        public TextListenerList() {
                listeners = new ArrayList<TextListener>();
        }

        public void add(TextListener l) {
                listeners.add(l);
        }

        public void remove(TextListener l) {
                listeners.remove(l);
        }

        public void fireTextEvent(String text) {
                if (listeners.size() == 0)
                        return;

                TextEvent textEvent = new TextEvent(this, text);
                TextListener[] ls = listeners.toArray(new TextListener[listeners.size()]);
                for (int i = ls.length-1; i >= 0; i--)
                        ls[i].textEvent(textEvent);
        }

}
