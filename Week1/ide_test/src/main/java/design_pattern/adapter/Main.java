package design_pattern.adapter;

public class Main {
    public static void main(String[] args) {
        Player player = new WAV();
        player.play();
        player = new Adapter(new MP3());
        player.play();
        player = new Adapter(new MP4());
        player.play();
    }
}
