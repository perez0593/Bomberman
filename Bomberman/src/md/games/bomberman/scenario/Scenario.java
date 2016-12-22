/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
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
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.object.GameObject;
import md.games.bomberman.util.CriteriaIterator;

/**
 *
 * @author mpasc
 */
public final class Scenario
{
    private TileManager tiles;
    private DeathBorder dborder;
    private final LinkedList<GameObject> objects = new LinkedList<>();
    private final HashMap<UUID, GameObject> objectHash = new HashMap<>();
    
    private Scenario(int rows, int columns)
    {
        tiles = new TileManager(rows, columns);
        tiles.setScenarioReference(this);
        dborder = null;
    }
    
    private Scenario() {}
    
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
    
    public final void addGameObject(GameObject go)
    {
        registerGameObject(go);
        objects.add(go);
    }
    
    public final GameObject getGameObject(UUID id)
    {
        return objectHash.get(id);
    }
    
    
    
    public final void draw(Graphics2D g)
    {
        tiles.draw(g);
        for(GameObject go : objects)
            go.draw(g);
    }
    
    public final void update(double delta)
    {
        tiles.update(delta);
        updateGameObjects(delta);
        
    }
    
    private void updateGameObjects(double delta)
    {
        ListIterator<GameObject> it = objects.listIterator();
        while(it.hasNext())
        {
            GameObject go = it.next();
            if(go.isDestroid())
            {
                if(go.hasScenarioReference())
                    unregisterGameObject(go);
                it.remove();
                continue;
            }
            else if(!go.hasScenarioReference())
            {
                it.remove();
                continue;
            }
            go.update(delta);
        }
    }
            
            
    
    public final Iterator<GameObject> findGameObjectsByTag(String tag)
    {
        return new CriteriaIterator<>(objects,it -> {
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
        return new CriteriaIterator<>(objects,it -> {
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
        return new CriteriaIterator<>(objects,it -> {
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
    
    public final TileManager getTileManager() { return tiles; }
    public final DeathBorder getDeathBorder() { return dborder; }
    
    
    public static final void store(Scenario scenario, OutputStream out) throws IOException
    {
        if(scenario == null)
            throw new NullPointerException();
        GameDataSaver gds = new GameDataSaver(out);
        gds.writeSerializableObject(scenario.tiles);
        gds.writeInt(scenario.objects.size());
        for(GameObject go : scenario.objects)
            gds.writeSerializableObject(go);
    }
    public static final void store(Scenario scenario, File file) throws IOException
    {
        try(FileOutputStream fos = new FileOutputStream(file))
        {
            store(scenario,fos);
        }
    }
    
    public static final Scenario load(ScenarioTheme theme, InputStream in) throws IOException
    {
        GameDataLoader gdl = new GameDataLoader(in,theme);
        Scenario scenario = new Scenario();
        scenario.tiles = gdl.readSerializableObject();
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
        {
            GameObject go = gdl.readSerializableObject();
            scenario.addGameObject(go);
        }
        return scenario;
    }
    public static final Scenario load(ScenarioTheme theme, File file) throws IOException
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return load(theme,fis);
        }
    }
}
