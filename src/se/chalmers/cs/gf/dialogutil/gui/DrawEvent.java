package se.chalmers.cs.gf.dialogutil.gui;

import java.util.EventObject;
import java.awt.Shape;

public class DrawEvent extends EventObject {

        private Shape shape;

        private long when;

        public DrawEvent(Object source, Shape shape, long when) {
                super(source);
                this.shape = shape;
                this.when = when;
        }

        public Shape getShape() {
                return shape;
        }
        
        public long getWhen() {
                return when;
        }

}
