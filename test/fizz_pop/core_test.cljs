(ns fizz-pop.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [fizz-pop.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 2 2))))
