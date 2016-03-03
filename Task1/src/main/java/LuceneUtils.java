package main.java;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by vraj on 11/21/15.
 */
public class LuceneUtils {
    public static final int INDEX_BUSINESS_ID = 0;
    public static final int INDEX_REVIEW_TEXT = 1;
    public static final int INDEX_TIP_TEXT = 2;
    public static final int INDEX_CATEGORIES = 3;

    static Directory directory = null;

    static Analyzer analyzer;
    static IndexWriterConfig config;
    static IndexWriter iwriter = null;

    public LuceneUtils() {
        analyzer = new StandardAnalyzer();
        config = new IndexWriterConfig(analyzer);
    }

    public static void generateReviewTipIndex(DatasetConfig dc) throws IOException {
        // Create object of Spark conf and context
        SparkConf conf = new SparkConf().setAppName("org.yelpchallenge.index").setMaster("local");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaSQLContext sqlContext = new JavaSQLContext(context);

        directory = FSDirectory.open(Paths.get(dc.getIndexDir()));
        iwriter = new IndexWriter(directory, config);

        JavaSchemaRDD businessSchema = sqlContext.jsonFile(dc.getBusinessJSON());
        JavaSchemaRDD reviewSchema = sqlContext.jsonFile(dc.getReviewJSON());
        JavaSchemaRDD tipSchema = sqlContext.jsonFile(dc.getTipJSON());

        businessSchema.registerTempTable("business");
        reviewSchema.registerTempTable("review");
        tipSchema.registerTempTable("tip");

        JavaSchemaRDD rows = sqlContext.sql("SELECT business.business_id, review.text, tip.text, business.categories FROM business INNER JOIN review INNER JOIN tip " +
                "where business.business_id = review.business_id and business.business_id = tip.business_id");

        rows.foreach(new VoidFunction<Row>() {
            public void call(Row row) throws Exception {
                createIndex(row, directory, iwriter);
            }
        });
        iwriter.commit();
        iwriter.close();
    }

    public static void createIndex(Row row, Directory directory, IndexWriter iwriter) {
        try {

            Document doc = new Document();

            doc.add(new StringField("ID", row.getString(INDEX_BUSINESS_ID), Field.Store.YES));
            doc.add(new StringField("REVIEW", row.getString(INDEX_REVIEW_TEXT), Field.Store.YES));
            doc.add(new StringField("TIP", row.getString(INDEX_TIP_TEXT), Field.Store.YES));
            List<String> categories = (List<String>) row.get(INDEX_CATEGORIES);

            for (String category : categories) {
//                System.out.println(category);
                doc.add(new StringField("CATEGORY", category, Field.Store.YES));
            }
            iwriter.addDocument(doc);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
