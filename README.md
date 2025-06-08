# lab7

Описание запуска 7 лабы в файле [RUN.md](RUN.md)
Описание того как вам понять лабу под свой вариант в [TASK.md](TASK.md) + там описание чуть чуть ветки `feature/parallel-send-read` 

## Про ветки
* README.md - описание для лабы на ветке `main`
* на ветке `feature/parallel-send-read` можно найти усложненную версию где СЕРВЕР не только всех клиентов параллельно обрабатывает но и для КАЖДОГО КЛИЕНТА параллельно читает и обрабатывает его команды, а клиент параллельно отправляет команды и читает их

для переключения по веткам в консоли

**На усложненную ветку**
```shell
  git checkout feature/parallel-send-read
```

**Обратно На main**
```shell
  git checkout feature/parallel-send-read
  git checkout main
```

## Description

### Изменения

**ОБЩИЕ ИЗМЕНЕНИЯ**

- переработано соединение клиента с сервером, теперь клиент подключается **1 раз**
  и уже в 1 подключении работает с сервером

- добавлена многопоточная обработка сервером соединений
  (иными словами теперь к серверу **может подключиться несколько клиентов одновременно**, раньше только 1)

- добавлено ограничение на количество подключений к
  серверу `ru.itmo.socket.common.util.ConnectionContext.MAX_CONNECTIONS`

- добавлена авторизация и регистрация + теперь можно выполнять команды только если пользователь залогинен!

- неавторизованным можно выполнить только `login/register/exit`

- убраны disconnect, save (и подгрузка из
  XML [XmlCollectionLoader](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fmanager%2FXmlCollectionLoader.java))
  (ВСЕ помеченное `@Deprecated` - устарело и не должно использоваться)

- в `pom.xml` у `client-socket` и `server-socket` добавлен build чтобы собирался полноценный `.jar` со всеми
  зависимостями
  (это нужно чтобы запустить несколько клиентов, не смотрел можно ли в IntelliJ так сделать, я делал через терминал:
  просто собираешь `.jar` запустив команду `mvn clean install` в корне проекта `lab7` и у тебя в папках `target`
  у `client-socket` и `server-socket` появляются готовые `.jar` файлы, нас
  инитересует [client-socket.jar](client-socket%2Ftarget%2Fclient-socket.jar) - мы стартуем сервер через IntelliJ, а
  клиентов много же должно быть, открываем 3 консоли и запускаем `.jar` через
  консоль `java -jar client-socket/target/client-socket.jar` и получается с 3 консолей у вас 3 клиента подключились)

Хотя думаю можно и через IntelliJ несколько client запустить, чтобы не париться с `.jar`, если хотите - попробуйте
прогуглить, думаю способ есть

**CLIENT**

**SERVER**

- переработан модуль взаимодействия с коллекцией  
  ([LabWorkTreeSetManager](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fmanager%2FLabWorkTreeSetManager.java)
  чтобы поток вначале сохранял в БД а потом менял коллекцию)

- добавлено взаимодействие с БД + подгрузка ранее сохраненных данных из БД

- добавлена логика многопоточности
  (теперь в сервере каждый клиент обслуживается в отдельном потоке)

### Новые классы

**COMMON**
[UserDto.java](common-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fcommon%2Fdto%2FUserDto.java) - объект для
авторизации пользователя, в нем пересылается логин пароль между client и server

* этот же класс используется чтобы из БД вытянуть данные, это конечно залипуха, но переписывать уже не хочется :)
  (по хорошему надо для БД отдельную сущность, DTO - data transfer object только для пересылок между client и server)

**CLIENT**

* [SecurityUtil.java](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2Futil%2FSecurityUtil.java) -
  класс для шифрования пароля (пароль тупо шифруется на client и так и кладется в БД, при login сравнивается)

**SERVER**

* [db](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb) - взаимодействие с БД (для всех сущностей)
* [concurrent](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent) - многопоточка

### Описание новых классов

### Изменения в старых классах

**CLIENT**

* [SecurityUtil.java](client-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fclient%2Futil%2FSecurityUtil.java) -
  просто шифрование пароля и все, как написано выше чисто шифруем и шифрованный отправляем на сервер, там и сравниваем
  ничего не дешифруем

**SERVER**
ща пойдет жара (не пугайтесь, просто долго расписывать и логика не очень простая)

1) пакет [db](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb) для работы с БД

