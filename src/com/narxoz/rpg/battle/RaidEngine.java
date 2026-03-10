package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        RaidResult result = new RaidResult();

        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setWinner("Error: Validation Failed");
            return result;
        }

        int rounds = 0;
        result.addLine("=== RAID START: " + teamA.getName() + " VS " + teamB.getName() + " ===");

        while (teamA.isAlive() && teamB.isAlive() && rounds < 100) {
            rounds++;
            result.addLine("\n--- Round " + rounds + " ---");

            result.addLine(teamA.getName() + " uses " + teamASkill.getSkillName() + " (" + teamASkill.getEffectName() + ") on " + teamB.getName() + "!");
            teamASkill.cast(teamB);
            result.addLine(" -> " + teamB.getName() + " remaining HP: " + teamB.getHealth());

            if (!teamB.isAlive()) break;

            boolean isCritical = random.nextInt(100) < 20;
            String critText = isCritical ? " [CRITICAL STRIKE!]" : "";

            result.addLine(teamB.getName() + " uses " + teamBSkill.getSkillName() + " (" + teamBSkill.getEffectName() + ") on " + teamA.getName() + "!" + critText);

            teamBSkill.cast(teamA);
            if (isCritical) teamBSkill.cast(teamA);

            result.addLine(" -> " + teamA.getName() + " remaining HP: " + teamA.getHealth());
        }

        result.setRounds(rounds);
        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
        } else if (!teamA.isAlive() && teamB.isAlive()) {
            result.setWinner(teamB.getName());
        } else {
            result.setWinner("Draw");
        }

        return result;
    }
}
