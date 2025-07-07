package com.example.DEMO_INTEGRATION.DAL.Repository;

import com.example.DEMO_INTEGRATION.DAL.Interfaces.IBaseRepository;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil.LogLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.scheduling.annotation.Async;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract base repository providing optimized CRUD operations for entities.
 * Extends SimpleJpaRepository and integrates batch processing, async operations, and professional error handling with logging.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity's ID
 */
@NoRepositoryBean

public abstract class BaseRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements IBaseRepository<T, ID> {

    private static final int BATCH_SIZE = 50; // Batch size for addAll to reduce database hits

    private final LoggerUtil logger;
    private final Class<T> entityType;
    protected final EntityManager entityManager;

    /**
     * Constructs a new BaseRepository for the specified entity type.
     *
     * @param entityInformation metadata about the entity
     * @param entityManager     the EntityManager instance
     * @throws IllegalArgumentException if entityInformation or entityManager is null
     */
    public BaseRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        if (entityInformation == null || entityManager == null) {
            throw new IllegalArgumentException("EntityInformation and EntityManager cannot be null");
        }
        this.entityType = entityInformation.getJavaType();
        this.entityManager = entityManager;
        this.logger = LoggerUtil.getLogger(getClass());
    }

    private Object convertToType(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number num) return num.intValue();
            if (value instanceof String s && s.matches("\\d+")) return Integer.parseInt(s);
        } else if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number num) return num.longValue();
            if (value instanceof String s && s.matches("\\d+")) return Long.parseLong(s);
        } else if (targetType == String.class) {
            return value.toString();
        }

        throw new IllegalArgumentException("Cannot convert value: " + value + " to type: " + targetType.getSimpleName());
    }

    @Override

    public boolean add(T entity) {
        validateNotNull(entity, "Entity");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Persisting single entity", null, null);
            save(entity);
            logger.log(LogLevel.INFO, "Successfully persisted entity", null,
                    Map.of("durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return true;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Error persisting entity", e,null, null);
            throw new DataIntegrityViolationException("Persist failed", e);
        }
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public CompletableFuture<Boolean> addAsync(T entity) {
        return CompletableFuture.supplyAsync(() -> add(entity));
    }

    @Override

    public boolean addAll(List<T> entities) {
        validateNotEmpty(entities, "Entities list");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Persisting " + entities.size() + " entities", null, null);
            saveAll(entities);
            logger.log(LogLevel.INFO, "Successfully persisted all entities", null,
                    Map.of("count", String.valueOf(entities.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return true;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Error persisting entities", e,null, null);
            throw new DataIntegrityViolationException("Bulk persist failed", e);
        }
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public CompletableFuture<Boolean> addAllAsync(List<T> entities) {
        validateNotEmpty(entities, "Entities list");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Persisting " + entities.size() + " entities", null, null);
            saveAll(entities);
            logger.log(LogLevel.INFO, "Successfully persisted all entities", null,
                    Map.of("count", String.valueOf(entities.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Error persisting entities", e, null, null);
            throw new DataIntegrityViolationException("Bulk persist failed", e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean batchAddAll(List<T> entities, int batchSize) {
        validateNotEmpty(entities, "Entities list");
        if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be greater than 0");

        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Starting batch persist for " + entities.size() + " entities with batch size " + batchSize, null, null);
            for (int i = 0; i < entities.size(); i++) {
                entityManager.persist(entities.get(i));
                if ((i + 1) % batchSize == 0 || i == entities.size() - 1) {
                    entityManager.flush();
                    entityManager.clear();
                    logger.log(LogLevel.DEBUG, "Persisted batch up to index: " + i, null, null);
                }
            }
            logger.log(LogLevel.INFO, "Successfully persisted all entities in batches", null,
                    Map.of("batchSize", String.valueOf(batchSize),
                            "totalEntities", String.valueOf(entities.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return true;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Batch persist failed", e,null, null);
            throw new DataIntegrityViolationException("Batch persist failed", e);
        }
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public CompletableFuture<Boolean> batchAddAllAsync(List<T> entities, int batchSize) {
        return CompletableFuture.supplyAsync(() -> batchAddAll(entities, batchSize));
    }

    @Override

    public boolean update(T entity) {
        validateNotNull(entity, "Entity");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Updating entity using save()", null, null);
            save(entity);
            logger.log(LogLevel.INFO, "Successfully updated entity", null,
                    Map.of("durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return true;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Update failed using save()", e, null,null);
            throw new DataAccessResourceFailureException("Update failed", e);
        }
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public CompletableFuture<Boolean> updateAsync(T entity) {
        return CompletableFuture.supplyAsync(() -> update(entity));
    }

    @Override

    public boolean deleteentity(T entity) {
        validateNotNull(entity, "Entity");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Deleting entity using SimpleJpaRepository delete()", null, null);
            delete(entity);
            logger.log(LogLevel.INFO, "Successfully deleted entity", null,
                    Map.of("durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return true;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Delete failed using SimpleJpaRepository delete()",e,null,null);
            throw new DataAccessResourceFailureException("Delete failed", e);
        }
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public CompletableFuture<Boolean> deleteAsync(T entity) {
        return CompletableFuture.supplyAsync(() -> deleteentity(entity));
    }

    @Transactional
    public Page<T> fetchPaginated(Pageable pageable) {
        try {
            logger.log(LogLevel.DEBUG, "Fetching paginated records", null, null);
            Page<T> results = findAll(pageable);
            logger.log(LogLevel.INFO, "Successfully fetched paginated records", null,
                    Map.of("page", String.valueOf(pageable.getPageNumber()),
                            "size", String.valueOf(pageable.getPageSize())));
            return results;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch paginated records",e,null,null);
            throw new DataRetrievalFailureException("Failed to fetch paginated records", e);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Page<T>> fetchPaginatedAsync(Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> fetchPaginated(pageable));
    }

    @Transactional
    public Optional<T> fetchSingle(Specification<T> spec) {
        validateNotNull(spec, "Specification");
        try {
            logger.log(LogLevel.DEBUG, "Fetching single entity by condition", null, null);
            Optional<T> result = findOne(spec);
            logger.log(LogLevel.INFO, "Successfully fetched single entity", null,
                    Map.of("found", String.valueOf(result.isPresent())));
            return result;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch single entity", e,null,null);
            throw new DataRetrievalFailureException("Failed to fetch single entity", e);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Optional<T>> fetchSingleAsync(Specification<T> spec) {
        return CompletableFuture.supplyAsync(() -> fetchSingle(spec));
    }

    @Transactional
    public List<T> fetchByCondition(Specification<T> spec) {
        validateNotNull(spec, "Specification");
        try {
            logger.log(LogLevel.DEBUG, "Fetching entities by condition", null, null);
            List<T> results = findAll(spec);
            logger.log(LogLevel.INFO, "Successfully fetched entities by condition", null,
                    Map.of("count", String.valueOf(results.size())));
            return results;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch records by condition", e,null,null);
            throw new DataRetrievalFailureException("Failed to fetch by condition", e);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<List<T>> fetchByConditionAsync(Specification<T> spec) {
        return CompletableFuture.supplyAsync(() -> fetchByCondition(spec));
    }

    @Override
    @Transactional
    public List<T> fetchAll() {
        try {
            logger.log(LogLevel.DEBUG, "Fetching all records", null, null);
            List<T> results = findAll();
            logger.log(LogLevel.INFO, "Successfully fetched all records", null,Map.of("count", String.valueOf(results.size())));
            return results;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch all records",e,null,null);
            throw new DataRetrievalFailureException("Failed to fetch all records", e);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<List<T>> fetchAllAsync() {
        return CompletableFuture.supplyAsync(() -> fetchAll());
    }

    @Override
    @Transactional
    public List<T> getAllByCondition(Predicate<T> condition) {
        if (condition == null) {
            logger.log(LogLevel.ERROR, "Attempted to fetch entities with null condition for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName()));
            throw new IllegalArgumentException("Condition cannot be null");
        }
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Fetching entities by condition for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName()));
            List<T> result = fetchAll().stream()
                    .filter(condition)
                    .collect(Collectors.toList());
            logger.log(LogLevel.INFO, "Fetched " + result.size() + " entities by condition for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName(),
                            "entityCount", String.valueOf(result.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return result;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch entities by condition for type: " + entityType.getSimpleName(), e,null,
                    Map.of("entityType", entityType.getSimpleName()));
            throw new DataRetrievalFailureException("Unable to fetch entities by condition for type " + entityType.getSimpleName(), e);
        }
    }

    @Override
    @Transactional
    public Optional<T> getSingle(Predicate<T> condition) {
        if (condition == null) {
            logger.log(LogLevel.ERROR, "Attempted to fetch single entity with null condition for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName()));
            throw new IllegalArgumentException("Condition cannot be null");
        }
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Fetching single entity by condition for type: " + entityType.getSimpleName(), null,null,
                    Map.of("entityType", entityType.getSimpleName()));
            Optional<T> result = fetchAll().stream()
                    .filter(condition)
                    .findFirst();
            logger.log(LogLevel.INFO, "Fetched single entity for type: " + entityType.getSimpleName() + ": " + (result.isPresent() ? "found" : "not found"), null,null,
                    Map.of("entityType", entityType.getSimpleName(),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
            return result;
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to fetch single entity by condition for type: " + entityType.getSimpleName(), e,null,
                    Map.of("entityType", entityType.getSimpleName()));
            throw new DataRetrievalFailureException("Unable to fetch single entity for type " + entityType.getSimpleName(), e);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void flush() {
        LocalDateTime startTime = LocalDateTime.now();
        try {
            logger.log(LogLevel.DEBUG, "Flushing EntityManager changes for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName()));
            entityManager.flush();
            logger.log(LogLevel.INFO, "Successfully flushed EntityManager changes for type: " + entityType.getSimpleName(), null,
                    Map.of("entityType", entityType.getSimpleName(),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))));
        } catch (PersistenceException e) {
            logger.log(LogLevel.ERROR, "Failed to flush EntityManager for type: " + entityType.getSimpleName(),e,null,
                    Map.of("entityType", entityType.getSimpleName()));
            throw new DataAccessResourceFailureException("Unable to flush changes for type " + entityType.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Async
    @Transactional
    public <R> List<R> executeDynamicNativeQuery(String sql, List<Object> params, Class<R> dtoClass) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL query cannot be null or empty");
        }

        Query query = entityManager.createNativeQuery(sql);

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
        }

        List<Object[]> rows = query.getResultList();
        List<R> resultList = new ArrayList<>(rows.size());

        Constructor<?>[] constructors = dtoClass.getDeclaredConstructors();
        Constructor<?> targetConstructor = null;

        for (Constructor<?> ctor : constructors) {
            if (ctor.getParameterCount() == (rows.isEmpty() ? 0 : rows.get(0).length)) {
                targetConstructor = ctor;
                break;
            }
        }

        if (targetConstructor == null) {
            throw new RuntimeException("No matching constructor found in DTO: " + dtoClass.getSimpleName());
        }

        for (Object[] row : rows) {
            Object[] castedArgs = new Object[row.length];

            Class<?>[] paramTypes = targetConstructor.getParameterTypes();
            for (int i = 0; i < row.length; i++) {
                Object value = row[i];
                Class<?> expectedType = paramTypes[i];
                castedArgs[i] = convertToType(value, expectedType);
            }

            try {
                R dto = (R) targetConstructor.newInstance(castedArgs);
                resultList.add(dto);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate DTO: " + dtoClass.getSimpleName(), e);
            }
        }

        return resultList;
    }



    private void validateNotNull(Object obj, String name) {
        if (obj == null) throw new IllegalArgumentException(name + " cannot be null");
    }

    private void validateNotEmpty(List<?> list, String name) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException(name + " cannot be null or empty");
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected LoggerUtil getLogger() {
        return logger;
    }
}