- [DatabaseConfig](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2FDatabaseConfig.java) - класс
  для подключения к БД через JDBC, здесь метод для создания Connection (подключения к БД)
- [DatabaseInitializer](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2FDatabaseInitializer.java) -
  класс для инициализации БД (создания таблиц ваших сущностей при старте сервера и подключении к БД)
- [dao](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fdb%2Fdao) - тут лежат классы которые
  взаимодействуют с БД
  Важно понимать что большинство dao написаны так чтобы с ними работал ТОЛЬКО поток клиента

Разберем пример:

Перед вами 2 метода с одинаковым названием - **перегрузка метода**
1 - тут **нет** в параметрах Connection connection

```java
    public Person findById(int id) {
    return findById(DbUserContext.getConnection(), id);
}
```

2 - тут **есть** в параметрах Connection connection

```java

public Person findById(Connection connection, int id) {
    String sql = "SELECT name, birthday, height, weight, eye_color FROM persons WHERE id = ?";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        // code
    }
}
```

3 - во многих методах просто напрямую пишется так

```java
PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql);
```

- `PreparedStatement` - класс java для взамодействия с БД (написан не мной)
- `DbUserContext.getConnection()` - метод написанный мной для получения текущего подключения конкретного клиента
- `.prepareStatement(sql)` - это метод который вызывается у `Connection` (тоже класс java написанный не мной), для
  создания объекта который поможет написать запрос в БД

"че за фигня?" спросите вы, отвечу я вам - перегрузка нужна чтобы можно было вызвать метод findById **не только из
потока client**, а просто передав ему Connection созданный например вручную а не тот что уже лежит у client
в `DbUserContext`

`DbUserContext.getConnection()` достает connection из `ThreadLocal<Connection>` - это **класс который дает каждому
потоку свою копию переменной**

остановитесь тут на секунду, перечитайте еще раз

что это значит? давайте глянем в `DbUserContext`

```java
public class DbUserContext {
    private static final ThreadLocal<Connection> имяПеременной = ...;
```

переменная **static**!! НО для каждого потока туда кладется **свое значение**

то есть у вас есть поток 1 и поток 2, они работают параллельно (одновременно)

и поток 1 кладет в static ну скажем "banana" (предположим что у нас `ThreadLocal<String>`)
а поток 2 кладет "apple" - тогда при обращении к static полю в потоке 1 будет "banana" а в потоке 2 - "apple"

зачем такие сложности? - все просто, это для легкого доступа к переменным потока, представляете сколько пришлось бы
написать кода если бы connection к БД передавался в конструкторе, его бы пришлось каждый раз передавать в dao (где
выполняется запрос), это неудобно, ниже когда про многопоточку будем говорить расскажу еще с одним примером чтобы было
понятно

2) пакет [concurrent](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent) - для работы с
   многопоточностью

многопоточка и в Server классе есть, просто в этом пакете собраны "инстурменты" которые используются в других частях
приложения

- [ProcessClientTask](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent%2FProcessClientTask.java)

Начнем с класса который является реализацией Runnable - **в методе `run()`** находится код, который и **запускается в
отдельном потоке для каждого клиента**. Запомните 1 поток = 1 клиент подключенный к серверу

Рассмотрим его подробнее:

```java

@Override
public void run() {
    try {
        initThread();
        processConnection();
    } catch (Exception e) {
        System.err.println("Ошибка сервера: " + e.getMessage());
        e.printStackTrace();
    } finally {
        destroyThread();
    }
}
```

Вначале вызывается метод `initThread();` - в нем инициализируется все что нужно для потока, то есть поток запоминает, а
какой у него клиент, а он авторизован? а какой у него login,...

`processConnection();` - старый код Server который обрабатывает команды там только единственное добавилась проверка на
то что пользователь авторизован **И ВСЕ, больше особо ничего нового**

а когда поток завершает работу - `destroyThread();` - мы должны очистить контекст потока **ВСЕГДА**. так как 1 поток = 1
клиент НО потоки переиспользуются, значит в потоках не должно оставатьс старой инфы о старом пользователе

