# task

**Здесь кратко опишу как поменять вариант под свой если захотите**

Ветки с кодом:

* ветка `main` - простая версия параллельности (слегка отходит от задания лабы)
* ветка `feature/parallel-send-read` сложная версия (по заданию лабы но могут быть баги)

Для переключения веток

```shell
    git checkout main
```

```shell
    git checkout feature/parallel-send-read
```

еще есть пример переделанной лабы - ветки

* `kristina`
* `kristina-parallel`

## Описание задания

### Исходный вариант(КРАТКО):

* Пароли при хранении хэшировать алгоритмом **SHA-384**
* Для многопоточного чтения запросов использовать **ForkJoinPool**
* Для многопотчной обработки полученного запроса использовать **создание нового потока (java.lang.Thread)**
* Для многопоточной отправки ответа использовать **Cached thread pool**
* Для синхронизации доступа к коллекции использовать **синхронизацию чтения и записи с помощью synchronized**

### Исходный вариант(полная версия):

1. Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.
2. Для генерации поля id использовать средства базы данных (sequence).
3. Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
4. Все команды получения данных должны работать с коллекцией в памяти, а не в БД
5. Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.
6. Пароли при хранении хэшировать алгоритмом **SHA-384**
7. Запретить выполнение команд не авторизованным пользователям.
8. При хранении объектов сохранять информацию о пользователе, который создал этот объект.
9. Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие
   им.
10. Для идентификации пользователя отправлять логин и пароль с каждым запросом.
    Необходимо реализовать многопоточную обработку запросов.
11. Для многопоточного чтения запросов использовать **ForkJoinPool**
12. Для многопотчной обработки полученного запроса использовать **создание нового потока (java.lang.Thread)**
13. Для многопоточной отправки ответа использовать **Cached thread pool**
14. Для синхронизации доступа к коллекции использовать **синхронизацию чтения и записи с помощью synchronized**
    Порядок выполнения работы:
15. В качестве базы данных использовать PostgreSQL.
16. Для подключения к БД на кафедральном сервере использовать хост pg, имя базы данных - studs, имя пользователя/пароль
    совпадают с таковыми для подключения к
---
## Как переделать лабу под свой вариант?

Первое - учтите что в данной версии (ветка main) используется **ТОЛЬКО forkJoinPool** - для параллельной обработки
каждого потока и все

в ветке **feature/parallel-send-read** есть реализация по ТЗ но она работает не так хорошо, например при exit нужно
отправить еще 1 команду чтобы выйти, так как чтение и отправка команд работает параллельно на клиенте тоже, могут быть
еще баги

### Что менять?

**для ветки `main`**
* `common-socket`
    * enum [AppCommand](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fcommand%2FAppCommand.java) - меняете объекты enum на свои команды
    * папка [entity](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fentity) - меняете на свои классы (это POJO, entity, model, называйте хотите, классы отображающие сущности лабы)
    * класс [LabWorkInputHelper](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Futil%2FLabWorkInputHelper.java) - этот класс вообще нужен только в `client-socket`, класс помощник для доставания данных из console, тут меняете под свои сущности КРОМЕ login/register - их можно оставить  
* `client-socket`
    * класс [SecurityUtil](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2Futil%2FSecurityUtil.java) - меняете алгоритм **SHA-384** на свой по варианту 
    * папка [impl](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2Fcommand%2Fimpl) - меняете реализации клиентских команд, **НО оставляете LoginCommand и RegisterCommand**
    * класс [ClientCommandContext](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2Fcommand%2FClientCommandContext.java) - меняете состав Map<AppCommand, ClientCommand> на свои (новые из common-socket(AppCommand) и client-socket(impl))
* `server-socket`
    * класс [DatabaseConfig](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2FDatabaseConfig.java) - меняете на свои креды от базы studs (гляньте в [RUN.md](RUN.md) там описано как прокинуть порты и подключиться)
    * класс [DatabaseInitializer](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2FDatabaseInitializer.java) - просите слезно chatGPT нагенерить вам создание таблиц по сущностям из [entity](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fentity) 
    * папка [dao](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2Fdao) - просите chatGPT нагенерить методв по взаимодействию с БД (но на ваши сущности из [entity](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fentity)) 
    * класс [LabWorkTreeSetManager](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fmanager%2FLabWorkTreeSetManager.java) - **это класс для работы с коллекцией и базой, тут нужно синхронизировать доступ к коллекции**, переименовываете и меняете логику под себя, (тут `synchronized` по варианту) и естественно свои сущности и свои dao для взаимодействия с БД, так что тут просто гляньте +- как логика и подсуньте свой класс 
    * папка [resources](server-socket%2Fsrc%2Fmain%2Fresources) - можете удалить, там старая подгрузка из файла 
    * класс [Server](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2FServer.java) - меняете поле `ForkJoinPool forkJoinPool` на свой по варианту
    * папка [impl](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fcommands%2Fimpl) - меняете реализации серверных команд, **НО оставляете LoginCommand и RegisterCommand**
    * класс [ServerCommandContext](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fcommands%2FServerCommandContext.java) - меняете состав Map<AppCommand, ServerCommand> на свои (новые из common-socket(AppCommand) и server-socket(impl))

**Удалить этот файл**

**для ветки `feature/parallel-send-read`**
аналогично как в main и еще кое что
* [Client](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2FClient.java) - здесь по варианту чтение с использованием **Thread** меняете по аналогии из [Server](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2FServer.java) или ветки `kristina-parallel`
* [ProcessClientTask](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent%2FProcessClientTask.java) - по варианту отправка должна быть тоже параллельно с использованием `CachedThreadPool`, меняете на свой вариант(если у вас `FixedThreadPool` или `ForkJoinPool`,... а не `CachedThreadPool`) в переменной `ExecutorService sender`  

**Удалить этот файл**

### May be improved
1) [LabWorkInputHelper](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Futil%2FLabWorkInputHelper.java) находится в common-socket, а используется только в client-socket
2) [UserDto](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fdto%2FUserDto.java) - используется для передачи между клиентом и сервером и для маппинга из базы, так нельзя, нужно отдельно сущности для передачи UserDto и User в entity/model
3) удалить [resources](server-socket%2Fsrc%2Fmain%2Fresources)
4) удалить [XmlCollectionLoader](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fmanager%2FXmlCollectionLoader.java)
5) ветка `feature/parallel-send-read` написана по заданию лабы но по-моему можно написать лучше
6) обращение к базе в циклах и вообще работа с базой - это ужас, нагенерил chatGPT от в ас не просят оптимальности так что сойдет, но знайте на будущее - никаких обращений к бд в цикле, лучше вытянуть сразу все сущности или написать запрос с join, зависит от задачи конечно но в большинстве случаев так
7) SQLRequestException почти нигде не обрабатывается, и его логи тупо пропадают, если у вас нарушения при add/ update то смотрите туда 





