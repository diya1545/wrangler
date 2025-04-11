package io.cdap.wrangler.directives;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

  @Test
  public void testAggregateStatsDirective() throws Exception {
    List<Row> inputRows = Arrays.asList(
      new Row("size", "1MB").add("time", "1s"),
      new Row("size", "2MB").add("time", "2000ms")
    );

    String[] recipe = {
      "aggregate-stats :size :time :total_mb :total_sec"
    };

    List<Row> result = TestingRig.execute(recipe, inputRows);

    Assert.assertEquals(1, result.size());
    Row output = result.get(0);

    Assert.assertEquals(3.0, (Double) output.getValue("total_mb"), 0.01);
    Assert.assertEquals(3.0, (Double) output.getValue("total_sec"), 0.01);
  }
}
