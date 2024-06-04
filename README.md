# Задача 2 – ICGFilter

# [Презентация с фильтрами](/misc/Lecture_02.pdf)

## Необходимо создать приложение для обработки растровых изображений.
**Графический интерфейс программы** должен состоять из меню, панели инструментов и области для просмотра изображения. Граница области отображения должна быть нарисована пунктирной линией и видна всегда, в том числе и до загрузки изображения. Ее размер должен быть привязан к размеру окна, а на границах должны быть сделаны отступы не менее 4 пикселей.

![image](/misc/Screenshot_42.png "Примерный интерфейс")

Пользователь имеет **возможность открывать файлы изображений** (PNG, JPEG, BMP, GIF). Для чтения файлов разрешается использовать стандартные библиотеки. После загрузки выбранное изображение должно отображаться в области просмотра в режиме реального размера, то есть пиксель к пикселю. Если изображение оказывается меньше области просмотра, незанятая часть должна быть заполнена нейтральным цветом; если изображение оказывается больше – то изначально отображается его верхний левый угол и имеется возможность посмотреть остальную часть с помощью скроллов. Также должна иметься **возможность переключать режим отображения между «реальный размер» и «подогнать под экран».** Во втором случае изображение должно вписываться в область просмотра, сохраняя соотношения сторон.

На панели инструментов **должны присутствовать** кнопки открытия файла, изменения режима отображения и все реализованные инструменты обработки изображения.

При нажатии на какой-либо инструмент обработки пользователю в **диалоговом окне** предлагается задать параметры данного инструмента (если таковые имеются), затем результат обработки отображается **вместо изначального изображения** - если хочется не на исходное - **добавить кнопку** для возможности делать цепочки. При клике мыши по изображению или соответствующим пунктам меню (типа radio button) отображаемое изображение **должно меняться между результатом обработки и оригиналом**. Каждое последующее использование инструментов обработки применяется к **оригинальному** изображению. Пользователь имеет возможность сохранить текущий результат обработки изображения в файл в формате PNG.

# Критерии оценки задания
## Общие обязательные требования (для этой и других задач):
* Размер окна приложения должен быть ограничен снизу 640×480.
* Все функции кнопок, представленные на панели инструментов, должны быть продублированы элементами меню.
* Все кнопки на панели инструментов должны иметь всплывающие подсказки.
* Изменение параметров происходит в модальных диалоговых окнах. Должна присутствовать возможность отмены. Обязательно должна проверяться корректность введенных параметров. В случае ввода некорректных значений, приложение должно уведомить об этом пользователя и указать диапазон допустимых значений.
* Должна присутствовать кнопка «О программе», показывающая диалоговое окно с информацией об авторе и программе.
* Отсутствие необработанных исключений / падений приложения при работе

## Обязательные требования (на тройку):
* Приложение должно соответствовать приведенному описанию.
* Должны быть реализованы следующие инструменты обработки:
  - Перевод цветного изображения в черно-белое (оттенки серого).
  - Преобразование изображения в негативное (инверсия).
  - Сглаживающий фильтр по окну 3×3 или 5×5 по выбору пользователя (по Гауссу).
  - Фильтр повышения резкости.
  - Тиснение.
  - Гамма-коррекция. Значение параметра гамма ограничить от 0.1 до 10.
  - Фильтры выделения границ (операторы Робертса и Собеля). Выбирается параметр бинаризации.
  - Дизеринг алгоритмом Флойда-Стейнберга (все цвета квантуются на 3 значения: 0, 128, 255).
  - Упорядоченный дизеринг с матрицей 8×8 (все цвета квантуются на 2 значения: 0 и 255).
* Все инструменты обработки должны корректно работать и на краях изображения.
* Последние заданные значения параметров инструментов должны сохраняться и предлагаться по умолчанию при следующем использовании (только на время текущей сессии работы приложения).

## Дополнительные требования (на пятерку):
* Для всех диалогов настроек параметров использовать связку Slider + EditBox (где возможно).
* Реализовать следующие инструменты обработки:
  - Акварелизация.
  - Поворот изображения на произвольный угол. Значение параметра ограничить от -180 до +180 градусов. Поворот выполняется относительно центра изображения. Цвет фона – белый.
  - Для сглаживающего фильтра реализовать выбор размера окна от 3 до 11 (размер окна – нечетный). Для размера 7 и больше можно использовать сглаживание по среднему значению.
  - Для дизеринга алгоритмом Флойда-Стейнберга должен иметься выбор числа квантования для каждого цвета (красного, синего и зеленого). От 2 до 128.
  - Для алгоритма упорядоченного дизеринга должен иметься выбор числа квантования для каждого цвета. От 2 до 128. Размер матрицы должен определяться автоматически.
* Реализовать дополнительный инструмент на свой выбор (согласовать с преподавателем).
* В качестве альтернативы использованию скроллов требуется реализовать возможность перемещения по изображению с помощью мыши (зажимая и сдвигая).
* Возможность изменять размер окна (и соответственно размер области отображения).
* Для режима отображения «подогнать под экран» должна использоваться интерполяция, режим которой выбирается в специальном окне настроек, доступном в любой момент. По умолчанию должен быть установлен режим билинейной интерполяции.
