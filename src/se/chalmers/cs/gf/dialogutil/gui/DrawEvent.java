package se.chalmers.cs.gf.dialogutil.gui;

import java.util.EventObject;
import java.awt.Shape;

public class DrawEvent extends EventObject {

        private Shape shape;

        public DrawEvent(Object source, Shape shape) {
                super(source);
                this.shape = shape;
        }

        public Shape getShape() {
                return shape;
        }
}
