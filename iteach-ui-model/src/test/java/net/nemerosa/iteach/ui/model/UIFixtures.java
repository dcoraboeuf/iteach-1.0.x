package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

import static net.nemerosa.iteach.common.json.JsonUtils.object;

public final class UIFixtures {

    private UIFixtures() {
    }

    public static ObjectNode jsonSchool() {
        return jsonSchool("EUR 45.00");
    }

    public static ObjectNode jsonSchool(String hourlyRate) {
        return object()
                .with("id", 1)
                .with("name", "My school")
                .with("colour", "#FFFF00")
                .with("contact", "My contact")
                .with("hourlyRate", hourlyRate)
                .with("postalAddress", "My address")
                .with("phone", "0123")
                .with("mobilePhone", "4567")
                .with("email", "school@test.com")
                .with("webSite", "http://school.my")
                .end();
    }

    public static UISchool school() {
        return school(Money.of(CurrencyUnit.EUR, new BigDecimal("45")));
    }

    public static UISchool school(Money hourlyRate) {
        return new UISchool(
                1,
                "My school",
                "#FFFF00",
                "My contact",
                hourlyRate,
                "My address",
                "0123",
                "4567",
                "school@test.com",
                "http://school.my"
        );
    }
}
