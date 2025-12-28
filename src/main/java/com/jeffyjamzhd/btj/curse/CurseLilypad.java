package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurseRender;
import com.jeffyjamzhd.btj.api.hook.FoodConsumedEvent;
import net.minecraft.src.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CurseLilypad extends AbstractCurseMeter implements ICurseRender, FoodConsumedEvent {
    @Override
    public void init() {
        super.init();
        this.setMaxValue(1200);
        this.setValue(0);
    }

    @Override
    public void clientInit() {
        super.clientInit();
        this.setDisplayStrings(new String[]{
                "curse.btj.lilypad.name",
                "curse.btj.lilypad.desc"
        });
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
        int value = this.getValue();
        // Slowly kill the player like the little insect they are
        this.setValue(++value);
        if (value == 1200) {
            player.attackEntityFrom(DamageSource.starve, 2.0F);
            this.setValue(0);
        }
    }

    @Override
    public void onFoodConsume(EntityPlayer player, ItemStack item) {
        // Reset bar and sync to client
        this.setValue(0);
        this.dirty = true;
    }

    @Override
    public void draw(GuiIngame gui) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        mc.getTextureManager().bindTexture(CURSES_TEX);
        // Draw empty bar
        gui.drawTexturedModalRect(width / 2 - 16, height - 80, 0, 18, 32, 16);
        // Draw the progress
        gui.drawTexturedModalRect(width / 2 - 16, height - 80, 32, 18, 32 - (this.getValue() * 32) / 1200, 16);
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        nbt = super.writeNBT(nbt);
        nbt.setInteger("hunger", this.getValue());
        return nbt;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        nbt = super.readNBT(nbt);
        this.setValue(nbt.getInteger("hunger"));
        return nbt;
    }

    @Override
    public void syncToClient(DataOutputStream stream) throws IOException {
        super.syncToClient(stream);
        stream.writeShort(this.getValue());
    }

    @Override
    public void syncFromServer(DataInputStream stream) throws IOException {
        super.syncFromServer(stream);
        this.setValue(stream.readShort());
    }
}
