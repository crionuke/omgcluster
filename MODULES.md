```mermaid
graph TD
    event-handler --> zone-state
    event-handler --> cache-service
    event-handler --> conn-entity
    event-handler --> init-service
    event-handler --> event-entity
    event-handler --> server-entity
    event-handler --> sim-entity
    event-handler --> zone-entity
    event-handler --> world-entity
    event-handler --> layer-entity
    event-handler --> runtime-context
    event-handler --> runtime-service

    lifecycle-service --> server-entity
    lifecycle-service --> job-service
    lifecycle-service --> event-entity
    lifecycle-service --> mdc-id
    lifecycle-service --> tick-service

    init-service --> runtime-context
    init-service --> runtime-service
    init-service --> prop-entity

    cache-service --> server-entity
    cache-service --> sim-entity
    cache-service --> zone-entity
    cache-service --> layer-entity
    cache-service --> world-entity

    runtime-context --> sim-entity
    runtime-context --> sim-result
    runtime-context --> layer-entity
    runtime-context --> world-entity
    runtime-context --> zone-entity
    runtime-context --> zone-state

    runtime-service --> runtime-context
    runtime-service --> sim-result
    runtime-service --> zone-state

    runtime-stub --> runtime-context
    runtime-stub --> runtime-service
    runtime-stub --> sim-result
    runtime-stub --> zone-state

    conn-entity --> server-entity
    conn-entity --> zone-entity
    conn-entity --> world-entity
    conn-entity --> event-entity
    conn-entity --> exception-clazz

    sim-entity --> zone-entity
    sim-entity --> event-entity
    sim-entity --> exception-clazz

    zone-entity --> layer-entity
    zone-entity --> event-entity
    zone-entity --> exception-clazz

    layer-entity --> world-entity
    layer-entity --> event-entity
    layer-entity --> exception-clazz

    world-entity --> exception-clazz
    world-entity --> event-entity

    server-entity --> exception-clazz
    server-entity --> event-entity

    job-service --> mdc-id

    job-worker --> job-service
    job-worker --> event-entity

    sim-executor --> cache-service
    sim-executor --> runtime-context
    sim-executor --> runtime-service
    sim-executor --> sim-result
    sim-executor --> zone-state

    zone-executor --> cache-service
    zone-executor --> runtime-context
    zone-executor --> runtime-service
    zone-executor --> sim-result
    zone-executor --> zone-state
    zone-executor --> sim-entity

    signal-handler --> server-entity
    signal-handler --> signal-service
    signal-handler --> cache-service
    signal-handler --> zone-executor
    signal-handler --> sim-executor
    signal-handler --> task-executor
    signal-handler --> sim-entity
    signal-handler --> zone-entity

    signal-service --> mdc-id

    tick-service --> server-entity
    tick-service --> cache-service
    tick-service --> zone-state
    tick-service --> signal-service
    tick-service --> mdc-id
    tick-service --> zone-entity
```