Почему так? Потому что
потоки внутри `ThreadPool` (он же `ExecutorService` он же `ForkJoinPool`, `CachedThreadPool`, `FixedThreadPool`,...
это все реализации интерфейса `ExecutorService` отличаются они тем, что каждый для своей задачи более удобен, тут лучше
спросить chatGPT он подробно расскажет что для чего) **переиспользуются** то есть допустим у вас fixedThreadPool(7) -
это значит внутри executorService будет 7 потоков, которые будут переиспользоваться, вы
делаете `threadPool.submit(Runnable)` - говоря вашему pool: "вот тебе задача, делай" а он подхватывает ее и один из 7
потоков ее делает, если потоки кончились то задача уходит в ожидание и как только поток освобождается то освободившийся
берет новую задачу!

А как собственно это происходит а вот так, щагляните в
код [Server](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2FServer.java)

```java
    forkJoinPool.submit(new ProcessClientTask(clientSocket, AVAILABLE_IDS, CONNECTION_COUNTER));
```

именно в этом коде мы приняли в бесконечном цикле подключение от клиента и отправили в pool новую задачу.

То есть основной поток (наше приложение) бесконечно принимает подключения, если лимит подключений не исчерпан то создает
задачу которую выполняет `forkJoinPool` она выполняется в отдельном потоке, это тоже бесконечный цикл прослушивания
команд клиента, пока тот не вызовет Exit

вот так кратко работает программа

Еще наверно не понятно а как задача собственно уходит в отдельный поток, наша задача `ProcessClientTask` - класс
написанный нами реализует интерфейс `Runnable` в котором 1 метод - `run()` он под капотом и вызывается где-то в
недрах `forkJoinPool` в новом потоке вот как наша задача и наш непосредственный код попадает в новый поток! ( то есть мы
реализовали `Runnable` и внутри метода `run()` находится код котрый запускается в отдельном потоке)

А теперь поговорим подробнее про контекст потока (клиента: 1 клиент = 1 поток)

- [DbUserContext](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent%2FDbUserContext.java)
- [UserContext](server-socket%2Fsrc%2Fmain%2Fjava%2Fru%2Fitmo%2Fsocket%2Fserver%2Fconcurrent%2FUserContext.java)

Это 2 класса, которые отображают контекст клиента, в UserContext мы храним общую инфу о пользователе типо его clientId и
login,
а в DbUserContext храним только его Connection с БД, как уже объяснено выше через ThreadLocal

Рассмотрим подробнее 2 метода: `initUserContext` и `destroyUserContext`

```java
 public static void initUserContext(int clientId) {
    try {
        // connect user to db
        DbUserContext.connectToDb();
        CLIENT_ID.set(clientId);
        LOGIN.set("unauthorized_" + clientId);
        AUTHORIZED.set(false);
        DB_USER_ID.set(null);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public static void destroyUserContext() {
    try {
        DbUserContext.disconnect();
        CLIENT_ID.remove();
        LOGIN.remove();
        AUTHORIZED.remove();
        DB_USER_ID.remove();
    } catch (SQLException ignore) {
    }
}

```

что мы в них видим - ~~ничего аххахаха~~. Мы видим в них то что когда к нам подключается клиент то у него
инициализируется контекст, запомните - **каждый клиент это отдельный поток** то есть **1 клиент = 1 поток**

детальнее:

1) `DbUserContext.connectToDb();` - создаем connection к БД для конкретного потока (клиента)
2) `CLIENT_ID.set(clientId);` - генерируем ему номер (типо талончик **ЭТО НЕ НОМЕР ПОЛЬЗОВАТЕЛЯ В БД**, это чисто
   талончик, номер пользователя в системе по номеру подключения, поделючился первым - первый, вторым - второй и т.д.)
3) `LOGIN.set("unauthorized_" + clientId);` - вот тут как раз используем client_id для создания default login пока
   пользователь не авторизован
4) `AUTHORIZED.set(false);` - инфа о том, авторизован ли пользователь (по умолчанию не авторизован)
5) `DB_USER_ID.set(null);` - **id пользователя в БД** пока не авторизовались, естественно его нет

То есть мы при создании пользователя инициализируем static переменные НО не забывайте что у каждого потока, хоть они и
static они будут уникальны - свои для каждого потока

а потом они же подчищаются чтобы поток, который мы переиспользуем мог взять нового клиента и у клиента не было бы данных
пользователя, которые остались от предыдущего который обслуживался в этом потоке



3 модуля

1) common-socket - тут лежат общие классы / сущности что будут передаваться между клиентом и сервером
2) server-socket - тут код сервера (к нему подключаются клиенты)
3) client-socket - тут код клиента, они подключаются к серверу

```shell
    mvn clean install
```

`Ctrl + Shft + F` - find in files