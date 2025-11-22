Запуск с отдельными компонентами JavaFX
```java  --module-path "target\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar target/combining-rent-receipts-javafx-1.0-SNAPSHOT.jar```

Просто запуск jar
```java -jar target/combining-rent-receipts-javafx-1.0-SNAPSHOT.jar```

Сборка проекта с очисткой папки
```mvn clean package```
Сборка проекта
```mvn package```

```mvn install:install-file -Dfile="D:\PROGRAMMING\MY PROJECTS\Java\combining-rent-receipts-javafx\dependencies\combining-rent-receipts.jar" -DgroupId=ru.ech0p1ng.combiningRentReciepts -DartifactId=combining-rent-receipts -Dversion=1.0-SNAPSHOT -Dpackaging=jar```