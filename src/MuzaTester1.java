import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class MuzaTester1 {

    public void play(){
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            System.out.println("We have sequencer!");
        } catch (MidiUnavailableException ex){
            System.out.println("We have a problem.");
        }

    }

    public static void main(String[] args) {
        MuzaTester1 mt = new MuzaTester1();

        mt.play();
    }
}
