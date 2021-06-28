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

import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RegionRouteTableOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.configured.MultiRegionRouteTableOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.alipay.sofa.jraft.rhea.util.concurrent.DistributedLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jiachun.fjc
 */
@Component
public class Client {

    private final RheaKVStore rheaKVStore = new DefaultRheaKVStore();

    @Value("${rheakv.cluster.nodes}")
    private String ALL_NODE_ADDRESSES;

    @Value("${rheakv.cluster.name}")
    private String CLUSTER_NAME;

    private boolean finish = false;

    public static String TASK_LOCK="lock_task";

    public DistributedLock<byte[]> getlock(String lockKey)
    {
        return rheaKVStore.getDistributedLock(lockKey, 10, TimeUnit.SECONDS);
    }

    public void init() {
        if (!finish)
        {
            finish = true;
            final List<RegionRouteTableOptions> regionRouteTableOptionsList = MultiRegionRouteTableOptionsConfigured
                    .newConfigured() //
                    .withInitialServerList(-1L /* default id */, ALL_NODE_ADDRESSES) //
                    .config();
            final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured() //
                    .withFake(true) //
                    .withRegionRouteTableOptionsList(regionRouteTableOptionsList) //
                    .config();
            final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured() //
                    .withClusterName(CLUSTER_NAME) //
                    .withPlacementDriverOptions(pdOpts) //
                    .config();
            System.out.println(opts);
            rheaKVStore.init(opts);
        }
    }

    public void shutdown() {
        this.rheaKVStore.shutdown();
    }

    public RheaKVStore getRheaKVStore() {
        return rheaKVStore;
    }
}
