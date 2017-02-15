/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Predicate;
import md.games.bomberman.bomb.BombBuilder;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.scenario.action.Action;
import md.games.bomberman.scenario.action.ActionReceiver;
import md.games.bomberman.scenario.action.ActionSender;
import md.games.bomberman.scenario.event.ScenarioEvent;
import md.games.bomberman.scenario.event.ScenarioEventManager;
import md.games.bomberman.script.Script;
import md.games.bomberman.script.ScriptId;
import md.games.bomberman.script.ScriptManager;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.CriteriaIterator;
import md.games.bomberman.util.RNG;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author mpasc
 */
public final class Scenario extends LPLObject
{
    private final SpriteManager spriteManager;
    private TileManager tiles;
    private DeathBorder dborder;
    private ScriptManager scripts;
    private final BombBuilder bombBuilder;
    private final LinkedList<Creature> creatures = new LinkedList<>();
    private final HashMap<UUID, GameObject> objectHash = new HashMap<>();
    private final ScenarioEventManager events = new ScenarioEventManager();
    private final Camera camera = new Camera();
    private final RNG rng = new RNG();
    private Script onInit;
    private ActionSender sender;
    private ActionReceiver receiver;
    
    public Scenario(SpriteManager spriteManager, int rows, int columns)
    {
        this.spriteManager = spriteManager;
        tiles = new TileManager(rows, columns);
        tiles.setScenarioReference(this);
        scripts = new ScriptManager();
        bombBuilder = new BombBuilder(spriteManager);
        dborder = null;
    }
    
    private Scenario(SpriteManager spriteManager)
    {
        this.spriteManager = spriteManager;
        bombBuilder = new BombBuilder(spriteManager);
    }
    
    public final void setActionReceiver(ActionReceiver receiver)
    {
        if(receiver == null)
            throw new NullPointerException();
        this.receiver = receiver;
    }
    public final ActionReceiver getActionReceiver() { return receiver; }
    
    public final void setActionSender(ActionSender sender)
    {
        if(sender == null)
            throw new NullPointerException();
        this.sender = sender;
    }
    public final ActionSender getActionSender() { return sender; }
    public final void sendAction(Action action)
    {
        if(sender != null)
            sender.sendAction(action);
    }
    
    public final Camera getCamera() { return camera; }
    
    public final void registerGameObject(GameObject go)
    {
        if(go == null)
            throw new NullPointerException();
        if(objectHash.containsKey(go.getId()))
            throw new IllegalArgumentException();
        go.setScenarioReference(this);
        objectHash.put(go.getId(),go);
    }
    
    public final void unregisterGameObject(GameObject go)
    {
        if(go == null)
            throw new NullPointerException();
        if(!go.hasScenarioReference())
            return;
        if(go.getScenarioReference() != this)
            throw new IllegalStateException();
        objectHash.remove(go.getId());
        go.setScenarioReference(null);
    }
    
    public final void unregisterGameObject(UUID id)
    {
        if(id == null)
            throw new NullPointerException();
        GameObject go = objectHash.remove(id);
        if(go == null)
            return;
        go.setScenarioReference(null);
    }
    
    public final void addCreature(Creature creature)
    {
        registerGameObject(creature);
        creatures.add(creature);
    }
    
    public final GameObject getGameObject(UUID id)
    {
        return objectHash.get(id);
    }
    
    
    public final void putCreatureInTile(int row, int column, Creature c)
    {
        if(!c.hasScenarioReference())
            throw new IllegalArgumentException();
        Tile tile = tiles.getTile(row,column);
        Vector2 position = tile.getPosition();
        position.add(c.getSizeWidth()/2,c.getSizeHeight()/2);
        c.setPosition(position);
    }
    
    public final void setCameraLimits()
    {
        Vector2 tsize = tiles.getSize();
        camera.setLimitedScope(tiles.getPosition(),tiles.getPosition().add(tsize));
    }
    
