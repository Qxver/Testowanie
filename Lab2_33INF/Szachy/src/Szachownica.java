import java.util.ArrayList;

public class Szachownica {
    ArrayList<String> pola = new ArrayList<>();


    public Szachownica() {
        for (char litera = 'A'; litera <= 'H'; litera++) {
            for (int i = 1; i <= 8; i++) {
                pola.add(litera + "" + i);
            }
        }
    }
}
