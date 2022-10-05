package com.projuris.projetoStag.specification;

public class AndSpecification<T> extends GenericSpecification<T> {

    private Specification<T> first;
    private Specification<T> second;

    public AndSpecification(Specification<T> firstSpecification, Specification<T> secondSpecification) {
        this.first = firstSpecification;
        this.second = secondSpecification;
    }

    public boolean isSatisfiedBy(T candidate) {
        return this.first.isSatisfiedBy(candidate) && this.second.isSatisfiedBy(candidate);
    }

}
