import javax.sound.midi.*;

public class MiniMiniMuzaAplk {

    public static void main(String[] args) {
        MiniMiniMuzaAplk mini = new MiniMiniMuzaAplk();
        mini.play();
    }

    public void play(){
        try{
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            Sequence sekw = new Sequence(Sequence.PPQ,4);

            Track sciezka = sekw.createTrack();

            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, 44, 100);
            MidiEvent nutaP = new MidiEvent(a, 1);
            sciezka.add(nutaP);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, 44, 100);
            MidiEvent nutaK = new MidiEvent(b, 16);
            sciezka.add(nutaK);

            sequencer.setSequence(sekw);

            sequencer.start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
