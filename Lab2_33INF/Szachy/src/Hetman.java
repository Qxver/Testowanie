import java.util.ArrayList;
import java.util.List;

public class Hetman {
    public List<String> calculateAttack(String pole, int n, List<String> przeszkody) {
        List<String> wynik = new ArrayList<>();
        char kolumnaStartowa = pole.charAt(0);
        int wierszStartowy = Integer.parseInt(pole.substring(1));

        int[][] kierunki = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        char maxLitera = (char) ('A' + n - 1);

        for (int i = 0; i < kierunki.length; i++) {
            int d_wiersz = kierunki[i][0];
            int d_kolumna = kierunki[i][1];

            int aktualnyWiersz = wierszStartowy + d_wiersz;
            char aktualnaKolumna = (char) (kolumnaStartowa + d_kolumna);

            while (aktualnaKolumna >= 'A' && aktualnaKolumna <= maxLitera &&
                    aktualnyWiersz >= 1 && aktualnyWiersz <= n) {

                String aktualnePole = aktualnaKolumna + "" + aktualnyWiersz;
                wynik.add(aktualnePole);

                if (przeszkody != null && przeszkody.contains(aktualnePole)) {
                    break;
                }

                aktualnyWiersz += d_wiersz;
                aktualnaKolumna += (char) d_kolumna;
            }
        }
        return wynik;
    }
}