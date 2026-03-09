package it.dst.garage.specification;

import org.springframework.data.jpa.domain.Specification;

import it.dst.garage.model.entity.CarEntity;

public class CarSpecifications {

    private CarSpecifications() {
    }

    public static Specification<CarEntity> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("brand"), "%" + brand + "%");
    }

    public static Specification<CarEntity> hasModel(String model) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("model"), "%" + model + "%");
    }

    public static Specification<CarEntity> hasYear(Integer year) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<CarEntity> yearLowerThan(Integer year) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("year"), year);
    }

    public static Specification<CarEntity> yearGreaterThan(Integer year) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("year"), year);
    }

    public static Specification<CarEntity> betweenYears(Integer yearFrom, Integer yearTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("year"), yearFrom, yearTo);
    }

    public static Specification<CarEntity> hasPlate(String plate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("plate"), "%" + plate + "%");
    }

}
