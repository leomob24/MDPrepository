package it.unicam.cs.mpgc.rpg125579.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Villain {
    private String name;
    private int atk;
    private int hp;
    private float damageMultiplier;
    private int bonusAtk;
    private static final String[] villainNames = {"Doctor Bad Mood", "Galactose Intolerant", "Doc Hawk", "Low-Key", "Choko Kraven", "Rhinoplasty", "K.O.M.O.D.O.", "Bronze Skater"};

    public static Villain generateVillain(){
        Random random = new Random();
        String name = villainNames[random.nextInt(villainNames.length)];
        int atk = random.nextInt(20) + 30;
        int hp = random.nextInt(100) + 100;
        float damageMultiplier = ((random.nextInt(10)+11)/10.0f);
        int bonusAtk = random.nextInt(5) + 1;
        return Villain.builder()
                .name(name)
                .atk(atk)
                .hp(hp)
                .damageMultiplier(damageMultiplier)
                .bonusAtk(bonusAtk)
                .build();
    }
}
