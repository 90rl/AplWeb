# trello
## Instalacja dockera (linux)
```bash
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker
```
Po instalacji sprawdzić wersję:
```bash
docker -v
```
W terminalu powinno pojawić się `Docker version 18.09.2, build 6247962`

Na sam koniec wykonać komendy poniżej i przelogować się
```bash
sudo apt install docker-compose
sudo usermod -a -G docker $USER
```

## Budowanie

### Build frontend + backend  docker images
Przy pierwszym uruchamianiu budowanie obrazów może potrwać nawet 20 min
```bash
docker-compose build
```
## Uruchomienie front + backend
```
docker-compose up -d
```

Po wykonaniu tego polecenia frontend oraz backend powinny działać, pozostała część readme opisuje zarządzanie.

## Zarzadzanie kontenerami dockera (zatrzymanie, przebudowa, postawienie)
```bash
docker-compose down
docker-compose build
docker-compose up -d
```
### Inne przydatne polecenia
1. Lista działających kontenerów
```bash
docker ps
```

2. Lista zbudowanych obrazów
```bash
docker images
```
3. Dostęp do logów backendowych (np. aby sprawdzić czy request dotarł)
```bash
docker logs -f trello-backend
docker logs -f trello-front
```
4. Bardziej zaawansowane rzeczy - grzebanie bezpośrednio w uruchomionym kontenerze, kasowanie kontenera, kasowanie obrazu
```bash
docker exec -it <nazwa-kontenera> bash
docker rm <nazwa-kontenera>
docker rmi <nazwa-obrazu>
```



Frontend powinien być dostępny na `http://localhost:8080`  

Przykładowrejestracja usera i operacje na tablicach (user: damian, haslo: 123)
```bash
#rejestracja
curl -v 'localhost:8082/v1/auth/register' -d '{"name": "damian", "email": "damian@gmail.com", "password": "123", "repeatPassword": "123"}'
#dodanie tablicy
curl -v 'localhost:8082/v1/trello/boards' -H 'Authorization: Basic ZGFtaWFuOjEyMw==' -d '{"name":"tabliczka"}'
#wyswietlenie listy tablic
curl -v 'localhost:8082/v1/trello/boards' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#wyswietlenie tablicy
curl -v 'localhost:8082/v1/trello/boards/4' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#usuniecie tablicy
curl -v -X DELETE 'localhost:8082/v1/trello/boards/2' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#dodanie kolumny
curl -v 'localhost:8082/v1/trello/boards/4/columns' -H 'Authorization: Basic ZGFtaWFuOjEyMw==' -d '{"name":"kolumna1"}'
#usuniecie kolumny
curl -v -X DELETE 'localhost:8082/v1/trello/boards/4/columns/9' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#dodanie taska
curl -v 'localhost:8082/v1/trello/boards/4/columns/10/tasks' -H 'Authorization: Basic ZGFtaWFuOjEyMw==' -d '{"name":"task12", "description":"opis taska"}'
#usuniecie taska
curl -v -X DELETE 'localhost:8082/v1/trello/boards/4/columns/10/tasks/11' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#przeniesienie taska do kolumny
curl -v -X POST 'localhost:8082/v1/trello/boards/3/columns/7/tasks/8/move?toColumn=9' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
#zmiana pozycji kolumny, w parametrze toPosition podajemy na ktorej pozycji dana kolumna ma sie znalezc zaczynajac od zera
#czyli w przykladzie ponizej przenosze kolumne o identyfikatorze 13 na pozycje 0, czyli poczatek listy
curl -v -X POST 'localhost:8082/v1/trello/boards/3/columns/13/move?toPosition=0' -H 'Authorization: Basic ZGFtaWFuOjEyMw=='
```
W dla nieautoryzownego usera backend zwraca 403 Forbidden!

Struktura odpowiedzi dla `/boards/{boardId}`
```json
{
  "id": 1,
  "name": "Tablica1",
  "columns": [
    {
      "id": 1,
      "name": "kolumna1",
      "order": 0,
      "tasks": [
        {
          "id": 1,
          "name": "Zadanie1",
          "description": "Opis zadania"
        },
        {
          "id": 1,
          "name": "Zadanie2",
          "description": "Opis zadania"
        }
      ]
    },
    {
      "id": 2,
      "name": "kolumna2",
      "order": 1,
      "tasks": []
    },
    {
      "id": 3,
      "name": "kolumna3",
      "order": 2,
      "tasks": []
    }
  ]
}
```