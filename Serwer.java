
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
    
	private Server server;
	HashSet<Character> loggedIn = new HashSet();
        Timer zegar;
	public Serwer () throws IOException {
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
                                        character.id=loggedIn.size()+1;
                                        CharacterID id = new CharacterID();
                                        id.id = character.id;
                                        if(loggedIn.size() == 0) {
                                            character.admin = true;
                                        }
                                        else character.admin = false;
                                        id.admin = character.admin;
                                        c.sendTCP(id);
					
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
                                        
                                      //  if(msg.x>0) character.right_move = true;
                                        //else if (msg.x<0) character.right_move = false;
                                        
                                       // if(msg.x == 0) character.move = false;
                                       // else character.move = true;
                                        
					UpdateCharacter update = new UpdateCharacter();
					update.id = character.id;
					update.x = character.x;
					update.y = character.y;
                                        update.a = character.a;
                                        update.b = character.b;
                                        //update.attack = character.attack;
                                        
					server.sendToAllUDP(update);
					return;
				}
                                if (object instanceof SendChat){
                                    
                                        SendChat info = (SendChat)object;
                                        server.sendToAllTCP(info);
                                        return;
                                }
                                if (object instanceof SetMap){
                                        
                                        SetMap info = (SetMap)object;
                                       
                                        server.sendToAllTCP(info);
                                        return;
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
