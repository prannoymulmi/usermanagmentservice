# 2. add all controllers in rest package

Date: 2022-04-08

## Status

Accepted

## Context

We need a proper separation of the controllers with the models. As this is a small project separation using domain are unnecessary. 

## Decision

Adding all the available controllers in the rest package.

## Consequences

All the controllers can be put into a single package which is later easy to find when a maintenance is required.   
