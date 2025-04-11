package io.cdap.wrangler;

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class TokenParsingTest {

  @Test
  public void testByteSizeParsing() {
    Assert.assertEquals(1024, new ByteSize("1KB").getBytes());
    Assert.assertEquals(1572864, new ByteSize("1.5MB").getBytes());
    Assert.assertEquals(1, new ByteSize("1B").getBytes());
  }

  @Test
  public void testTimeDurationParsing() {
    Assert.assertEquals(1000, new TimeDuration("1s").getMillis());
    Assert.assertEquals(60000, new TimeDuration("1m").getMillis());
    Assert.assertEquals(250, new TimeDuration("250ms").getMillis());
  }
}
