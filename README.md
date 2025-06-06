# lab7
описание запуска 7 лабы в файле [RUN.md](RUN.md)

## Description
6 лаба Итмо по сокетам

2 модуля

1) common-socket - тут лежат общие классы / сущности что будут передаваться между клиентом и сервером
2) server-socket - тут код сервера (к нему подключаются клиенты)
3) client-socket - тут код клиента, они подключаются к серверу

```shell
    mvn clean install
```

`Ctrl + Shft + F` - find in files