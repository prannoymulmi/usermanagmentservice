# 4. user entity and user separate

Date: 2022-04-08

## Status

Accepted

## Context

The data input in the rest should not define how the data should be structured in the backend. Having a single 
user pojo forces us to change the interface when the backend changes.

## Decision

Separate the input schema and the entity schema which is to be stored in the database. 

## Consequences

Now in case the backend changes its structure or database the user interface does not has to be changed and 
will be directly effect it.
