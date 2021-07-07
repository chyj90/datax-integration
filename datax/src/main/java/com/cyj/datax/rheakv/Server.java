/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cyj.datax.rheakv;

import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.StoreEngineOptions;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RocksDBOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.StoreEngineOptionsConfigured;
import com.alipay.sofa.jraft.rhea.storage.StorageType;
import com.alipay.sofa.jraft.util.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;

/**
 * @author jiachun.fjc
 */
@Component
public class Server {

    public Server(@Value("${rheakv.instance.dataPath}") String RAFT_DATA_PATH, @Value("${rheakv.instance.dbPath}") String DB_PATH, @Value("${rheakv.cluster.name}") String CLUSTER_NAME, @Value("${rheakv.cluster.nodes}") String ALL_NODE_ADDRESSES, @Value("${rheakv.instance.port}") String SERVER_PORT) {
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured()
                .withFake(true) // use a fake pd
                .config();
        final StoreEngineOptions storeOpts = StoreEngineOptionsConfigured.newConfigured() //
                .withStorageType(StorageType.RocksDB)
                .withRocksDBOptions(RocksDBOptionsConfigured.newConfigured().withDbPath(DB_PATH).config())
                .withRaftDataPath(RAFT_DATA_PATH)
                .withServerAddress(new Endpoint("127.0.0.1", Integer.parseInt(SERVER_PORT)))
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured() //
                .withClusterName(CLUSTER_NAME) //
                .withInitialServerList(ALL_NODE_ADDRESSES)
                .withStoreEngineOptions(storeOpts) //
                .withPlacementDriverOptions(pdOpts) //
                .config();
        System.out.println(opts);
        final Node node = new Node(opts);
        node.start();
        Runtime.getRuntime().addShutdownHook(new Thread(node::stop));
        System.out.println("server1 start OK");
    }
}
