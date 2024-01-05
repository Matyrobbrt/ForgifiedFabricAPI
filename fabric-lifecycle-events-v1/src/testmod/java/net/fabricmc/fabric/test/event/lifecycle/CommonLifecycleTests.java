/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.test.event.lifecycle;

import net.fabricmc.fabric.test.event.lifecycle.client.ClientBlockEntityLifecycleTests;
import net.fabricmc.fabric.test.event.lifecycle.client.ClientEntityLifecycleTests;
import net.fabricmc.fabric.test.event.lifecycle.client.ClientLifecycleTests;
import net.fabricmc.fabric.test.event.lifecycle.client.ClientTickTests;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

@Mod("fabric-lifecycle-events-v1-testmod")
public class CommonLifecycleTests  {
	public CommonLifecycleTests(Dist dist) {
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			ServerLifecycleTests.LOGGER.info("Tags (re)loaded on {} {}", client ? "client" : "server", Thread.currentThread());
		});

		new ServerBlockEntityLifecycleTests().onInitialize();
		new ServerEntityLifecycleTests().onInitialize();
		new ServerLifecycleTests().onInitialize();
		new ServerResourceReloadTests().onInitialize();
		new ServerTickTests().onInitialize();

		if (dist.isClient()) {
			new ClientTickTests().onInitializeClient();
			new ClientBlockEntityLifecycleTests().onInitializeClient();
			new ClientLifecycleTests().onInitializeClient();
			new ClientEntityLifecycleTests().onInitializeClient();
		}
	}
}
