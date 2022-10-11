package com.ponkratov.beerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(R.layout.activity_main) {
}


/*
* Используя 3 домашнюю работу добавить поддержку (Coroutines/Flow). Необходимо использовать паттерн MVVM.
* Для предоставления зависимостей для вью моделей используем паттерн ServiceLocator.

Опционально:
- Добавление на экраны LCE состояния
- Добавление кеширования через Room (грузите данные в цепочке и потом переводите в бд,
* при открытии списка если есть данные с бд то показываем сначала их, а потом уже свежие из сети)
* */