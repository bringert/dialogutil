package se.chalmers.cs.gf.dialogutil.gui;

import java.awt.Component;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.event.*;

public class MouseDrawer {

        private EventListenerList listenerList = new EventListenerList();

        private GeneralPath path;
        
        public MouseDrawer(Component c) {
                MouseInputAdapter l = new MouseDrawListener();
                c.addMouseListener(l);
                c.addMouseMotionListener(l);
        }

        private class MouseDrawListener extends MouseInputAdapter {
                public void mouseDragged(MouseEvent e) {
                        path.lineTo(e.getX(), e.getY());
                        fireDrawStarted(e.getSource(), e.getWhen());
                }
                public void mousePressed(MouseEvent e) {
                        path = new GeneralPath();
                        path.moveTo(e.getX(), e.getY());
                        fireDrawUpdated(e.getSource(), e.getWhen());
                }
                public void mouseReleased(MouseEvent e) {
                        fireDrawFinished(e.getSource(), e.getWhen());
                }
        }

        public void addDrawListener(DrawListener l) {
                listenerList.add(DrawListener.class, l);
        }

        public void removeDrawListener(DrawListener l) {
                listenerList.remove(DrawListener.class, l);
        }


        protected void fireDrawStarted(Object source, long when) {
                DrawEvent e = null;
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==DrawListener.class) {
                                if (e == null)
                                        e = new DrawEvent(source, path, when);
                                ((DrawListener)listeners[i+1]).drawStarted(e);
                        }
                }
        }

        protected void fireDrawUpdated(Object source, long when) {
                DrawEvent e = null;
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==DrawListener.class) {
                                if (e == null)
                                        e = new DrawEvent(source, path, when);
                                ((DrawListener)listeners[i+1]).drawUpdated(e);
                        }
                }
        }

        protected void fireDrawFinished(Object source, long when) {
                DrawEvent e = null;
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==DrawListener.class) {
                                if (e == null)
                                        e = new DrawEvent(source, path, when);
                                ((DrawListener)listeners[i+1]).drawFinished(e);
                        }
                }
        }

}
