package examples.io.vertx.reactivex.database.jdbc;

import examples.io.vertx.util.Runner;
import io.reactivex.Single;
import io.reactivex.exceptions.CompositeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

/*
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class Transaction extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Transaction.class);
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");

    String sql = "CREATE TABLE colors (" +
      "id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
      "name VARCHAR(255), " +
      "datetime TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL)";

    JDBCClient client = JDBCClient.createShared(vertx, config);

    // Connect to the database
    client
      .rxGetConnection()
      .flatMap(conn ->
        conn
          // Disable auto commit to handle transaction manually
          .rxSetAutoCommit(false)
          // Switch from Completable to default Single value
          .toSingleDefault(false)
          // Create table
          .flatMap(autoCommit -> conn.rxExecute(sql).toSingleDefault(true))
          // Insert colors
          .flatMap(executed -> conn.rxUpdateWithParams("INSERT INTO colors (name) VALUES (?)", new JsonArray().add("BLACK")))
          .flatMap(updateResult -> conn.rxUpdateWithParams("INSERT INTO colors (name) VALUES (?)", new JsonArray().add("WHITE")))
          .flatMap(updateResult -> conn.rxUpdateWithParams("INSERT INTO colors (name) VALUES (?)", new JsonArray().add("PURPLE")))
          // commit if all succeeded
          .flatMap(updateResult -> conn.rxCommit().toSingleDefault(true).map(commit -> updateResult))
          // Rollback if any failed with exception propagation
          .onErrorResumeNext(ex -> conn.rxRollback()
            .toSingleDefault(true)
            .onErrorResumeNext(ex2 -> Single.error(new CompositeException(ex, ex2)))
            .flatMap(ignore -> Single.error(ex))
          )
          // Get colors if all succeeded
          .flatMap(updateResult -> conn.rxQuery("SELECT * FROM colors"))
          // close the connection regardless succeeded or failed
          .doAfterTerminate(conn::close)
      ).subscribe(resultSet -> {
      // Subscribe to get the final result
      System.out.println("Results : " + resultSet.getRows());
    }, Throwable::printStackTrace);
  }
}
