package kr.hhplus.be.server.domain.balance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserBalance is a Querydsl query type for UserBalance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBalance extends EntityPathBase<UserBalance> {

    private static final long serialVersionUID = -1003488350L;

    public static final QUserBalance userBalance = new QUserBalance("userBalance");

    public final NumberPath<java.math.BigDecimal> currentBalance = createNumber("currentBalance", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QUserBalance(String variable) {
        super(UserBalance.class, forVariable(variable));
    }

    public QUserBalance(Path<? extends UserBalance> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserBalance(PathMetadata metadata) {
        super(UserBalance.class, metadata);
    }

}

