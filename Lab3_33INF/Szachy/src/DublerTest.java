import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class DublerTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


    private void symulujWejscieUzytkownika(String[] komendy) {
        String sklejoneKomendy = String.join("\n", komendy) + "\n";
        System.setIn(new ByteArrayInputStream(sklejoneKomendy.getBytes()));
    }

    @Test
    public void testUstawienieHetmanaIOpuszczenieProgramu() {
        // 1. Wybierz H -> Ustaw hetmana na B3 -> Wybierz X (Koniec)
        String[] skrypt = {"H", "B3", "X"};
        symulujWejscieUzytkownika(skrypt);

        // Uruchamiamy testowaną metodę main
        Main.main(new String[]{});

        String wyjscieKonsoli = outContent.toString();

        // Sprawdzamy czy edytor poprawnie przetworzył pole i zamknął aplikację
        assertTrue(wyjscieKonsoli.contains("Hetman: B3"));
        assertTrue(wyjscieKonsoli.contains("Zamykanie edytora."));
    }

    @Test
    public void testBladHetmanaIPonownaProba() {
        // 1. Wybierz H -> Podaj złe pole "Z9" -> Podaj dobre pole "A1" -> Wybierz X
        String[] skrypt = {"H", "Z9", "A1", "X"};
        symulujWejscieUzytkownika(skrypt);

        Main.main(new String[]{});

        String wyjscieKonsoli = outContent.toString();

        // Sprawdzamy, czy pętla while(true) wewnątrz H przechwyciła błąd i ponowiła pytanie
        assertTrue(wyjscieKonsoli.contains("BŁĄD:"));
        assertTrue(wyjscieKonsoli.contains("Hetman: A1"));
    }

    @Test
    public void testWielokrotnePrzenoszeniePrzeszkodBezWywalaniaDoMenu() {
        // Najpierw ustawiamy Hetmana (A1), potem dodajemy przeszkodę (C3)
        // Następnie wchodzimy w tryb przenoszenia (M):
        //   - Przenosimy C3 na D4
        //   - Przenosimy D4 na E5
        //   - Wpisujemy K aby wyjść z trybu M
        // Na koniec zamykamy program (X)
        String[] skrypt = {
                "H", "A1",
                "P", "C3", "K",
                "M", "C3", "D4", "D4", "E5", "K",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.main(new String[]{});

        String wyjscieKonsoli = outContent.toString();

        // Weryfikacja krok po kroku z historii konsoli
        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z C3 na D4"));
        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z D4 na E5"));
        assertTrue(wyjscieKonsoli.contains("Powrót do menu głównego."));
    }

    @Test
    public void testZlaPrzeszkodaWTrybieMnieWychodziDoMenu() {
        // Ustawienia: Hetman A1, Przeszkoda B2
        // Przenoszenie (M):
        //   - Podajemy złe pole źródłowe "H8" (nie ma tam przeszkody)
        //   - Podajemy dobre pole "B2"
        //   - Podajemy nowe zajęte pole "A1" (stoi tam hetman - wygeneruje błąd)
        //   - Podajemy dobre puste pole "F6"
        //   - Kończymy tryb M ("K") i zamykamy ("X")
        String[] skrypt = {
                "H", "A1",
                "P", "B2", "K",
                "M", "H8", "B2", "A1", "F6", "K",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.main(new String[]{});

        String wyjscieKonsoli = outContent.toString();

        // Sprawdzamy czy aplikacja informowała o błędach wewnątrz trybu M zamiast uciekać
        assertTrue(wyjscieKonsoli.contains("BŁĄD: Na polu H8 nie ma przeszkody."));
        assertTrue(wyjscieKonsoli.contains("BŁĄD: Nie można postawić przeszkody na hetmanie!"));
        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z B2 na F6"));
    }
}