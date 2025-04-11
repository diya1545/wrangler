package io.cdap.wrangler.directives.aggregates;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.Collections;
import java.util.List;

/**
 * Custom directive to aggregate byte size and time duration across rows.
 */
public class AggregateStats implements Directive {
  private String sizeColumn;
  private String timeColumn;
  private String outputSizeColumn;
  private String outputTimeColumn;

  @Override
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
    builder.define("sizeColumn", TokenType.COLUMN_NAME);
    builder.define("timeColumn", TokenType.COLUMN_NAME);
    builder.define("outputSizeColumn", TokenType.COLUMN_NAME);
    builder.define("outputTimeColumn", TokenType.COLUMN_NAME);
    return builder.build();
  }
  

  @Override
  public void initialize(Arguments arguments) {
    sizeColumn = ((ColumnName) arguments.value("sizeColumn")).value();
    timeColumn = ((ColumnName) arguments.value("timeColumn")).value();
    outputSizeColumn = ((ColumnName) arguments.value("outputSizeColumn")).value();
    outputTimeColumn = ((ColumnName) arguments.value("outputTimeColumn")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context) {
    long totalBytes = 0;
    long totalMillis = 0;

    for (Row row : rows) {
      Object sizeVal = row.getValue(sizeColumn);
      Object timeVal = row.getValue(timeColumn);

      if (sizeVal != null) {
        ByteSize size = new ByteSize(sizeVal.toString());
        totalBytes += size.getBytes();
      }

      if (timeVal != null) {
        TimeDuration time = new TimeDuration(timeVal.toString());
        totalMillis += time.getMillis();
      }
    }

    double totalSizeMB = totalBytes / (1024.0 * 1024.0);
    double totalTimeSec = totalMillis / 1000.0;

    Row result = new Row();
    result.add(outputSizeColumn, totalSizeMB);
    result.add(outputTimeColumn, totalTimeSec);

    return Collections.singletonList(result);
  }
}
