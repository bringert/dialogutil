package se.chalmers.cs.gf.dialogutil.gui;

import java.util.EventListener;

public interface DrawListener extends EventListener {
        
        public void drawStarted(DrawEvent e);

        public void drawUpdated(DrawEvent e);

        public void drawFinished(DrawEvent e);
}
