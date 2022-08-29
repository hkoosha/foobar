- Prevent deletion of a customer who has order in progress.
- Prevent deletion of a seller who has order in progress.

- Service Discovery.
- Implement Saga for transactions.
- Do not expose all REST CRUD endpoints (follow DDD).
- Sketch out eventual consistency considerations and implications.


- Handle `Lock` and transaction failure.
- Make write to DB and kafka transactional or at least recover from crashes
  and failures, aka, make sure write to db == write to kafka.

- Critical: Finish DB tx before sending something to kafka
- Critical: cases when service dies before sending message to kafka, is not 
  handled.
- Recovery service that runs initially for case above, and *then*, the original
  service starts.


- Properly handle SQL exceptions to HTTP response code, currently application
  error (unique key violation) is translated to 403.


- Add the configuration annotation that makes `LastModifiedAt` and `CreatedDate`
  annotation working.
- Adjust transactions isolation levels.
- Proper custom error handling, instead of exposing message.
- Unit tests :-)
- Make errors in Controllers consistent.
- Make exception messages 1. consistent and 2. informative.
- More descriptive exception messages exposed in REST.
- Authentication (hopefully JWT)
- API rate limiter
- Do not turn DataIntegrityViolationException into EntityInIllegalStateException
  instead check beforehand, and treat the DataIntegrity exception as http 500
  error.
- Add paging to REST endpoints.


- remove foobar-gen and only override templates
