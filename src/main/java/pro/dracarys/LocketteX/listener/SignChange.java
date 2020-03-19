package pro.dracarys.LocketteX.listener;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import pro.dracarys.LocketteX.LocketteX;
import pro.dracarys.LocketteX.api.LocketteXAPI;
import pro.dracarys.LocketteX.config.Config;
import pro.dracarys.LocketteX.config.Message;
import pro.dracarys.LocketteX.utils.ClaimUtil;
import pro.dracarys.LocketteX.utils.Util;

public class SignChange implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent e) {
        if (!Util.isEnabledWorld(e.getPlayer().getWorld().getName())) {
            return;
        }
        String line0 = e.getLine(0);
        if (line0 == null || !line0.equalsIgnoreCase(Config.SIGN_ID_LINE.getString())) return;
        // From this point forward we're sure the player is trying to create a [Protect] sign
        if (!e.getPlayer().hasPermission(Config.PERMISSION_CREATION.getString())) {
            e.getPlayer().sendMessage(Message.PREFIX.getMessage() + Message.CREATION_NOPERMISSION.getMessage());
            e.getBlock().breakNaturally();
            return;
        }
        if (LocketteX.UseEconomy) {
            if (LocketteX.econ.getBalance(e.getPlayer()) < Config.PRICE_CREATION.getInt()) {
                e.getPlayer().sendMessage(Message.PREFIX.getMessage() + Message.NOT_ENOUGH_MONEY.getMessage().replace("%price%", Config.PRICE_CREATION.getInt() + ""));
                e.getBlock().breakNaturally();
                return;
            }
        }
        Block attachedBlock;
        Sign s = (Sign) e.getBlock().getState();
        try {
            org.bukkit.material.Sign sd = (org.bukkit.material.Sign) s.getData();
            attachedBlock = e.getBlock().getRelative(sd.getAttachedFace());
        } catch (ClassCastException ex) {
            WallSign signData = (WallSign) s.getBlockData();
            attachedBlock = e.getBlock().getRelative(signData.getFacing().getOppositeFace());
        }
        if (Config.PROTECT_CLAIMED_ONLY.getOption() && !ClaimUtil.isClaimedAt(attachedBlock.getLocation())) {
            e.getPlayer().sendMessage(Message.PREFIX.getMessage() + Message.CANT_PROTECT_ON_UNCLAIMED.getMessage());
            e.getBlock().breakNaturally();
            return;
        }
        if ((attachedBlock.getState() instanceof DoubleChest) || attachedBlock.getState() instanceof Chest) {
            Chest chest = (Chest) attachedBlock.getState();
            String owner = LocketteXAPI.getChestOwner(chest.getInventory().getHolder());
            if (owner != null) {
                e.getPlayer().sendMessage(Message.PREFIX.getMessage() + Message.CHEST_ALREADY_PROTECTED.getMessage().replace("%owner%", owner));
                e.getBlock().breakNaturally();
                return;
            }
        } else {
            e.getPlayer().sendMessage(Message.PREFIX.getMessage() + Message.CANT_PROTECT_THIS_CONTAINER.getMessage());
            return;
        }
        int num = 0;
        for (String ln : Config.SIGN_FORMATTED_LINES.getStrings()) {
            e.setLine(num, Util.color(ln.replace("%owner%", e.getPlayer().getName())));
            num++;
            if (num >= 5) // Sign has 4 lines
                break;
        }
        if (LocketteX.UseEconomy) {
            LocketteX.econ.withdrawPlayer(e.getPlayer(), Config.PRICE_CREATION.getInt());
            e.getPlayer().sendMessage(Message.CHEST_PROTECT_SUCCESS_ECON.getMessage().replace("%price%", Config.PRICE_CREATION.getInt() + ""));
        } else {
            e.getPlayer().sendMessage(Message.CHEST_PROTECT_SUCCESS.getMessage());
        }
    }
}