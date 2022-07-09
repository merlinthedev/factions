package me.merlin.utils;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Spawner implements CreatureSpawner {

    private CreatureType creatureType;
    private EntityType spawnedEntityType;
    private int delay;
    private int typeId;

    private byte lightLevel;
    private byte rawData;

    private Block block;
    private MaterialData materialData;
    private Material type;
    private World world;
    private Location location;

    @Override
    public CreatureType getCreatureType() {
        return creatureType;
    }

    @Override
    public EntityType getSpawnedType() {
        return spawnedEntityType;
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        spawnedEntityType = entityType;
    }

    @Override
    public void setCreatureType(CreatureType creatureType) {
        this.creatureType = creatureType;
    }

    @Override
    public String getCreatureTypeId() {
        return creatureType.toString();
    }

    @Override
    public void setCreatureTypeByName(String s) {
        CreatureType.valueOf(s.toUpperCase());
        creatureType = CreatureType.valueOf(s.toUpperCase());
    }

    @Override
    public String getCreatureTypeName() {
        return creatureType.getName();
    }

    @Override
    public void setCreatureTypeId(String s) {
        throw new NotImplementedException();
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int i) {
        delay = i;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public MaterialData getData() {
        return materialData;
    }

    @Override
    public Material getType() {
        return type;
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public byte getLightLevel() {
        return lightLevel;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getX() {
        return (int) location.getX();
    }

    @Override
    public int getY() {
        return (int) location.getY();
    }

    @Override
    public int getZ() {
        return (int) location.getZ();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Location getLocation(Location location) {
        throw new NotImplementedException();
    }

    @Override
    public Chunk getChunk() {
        return location.getChunk();
    }

    @Override
    public void setData(MaterialData materialData) {
        this.materialData = materialData;
    }

    @Override
    public void setType(Material material) {
        type = material;
    }

    @Override
    public boolean setTypeId(int i) {
        typeId = i;
        return true;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean update(boolean b) {
        return false;
    }

    @Override
    public boolean update(boolean b, boolean b1) {
        return false;
    }

    @Override
    public byte getRawData() {
        return rawData;
    }

    @Override
    public void setRawData(byte b) {
        rawData = b;
    }

    @Override
    public boolean isPlaced() {
        return false;
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return null;
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {

    }
}
