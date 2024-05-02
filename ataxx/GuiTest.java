package ataxx;

public class GuiTest {
    public static void main(String[] args) {
        Game game;
        GUI display = new GUI("Ataxx");
        game = new Game(display, display, display);
        display.pack();
        display.setVisible(true);
        System.exit(game.play());
    }
}
