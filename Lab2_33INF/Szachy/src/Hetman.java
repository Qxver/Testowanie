import java.util.ArrayList;
import java.util.List;

public class Hetman {
    public List<String> atakowanePolaHetmana(String pole) {
        List<String> wynik = new ArrayList<>();
        char kolumna = pole.charAt(0);
        int wiersz = pole.charAt(1) - '0';

        int[][] kierunki = {
                {1, 0},   // góra
                {-1, 0},  // dół
                {0, 1},   // prawo
                {0, -1},  // lewo
                {1, 1},   // skos prawo góra
                {1, -1},  // skos lewo góra
                {-1, 1},  // skos prawo dół
                {-1, -1}  // skos lewo dół
        };




        return wynik;
    }
}
