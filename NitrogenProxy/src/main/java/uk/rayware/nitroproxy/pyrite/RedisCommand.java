package uk.rayware.nitroproxy.pyrite;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {

    /**
     * Execute Command.
     *
     * @param redis instance.
     */
    T execute(Jedis redis);

}

