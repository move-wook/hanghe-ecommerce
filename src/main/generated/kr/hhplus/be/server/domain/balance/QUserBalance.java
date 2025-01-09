package kr.hhplus.be.server.domain.balance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserBalance is a Querydsl query type for UserBalance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBalance extends EntityPathBase<UserBalance> {

    private static final long serialVersionUID = -1003488350L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserBalance userBalance = new QUserBalance("userBalance");

    public final NumberPath<java.math.BigDecimal> currentBalance = createNumber("currentBalance", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final kr.hhplus.be.server.domain.user.QUser user;

    public QUserBalance(String variable) {
        this(UserBalance.class, forVariable(variable), INITS);
    }

    public QUserBalance(Path<? extends UserBalance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserBalance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserBalance(PathMetadata metadata, PathInits inits) {
        this(UserBalance.class, metadata, inits);
    }

    public QUserBalance(Class<? extends UserBalance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.hhplus.be.server.domain.user.QUser(forProperty("user")) : null;
    }

}

