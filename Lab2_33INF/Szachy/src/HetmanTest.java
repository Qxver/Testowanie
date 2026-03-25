import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

public class HetmanTest {

    @Test
    public void czyPoleHetmanaJestNaSzachownicy() {
        int N = 8;
        Szachownica szachownica = new Szachownica(N);

        String poprawnePole = "E5";
        String niepoprawnePole = "Z9";

        assertTrue("Pole " + poprawnePole + " powinno znajdować się na szachownicy",
                szachownica.pola.contains(poprawnePole));

        assertFalse("Pole " + niepoprawnePole + " nie powinno znajdować się na szachownicy",
                szachownica.pola.contains(niepoprawnePole));
    }

    @Test
    public void czyPolePrzeszkodyJestNaSzachownicy() {
        int N = 8;
        Szachownica szachownica = new Szachownica(N);

        List<String> poprawnePrzeszkody = List.of("E3", "B5", "H8");
        List<String> niepoprawnePrzeszkody = List.of("E10", "A90", "A10");

        for (String przeszkoda : poprawnePrzeszkody) {
            assertTrue("Pole " + przeszkoda + " powinno znajdować się na szachownicy",
                    szachownica.pola.contains(przeszkoda));
        }

        for (String przeszkoda : niepoprawnePrzeszkody) {
            assertFalse("Pole " + przeszkoda + " nie powinno znajdować się na szachownicy",
                    szachownica.pola.contains(przeszkoda));
        }
    }

    @Test
    public void czyPrzeszkodaNieZnajdujeSieNaPoluHetmana() {
        String poleHetmana = "E5";

        List<String> przeszkody = List.of("E3", "B5", "H8");
        List<String> przeszkody2 = List.of("E3", "E3", "B5", "H8");

        assertFalse("Lista przeszkód nie powinna zawierać pola, na którym stoi aktualnie hetman (" + poleHetmana + ")",
                przeszkody.contains(poleHetmana));

        assertNotEquals("Lista 'przeszkody2' zawiera duplikaty, więc rozmiary powinny być różne",
                przeszkody2.size(), new HashSet<>(przeszkody2).size());
    }
}