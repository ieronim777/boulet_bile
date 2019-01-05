(ns prism-break.views.html
  (:require [yegortimoshenko.unstable.data.html :as html :refer [html]]))

(defn category [here there text]
  (if (= here there)
    [:a.active text]
    [:a {:href there} text]))

(defn layout [{:keys [content description disclaimer title]}]
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:title title]
    [:meta {:name :viewport :content "width=device-width,initial-scale=1"}]
    [:meta {:name :description :content description}]
    [:meta {:name :referrer :content "no-referrer"}]
    [:link {:rel :stylesheet :href "style.css"}]
    [:link {:rel :icon :href "favicon.png"}]]
   [:body {:class #{:todo}}
    [:div {:id :header}
     [:a {:href "#TODO" :id :brand}
      [:strong "PRISM"]
      [:span {:class #{:lightning}} "⚡"]
      [:strong "Break"]]
     [:div {:class #{:menu}}
      [:nav {:class #{:nav-all}}
       (category "#TODO" "all" "Projects")]
      [:nav {:class #{:nav-platforms}}
       (category "#TODO" "platforms" "Platforms")]
      [:nav {:class #{:nav-standards}}
       (category "#TODO" "standards" "Standards")]
      [:nav {:class #{:nav-about}}
       (category "#TODO" "about" "About")]]]
    [:div {:id :container} content]
    [:div {:id :footer}
     [:div {:class #{:content-side}}
      [:div {:class #{:text-block}}
       [:p {:class #{:disclaimer}} disclaimer]]]
     [:div {:class #{:content-main}}
      [:ul {:class #{:languages}}
       (for [locale [:en/US]]
         [:li [:a {:href "#TODO"} "English"]])]]]]])

(defn render [data]
  (html/write-string (html (layout data))))
