[![Build Status](https://travis-ci.org/ufukhalis/r-cache.svg?branch=master)](https://travis-ci.org/ufukhalis/r-cache)
[![Coverage Status](https://coveralls.io/repos/github/ufukhalis/r-cache/badge.svg?branch=master)](https://coveralls.io/github/ufukhalis/r-cache?branch=master)


R-Cache
===================
Simple in-memory reactive cache option.

How to Use
------------
Firstly, you should add latest `r-cache` dependency to your project.

    <dependency>
        <groupId>io.github.ufukhalis</groupId>
        <artifactId>r-cache</artifactId>
        <version>0.1.0</version>
    </dependency> 
    
Then, you can create an instance like below.

    RCache<String, String> rCache = new RCache.Builder()
                .maxSize(5) // Default 100
                .expire(Duration.ofSeconds(2)) // Default 5 mins
                .cachePoolSize(5) // Default 10
                .build();
                
After instance creation, you can use `R-Cache` methods like below.

Add element to cache

    Mono<Void> result = rCache.put("key", "value");

Remove element from cache
    
    Mono<Void> result = rCache.remove("key");
    
Get element from cache

    Mono<String> result = rCache.get("key");
    
Note
---

This project is still under development. But you can use in your projects.

For more information about Project Reactor, check the site https://projectreactor.io

License
---
All code in this repository is licensed under the Apache License, Version 2.0. See [LICENCE](./LICENSE).