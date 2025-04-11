package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
  private final long millis;
  private final String raw;

  public TimeDuration(String value) {
    this.raw = value;
    this.millis = parse(value);
  }

  private long parse(String value) {
    value = value.trim().toLowerCase();
    double number = Double.parseDouble(value.replaceAll("[^0-9.]", ""));

    if (value.endsWith("ms")) return (long) number;
    if (value.endsWith("s")) return (long) (number * 1000);
    if (value.endsWith("sec")) return (long) (number * 1000);
    if (value.endsWith("min")) return (long) (number * 60 * 1000);
    if (value.endsWith("h") || value.endsWith("hr") || value.endsWith("hour"))
      return (long) (number * 60 * 60 * 1000);

    throw new IllegalArgumentException("Invalid time duration: " + value);
  }

  public long getMillis() {
    return millis;
  }

  @Override
  public Object value() {
    return millis;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(millis);
  }
}
