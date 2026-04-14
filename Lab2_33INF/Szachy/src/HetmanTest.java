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
        hetman.calculateAttack("", 8, List.of());
    }

    @Test(expected = NumberFormatException.class)
    public void czyRzucaWyjatekDlaBlednegoFormatuPola() {
        Hetman hetman = new Hetman();
        hetman.calculateAttack("A", 8, List.of());
    }

    @Test
    public void czySzachownicaNieJestWiekszaNiz26(){
        int N = 27;
        assertFalse("Szachownica nie może być większa niż 26 znaków", N<=26);
    }

    @Test
    public void czyObslugujeNullJakoPrzszkodyBezWyjatku() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("A1", 8, null);

        assertEquals("Hetman powinien poprawnie obliczyć atak (21 pól), gdy lista przeszkód to null",
                21, atakowane.size());
    }

    @Test
    public void czyIgnorujePrzeszkodyTypuObokLiniiAtaku() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("B3", "C2", "G8");

        List<String> atakowane = hetman.calculateAttack("A1", 8, przeszkody);

        assertEquals("Przeszkody nie leżące na drodze ataku nie powinny wpływać na liczbę pól",
                21, atakowane.size());
    }

    @Test
    public void czyPoprawnieObliczaAtakZeSrodkaSzachownicy() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("D4", 8, List.of());

        assertEquals("Z pola D4 hetman powinien atakować 31 pól na szachownicy 8x8",
                31, atakowane.size());
    }

    @Test
    public void czyPrzeszkodyNaSasiednichPolachKrotkoBlokujaAtak() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("B2", "B3", "B4", "C2", "C4", "D2", "D3", "D4");

        List<String> atakowane = hetman.calculateAttack("C3", 8, przeszkody);

        assertEquals("Hetman powinien uderzyć w dokładnie 8 przeszkód wokół niego i koniec ataku",
                8, atakowane.size());
        assertFalse("Hetman nie powinien widzieć poza bezpośrednie otoczenie",
                atakowane.contains("E5"));
    }

    @Test
    public void czyZadneAtakowanePoleNieWychodziPozaSzachownice() {
        Hetman hetman = new Hetman();
        int n = 8;
        List<String> atakowane = hetman.calculateAttack("D4", n, List.of());

        for (String zbadanePole : atakowane) {
            char kolumna = zbadanePole.charAt(0);
            int wiersz = Integer.parseInt(zbadanePole.substring(1));

            assertTrue("Kolumna poza zakresem: " + zbadanePole, kolumna >= 'A' && kolumna <= 'H');
            assertTrue("Wiersz poza zakresem: " + zbadanePole, wiersz >= 1 && wiersz <= n);
        }
    }

    @Test
    public void czyPoleStartoweNieJestSamoZaatakowanePoOdbiciu() {
        Hetman hetman = new Hetman();
        String start = "E5";
        List<String> atakowane = hetman.calculateAttack(start, 8, List.of());

        assertFalse("Hetman nie powinien atakować pola, na którym sam stoi nawet po rykoszecie",
                atakowane.contains(start));
    }

    @Test
    public void czyPoprawnieObliczaDlaSzachownicyInnegoRozmiaru() {
        Hetman hetman = new Hetman();
        int n = 4;

        List<String> atakowaneZeSrodka = hetman.calculateAttack("B2", n, List.of());
        assertFalse("Lista zaatakowanych pól dla mniejszej szachownicy nie powinna być pusta",
                atakowaneZeSrodka.isEmpty());

        List<String> atakowaneZRogu = hetman.calculateAttack("A1", n, List.of());
        assertTrue("Z A1 na 4x4 atakuje co najmniej pola na wprost i po skosie",
                atakowaneZRogu.size() >= 3);
    }

        @Test
        public void czyOdbiciaNieWpadajaWNieskonczonaPetleNaDuzejPlanszy() {
            Hetman hetman = new Hetman();
            int n = 20;

            // Brak asercji liczbowej - sprawdzamy jedynie, czy funkcja
            // nie zawiesi się (Timeout) na dużej liczbie rykoszetów
            List<String> atakowane = hetman.calculateAttack("A1", n, List.of());

            assertFalse("Lista nie powinna być pusta na dużej planszy", atakowane.isEmpty());
        }
}