    public final void setOnInitScript(String script)
    {
        Script s = scripts.getScript(script);
        if(s == null)
            throw new IllegalArgumentException("Script \"" + script + "\" does not exists");
        onInit = s;
    }
    public final Script getOnInitScript() { return onInit; }
    public final void removeOnInitScript() { onInit = null; }
    
    public final void addEvent(ScenarioEvent event) { events.addEvent(event); }
    
    
    
    
    public final void draw(Graphics2D g)
    {
        AffineTransform oldTransform = g.getTransform();
        g.setTransform(camera.getAffineTransform());
        
        tiles.draw(g);
        for(Creature c : creatures)
        {
            if(c.canSee(camera))
                c.draw(g);
        }
        
        g.setTransform(oldTransform);
    }
    
    public final void update(double delta)
    {
        tiles.update(delta);
        updateCreatures(delta);
        events.dispatchEvents(this,delta);
    }
    
    private void updateCreatures(double delta)
    {
        ListIterator<Creature> it = creatures.listIterator();
        while(it.hasNext())
        {
            Creature c = it.next();
            if(c.isDestroid())
            {
                if(c.hasScenarioReference())
                    unregisterGameObject(c);
                it.remove();
                continue;
            }
            else if(!c.hasScenarioReference())
            {
                it.remove();
                continue;
            }
            c.update(delta);
            if(!c.isAlive())
            {
                c.die();
                c.executeScript(ScriptId.ON_DIE);
                if(!c.isDestroid())
                    c.destroy();
                if(c.hasScenarioReference())
                    unregisterGameObject(c);
                it.remove();
            }
        }
    }
    
    public final void reloadSprites()
    {
        for(GameObject go : objectHash.values())
            go.reloadSprites(spriteManager);
        tiles.reloadSprites(spriteManager);
    }
    
    public final Iterator<GameObject> findGameObjectsByTag(String tag)
    {
        return new CriteriaIterator<>(objectHash.values(),it -> {
            GameObject obj;
            while(it.hasNext())
            {
                obj = it.next();
                if(obj.getTag().equals(tag))
                    return obj;
            }
            return null;
        });
    }
    
    public final Iterator<GameObject> findGameObjectByType(int type)
    {
        return new CriteriaIterator<>(objectHash.values(),it -> {
            GameObject obj;
            while(it.hasNext())
            {
                obj = it.next();
                if(obj.getGameObjectType() == type)
                    return obj;
            }
            return null;
        });
    }
    
    public final Iterator<GameObject> findGameObjectByUserCriteria(Predicate<GameObject> checker)
    {
        return new CriteriaIterator<>(objectHash.values(),it -> {
            GameObject obj;
            while(it.hasNext())
            {
                obj = it.next();
                if(checker.test(obj))
                    return obj;
            }
            return null;
        });
    }
    
    public final Iterable<Creature> getCreaturesIterable() { return creatures; }
    
    public final SpriteManager getSpriteManager() { return spriteManager; }
    public final TileManager getTileManager() { return tiles; }
    public final DeathBorder getDeathBorder() { return dborder; }
    public final BombBuilder getBombBuilder() { return bombBuilder; }
    public final ScriptManager getScriptManager() { return scripts; }
    public final ScenarioEventManager getEventManager() { return events; }
    public final RNG getRNG() { return rng; }
    
    
    public static final void store(Scenario scenario, OutputStream out) throws IOException
    {
        if(scenario == null)
            throw new NullPointerException();
        GameDataSaver gds = new GameDataSaver(out);
        gds.writeSerializableObject(scenario.scripts);
        gds.writeInt(scenario.objectHash.size());
        for(GameObject go : scenario.objectHash.values())
            gds.writeSerializableObject(go);
        gds.writeSerializableObject(scenario.tiles);
        gds.writeInt(scenario.creatures.size());
        for(Creature c : scenario.creatures)
            gds.writeUUID(c.getId());
        gds.writeIfNonNull(scenario.onInit,() -> gds.writeScript(scenario.onInit));
    }
    public static final void store(Scenario scenario, File file) throws IOException
    {
        try(FileOutputStream fos = new FileOutputStream(file))
        {
            store(scenario,fos);
        }
    }
    
