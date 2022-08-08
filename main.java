package mrfix1033.rounded_square;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main extends JavaPlugin implements CommandExecutor, Listener {

    private double radius = 5;
    private double gran = Math.sqrt(82);
    private double g = 4;
    private Byte[] f = new Byte[] {};
    private Byte[] p = new Byte[] {};

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        List<Byte> f = new ArrayList<>();
        List<Byte> p = new ArrayList<>();
        for (int i : Arrays.asList(-1, 0, 0, -1, 1, 0, 0, 1))
            f.add((byte) i);
        for (int i : Arrays.asList(1, 1, -1, 1, -1, -1, 1, -1))
            p.add((byte) i);
        this.f = f.toArray(new Byte[] {});
        this.p = p.toArray(new Byte[] {});
    }

    @EventHandler
    public void c(PlayerInteractEvent e) {
        Location locPlayer = e.getPlayer().getLocation();
        Location loc = locPlayer.clone().add(5, 0, 5);
        loc.setPitch(0);
        double t = 25 / radius;
        for (int i = 0; i < 4; i++) {
            double[] args = new double[] {t * f[i * 2], t * f[i * 2 + 1]};
            double[] argsTwo = new double[] {g * p[i * 2], g * p[i * 2 + 1]};
            for (int o = 0; o < radius * 2; o += t) {
                Location endLoc = loc.add(args[0], 0, args[1]).clone();
                Vector vec = new Vector();
                if (loc.distance(locPlayer) > gran) {
                    endLoc = locPlayer.clone().add(argsTwo[0], 0, argsTwo[1]);
                    vec = loc.clone().subtract(endLoc).getDirection();
                    endLoc.add(vec.multiply(radius - g));
                }
                endLoc.setDirection(vec);
                ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(endLoc, EntityType.ARMOR_STAND);
                a.setHelmet(new ItemStack(Material.WOOD));
                a.setGravity(false);
                EntityArmorStand b = ((CraftArmorStand) a).getHandle();
                b.setInvisible(true);
                a.setCollidable(true);
                Bukkit.getScheduler().runTaskLater(this, a::remove, 600);
            }
        }
        for (int i = 0; i < 360; i += 25 / radius) {
            loc.setYaw(i);
            ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(
                    loc.clone().add(loc.getDirection().multiply(radius)), EntityType.ARMOR_STAND);
            a.setHelmet(new ItemStack(Material.WOOD));
            a.setGravity(false);
            EntityArmorStand b = ((CraftArmorStand) a).getHandle();
            b.setInvisible(true);
            a.setCollidable(true);
            Bukkit.getScheduler().runTaskLater(this, a::remove, 600);
        }
    }

    @EventHandler
    public void d(AsyncPlayerChatEvent e) {
        try {
            radius = Double.parseDouble(e.getMessage());
        } catch (NumberFormatException ignored) {
            if (e.getMessage().startsWith("a"))
                try {
                    g = Double.parseDouble(e.getMessage().substring(1));
                    gran = Math.sqrt(Math.pow(g, 2) + radius * radius);
                } catch (NumberFormatException ignored1) {}
        }
    }

    @EventHandler
    public void a(PlayerDropItemEvent e) {
        e.getItemDrop().setPickupDelay(300);
        e.getItemDrop().addPassenger(e.getPlayer());
    }

    @EventHandler
    public void b(VehicleExitEvent e) {
        Bukkit.getPlayer("mrfix1033").sendMessage(e.getVehicle().getType().toString());
        if (e.getVehicle().getType() == EntityType.DROPPED_ITEM)
            e.getVehicle().addPassenger(e.getExited());
    }
}
