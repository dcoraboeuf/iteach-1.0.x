package net.nemerosa.iteach.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;

import java.io.IOException;

public class MoneyDeserializer extends JsonDeserializer<Money> {

    @Override
    public Money deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String s = jp.readValueAs(String.class);
        return StringUtils.isNotBlank(s) ? Money.parse(s) : null;
    }
}
