# Арбитражный бот

> Криптовалютная биржа - это элемент инфраструктуры валютного рынка, деятельность которой состоит в предоставлении услуг по организации и проведению торгов, в ходе которых участники заключают сделки с криптовалютой.

#### Ордер
Для заключение сделки продажи или покупки необходимо открыть биржевую заявку - **ордер**. 

Ордер может быть описан следующими параметрами: пара, стоимость, объем, тип операции. Например:

| Пара | Стоимость | Объем | Тип операции |
| --- | --- | --- | --- |
| **BTC_ETH** | 1 | 0.5 | Продажа |

#### Биржевой стакан
Если вы откроете [страницу торгов](https://poloniex.com/exchange#btc_et) на бирже Poloniex, то увидите ордера на продажу - **SELL ORDERS** и ордера на покупку - **BUY-ORDERS**, которые выставляют трейдеры. Данные таблицы называются **биржевым стаканом** и отображают информацию о том сколько и за какую цену хотят *купить / продать* валюту.

#### Арбитражная ситуация
Арбитражная ситуация - это ситуация, при которой за счет разницы в оценке актива (в данном случае валюты) трейдер может получить прибыль.

Например на бирже [Poloniex](https://poloniex.com/) есть три пары валют: [BTC](https://ru.wikipedia.org/wiki/%D0%91%D0%B8%D1%82%D0%BA%D0%BE%D0%B9%D0%BD), [BCH](https://ru.wikipedia.org/wiki/Bitcoin_Cash), [ETH](https://ru.wikipedia.org/wiki/Ethereum).
И представим, что в биржевом стакане были созданы следующие ордера:

| Пара | Стоймость | Объем | Тип операции |
| --- | --- | --- | --- |
| **BTC_ETH** | 0.1 | 10 | Продажа |
| **BTC_BCH** | 0.2 | 8 | Покупка |
| **BTH_ETH** | 0.8 | 8 | Покупка |

Данная таблица демонстрирует арбитражную ситуацию внутри биржы. Если вы выполните ордера, выставленные трейдерами (совершите с ними сделку) : 
 
* 1 **BTC** => 10 **ETH**
* 10 **ETH** => 8 **BCH**
* 8 **BCH** => 1.6 **BTC**

то останетесь в прибыли на 0.6 **BTC**, так как изначально имели 1 **BTC**.
