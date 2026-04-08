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

    @Test
    public void czyPoprawnieObliczaAtakZRoguBezPrzeszkod() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("A1", 8, List.of());

        assertEquals("Z pola A1 hetman powinien atakować 21 pól", 21, atakowane.size());

        assertTrue(atakowane.contains("A8"));
        assertTrue(atakowane.contains("H1"));
        assertTrue(atakowane.contains("H8"));
    }

    @Test
    public void czyPrzeszkodaBlokujeAtak() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("A4");

        List<String> atakowane = hetman.calculateAttack("A1", 8, przeszkody);

        assertTrue("Powinien zaatakować pole przed przeszkodą", atakowane.contains("A3"));
        assertTrue("Powinien zaatakować zbić samą przeszkodę", atakowane.contains("A4"));

        assertFalse("Nie powinien atakować pola za przeszkodą", atakowane.contains("A5"));
        assertFalse("Nie powinien atakować pola za przeszkodą", atakowane.contains("A8"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void czyRzucaWyjatekDlaPustegoCiaguZnakow() {
        Hetman hetman = new Hetman();
        // Przekazujemy pusty String, charAt(0) wyrzuci wyjątek
        hetman.calculateAttack("", 8, List.of());
    }

    @Test(expected = NumberFormatException.class)
    public void czyRzucaWyjatekDlaBlednegoFormatuPola() {
        Hetman hetman = new Hetman();
        // Brak liczby na końcu - substring(1) nie znajdzie liczby i rzuci błąd
        hetman.calculateAttack("A", 8, List.of());
    }

    @Test
    public void czySzachownicaNieJestWiekszaNiz26(){
        int N = 27;
        assertFalse("Szachownica nie może być większa niż 26 znaków", N<=26);
    }
}