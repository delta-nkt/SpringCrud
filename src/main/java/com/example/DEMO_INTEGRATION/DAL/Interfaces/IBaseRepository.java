package com.example.DEMO_INTEGRATION.DAL.Interfaces;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBaseRepository<T, ID> {
    /**
     * Adds a new entity to the database.
     *
     * @param entity the entity to persist
     * @return true if the operation was successful
     * @throws IllegalArgumentException if the entity is null
     * @throws org.springframework.dao.DataIntegrityViolationException if persistence fails
     */
    @Transactional(rollbackOn = Exception.class)
    boolean add(T entity);

    /**
     * Asynchronously adds a new entity to the database.
     *
     * @param entity the entity to persist
     * @return a CompletableFuture with true if successful
     */
    CompletableFuture<Boolean> addAsync(T entity);

    /**
     * Adds a list of entities to the database.
     *
     * @param entities the list of entities to persist
     * @return true if all entities were persisted successfully
     * @throws IllegalArgumentException if the entities list is null or empty
     * @throws org.springframework.dao.DataIntegrityViolationException if persistence fails
     */
    @Transactional(rollbackOn = Exception.class)
    boolean addAll(List<T> entities);

    /**
     * Asynchronously adds a list of entities to the database.
     *
     * @param entities the list of entities to persist
     * @return a CompletableFuture with true if successful
     */
    CompletableFuture<Boolean> addAllAsync(List<T> entities);

    /**
     * Adds a list of entities to the database using batch processing.
     *
     * @param entities the list of entities to persist
     * @param batchSize the size of each batch
     * @return true if all entities were persisted successfully
     * @throws IllegalArgumentException if the entities list is null or empty, or batchSize is invalid
     * @throws org.springframework.dao.DataIntegrityViolationException if persistence fails
     */
    @Transactional(rollbackOn = Exception.class)
    boolean batchAddAll(List<T> entities, int batchSize);

    /**
     * Asynchronously adds a list of entities to the database using batch processing.
     *
     * @param entities the list of entities to persist
     * @param batchSize the size of each batch
     * @return a CompletableFuture with true if successful
     */
    CompletableFuture<Boolean> batchAddAllAsync(List<T> entities, int batchSize);

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return true if the update was successful
     * @throws IllegalArgumentException if the entity is null
     * @throws org.springframework.dao.DataAccessResourceFailureException if the update fails
     */
    @Transactional(rollbackOn = Exception.class)
    boolean update(T entity);

    /**
     * Asynchronously updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return a CompletableFuture with true if successful
     */
    CompletableFuture<Boolean> updateAsync(T entity);

    /**
     * Deletes an entity from the database.
     *
     * @param entity the entity to delete
     * @return true if the deletion was successful
     * @throws IllegalArgumentException if the entity is null
     * @throws org.springframework.dao.DataAccessResourceFailureException if the deletion fails
     */
    @Transactional(rollbackOn = Exception.class)
    boolean deleteentity(T entity);

    /**
     * Asynchronously deletes an entity from the database.
     *
     * @param entity the entity to delete
     * @return a CompletableFuture with true if successful
     */
    CompletableFuture<Boolean> deleteAsync(T entity);

    /**
     * Retrieves all entities with pagination.
     *
     * @param pageable the pagination information
     * @return a Page of entities
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    Page<T> fetchPaginated(Pageable pageable);

    /**
     * Asynchronously retrieves all entities with pagination.
     *
     * @param pageable the pagination information
     * @return a CompletableFuture with a Page of entities
     */
    CompletableFuture<Page<T>> fetchPaginatedAsync(Pageable pageable);

    /**
     * Retrieves a single entity matching the given specification.
     *
     * @param spec the specification to filter entities
     * @return an Optional containing the matching entity, or empty if none found
     * @throws IllegalArgumentException if the specification is null
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    Optional<T> fetchSingle(Specification<T> spec);

    /**
     * Asynchronously retrieves a single entity matching the given specification.
     *
     * @param spec the specification to filter entities
     * @return a CompletableFuture with an Optional containing the matching entity
     */
    CompletableFuture<Optional<T>> fetchSingleAsync(Specification<T> spec);

    /**
     * Retrieves entities matching the given specification.
     *
     * @param spec the specification to filter entities
     * @return a list of matching entities
     * @throws IllegalArgumentException if the specification is null
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    List<T> fetchByCondition(Specification<T> spec);

    /**
     * Asynchronously retrieves entities matching the given specification.
     *
     * @param spec the specification to filter entities
     * @return a CompletableFuture with a list of matching entities
     */
    CompletableFuture<List<T>> fetchByConditionAsync(Specification<T> spec);

    /**
     * Retrieves all entities of the specified type.
     *
     * @return a list of all entities
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    List<T> fetchAll();

    /**
     * Asynchronously retrieves all entities of the specified type.
     *
     * @return a CompletableFuture with a list of all entities
     */
    CompletableFuture<List<T>> fetchAllAsync();

    /**
     * Retrieves all entities matching the given condition using in-memory filtering.
     *
     * @param condition the predicate to filter entities
     * @return a list of entities matching the condition
     * @throws IllegalArgumentException if the condition is null
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    List<T> getAllByCondition(Predicate<T> condition);

    /**
     * Retrieves a single entity matching the given condition using in-memory filtering.
     *
     * @param condition the predicate to filter entities
     * @return an Optional containing the first matching entity, or empty if none found
     * @throws IllegalArgumentException if the condition is null
     * @throws org.springframework.dao.DataRetrievalFailureException if the query fails
     */
    @Transactional
    Optional<T> getSingle(Predicate<T> condition);

    /**
     * Flushes pending changes to the database.
     *
     * @throws org.springframework.dao.DataAccessResourceFailureException if the flush operation fails
     */
    @Transactional(rollbackOn = Exception.class)
    void flush();

    /**
     * Executes a dynamic native SQL query and maps results to a DTO.
     *
     * @param sql      the native SQL query
     * @param params   the query parameters
     * @param dtoClass the DTO class to map results to
     * @param <R>      the DTO type
     * @return a list of DTOs
     * @throws IllegalArgumentException if the SQL is invalid
     * @throws RuntimeException if DTO instantiation fails
     */
    @Transactional
    <R> List<R> executeDynamicNativeQuery(String sql, List<Object> params, Class<R> dtoClass);
}