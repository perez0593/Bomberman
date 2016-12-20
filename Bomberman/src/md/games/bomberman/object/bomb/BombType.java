/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.bomb;

/**
 *
 * @author David
 */
public enum BombType {
    NORMAL, // Bomba normal
    SPIKES, // Larga distancia
    THROWABLE, // Bomba lanzable
    FASTEXPOLDE, // Bomba de poco tiempo
    C4, // Bomba con control remoto
    HIGHRANGE, // Bomba con rango cuadrado, no cruz
    NINJA, // Bomba invisible Normal
    MINE, // Bomba invisible que solo explota cuando la pisas
    HEAL, // Bomba que te da una vida si te toca
    ICEBOMB, // Bomba que te paraliza si te toca
    TELEPORT; // Bomba que se teletransporta a otro punto del mapa aleatorio y explota
    
}
