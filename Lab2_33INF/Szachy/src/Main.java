import java.util.List;

public class Main {
    public static void main(String[] args) {
        int N = 20;
        Szachownica szachownica = new Szachownica(N);
        Hetman hetman = new Hetman();

        String poleStartowe = "E5";
        List<String> przeszkody = List.of("C5", "E3", "E6", "G5", "F2");

        var atakowane = hetman.calculateAttack(poleStartowe, szachownica.N, przeszkody);

        System.out.println("Szachownica rozmiaru: " + N + "x" + N);
        System.out.println("Hetman na polu: " + poleStartowe);
        System.out.println("Przeszkody na polach: " + przeszkody);
        System.out.println("Możliwość atakowanych pól " + atakowane.size() + " pól.\n");

        System.out.println("Widok planszy:");

        // Rysowanie planszy od górnego wiersza w dół
        for (int wiersz = N; wiersz >= 1; wiersz--) {
            // Wypisanie numeru wiersza z wyrównaniem
            System.out.printf("%2d ", wiersz);

            for (int kolumna = 0; kolumna < N; kolumna++) {
                char litera = (char) ('A' + kolumna);
                String aktualnePole = litera + "" + wiersz;

                if (aktualnePole.equals(poleStartowe)) {
                    System.out.print("H ");
                } else if (przeszkody != null && przeszkody.contains(aktualnePole)) {
                    // Jeśli przeszkoda została trafiona, wciąż pokazujemy ją jako P (ale można też jako x)
                    System.out.print("P ");
                } else if (atakowane.contains(aktualnePole)) {
                    System.out.print("x ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        // Rysowanie liter kolumn na dole
        System.out.print("   ");
        for (int kolumna = 0; kolumna < N; kolumna++) {
            System.out.print((char) ('A' + kolumna) + " ");
        }
        System.out.println("\n");
    }
}