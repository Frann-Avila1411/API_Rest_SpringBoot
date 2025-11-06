package edu.sv.ues.dam235.apirestdemo.services;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    // Usamos un Set concurrente para una lista negra simple en memoria.

    private Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void addTokenToBlacklist(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
