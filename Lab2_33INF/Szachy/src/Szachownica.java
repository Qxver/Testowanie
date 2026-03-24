import java.util.ArrayList;

public class Szachownica {
    public int N;
    public ArrayList<String> pola = new ArrayList<>();

    public Szachownica(int rozmiar) {
        this.N = rozmiar;
        for (int i = 0; i < N; i++) {
            char litera = (char) ('A' + i);
            for (int j = 1; j <= N; j++) {
                pola.add(litera + "" + j);
            }
        }
    }
}