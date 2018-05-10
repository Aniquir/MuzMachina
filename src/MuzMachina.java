import com.sun.deploy.panel.JavaPanel;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//this program has been rewritten from the book "Java Rusz Glowa"
public class MuzMachina implements MetaEventListener {

    JPanel panelGlowny;
    ArrayList<JCheckBox> listaPolWyboru;
    Sequencer sekwenser;
    Sequence sekwencja;
    Track sciezka;
    JFrame ramkaGlowna;

    String[] nazwyInstrumentow = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare",
                                    "Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas",
                                    "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom",
                                    "High Agogo", "Open Hi Conga"};

    int[] instrumenty = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String[] args) {
        new MuzMachina().tworzGUI();
    }

    private void tworzGUI() {
        ramkaGlowna = new JFrame("MuzMachina");
        ramkaGlowna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout uklad = new BorderLayout();
        JPanel panelTla = new JPanel(uklad);
        panelTla.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        listaPolWyboru = new ArrayList<JCheckBox>();
        Box obszarPrzyciskow = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MojStartListener());
        obszarPrzyciskow.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MojStopListener());
        obszarPrzyciskow.add(stop);

        JButton tempoG = new JButton("Szybciej");
        tempoG.addActionListener(new MojTempoGListener());
        obszarPrzyciskow.add(tempoG);

        JButton tempoD = new JButton("Wolniej");
        tempoD.addActionListener(new MojTempoDListener());
        obszarPrzyciskow.add(tempoD);

        Box obszarNazw = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            obszarNazw.add(new Label(nazwyInstrumentow[i]));
        }

        panelTla.add(BorderLayout.EAST, obszarPrzyciskow);
        panelTla.add(BorderLayout.WEST, obszarNazw);

        ramkaGlowna.getContentPane().add(panelTla);

        GridLayout siatkaPolWyboru = new GridLayout(16,16);
        siatkaPolWyboru.setVgap(1);
        siatkaPolWyboru.setHgap(2);
        panelGlowny = new JPanel(siatkaPolWyboru);
        panelTla.add(BorderLayout.CENTER, panelGlowny);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            listaPolWyboru.add(c);
            panelGlowny.add(c);
        }

        konfigurujMidi();

        ramkaGlowna.setBounds(50,50,300,300);
        ramkaGlowna.pack();
        ramkaGlowna.setVisible(true);
    }

    public void konfigurujMidi(){
        try{
            sekwenser = MidiSystem.getSequencer();
            sekwenser.open();
            sekwencja = new Sequence(Sequence.PPQ,4);
            sciezka = sekwencja.createTrack();
            sekwenser.setTempoInBPM(120);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void utworzSciezkeIOdtworz(){
        int[] listaSciezki = null;

        sekwencja.deleteTrack(sciezka);
        sciezka = sekwencja.createTrack();

        for (int i = 0; i < 16; i++) {

            listaSciezki = new int[16];

            int klucz = instrumenty[i];

            for (int j = 0; j < 16; j++) {

                JCheckBox jc = (JCheckBox) listaPolWyboru.get(j + (16*i));
                if(jc.isSelected()){
                    listaSciezki[j] = klucz;
                } else {
                    listaSciezki[j] = 0;
                }
            }
            utworzSciezke(listaSciezki);
            sciezka.add(tworzZdarzenie(176, 1,127,0,16));
        }
        sciezka.add(tworzZdarzenie(192,9,1,0,15));

        try{

            sekwenser.setSequence(sekwencja);
            sekwenser.setLoopCount(sekwenser.LOOP_CONTINUOUSLY);
            sekwenser.start();
            sekwenser.setTempoInBPM(120);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void meta(MetaMessage meta) {

    }

    public class MojStartListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            utworzSciezkeIOdtworz();
        }
    }

    public class MojStopListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            sekwenser.stop();
        }
    }

    public class MojTempoGListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float wspTempa = sekwenser.getTempoFactor();
            sekwenser.setTempoFactor((float) (wspTempa * 1.03));
        }
    }

    public class MojTempoDListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float wspTempa = sekwenser.getTempoFactor();
            sekwenser.setTempoFactor((float) (wspTempa * .97));
        }
    }

    public void utworzSciezke(int[] lista){
        for (int i = 0; i < 16; i++) {
            int klucz = lista[i];
            if(klucz != 0){
                sciezka.add(tworzZdarzenie(144,9,klucz,100,i));
                sciezka.add(tworzZdarzenie(128,9,klucz,100,i+1));
            }
        }
    }

    public static MidiEvent tworzZdarzenie(int plc, int kanal, int jeden, int dwa, int takt){
        MidiEvent zdarzenie = null;
        try{
            ShortMessage a = new ShortMessage();
            a.setMessage(plc, kanal, jeden, dwa);
            zdarzenie = new MidiEvent(a, takt);
        } catch (Exception e){
            e.printStackTrace();
        }
        return zdarzenie;
    }

}
