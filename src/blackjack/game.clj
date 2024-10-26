(ns blackjack.game
  (:require [card-ascii-art.core :as card]))

(defn new-card []
  "Gerando uma carta entre 1 e 13"
  (inc (rand-int 13)))

; vai calcular os pontos de acordo com as cartas
; J, Q, K = 10 (nao 11, 12 e 13)
; [A 10] = 11 ou 21 = 21
; [A 5 7] = 1+5+7 (13) ou 11+5+7 (23)
; A = 11 porém se passar de 21, ele vai valer 1

; (reduce + [2 3 4) 1)
; (+ 1 2) = 3
; (+ 3 3) = 6
; (+ 6 4) = 10

(defn JQK->10 [card]
  (if (> card 10) 10 card))

(defn A->11 [card]
  (if (= card 1) 11 card))

;vai calcular os pontos de acordo com as cartas
(defn points-cards [cards]
  (let [cards-witout-JQK (map JQK->10 cards)
        cards-with-A11 (map A->11 cards-witout-JQK)
        points-with-A-1 (reduce + cards-witout-JQK)
        points-with-A-11 (reduce + cards-with-A11)]
    (if (> points-with-A-11 21) points-with-A-1 points-with-A-11)))
(points-cards [1 10])
(points-cards [1 5 7])
;como representar um jogadot
;{:player "Rodrigo"'
; :cards [3 4]}
(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)
        cards [card1 card2]
        points (points-cards cards)]
    {:player-name player-name
     :cards  cards
     :points points}))

; chamar a funcao new-card para gerar a nova carta
; atualizar o vetor cards dentro do player com a nova carta
; calcular os pontos do jogador com o novo vetor de cartas
; retornar esse novo jogador
(defn more-card [player]
  (let [card (new-card)
        cards (conj (:cards player) card)
        new-player (update player :cards conj card)
        points (points-cards cards)]
    (assoc new-player :points points)))

(defn player-decision-continue? []
  (= (read-line) "sim"))

; chamar a funcao new-card para gerar a nova carta
; atualizar o vetor cards dentro do player com a nova carta
; calcular os pontos do jogador com o novo vetor de cartas
; retornar esse novo jogador
(defn game [player]
  (println (:player-name player) ": mais cartas")
  (if (player-decision-continue?)
    (let [player-with-more-cards (more-card player)]
      (card/print-player player-with-more-cards)
      (game player-with-more-cards))
    player))

(def player-1 (player "Rodrigo"))
(card/print-player  player-1)

(def hook (player "Hook"))
(card/print-player  hook)
(game player-1)
(game hook)