;---
; Excerpted from "Web Development with Clojure, Second Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/dswdcloj2 for more book information.
;---
(ns swagger-service.routes.services_default
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]))

(s/defschema Thingie {:id Long
                      :hot Boolean
                      :tag (s/enum :kikka :kukka)
                      :chief [{:name String
                               :type #{{:id String}}}]})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  ;JSON docs available at the /swagger.json route
  (context "/api" []
           :tags ["thingie"]

     (GET "/plus" []
          :return       Long
          :query-params [x :- Long, {y :- Long 1}]
          :summary      "x+y with query-parameters. y defaults to 1."
          (ok (+ x y)))

     (POST "/minus" []
           :return      Long
           :body-params [x :- Long, y :- Long]
           :summary     "x-y with body-parameters."
           (ok (- x y)))

     (GET "/times/:x/:y" []
          :return      Long
          :path-params [x :- Long, y :- Long]
          :summary     "x*y with path-parameters"
          (ok (* x y)))

     (POST "/divide" []
           :return      Double
           :form-params [x :- Long, y :- Long]
           :summary     "x/y with form-parameters"
           (ok (/ x y)))

     (GET "/power" []
          :return      Long
          :header-params [x :- Long, y :- Long]
          :summary     "x^y with header-parameters"
          (ok (long (Math/pow x y))))

     (PUT "/echo" []
          :return   [{:hot Boolean}]
          :body     [body [{:hot Boolean}]]
          :summary  "echoes a vector of anonymous hotties"
          (ok body))

     (POST "/echo" []
           :return   (s/maybe Thingie)
           :body     [thingie (s/maybe Thingie)]
           :summary  "echoes a Thingie from json-body"
           (ok thingie)))

  (context "/context" []
           :tags ["context"]
           :summary "summary inherited from context"
           (context "/:kikka" []
                    :path-params [kikka :- s/Str]
                    :query-params [kukka :- s/Str]
                    (GET "/:kakka" []
                         :path-params [kakka :- s/Str]
                         (ok {:kikka kikka
                              :kukka kukka
                              :kakka kakka})))))
