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
### 1. Build backend.jar

```bash
cd backend
mvn clean install
cd ..
```

### 2. Build frontend + backend  docker images
Przy pierwszym uruchamianiu budowanie obrazów może potrwać nawet 20m min
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
docker logs -f trello-frontend
```
4. Bardziej zaawansowane rzeczy - grzebanie bezpośrednio w uruchomionym kontenerze, kasowanie kontenera, kasowanie obrazu
```bash
docker exec -it <nazwa-kontenera> bash
docker rm <nazwa-kontenera>
docker rmi <nazwa-obrazu>
```



Frontend powinien być dostępny na `http://localhost:8080`  
Jak puścić przykładowego POSTa na backend?
```bash
curl -v 'localhost:8082/auth/register' -d '{"name": "kowalski"}'
```