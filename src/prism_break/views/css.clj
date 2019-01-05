(ns prism-break.views.css
  (:require [garden.core :refer [css]]
            [garden.color :refer [hsl]]
            [garden.compression :refer [compress-stylesheet]]
            [garden.units :refer [defunit em px]]))

(defunit %) ;; TODO: upstream to https://github.com/noprompt/garden

(def colors
  {:anchor (hsl 205 100 37)
   :dimmed (hsl 0 0 40)
   :hovered (hsl 205 95 35)
   :normal (hsl 0 0 20)})

(def clearfix
  "Port of Nib's magnificent micro clearfix: https://git.io/fhU0L"
  [[:&:before :&:after {:content "" :display :table}]
   [:&:after {:clear :both}]])

(def reset
  [:* :*:before :*:after
   {:box-sizing :border-box
    :margin 0
    :padding 0}])

(def header
  [:#header {:border-bottom "1px solid #ddd"
             :background "#fff"
             :height "44px"}
            clearfix
   [:a#brand {:padding "0 .75em"
              :float :left
              :font "1em/43px sans-serif"
              :color (:normal colors)}
    [:&:hover {:color (:hovered colors)}]]
   [:.menu {:float :right}
    [:.nav {:float :left}
     [:a {:display :block
          :font ".8em/43px sans-serif"
          :padding "0 .9375em"}
      [:&:active {:color (:anchor colors)}]
      [:&.active {:color (:normal colors)
                  :cursor :default
                  :font-weight :bold}]
      [:&:hover {:color (:hovered colors)}]]]]])

(def footer
  [:#footer {:clear :both
             :font ".8em/1.5 sans-serif"
             :background :white ;; redundant?
             :padding "1em 0"
             :position :relative
             :z-index 100}
            clearfix
     [:ul.languages {:border-left "1px solid #ddd"
                     :border-top "1px solid #ddd"
                     :margin-bottom (em 1)}
                    clearfix
      [:li {:border-right "1px solid #ddd"
            :border-bottom "1px solid #ddd"
            :float :left
            :width (% 25)}
       [:a {:display :block
            :padding "0 .5em"
            :font "1em/2 sans-serif"}
        [:&:hover {:background (:hovered colors)
                   :color :white}]]]]
     [:.text-block {:padding-top 0
                    :font "1em/1.5 sans-serif"
                    :border-bottom :none}]
     [:p {:color (:dimmed colors)
          :font "italic .9em/1.5 sans-serif"
          :padding-right (em 1.5)}]])

(def style
  [reset
   [:html {:background :white}]
   [:body {:background :white
           :color (:normal colors)
           :direction :ltr
           :font-size (px 16)
           :line-height 1.5
           :font-family :sans-serif
           :margin "0 auto"
           :max-width (px 1040)}]
   [:.ltr {:display :block}]
   [:.rtl {:display :none}]
   [:.filler-columns-two
    :.filler-columns-three {:display :none}]
   [:.area {:margin-bottom (em 2)}
           clearfix
    [:.section clearfix
     [:&.empty {:display :none}]]]
   [:.content-main {:display :block}]
   [:.content-side {:display :none}]
   [:body.root.index
    [:.content-side {:display :block}]]
   [:#container clearfix]
   header
   footer])

(def stylesheet
  (compress-stylesheet (css style)))
