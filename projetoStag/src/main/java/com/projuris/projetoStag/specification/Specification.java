package com.projuris.projetoStag.specification;

public interface Specification<T> {

    boolean isSatisfiedBy(T var);

    String getMessage();

    String getField();
}
