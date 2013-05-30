
package serwer;

import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.util.Timer;
import java.util.TimerTask;

import serwer.Network.*;

public class Serwer {
    
    
    
        przycisk2 Przycisk;
	public Server server;
	HashSet<Character> loggedIn = new HashSet();
        Timer zegar;
	public Serwer (przycisk2 a) throws IOException {
                Przycisk = a;
		server = new Server() {
                        @Override
			protected Connection newConnection () {
				return new CharacterConnection();
			}
		};
		Network.register(server);

		server.addListener(new Listener() {
                        @Override
			public void received (Connection c, Object object) {
				CharacterConnection connection = (CharacterConnection)c;
				Character character = connection.character;
                                System.out.println(connection.getRemoteAddressTCP());
				if (object instanceof Register) {
                                    
					if (character != null) return;

					Register register = (Register)object;

					if (!isValid(register.name)) {
						c.close();
						return;
					}
                                        // sprawdzenie czy juz jest zalogowana postac o takim samym nicku
                                        for (Character other : loggedIn) {
						if (other.name.equals(register.name)) {
							c.close();
							return;
						}
					}
					
					character = new Character();
					character.name = register.name;
					character.x = 320;
					character.y = 500;
                                        character.image = register.image;
                                        character.id = newID();
                                        character.hp = 5;
                                        character.frags = 0;
                                        character.dead = 0;
                                        character.immortal = false;
                                        CharacterID id = new CharacterID();
                                        id.id = character.id;
                                        if(loggedIn.size() == 0) {
                                            character.admin = true;
                                        }
                                        else character.admin = false;
                                        id.admin = character.admin;
                                        c.sendTCP(id);
					Przycisk.log.setText(Przycisk.log.getText()+"\n"+character.name+" dołączył do gry.");
                                        loggedIn(connection, character);
					return;
				}

				if (object instanceof MoveCharacter) {
                                    
					if (character == null) return;

					MoveCharacter msg = (MoveCharacter)object;

                                        character.x += msg.x;
					character.y += msg.y;
                                        character.a = msg.a;
                                        character.b = msg.b;
                                        character.attack = msg.attack;
                                     
					UpdateCharacter update = new UpdateCharacter();
					update.id = character.id;
					update.x = character.x;
					update.y = character.y;
                                        update.a = character.a;
                                        update.b = character.b;
                                        update.dead = character.dead;
                                        update.frags = character.frags;
                                        update.attack = character.attack;
                                        
					server.sendToAllUDP(update);
					return;
				}
                                if (object instanceof SendChat){
                                    
                                        SendChat info = (SendChat)object;
                                        server.sendToAllTCP(info);
                                        Przycisk.log.setText(Przycisk.log.getText()+"\n"+info.napis);
                                        return;
                                }
                                if (object instanceof SetMap){
                                        
                                        SetMap info = (SetMap)object;
                                       
                                        server.sendToAllTCP(info);
                                        return;
                                }
                                if (object instanceof Dead){
                                        Dead dead = (Dead) object;
                                        
                                        for (Character ch : loggedIn) {
                                            if(ch.id == dead.characterID) {
                                                ch.frags++;
                                                break;
                                            }
                                              
                                        }
                                        
                                        character.x = 320;
					character.y = 500;
                                        character.hp = 5;
                                        character.dead ++;
                                        NewPosition msg = new NewPosition();
                                        msg.x = character.x;
                                        msg.y = character.y;
                                        msg.hp = character.hp;
                                        c.sendTCP(msg);
                                        
                                }
                                if (object instanceof Kick){
                                        
                                    server.sendToAllTCP((Kick) object);
                                }
                                
                                if (object instanceof Combat){
                                        
                                        //Combat info = (Combat)object;
                                        
                                        //character.attack = info.attack;
                                }
			}
                        
                        
                       
                        
                        @Override
                        /**
                         * Rozłączenie klienta z serwerem. Usunięcie rozłączonej postaci.
                         */
			public void disconnected (Connection c) {
				CharacterConnection connection = (CharacterConnection)c;
				if (connection.character != null) {
					loggedIn.remove(connection.character);
                                        
					RemoveCharacter removeCharacter = new RemoveCharacter();
					removeCharacter.id = connection.character.id;
					server.sendToAllTCP(removeCharacter);
				}
			}
		});
		server.bind(Network.port,Network.port+2);
		server.start();
	}
        
         /**
         * sprawdzenie poprawnosci stringa
         */
        private boolean isValid (String value) {
                if (value == null) return false;
                value = value.trim();
                if (value.length() == 0) return false;
                return true;
        }
        /**
         * Szuka najwiekszego ID
         */
        private int newID(){
            int najwiekszy=0;
            for (Character character : loggedIn) {
                
			if(character.id>najwiekszy) najwiekszy = character.id;
		}
            
            
            return najwiekszy+1;
        }
        
        
        /**
         * Zalogowanie postaci. Dodanie aktualnych postaci do nowego klienta.
         * Dodanie nowej postaci do istniejących klientów.
         * @param c
         * @param character 
         */
        
	void loggedIn (CharacterConnection c, Character character) {
		c.character = character;

		
		for (Character other : loggedIn) {
			AddCharacter addCharacter = new AddCharacter();
			addCharacter.character = other;
			c.sendTCP(addCharacter);
		}

		loggedIn.add(character);
		AddCharacter addCharacter = new AddCharacter();
		addCharacter.character = character;
		server.sendToAllTCP(addCharacter);
	}
	static class CharacterConnection extends Connection {
		public Character character;
	}
        
}
