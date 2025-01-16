package kr.hhplus.be.server.domain.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductInventory is a Querydsl query type for ProductInventory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductInventory extends EntityPathBase<ProductInventory> {

    private static final long serialVersionUID = -1779414263L;

    public static final QProductInventory productInventory = new QProductInventory("productInventory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QProductInventory(String variable) {
        super(ProductInventory.class, forVariable(variable));
    }

    public QProductInventory(Path<? extends ProductInventory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductInventory(PathMetadata metadata) {
        super(ProductInventory.class, metadata);
    }

}

