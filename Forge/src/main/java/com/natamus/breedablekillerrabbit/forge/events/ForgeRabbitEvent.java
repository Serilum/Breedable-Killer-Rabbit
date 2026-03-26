package com.natamus.breedablekillerrabbit.forge.events;

import com.natamus.breedablekillerrabbit.events.RabbitEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeRabbitEvent {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeRabbitEvent.class);
	}

	@SubscribeEvent
	public static void onBaby(BabyEntitySpawnEvent e) {
		AgeableMob child = e.getChild();
		Level level = child.level();
		if (level.isClientSide()) {
			return;
		}

		Mob parentA = e.getParentA();
		Mob parentB = e.getParentB();
		if (!(parentA instanceof Animal) || !(parentB instanceof Animal)) {
			return;
		}

		RabbitEvent.onBaby((ServerLevel)level, (Animal)parentA, (Animal)parentB, e.getChild());
	}
	
	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific e) {
		Level world = e.getLevel();
		if (world.isClientSide()) {
			return;
		}

		RabbitEvent.onEntityInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getTarget(), null);
	}
	
	@SubscribeEvent
	public static boolean onTarget(LivingAttackEvent e) {
		Entity entity = e.getEntity();
		if (!RabbitEvent.onTarget(entity.level(), entity, e.getSource(), e.getAmount())) {
			return true;
		}
		return false;
	}
	
	@SubscribeEvent
	public static void mobSpawn(EntityJoinLevelEvent e) {
		RabbitEvent.mobSpawn(e.getLevel(), e.getEntity());
	}
	
	@SubscribeEvent
	public static void onPlayerDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		RabbitEvent.onPlayerDamage(entity.level(), entity, e.getSource(), e.getAmount());
	}
}
