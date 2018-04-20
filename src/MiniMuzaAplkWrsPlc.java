import javax.sound.midi.*;
import java.util.Scanner;

public class MiniMuzaAplkWrsPlc {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String instr1 = sc.nextLine();
        String note1 = sc.nextLine();

        MiniMuzaAplkWrsPlc mini = new MiniMuzaAplkWrsPlc();
        if(args.length > 2){
            System.out.println("Dont forget about the arguments defining the instrument and the note.");
        } else {
            int instrument = Integer.parseInt(instr1);
            int note = Integer.parseInt(note1);
            mini.play(instrument, note);
        }
    }

    public void play(int instrument, int note){

        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            Sequence sekw = new Sequence(Sequence.PPQ, 4);
            Track sciezka = sekw.createTrack();

            MidiEvent zdarzenie = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192, 1, instrument, 0);
            MidiEvent switchInstrument = new MidiEvent(first, 1);
            sciezka.add(switchInstrument);

            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, note, 100);
            MidiEvent noteP = new MidiEvent(a, 1);
            sciezka.add(noteP);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, note, 100);
            MidiEvent noteK = new MidiEvent(b, 16);
            sciezka.add(noteK);
            sequencer.setSequence(sekw);
            sequencer.start();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
