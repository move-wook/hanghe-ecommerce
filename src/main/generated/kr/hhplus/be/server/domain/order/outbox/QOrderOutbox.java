package kr.hhplus.be.server.domain.order.outbox;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderOutbox is a Querydsl query type for OrderOutbox
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderOutbox extends EntityPathBase<OrderOutbox> {

    private static final long serialVersionUID = 801077527L;

    public static final QOrderOutbox orderOutbox = new QOrderOutbox("orderOutbox");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath eventType = createString("eventType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final StringPath payload = createString("payload");

    public final BooleanPath processed = createBoolean("processed");

    public QOrderOutbox(String variable) {
        super(OrderOutbox.class, forVariable(variable));
    }

    public QOrderOutbox(Path<? extends OrderOutbox> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderOutbox(PathMetadata metadata) {
        super(OrderOutbox.class, metadata);
    }

}

