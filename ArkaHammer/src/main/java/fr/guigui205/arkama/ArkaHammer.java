package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;


public class ArkaHammer extends JavaPlugin {
    private static ItemStack item(Material m,String str){
        ItemStack it = new ItemStack(m);
        ItemMeta im = it.getItemMeta();
        im.setLore(Arrays.asList("§eArkaHammer",""));
        im.setDisplayName("Marteau en "+str);
        it.setItemMeta(im);
        return it;
    }
    @Override
    public void onEnable(){
        ItemStack SH = item(Material.STONE_PICKAXE,"pierre");
        Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this,"stonehammer"),SH).shape(
                "III",
                "ISI",
                "XSX"
        )       .setIngredient('X',Material.AIR)
                .setIngredient('I',Material.COBBLESTONE)
                .setIngredient('S',Material.STICK));

        ItemStack IH = item(Material.IRON_PICKAXE,"fer");
        Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this,"ironhammer"),IH).shape(
                "III",
                "ISI",
                "XSX"
        )       .setIngredient('X',Material.AIR)
                .setIngredient('I',Material.IRON_INGOT)
                .setIngredient('S',Material.STICK));

        ItemStack GH = item(Material.GOLDEN_PICKAXE,"or");
        Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this,"goldhammer"),GH).shape(
                "III",
                "ISI",
                "XSX"
        )       .setIngredient('X',Material.AIR)
                .setIngredient('I',Material.GOLD_INGOT)
                .setIngredient('S',Material.STICK));

        ItemStack DH = item(Material.DIAMOND_PICKAXE,"diamant");
        Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this,"diamondhammer"),DH).shape(
                "III",
                "ISI",
                "XSX"
        )       .setIngredient('X',Material.AIR)
                .setIngredient('I',Material.DIAMOND)
                .setIngredient('S',Material.STICK));

        ItemStack NH = item(Material.NETHERITE_PICKAXE,"netherite");
        Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this,"netheritehammer"),NH).shape(
                "III",
                "ISI",
                "XSX"
        ).setIngredient('X', Material.AIR)
                .setIngredient('I', Material.NETHERITE_INGOT)
                .setIngredient('S', Material.STICK));
        getServer().getPluginManager().registerEvents(new HammerEvent(), this);
        getLogger().warning("[ArkaHammer] chargé");
    }
}

class HammerEvent implements Listener {

    private static void Break(Player p, int x, int y, int z,Block b,Material type) {
        Block dir = p.getWorld().getBlockAt(x,y,z);
        if (b.getType().equals(Material.DIRT) || b.getType().equals(Material.GRAVEL) || b.getType().equals(Material.SAND) || b.getType().equals(Material.GRASS_BLOCK)){
            if (dir.getType().equals(Material.DIRT) || dir.getType().equals(Material.GRAVEL) || dir.getType().equals(Material.SAND) || dir.getType().equals(Material.GRASS_BLOCK)) {
                if (p.getGameMode().equals(GameMode.CREATIVE)) {
                    dir.setType(Material.AIR);
                } else {
                    dir.breakNaturally(p.getInventory().getItemInMainHand());
                }
            } 
        } else {
            if (!(dir.getType().equals(Material.DIRT)) && !(dir.getType().equals(Material.GRAVEL)) && !(dir.getType().equals(Material.SAND)) && !(dir.getType().equals(Material.GRASS_BLOCK))){
                if (type.equals(Material.STONE_PICKAXE)){
                    if (!(dir.getType().equals(Material.IRON_ORE)) && dir.getDrops(new ItemStack(type)).size() != 0) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                            dir.setType(Material.AIR);
                        } else {
                            p.getWorld().getBlockAt(x, y, z).breakNaturally(p.getInventory().getItemInMainHand());
                        }
                    }
                } else if (type.equals(Material.IRON_PICKAXE)){
                    if (!(dir.getType().equals(Material.DIAMOND_ORE)) && dir.getDrops(new ItemStack(type)).size() != 0) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                            dir.setType(Material.AIR);
                        } else {
                            p.getWorld().getBlockAt(x, y, z).breakNaturally(p.getInventory().getItemInMainHand());
                        }
                    }
                } else if (type.equals(Material.DIAMOND_PICKAXE)){
                    if (b.getType().equals(Material.OBSIDIAN)) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                            dir.setType(Material.AIR);
                        } else {
                            p.getWorld().getBlockAt(x, y, z).breakNaturally(p.getInventory().getItemInMainHand());
                        }
                    }

                else if (!(dir.getType().equals(Material.OBSIDIAN)) && dir.getDrops(new ItemStack(type)).size() != 0) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                            dir.setType(Material.AIR);
                        } else {
                            p.getWorld().getBlockAt(x, y, z).breakNaturally(p.getInventory().getItemInMainHand());
                        }
                    }
                } else {
                    if (p.getGameMode().equals(GameMode.CREATIVE)) {
                        dir.setType(Material.AIR);
                    } else {
                        p.getWorld().getBlockAt(x, y, z).breakNaturally(p.getInventory().getItemInMainHand());
                    }
                }
            }
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("§eArkaHammer")) {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            BlockFace bf = player.getTargetBlockFace(3);
            if (bf == null) {
                return; //what in the fucking hell
            }

            bf = bf.getOppositeFace();
            int modX = Math.abs(bf.getModX());
            int modY = Math.abs(bf.getModY());
            int modZ = Math.abs(bf.getModZ());
            if (modY == 0) {
                //ni en haut ni en bas
                Break(player, x + modZ, y + 1, z + modX,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x + modZ, y, z + modX,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x + modZ, y - 1, z + modX,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x, y + 1, z,block,player.getInventory().getItemInMainHand().getType());
                //Break(player,x,y,z);
                Break(player, x, y - 1, z,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - modZ, y + 1, z - modX,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - modZ, y, z - modX,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - modZ, y - 1, z - modX,block,player.getInventory().getItemInMainHand().getType());
                //  11 10 1-1
                //  01 00 0-1
                //  -11 -10 -1-1
            } else {
                //haut ou bas
                Break(player, x + 1, y, z + 1,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x + 1, y, z,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x + 1, y, z - 1,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x, y, z + 1,block,player.getInventory().getItemInMainHand().getType());
                //Break(player,x,y,z);
                Break(player, x, y, z - 1,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - 1, y, z + 1,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - 1, y, z,block,player.getInventory().getItemInMainHand().getType());
                Break(player, x - 1, y, z - 1,block,player.getInventory().getItemInMainHand().getType());

            }
        }
    }
}
