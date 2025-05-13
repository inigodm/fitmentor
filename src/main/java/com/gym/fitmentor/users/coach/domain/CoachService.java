package com.gym.fitmentor.users.coach.domain;

import com.gym.fitmentor.users.coach.infrastructure.CoachJPA;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link CoachJPA}.
 */
public interface CoachService {
    /**
     * Save a coach.
     *
     * @param coach the entity to save.
     * @return the persisted entity.
     */
    void save(Coach coach);

    /**
     * Get all the coaches.
     *
     * @return the list of entities.
     */
    List<Coach> findAll();

    /**
     * Get the "id" coach.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Coach> findOne(UUID id);

    /**
     * Delete the "id" coach.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
