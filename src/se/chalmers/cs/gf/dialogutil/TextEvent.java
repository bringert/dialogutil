package se.chalmers.cs.gf.dialogutil;

import java.util.EventObject;

/**
 *  Represents a text input event.
 */
public class TextEvent extends EventObject {

        private String text;

        public TextEvent(Object source, String text) {
                super(source);
                this.text = text;
        }

        /**
         *  Gets the text that was input.
         */
        public String getText() {
                return text;
        }

}
