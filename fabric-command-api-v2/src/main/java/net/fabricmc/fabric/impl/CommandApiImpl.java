package net.fabricmc.fabric.impl;

import java.util.HashMap;
import java.util.Map;

import com.mojang.brigadier.arguments.ArgumentType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;

@Mod(CommandApiImpl.MODID)
public class CommandApiImpl {
	public static final String MODID = "fabric_command_api_v2";

	@SuppressWarnings("rawtypes")
	private static final Map<Class, ArgumentSerializer<?, ?>> ARGUMENT_TYPE_CLASSES = new HashMap<>();
	private static final Map<Identifier, ArgumentSerializer<?, ?>> ARGUMENT_TYPES = new HashMap<>();

	public static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> void registerArgumentType(Identifier id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
		ARGUMENT_TYPE_CLASSES.put(clazz, serializer);
		ARGUMENT_TYPES.put(id, serializer);
	}

	public CommandApiImpl(IEventBus bus, Dist dist) {
		bus.addListener(CommandApiImpl::registerArgumentTypes);
		if (dist.isClient()) {
			NeoForge.EVENT_BUS.addListener(ClientCommandInternals::registerClientCommands);
		}
	}

	private static void registerArgumentTypes(RegisterEvent event) {
		event.register(RegistryKeys.COMMAND_ARGUMENT_TYPE, helper -> {
			ARGUMENT_TYPE_CLASSES.forEach(ArgumentTypes::registerByClass);
			ARGUMENT_TYPES.forEach(helper::register);
		});
	}
}
