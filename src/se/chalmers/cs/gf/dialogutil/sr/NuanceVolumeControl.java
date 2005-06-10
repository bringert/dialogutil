package se.chalmers.cs.gf.dialogutil.sr;

import com.sri.oaa2.com.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

import se.chalmers.cs.gf.dialogutil.OAAClient;
import se.chalmers.cs.gf.dialogutil.IclUtil;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  A GUI for setting Nuance's input voulume.
 */
public class NuanceVolumeControl extends JPanel {

        private OAAClient client;

        private JSlider slider;

        public NuanceVolumeControl(OAAClient client) {
                this.client = client;
                slider = new JSlider(JSlider.VERTICAL);
                slider.setMinimum(0);
                slider.setMaximum(255);
                slider.addChangeListener(new SliderListener());
                add(slider);
        }

        private void updateVolume() {
                try {
                        slider.setValue(getNuanceVolume());
                } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                }
        }

        private int getNuanceVolume() throws IOException {
                IclTerm t = new IclStruct("nscGetParameter",
                                          new IclStr("audio.InputVolume"), 
                                          new IclVar("Vol"));
                List<IclTerm> as = client.solve(t);
                if (as.size() != 1) {
                        throw new IOException(t + " returned " 
                                              + as.size() + " results.");
                }
                
                IclTerm vol = IclUtil.matchAndGetVar(t, as.get(0), "Vol");
                return ((IclInt)vol).toInt();
        }

        private void setNuanceVolume(int vol) {
                IclTerm t = new IclStruct("nscSetParameter",
                                          new IclStr("audio.InputVolume"), 
                                          new IclInt(vol));
                client.solve(t);
        }

        private class SliderListener implements ChangeListener {
                public void stateChanged(ChangeEvent e) {
                        if (!slider.getValueIsAdjusting()) {
                                int vol = slider.getValue();
                                setNuanceVolume(vol);
                                updateVolume();
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
                c.updateVolume();
        }

}
