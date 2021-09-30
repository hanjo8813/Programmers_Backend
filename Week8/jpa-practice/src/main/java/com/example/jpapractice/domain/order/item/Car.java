package com.example.jpapractice.domain.order.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("CAR")
public class Car extends Item{
    private int power;
}
