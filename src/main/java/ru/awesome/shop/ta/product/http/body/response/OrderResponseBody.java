package ru.awesome.shop.ta.product.http.body.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.awesome.shop.ta.utils.JsonRepresentation;

import java.util.Objects;

public class OrderResponseBody {
    private String error;

    public OrderResponseBody() {
    }

    public OrderResponseBody(String error) {
        Objects.requireNonNull(error, "Error message cannot be null");
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public int hashCode() {
        final int firstPrime = 29;
        final int secondPrime = 71;
        return new HashCodeBuilder(firstPrime, secondPrime)
                .append(error)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        OrderResponseBody other = (OrderResponseBody) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(error, other.error)
                .isEquals();
    }

    @Override
    public String toString() {
        return JsonRepresentation.convertToJsonString(this);
    }
}
