package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ByteSize implements Token {
  private final long bytes;
  private final String raw;

  public ByteSize(String value) {
    this.raw = value;
    this.bytes = parse(value);
  }

  private long parse(String value) {
    value = value.trim().toUpperCase();
    double number = Double.parseDouble(value.replaceAll("[^0-9.]", ""));

    if (value.endsWith("KB")) return (long) (number * 1024);
    if (value.endsWith("MB")) return (long) (number * 1024 * 1024);
    if (value.endsWith("GB")) return (long) (number * 1024 * 1024 * 1024);
    if (value.endsWith("B")) return (long) number;

    throw new IllegalArgumentException("Invalid byte size: " + value);
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(bytes);
  }
}
