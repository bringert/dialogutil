package se.chalmers.cs.gf.dialogutil.sr;

import com.sri.oaa2.com.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

import se.chalmers.cs.gf.dialogutil.OAAClient;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  A GUI for setting Nuance's input voulume.
 */
public class NuanceVolumeControl extends JPanel {

        private OAAClient client;

        public NuanceVolumeControl(OAAClient client) {
                this.client = client;
                JSlider slider = new JSlider(JSlider.VERTICAL, 0, 255);
                slider.addChangeListener(new SliderListener());
                add(slider);
        }

        private void setVolume(int vol) {
                IclTerm t = new IclStruct("nscSetParameter",
                                          new IclStr("audio.InputVolume"), 
                                          new IclInt(vol));
                client.solve(t);
        }

        private class SliderListener implements ChangeListener {
                public void stateChanged(ChangeEvent e) {
                        JSlider source = (JSlider)e.getSource();
                        if (!source.getValueIsAdjusting()) {
                                int vol = source.getValue();
                                setVolume(vol);
                        }
                }
        }

        public static void main(String[] args) throws IOException {
                OAAClient client = new OAAClient("volume", args);
                NuanceVolumeControl c = new NuanceVolumeControl(client);
                JFrame f = new JFrame("Nuance Volume");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(c);
                f.pack();
                f.setVisible(true);
        }

}
