(ns fastmath-clustering.distance
  (:import [smile.math.distance Distance EuclideanDistance ChebyshevDistance CorrelationDistance
            JaccardDistance JensenShannonDistance ManhattanDistance EditDistance
            MahalanobisDistance MinkowskiDistance]
           [clojure.lang IFn]))

(defn ->smile-distance
  "Convert any Clojure function to a SMILE distance object"
  [distance]
  (reify
    IFn (invoke [_ x y] (distance x y))
    Distance (d [_ x y] (distance x y))))

(def ^:private double-array-type (Class/forName "[D"))
(def ^:private double-double-array-type (Class/forName "[[D"))

(defn seq->double-array
  "Convert sequence to double array. Returns input if `vs` is double array already."
  ^doubles [vs]
  (cond
    (= (type vs) double-array-type) vs
    (nil? vs) nil
    (seqable? vs) (double-array vs)
    :else (let [arr (double-array 1)] 
            (aset arr 0 (double vs))
            arr)))

(defn seq->double-double-array
  "Convert sequence to double-array of double-arrays.
  
  If sequence is double-array of double-arrays returns `vss`"
  #^"[[D" [vss]
  (cond 
    (= (type vss) double-double-array-type) vss
    (nil? vss) nil
    :else (into-array (map seq->double-array vss))))

(defn wrap-smile
  "Make SMILE object invocable from Clojure as function."
  [^Distance distance]
  (reify
    IFn (invoke [_ x y] (.d distance (seq->double-array x) (seq->double-array y)))
    Distance (d [_ x y] (.d distance (seq->double-array x) (seq->double-array y)))))

(def ^{:doc "Euclidean distance"} euclidean (wrap-smile (EuclideanDistance.)))
(def ^{:doc "Chebyshev distance"} chebyshev (wrap-smile (ChebyshevDistance.)))
(def ^{:doc "Manhattan distance"} manhattan (wrap-smile (ManhattanDistance.)))
(def ^{:doc "Jensen-Shannon distance"} jensen-shannon (wrap-smile (JensenShannonDistance.)))
(def ^{:doc "Jaccard distance"} jaccard (wrap-smile (JaccardDistance.)))
(def ^{:doc "Pearson correlation distance"} pearson (wrap-smile   (CorrelationDistance. "pearson")))
(def ^{:doc "Spearman correlation distance"} spearman (wrap-smile (CorrelationDistance. "spearman")))
(def ^{:doc "Kendall correlation distance"} kendall (wrap-smile   (CorrelationDistance. "kendall")))

(defn ->edit
  "Create Edit distance function."
  ([] (wrap-smile (EditDistance.)))
  ([^long max-string-length] (wrap-smile (EditDistance. (unchecked-int max-string-length))))
  ([^long max-string-length damerau?] (wrap-smile (EditDistance. (unchecked-int max-string-length)
                                                                 (boolean damerau?)))))


(defn ->mahalanobis
  "Create Mahalanobis distance function. Covariance should be seq of seqs (or double double array)"
  ([covariance] (wrap-smile (MahalanobisDistance. (seq->double-double-array covariance)))))

(defn ->minkowski
  "Create Minkowski distance"
  ([^long p] (wrap-smile (MinkowskiDistance. (unchecked-inc p))))
  ([^long p weights] (wrap-smile (MinkowskiDistance. (unchecked-inc p) (seq->double-array weights)))))
