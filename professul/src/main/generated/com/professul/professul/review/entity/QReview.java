package com.professul.professul.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 199610288L;

    public static final QReview review1 = new QReview("review1");

    public final NumberPath<Integer> classLevel = createNumber("classLevel", Integer.class);

    public final StringPath classPlan = createString("classPlan");

    public final NumberPath<Integer> classRate = createNumber("classRate", Integer.class);

    public final NumberPath<Integer> groupProject = createNumber("groupProject", Integer.class);

    public final NumberPath<Integer> profId = createNumber("profId", Integer.class);

    public final NumberPath<Integer> profRate = createNumber("profRate", Integer.class);

    public final StringPath review = createString("review");

    public final NumberPath<Integer> reviewId = createNumber("reviewId", Integer.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QReview(String variable) {
        super(Review.class, forVariable(variable));
    }

    public QReview(Path<? extends Review> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReview(PathMetadata metadata) {
        super(Review.class, metadata);
    }

}

