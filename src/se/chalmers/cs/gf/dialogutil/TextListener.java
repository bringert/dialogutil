package se.chalmers.cs.gf.dialogutil;

import java.util.EventListener;

/**
 *  Interface for objects that receive text input.
 */
public interface TextListener extends EventListener {

        /**
         *  Handle a text event.
         *  @param e The event object
         */
        public void textEvent(TextEvent e);

}
