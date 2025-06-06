# how to run app?

## Пошаговая инструкция
1) download PostgreSQL or run it on docker
внизу инструкция как запустить через docker-compose в терминале

2) запускаем Server через IntelliJ

3) собираем `.jar` файл клиента чтобы запустить несколько клиентов

как это сделать? - запускаем в терминале intelliJ:
```shell
  mvn clean install
```
это команда maven которая удаляет генерируемую папку [target](common-socket%2Ftarget) (не только в client, но и в server и в common)
и генерирует НОВЫЕ `.jar` с последними изменениями в коде
нас интересует именно [client-socket/target/client-socket.jar](client-socket%2Ftarget%2Fclient-socket.jar)

эта папка появится у вас после mvn clean install

4) открываем терминал можно и intelliJ, можно обычный (главное чтобы java работала в терминале)

как проверить? - запускаем команду
```shell
  java --version
```
если на экране вывелась версия то все ок)

далее открыли мы терминал и че? Идем в папку [client-socket](client-socket)
и дублируем терминалы до 4 штук (то есть открываете 4 терминала все в папке [client-socket](client-socket))

5) Запускаем по очереди 4 клиента (в 4 разных терминалах, ТОЛЬКО 3 подключатся)
```shell
  java -jar target/client-socket.jar
```

и должны увидеть в Server что подключилось 3 из 4!!!!
```text
Сервер запущен и ожидает подключения...
[Tech] [INFO] Клиент [unauthorized_1] подключился 
[Tech] [INFO] Клиент [unauthorized_2] подключился 
[Tech] [INFO] Клиент [unauthorized_3] подключился 
```

потому что `ru.itmo.socket.common.util.ConnectionContext.MAX_CONNECTIONS = 3`
4 специально не принимается) вот и все


6) далее выполняем register login 
так как без login нельзя выполнить другие команды :)

```text
    register
    lol
    lol
```


```text
    login
    lol
    lol
```


```text
Отправлено клиенту [lol] вывод команды: login
```

7) Все вы залогинены) так что можно прогонять команды 
help, show, add ,....

кстати при add была добавлена автогенерация чтобы 3 часа не вводить каждый раз сущность!


## docker
Инструкция как через docker-compose запустить  PostgreSQL
0) скачиваем docker, docker-compose


**START**
создаем контейнер
```shell
docker-compose -f docker-compose.yml up -d
```

**CHECK POSTGRESQL CONTAINER**
чекаем что он жив
```shell
docker ps
```

**END**
удаляем контейнер ВМЕСТЕ С СОДЕРЖИМЫМ
```shell
docker-compose down -v
```
(если не хотите удалять сожержимое то не пишите `-v` флаг)

Удачи :) 