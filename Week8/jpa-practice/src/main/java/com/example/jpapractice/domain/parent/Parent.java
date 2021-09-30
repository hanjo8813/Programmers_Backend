package com.example.jpapractice.domain.parent;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
//@IdClass(ParentId.class)
public class Parent {
    @EmbeddedId
    private ParentId id;

//    @Id
//    private String id1;
//    @Id
//    private String id2;
}
