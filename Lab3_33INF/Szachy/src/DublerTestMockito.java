import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DublerTestMockito {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Mock
    private IAttackService mockAttackService;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(mockAttackService.calculateAttack(anyString(), anyInt(), anyList())).thenReturn(Collections.emptyList());
        when(mockAttackService.count(anyList())).thenReturn(0);
    }

    @After
    public void tearDown() throws Exception {
        System.setIn(originalIn);
        System.setOut(originalOut);
        if (closeable != null) {
            closeable.close();
        }
    }

    private void symulujWejscieUzytkownika(String[] komendy) {
        String PolKomendy = String.join("\n", komendy) + "\n";
        System.setIn(new ByteArrayInputStream(PolKomendy.getBytes()));
    }

    @Test
    public void testUstawienieHetmana() {
        String[] skrypt = {"H", "B3", "X"};
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Hetman: B3"));
        assertTrue(wyjscieKonsoli.contains("Zamykanie edytora."));

        verify(mockAttackService, atLeastOnce()).calculateAttack(eq("B3"), anyInt(), anyList());
    }

    @Test
    public void testBladHetmanaIPonownaProba() {
        String[] skrypt = {"H", "Z9", "A1", "X"};
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("BŁĄD:"));
        assertTrue(wyjscieKonsoli.contains("Hetman: A1"));

        verify(mockAttackService, atLeastOnce()).calculateAttack(eq("A1"), anyInt(), anyList());
    }

    @Test
    public void testPrzenoszeniePrzeszkod() {
        String[] skrypt = {
                "H", "A1",
                "P", "C3", "K",
                "M", "C3", "D4", "D4", "E5", "K",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z C3 na D4"));
        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z D4 na E5"));
        assertTrue(wyjscieKonsoli.contains("Powrót do menu głównego."));
    }

    @Test
    public void testZlaPrzeszkodaWTrybiePrzenoszeniaPrzeszkod() {
        String[] skrypt = {
                "H", "A1",
                "P", "B2", "K",
                "M", "H8", "B2", "A1", "F6", "K",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("BŁĄD: Na polu H8 nie ma przeszkody."));
        assertTrue(wyjscieKonsoli.contains("BŁĄD: Nie można postawić przeszkody na hetmanie!"));
        assertTrue(wyjscieKonsoli.contains("Pomyślnie przeniesiono przeszkodę z B2 na F6"));
    }

    @Test
    public void testDodawaniePrzeszkod() {
        String[] skrypt = {
                "H", "A1",
                "P", "C3", "D4", "C3", "K",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Dodano przeszkodę: C3"));
        assertTrue(wyjscieKonsoli.contains("Dodano przeszkodę: D4"));
        assertTrue(wyjscieKonsoli.contains("BŁĄD: Na tym polu znajduje się już przeszkoda!"));
    }

    @Test
    public void testWyczyszczeniezachownicy() {
        String[] skrypt = {
                "H", "E4",
                "P", "A1", "K",
                "C",
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Szachownica wyczyszczona!"));
    }

    @Test
    public void testZapisDoPliku() {
        String testowyPlik = "zapis_planszy_test.txt";

        String[] skrypt = {
                "H", "A1",
                "P", "B2", "C3", "K",
                "Z", testowyPlik,
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Zapisano stan szachownicy do pliku: " + testowyPlik));

        java.io.File file = new java.io.File(testowyPlik);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testOdczytZPliku() {
        String testowyPlik = "odczyt_planszy_test.txt";

        String[] skrypt = {
                "H", "E4",
                "Z", testowyPlik,
                "C",
                "O", testowyPlik,
                "X"
        };
        symulujWejscieUzytkownika(skrypt);

        Main.uruchomEdytor(mockAttackService);

        String wyjscieKonsoli = outContent.toString();

        assertTrue(wyjscieKonsoli.contains("Pomyślnie odczytano stan szachownicy z pliku!"));
        assertTrue(wyjscieKonsoli.contains("Hetman: E4"));

        java.io.File file = new java.io.File(testowyPlik);
        if (file.exists()) {
            file.delete();
        }
    }
}