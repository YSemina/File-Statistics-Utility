### Доброго времени суток :)

Проект был упрощен по сравнению с исходным техническим заданием:
- основной фокус на корректном подсчете статистики
- добавлен формат вывода статистики

### Тестовый запуск

#### Пример 1: Вывод в формате PLAIN (по умолчанию)

```bash
./gradlew run --args "test_files"
```

#### Ожидаемый вывод:

```
FILE STATISTICS SUMMARY
Total files: 4
Total size: 100 bytes
Total lines: 7
Non-empty lines: 5
Comment lines: 1
```

#### Пример 2: Вывод в формате XML

```bash
./gradlew run --args "test_files --output=xml"
```

#### Ожидаемый вывод:

```
<?xml version="1.0" encoding="UTF-8"?>
<fileStatistics>
  <summary>
    <totalFiles>4</totalFiles>
    <totalSizeBytes>100</totalSizeBytes>
    <totalLines>7</totalLines>
    <totalNonEmptyLines>5</totalNonEmptyLines>
    <totalCommentLines>1</totalCommentLines>
  </summary>
</fileStatistics>
```

#### Пример 3: Вывод в формате JSON

```bash
./gradlew run --args "test_files --output=json"
```

#### Ожидаемый вывод:

```
{
  "summary": {
    "totalFiles": 4,
    "totalSizeBytes": 100,
    "totalLines": 7,
    "totalNonEmptyLines": 5,
    "totalCommentLines": 1
  }
}
```