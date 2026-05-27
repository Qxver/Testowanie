import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        IAttackService attackService = new Hetman();
        Scanner scanner = new Scanner(System.in);
        Szachownica szachownica = new Szachownica(8);

        boolean programDziala = true;

        while (programDziala) {
            List<String> atakowane = null;
            if (szachownica.hetmanPos != null) {
                atakowane = attackService.calculateAttack(szachownica.hetmanPos, szachownica.N, szachownica.przeszkody);
                int liczbaPola = attackService.count(atakowane);

                System.out.println("\n=== EDYTOR SZACHOWNICY INTERAKTYWNY ===");
                System.out.println("Hetman: " + szachownica.hetmanPos + " | Przeszkody: " + szachownica.przeszkody);
                System.out.println("Atakowane pola: " + liczbaPola);
                rysujSzachownice(szachownica.N, szachownica.hetmanPos, szachownica.przeszkody, atakowane);
                }

            // 2. Menu nawigacyjne
            System.out.println("WYBIERZ AKCJĘ:");
            System.out.println("H - Ustaw/Zmień Hetmana");
            System.out.println("P - Dodaj Przeszkody");
            System.out.println("M - Przenieś istniejące przeszkody (wiele zmian)");
            System.out.println("Z - Zapisz stan szachownicy do pliku");
            System.out.println("O - Odczytaj stan szachownicy z pliku");
            System.out.println("C - Wyczyść wszystko (nowa gra)");
            System.out.println("X - Zakończ program");
            System.out.print("Twój wybór: ");

            String wybor = scanner.nextLine();

            switch (wybor) {
                case "H":
                    while (true) {
                        System.out.print("Podaj nowe pole dla hetmana: ");
                        String nowyHetman = scanner.nextLine();
                        try {
                            szachownica.ustawHetmana(nowyHetman);
                            break;
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            System.out.println("BŁĄD: " + e.getMessage() + " Spróbuj ponownie.");
                        }
                    }
                    break;

                case "P":
                    System.out.println("Podaj przeszkody (wpisz 'K' aby przestać dodawać):");
                    while (true) {
                        System.out.print("Pole przeszkody: ");
                        String p = scanner.nextLine();
                        if (p.equals("K")) break;
                        try {
                            if (szachownica.przeszkody.contains(p)) {
                                throw new IllegalStateException("Na tym polu znajduje się już przeszkoda!");
                            }
                            szachownica.dodajPrzeszkode(p);
                            System.out.println("Dodano przeszkodę: " + p);
                        } catch (Exception e) {
                            System.out.println("BŁĄD: " + e.getMessage());
                        }
                    }
                    break;

                case "M":
                    while (true) {
                        if (szachownica.przeszkody.isEmpty()) {
                            System.out.println("INFO: Na szachownicy nie ma już żadnych przeszkód do przeniesienia.");
                            break;
                        }

                        String starePole = "";
                        boolean wyjscieDoMenu = false;

                        while (true) {
                            System.out.println("\nAktualne przeszkody: " + szachownica.przeszkody);
                            System.out.print("Wybierz przeszkodę, którą chcesz przenieść (wpisz 'K' aby wrócić do menu głównego): ");
                            starePole = scanner.nextLine();

                            if (starePole.equals("K")) {
                                wyjscieDoMenu = true;
                                break;
                            }

                            if (szachownica.przeszkody.contains(starePole)) {
                                break;
                            } else {
                                System.out.println("BŁĄD: Na polu " + starePole + " nie ma przeszkody. Spróbuj ponownie.");
                            }
                        }

                        if (wyjscieDoMenu) {
                            System.out.println("Powrót do menu głównego.");
                            break;
                        }

                        while (true) {
                            System.out.print("Podaj nowe pole dla przeszkody " + starePole + " (wpisz 'K' aby anulować ten ruch): ");
                            String nowePole = scanner.nextLine();

                            if (nowePole.equals("K")) {
                                System.out.println("Anulowano ruch dla przeszkody " + starePole + ".");
                                break;
                            }

                            try {
                                if (!szachownica.pola.contains(nowePole)) {
                                    throw new IllegalArgumentException("Pole poza szachownicą!");
                                }
                                if (nowePole.equals(szachownica.hetmanPos)) {
                                    throw new IllegalStateException("Nie można postawić przeszkody na hetmanie!");
                                }
                                if (szachownica.przeszkody.contains(nowePole) && !nowePole.equals(starePole)) {
                                    throw new IllegalStateException("Na nowym polu znajduje się już inna przeszkoda!");
                                }

                                szachownica.przeszkody.remove(starePole);
                                szachownica.dodajPrzeszkode(nowePole);
                                System.out.println("Pomyślnie przeniesiono przeszkodę z " + starePole + " na " + nowePole);

                                if (szachownica.hetmanPos != null) {
                                    List<String> aktualneAtaki = attackService.calculateAttack(szachownica.hetmanPos, szachownica.N, szachownica.przeszkody);
                                    System.out.println("\n--- PLANSZA PO PRZESUNIĘCIU ---");
                                    rysujSzachownice(szachownica.N, szachownica.hetmanPos, szachownica.przeszkody, aktualneAtaki);
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("BŁĄD: " + e.getMessage() + " Spróbuj ponownie.");
                            }
                        }
                    }
                    break;

                case "Z":
                    System.out.print("Podaj nazwę pliku do zapisu (np. szachownica.txt): ");
                    String nazwaPlikuZapis = scanner.nextLine();
                    try {
                        szachownica.zapisz(nazwaPlikuZapis);
                        System.out.println("Zapisano stan szachownicy do pliku: " + nazwaPlikuZapis);
                    } catch (IOException e) {
                        System.out.println("BŁĄD ZAPISU: Nie udało się zapisać pliku. " + e.getMessage());
                    }
                    break;

                case "O":
                    System.out.print("Podaj nazwę pliku do odczytu (np. szachownica.txt): ");
                    String nazwaPlikuOdczyt = scanner.nextLine();
                    try {
                        szachownica.odczytaj(nazwaPlikuOdczyt);
                        System.out.println("Pomyślnie odczytano stan szachownicy z pliku!");
                    } catch (IOException e) {
                        System.out.println("BŁĄD ODCZYTU: Nie znaleziono pliku lub plik jest uszkodzony. " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("BŁĄD PARSOWANIA: Format danych w pliku jest niepoprawny.");
                    }
                    break;

                case "C":
                    szachownica.hetmanPos = null;
                    szachownica.przeszkody.clear();
                    System.out.println("Szachownica wyczyszczona!\n");
                    break;

                case "X":
                    programDziala = false;
                    System.out.println("Zamykanie edytora.");
                    break;

                default:
                    System.out.println("Nieznana opcja, spróbuj ponownie.\n");
            }
        }
        scanner.close();
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