Запуск с отдельными компонентами JavaFX
```cmd
java  --module-path "target\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar target/combining-rent-receipts-javafx-1.0-SNAPSHOT.jar
```

Запуск jar
```cmd
java -jar target/combining-rent-receipts-javafx-1.0-SNAPSHOT.jar
```

Сборка проекта в Fat-JAR
```cmd
mvn package
```

Сборка проекта в Fat-JAR с очисткой папки
```cmd
mvn clean package
```
