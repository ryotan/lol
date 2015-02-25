Chapter 2
============

グローバルな変数
-----------------------

グローバルな変数には、耳あて(earmuff)をつける。たぶん、Clojureではdynamicで動的束縛可能な変数ってやつのことだと思う。

```clojure
(def ^:dynamic *status* (atom {}))
```

変数をプライベートにするには、こんな感じにする？らしい。

```clojure
(def ^:private status (atom {}))
```

ローカル関数の定義
-----------------------

ローカル関数は `letfn` で定義できる。これは、Common Lispで言うところの、
`label` 的な雰囲気のやつで、同じ `letfn` のスコープで定義されている別の関数を参照できる。

```clojure
(letfn [(a [] 1)
        (b [] (dec (a)))]
  (b)) ;=> 0
```
