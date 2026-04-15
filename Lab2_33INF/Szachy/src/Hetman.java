import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<String> odwiedzoneStany = new HashSet<>();

        for (int i = 0; i < kierunki.length; i++) {
            int d_wiersz = kierunki[i][0];
            int d_kolumna = kierunki[i][1];

            int aktualnyWiersz = wierszStartowy;
            char aktualnaKolumna = kolumnaStartowa;

            while (true) {
                int nastepnyWiersz = aktualnyWiersz + d_wiersz;
                char nastepnaKolumna = (char) (aktualnaKolumna + d_kolumna);

                boolean odbicie = false;

                // Odwracanie wektora poziomego przy uderzeniu w prawą/lewą krawędź
                if (nastepnaKolumna < 'A' || nastepnaKolumna > maxLitera) {
                    d_kolumna = -d_kolumna;
                    odbicie = true;
                }
                // Odwracanie wektora pionowego przy uderzeniu w górną/dolną krawędź
                if (nastepnyWiersz < 1 || nastepnyWiersz > n) {
                    d_wiersz = -d_wiersz;
                    odbicie = true;
                }

                // Kalkulacja nowej pozycji po ewentualnym uderzeniu i odbiciu
                if (odbicie) {
                    nastepnyWiersz = aktualnyWiersz + d_wiersz;
                    nastepnaKolumna = (char) (aktualnaKolumna + d_kolumna);
                }

                aktualnyWiersz = nastepnyWiersz;
                aktualnaKolumna = nastepnaKolumna;

                String aktualnePole = aktualnaKolumna + "" + aktualnyWiersz;
                String stan = aktualnePole + ":" + d_wiersz + ":" + d_kolumna;

                // Zabezpieczenie przed wpadnięciem we wcześniej wytyczoną trasę lub powrotem na start po odbiciach
                if (odwiedzoneStany.contains(stan) || aktualnePole.equals(pole)) {
                    break;
                }
                odwiedzoneStany.add(stan);

                // Zapobieganie powielaniu tych samych pól na liście na skutek przecinających się linii ataku
                if (!wynik.contains(aktualnePole)) {
                    wynik.add(aktualnePole);
                }

                // Przerwanie, jeżeli hetman uderzy w przeszkodę (zalicza samo pole przeszkody jako atakowane)
                if (przeszkody != null && przeszkody.contains(aktualnePole)) {
                    break;
                }
            }
        }
        return wynik;
    }
}