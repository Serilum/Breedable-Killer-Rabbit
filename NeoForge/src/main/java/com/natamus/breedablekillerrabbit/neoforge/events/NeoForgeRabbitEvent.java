package com.natamus.breedablekillerrabbit.neoforge.events;

import com.natamus.breedablekillerrabbit.events.RabbitEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class NeoForgeRabbitEvent {
	@SubscribeEvent
	public static void onBaby(BabyEntitySpawnEvent e) {
		AgeableMob child = e.getChild();
		Level level = child.level();
		if (level.isClientSide) {
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
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}

		RabbitEvent.onEntityInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getTarget(), null);
	}
	
	@SubscribeEvent
	public static void onTarget(LivingIncomingDamageEvent e) {
		Entity entity = e.getEntity();
		if (!RabbitEvent.onTarget(entity.level(), entity, e.getSource(), e.getAmount())) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void mobSpawn(EntityJoinLevelEvent e) {
		RabbitEvent.mobSpawn(e.getLevel(), e.getEntity());
	}
	
	@SubscribeEvent
	public static void onPlayerDamage(LivingDamageEvent.Post e) {
		Entity entity = e.getEntity();
		RabbitEvent.onPlayerDamage(entity.level(), entity, e.getSource(), e.getNewDamage());
	}
}
