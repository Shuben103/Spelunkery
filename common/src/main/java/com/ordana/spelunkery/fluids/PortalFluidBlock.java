package com.ordana.spelunkery.fluids;

import com.mlib.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class PortalFluidBlock extends ModLiquidBlock {

    public PortalFluidBlock(Supplier<FlowingFluid> flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    private int tickCounter = 0;

    public void setTickCounter(int tick) {
        tickCounter = tick;
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        this.tickCounter++;
        if (this.tickCounter == 1) level.playSound(null, entity.blockPosition(), SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions() && this.tickCounter == 250) {
            this.setTickCounter(0);
            if (entity instanceof ServerPlayer player) LevelHelper.teleportToSpawnPosition(player);
            else  {
                LevelHelper.teleportToWorldspawn(level, entity);
                level.playSound(null, entity.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
            }

        }
    }

}