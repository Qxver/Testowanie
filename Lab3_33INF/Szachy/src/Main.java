import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Używamy interfejsu i wstrzykujemy naszego ręcznego dublera
        IAttackService attackService = new Hetman();

        try {
            int N = 9;
            Szachownica szachownica = new Szachownica(N);

            // Korzystamy z metod walidujących
            szachownica.ustawHetmana("E5");
            szachownica.dodajPrzeszkode("C5");
            szachownica.dodajPrzeszkode("E3");
            szachownica.dodajPrzeszkode("E6");
            szachownica.dodajPrzeszkode("G5");
            szachownica.dodajPrzeszkode("F2");
            

            // Obliczenia przez serwis
            var atakowane = attackService.calculateAttack(szachownica.hetmanPos, szachownica.N, szachownica.przeszkody);
            int liczbaPola = attackService.count(atakowane);

            System.out.println("--- EDYTOR SZACHOWNICY ---");
            System.out.println("Rozmiar: " + szachownica.N + "x" + szachownica.N);
            System.out.println("Hetman na polu: " + szachownica.hetmanPos);
            System.out.println("Przeszkody: " + szachownica.przeszkody);
            System.out.println("Ilość atakowanych pól: " + liczbaPola + "\n");

            System.out.println("Widok planszy:");
            rysujSzachownice(szachownica.N, szachownica.hetmanPos, szachownica.przeszkody, atakowane);

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("BŁĄD UŻYTKOWNIKA: " + e.getMessage());
        }
    }

    private static void rysujSzachownice(int N, String poleStartowe, List<String> przeszkody, List<String> atakowane) {
        for (int wiersz = N; wiersz >= 1; wiersz--) {
            System.out.printf("%2d ", wiersz);

            for (int kolumna = 0; kolumna < N; kolumna++) {
                char litera = (char) ('A' + kolumna);
                String aktualnePole = litera + "" + wiersz;

                if (aktualnePole.equals(poleStartowe)) {
                    System.out.print("H ");
                } else if (przeszkody != null && przeszkody.contains(aktualnePole)) {
                    System.out.print("P ");
                } else if (atakowane != null && atakowane.contains(aktualnePole)) {
                    System.out.print("x ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        System.out.print("   ");
        for (int kolumna = 0; kolumna < N; kolumna++) {
            System.out.print((char) ('A' + kolumna) + " ");
        }
        System.out.println("\n");
    }
}