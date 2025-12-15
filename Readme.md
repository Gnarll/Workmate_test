Notes during developing

- Actual api doesn't support pagination, therefore I delete paging library deps.
- Nor api provides with ids for items, that's super strange, it forces to autogenerate them in
  Room.
- Also I can't but mention reason of fetching detailed objects for screen, where they actually not
  required.
  One of your requirements was the entire app working without the internet. Making 2 queries (first
  for the list
  of not detailed items, and second for one detailed) would allow only the list working without the
  internet.
  also it seems unsafe fetching the item not by id, but by anything else.
- I could have used UseCases like in classic clean architecture, but i found them redundant in this
  case.
- Couldn't find emoji flag in api, so didn't implement field with it

- Расскажите про свой путь в андроид разработке
  Работал React Native разработчиком около 2 лет, затем пошел работать по распределению, решил
  углубить
  знания в мобильной разработке, начав с андройда, View система мне совершенно не понравилась, думал
  дроп,
  но Jetpack compose оказался великолепен, решил полностью перейти к андройд разработке, проходил
  курсы на
  developers.android, делаю свой pet-проект, придумывая таски для большего погружения в экосистему и
  активно ищу работу.
- Какие команды в Git использовали? Знакомы ли с понятием Git Flow?
  С гитом, как и с Git Flow знаком довольно неплохо: и благодаря знаниям с универа, и коммерческому
  опыту.
- Что знаете про HashMap / HashSet?
  Коллекции, для которых характерны быстрая вставка и получение значения, у HashMap уникальны ключи,
  у
  HashSet - значения, для HashMap стоит использовать иммутабельные переменные с валидным хеш-кодом.
- Какие основные вещи знаете про корутины?
  Это по сути легковесные виртуальные потоки, работающие конкурентно. Structured Concurrency -
  концепция, позволяющая
  выстраивать гибкие логические структуры из иерарзии корутин, имеющие возможность отменяться, когда
  это необходимо, либо игнорировать отмену при отмене сиблинга и т.д . Запустить их можно только
  внутри корутинскопа через launch (без результата) или async (с резулльтатом). В андройд есть набор
  Dispatchers, для логического разделения операций. 