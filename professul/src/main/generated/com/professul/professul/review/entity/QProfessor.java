package com.professul.professul.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfessor is a Querydsl query type for Professor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfessor extends EntityPathBase<Professor> {

    private static final long serialVersionUID = 853540631L;

    public static final QProfessor professor = new QProfessor("professor");

    public final NumberPath<Integer> deptId = createNumber("deptId", Integer.class);

    public final NumberPath<Integer> profId = createNumber("profId", Integer.class);

    public final StringPath profName = createString("profName");

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final NumberPath<Integer> univId = createNumber("univId", Integer.class);

    public QProfessor(String variable) {
        super(Professor.class, forVariable(variable));
    }

    public QProfessor(Path<? extends Professor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfessor(PathMetadata metadata) {
        super(Professor.class, metadata);
    }

}

