package se.chalmers.cs.gf.dialogutil;

/**
 *  Interface for objects that produce text events.
 */
public interface TextInput {

        public void addTextListener(TextListener l);

        public void removeTextListener(TextListener l);

        public void setEnabled(boolean enabled);

}
