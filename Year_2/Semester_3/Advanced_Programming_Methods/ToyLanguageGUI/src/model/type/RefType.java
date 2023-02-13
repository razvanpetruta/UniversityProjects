package model.type;

import model.value.RefValue;
import model.value.Value;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Type anotherType) {
        if (anotherType instanceof RefType) {
            return this.inner.equals(((RefType) anotherType).getInner());
        }
        return false;
    }

    @Override
    public Value defaultValue() {
        return new RefValue(0, this.inner);
    }

    @Override
    public Type deepCopy() {
        return new RefType(this.inner.deepCopy());
    }

    @Override
    public String toString() {
        return "RefType(" + this.inner + ")";
    }
}
