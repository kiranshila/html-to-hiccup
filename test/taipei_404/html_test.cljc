(ns taipei-404.html-test
  (:require [clojure.test :refer [deftest testing is are]]
            [taipei-404.html :refer [html->hiccup]]))

(deftest html->hiccup-test
  (testing "the basic usage"
    (are [html hiccup]
      (= (html->hiccup html) hiccup)

      ;; Obligatory test
      "<p>hello, world</p>"
      '([:p "hello, world"])

      ;; Nested elements
      "<p><ol><li>a</li><li>b</li><li>c</li></ol></p>"
      '([:p
         [:ol
          [:li "a"]
          [:li "b"]
          [:li "c"]]])

      ;; Sequence of html tags separated by texts
      "a<p/>b<img/>c"
      '("a" [:p] "b" [:img] "c")

      ;; Void elements
      "<br><hr>"
      '([:br] [:hr])

      ;; Void element with attributes
      "<img hidden src=\"flower.jpg\" alt=\"A flower\" width=\"500\" height=\"600\" style=\"border:5px solid black\">"
      '([:img {:hidden true
               :src    "flower.jpg"
               :alt    "A flower"
               :width  "500"
               :height "600"
               :style  "border:5px solid black"}])

      ;; That thing that goes before the html element
      "<!doctype html>"
      '([:!doctype {:html true}])))

  (testing "the :discard-comments option"
    (is (= (html->hiccup "<!--Some useful comment--><p><!--Here also--></p>"
                         {:comment-keyword :!--})
           '([:!-- "Some useful comment"] [:p [:!-- "Here also"]])))
    (is (= (html->hiccup "<!--Some useful comment--><p><!--Here also--></p>"
                         {:comment-keyword nil})
          '([:p])))))
