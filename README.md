# trello

## Instalacja (linux)
```bash
sudo apt install nodejs npm
sudo apt install npm
```

## Uruchomienie projektu
```bash
npm install
npm start
```

## URL
```
localhost:8080
```

## Dokeryzacja (linux)
### Instalacja dockera
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

Na sam koniec wykonujemy komendę poniżej i logujemy się ponownie
```bash
sudo usermod -a -G docker $USER
```

### Budowanie obrazue
```bash
docker build --rm -t trello .
```
### Uruchomienie obrazu dockera
```
docker run -p 8080:8080 --name hello-trello -d trello
```

## Budowa i uruchomienie backendu
```bash
mvn clean install
mvn spring-boot:run
```
Hello world bekendowe na `http://localhost:8082/auth/login`