    public static final Scenario load(SpriteManager sprites, InputStream in) throws IOException
    {
        GameDataLoader gdl = new GameDataLoader(in,sprites);
        Scenario scenario = new Scenario(sprites);
        gdl.setScenarioReference(scenario);
        scenario.scripts = gdl.readSerializableObject();
        gdl.setScriptManager(scenario.scripts);
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
        {
            GameObject go = gdl.readSerializableObject();
            scenario.objectHash.put(go.getId(),go);
        }
        scenario.tiles = gdl.readSerializableObject();
        len = gdl.readInt();
        for(int i=0;i<len;i++)
        {
            GameObject go = scenario.objectHash.get(gdl.readUUID());
            scenario.creatures.add((Creature) go);
        }
        gdl.readIfNonNull(() -> scenario.onInit = gdl.readScript());
        return scenario;
    }
    public static final Scenario load(SpriteManager sprites, File file) throws IOException
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return load(sprites,fis);
        }
    }
    
    
    
    
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "addCreature": return ADD_CREATURE;
            case "findObjectByTag": return FIND_OBJECT_BY_TAG;
            case "findObjectByUserCriteria": return FIND_OBJECT_BY_USER_CRITERIA;
            case "getObjectByTag": return GET_OBJECT_BY_TAG;
            case "getObjectByUserCriteria": return GET_OBJECT_BY_USER_CRITERIA;
            case "getTile": return GET_TILE;
            case "getTileFromPosition": return GET_TILE_FROM_POSITION;
        }
    }
    
    
    private static final LPLValue ADD_CREATURE = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<Scenario>toLPLObject().addCreature(arg1.toLPLObject());
    });
    private static final LPLValue FIND_OBJECT_BY_TAG = LPLFunction.createFunction((arg0, arg1) -> {
        return valueOf(arg0.<Scenario>toLPLObject().findGameObjectsByTag(arg1.toString()));
    });
    private static final LPLValue FIND_OBJECT_BY_USER_CRITERIA = LPLFunction.createFunction((arg0, arg1) -> {
        return valueOf(arg0.<Scenario>toLPLObject().findGameObjectByUserCriteria(c -> arg1.callLimited(c).toJavaBoolean()));
    });
    private static final LPLValue GET_OBJECT_BY_USER_CRITERIA = LPLFunction.createFunction((arg0, arg1, arg2) -> {
        int n = arg2 == UNDEFINED ? 1 : arg2.toJavaInt();
        int count = 0;
        GameObject first = null;
        Iterator<GameObject> it = arg0.<Scenario>toLPLObject().findGameObjectByUserCriteria(c -> arg1.callLimited(c).toJavaBoolean());
        while(it.hasNext())
        {
            GameObject go = it.next();
            if(count == 0)
                first = go;
            if(++count == n)
                return go;
        }
        return first == null ? NULL : first;
    });
    private static final LPLValue GET_OBJECT_BY_TAG = LPLFunction.createFunction((arg0, arg1, arg2) -> {
        int n = arg2 == UNDEFINED ? 1 : arg2.toJavaInt();
        int count = 0;
        GameObject first = null;
        Iterator<GameObject> it = arg0.<Scenario>toLPLObject().findGameObjectsByTag(arg1.toString());
        while(it.hasNext())
        {
            GameObject go = it.next();
            if(count == 0)
                first = go;
            if(++count == n)
                return go;
        }
        return first == null ? NULL : first;
    });
    private static final LPLValue GET_TILE = LPLFunction.createFunction((arg0, arg1, arg2) -> {
        int row = arg1.toJavaInt();
        int column = arg2.toJavaInt();
        Tile tile = arg0.<Scenario>toLPLObject().tiles.getTile(row,column);
        return tile == null ? NULL : tile;
    });
    private static final LPLValue GET_TILE_FROM_POSITION = LPLFunction.createFunction((arg0, arg1) -> {
        Tile tile = arg0.<Scenario>toLPLObject().tiles.getTileByPosition(arg1.toLPLObject());
        return tile == null ? NULL : tile;
    });
}
