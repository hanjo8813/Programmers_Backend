package design_pattern.adapter;

public class WAV implements Player {
    @Override
    public void play() {
        System.out.println("Play WAV file");
    }
}
