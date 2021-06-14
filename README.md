# e-goat  
sieć wymiany plików peer-to-peer

*Paulina Czyż, Julian Nowak*

## Założenia

Projekt składa się z 2 programów: klienta i serwera. Na serwerze przechowywana jest lista plików udostępnianych przez poszczególnych klientów, wraz z informacją o klientach, którzy je posiadają. Klienci wymieniają się plikami bezpośrednio między sobą.

## Wymagania
 - Java 15

## Opis działania

Do komunikacji pomiędzy serwerem a klientami wykorzystany jest protokół UDP (podczas komunikacji sprawdzane są sumy kontrolne poszczególnych plików, nie potrzebna jest zatem kontrola na poziomie protokołu).

 - Uruchamiamy serwer:

 `java UDPServer`
 - Uruchamiamy klienta:

 `java UDPClient`

 - Podajemy ścieżkę do katalogu, w którym znajdują się udostępniane pliki. Klient oblicza sumy kontrolne `SHA512` plików znajdujących się w katalogu i wysyła do serwera wiadomość *List of files*. Następnie wysyła do serwera listę z sumami kontrolnymi plików.

 Do przechowywania informacji o plikach (ich sumy kontrolnej i adresu) stworzona została dodatkowa klasa `Files`.

- Serwer po odebraniu listy wysyła odpowiedź *files added to the list*.

- Następnie klient prosi o podanie sumy SHA512 pliku, który chcemy pobrać i wysyła ją do serwera.

- Serwer w odpowiedzi odsyła listę wszystkich klientów, którzy zgłosili, że taki plik posiadają. W przypadku gdy nikt nie udostępnia takiego pliku serwer wysyła odpowiednią wiadomość.

-(niezaimplementowane) Jeśli plik istnieje, klient pobiera go bezpośrednio od osoby, która go udostępnia i sprawdza czy otrzymał plik o prawidłowej sumie kontrolnej.
