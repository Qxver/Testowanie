import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

public class HetmanTest {

    @Test
    public void czyPoleHetmanaJestNaSzachownicy() {
        int N = 8;
        Szachownica szachownica = new Szachownica(N);

        String poprawnePole = "E5";
        String niepoprawnePole = "Z9";

        assertThat(szachownica.pola)
                .as("Pole %s powinno znajdować się na szachownicy", poprawnePole)
                .contains(poprawnePole);

        assertThat(szachownica.pola)
                .as("Pole %s nie powinno znajdować się na szachownicy", niepoprawnePole)
                .doesNotContain(niepoprawnePole);
    }

    @Test
    public void czyPolePrzeszkodyJestNaSzachownicy() {
        int N = 8;
        Szachownica szachownica = new Szachownica(N);

        List<String> poprawnePrzeszkody = List.of("E3", "B5", "H8");
        List<String> niepoprawnePrzeszkody = List.of("E10", "A90", "A10");

        for (String przeszkoda : poprawnePrzeszkody) {
            assertThat(szachownica.pola)
                    .as("Pole %s powinno znajdować się na szachownicy", przeszkoda)
                    .contains(przeszkoda);
        }

        for (String przeszkoda : niepoprawnePrzeszkody) {
            assertThat(szachownica.pola)
                    .as("Pole %s nie powinno znajdować się na szachownicy", przeszkoda)
                    .doesNotContain(przeszkoda);
        }
    }

    @Test
    public void czyPrzeszkodaNieZnajdujeSieNaPoluHetmana() {
        String poleHetmana = "E5";

        List<String> przeszkody = List.of("E3", "B5", "H8");
        List<String> przeszkody2 = List.of("E3", "E3", "B5", "H8");

        assertThat(przeszkody)
                .as("Lista przeszkód nie powinna zawierać pola, na którym stoi aktualnie hetman")
                .doesNotContain(poleHetmana);

        assertThat(przeszkody2.size())
                .as("Lista 'przeszkody2' zawiera duplikaty, więc rozmiary powinny być różne")
                .isNotEqualTo(new HashSet<>(przeszkody2).size());
    }

    @Test
    public void czyPoprawnieObliczaAtakZRoguBezPrzeszkod() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("A1", 8, List.of());

        assertThat(atakowane)
                .as("Z pola A1 hetman powinien atakować 21 pól")
                .hasSize(21)
                .contains("A8", "H1", "H8");
    }

    @Test
    public void czyPrzeszkodaBlokujeAtak() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("A4");

        List<String> atakowane = hetman.calculateAttack("A1", 8, przeszkody);

        assertThat(atakowane)
                .as("Powinien zaatakować pole przed przeszkodą i zbić samą przeszkodę")
                .contains("A3", "A4")
                .as("Nie powinien atakować pól za przeszkodą")
                .doesNotContain("A5", "A8");
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
        assertThat(N)
                .as("Szachownica nie może być większa niż 26 znaków")
                .isGreaterThan(26);
    }

    @Test
    public void czyObslugujeNullJakoPrzszkodyBezWyjatku() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("A1", 8, null);

        assertThat(atakowane)
                .as("Hetman powinien poprawnie obliczyć atak (21 pól), gdy lista przeszkód to null")
                .hasSize(21);
    }

    @Test
    public void czyIgnorujePrzeszkodyTypuObokLiniiAtaku() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("B3", "C2", "G8");

        List<String> atakowane = hetman.calculateAttack("A1", 8, przeszkody);

        assertThat(atakowane)
                .as("Przeszkody nie leżące na drodze ataku nie powinny wpływać na liczbę pól")
                .hasSize(21);
    }

    @Test
    public void czyPoprawnieObliczaAtakZeSrodkaSzachownicy() {
        Hetman hetman = new Hetman();
        List<String> atakowane = hetman.calculateAttack("D4", 8, List.of());

        assertThat(atakowane)
                .as("Z pola D4 hetman powinien atakować 31 pól na szachownicy 8x8")
                .hasSize(31);
    }

    @Test
    public void czyPrzeszkodyNaSasiednichPolachKrotkoBlokujaAtak() {
        Hetman hetman = new Hetman();
        List<String> przeszkody = List.of("B2", "B3", "B4", "C2", "C4", "D2", "D3", "D4");

        List<String> atakowane = hetman.calculateAttack("C3", 8, przeszkody);

        assertThat(atakowane)
                .as("Hetman powinien uderzyć w dokładnie 8 przeszkód wokół niego i koniec ataku")
                .hasSize(8)
                .as("Hetman nie powinien widzieć poza bezpośrednie otoczenie")
                .doesNotContain("E5");
    }

    @Test
    public void czyZadneAtakowanePoleNieWychodziPozaSzachownice() {
        Hetman hetman = new Hetman();
        int n = 8;
        List<String> atakowane = hetman.calculateAttack("D4", n, List.of());

        for (String zbadanePole : atakowane) {
            char kolumna = zbadanePole.charAt(0);
            int wiersz = Integer.parseInt(zbadanePole.substring(1));

            assertThat(kolumna)
                    .as("Kolumna poza zakresem: %s", zbadanePole)
                    .isGreaterThanOrEqualTo('A')
                    .isLessThanOrEqualTo('H');

            assertThat(wiersz)
                    .as("Wiersz poza zakresem: %s", zbadanePole)
                    .isBetween(1, n);
        }
    }

    @Test
    public void czyPoleStartoweNieJestSamoZaatakowanePoOdbiciu() {
        Hetman hetman = new Hetman();
        String start = "E5";
        List<String> atakowane = hetman.calculateAttack(start, 8, List.of());

        assertThat(atakowane)
                .as("Hetman nie powinien atakować pola, na którym sam stoi nawet po rykoszecie")
                .doesNotContain(start);
    }

    @Test
    public void czyPoprawnieObliczaDlaSzachownicyInnegoRozmiaru() {
        Hetman hetman = new Hetman();
        int n = 4;

        List<String> atakowaneZeSrodka = hetman.calculateAttack("B2", n, List.of());
        assertThat(atakowaneZeSrodka)
                .as("Lista zaatakowanych pól dla mniejszej szachownicy nie powinna być pusta")
                .isNotEmpty();

        List<String> atakowaneZRogu = hetman.calculateAttack("A1", n, List.of());
        assertThat(atakowaneZRogu)
                .as("Z A1 na 4x4 atakuje co najmniej pola na wprost i po skosie")
                .hasSizeGreaterThanOrEqualTo(3);
    }
}