# 7. indexing username in the user table

Date: 2022-04-09

## Status

Accepted

## Context

The users can be also queried using the username which is not the primary key of the table. 
This might lead to slower search time when the table is big.

## Decision

Assign an index also to the username field.

## Consequences

Indexing the username makes it more efficient to query for users using this field.
