/**
 * Created by vraj on 11/19/15.
 */
package main.java;

import java.io.Serializable;

public class SparkMain implements Serializable {

//    private static final FlatMapFunction<String, String> WORDS_EXTRACTOR = new FlatMapFunction<String, String>() {
//        public Iterable<String> call(String s) throws Exception {
//            return Arrays.asList(s.split(" "));
//        }
//    };
//
//    private static final PairFunction<String, String, Integer> WORDS_MAPPER = new PairFunction<String, String, Integer>() {
//        public Tuple2<String, Integer> call(String s) throws Exception {
//            return new Tuple2<String, Integer>(s, 1);
//        }
//    };
//
//    private static final Function2<Integer, Integer, Integer> WORDS_REDUCER = new Function2<Integer, Integer, Integer>() {
//        public Integer call(Integer a, Integer b) throws Exception {
//            return a + b;
//        }
//    };

    public static void main(String[] args) throws Exception {

        DatasetConfig dc = new DatasetConfig();
        LuceneUtils lu = new LuceneUtils();

        dc.loadProperties();

        lu.generateReviewTipIndex(dc);
    }


}
