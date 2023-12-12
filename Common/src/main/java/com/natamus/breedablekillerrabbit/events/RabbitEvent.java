package com.natamus.breedablekillerrabbit.events;

import com.natamus.breedablekillerrabbit.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class RabbitEvent {
	public static boolean onBaby(ServerLevel world, Animal parentA, Animal parentB, AgeableMob offspring) {
		if (!(offspring instanceof Rabbit)) {
			return true;
		}
		Rabbit rabbit = (Rabbit)offspring;
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= ConfigHandler.chanceBabyRabbitIsKiller) {
			rabbit.setVariant(Rabbit.Variant.EVIL);
			if (ConfigHandler.removeKillerRabbitNameTag) {
				rabbit.setCustomName(null);
			}
			
			Vec3 vec = offspring.position();
			for (Entity entityaround : world.getEntities(null, new AABB(vec.x-10, vec.y-10, vec.z-10, vec.x+10, vec.y+10, vec.z+10))) {
				if (entityaround instanceof Player) {
					Player player = (Player)entityaround;
					StringFunctions.sendMessage(player, "A killer rabbit has been born! Are you far enough away or do you have a golden carrot to share?", ChatFormatting.DARK_GREEN);
					return true;
				}
			}
		}
		
		return true;
	}
	
	public static InteractionResult onEntityInteract(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		if (!(entity instanceof Rabbit)) {
			return InteractionResult.PASS;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		
		if (!itemstack.getItem().equals(Items.GOLDEN_CARROT)) {
			return InteractionResult.PASS;
		}
		
		Rabbit rabbit = (Rabbit)entity;
		if (!rabbit.getVariant().equals(Rabbit.Variant.EVIL)) {
			return InteractionResult.PASS;
		}
		if (rabbit.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			StringFunctions.sendMessage(player, "The killer rabbit has already been tamed.", ChatFormatting.DARK_GREEN);
			return InteractionResult.PASS;
		}
		
		rabbit.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_CARROT, 1));
		itemstack.shrink(1);
		StringFunctions.sendMessage(player, "The killer rabbit has been tamed!", ChatFormatting.DARK_GREEN);
		return InteractionResult.SUCCESS;
	}
	
	public static boolean onTarget(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return true;
		}
		
		Entity source = damageSource.getDirectEntity();
		if (!(source instanceof Rabbit)) {
			return true;
		}

		return !((Rabbit)source).getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT);
	}
	
	public static void mobSpawn(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof Rabbit)) {
			return;
		}
		if (!ConfigHandler.removeKillerRabbitNameTag) {
			return;
		}
		if (!((Rabbit)entity).getVariant().equals(Rabbit.Variant.EVIL)) {
			return;
		}
		if (!entity.hasCustomName()) {
			return;
		}
		
		if (entity.getCustomName().equals(Component.translatable("entity.minecraft.killer_bunny"))) {
			entity.setCustomName(null);
		}
	}
	
	public static float onPlayerDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return damageAmount;
		}
		
		if (!(entity instanceof Player)) {
			return damageAmount;
		}
		
		Entity source = damageSource.getDirectEntity();
		if (source == null) {
			return damageAmount;
		}
		
		if (!(source instanceof Rabbit)) {
			return damageAmount;
		}
		
		if (!((Rabbit)source).getVariant().equals(Rabbit.Variant.EVIL)) {
			StringFunctions.sendMessage((Player)entity, "The killer rabbit wants a golden carrot!", ChatFormatting.RED);
		}
		
		return damageAmount;
	}
}
