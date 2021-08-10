package design_pattern.adapter;

public class MP3 implements OtherPlayer {
    @Override
    public void playOther() {
        System.out.println("Play MP3 file");
    }
}
