package kr.hhplus.be.server.domain.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductInventory is a Querydsl query type for ProductInventory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductInventory extends EntityPathBase<ProductInventory> {

    private static final long serialVersionUID = -1779414263L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductInventory productInventory = new QProductInventory("productInventory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final QProduct product;

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QProductInventory(String variable) {
        this(ProductInventory.class, forVariable(variable), INITS);
    }

    public QProductInventory(Path<? extends ProductInventory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductInventory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductInventory(PathMetadata metadata, PathInits inits) {
        this(ProductInventory.class, metadata, inits);
    }

    public QProductInventory(Class<? extends ProductInventory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

