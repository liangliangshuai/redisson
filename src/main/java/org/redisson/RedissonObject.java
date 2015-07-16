/**
 * Copyright 2014 Nikita Koksharov, Nickolay Borbit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson;

import org.redisson.client.protocol.RedisCommands;
import org.redisson.connection.ConnectionManager;
import org.redisson.core.RObject;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

/**
 * Base Redisson object
 *
 * @author Nikita Koksharov
 *
 */
abstract class RedissonObject implements RObject {

    final ConnectionManager connectionManager;
    private final String name;

    public RedissonObject(ConnectionManager connectionManager, String name) {
        this.connectionManager = connectionManager;
        this.name = name;
    }

    protected <V> Promise<V> newPromise() {
        return connectionManager.getGroup().next().<V>newPromise();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean rename(String newName) {
        return connectionManager.get(renameAsync(newName));
    }

    @Override
    public Future<Boolean> renameAsync(String newName) {
        return connectionManager.writeAsync(getName(), RedisCommands.RENAME, getName(), newName);
    }

    @Override
    public boolean renamenx(String newName) {
        return connectionManager.get(renamenxAsync(newName));
    }

    @Override
    public Future<Boolean> renamenxAsync(String newName) {
        return connectionManager.writeAsync(getName(), RedisCommands.RENAMENX, getName(), newName);
    }

    @Override
    public boolean delete() {
        return connectionManager.get(deleteAsync());
    }

    @Override
    public Future<Boolean> deleteAsync() {
        return connectionManager.writeAsync(getName(), RedisCommands.DEL_SINGLE, getName());
    }

}
