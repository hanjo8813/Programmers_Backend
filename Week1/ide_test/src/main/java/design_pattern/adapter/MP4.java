package design_pattern.adapter;

public class MP4 implements OtherPlayer {
    @Override
    public void playOther() {
        System.out.println("Play MP4 file");
    }
}
