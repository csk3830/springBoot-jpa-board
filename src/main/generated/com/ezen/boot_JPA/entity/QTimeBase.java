package com.ezen.boot_JPA.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimeBase is a Querydsl query type for TimeBase
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QTimeBase extends EntityPathBase<TimeBase> {

    private static final long serialVersionUID = 1409947692L;

    public static final QTimeBase timeBase = new QTimeBase("timeBase");

    public final DateTimePath<java.time.LocalDateTime> modifyAt = createDateTime("modifyAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> registerAt = createDateTime("registerAt", java.time.LocalDateTime.class);

    public QTimeBase(String variable) {
        super(TimeBase.class, forVariable(variable));
    }

    public QTimeBase(Path<? extends TimeBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimeBase(PathMetadata metadata) {
        super(TimeBase.class, metadata);
    }

}

