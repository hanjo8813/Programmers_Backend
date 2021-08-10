package design_pattern.adapter;

public class Adapter implements Player {
    private OtherPlayer otherPlayer;

    public Adapter(OtherPlayer o){
        otherPlayer = o;
    }

    @Override
    public void play() {
        System.out.print("Using Adapter --> ");
        otherPlayer.playOther();
    }
}
